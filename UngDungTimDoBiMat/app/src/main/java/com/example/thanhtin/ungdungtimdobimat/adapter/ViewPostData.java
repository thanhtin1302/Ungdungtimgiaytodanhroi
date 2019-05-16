package com.example.thanhtin.ungdungtimdobimat.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.thanhtin.ungdungtimdobimat.Class.News;
import com.example.thanhtin.ungdungtimdobimat.Class.Users;
import com.example.thanhtin.ungdungtimdobimat.R;
import com.example.thanhtin.ungdungtimdobimat.fragments.Map;
import com.example.thanhtin.ungdungtimdobimat.fragments.MapPost;
import com.example.thanhtin.ungdungtimdobimat.fragments.UpdateNewsActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewPostData extends RecyclerView.Adapter<ViewPostData.MyViewHolder> {
    private List<News> listData;
    private Context context;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Double x = 0.0 , y = 0.0, a, b, m, n;


    public ViewPostData(List<News> listData, Context context) {

        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.custom_layout_post_item, viewGroup, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        final News item = listData.get(i);
//        myViewHolder.avatar.setImageResource(item.getImg());
//        myViewHolder.

        db.collection("users").document(item.getUserID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                final Users u = documentSnapshot.toObject(Users.class);

                myViewHolder.txtname.setText(u.getName());
                try {
                    Glide.with(context).load(u.getImage()).override(100).placeholder(R.drawable.ic_picture).into(myViewHolder.avatar);
                } catch (Exception ex) {};
                myViewHolder.txtdiachi.setText(item.getDistrict() + "-" + item.getCity()+"      "+item.getDatetime());
                myViewHolder.txttitle.setText(item.getTitle());
                myViewHolder.txtcontent.setText(item.getContent());
                myViewHolder.txtdatetime.setText(item.getCreated_at());
                myViewHolder.btnlienhe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "sdt " +u.getPhone(), Toast.LENGTH_LONG).show();
                        callPhoneNumber(u);
                    }
                });

                if (item.getImage().matches("")) {
                    myViewHolder.imagepost.setVisibility(View.GONE);

                } else {
                    Glide.with(context).load(item.getImage()).override(500, 500).into(myViewHolder.imagepost);
                }

                myViewHolder.btnvitri.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(item.getPost().matches(""))
                        {
                            Toast.makeText(context, "không tồn tại vị trí ", Toast.LENGTH_LONG).show();
                        }else {
                            Intent sendIntent = new Intent(context, MapPost.class);
                            sendIntent.putExtra("post", item.getPost());
                            context.startActivity(sendIntent);
                        }


                    }
                });


            }
        });
    }

    @Override
    public int getItemCount() {

        return listData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView avatar;
        private ImageView imagepost;
        private TextView txtname, txttitle, txtcontent, txtdiachi, txtdatetime;
        private Button btnvitri, btnlienhe;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            imagepost = itemView.findViewById(R.id.imagepost);
            txtname = itemView.findViewById(R.id.txtname);
            txttitle = itemView.findViewById(R.id.txttitle);
            txtcontent = itemView.findViewById(R.id.txtnoidung);
            txtdiachi = itemView.findViewById(R.id.txtdiachi);
            txtdatetime = itemView.findViewById(R.id.txtdatetime);
            btnlienhe = itemView.findViewById(R.id.btnlienhe);
            btnvitri = itemView.findViewById(R.id.btnvitri);


        }
    }

    private void callPhoneNumber(final Users users) {
        try {
            if (Build.VERSION.SDK_INT > 22) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn có chắc chắn gọi tới số " + users.getPhone() + " không?");
                builder.setCancelable(false);
                builder.setPositiveButton("Gọi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling

                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 101);

                            return;
                        }
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + users.getPhone()));
                        context.startActivity(callIntent);
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn có chắc chắn gọi tới số " + users.getPhone() + " không?");
                builder.setCancelable(false);
                builder.setPositiveButton("Gọi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + users.getPhone()));
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 101);

                            return;
                        }
                        context.startActivity(callIntent);
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
