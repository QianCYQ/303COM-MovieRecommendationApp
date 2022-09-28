package com.example.movierecommendation;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditYear extends AppCompatActivity {
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String mGetDocumentID;

    private EditText mYearMin, mYearMax;
    private Button mUpdateYear;

    private String YearMin, YearMax;
    private int YearMinInt, YearMaxInt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year_edit);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mGetDocumentID = fAuth.getCurrentUser().getUid();

        mYearMin = findViewById(R.id.EditPreferenceMovie_YearMin);
        mYearMax = findViewById(R.id.EditPreferenceMovie_YearMax);
        mUpdateYear = findViewById(R.id.UpdateYearBtn);

        mYearMin.addTextChangedListener(EmptyString);
        mYearMax.addTextChangedListener(EmptyString);

        mUpdateYear.setEnabled(false);

        mUpdateYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YearMin = mYearMin.getText().toString().trim();
                YearMax = mYearMax.getText().toString().trim();
                YearMinInt = Integer.parseInt(YearMin);
                YearMaxInt = Integer.parseInt(YearMax);

//                if(TextUtils.isEmpty(YearMin)){
//                    mYearMin.setError("Please fill in the required information.");
//                    mYearMin.requestFocus();
//                    return;
//                }
//                if(TextUtils.isEmpty(YearMax)){
//                    mYearMax.setError("Please fill in the required information.");
//                    mYearMax.requestFocus();
//                    return;
//                }

                if(YearMinInt < 1920){
                    mYearMin.setError("The value should not less than 1920.");
                    mYearMin.requestFocus();
                    return;
                }
                if(YearMaxInt < 1920){
                    mYearMax.setError("The value should not less than 1920.");
                    mYearMax.requestFocus();
                    return;
                }

                if(YearMinInt > 2020){
                    mYearMin.setError("The value should not more than 2020.");
                    mYearMin.requestFocus();
                    return;
                }
                if(YearMaxInt > 2020){
                    mYearMax.setError("The value should not more than 2020.");
                    mYearMax.requestFocus();
                    return;
                }

                if(YearMinInt > YearMaxInt){
                    mYearMax.setError("Invalid input.");
                    mYearMax.requestFocus();
                    return;
                }

                DocumentReference documentReference = fStore.collection("UserInfo").document(mGetDocumentID);
                documentReference.update("User_Preference_Year_Min", YearMinInt);
                documentReference.update("User_Preference_Year_Max", YearMaxInt);

                finish();
            }
        });
    }

    private TextWatcher EmptyString = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String YearMin = mYearMin.getText().toString();
            String YearMax = mYearMax.getText().toString();

            if(!YearMin.isEmpty() && !YearMax.isEmpty()){
                mUpdateYear.setEnabled(true);
            }else {
                mUpdateYear.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
