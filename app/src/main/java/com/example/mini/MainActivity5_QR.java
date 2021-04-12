package com.example.mini;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class MainActivity5_QR extends AppCompatActivity implements View.OnClickListener {

    private FirebaseUser mUser;
    private DatabaseReference mDatabase;
    private DatabaseReference doctorReference;

    Button scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity5__q_r);

        Intent intent = getIntent();
        requiredVaccineMetaData = intent.getIntArrayExtra("contentsForQR");

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid());
        doctorReference = FirebaseDatabase.getInstance().getReference().child("DoctorDB");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                localUser.setUserChildren(snapshot.getValue(userDB.class).getUserChildren());
                if (requiredVaccineMetaData.length == 2) {
                    localUserChildVaccine = localUser.getUserChildren().get(requiredVaccineMetaData[0]).getChildVaccines();
                } else {
                    Toast.makeText(MainActivity5_QR.this, "Fetching the data from Select Activity Failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity5_QR.this, "Fault in your child!", Toast.LENGTH_SHORT).show();
            }
        });

        doctorReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DoctorDB localDoctor = new DoctorDB();
                    localDoctor.setDoctorName(dataSnapshot.getValue(DoctorDB.class).getDoctorName());
                    localDoctor.setDoctorDegree(dataSnapshot.getValue(DoctorDB.class).getDoctorDegree());
                    localDoctor.setDoctorCity(dataSnapshot.getValue(DoctorDB.class).getDoctorCity());
                    localDoctor.setDoctorHospital(dataSnapshot.getValue(DoctorDB.class).getDoctorHospital());
                    localDoctor.setDoctorID(dataSnapshot.getValue(DoctorDB.class).getDoctorID());
                    doctorListFromDB.add(localDoctor);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        scanButton = findViewById(R.id.scanButton);
        scanButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        scanCode();
    }

    private void scanCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Getting doctor details...");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents() != null) {
                qrDoctorID = Long.parseLong(result.getContents());
                for (DoctorDB doctorTemp : doctorListFromDB) {
                    if (qrDoctorID == doctorTemp.getDoctorID()) {
                        Toast.makeText(getBaseContext(), "Congratulations, your child is safe from anti-vaxxers!", Toast.LENGTH_SHORT).show();
                        localUserChildVaccine.get(requiredVaccineMetaData[1]).setVaccincated(true);
                        mDatabase.setValue(localUser);
                    } else {
                        Toast.makeText(MainActivity5_QR.this, "Doctor Validation Failed! Vaccine not administered according to us!", Toast.LENGTH_SHORT).show();
                    }
                }
/*
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Scanning Successful");
                qrDoctorID = Long.parseLong(result.getContents());
                builder.setTitle("Scanning Result");
                builder.setPositiveButton("Scan Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scanCode();
                    }
                }).setNegativeButton("Finish", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent vaccineValidated = new Intent(MainActivity5_QR.this, MainActivity6.class);
                        vaccineValidated.putExtra("qrDoctorID", qrDoctorID);
                        startActivity(vaccineValidated);
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
*/
            }
            else{
                Toast.makeText(this, "No Results", Toast.LENGTH_LONG).show();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}