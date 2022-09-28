package com.example.movierecommendation;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.opencsv.CSVReader;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyWatchlist extends AppCompatActivity {
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String mGetDocumentID;

    private RecyclerView recyclerView;
    private ArrayList<WatchlistDB> mWatchlistDB;

    private MyWatchlistAdapter mMyWatchlistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mywatclist);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mGetDocumentID = fAuth.getCurrentUser().getUid();

        recyclerView = findViewById(R.id.MyWatchlistRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mWatchlistDB = new ArrayList<>();

        mMyWatchlistAdapter = new MyWatchlistAdapter(mWatchlistDB, MyWatchlist.this);

        getWatchlistDB();

        recyclerView.setAdapter(mMyWatchlistAdapter);
    }

    private void getWatchlistDB() {
        fStore.collection("UserInfo").whereEqualTo(FieldPath.documentId(), mGetDocumentID)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            //List<Map<String, Object>> watchlist = (List<Map<String, Object>>) document.get("Watchlist");

                            ArrayList<String> watchlist = (ArrayList<String>)document.get("Watchlist");
                            String visibleString = null;
                            for( int i = 0 ; i < watchlist.size() ; i++) {
                                    visibleString = watchlist.get(i).toString();
                                WatchlistDB watchlistDB = new WatchlistDB(visibleString);
                                mWatchlistDB.add(watchlistDB);
                            }

//                            WatchlistDB watchlistDB = new WatchlistDB(watchlist);
//                            mWatchlistDB.add(watchlistDB);
                        }
                        mMyWatchlistAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
