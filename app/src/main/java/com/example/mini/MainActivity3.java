package com.example.mini;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.LocalDate;

import java.util.Random;

public class MainActivity3 extends AppCompatActivity {
    private RadioGroup radioGroupMainGender;
    private int radioBtnMainChecked;
    private EditText edtMainChildName, edtMainChildDOB, edtMainChildAge, edtMainChildPOB;
    private Button btnMainSubmit;
    private childDB localChild = new childDB();

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    private Random randomGenerator = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("ChildrenList");

        edtMainChildName = findViewById(R.id.edtActChildName);
        edtMainChildDOB = findViewById(R.id.edtActChildDOB);
        edtMainChildAge = findViewById(R.id.edtActChildAge);
        edtMainChildPOB = findViewById(R.id.edtActChildPOB);
        radioGroupMainGender = findViewById(R.id.radioGroupGender);

        radioBtnMainChecked = radioGroupMainGender.getCheckedRadioButtonId();
        radioGroupMainGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioBoy) {
                    Toast.makeText(MainActivity3.this, "Gender male selected", Toast.LENGTH_SHORT).show();
                    localChild.setChildGender(0);
                } else if (i == R.id.radioGirl) {
                    Toast.makeText(MainActivity3.this, "Gender female selected.", Toast.LENGTH_SHORT).show();
                    localChild.setChildGender(1);
                }
            }
        });

        btnMainSubmit = findViewById(R.id.btnAct3Submit);
        btnMainSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnMain3SubmitClicked();
            }
        });
    }

    private void btnMain3SubmitClicked() {
        String mainChildName = edtMainChildName.getText().toString();
        String mainChildPOB = edtMainChildPOB.getText().toString();
        int mainChildAge = Integer.parseInt(edtMainChildAge.getText().toString());

        if (mainChildName.isEmpty()) {
            edtMainChildName.setError("Name is required.");
            edtMainChildName.requestFocus();
            return;
        }

        if (mainChildPOB.isEmpty()) {
            edtMainChildPOB.setError("Place of birth not provided.");
        }

        //TODO If Child Age is not provided then app crashes, the following fix is not working check any other fix
        if (edtMainChildAge.getText() == null) {
            edtMainChildAge.setError("Age is required.");
            edtMainChildAge.requestFocus();
            return;
        }

//        TODO check how to input the dates from EditText, currently added jodo-time implementation
//        LocalDate localDate = new LocalDate(2020,1,11);
//        Toast.makeText(MainActivity3.this, localDate.toString(), Toast.LENGTH_SHORT).show();

        localChild.setChildName(mainChildName);
        localChild.setPlaceOfBirth(mainChildPOB);
        localChild.setChildAge(mainChildAge);
        localChild.setChildID(randomGenerator.nextInt(999999999));

        databaseReference.child("child" + localChild.getChildID()).setValue(localChild)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity3.this, "Child sent to cloud DB.", Toast.LENGTH_SHORT).show();
                            edtMainChildName.getText().clear();
                            edtMainChildDOB.getText().clear();
                            edtMainChildAge.getText().clear();
                            edtMainChildPOB.getText().clear();
                            OpenActivity4();
                        } else {
                            Toast.makeText(MainActivity3.this, "Your child is defective.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void OpenActivity4() {
        Intent childSentToCloud = new Intent(MainActivity3.this, MainActivity4.class);
        startActivity(childSentToCloud);
    }
}