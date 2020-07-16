package com.example.tiktoklite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tiktoklite.database.LoginDao;
import com.example.tiktoklite.database.LoginDatabase;
import com.example.tiktoklite.database.LoginEntity;
import com.example.tiktoklite.MainActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

public class LoginActivity extends AppCompatActivity {

    private EditText id, passsword;
    private ImageButton OK;

    private String uid;
    private String upassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        id = findViewById(R.id.editTextTextPersonName);
        passsword = findViewById(R.id.editTextTextPassword);
        OK = findViewById(R.id.ib_OK);



        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        uid = id.getText().toString();
                        upassword = passsword.getText().toString();
                        LoginDao dao = LoginDatabase.inst(LoginActivity.this).loginDao();
                        Intent returnIntent = new Intent();
                        String name = dao.select_id(uid);
                        if(name.equals(upassword)) {
                            MainActivity.uid = uid;
                            MainActivity.upassword = upassword;
                            MainActivity.uavatar = R.drawable.zju;
                            returnIntent.putExtra("istrue", 1);
                            Log.d(TAG, "istrue");
                        } else {
                            returnIntent.putExtra("istrue", 0);
                            Log.d(TAG, "isfalse");
                        }
                        setResult(RESULT_OK, returnIntent);
                        LoginActivity.this.finish();
                    }
                }.start();
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
