package com.example.arufureddotask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

   private static final int SPLASH = 3300;

    Animation logoanim,textanim;
    ImageView ImageView;
    TextView textView,company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        logoanim = AnimationUtils.loadAnimation(this, R.anim.logoanimation);
        textanim = AnimationUtils.loadAnimation(this, R.anim.textanim);
        ImageView = findViewById(R.id.imageView);
        company = findViewById(R.id.Company);
        textView =findViewById(R.id.textView);

        ImageView.setAnimation(logoanim);
        textView.setAnimation(textanim);
        company.setAnimation(textanim);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH);
    }
}