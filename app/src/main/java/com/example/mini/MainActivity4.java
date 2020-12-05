package com.example.mini;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity4 extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private Button btnMainAddChild;
    private Button btnMainSubmit;
    private Spinner spinnerChildren;
    private userDB currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
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
        ArrayList<childDB> userChildrenList = new ArrayList<>();

        ArrayAdapter<childDB> childAdapter = new ArrayAdapter<childDB>(
                MainActivity4.this,
                android.R.layout.simple_spinner_dropdown_item,
                userChildrenList
        );

        userChildrenList.add(new childDB(1234, "Child 1", "Place", new Date(01 - 01 - 0000), 'M'));
        userChildrenList.add(new childDB(1235, "Child 2", "Place", new Date(01 - 01 - 0000), 'F'));

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