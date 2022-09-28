package com.example.movierecommendation;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;

    private String Username, Email, Password, mGetDocumentID;
    private EditText mUsername, mEmail, mPassword;
    private Button mBtnRegister;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_register, container, false);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        mUsername = view.findViewById(R.id.SignUpusername);
        mEmail = view.findViewById(R.id.SignUpemail);
        mPassword = view.findViewById(R.id.SignUppassword);
        mBtnRegister = view.findViewById(R.id.BtnRegister);

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Username = mUsername.getText().toString().trim();
                Email = mEmail.getText().toString().trim();
                Password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(Username)){
                    mUsername.setError("Username cannot be empty.");
                    mUsername.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(Email)){
                    mEmail.setError("Email cannot be empty.");
                    mEmail.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(Password)){
                    mPassword.setError("Password cannot be empty.");
                    mPassword.requestFocus();
                    return;
                }

                if(Password.length() < 6) {
                    mPassword.setError("Minimum password length is 6 characters");
                    mPassword.requestFocus();
                    return;
                }


                fAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            mGetDocumentID = fAuth.getCurrentUser().getUid();
                            Toast.makeText(getContext(), "New Account was created.", Toast.LENGTH_SHORT).show();
                            DocumentReference documentReference = fStore.collection("UserInfo").document(mGetDocumentID);
                            Map<String, Object> UserInfo = new HashMap<>();
                            UserInfo.put("Username", Username);
                            UserInfo.put("Email", Email);

                            UserInfo.put("Watchlist", Arrays.asList());
                            UserInfo.put("Users_Prefer_Genre", Arrays.asList("Drama"));
                            UserInfo.put("User_Preference_Year_Max", 2020);
                            UserInfo.put("User_Preference_Year_Min", 1920);

                            documentReference.set(UserInfo);



                            Intent intent = new Intent(getActivity(), HomePage.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getContext(), "Error occur. Please try again." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        return view;
    }




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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