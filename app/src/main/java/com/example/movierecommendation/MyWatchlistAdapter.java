 package com.example.movierecommendation;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

 public class MyWatchlistAdapter extends RecyclerView.Adapter<MyWatchlistAdapter.MyWatchlistHolder> {
    ArrayList<WatchlistDB> mWatchlistDB;
    Context mContext;

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String mGetDocumentID;
    Interpreter tflite;

    public MyWatchlistAdapter(ArrayList<WatchlistDB> WatchlistDB, Context context) {
        this.mWatchlistDB = WatchlistDB;
        this.mContext = context;

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mGetDocumentID = fAuth.getCurrentUser().getUid();
    }


    @NonNull
    @Override
    public MyWatchlistAdapter.MyWatchlistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mywatchlist_adapter,parent,false);
        return new MyWatchlistHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyWatchlistAdapter.MyWatchlistHolder holder, int position) {
        holder.mMovie_Title.setText(mWatchlistDB.get(position).getWatchlistMovie());


        holder.mMyWatchlistOptionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, holder.mMyWatchlistOptionMenu);

                popupMenu.getMenuInflater().inflate(R.menu.option_menu,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.OptionRemove:
                                //handle menu1 click
                                String movieTitle = mWatchlistDB.get(position).getWatchlistMovie();

                                fStore.collection("UserInfo").whereArrayContains("Watchlist",movieTitle)
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentReference documentReference = fStore.collection("UserInfo").document(mGetDocumentID);
                                            documentReference.update("Watchlist", FieldValue.arrayRemove(movieTitle));

                                            mWatchlistDB.remove(position);
                                            notifyDataSetChanged();
                                            Toast.makeText(mContext, "The movie was removed from your watchlist", Toast.LENGTH_SHORT).show();

                                        }
                                        else{
                                            Toast.makeText(mContext, "Error Occurred", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                                return true;
                            case R.id.OptionRecommendation:
                                //handle menu2 click


//                                try{
//                                    tflite = new Interpreter(loadModelFile());
//                                } catch (Exception ex){
//                                    ex.printStackTrace();
//                                }
//
//                                String movieTitle2 = mWatchlistDB.get(position).getWatchlistMovie();
//                                Toast.makeText(mContext, Inference("movieTitle2"), Toast.LENGTH_SHORT).show();
                                Toast.makeText(mContext, "This feature is temporary unavailable.", Toast.LENGTH_SHORT).show();


                                return true;

                            default:
                                return false;
                        }
                    }
                });

                popupMenu.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return mWatchlistDB.size();
    }

    public static class MyWatchlistHolder extends RecyclerView.ViewHolder{
        private TextView mMovie_Title;
        private ImageView mMyWatchlistOptionMenu;

        public MyWatchlistHolder(@NonNull View itemView) {
            super(itemView);

            mMovie_Title = itemView.findViewById(R.id.Movie_Title2);
            mMyWatchlistOptionMenu = itemView.findViewById(R.id.MyWatchlistOptionMenu);

        }
    }

    private MappedByteBuffer loadModelFile() throws IOException{
        AssetFileDescriptor fileDescriptor = mContext.getAssets().openFd("SimilarityModel.tflite");
        FileInputStream fileInputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = fileInputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public String Inference(String s){
//        String[] inputValue = new String[1];
//        inputValue[0] = String.valueOf(s);
//
//        String[][] outputValue = new String[1][1];
        String[] input = {"test"};  // Input tensor shape is [1].
        //String[][] output = new String[1][10];  // Output tensor shape is [3, 2].
        Map<Integer, Object> output = new HashMap<>();
        output.put(1, 10);
        tflite.run(input,output);
        String inferedValue = output.toString();
        return inferedValue;

    }

}
