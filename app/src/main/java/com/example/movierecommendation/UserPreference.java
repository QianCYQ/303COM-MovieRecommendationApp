package com.example.movierecommendation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserPreference extends AppCompatActivity {
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String mGetDocumentID;
    private TextView mPreferenceGenre, mPreferenceYearMin, mPreferenceYearMax;
    private Button mEditGenre, mEditYear;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_preference);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mGetDocumentID = fAuth.getCurrentUser().getUid();

        mPreferenceGenre =findViewById(R.id.UserPreferenceGenre);
        mPreferenceYearMin = findViewById(R.id.PreferenceMovie_YearMin);
        mPreferenceYearMax = findViewById(R.id.PreferenceMovie_YearMax);
        mEditGenre = findViewById(R.id.EditGenreBtn);
        mEditYear = findViewById(R.id.EditYearBtn);


        getPreferenceDB();
        getPreferenceYearDB();

        mEditGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserPreference.this, EditGenre.class);
                startActivity(intent);
            }
        });


        mEditYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserPreference.this, EditYear.class);
                startActivity(intent);
            }
        });
    }

    private void getPreferenceDB() {
        fStore.collection("UserInfo").whereEqualTo(FieldPath.documentId(), mGetDocumentID)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {

                            ArrayList<String> GenreList = (ArrayList<String>)document.get("Users_Prefer_Genre");
                            String visibleString = null;
                            for( int i = 0 ; i < GenreList.size() ; i++) {
                                if(i == 0) {
                                    visibleString = "1. " + GenreList.get(i);
                                } else {
                                    visibleString +=  "\n" + (i+1) + ". " +GenreList.get(i);
                                }
                            }
                            mPreferenceGenre.setText(visibleString);
                        }
                    }
                }
            }
        });
    }

    private void getPreferenceYearDB() {
        fStore.collection("UserInfo").whereEqualTo(FieldPath.documentId(), mGetDocumentID)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            int Min = document.getLong("User_Preference_Year_Min").intValue();
                            mPreferenceYearMin.setText(String.valueOf(Min));

                            int Max = document.getLong("User_Preference_Year_Max").intValue();
                            mPreferenceYearMax.setText(String.valueOf(Max));
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getPreferenceDB();
        getPreferenceYearDB();
    }
}
