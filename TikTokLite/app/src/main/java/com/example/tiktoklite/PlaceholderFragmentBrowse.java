package com.example.tiktoklite;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.tiktoklite.api.IMiniDouyinService;
import com.example.tiktoklite.model.GetVideosResponse;
import com.example.tiktoklite.model.Video;
import com.example.tiktoklite.util.ImageHelper;
import com.example.tiktoklite.util.ResourceUtils;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PlaceholderFragmentBrowse extends Fragment {
    private ListView List;
    private ArrayAdapter ArrAdapter;
    private AnimatorSet animatorSet;

    private static final int PICK_IMAGE = 1;
    private static final int PICK_VIDEO = 2;
    private static final String TAG = "MainActivity";
    private RecyclerView mRv;
    private ImageButton f5;
    private java.util.List<Video> mVideos = new ArrayList<>();
    public Uri mSelectedImage;
    private Uri mSelectedVideo;


    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(IMiniDouyinService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private IMiniDouyinService miniDouyinService = retrofit.create(IMiniDouyinService.class);


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_placeholder_browse, container, false);
        mRv = view.findViewById(R.id.rv);
        f5 = view.findViewById(R.id.ib_f5);
        f5.setClickable(true);
        initRecyclerView();
        fetchFeed(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        f5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f5.setClickable(false);
                AnimatorSet animatorSet = new AnimatorSet();
                fetchFeed(getView());
                final ObjectAnimator rotate = ObjectAnimator.ofFloat(f5, "rotation", 0f, 360f).setDuration(750);
                rotate.start();
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rotate.start();
                    }
                }, 750);
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        f5.setClickable(true);
                    }
                }, 1500);
            }
        });

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        public VideoView videoView;
        public ProgressBar bar;
        public ImageButton play;
        public TextView usernm;
        public ImageButton ib_like;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_large);
            play = itemView.findViewById(R.id.ib_play);
            usernm = itemView.findViewById(R.id.usn);
            ib_like = itemView.findViewById(R.id.ib_like_vid);

        }

        public void bind(final Activity activity, final Video video) {
            ib_like.setClickable(true);
            ib_like.setImageResource(R.drawable.like_undone);
            img.setVisibility(View.VISIBLE);
            play.setVisibility(View.VISIBLE);
            ImageHelper.displayWebImage(video.imageUrl, img);
            usernm.setText("@"+video.userName);
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // VideoActivity.launch(activity, video.videoUrl);
                    img.setVisibility(View.INVISIBLE);
                    play.setVisibility(View.INVISIBLE);
                    VideoView videoView = itemView.findViewById(R.id.vplay);
                    final ProgressBar progressBar = itemView.findViewById(R.id.vplay_progressbar);
                    videoView.setMediaController(new MediaController(activity));
                    progressBar.setVisibility(View.VISIBLE);
                    videoView.setVideoURI(Uri.parse(video.videoUrl));
                    //videoView.requestFocus();
                    videoView.start();
                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            });
            ib_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ib_like.setClickable(false);
                    AnimatorSet animatorSet = new AnimatorSet();
                    ObjectAnimator rotate = ObjectAnimator.ofFloat(ib_like, "rotation", 0f, 15f).setDuration(150);
                    final ObjectAnimator unrotate = ObjectAnimator.ofFloat(ib_like, "rotation", 15f, 0f).setDuration(100);
                    rotate.start();
                    v.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                             unrotate.start();
                        }
                    }, 150);
                    v.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ib_like.setImageResource(R.drawable.like_done);
                        }
                    }, 150);


                }
            });


        }
    }
    private void initRecyclerView() {

        mRv.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        mRv.setAdapter(new RecyclerView.Adapter<MyViewHolder>() {
            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                MyViewHolder myViewHolder = new MyViewHolder(
                        LayoutInflater.from(getActivity())
                                .inflate(R.layout.video_item_view_large, viewGroup, false));
                return myViewHolder;
            }

            @Override
            public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
                final Video video = mVideos.get(i);
                viewHolder.bind(getActivity(), video);
            }

            @Override
            public int getItemCount() {
                return mVideos.size();
            }
        });
    }

    private MultipartBody.Part getMultipartFromUri(String name, Uri uri) {
        File f = new File(ResourceUtils.getRealPath(getActivity(), uri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        return MultipartBody.Part.createFormData(name, f.getName(), requestFile);
    }

    public void fetchFeed(View view) {

        miniDouyinService.getVideos().enqueue(new Callback<GetVideosResponse>() {
            @Override
            public void onResponse(Call<GetVideosResponse> call, Response<GetVideosResponse> response) {
                if (response.body() != null && response.body().videos != null) {
                    mVideos = response.body().videos;
                    mRv.getAdapter().notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<GetVideosResponse> call, Throwable throwable) {
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
