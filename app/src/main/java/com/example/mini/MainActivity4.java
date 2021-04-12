package com.example.mini;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.view.Gravity.*;

public class MainActivity4 extends AppCompatActivity {

    //creating ArrayList of String for storing the names of children
    ArrayList<String> userChildrenList = new ArrayList<>();
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private Button btnMainAddChild;
    private Button btnMainSubmit;
    private FirebaseAuth mAuth;
    private Spinner spinnerChildren;
    private Button btnSignOut;
    private userDB currentUser;
    private userDB userFetchedFromDB;
    private int spinnerItemNumber = -1;
    private String defaultName = "108Name108108";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        btnMainAddChild = (Button) findViewById(R.id.btnAddChild);
        btnMainSubmit = (Button) findViewById(R.id.btnSubmitChild);
        btnSignOut = (Button) findViewById(R.id.btnSignOut);
        spinnerChildren = (Spinner) findViewById(R.id.spinnerShowChildren);

        btnMainAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenActivity3();
            }
        });

        //Warning!!! Check if the reference given is exactly the same as in MainActivity3
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                clearing the ArrayList to ensure nothing from past appears as Karma!
                userChildrenList.clear();
                userChildrenList.add("Select a child - ");

//                fetching the names through the following route, FB DB -> Local Object user's child list -> Adapter(Only Names)
                userFetchedFromDB = new userDB(snapshot.getValue(userDB.class).getUserChildren());
//                adding all the names to the Adapter
                for (int i = 0; i < userFetchedFromDB.getUserChildren().size(); i++) {
                    //adding the names from local object
                    if (userFetchedFromDB.getUserChildren().get(i).getChildName() != defaultName) {
                        Log.d("ChildrenName", userFetchedFromDB.getUserChildren().get(i).getChildName());
                        userChildrenList.add(userFetchedFromDB.getUserChildren().get(i).getChildName());
                    }
                }

                spinnerFunction();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity4.this, "Database Fetching failed!", Toast.LENGTH_SHORT).show();
            }
        });

        btnMainSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenActivity6(spinnerItemNumber);
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Toast.makeText(MainActivity4.this, "Logged out successfully. Please don't forget to vaccinate your child though!", Toast.LENGTH_LONG).show();
                Intent intentUserSignOut = new Intent(MainActivity4.this, MainActivity.class);
                startActivity(intentUserSignOut);
            }
        });
    }

    private void spinnerFunction() {
        for (String defaultChildName : userChildrenList) {
            if (defaultChildName == defaultName) {
                userChildrenList.remove(defaultChildName);
            }
            ;
        }
        ArrayAdapter<String> childAdapter = new ArrayAdapter<String>(
                MainActivity4.this,
                android.R.layout.simple_spinner_dropdown_item,
                userChildrenList
        );
        childAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChildren.setAdapter(childAdapter);
        spinnerChildren.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    Toast vaccinationReadyMsg = Toast.makeText(MainActivity4.this, userChildrenList.get(i) + " ready for vaccination!", Toast.LENGTH_LONG);
                    vaccinationReadyMsg.show();
                }
                spinnerItemNumber = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(MainActivity4.this, "Please select the child to be vaccinated!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void OpenActivity3() {
        Intent intent = new Intent(this, MainActivity3.class);
        startActivity(intent);
    }

    public void OpenActivity6(int selectedChild) {
        if (selectedChild > 0) {
            Intent intent = new Intent(this, MainActivity6.class);
            intent.putExtra("childID", selectedChild - 1);
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity4.this, "Please select the child first.", Toast.LENGTH_LONG).show();
        }
    }
}