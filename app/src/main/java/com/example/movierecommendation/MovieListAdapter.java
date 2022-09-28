package com.example.movierecommendation;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.opencsv.CSVReader;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private final MovieDetail movieDetail;
    ArrayList<MovieList> movieLists;

    public MovieListAdapter(ArrayList<MovieList> movieLists, MovieDetail movieDetail) {

        this.movieLists = movieLists;
        this.movieDetail = movieDetail;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_adapter,parent,false);
        return new MovieViewHolder(view, movieDetail);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Picasso.get().load(movieLists.get(position).getMovieImage()).into(holder.mMovie_Image);

        holder.mMovie_Title.setText(movieLists.get(position).getMovieTitle());
        holder.mMovie_ReleasedYear.setText(String.valueOf(movieLists.get(position).getReleaseYear()));
        holder.mMovie_Genre.setText(movieLists.get(position).getMovieGenre());
        holder.mIMDB_rate.setText(String.valueOf(movieLists.get(position).getIMDBRate()));
        holder.mMeta_score.setText(movieLists.get(position).getMetaScore());



    }

    @Override
    public int getItemCount() {
        return movieLists.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{
        private TextView mMovie_Title, mMovie_Genre, mMovie_ReleasedYear, mIMDB_rate, mMeta_score;
        private ImageView mMovie_Image;


        public MovieViewHolder(@NonNull View itemView, MovieDetail movieDetail) {
            super(itemView);
            mMovie_Title = itemView.findViewById(R.id.MovieTitle);
            mMovie_Genre = itemView.findViewById(R.id.MovieGenre);
            mMovie_ReleasedYear = itemView.findViewById(R.id.MovieReleaseYear);
            mIMDB_rate = itemView.findViewById(R.id.IMDBRating);
            mMeta_score = itemView.findViewById(R.id.MetaScore);
            mMovie_Image = itemView.findViewById(R.id.MovieImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(movieDetail != null){
                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION){
                            movieDetail.onItemClick(position);
                        }
                    }
                }
            });

        }

    }


}