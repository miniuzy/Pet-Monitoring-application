package com.example.mohammeduzair.androidapp.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohammeduzair.androidapp.R;
import com.facebook.share.Share;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class locksys extends AppCompatActivity {

    private boolean inside;
    private boolean outside;
    private TextView petloctxt;
    private TextView doorPos;
    private boolean locked;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser user;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        petloctxt = (TextView) findViewById(R.id.ruletxt);
        doorPos = (TextView) findViewById(R.id.lockpos);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        databaseReference.child(user.getUid()).child("Inside").addValueEventListener(locListener);
        databaseReference.child(user.getUid()).child("Outside").addValueEventListener(locListener);
        databaseReference.child(user.getUid()).child("Door").addValueEventListener(locListener);
        databaseReference.child(user.getUid()).child("Inside").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                inside = dataSnapshot.getValue(Boolean.class);
                Log.d("dbbREAD", "first inside read: " + inside);
                if(inside) {
                    petloctxt.setText("Pet is: inside");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        databaseReference.child(user.getUid()).child("Outside").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                outside = dataSnapshot.getValue(Boolean.class);
                Log.d("dbbREAD", "first outside read: " + outside);
                if(outside) {
                    petloctxt.setText("Pet is: outside");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        databaseReference.child(user.getUid()).child("Door").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                locked = dataSnapshot.getValue(Boolean.class);
                if(locked) {
                    doorPos.setText("Door is: Locked");
                }
                else {
                    doorPos.setText("Door is: Unlocked");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // add functionality to the back button to send back to home page
                Intent intent = new Intent(locksys.this, Homepage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void unlockBtn (View view) {
        if(outside && locked) {
            Toast.makeText(this, "Unlocking...", Toast.LENGTH_SHORT).show();
            doorPos.setText("Door is: Unlocked");
            locked = false;
            databaseReference.child(user.getUid()).child("Door").setValue(locked);
        }
        else if(inside && locked) {
            Toast.makeText(this, "Unlocking...", Toast.LENGTH_SHORT).show();
            doorPos.setText("Door is: Unlocked");
            locked = false;
            databaseReference.child(user.getUid()).child("Door").setValue(locked);

        }
        else {
            return;
        }
    }

    public void lockBtn (View view) {
        if(inside && !locked) {
            Toast.makeText(this, "Locking...", Toast.LENGTH_SHORT).show();
            doorPos.setText("Door is: Locked");
            locked = true;
            databaseReference.child(user.getUid()).child("Door").setValue(locked);

        }
        else if (inside && locked) {
            return ;
        }
        else {
            Toast.makeText(this, "Pet outside. Unable to lock...", Toast.LENGTH_SHORT).show();
            locked = false;
            databaseReference.child(user.getUid()).child("Door").setValue(locked);
        }

    }

    ValueEventListener locListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Log.d("dbREAD", "i get called value: " + dataSnapshot.toString() );
            String key = dataSnapshot.getKey();
            if(key == "Inside") {
                inside = dataSnapshot.getValue(Boolean.class);
            }
            else if( key == "Outside") {
                outside = dataSnapshot.getValue(Boolean.class);
            }
            else if ( key == "Door") {
                locked = dataSnapshot.getValue(Boolean.class);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w("db", "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };

    //When you press sync image it will move between rules
    //then whatever rule it is on will be presented on ruletxt
    //then if you try to lock and unlock it should or shouldnt work

}
