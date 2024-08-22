package com.example.mightworthit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mightworthit.Cards.Cards;
import com.example.mightworthit.Cards.MyArrayAdapter;
import com.example.mightworthit.Matches.MatchesActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Cards cards_data[];
    private MyArrayAdapter arrayAdapter;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener AuthStateListener;
    private String currentUID;
    private DatabaseReference usersDb;
    ListView listView;
    List<Cards> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();

        AuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // User is signed out, navigate to login activity
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // User is signed in, update currentUID
                    currentUID = user.getUid();
                    getUserSex(); // Fetch user sex after updating currentUID
                }
            }
        };
        mAuth.addAuthStateListener(AuthStateListener);

        rowItems = new ArrayList<Cards>();
        arrayAdapter = new MyArrayAdapter(this, R.layout.item, rowItems);

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                Cards obj = (Cards)dataObject;
                String userID = obj.getUserID();
                usersDb.child(userID).child("connections").child("nope").child(currentUID).setValue(true);
                Toast.makeText(MainActivity.this, "Left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Cards obj = (Cards)dataObject;
                String userID = obj.getUserID();
                usersDb.child(userID).child("connections").child("yep").child(currentUID).setValue(true);
                isConnectionMatch(userID);
                Toast.makeText(MainActivity.this, "Right", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {}
            @Override
            public void onScroll(float scrollProgressPercent) {}
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "Click", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (AuthStateListener != null) {
            mAuth.removeAuthStateListener(AuthStateListener);
        }
    }
    private void isConnectionMatch(String userID){
        DatabaseReference currentUserConnectionDB = usersDb.child(currentUID).child("connections").child("yep").child(userID);
        currentUserConnectionDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Toast.makeText(MainActivity.this,"It's a match",Toast.LENGTH_LONG).show();

                    String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                    usersDb.child(snapshot.getKey()).child("connections").child("matches").child(currentUID).child("ChatID").setValue(key);
                    usersDb.child(currentUID).child(    "connections").child("matches").child(snapshot.getKey()).child("ChatID").setValue(key);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
    private String  userSex;
    private String oppositeSex;
    public void getUserSex(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // Handle the case where the user is not logged in or null
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        DatabaseReference userDb = usersDb.child(user.getUid());
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    if (snapshot.child("sex") != null) {
                        userSex = snapshot.child("sex").getValue().toString();
                        switch (userSex) {
                            case "Male":
                                oppositeSex = "Female";
                                break;
                            case "Female":
                                oppositeSex = "Male";
                                break;
                        }
                        getOppositeSexUser();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void getOppositeSexUser(){
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {
                String sex = snapshot.child("sex").getValue() != null ? snapshot.child("sex").getValue().toString() : null;
                String profileImageURL = "default";
                String name = snapshot.child("name").getValue() != null ? snapshot.child("name").getValue().toString() : null;

                if(sex != null) {
                    if(!snapshot.child("connections").child("nope").hasChild(currentUID) &&
                    !snapshot.child("connections").child("yep").hasChild(currentUID) &&
                    sex.equals(oppositeSex)){

                        if(snapshot.child("profileImageUrl").getValue() != null &&
                        snapshot.child("profileImageUrl").getValue().equals("default")){
                            profileImageURL = snapshot.child("profileImageUrl").getValue().toString();
                        }

                        if(name != null){
                            Cards item = new Cards(snapshot.getKey(),name, profileImageURL);
                            rowItems.add(item);
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MainActivity", "Error fetching opposite sex users", error.toException());
            }
        });
    }

    public void logoutUser(View view) {
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, ChooseLoginRegActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToSettings(View view) {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    public void goToMatches(View view) {
        Intent intent = new Intent(MainActivity.this, MatchesActivity.class);
        startActivity(intent);
    }
}