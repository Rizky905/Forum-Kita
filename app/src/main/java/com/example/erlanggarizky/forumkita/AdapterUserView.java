package com.example.erlanggarizky.forumkita;

import com.google.firebase.database.ValueEventListener;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterUserView extends RecyclerView.Adapter<AdapterUserView.MyHolder> {

    Context context;
    List<ModelUser> userList;



    //Constructor
    public AdapterUserView(Context context, List<ModelUser> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflate layout(row_user)
        View view = LayoutInflater.from(context).inflate(R.layout.row_users, viewGroup, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        //get data
        String userImage = userList.get(i).getImage();
        final String userName = userList.get(i).getName();
        String userJurusan = userList.get(i).getJurusan();
        String userUniversitas = userList.get(i).getUniversitas();

        //setdata
        myHolder.mName.setText(userName);
        myHolder.mJurusan.setText(userJurusan);
        myHolder.mUniversitas.setText(userUniversitas);
        try {
            Picasso.get().load(userImage)
                    .placeholder(R.drawable.user_acc_ic)
                    .into(myHolder.mFotoprofile);

        }catch (Exception e){

        }

        //handler iteem click
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ""+userName, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    //View holder vlass
    class MyHolder extends RecyclerView.ViewHolder{

        ImageView mFotoprofile;
        TextView mName, mUniversitas,mJurusan;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //init views
            mFotoprofile = itemView.findViewById(R.id.fotoprofile);
            mName = itemView.findViewById(R.id.namaprofile);
            mUniversitas = itemView.findViewById(R.id.universitas);
            mJurusan = itemView.findViewById(R.id.jurusan);
        }
    }


}
