package com.example.dulichhaiphong.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.dulichhaiphong.Adapter.Slider_Adapter_Trangchu;
import com.example.dulichhaiphong.Model.Slider_Item_Trangchu;
import com.example.dulichhaiphong.R;

import java.util.ArrayList;
import java.util.List;

public class Trangchu extends AppCompatActivity {
    private ImageView logohoaphuong;
    private ViewPager2 viewPager2;
    private CardView logout,cvTaikhoan,cardFaq,cardNvlichsu,cardLangnghe,cardLeHoi,cardLichsuphatrien;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trangchu);
        Anhxa();
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.animition_logo_rotate);
        logohoaphuong.startAnimation(animation);
        CauhinhSlider();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        cvTaikhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Trangchu.this,HomeActivity.class);
                startActivity(intent);
            }
        });
        cardFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Trangchu.this,FaQActivity.class);
                startActivity(intent);
            }
        });
        cardNvlichsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Trangchu.this,NhanvatlichsuActivity.class);
                startActivity(intent);
            }
        });
        cardLangnghe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Trangchu.this,LangngheActivity.class);
                startActivity(intent);
            }
        });
        cardLeHoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Trangchu.this,LehoiActivity.class);
                startActivity(intent);
            }
        });
        cardLichsuphatrien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Trangchu.this,LichsuphattrienActivity.class);
                startActivity(intent);
            }
        });
    }
    private void Anhxa(){
        logohoaphuong = (ImageView)findViewById(R.id.imgLogohoaphuong);
        viewPager2 = (ViewPager2)findViewById(R.id.viewPageSlider);
        logout = (CardView) findViewById(R.id.Logout);
        cvTaikhoan = (CardView) findViewById(R.id.cVTaikhoan);
        cardFaq = (CardView) findViewById(R.id.cardFaq);
        cardNvlichsu = (CardView) findViewById(R.id.cardNvLichsu);
        cardLangnghe = (CardView) findViewById(R.id.cardLangnghe);
        cardLeHoi = (CardView) findViewById(R.id.cardLeHoi);
        cardLichsuphatrien = (CardView) findViewById(R.id.cardLichsuphatrien);
    }
    private void CauhinhSlider(){
        List<Slider_Item_Trangchu> itemList = new ArrayList<>();
        itemList.add(new Slider_Item_Trangchu(R.drawable.diadiem_tamlinh,"Địa điểm tâm linh"));
        itemList.add(new Slider_Item_Trangchu(R.drawable.ditich_lichsu,"Di tích lịch sử"));
        itemList.add(new Slider_Item_Trangchu(R.drawable.vinh_dao,"Vịnh-đảo-hang động"));
        viewPager2.setAdapter(new Slider_Adapter_Trangchu(itemList,viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(20));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f+r*0.15f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.setCurrentItem(1);
    }
}