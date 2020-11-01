package com.example.dulichhaiphong.Fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.dulichhaiphong.Model.Timkiemlehoi;
import com.example.dulichhaiphong.R;

import java.util.ArrayList;

public class Fragment_search_lehoi extends DialogFragment {
    Spinner spQuanhuyen,spCap,spThang;
    ImageView imgDong;
    Button btnLoc;
    View view;
    String tenCap,tenQuan,tenThang;
    ArrayList<String> dsquanhuyen,dsCapcongnhan,dsThang;
    ArrayAdapter adapterQuanhuyen,adapterCapcongnhan,adapterThang;
    Timkiemlehoi timkiemlehoi;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_lehoi,container,false);
        Anhxa();
        KhoiTao();
        adapterQuanhuyen = new ArrayAdapter(getActivity(),R.layout.support_simple_spinner_dropdown_item,dsquanhuyen);
        spQuanhuyen.setAdapter(adapterQuanhuyen);
        adapterCapcongnhan = new ArrayAdapter(getActivity(),R.layout.support_simple_spinner_dropdown_item,dsCapcongnhan);
        spCap.setAdapter(adapterCapcongnhan);
        adapterThang = new ArrayAdapter(getActivity(),R.layout.support_simple_spinner_dropdown_item,dsThang);
        spThang.setAdapter(adapterThang);
        timkiemlehoi = (Timkiemlehoi) getActivity();

        imgDong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        spThang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(dsThang.get(i).equals("Chọn tất cả")){
                    tenThang = "";
                }else{
                    tenThang = dsThang.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spQuanhuyen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(dsquanhuyen.get(i).equals("Chọn tất cả")){
                    tenQuan = "";
                }else{
                    tenQuan = dsquanhuyen.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spCap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(dsCapcongnhan.get(i).equals("Chọn tất cả")){
                    tenCap = "";
                }else{
                    tenCap = dsCapcongnhan.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timkiemlehoi.Timkiem(tenCap,tenQuan,tenThang);
                dismiss();
            }
        });
        return view;
    }

    private void Anhxa(){
        spQuanhuyen = (Spinner) view.findViewById(R.id.spinnerQuanhuyen);
        spCap = (Spinner) view.findViewById(R.id.spinnerCapcongnhan);
        spThang = (Spinner) view.findViewById(R.id.spinnerLoaiDiTich);
        btnLoc = (Button) view.findViewById(R.id.btnLocketqua);
        imgDong = (ImageView) view.findViewById(R.id.imgClose);
    }
    private void KhoiTao(){
        dsquanhuyen = new ArrayList<>();
        dsCapcongnhan = new ArrayList<>();
        dsThang = new ArrayList<>();
        dsThang.add("Chọn tất cả");
        dsThang.add("Tháng 1");
        dsThang.add("Tháng 2");
        dsThang.add("Tháng 3");
        dsThang.add("Tháng 4");
        dsThang.add("Tháng 5");
        dsThang.add("Tháng 6");
        dsThang.add("Tháng 7");
        dsThang.add("Tháng 8");
        dsThang.add("Tháng 9");
        dsThang.add("Tháng 10");
        dsThang.add("Tháng 11");
        dsThang.add("Tháng 12");
        Bundle bundle = getArguments();
        if(bundle!= null){
            dsCapcongnhan = bundle.getStringArrayList("DsCapcongnhan");
            dsquanhuyen = bundle.getStringArrayList("DsQuanhuyen");
        }
    }

}
