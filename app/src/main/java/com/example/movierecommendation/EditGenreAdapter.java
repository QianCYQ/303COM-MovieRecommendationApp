package com.example.movierecommendation;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class EditGenreAdapter extends RecyclerView.Adapter<EditGenreAdapter.EditGenreHolder> {
    Context mContext;
    ArrayList<CheckboxModel> mCheckboxModel;


    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String mGetDocumentID;

    public EditGenreAdapter(ArrayList<CheckboxModel> Checkbox, Context context) {
        mCheckboxModel = Checkbox;
        mContext = context;

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mGetDocumentID = fAuth.getCurrentUser().getUid();
    }


    @NonNull
    @Override
    public EditGenreAdapter.EditGenreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.editgenre_adapter,parent,false);
        return new EditGenreHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditGenreAdapter.EditGenreHolder holder, int position) {
        CheckboxModel checkboxModel = mCheckboxModel.get(position);
        holder.mMovie_Genre.setText(checkboxModel.getGenre());
        holder.mEditGenreCheckbox.setChecked(checkboxModel.isSelected());


        fStore.collection("UserInfo").whereArrayContains("Users_Prefer_Genre",checkboxModel.getGenre())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            holder.mEditGenreCheckbox.setChecked(true);
                            checkboxModel.setSelected(true);
                        }

                    }
                }
            }
        });

        holder.mEditGenreCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.mEditGenreCheckbox.isChecked()){
                    holder.mEditGenreCheckbox.setChecked(true);
                    checkboxModel.setSelected(true);

                    DocumentReference documentReference = fStore.collection("UserInfo").document(mGetDocumentID);
                    documentReference.update("Users_Prefer_Genre", FieldValue.arrayUnion(checkboxModel.getGenre()));
                }
                else {
                    holder.mEditGenreCheckbox.setChecked(false);
                    checkboxModel.setSelected(false);

                    DocumentReference documentReference = fStore.collection("UserInfo").document(mGetDocumentID);
                    documentReference.update("Users_Prefer_Genre", FieldValue.arrayRemove(checkboxModel.getGenre()));
                }
            }
        });


//        holder.mEditGenreCheckbox.setOnCheckedChangeListener(null);
//
//        holder.mEditGenreCheckbox.setChecked(checkboxModel.isSelected());

    }

    @Override
    public int getItemCount() {
        return mCheckboxModel.size();
    }


    public static class EditGenreHolder extends RecyclerView.ViewHolder{

        private TextView mMovie_Genre;
        private CheckBox mEditGenreCheckbox;

        public EditGenreHolder(@NonNull View itemView) {
            super(itemView);

            mMovie_Genre = itemView.findViewById(R.id.Movie_Genre);
            mEditGenreCheckbox = itemView.findViewById(R.id.EditGenreCheckbox);



        }

    }


}
