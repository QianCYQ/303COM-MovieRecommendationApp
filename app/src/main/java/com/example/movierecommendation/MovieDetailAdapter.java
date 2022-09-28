package com.example.movierecommendation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MovieDetailAdapter extends AppCompatActivity {
    private int mReleaseYear;
    private String mMovieImage, mMovieTitle,  mMetaScore, mMovieGenre;
    private double mIMDBRate;
    private String mCertificate, mOverview, mDirector, mActor1, mActor2, mActor3, mActor4, mRuntime;

    private TextView mMovie_Title, mMovie_Genre, mMovie_Year, mMovie_Runtime, mIMDB_Rating, mMeta_Score, mMovie_Cert, mMovie_Overview, mMovie_DirectorName, mMovie_Actor1, mMovie_Actor2, mMovie_Actor3, mMovie_Actor4;
    private ImageView mMovie_Image;

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;

    private Button mBtnAddWatchList, mViewMovieCommentBtn;
    private String mGetDocumentID;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mGetDocumentID = fAuth.getCurrentUser().getUid();

        mBtnAddWatchList = findViewById(R.id.AddToWatchlistBtn);
        mViewMovieCommentBtn = findViewById(R.id.ViewMovieCommentBtn);

        getIntentInfo();

        mMovie_Image = findViewById(R.id.Movie_Image);
        mMovie_Title = findViewById(R.id.Movie_Title);
        mMovie_Year = findViewById(R.id.Movie_Year);
        mMovie_Genre = findViewById(R.id.Movie_Genre);
        mIMDB_Rating = findViewById(R.id.IMDB_Rating);
        mMeta_Score = findViewById(R.id.Meta_Score);

        mMovie_Cert = findViewById(R.id.Movie_Cert);
        mMovie_Overview = findViewById(R.id.Movie_Overview);
        mMovie_DirectorName = findViewById(R.id.Movie_DirectorName);
        mMovie_Actor1 = findViewById(R.id.Movie_Actor1);
        mMovie_Actor2 = findViewById(R.id.Movie_Actor2);
        mMovie_Actor3 = findViewById(R.id.Movie_Actor3);
        mMovie_Actor4 = findViewById(R.id.Movie_Actor4);
        mMovie_Runtime =findViewById(R.id.Movie_Runtime);

        setIntentInfo();

        fStore.collection("UserInfo").whereEqualTo(FieldPath.documentId(), mGetDocumentID).whereArrayContains("Watchlist",mMovieTitle)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            mBtnAddWatchList.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        });


        mBtnAddWatchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DocumentReference documentReference = fStore.collection("UserInfo").document(mGetDocumentID).collection("Watchlist").document(mGetDocumentID);
                //Map<String, Object> AddtoWatchlist = new HashMap<>();
                //AddtoWatchlist()
                fStore.collection("UserInfo").whereArrayContains("Watchlist",mMovieTitle)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentReference documentReference = fStore.collection("UserInfo").document(mGetDocumentID);
                            documentReference.update("Watchlist", FieldValue.arrayUnion(mMovieTitle));

                            Toast.makeText(MovieDetailAdapter.this, "The movie is in your watchlist now!", Toast.LENGTH_SHORT).show();
                            mBtnAddWatchList.setVisibility(View.INVISIBLE);
                        }
                        else{
                            Toast.makeText(MovieDetailAdapter.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mViewMovieCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieDetailAdapter.this, MovieComment.class);
                intent.putExtra("ImageInfo", mMovieImage);
                intent.putExtra("TitleInfo", mMovieTitle);
                intent.putExtra("YearInfo", mReleaseYear);
                intent.putExtra("GenreInfo", mMovieGenre);
                intent.putExtra("IMDBInfo", mIMDBRate);
                intent.putExtra("MetaInfo", mMetaScore);
                intent.putExtra("RuntimeInfo",mRuntime);
                intent.putExtra("CertificateInfo",mCertificate);

                startActivity(intent);
            }
        });
    }

    private void getIntentInfo(){
        mMovieImage = getIntent().getStringExtra("ImageInfo");
        mMovieTitle = getIntent().getStringExtra("TitleInfo");
        mReleaseYear = getIntent().getIntExtra("YearInfo", 0);
        mMovieGenre = getIntent().getStringExtra("GenreInfo");
        mIMDBRate = getIntent().getDoubleExtra("IMDBInfo", 0);
        mMetaScore =getIntent().getStringExtra("MetaInfo");

        mCertificate = getIntent().getStringExtra("CertificateInfo");
        mOverview = getIntent().getStringExtra("OverviewInfo");
        mDirector = getIntent().getStringExtra("DirectorInfo");
        mActor1 = getIntent().getStringExtra("Actor1Info");
        mActor2 = getIntent().getStringExtra("Actor2Info");
        mActor3 = getIntent().getStringExtra("Actor3Info");
        mActor4 = getIntent().getStringExtra("Actor4Info");
        mRuntime = getIntent().getStringExtra("RuntimeInfo");
    }



    private void setIntentInfo(){
        Picasso.get().load(mMovieImage).into(mMovie_Image);
        mMovie_Title.setText(mMovieTitle);
        mMovie_Year.setText(String.valueOf(mReleaseYear));
        mMovie_Genre.setText(mMovieGenre);
        mIMDB_Rating.setText(String.valueOf(mIMDBRate));
        mMeta_Score.setText(mMetaScore);

        mMovie_Cert.setText(mCertificate);
        mMovie_Overview.setText(mOverview);
        mMovie_DirectorName.setText(mDirector);
        mMovie_Actor1.setText(mActor1);
        mMovie_Actor2.setText(mActor2);
        mMovie_Actor3.setText(mActor3);
        mMovie_Actor4.setText(mActor4);
        mMovie_Runtime.setText(mRuntime);
    }
}


