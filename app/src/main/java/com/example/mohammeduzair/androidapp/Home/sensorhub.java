package com.example.mohammeduzair.androidapp.Home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohammeduzair.androidapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Objects;

public class sensorhub extends AppCompatActivity{
    private TextView sensor1txt;
    private TextView sensor2txt;
    private int counterValue = 5;
    private int counterValue1 = 0;
    private TextView sensor3txt;
    private int counterValue2 ;
    private boolean in = true;
    private boolean out = false;
    private int dbMealEaten;

    private TextView mealtxt;
    private Button rstBtn;
    AlertDialog.Builder builder;
    EditText inputValue, changeInput;
    LinearLayout AlertDialogLayout;
    LinearLayout.LayoutParams layoutParams;
    SharedPreferences.Editor editor;
    SharedPreferences newPrefs;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensors);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
//        CardView inputNewValue2 = (CardView) findViewById(R.id.inputsetting2);
//        sensor1txt = (TextView) findViewById(R.id.countertext);
//        sensor2txt = (TextView) findViewById(R.id.countertext1);
//        sensor3txt = (TextView) findViewById(R.id.countertext2);
        mealtxt = (TextView) findViewById(R.id.mealcount);
        rstBtn = (Button) findViewById(R.id.reset);
        mAuth = FirebaseAuth.getInstance();
        builder = new AlertDialog.Builder(this);
        inputValue=new EditText(this);
//        inputNewValue2.setOnClickListener(this);
        user = mAuth.getCurrentUser();
        databaseReference.child(user.getUid()).child("Meals").addValueEventListener(mealListener);

        newPrefs = getSharedPreferences("current", Context.MODE_PRIVATE);
        editor = newPrefs.edit();

        counterValue2 = newPrefs.getInt("counter",10);
//        sensor3txt.setText(String.valueOf(counterValue2));

        rstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetInfo();
                dbMealEaten = 0;
                mealtxt.setText(String.valueOf(dbMealEaten));
                databaseReference.child(user.getUid()).child("Meals").setValue(dbMealEaten);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // add functionality to the back button to send back to home page
                Intent intent = new Intent(sensorhub.this, Homepage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void plusBtnClick(View view) {
        if(counterValue < 5 ) {
            counterValue+=1;
        }
        counterValue1 = 5 - counterValue;

        //increment value by 1
        //convert value into a String to be printed in textfield
        sensor1txt.setText(String.valueOf(counterValue));
        sensor2txt.setText(String.valueOf(counterValue1));
        petLoc();
    }

    public void minusBtnClick(View view) {
        if(counterValue == 0)
            return;
        else {
            counterValue-=1;
            counterValue1 = 5 - counterValue;
        }

        //decrement value by 1
        //convert value into a String to be printed in textfield
        sensor1txt.setText(String.valueOf(counterValue));
        sensor2txt.setText(String.valueOf(counterValue1));
        petLoc();
    }

    //*********SENSOR 2 TEXT STUFF*********//

    public void plusBtnClick1(View view) {
        if(counterValue1 < 5 ) {
            counterValue1+=1;
        }
        counterValue = 5 - counterValue1;

        //increment value by 1
        //convert value into a String to be printed in textfield
        sensor1txt.setText(String.valueOf(counterValue));
        sensor2txt.setText(String.valueOf(counterValue1));
        petLoc();
    }

    public void minusBtnClick1(View view) {
        if(counterValue1 == 0)
            return;
        else {
            counterValue1-=1;
            counterValue = 5 - counterValue1;
        }

        //decrement value by 1
        //convert value into a String to be printed in textfield
        sensor1txt.setText(String.valueOf(counterValue));
        sensor2txt.setText(String.valueOf(counterValue1));
        petLoc();
    }

    //**Add if else statement, regarding value reaching 0, when value is 0 then add 1 to a seperate integer counter**//
    public void petLoc(){
        if (counterValue == 5){
            in=true;
            out=false;

            Toast.makeText(this, "Entering...", Toast.LENGTH_SHORT).show();


            String inside = "Inside";
            databaseReference.child(user.getUid()).child("Position").setValue(inside);
            databaseReference.child(user.getUid()).child("Inside").setValue(in);
            databaseReference.child(user.getUid()).child("Outside").setValue(out);
        }
        else if(counterValue1 == 5) {
            out=true;
            in=false;

            Toast.makeText(this, "Exiting...", Toast.LENGTH_SHORT).show();


            String outside = "Outside";
            databaseReference.child(user.getUid()).child("Position").setValue(outside);
            databaseReference.child(user.getUid()).child("Inside").setValue(in);
            databaseReference.child(user.getUid()).child("Outside").setValue(out);
        }

    }


    ValueEventListener mealListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            dbMealEaten = dataSnapshot.getValue(Integer.class);
            mealtxt.setText(Integer.toString(dbMealEaten));
            Log.d("dbREAD", "meal value: " + dbMealEaten);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w("db", "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };


    private void resetInfo() {
        Toast.makeText(this, "Resetting...", Toast.LENGTH_SHORT).show();

    }

    //****************SENSOR 3 TEXT STUFF*******************//
    public void createAlert1(){
        builder = new AlertDialog.Builder(this);

        AlertDialogLayout = new LinearLayout(this);
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        AlertDialogLayout.setOrientation(LinearLayout.VERTICAL);
        AlertDialogLayout.setLayoutParams(layoutParams);


        AlertDialogLayout.setGravity(Gravity.CENTER);

        changeInput = new EditText(this);
        changeInput.setHint("Example: 10");
        changeInput.setInputType(InputType.TYPE_CLASS_DATETIME);


        AlertDialogLayout.addView(changeInput, new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                .WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        builder.setView(AlertDialogLayout);
        builder.setCancelable(true);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (changeInput.length() > 0) {
                    counterValue2 = Integer.parseInt(changeInput.getText().toString());
                    sensor3txt.setText((String.valueOf(counterValue2)));

                } else {
                    changeInput.setError("Enter valid number");
                }
            }
        });
        builder.setIcon(R.drawable.ic_info_outline_black_24dp);
        builder.setTitle("Input a value:");

        AlertDialog alertDialog = builder.create();

        try {
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void minusBtnClick2(View view) {
        if(in && counterValue2 >0){
            counterValue2-=1;
        }
        else if(out) {
            Toast.makeText(this, "Pet can not eat outside...", Toast.LENGTH_SHORT).show();
        }
        if(counterValue2 == 1) {
            dbMealEaten++;
            mealtxt.setText(String.valueOf(dbMealEaten));
            databaseReference.child(user.getUid()).child("Meals").setValue(dbMealEaten);

        }
        sensor3txt.setText(String.valueOf(counterValue2));
        editor.putInt("counter", counterValue2);
        editor.commit();
    }

//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.inputsetting2:
//                createAlert1();
//                break;
//            default:
//                break;
//        }
//
//    }


}
