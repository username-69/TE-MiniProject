package com.example.mini;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity3 extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private DatabaseReference vaccineDBReference;
    private FirebaseUser firebaseUser;

    private RadioGroup radioGroupMainGender;
    private int radioBtnMainChecked;
    Integer[] mainChildAge = new Integer[1];
    boolean dontSaveDataToDB = false;
    private Button btnMainSubmit;
    private userDB localUser = new userDB();
    private childDB localChild = new childDB();
    private DOB localChildDOB;
    private List<childDB> localUserChildren = new ArrayList<>();
    private List<VaccineData> wholeVaccineData = new ArrayList<>();
    private EditText edtMainChildName, edtMainChildDOB, edtMainChildPOB;
    private TextView tVMainChildAge;


    private Random randomGenerator = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        vaccineDBReference = FirebaseDatabase.getInstance().getReference().child("VaccineDB");

        databaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                localUser.setUserChildren(snapshot.getValue(userDB.class).getUserChildren());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity3.this, "Adding child failed please try later!", Toast.LENGTH_LONG).show();
            }
        });

        vaccineDBReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    VaccineData sampleFetchedVaccine = new VaccineData();
                    sampleFetchedVaccine.setVaccineName(dataSnapshot.getValue(VaccineData.class).getVaccineName());
                    sampleFetchedVaccine.setVaccineDose(dataSnapshot.getValue(VaccineData.class).getVaccineDose());
                    sampleFetchedVaccine.setVaccineWeek(dataSnapshot.getValue(VaccineData.class).getVaccineWeek());
                    wholeVaccineData.add(sampleFetchedVaccine);
                }
                localChild.setChildVaccines(wholeVaccineData);
                for (VaccineData tempVaccine : localChild.getChildVaccines()) {
                    tempVaccine.setVaccincated(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity3.this, "Not happening bro! Tera at chuka hai!", Toast.LENGTH_SHORT).show();
            }
        });

        edtMainChildName = findViewById(R.id.edtActChildName);
        edtMainChildDOB = findViewById(R.id.edtActChildDOB);
        tVMainChildAge = findViewById(R.id.tVActChildAge);
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

        edtMainChildDOB.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String[] splitDate = edtMainChildDOB.getText().toString().split("/");
                if (splitDate.length == 3) {
                    int daysOfMonth = 31;
                    localChildDOB = new DOB(Integer.parseInt(splitDate[0]), Integer.parseInt(splitDate[1]), Integer.parseInt(splitDate[2]));
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        YearMonth yearMonth = YearMonth.of(localChildDOB.getYear(), localChildDOB.getMonth());
                        daysOfMonth = yearMonth.lengthOfMonth();
                    }
                    if (localChildDOB.getDate() < 1 || localChildDOB.getDate() > daysOfMonth || localChildDOB.getMonth() < 1 || localChildDOB.getMonth() > 12) {
                        edtMainChildDOB.setError("Don't play around you gEeK tEsTEr!");
                        edtMainChildDOB.requestFocus();
                        tVMainChildAge.setText(null);
                        dontSaveDataToDB = true;
                        return;
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            if ((LocalDate.of(localChildDOB.getYear(), localChildDOB.getMonth(), localChildDOB.getDate()).isAfter(LocalDate.now()))) {
                                edtMainChildDOB.setError("Don't play around you gEeK tEsTEr!");
                                edtMainChildDOB.requestFocus();
                                tVMainChildAge.setText(null);
                                dontSaveDataToDB = true;
                                return;
                            }
                            mainChildAge[0] = Math.toIntExact(
                                    ChronoUnit.WEEKS.between(
                                            LocalDate.of(localChildDOB.getYear(), localChildDOB.getMonth(), localChildDOB.getDate()),
                                            LocalDate.now()
                                    )
                            );
                            tVMainChildAge.setText(mainChildAge[0].toString());
                            dontSaveDataToDB = false;
                        }
                    }
                }
            }

            ;
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

        if (mainChildName.isEmpty()) {
            edtMainChildName.setError("Name is required.");
            edtMainChildName.requestFocus();
            return;
        }

        if (mainChildPOB.isEmpty()) {
            edtMainChildPOB.setError("Place of birth not provided.");
        }

        if (edtMainChildDOB.getText().toString().isEmpty()) {
            edtMainChildDOB.setError("Date of Birth required.");
            edtMainChildDOB.requestFocus();
            return;
        }

        if (mainChildAge[0] < 0) {
            edtMainChildDOB.setError("Please enter accurate DOB in required format!");
            edtMainChildDOB.requestFocus();
            return;
        }

        localChild.setChildName(mainChildName);
        localChild.setPlaceOfBirth(mainChildPOB);
        localChild.setChildID(randomGenerator.nextInt(999999999));
        localChild.setChildDOB(localChildDOB);
        if ((mainChildAge != null) && (!(mainChildAge[0] < 0))) {
            localChild.setChildAge(mainChildAge[0]);
        }

        //copying the list to a local list and then adding the current child and then again saving it.
        List<childDB> copyList = new ArrayList<>();
        copyList = localUser.getUserChildren();
        if (copyList.get(0).getChildID() == -1) {
            copyList.clear();
        }

        if ((mainChildAge != null) && (!dontSaveDataToDB)) {
            copyList.add(localChild);
            localUser.setUserChildren(copyList);
        }

        if (!dontSaveDataToDB) {
            databaseReference
                    .child(firebaseUser.getUid())
                    .setValue(localUser)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity3.this, "Child sent to cloud DB.", Toast.LENGTH_SHORT).show();
                                edtMainChildName.getText().clear();
                                edtMainChildDOB.getText().clear();
                                edtMainChildPOB.getText().clear();
                                OpenActivity4();
                            } else {
                                Toast.makeText(MainActivity3.this, "Your child is defective.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            edtMainChildDOB.requestFocus();
            return;
        }
    }

    private void OpenActivity4() {
        Intent childSentToCloud = new Intent(MainActivity3.this, MainActivity4.class);
        startActivity(childSentToCloud);
    }
}