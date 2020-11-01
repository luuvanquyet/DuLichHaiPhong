package com.example.dulichhaiphong.Fragment;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.dulichhaiphong.R;

public class Fragment_chitiet_baiviet_lichsu extends DialogFragment {
    WebView noidung;
    ImageView back;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chitiet_baiviet,container,false);
        noidung = (WebView) view.findViewById(R.id.txtNoidung);
        back = (ImageView) view.findViewById(R.id.imgClose);
        Bundle bundle = getArguments();
        if(bundle!=null){
            String txtNoidung = bundle.getString("Noidung");
            noidung.getSettings().setJavaScriptEnabled(true);
            noidung.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            noidung.clearView();
            noidung.measure(300, 300);
            noidung.getSettings().setDomStorageEnabled(true);
            noidung.getSettings().setLoadWithOverviewMode(true);
            noidung.loadDataWithBaseURL("http://dulichhaiphong.xyz","<style>img{display: inline;height: auto;max-width: 100%;}</style>"+ txtNoidung,"text/html","utf-8",null);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }


}
