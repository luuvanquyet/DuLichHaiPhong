package com.example.dulichhaiphong.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dulichhaiphong.R;

public class Slider extends AppCompatActivity {
    private static int TIME_SCREEN = 5000;
    Animation top_anm;
    Animation bottom_anm;
    ImageView imgLogo;
    TextView txtTittle1,txtTittle2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);
        Anhxa();
        imgLogo.startAnimation(top_anm);
        txtTittle1.startAnimation(bottom_anm);
        txtTittle2.startAnimation(bottom_anm);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Slider.this,Slider_Next.class);
                startActivity(intent);
                finish();
            }
        },TIME_SCREEN);
    }
    private void Anhxa(){
        top_anm = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottom_anm = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        imgLogo = (ImageView)findViewById(R.id.imglogo);
        txtTittle1 =(TextView)findViewById(R.id.txttitle1);
        txtTittle2 = (TextView)findViewById(R.id.txttitle2);
    }
}