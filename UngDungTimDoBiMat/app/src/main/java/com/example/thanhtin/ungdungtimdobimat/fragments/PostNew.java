package com.example.thanhtin.ungdungtimdobimat.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thanhtin.ungdungtimdobimat.Class.News;
import com.example.thanhtin.ungdungtimdobimat.Class.Users;
import com.example.thanhtin.ungdungtimdobimat.InfoUser;
import com.example.thanhtin.ungdungtimdobimat.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class PostNew extends AppCompatActivity {
    private static final int PICK_IMAGE = 101;
    private Spinner sploaitin,sptinhthanh;
    private  EditText ednoidung,edtieude,edquanhuyen,edvitri ;
    private Button btnthemanh,btnvitri,btndang;
    private TextView tvdatetime;
    private ImageView img_news, imgback;
    private ProgressBar progressBar;
    private DatePickerDialog.OnDateSetListener mDataSet;
    private FirebaseAuth mAuth ;
    private FirebaseFirestore mFirestore;
    private StorageReference mStorage;
    private DatePickerDialog.OnDateSetListener mData;


    private ProgressDialog progressDialog;
    private UploadTask uploadTask;
    private Uri imageUri;


    private News news;
    private Users users;
    private String userid,newsid ;
    private String arrayLoaiTin[]={"Đăng tin nhặt","Đăng tin tìm"};

    private String arrayCity[]={"An Giang","Bà Rịa - Vũng Tàu","Bắc Giang","Bắc Kạn","Bạc Liêu","Bắc Ninh","Bến Tre","Bình Định","Bình Dương","Bình Phước","Bình Thuận","Cà Mau","Cao Bằng","Đắk Lắk", "Đắk Nông", "Điện Biên", "Đồng Nai","Đồng Tháp"
            ,"Gia Lai", "Hà Giang",	"Hà Nam" ,"Hà Tĩnh","Hải Dương" ,"Hậu Giang", "Hòa Bình","Hưng Yên", "Khánh Hòa","Kiên Giang", "Kon Tum", "Lai Châu", "Lâm Đồng", "Lạng Sơn",
            "Lào Cai", "Long An", "Nam Định", "Nghệ An", "Ninh Bình", "Ninh Thuận", "Phú Thọ", "Quảng Bình","Quảng Nam", "Quảng Ngãi", "Quảng Ninh", "Quảng Trị", "Sóc Trăng", "Sơn La", "Tây Ninh", "Thái Bình", "Thái Nguyên",
            "Thanh Hóa", "Thừa Thiên Huế", "Tiền Giang", "Trà Vinh", "Tuyên Quang", "Vĩnh Long", "Vĩnh Phúc", "Yên Bái","Phú Yên",	"Cần Thơ", "Đà Nẵng", "Hải Phòng", "Hà Nội","TP HCM"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_post_new);
        imageUri = null;

        mStorage = FirebaseStorage.getInstance().getReference().child("images");
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        init();
        setcontrol();



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    private void setcontrol() {
        btnthemanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChoseImage();
            }
        });


        btndang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String uID = mAuth.getCurrentUser().getUid();
                if (edquanhuyen.getText().toString().matches("") || tvdatetime.getText().toString().matches("") || edtieude.getText().toString().matches("") || ednoidung.getText().toString().matches("")) {
                    Toast.makeText(PostNew.this, "Bạn cần nhập đầy đủ!",
                            Toast.LENGTH_SHORT).show();
                } else { if(imageUri != null)
                {
//                    btndang.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);

                    SimpleDateFormat fm = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    Date today0 = new Date();
                    final String create_at = fm.format(today0);
                    final StorageReference user_profile = mStorage.child(uID + create_at+ ".jpg");
                    user_profile.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()){
                                throw task.getException();
                            }
                            return user_profile.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()){

                                String path = task.getResult().toString();
                                String key=mFirestore.collection("News").document().getId();
                                int loaitin = 0;
                                if(sploaitin.getSelectedItem().toString().matches("Đăng tin tìm"))
                                {
                                    loaitin =1;

                                }

                                news = new News(sptinhthanh.getSelectedItem().toString(),
                                        edquanhuyen.getText().toString(),
                                        edtieude.getText().toString(),
                                        ednoidung.getText().toString(),
                                        path,
                                        edvitri.getText().toString(),
                                        uID,
                                        key,
                                        tvdatetime.getText().toString(),
                                        create_at
                                        ,loaitin);
                                mFirestore.collection("News").document(key).set(news).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        btndang.setVisibility(View.GONE);
                                        progressBar.setVisibility(View.INVISIBLE);
                                        finish();
                                    }
                                });
                            } else {
                                Toast.makeText(PostNew.this, "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                                btndang.setVisibility(View.GONE);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }else {

                    String key=mFirestore.collection("News").document().getId();
                    int loaitin = 0;
                    if(sploaitin.getSelectedItem().toString().matches("Đăng tin tìm"))
                    {
                        loaitin =1;

                    }
                    SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date today0 = new Date();
                    String create_at = fm.format(today0);
                    news = new News(sptinhthanh.getSelectedItem().toString(),
                            edquanhuyen.getText().toString(),
                            edtieude.getText().toString(),
                            ednoidung.getText().toString(),
                            "",
                            edvitri.getText().toString(),
                            uID,
                            key,
                            tvdatetime.getText().toString(),
                            create_at
                            ,loaitin);
                    mFirestore.collection("News").document(key).set(news).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
//                            btndang.setVisibility(View.GONE);
                            progressBar.setVisibility(View.INVISIBLE);
                            finish();
                        }
                    });

                }
                }
            }
        });
        tvdatetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year1 = cal.get(Calendar.YEAR);
                int month1 = cal.get(Calendar.MONTH);
                int day1 = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        PostNew.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        mData,
                        year1,month1,day1);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                mData = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year1, int month1, int day1) {
                        month1 = month1 + 1;
                        String datetime = day1 + "/" + month1 + "/" + year1;
                        tvdatetime.setText(datetime);
                    }
                };



            }
        });
        btnvitri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostNew.this,Map.class);
                intent.putExtra("Map","ok");
                startActivity(intent);
            }
        });
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void ChoseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && data != null){
            imageUri = data.getData();
            Toast.makeText(this, imageUri.toString(), Toast.LENGTH_LONG);
            img_news.setImageURI(imageUri);
            img_news.setVisibility(View.VISIBLE);
        }
    }



    private void init() {

            sploaitin = findViewById(R.id.spiner_loaitin);
            sptinhthanh = findViewById(R.id.spiner_TinhThanh);
            edquanhuyen = findViewById(R.id.edQuanHuyen);
            ednoidung = findViewById(R.id.ednoidung);
            edtieude = findViewById(R.id.edtieude);
            tvdatetime = findViewById(R.id.tvdatetime);
            btnthemanh = findViewById(R.id.btnthemanh);
            btnvitri = findViewById(R.id.btnvitri);
            btndang = findViewById(R.id.btndang);
            imgback = findViewById(R.id.update_seller_btn_back_map);
            img_news = findViewById(R.id.imgnews);
            edvitri = findViewById(R.id.edvitri);
            progressBar = findViewById(R.id.progressBar);

        SimpleDateFormat fm = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        Date today = new Date();
        String create_at = fm.format(today);

            //cau hinh Spiner
        final ArrayAdapter adapterC = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arrayCity);
        adapterC.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        sptinhthanh.setAdapter(adapterC);
        sptinhthanh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg, int arg2, long arg3) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final ArrayAdapter adapterL = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arrayLoaiTin);
        adapterL.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        sploaitin.setAdapter(adapterL);
        sploaitin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg, int arg2, long arg3) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }
//


    @Override
    protected void onResume() {

        super.onResume();
       String vitri = getIntent().getStringExtra("address");
        edvitri.setText(vitri);
    }
}
