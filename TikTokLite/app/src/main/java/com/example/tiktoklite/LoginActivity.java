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

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText id, passsword;
    private ImageButton OK;



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
                MainActivity.uavatar = R.drawable.zju;
                MainActivity.uid = id.getText().toString();
                MainActivity.upassword = passsword.getText().toString();

                Intent intent = new Intent (LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent intent = new Intent (LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
