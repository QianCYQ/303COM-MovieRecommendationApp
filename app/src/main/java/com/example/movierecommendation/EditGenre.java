package com.example.movierecommendation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class EditGenre extends AppCompatActivity {
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String mGetDocumentID;

    private String[] GenreList;
    private ArrayList<CheckboxModel> mCheckboxModel = new ArrayList<>();

    private RecyclerView recyclerView;
    private EditGenreAdapter mEditGenreAdapter;

    private Button mEditGenreCancel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_edit);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mGetDocumentID = fAuth.getCurrentUser().getUid();

        GenreList = getResources().getStringArray(R.array.GenreList);

        recyclerView = findViewById(R.id.EditGenreRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mEditGenreAdapter = new EditGenreAdapter(getData(), EditGenre.this);

        recyclerView.setAdapter(mEditGenreAdapter);

        mEditGenreCancel = findViewById(R.id.EditGenreCancel);

        mEditGenreCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private ArrayList<CheckboxModel> getData(){
        for(int x=0; x<GenreList.length; x++){
            mCheckboxModel.add(new CheckboxModel(GenreList[x],false));
        }
        return mCheckboxModel;
    }
}
