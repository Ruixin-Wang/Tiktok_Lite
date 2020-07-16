package com.example.tiktoklite;


import android.animation.AnimatorSet;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.tiktoklite.database.LoginDao;
import com.example.tiktoklite.database.LoginDatabase;
import com.example.tiktoklite.database.LoginEntity;

import java.io.File;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;


public class PlaceholderFragmentInfo extends Fragment implements View.OnClickListener{

    private boolean DEBUG = false;

    private ConstraintLayout Likes, Favorites, ShiftAccount;
    private mFragmentPagerAdapter.IOnItemClickListener mItemClickListener;
    public TextView tv_username, tv_userid;
    public ImageView iv_avatar;

    private final int LOGIN = 100;


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

        if(DEBUG)
        {
            new Thread() {
                @Override
                public void run() {
                    LoginDao dao = LoginDatabase.inst(getActivity()).loginDao();
                    dao.deleteAll();
                    dao.addInfo(new LoginEntity("zju", "zju", 1));
                    dao.addInfo(new LoginEntity("110", "okk", 2));
                }
            }.start();
        }

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
                new Thread() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivityForResult(intent, LOGIN);
                    }
                }.start();
            }
        });

        tv_userid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent, LOGIN);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult() called with: requestCode = ["
                + requestCode
                + "], resultCode = ["
                + resultCode
                + "], data = ["
                + data
                + "]");

        Log.d(TAG, MainActivity.uid);

        if (resultCode == -1 && null != data) {
            if (requestCode == LOGIN) {
                int istrue = data.getIntExtra("istrue", -1);
                Log.d(TAG, Integer.toString(istrue));
                if(istrue == 1) {
                    Log.d(TAG, "ok");
                    Toast toast=Toast.makeText(getActivity(),"Login successfully",Toast.LENGTH_SHORT);
                    toast.show();
                    tv_username.setText(MainActivity.upassword);
                    tv_userid.setText(MainActivity.uid);
                    iv_avatar.setImageResource(MainActivity.uavatar);
                } else {
                    Toast toast=Toast.makeText(getActivity(),"Wrong password",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
    }

    @Override
    public void onClick(View view) {

    }
}
