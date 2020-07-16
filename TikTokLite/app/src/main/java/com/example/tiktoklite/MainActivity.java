package com.example.tiktoklite;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.tiktoklite.api.IMiniDouyinService;
import com.example.tiktoklite.model.GetVideosResponse;
import com.example.tiktoklite.model.Video;
import com.example.tiktoklite.util.ImageHelper;
import com.example.tiktoklite.util.ResourceUtils;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.tiktoklite.model.PostVideoResponse;
import com.flyco.tablayout.SlidingTabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.net.Uri.fromFile;

public class MainActivity extends AppCompatActivity {
    private static final int RC_PERMISSION = 1008;
    private List<Fragment> list;
    private ViewPager viewPager;
    private mFragmentPagerAdapter Adapter;
    private SlidingTabLayout  mTabLayout;
    private String[] mTitlesArrays ={"视频","广场","我的","主页"};
    private PlaceholderFragmentWorld mPlaceholderFragmentWorld;
    private PlaceholderFragmentHome mPlaceholderFragmentHome;
    private PlaceholderFragmentInfo mPlaceholderFragmentInfo;

    public static String uid = "点击登录";
    public static String upassword = "未登录";
    public static int uavatar = R.drawable.unknown;

    private String id;
    private String name;

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(IMiniDouyinService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private IMiniDouyinService miniDouyinService = retrofit.create(IMiniDouyinService.class);

    private ImageButton btn_post, btn_file, btn_rec, btn_cancel;
    FrameLayout postMethod;
    private AnimatorSet animatorSet;
    private ImageView plain;
    private ImageButton upload;

    private Uri mSelectedImage;
    private Uri mSelectedVideo;

    private boolean hasChooseImage = false;
    private boolean hasChooseVideo = false;

    private final int PICK_IMAGE = 1;
    private final int PICK_VIDEO = 2;
    private final int RECORD = 3;

    private final String TAG = "MainActivity";

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new ArrayList<>();
        list.add(new PlaceholderFragmentBrowse());
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
        upload = findViewById(R.id.upload);
        upload.setVisibility(View.INVISIBLE);

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload.setVisibility(View.INVISIBLE);
                upload.setClickable(false);
                btn_post.setClickable(false);
                btn_cancel.setClickable(false);
                plain.setClickable(false);
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
                        plain.setClickable(true);
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
                plain.setClickable(false);
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
                plain.setClickable(false);
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

        btn_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (MainActivity.this, RecorderActivity.class);
                startActivityForResult(intent, RECORD);
            }
        });

        btn_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "选择一张封面图片", Toast.LENGTH_LONG).show();
                chooseImage();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postVideo();
            }
        });

    }

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    public void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult() called with: requestCode = ["
                + requestCode
                + "], resultCode = ["
                + resultCode
                + "], data = ["
                + data
                + "]");

        if (resultCode == RESULT_OK && null != data) {


            if (requestCode == PICK_IMAGE) {
                mSelectedImage = data.getData();
                hasChooseImage = true;
                Log.d(TAG, "selectedImage = " + mSelectedImage);
                Toast.makeText(MainActivity.this, "选择一个视频", Toast.LENGTH_LONG).show();
                chooseVideo();
            } else if (requestCode == PICK_VIDEO) {
                mSelectedVideo = data.getData();
                upload.setVisibility(View.VISIBLE);
                upload.setClickable(true);
                upload.setImageResource(R.drawable.post);
                hasChooseVideo = true;
                Log.d(TAG, "mSelectedVideo = " + mSelectedVideo);
            } else if (requestCode == RECORD) {
                int type = data.getIntExtra("type", -1);
                String path = data.getStringExtra("path");
                if(type == 1) {
                    mSelectedImage = Uri.fromFile(new File(path));
                    hasChooseImage = true;
                    if (hasChooseImage && hasChooseVideo) {
                        upload.setVisibility(View.VISIBLE);
                        upload.setClickable(true);
                        upload.setImageResource(R.drawable.post);
                    }
                } else {
                    mSelectedVideo = Uri.fromFile(new File(path));
                    hasChooseVideo = true;
                    if (hasChooseImage && hasChooseVideo) {
                        upload.setVisibility(View.VISIBLE);
                        upload.setClickable(true);
                        upload.setImageResource(R.drawable.post);
                    }
                }
            }
        }
    }

    private MultipartBody.Part getMultipartFromUri(String name, Uri uri) {
        File f = new File(ResourceUtils.getRealPath(MainActivity.this, uri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        return MultipartBody.Part.createFormData(name, f.getName(), requestFile);
    }

    private void postVideo() {

        upload.setEnabled(false);
        upload.setImageResource(R.drawable.posting);

        MultipartBody.Part coverImagePart = getMultipartFromUri("cover_image", mSelectedImage);
        MultipartBody.Part videoPart = getMultipartFromUri("video", mSelectedVideo);
        id = uid;
        name = upassword;
        miniDouyinService.postVideo(id, name, coverImagePart, videoPart).enqueue(
                new Callback<PostVideoResponse>() {
                    @Override
                    public void onResponse(Call<PostVideoResponse> call, Response<PostVideoResponse> response) {
                        if (response.body() != null) {
                            Toast.makeText(MainActivity.this, response.body().toString(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PostVideoResponse> call, Throwable throwable) {
                        Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        upload.setImageResource(R.drawable.post);
                        upload.setEnabled(true);
                    }
                });
        upload.setVisibility(View.INVISIBLE);
    }
}