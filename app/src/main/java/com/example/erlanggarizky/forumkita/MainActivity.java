package com.example.erlanggarizky.forumkita;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
//    TabLayout pertanyaan;
    TabLayout jawab;
    TabLayout akun;
    ImageButton addpost;
    PagerViewAdapter pagerViewAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_main);

        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.grenn));

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        tabLayout = findViewById(R.id.tabs);
//        pertanyaan = findViewById(R.id.pertanyaan);
        jawab = findViewById(R.id.jawab);
        akun = findViewById(R.id.akun);
        addpost = findViewById(R.id.addpertanyaan);

        //add post
        addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddPostActivity.class));
            }
        });


        viewPager = findViewById(R.id.fragment_container);
        pagerViewAdapter = new PagerViewAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerViewAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout = findViewById(R.id.tabs);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 1){
                    toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.purplesoft));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.purplesoft));
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.purplesoft));
                    }
                }
                else{
                    toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.grenn));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.grenn));
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.grenn));
                    }
                }
//                else{
//                    toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.bluesky));
//                    tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.bluesky));
//                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//                        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.bluesky));
//                    }
//                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
