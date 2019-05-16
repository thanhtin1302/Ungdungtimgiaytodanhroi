package com.example.thanhtin.ungdungtimdobimat.fragments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.thanhtin.ungdungtimdobimat.Class.News;
import com.example.thanhtin.ungdungtimdobimat.Class.Users;
import com.example.thanhtin.ungdungtimdobimat.R;
import com.example.thanhtin.ungdungtimdobimat.adapter.ViewManagerNews;
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

public class ManagerEditNews extends AppCompatActivity {

    private RecyclerView mnrcv;
    private List<News> mnlData;
    private ViewManagerNews mnadapter;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ImageView mnback;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_edit_news);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getCurrentUser().getUid();
        mnback = findViewById(R.id.mnupdate_seller_btn_back_manager);


        mnlData = new ArrayList<>();

        mnadapter = new ViewManagerNews(mnlData, this);

        mnrcv = findViewById(R.id.rcv_manager);
        mnrcv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mnrcv.setAdapter(mnadapter);
        mnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {


        db.collection("News").whereEqualTo("userID",userid).orderBy("created_at",Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots!=null)
                {
                    mnlData.clear();
                    for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges())
                    {
                        News n = doc.getDocument().toObject(News.class);
                        mnlData.add(n);

                    }
                    mnadapter.notifyDataSetChanged();
                }
            }
        });
    }
}
