package com.example.tiktoklite;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class RecorderActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private static final int RC_PERMISSION = 1008;
    private static final String TAG = "RecordActivity";
    private SurfaceView surfaceView;
    private ImageButton btn_rec, btn_shot, btn_lit;
    private Camera mCamera;
    private MediaRecorder mRecorder;
    private SurfaceHolder mSurfaceHolder;
    private ImageView iv;
    private VideoView vv;
    private ImageButton btn_yes, btn_no;
    private ImageView iv_focus;
    private String vpath;
    private String ipath;

    private int returnType = 0;
    private boolean isRecording;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!(ActivityCompat.checkSelfPermission(RecorderActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                || !(ActivityCompat.checkSelfPermission(RecorderActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                || !(ActivityCompat.checkSelfPermission(RecorderActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
            //没有权限，申请权限
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
            //申请权限，其中RC_PERMISSION是权限申请码，用来标志权限申请的
            ActivityCompat.requestPermissions(RecorderActivity.this,permissions, RC_PERMISSION);
        }else {
            //拥有权限
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_recorder);
        initCamera();
        initView();
        init();
    }


    private String getOutputImagePath() {
        File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir, "IMG_" + timestamp + ".jpg");
        if (!mediaFile.exists()) {
            mediaFile.getParentFile().mkdirs();
        }
        return mediaFile.getAbsolutePath();
    }

    private String getOutputVideoPath() {
        File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir, "MOV_" + timestamp + ".mp4");
        if (!mediaFile.exists()) {
            mediaFile.getParentFile().mkdirs();
        }
        return mediaFile.getAbsolutePath();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == RC_PERMISSION && grantResults.length == 2
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "权限申请成功");
        }else {
            Log.e(TAG, "权限申请失败");
        }
    }

    private void initView() {
        surfaceView = findViewById(R.id.surfaceview);
        btn_rec = findViewById(R.id.btn_rec);
        btn_shot = findViewById(R.id.btn_shot);
        btn_lit = findViewById(R.id.btn_lit);
        iv = findViewById(R.id.imageView);
        vv = findViewById(R.id.videoView);
        btn_yes = findViewById(R.id.btn_yes);
        btn_yes.setClickable(false);
        btn_no = findViewById(R.id.btn_no);
        btn_no.setClickable(false);
        iv_focus = findViewById(R.id.iv_focus);
        iv_focus.setVisibility(View.GONE);
        isRecording = false;
        mSurfaceHolder = surfaceView.getHolder();
        mSurfaceHolder.addCallback(RecorderActivity.this);
    }

    private void initCamera() {
        mCamera = Camera.open();
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPictureFormat(ImageFormat.JPEG);
        // 点击屏幕自动对焦
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        // 调节闪光灯
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
        parameters.set("orientation", "portrait");
//        parameters.set("rotation", 90);
        mCamera.setParameters(parameters);
        mCamera.setDisplayOrientation(90);
    }

    private void setAutofocus() {
        if (mCamera != null) {
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    if (success) {
                        iv_focus.setVisibility(View.VISIBLE);
                        AnimatorSet animatorSet = new AnimatorSet();
                        ObjectAnimator sizeX = ObjectAnimator.ofFloat(iv_focus, "scaleX", 2, 1).setDuration(1000);
                        ObjectAnimator sizeY = ObjectAnimator.ofFloat(iv_focus, "scaleY", 2, 1).setDuration(1000);
                        animatorSet.playTogether(sizeX);
                        animatorSet.playTogether(sizeY);
                        animatorSet.start();
                    }
                }
            });
        }
    }


    public String getIsOpenFlashMode() {
        return mCamera.getParameters().getFlashMode();
    }

    public void setIsOpenFlashMode() {
        Camera.Parameters mParameters;
        if(getIsOpenFlashMode().equals(Camera.Parameters.FLASH_MODE_OFF)) {
            mParameters = mCamera.getParameters();
            //设置闪光灯模式
            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            mCamera.setParameters(mParameters);
            btn_lit.setImageResource(R.drawable.flon);
        }
        else if(getIsOpenFlashMode().equals(Camera.Parameters.FLASH_MODE_ON)){
            mParameters = mCamera.getParameters();
            //设置闪光灯模式
            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
            mCamera.setParameters(mParameters);
            btn_lit.setImageResource(R.drawable.flauto);
        } else {
            mParameters = mCamera.getParameters();
            //设置闪光灯模式
            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(mParameters);
            btn_lit.setImageResource(R.drawable.floff);
        }
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) {
        if (mSurfaceHolder.getSurface() == null) {
            return;
        }
        mCamera.stopPreview();
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null) {
            initCamera();
        }
        mCamera.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCamera.stopPreview();
    }

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            FileOutputStream fos = null;
            String fpath = getOutputImagePath();
            ipath = fpath;
            File file = new File(fpath);
            try {
                fos = new FileOutputStream(file);
                fos.write(data);
                fos.flush();
                Bitmap bitmap = BitmapFactory.decodeFile(fpath);
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                iv.setVisibility(View.VISIBLE);
                vv.setVisibility(View.GONE);
                btn_yes.setVisibility(View.VISIBLE);
                btn_yes.setClickable(true);
                btn_no.setVisibility(View.VISIBLE);
                btn_no.setClickable(true);
                iv.setImageBitmap(rotateBitmap);
                returnType = 1;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mCamera.startPreview();
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean prepareVideoRecorder() {
        mRecorder = new MediaRecorder();
        mCamera.unlock();
        mRecorder.setCamera(mCamera);
        mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        vpath = getOutputVideoPath() ;
        mRecorder.setOutputFile(vpath);
        mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
        mRecorder.setOrientationHint(90);
        try {
            mRecorder.prepare();
        } catch (Exception e) {
            mRecorder = null;
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void record(View view) {
        if (isRecording) {
            btn_rec.setImageResource(R.drawable.rec);
            mRecorder.stop();
            mRecorder.reset();
            mRecorder.release();
            mRecorder = null;
            mCamera.lock();

            vv.setVisibility(View.VISIBLE);
            btn_yes.setVisibility(View.VISIBLE);
            btn_yes.setClickable(true);
            btn_no.setVisibility(View.VISIBLE);
            btn_no.setClickable(true);
            iv.setVisibility(View.GONE);
            vv.setVideoPath(vpath);
            vv.start();
            returnType = 2;
        } else {
            if (prepareVideoRecorder()) {
                btn_rec.setImageResource(R.drawable.stop);
                mRecorder.start();
            }
        }
        isRecording = !isRecording;
    }

    private void init() {
        btn_rec.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                record(surfaceView);
            }
        });
        btn_shot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(null, null, mPictureCallback);
            }
        });
        btn_lit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIsOpenFlashMode();
            }
        });
        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAutofocus();
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iv_focus.setVisibility(View.INVISIBLE);
                    }
                }, 1000);
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv.setVisibility(View.GONE);
                vv.setVisibility(View.GONE);
                btn_yes.setVisibility(View.GONE);
                btn_no.setVisibility(View.GONE);
            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv.setVisibility(View.GONE);
                vv.setVisibility(View.GONE);
                btn_yes.setVisibility(View.GONE);
                btn_no.setVisibility(View.GONE);

                // TODO 1: Upload the video
                Intent returnIntent = new Intent();
                returnIntent.putExtra("type", returnType);
                if(returnType == 1)
                    returnIntent.putExtra("path", ipath);
                else
                    returnIntent.putExtra("path", vpath);

                setResult(RESULT_OK, returnIntent);
                RecorderActivity.this.finish();
            }
        });


    }



}