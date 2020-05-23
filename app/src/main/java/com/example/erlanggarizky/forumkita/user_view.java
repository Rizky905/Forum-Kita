package com.example.erlanggarizky.forumkita;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class user_view extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterUserView adapterUserView;
    List<ModelUser> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);

        getWindow().setStatusBarColor(ContextCompat.getColor(user_view.this, R.color.purplesoft));

        //init recycleview
        recyclerView = (RecyclerView) findViewById(R.id.users_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //set its properties
        recyclerView.setHasFixedSize(true);


        //init userlist
        userList = new ArrayList<>();

        getAllUsers();


    }

    private void getAllUsers() {
        //get current user
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

        //get path database
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("USERS");

        //GET ALL DATA FROM PATH
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelUser modelUser = ds.getValue(ModelUser.class);

                    //get all user except current sign in user
                    if (!modelUser.getUid().equals(fUser.getUid())){
                        userList.add(modelUser);
                    }

                    //adapter
                    adapterUserView = new AdapterUserView(getBaseContext(), userList);
//                    adapterUserView = new AdapterUserView();

                    //set adapter to recycler view
                    recyclerView.setAdapter(adapterUserView);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
