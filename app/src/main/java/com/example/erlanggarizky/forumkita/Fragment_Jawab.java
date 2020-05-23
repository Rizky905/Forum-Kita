package com.example.erlanggarizky.forumkita;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Jawab extends Fragment {

    FirebaseAuth firebaseAuth;

    RecyclerView recyclerView;
    List<ModelPost>postList;
    AdapterPosts adapterPosts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_jawab, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        //recycleview
        recyclerView = view.findViewById(R.id.postRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //show newest post first, for this load from list
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        //SET layout to recycle view
        recyclerView.setLayoutManager(layoutManager);


        //init post list
        postList = new ArrayList<>();

        loadpost();

        return view;
    }

    private void loadpost() {
        //path of all post
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelPost modelPost = ds.getValue(ModelPost.class);
                    postList.add(modelPost);
                    //adapter
                    adapterPosts = new AdapterPosts(getActivity(), postList);
                    //set adapater
                    recyclerView.setAdapter(adapterPosts);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //in case of error
                Toast.makeText(getActivity(),""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void search(String searchQuery){

    }


}
