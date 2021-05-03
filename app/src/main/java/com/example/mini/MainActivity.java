package com.example.mini;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firestore.v1.FirestoreGrpc;

import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private Button btnMainSignIn;
    private Button btnMainRegister;
    private EditText edtMainSignInEmail, edtMainSignInPassword;
    private TextView mainForgotPassword;
    private ProgressBar progressBarMainSignIn;
    private Button button;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null)
            if (currentUser.isEmailVerified())
                OpenActivity4();
            else {
                Toast.makeText(MainActivity.this, "Failed to login. Email ID not verified!", Toast.LENGTH_SHORT).show();
                firebaseUser.sendEmailVerification();
            }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        button = (Button) findViewById(R.id.btnHelp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity7();
            }
        });

        progressBarMainSignIn = findViewById(R.id.progressBarSignIn);
        progressBarMainSignIn.setVisibility(View.GONE);

        btnMainSignIn = (Button) findViewById(R.id.btnSignIn);
        btnMainSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInSuccessful();
            }
        });

        btnMainRegister = (Button) findViewById(R.id.btnRegister);
        btnMainRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenActivity2();
            }
        });

        mainForgotPassword = (TextView) findViewById(R.id.forgotPassword);
        mainForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPasswordMethod();
            }
        });
    }

    public void openActivity7() {
        Intent intent = new Intent(this, MainActivity7.class);
        startActivity(intent);
    }

    private void logInSuccessful() {

        edtMainSignInEmail = findViewById(R.id.edtSignInEmail);
        edtMainSignInPassword = findViewById(R.id.edtSignInPassword);

        String signInEmail = edtMainSignInEmail.getText().toString();
        String signInPassword = edtMainSignInPassword.getText().toString();

        if (signInEmail.isEmpty()) {
            edtMainSignInEmail.setError("Email ID is required.");
            edtMainSignInEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(signInEmail).matches()) {
            edtMainSignInEmail.setError("Not a valid email ID.");
            edtMainSignInEmail.requestFocus();
            return;
        }

        if (signInPassword.isEmpty()) {
            edtMainSignInPassword.setError("Password is also required!");
            edtMainSignInPassword.requestFocus();
            return;
        }

        progressBarMainSignIn.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(signInEmail, signInPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    firebaseUser = mAuth.getCurrentUser();
                    if (firebaseUser.isEmailVerified()) {
                        Toast.makeText(MainActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                        edtMainSignInEmail.getText().clear();
                        edtMainSignInPassword.getText().clear();
                        OpenActivity4();
                        progressBarMainSignIn.setVisibility(View.GONE);
                    } else {
                        firebaseUser.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Email ID not verified.", Toast.LENGTH_LONG).show();
                        progressBarMainSignIn.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Failed to login! Please check your credentials.", Toast.LENGTH_SHORT).show();
                    edtMainSignInPassword.getText().clear();
                    progressBarMainSignIn.setVisibility(View.GONE);
                }
            }
        });
    }

    private void forgotPasswordMethod() {
        edtMainSignInEmail = findViewById(R.id.edtSignInEmail);
        edtMainSignInPassword = findViewById(R.id.edtSignInPassword);

        String signInEmail = edtMainSignInEmail.getText().toString();
        String signInPassword = edtMainSignInPassword.getText().toString();

        if (signInEmail.isEmpty()) {
            edtMainSignInEmail.setError("Email ID is required.");
            edtMainSignInEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(signInEmail).matches()) {
            edtMainSignInEmail.setError("Not a valid email ID.");
            edtMainSignInEmail.requestFocus();
            return;
        }

        if (!signInPassword.isEmpty()) {
            edtMainSignInPassword.setError("Password is not required!");
            edtMainSignInPassword.requestFocus();
            return;
        }

        if (firebaseUser.isEmailVerified()) {
            mAuth.sendPasswordResetEmail(signInEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Please check your Email.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Please try again after sometime.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "Cannot send reset password link to an unregistered user.", Toast.LENGTH_LONG).show();
        }
    }

    public void OpenActivity4() {
        Intent intentSignInSuccess = new Intent(MainActivity.this, MainActivity4.class);
        startActivity(intentSignInSuccess);
    }

    public void OpenActivity2() {
        Intent intentRegisterActivity = new Intent(MainActivity.this, MainActivity2.class);
        startActivity(intentRegisterActivity);
    }
}