package com.example.movierecommendation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieComment extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String mGetDocumentID;

    private int mReleaseYear;
    private String mMovieImage, mMovieTitle,  mMetaScore, mMovieGenre;
    private double mIMDBRate;
    private String mCertificate, mRuntime;

    private TextView mMovie_Title, mMovie_Genre, mMovie_Year, mMovie_Runtime, mIMDB_Rating, mMeta_Score, mMovie_Cert;
    private ImageView mMovie_Image;

    private FloatingActionButton mAddCommentButton;

    private RecyclerView recyclerView;
    private ArrayList<MovieCommentDB> mMovieCommentDB;

    private MovieCommentAdapter mMovieCommentAdapter;
    private String Username, Comment;
    private double Rating;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_and_rating);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mGetDocumentID = fAuth.getCurrentUser().getUid();

        mAddCommentButton = findViewById(R.id.AddCommentButton);

        getIntentInfo();

        mMovie_Image = findViewById(R.id.CommentMovie_Image);
        mMovie_Title = findViewById(R.id.CommentMovie_Title);
        mMovie_Year = findViewById(R.id.CommentMovie_Year);
        mMovie_Genre = findViewById(R.id.CommentMovie_Genre);
        mIMDB_Rating = findViewById(R.id.CommentIMDB_Rating);
        mMeta_Score = findViewById(R.id.CommentMeta_Score);
        mMovie_Cert = findViewById(R.id.CommentMovie_Cert);
        mMovie_Runtime =findViewById(R.id.CommentMovie_Runtime);

        setIntentInfo();

        mAddCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieComment.this, LeaveMovieComment.class);
                intent.putExtra("ImageInfo", mMovieImage);
                intent.putExtra("TitleInfo", mMovieTitle);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.CommentRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMovieCommentDB = new ArrayList<>();

        mMovieCommentAdapter = new MovieCommentAdapter(mMovieCommentDB, MovieComment.this);

        getMovieCommentDB();

        recyclerView.setAdapter(mMovieCommentAdapter);
    }

    private void getMovieCommentDB() {
//        fStore.collection("MovieComment").document(mMovieTitle)
//                .collection("Comments").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                for (int x = 0; x<queryDocumentSnapshots.size(); x++){
//                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(x);
//
//                    Username = documentSnapshot.getString("Username");
//                    Comment = documentSnapshot.getString("Comment");
//                    Rating = documentSnapshot.getDouble("Rating");
//
//                    MovieCommentDB movieCommentDB = new MovieCommentDB(Username, Comment, Rating);
//                    mMovieCommentDB.add(movieCommentDB);
//                }
//                mMovieCommentAdapter.notifyDataSetChanged();
//            }
//        });

        fStore.collection("MovieComment").document(mMovieTitle)
                .collection("Comments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            Username = document.getString("Username");
                            Comment = document.getString("Comment");
                            Rating = document.getDouble("Rating");

                            MovieCommentDB movieCommentDB = new MovieCommentDB(Username, Comment, Rating);
                            mMovieCommentDB.add(movieCommentDB);
                        }
                        mMovieCommentAdapter.notifyDataSetChanged();
                    }
                }
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
        mMovie_Runtime.setText(mRuntime);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMovieCommentAdapter.notifyDataSetChanged();
    }
}
