package com.example.mini;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaCodec;
import android.os.Bundle;
import android.service.autofill.FieldClassification;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity2 extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private Button btnMainRegisterMe;
    private EditText edtMainRegisterEmail, edtMainRegisterPassword, getEdtMainRegisterConfirmPW;
    private ProgressBar progressBarRegisterMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        btnMainRegisterMe = findViewById(R.id.btnSubmit);
        edtMainRegisterEmail = findViewById(R.id.edtRegisterEmail);
        edtMainRegisterPassword = findViewById(R.id.edtRegisterPassword);
        getEdtMainRegisterConfirmPW = findViewById(R.id.edtRegisterConfirmPW);
        progressBarRegisterMain = findViewById(R.id.progressBarRegister);
        progressBarRegisterMain.setVisibility(View.GONE);

        btnMainRegisterMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUser();
            }
        });
    }

    private void RegisterUser() {
        final String getEmail = edtMainRegisterEmail.getText().toString();
        String getPassword = edtMainRegisterPassword.getText().toString();
        String getConfirmPW = getEdtMainRegisterConfirmPW.getText().toString();

        if (getEmail.isEmpty()) {
            edtMainRegisterEmail.setError("Email is required!");
            edtMainRegisterEmail.requestFocus();
            return;
        }

        if (getPassword.isEmpty()) {
            edtMainRegisterPassword.setError("Password is required!");
            edtMainRegisterPassword.requestFocus();
            return;
        }

        if (getConfirmPW.isEmpty()) {
            getEdtMainRegisterConfirmPW.setError("Confirm password field cannot be empty.");
            getEdtMainRegisterConfirmPW.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(getEmail).matches()) {
            edtMainRegisterEmail.setError("Email ID not valid.");
            return;
        }

        if (getPassword.length() < 8 || getPassword.length() > 20) {
            edtMainRegisterPassword.setError("Password should be between 8-20 characters.");
            edtMainRegisterPassword.requestFocus();
            return;
        }

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[~!@#$%^&*()<>_+{}]).{8,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(getPassword);
        if (!matcher.matches()) {
            edtMainRegisterPassword.setError("Not a valid password.");
            edtMainRegisterPassword.requestFocus();
            edtMainRegisterPassword.getText().clear();
            getEdtMainRegisterConfirmPW.getText().clear();
            return;
        }

        if (!getPassword.equals(getConfirmPW)) {
            edtMainRegisterPassword.setError("Password and Confirm Password do not match. Please try again.");
            getEdtMainRegisterConfirmPW.setError("Password and Confirm Password do not match. PLease try again.");
            edtMainRegisterPassword.requestFocus();
            return;
        }

        progressBarRegisterMain.setVisibility(View.VISIBLE);
        btnMainRegisterMe.requestFocus();
        mAuth.createUserWithEmailAndPassword(getEmail, getPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            userDB registerSuccessful = new userDB(getEmail);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(registerSuccessful).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressBarRegisterMain.setVisibility(View.VISIBLE);
                                        Toast.makeText(MainActivity2.this, "Task failed unsuccessfully!", Toast.LENGTH_SHORT).show();
                                        firebaseUser.sendEmailVerification();
                                        edtMainRegisterEmail.getText().clear();
                                        edtMainRegisterPassword.getText().clear();
                                        getEdtMainRegisterConfirmPW.getText().clear();
                                        btnMainRegisterMe.requestFocus();
                                        progressBarRegisterMain.setVisibility(View.GONE);
                                        OpenActivity2();
                                    } else {
                                        Toast.makeText(MainActivity2.this, "Sorry! Failed to register, try again.", Toast.LENGTH_LONG).show();
                                        progressBarRegisterMain.setVisibility(View.GONE);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(MainActivity2.this, "Failed to register, try again twice.", Toast.LENGTH_LONG).show();
                            progressBarRegisterMain.setVisibility(View.GONE);
                        }
                    }
                });
    }

    public void OpenActivity2() {
        Intent intentRegisterSuccess = new Intent(MainActivity2.this, MainActivity.class);
        startActivity(intentRegisterSuccess);
        Toast.makeText(MainActivity2.this, "Kindly verify your Email ID.", Toast.LENGTH_LONG).show();
    }
}