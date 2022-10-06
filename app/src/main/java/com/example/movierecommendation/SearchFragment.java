package com.example.movierecommendation;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.opencsv.CSVReader;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String mGetDocumentID;
    private int YearMinInt, YearMaxInt;

    private CardView mCardViewRecommendation1, mCardViewRecommendation2;

    private ArrayList<MovieList> FilteredMovieLists;
    private String Genre1, Genre2, Genre3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search, container, false);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mGetDocumentID = fAuth.getCurrentUser().getUid();
        FilteredMovieLists = new ArrayList<>();


        mCardViewRecommendation1 = view.findViewById(R.id.Recommendation1);
        mCardViewRecommendation2 = view.findViewById(R.id.Recommendation2);

        mCardViewRecommendation2.setVisibility(view.INVISIBLE);
        loadUserInfo();
        mCardViewRecommendation2.setVisibility(view.VISIBLE);

        mCardViewRecommendation1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),Recommendation1.class);
                startActivity(intent);
            }
        });

        mCardViewRecommendation2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUserInfo();
                getRecommendation();
            }
        });



        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        loadUserInfo();
    }

    private void loadUserInfo(){
        fStore.collection("UserInfo").whereEqualTo(FieldPath.documentId(), mGetDocumentID)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    if (document.exists()) {
                        ArrayList<String> GenreList = (ArrayList<String>) document.get("Users_Prefer_Genre");
                        YearMinInt = document.getLong("User_Preference_Year_Min").intValue();
                        YearMaxInt = document.getLong("User_Preference_Year_Max").intValue();

                        if(GenreList.size() == 0){
                            mCardViewRecommendation2.setVisibility(View.INVISIBLE);
                        }
                        else{
                            mCardViewRecommendation2.setVisibility(View.VISIBLE);
                            Random random = new Random();
                            int number = random.nextInt(GenreList.size());
                            int number2 = random.nextInt(GenreList.size());
                            int number3 = random.nextInt(GenreList.size());
                            Genre1 = GenreList.get(number);
                            Genre2 = GenreList.get(number2);
                            Genre3 = GenreList.get(number3);
                        }

                    }
                }
            }
        });

//        fStore.collection("UserInfo").whereEqualTo(FieldPath.documentId(), mGetDocumentID)
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    for (DocumentSnapshot document : task.getResult()) {
//                        if (document.exists()) {
//                            ArrayList<String> GenreList = (ArrayList<String>) document.get("Users_Prefer_Genre");
//                            YearMinInt = document.getLong("User_Preference_Year_Min").intValue();
//                            YearMaxInt = document.getLong("User_Preference_Year_Max").intValue();
//
//                            Random random = new Random();
//                            int number = random.nextInt(GenreList.size());
//                            int number2 = random.nextInt(GenreList.size());
//                            int number3 = random.nextInt(GenreList.size());
//                            Genre1 = GenreList.get(number);
//                            Genre2 = GenreList.get(number2);
//                            Genre3 = GenreList.get(number3);
//                        }
//                    }
//                }
//            }
//        });


    }

    private void getRecommendation(){

        //Toast.makeText(getContext(), Genre1+" "+Genre2+" "+Genre3+" "+YearMaxInt+" "+YearMinInt, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), "No result found. Please try other combination. ", Toast.LENGTH_SHORT).show();
        }
        else{
            Random random = new Random();
            int value = random.nextInt(FilteredMovieLists.size());
            //Toast.makeText(Recommendation1.this, FilteredMovieLists.get(value).getMovieTitle(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getContext(), MovieDetailAdapter.class);
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















    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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