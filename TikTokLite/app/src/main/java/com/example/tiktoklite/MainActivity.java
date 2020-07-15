package com.example.tiktoklite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Fragment> list;
    private ViewPager viewPager;
    private mFragmentPagerAdapter Adapter;
    private SlidingTabLayout  mTabLayout;
    private String[] mTitlesArrays ={"广场","我的","主页"};
    private PlaceholderFragmentWorld mPlaceholderFragmentWorld;
    private PlaceholderFragmentHome mPlaceholderFragmentHome;
    private PlaceholderFragmentInfo mPlaceholderFragmentInfo;

    private ImageButton btn_post, btn_file, btn_rec, btn_cancel;
    FrameLayout postMethod;
    private AnimatorSet animatorSet;
    private ImageView plain;


    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new ArrayList<>();
        list.add(new PlaceholderFragmentWorld());
        list.add(new PlaceholderFragmentHome());
        list.add(new PlaceholderFragmentInfo());
        Adapter = new mFragmentPagerAdapter(getSupportFragmentManager(), list);
        viewPager = (ViewPager) findViewById(R.id.viewpages);
        viewPager.setAdapter(Adapter);
        viewPager.setCurrentItem(0);
        btn_post = findViewById(R.id.btn_post);
        btn_file = findViewById(R.id.btn_file);
        btn_rec = findViewById(R.id.btn_rec);
        btn_cancel = findViewById(R.id.btn_cancel);
        plain = findViewById(R.id.plain);
        postMethod = findViewById(R.id.post_method);
        postMethod.setVisibility(View.INVISIBLE);
        mTabLayout = (SlidingTabLayout)findViewById(R.id.tablayout);
        mTabLayout.setViewPager(viewPager, mTitlesArrays);




        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_post.setClickable(false);
                btn_cancel.setClickable(false);
                postMethod.setVisibility(View.VISIBLE);
                postMethod.setAlpha((float)0.0);
                ObjectAnimator rotate = ObjectAnimator.ofFloat(btn_post, "rotation", 0f, 45f).setDuration(700);
                rotate.setInterpolator(new BounceInterpolator());
                ObjectAnimator fade = ObjectAnimator.ofFloat(btn_post,"alpha",1.0f,0.0f).setDuration(700);
                ObjectAnimator appear = ObjectAnimator.ofFloat(postMethod,"alpha",0.0f,1.0f).setDuration(900);
                animatorSet = new AnimatorSet();
                animatorSet.playTogether(rotate);
                animatorSet.playTogether(fade);
                animatorSet.playTogether(appear);
                animatorSet.start();
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn_cancel.setClickable(true);
                        btn_post.setVisibility(View.INVISIBLE);
                        ObjectAnimator rotate = ObjectAnimator.ofFloat(btn_post, "rotation", 45f, 0f).setDuration(0);
                        rotate.start();
                    }
                }, 900);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_cancel.setClickable(false);
                btn_post.setClickable(false);
                btn_post.setVisibility(View.VISIBLE);
                btn_post.setAlpha((float)0.0);
                ObjectAnimator rotate = ObjectAnimator.ofFloat(btn_cancel, "rotation", 0f, -45f).setDuration(700);
                rotate.setInterpolator(new BounceInterpolator());
                ObjectAnimator fade = ObjectAnimator.ofFloat(postMethod,"alpha",1.0f,0.0f).setDuration(700);
                ObjectAnimator appear = ObjectAnimator.ofFloat(btn_post,"alpha",0.0f,1.0f).setDuration(900);
                animatorSet = new AnimatorSet();
                animatorSet.playTogether(rotate);
                animatorSet.playTogether(fade);
                animatorSet.playTogether(appear);
                animatorSet.start();
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        postMethod.setVisibility(View.INVISIBLE);
                        btn_post.setClickable(true);
                        ObjectAnimator rotate = ObjectAnimator.ofFloat(btn_post, "rotation", -45f, 0f).setDuration(0);
                        rotate.start();
                    }
                }, 700);
            }
        });

        plain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_cancel.setClickable(false);
                btn_post.setClickable(false);
                btn_post.setVisibility(View.VISIBLE);
                btn_post.setAlpha((float)0.0);
                ObjectAnimator rotate = ObjectAnimator.ofFloat(btn_cancel, "rotation", 0f, -45f).setDuration(700);
                rotate.setInterpolator(new BounceInterpolator());
                ObjectAnimator fade = ObjectAnimator.ofFloat(postMethod,"alpha",1.0f,0.0f).setDuration(700);
                ObjectAnimator appear = ObjectAnimator.ofFloat(btn_post,"alpha",0.0f,1.0f).setDuration(900);
                animatorSet = new AnimatorSet();
                animatorSet.playTogether(rotate);
                animatorSet.playTogether(fade);
                animatorSet.playTogether(appear);
                animatorSet.start();
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        postMethod.setVisibility(View.INVISIBLE);
                        btn_post.setClickable(true);
                        ObjectAnimator rotate = ObjectAnimator.ofFloat(btn_post, "rotation", -45f, 0f).setDuration(0);
                        rotate.start();
                    }
                }, 700);
            }
        });

    }





}