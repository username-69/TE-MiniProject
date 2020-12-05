package com.example.mini;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity4 extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private Button btnMainAddChild;
    private Button btnMainSubmit;
    private Spinner spinnerChildren;
    private userDB currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("ChildrenList");
        btnMainAddChild = (Button) findViewById(R.id.btnAddChild);
        btnMainSubmit = (Button) findViewById(R.id.btnSubmitChild);
        spinnerChildren = (Spinner) findViewById(R.id.spinnerShowChildren);
        btnMainAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenActivity3();
            }
        });
        btnMainSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenActivity5_QR();
            }
        });

        //creating ArrayList of String for storing the names of children
        final ArrayList<String> userChildrenList = new ArrayList<>();
        //creating Adapter to pass it to spinner
        final ArrayAdapter<String> childAdapter = new ArrayAdapter<String>(
                MainActivity4.this,
                android.R.layout.simple_dropdown_item_1line,
                userChildrenList
        );

        //TODO Check if the reference given is exactly the same as in MainActivty3
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //clearing the ArrayList to ensure nothing from past appears as Karma!
                userChildrenList.clear();

                //fetching the names through the following route, FB DB -> Local Object of same class -> ArrayList
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    //in a way casting the "Object" from Firebase to childDB i.e. what we want
                    childDB sampleChild = dataSnapshot.getValue(childDB.class);
                    //adding the names from local object see the above route
                    userChildrenList.add(sampleChild.getChildName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //TODO Check if the following is required or not.
//        childAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChildren.setAdapter(childAdapter);
    }

    public void OpenActivity3(){
        Intent intent = new Intent(this,MainActivity3.class);
        startActivity(intent);
    }
    public void OpenActivity5_QR(){
        Intent intent = new Intent(this,MainActivity5_QR.class);
        startActivity(intent);
    }
}