package com.example.movierecommendation;

import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.ColumnPositionMappingStrategyBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.HeaderColumnNameMappingStrategyBuilder;
import com.opencsv.bean.StatefulBeanToCsv;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieListFragment extends Fragment implements MovieDetail{
    private RecyclerView recyclerView;
    private ArrayList<MovieList> movieLists;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        recyclerView = view.findViewById(R.id.Movierecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        movieLists = new ArrayList<>();

        //MovieList ob1 = new MovieList("https://m.media-amazon.com/images/M/MV5BMDFkYTc0MGEtZmNhMC00ZDIzLWFmNTEtODM1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_UX67_CR0,0,67,98_AL_.jpg", 1990, "a", 12, "34.2", "horror" );
        //movieLists.add(ob1);
        readMovieData();

        recyclerView.setAdapter(new MovieListAdapter(movieLists, this));
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    private void readMovieData(){
        try{
            CSVReader reader = new CSVReader(new InputStreamReader(getResources().openRawResource(R.raw.movie_data)));
            String [] nextline;

            //skip header (first row of csv file)
            reader.readNext();
            while ((nextline = reader.readNext()) != null){
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

                movieLists.add(List);
            }
            reader.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), MovieDetailAdapter.class);
        intent.putExtra("ImageInfo", movieLists.get(position).getMovieImage());
        intent.putExtra("TitleInfo", movieLists.get(position).getMovieTitle());
        intent.putExtra("YearInfo", movieLists.get(position).getReleaseYear());
        intent.putExtra("GenreInfo", movieLists.get(position).getMovieGenre());
        intent.putExtra("IMDBInfo", movieLists.get(position).getIMDBRate());
        intent.putExtra("MetaInfo", movieLists.get(position).getMetaScore());

        intent.putExtra("CertificateInfo", movieLists.get(position).getCertificate());
        intent.putExtra("OverviewInfo", movieLists.get(position).getOverview());
        intent.putExtra("DirectorInfo", movieLists.get(position).getDirector());
        intent.putExtra("Actor1Info", movieLists.get(position).getActor1());
        intent.putExtra("Actor2Info", movieLists.get(position).getActor2());
        intent.putExtra("Actor3Info", movieLists.get(position).getActor3());
        intent.putExtra("Actor4Info", movieLists.get(position).getActor4());
        intent.putExtra("RuntimeInfo", movieLists.get(position).getRuntime());


        startActivity(intent);
    }












    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MovieListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MovieListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieListFragment newInstance(String param1, String param2) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


}