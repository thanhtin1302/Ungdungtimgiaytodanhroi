package com.example.thanhtin.ungdungtimdobimat.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.thanhtin.ungdungtimdobimat.Class.News;
import com.example.thanhtin.ungdungtimdobimat.R;
import com.example.thanhtin.ungdungtimdobimat.adapter.ViewPostData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class TimDoDanhRoi extends Fragment {
    View view;
    private RecyclerView rcv;
    private List<News> lData;
    private ViewPostData adapter;
    private FirebaseAuth mAuth ;
    private FirebaseFirestore db;

    public TimDoDanhRoi() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tim_do_danh_roi, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        lData = new ArrayList<>();

        adapter = new ViewPostData(lData, getContext());

        rcv = view.findViewById(R.id.rcv_tim);
        rcv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rcv.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        loadData();
    }

    private void loadData() {


        db.collection("News").whereEqualTo("type",0).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots!=null)
                {
                    lData.clear();
                    for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges())
                    {
                        News n = doc.getDocument().toObject(News.class);
                        lData.add(n);

                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
