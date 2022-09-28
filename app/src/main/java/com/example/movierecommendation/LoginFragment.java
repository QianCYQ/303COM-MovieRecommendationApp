package com.example.movierecommendation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class LoginFragment extends Fragment {

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;

    private String Email, Password, UserID;
    private EditText mEmail, mPassword;
    private Button mBtnLogin;
    private TextView mForgetPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //UserID = fAuth.getCurrentUser().getUid();

        mEmail = view.findViewById(R.id.Loginemail);
        mPassword = view.findViewById(R.id.Loginpassword);
        mBtnLogin = view.findViewById(R.id.LoginBtn);
        mForgetPassword = view.findViewById(R.id.TextForgetPassword);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email = mEmail.getText().toString().trim();
                Password = mPassword.getText().toString().trim();

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

                fAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //DocumentReference documentReference = fStore.collection("UserInfo").document();
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "Login Successful.", Toast.LENGTH_SHORT).show();

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

        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ResetPassword = new EditText(view.getContext());
                AlertDialog.Builder ResetEmailPassword = new AlertDialog.Builder(view.getContext());
                ResetEmailPassword.setTitle("Enter your email to reset password.");
                ResetEmailPassword.setMessage("You will receive a reset password link from your email.");
                ResetEmailPassword.setView(ResetPassword);

                ResetEmailPassword.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String userEmail = ResetPassword.getText().toString().trim();
                        fAuth.sendPasswordResetEmail(userEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(view.getContext(), "Please check your email to reset the password", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Error occur. Please try again." + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                ResetEmailPassword.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(view.getContext(), "The action was cancelled.", Toast.LENGTH_SHORT).show();
                    }
                });

                ResetEmailPassword.create().show();
            }
        });

        return view;
    }



}

