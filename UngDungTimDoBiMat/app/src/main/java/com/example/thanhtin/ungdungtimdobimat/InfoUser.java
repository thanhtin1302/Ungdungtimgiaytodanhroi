package com.example.thanhtin.ungdungtimdobimat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thanhtin.ungdungtimdobimat.Class.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class InfoUser extends AppCompatActivity {
    private TextView tvhoten,tvngaysinh,tvsdt,tvemail,tvdiachi;
    private Button btnhoten,btnngaysinh,btnsdt,btndiachi,btnemail,btnhuy,btncapnhat;
    private ImageView btnback;
    private EditText edhoten,edngaysinh,edsdt,edemail;
    private int checkname ,checksdt, checkngaysinh =0;
    private DatePickerDialog.OnDateSetListener mDataSet;


    String vitri,name,ngaysinh,sdt,email;

    private FirebaseAuth mAuth ;
    private FirebaseFirestore db;

    String userid ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);

        init();

        userid = mAuth.getCurrentUser().getUid();
        setControl();
        loadData();


    }

    private void loadData() {
        db.collection("users").document(userid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                Users us = documentSnapshot.toObject(Users.class);
                if(us!=null)
                {
                    tvhoten.setText(us.getName());
                    tvngaysinh.setText(us.getBirthday());
                    tvsdt.setText(us.getPhone()+"");
                    if(vitri == null)
                        tvdiachi.setText(us.getAddress());

                }

            }
        });
    }

    private void setControl() {

        btncapnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vitri != null){
                    db.collection("users").document(userid).update("address",vitri).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(InfoUser.this,"lưu thành công",Toast.LENGTH_LONG).show();
                            finish();

                        }
                    });
                }
                else {
                    Toast.makeText(InfoUser.this, "Dia chi khong thay doi!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        btnhoten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkname==0)
                {
                    edhoten.setVisibility(View.VISIBLE);
                    tvhoten.setVisibility(View.GONE);
                    edhoten.setText(tvhoten.getText());
                    checkname = 1;
                    btnhoten.setText("lưu");
                }
                else{
                    tvhoten.setVisibility(View.VISIBLE);
                    edhoten.setVisibility(View.GONE);
                    tvhoten.setText(edhoten.getText());
                    checkname=0;
                    db.collection("users").document(userid).update("name", tvhoten.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(InfoUser.this,"lưu thành công",Toast.LENGTH_LONG).show();
                        }
                    });

                    btnhoten.setText("...");
                }

            }
        });
        btnsdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checksdt==0)
                {
                    edsdt.setVisibility(View.VISIBLE);
                    tvsdt.setVisibility(View.GONE);
                    edsdt.setText(tvsdt.getText());
                    checksdt = 1;
                    btnsdt.setText("lưu");
                }
                else{
                    tvsdt.setVisibility(View.VISIBLE);
                    edsdt.setVisibility(View.GONE);
                    tvsdt.setText(edsdt.getText());
                    checksdt=0;
                    db.collection("users").document(userid).update("phone",edsdt.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(InfoUser.this,"lưu thành công",Toast.LENGTH_LONG).show();
                        }
                    });

                    btnsdt.setText("...");
                }
            }
        });
            btnngaysinh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(
                            InfoUser.this,
                            android.R.style.Theme_Holo_Dialog_MinWidth,
                            mDataSet,
                            year,month,day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                    mDataSet = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            month = month + 1;
                            String date = day + "/" + month + "/" + year;
                            tvngaysinh.setText(date);
                            db.collection("users").document(userid).update("birthday",date).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(InfoUser.this,"lưu thành công",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    };
                }
            });

    }



    private void init() {

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        name = mAuth.getCurrentUser().getDisplayName();
        email = mAuth.getCurrentUser().getEmail();
        sdt = mAuth.getCurrentUser().getPhoneNumber();


        tvhoten = findViewById(R.id.tvhoten);
        tvdiachi = findViewById(R.id.tvdiachi);
        tvemail = findViewById(R.id.tvemail);
        tvsdt = findViewById(R.id.tvsdt);
        tvngaysinh = findViewById(R.id.tvngaysinh);

        edhoten = findViewById(R.id.edhoten);
        edsdt = findViewById(R.id.edsdt);

        btnngaysinh = findViewById(R.id.btnNgaysinh);
        btnsdt = findViewById(R.id.btnSdt);
        btnemail = findViewById(R.id.btnEmail);
        btnhoten = findViewById(R.id.btnHoten);
        btnback = findViewById(R.id.btnback);
        btncapnhat = findViewById(R.id.btnCapnhat);
        btndiachi = findViewById(R.id.btnDiachi);
        btndiachi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InfoUser.this, com.example.thanhtin.ungdungtimdobimat.fragments.Map.class));
            }
        });
        btnhuy = findViewById(R.id.btnHuy);
        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        vitri = getIntent().getStringExtra("address");
        tvdiachi.setText(vitri);

        tvhoten.setText(name);
        tvemail.setText(email);
        tvsdt.setText(sdt);
    }
}
