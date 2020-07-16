package com.example.tiktoklite;


import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.tiktoklite.database.LoginDao;
import com.example.tiktoklite.database.LoginDatabase;
import com.example.tiktoklite.database.LoginEntity;

import java.util.List;


public class PlaceholderFragmentInfo extends Fragment implements View.OnClickListener{
    private ConstraintLayout Likes, Favorites, ShiftAccount;
    private mFragmentPagerAdapter.IOnItemClickListener mItemClickListener;
    public TextView tv_username, tv_userid;
    public ImageView iv_avatar;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_placeholder_info, container, false);
        Likes = view.findViewById(R.id.Likes);
        Favorites = view.findViewById(R.id.Favorites);
        ShiftAccount = view.findViewById(R.id.shiftAccount);
        tv_username = view.findViewById(R.id.tv_username);
        tv_userid = view.findViewById(R.id.tv_userid);
        iv_avatar = view.findViewById(R.id.iv_avatar);


        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO 3: Database-Relative issue
        tv_username.setText(MainActivity.upassword);
        tv_userid.setText(MainActivity.uid);
        iv_avatar.setImageResource(MainActivity.uavatar);

        ShiftAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        ShiftAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });




    }

    @Override
    public void onClick(View view) {

    }
}
