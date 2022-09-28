package com.example.movierecommendation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MovieCommentAdapter extends RecyclerView.Adapter<MovieCommentAdapter.MovieCommentHolder> {
    ArrayList<MovieCommentDB> mMovieCommentDB;
    Context mContext;

    public MovieCommentAdapter(ArrayList<MovieCommentDB> movieCommentDB, Context context) {
        mMovieCommentDB = movieCommentDB;
        mContext = context;
    }

    @NonNull
    @Override
    public MovieCommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.moviecomment_adapter,parent,false);
        return new MovieCommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieCommentHolder holder, int position) {
        holder.mCommentDBUserName.setText(mMovieCommentDB.get(position).getUsername());
        holder.mCommentDBComment.setText(mMovieCommentDB.get(position).getComment());
        holder.mCommentDBRating.setText(String.valueOf(mMovieCommentDB.get(position).getRating()));
    }

    @Override
    public int getItemCount() {
        return mMovieCommentDB.size();
    }

    public static class MovieCommentHolder extends RecyclerView.ViewHolder{
        private TextView mCommentDBUserName, mCommentDBComment, mCommentDBRating;

        public MovieCommentHolder(@NonNull View itemView) {
            super(itemView);

            mCommentDBUserName = itemView.findViewById(R.id.CommentDBUserName);
            mCommentDBComment = itemView.findViewById(R.id.CommentDBComment);
            mCommentDBRating = itemView.findViewById(R.id.CommentDBRating);
        }
    }

}
