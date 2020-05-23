package com.example.erlanggarizky.forumkita;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class Fragment_Pertanyaan extends Fragment {

    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pertanyaan, null);
//
//        //Button
//        ImageButton addPost = (ImageButton) v.findViewById(R.id.add_pertanyaan);
//
//        addPost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent myIntent = new Intent(Fragment_Pertanyaan.this.getActivity(), AddPostActivity.class);
//                startActivity(myIntent);
//            }
//        });

        return v;
    }
}
