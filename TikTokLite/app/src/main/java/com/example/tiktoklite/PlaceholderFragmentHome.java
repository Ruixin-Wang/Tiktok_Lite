package com.example.tiktoklite;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.Iterator;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PlaceholderFragmentHome extends Fragment {
    private ListView List;
    private ArrayAdapter ArrAdapter;
    private AnimatorSet animatorSet;

    private static final int PICK_IMAGE = 1;
    private static final int PICK_VIDEO = 2;
    private static final String TAG = "MainActivity";
    private RecyclerView mRv;
    private java.util.List<Video> mVideos = new ArrayList<>();
    public Uri mSelectedImage;
    private Uri mSelectedVideo;

    private String id = MainActivity.uid;
    private String name = MainActivity.upassword;

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(IMiniDouyinService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private IMiniDouyinService miniDouyinService = retrofit.create(IMiniDouyinService.class);


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_placeholder_world, container, false);
        mRv = view.findViewById(R.id.rv);
        initRecyclerView();
        fetchFeed(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }

        public void bind(final Activity activity, final Video video) {
            ImageHelper.displayWebImage(video.imageUrl, img);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoActivity.launch(activity, video.videoUrl);
                }
            });
        }
    }
    private void initRecyclerView() {

        mRv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRv.setAdapter(new RecyclerView.Adapter<PlaceholderFragmentWorld.MyViewHolder>() {
            @NonNull
            @Override
            public PlaceholderFragmentWorld.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                PlaceholderFragmentWorld.MyViewHolder myViewHolder = new PlaceholderFragmentWorld.MyViewHolder(
                        LayoutInflater.from(getActivity())
                                .inflate(R.layout.video_item_view, viewGroup, false));
                return myViewHolder;
            }

            @Override
            public void onBindViewHolder(@NonNull PlaceholderFragmentWorld.MyViewHolder viewHolder, int i) {
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
                    Iterator<Video> it = mVideos.iterator();
                    while(it.hasNext()){
                        Video v = it.next();
                        if(! id.equals(v.studentId)){
                            it.remove();
                        }
                    }
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
