package com.example.movierecommendation;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.opencsv.CSVReader;

import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class Recommendation1 extends AppCompatActivity {
    private Spinner GenreSpinner1, GenreSpinner2, GenreSpinner3;
    private Button mSubmit;

    private EditText mYearMin, mYearMax;
    private String YearMin, YearMax, Genre1, Genre2, Genre3;
    private int YearMinInt, YearMaxInt;

    private ArrayList<MovieList> FilteredMovieLists;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation_basic);

        GenreSpinner1 = findViewById(R.id.MovieGenreSpinner1);
        GenreSpinner2 = findViewById(R.id.MovieGenreSpinner2);
        GenreSpinner3 = findViewById(R.id.MovieGenreSpinner3);
        mSubmit = findViewById(R.id.MakeRecommendation1Btn);

        mYearMin = findViewById(R.id.EnterPreferenceMovie_YearMin);
        mYearMax = findViewById(R.id.EnterPreferenceMovie_YearMax);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.GenreList, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        GenreSpinner1.setAdapter(adapter);
        GenreSpinner2.setAdapter(adapter);
        GenreSpinner3.setAdapter(adapter);


        mYearMin.addTextChangedListener(EmptyString);
        mYearMax.addTextChangedListener(EmptyString);

        mSubmit.setEnabled(false);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YearMin = mYearMin.getText().toString().trim();
                YearMax = mYearMax.getText().toString().trim();
                YearMinInt = Integer.parseInt(YearMin);
                YearMaxInt = Integer.parseInt(YearMax);

                Genre1 = GenreSpinner1.getSelectedItem().toString();
                Genre2 = GenreSpinner2.getSelectedItem().toString();
                Genre3 = GenreSpinner3.getSelectedItem().toString();
                FilteredMovieLists = new ArrayList<>();

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

                try{
                    CSVReader reader = new CSVReader(new InputStreamReader(getResources().openRawResource(R.raw.movie_data)));
                    String [] nextline;

                    reader.readNext();
                    while ((nextline = reader.readNext()) != null){
                        if(
                                (nextline[5].contains(Genre1) || nextline[5].contains(Genre2) || nextline[5].contains(Genre3))
                                        && (Integer.parseInt(nextline[2]) >= YearMinInt && Integer.parseInt(nextline[2]) <= YearMaxInt)
                        ){
                            MovieList List = new MovieList();

                            List.setMovieImage(nextline[0]);
                            List.setMovieTitle(nextline[1]);
                            List.setReleaseYear(Integer.parseInt(nextline[2]));
                            List.setMovieGenre(nextline[5]);
                            List.setIMDBRate(Double.parseDouble(nextline[6]));
                            List.setMetaScore(nextline[8]);

                            List.setCertificate(nextline[3]);
                            List.setOverview(nextline[7]);
                            List.setDirector(nextline[9]);
                            List.setActor1(nextline[10]);
                            List.setActor2(nextline[11]);
                            List.setActor3(nextline[12]);
                            List.setActor4(nextline[13]);
                            List.setRuntime(nextline[4]);

                            FilteredMovieLists.add(List);
                        }else {
                            reader.readNext();
                        }


                    }
                }catch(Exception e){
                    e.printStackTrace();
                }

                if(FilteredMovieLists.size() == 0){
                    Toast.makeText(Recommendation1.this, "No result found. Please try other combination. ", Toast.LENGTH_SHORT).show();
                }
                else{
                    Random random = new Random();
                    int value = random.nextInt(FilteredMovieLists.size());
                    //Toast.makeText(Recommendation1.this, FilteredMovieLists.get(value).getMovieTitle(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Recommendation1.this, MovieDetailAdapter.class);
                    intent.putExtra("ImageInfo", FilteredMovieLists.get(value).getMovieImage());
                    intent.putExtra("TitleInfo", FilteredMovieLists.get(value).getMovieTitle());
                    intent.putExtra("YearInfo", FilteredMovieLists.get(value).getReleaseYear());
                    intent.putExtra("GenreInfo", FilteredMovieLists.get(value).getMovieGenre());
                    intent.putExtra("IMDBInfo", FilteredMovieLists.get(value).getIMDBRate());
                    intent.putExtra("MetaInfo", FilteredMovieLists.get(value).getMetaScore());

                    intent.putExtra("CertificateInfo", FilteredMovieLists.get(value).getCertificate());
                    intent.putExtra("OverviewInfo", FilteredMovieLists.get(value).getOverview());
                    intent.putExtra("DirectorInfo", FilteredMovieLists.get(value).getDirector());
                    intent.putExtra("Actor1Info", FilteredMovieLists.get(value).getActor1());
                    intent.putExtra("Actor2Info", FilteredMovieLists.get(value).getActor2());
                    intent.putExtra("Actor3Info", FilteredMovieLists.get(value).getActor3());
                    intent.putExtra("Actor4Info", FilteredMovieLists.get(value).getActor4());
                    intent.putExtra("RuntimeInfo", FilteredMovieLists.get(value).getRuntime());


                    startActivity(intent);
                }


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
                mSubmit.setEnabled(true);
            }else {
                mSubmit.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
