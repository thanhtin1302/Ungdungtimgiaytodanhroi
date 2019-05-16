package com.example.thanhtin.ungdungtimdobimat.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.thanhtin.ungdungtimdobimat.Class.News;
import com.example.thanhtin.ungdungtimdobimat.Class.Users;
import com.example.thanhtin.ungdungtimdobimat.R;
import com.example.thanhtin.ungdungtimdobimat.fragments.PostNew;
import com.example.thanhtin.ungdungtimdobimat.fragments.UpdateNewsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.List;


public class ViewManagerNews extends RecyclerView.Adapter<ViewManagerNews.MyViewHolder> {
    private List<News> listData;
    private Context context;
    private FirebaseFirestore db;





    public ViewManagerNews(List<News> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.custom_layout_manager, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        db = FirebaseFirestore.getInstance();
        final News item = listData.get(i);
                    String t =  item.getTitle();
                    if(t.length()>20)
                    {
                        myViewHolder.mntitle.setText(t.substring(0,15)+"...");
                    }else {
                        myViewHolder.mntitle.setText(item.getTitle());
                    }


                        String q =  item.getContent();
                        if(q.length()>20)
                        {
                            myViewHolder.mncontent.setText(q.substring(0,15)+"...");
                        }else {
                            myViewHolder.mncontent.setText(item.getTitle());
                        }
                    myViewHolder.mndatetime.setText(item.getCreated_at());
                    if (item.getImage().matches("")) {
                        myViewHolder.mnimage.setImageResource(R.drawable.ic_picture);

                    } else {
                        Glide.with(context).load(item.getImage()).override(100).into(myViewHolder.mnimage);
                    }

                    myViewHolder.mndelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Thông báo");
                            builder.setMessage("Bạn có chắc chắn muốn xoá bài viết không?");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i1) {

                                    db.collection("News").document(item.getNewsID()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(context,"Bạn đã xoá",Toast.LENGTH_LONG).show();
                                                listData.remove(i);
                                                notifyItemRemoved(i);
                                                notifyItemRangeChanged(i,listData.size());
                                                notifyDataSetChanged();
                                            }
                                        }
                                    });
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
                    });
                    myViewHolder.mn_linear_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context,UpdateNewsActivity.class);
                            intent.putExtra("pass", item);
                            context.startActivity(intent);
                        }
                    });


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mntitle,mncontent,mndatetime;
        private ImageView mnimage,mndelete,mnback;
        private LinearLayout mn_linear_layout;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mntitle = itemView.findViewById(R.id.mntitle);
            mncontent = itemView.findViewById(R.id.mncontent);
            mndatetime = itemView.findViewById(R.id.mndatetime);
            mnimage = itemView.findViewById(R.id.mnimg);
            mndelete = itemView.findViewById(R.id.imgdelete);
            mn_linear_layout = itemView.findViewById(R.id.mn_linear_layout);

        }
    }
}
