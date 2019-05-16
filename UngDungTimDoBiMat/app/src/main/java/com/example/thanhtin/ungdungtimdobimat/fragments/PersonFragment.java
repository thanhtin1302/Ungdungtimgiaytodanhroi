package com.example.thanhtin.ungdungtimdobimat.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.thanhtin.ungdungtimdobimat.Class.Users;
import com.example.thanhtin.ungdungtimdobimat.InfoUser;
import com.example.thanhtin.ungdungtimdobimat.R;
import com.example.thanhtin.ungdungtimdobimat.Signin;
import com.example.thanhtin.ungdungtimdobimat.adapter.ViewManagerNews;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonFragment extends Fragment {

    View view;
    private TextView txthoten,txtemail;
    private LinearLayout editInfo,txtbaidang;
    private Button btndangxuat;
    private RecyclerView rcv;
    private FirebaseAuth mAuth ;
    private FirebaseFirestore db;
    private Users users;
    private CircleImageView img;
    private GoogleSignInClient mGoogleSignInClient;
    String userid ,image;

    public PersonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_person, container, false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getCurrentUser().getUid();
        image = mAuth.getCurrentUser().getPhotoUrl().toString();
        img = view.findViewById(R.id.profile_image);
        editInfo = view.findViewById(R.id.editInfo);
        txthoten = view.findViewById(R.id.txtHoten);
        txtemail = view.findViewById(R.id.txtEmail);
        txtbaidang = view.findViewById(R.id.txtbaidang);

        txtbaidang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),ManagerEditNews.class));
            }
        });
        editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),InfoUser.class));
            }
        });
        loadData();
        btndangxuat = view.findViewById(R.id.btndangxuat);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1065398276824-0v30p33shfrph24bm5p9kaornf9v8cj2.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        btndangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }
    private void signOut() {

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mAuth.signOut();
                        startActivity( new Intent(getActivity(), Signin.class));
                        getActivity().finish();
                    }
                });
    }
    private void loadData() {
        db.collection("users").document(userid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                Users us = documentSnapshot.toObject(Users.class);
                if(us.getName() != null){
                    txthoten.setText(us.getName());
                    txtemail.setText(us.getEmail());
                }else {
                    String ten, mail;
                    ten = mAuth.getCurrentUser().getDisplayName();
                    mail = mAuth.getCurrentUser().getEmail();
                    txthoten.setText(ten);
                    txtemail.setText(mail);
                }
                Glide.with(getContext()).load(image).into(img);
            }
        });
    }
}
