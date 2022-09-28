package com.example.movierecommendation;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class LeaveMovieComment extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String mGetDocumentID, mCurrentUsername;

    private String mMovieImage, mMovieTitle, Comment;
    private double Rating;

    private TextView mMovie_Title;
    private ImageView mMovie_Image;

    private RatingBar mRatingBar;
    private EditText mEditCommentEditText;
    private Button mEditCommentSubmitBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_comment);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mGetDocumentID = fAuth.getCurrentUser().getUid();

        mRatingBar = findViewById(R.id.EditCommentRatingBar);
        mEditCommentEditText = findViewById(R.id.EditCommentEditText);
        mEditCommentSubmitBtn = findViewById(R.id.EditCommentSubmitBtn);
        Rating = 2.5;

        mMovie_Image = findViewById(R.id.AddCommentMovie_Image);
        mMovie_Title = findViewById(R.id.AddCommentMovie_Title);

        getIntentInfo();

        setIntentInfo();

        fStore.collection("UserInfo").whereEqualTo(FieldPath.documentId(), mGetDocumentID)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            mCurrentUsername = document.get("Username").toString();
                        }
                    }
                }
            }
        });

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Rating = rating;
            }
        });

        mEditCommentSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comment = mEditCommentEditText.getText().toString().trim();

                if(TextUtils.isEmpty(Comment)){
                    mEditCommentEditText.setError("Please enter your comment");
                    mEditCommentEditText.requestFocus();
                    return;
                }

                DocumentReference addNewComment = fStore.collection("MovieComment").document(mMovieTitle).collection("Comments").document();
                Map<String, Object> NewComment = new HashMap<>();
                NewComment.put("Username",mCurrentUsername);
                NewComment.put("Rating",Rating);
                NewComment.put("Comment",Comment);

                addNewComment.set(NewComment);
                Toast.makeText(LeaveMovieComment.this,"New Comment has been added", Toast.LENGTH_LONG).show();

                finish();
            }
        });
    }

    private void getIntentInfo(){
        mMovieImage = getIntent().getStringExtra("ImageInfo");
        mMovieTitle = getIntent().getStringExtra("TitleInfo");
    }

    private void setIntentInfo(){
        Picasso.get().load(mMovieImage).into(mMovie_Image);
        mMovie_Title.setText(mMovieTitle);
    }
}
