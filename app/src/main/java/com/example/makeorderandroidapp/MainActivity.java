package com.example.makeorderandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.MailTo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn, getBtnSignUp;
    TextView txtSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        getBtnSignUp = (Button) findViewById(R.id.btnSignUp);

        txtSlogan = (TextView)findViewById(R.id.txtSlogan);

        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/Nabila.ttf");
        txtSlogan.setTypeface(face);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, SignIn.class);
                startActivity(intent);

            }
        });

        getBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);

            }
        });
    }
}
