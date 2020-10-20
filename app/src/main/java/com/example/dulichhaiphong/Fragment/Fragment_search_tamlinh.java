package com.example.dulichhaiphong.Fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dulichhaiphong.Model.TimkiemTamlinh;
import com.example.dulichhaiphong.Model.TruyenDataPass;
import com.example.dulichhaiphong.R;

import java.util.ArrayList;

public class Fragment_search_tamlinh extends DialogFragment {
    Spinner spQuanhuyen,spCap,spLoaiDiTich;
    ImageView imgDong;
    Button btnLoc;
    View view;
    String tenCap,tenQuan,tenLoaiDitich,tenView;
    ArrayList<String> dsquanhuyen,dsCapcongnhan,dsLoaiDiTich;
    ArrayAdapter adapterQuanhuyen,adapterCapcongnhan,adapterLoaiDiTich;
    TimkiemTamlinh timkiemTamlinh;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_tamlinh,container,false);
        Anhxa();
        KhoiTao();
        adapterQuanhuyen = new ArrayAdapter(getActivity(),R.layout.support_simple_spinner_dropdown_item,dsquanhuyen);
        spQuanhuyen.setAdapter(adapterQuanhuyen);
        adapterCapcongnhan = new ArrayAdapter(getActivity(),R.layout.support_simple_spinner_dropdown_item,dsCapcongnhan);
        spCap.setAdapter(adapterCapcongnhan);
        adapterLoaiDiTich = new ArrayAdapter(getActivity(),R.layout.support_simple_spinner_dropdown_item,dsLoaiDiTich);
        spLoaiDiTich.setAdapter(adapterLoaiDiTich);
        timkiemTamlinh = (TimkiemTamlinh) getActivity();

        imgDong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        spLoaiDiTich.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(dsLoaiDiTich.get(i).equals("Chọn tất cả")){
                    tenLoaiDitich = "";
                }else{
                    tenLoaiDitich = dsLoaiDiTich.get(i);
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
                timkiemTamlinh.Timkiem(tenView,tenCap,tenLoaiDitich,tenQuan);
                dismiss();
            }
        });
        return view;
    }

    private void Anhxa(){
        spQuanhuyen = (Spinner) view.findViewById(R.id.spinnerQuanhuyen);
        spCap = (Spinner) view.findViewById(R.id.spinnerCapcongnhan);
        spLoaiDiTich = (Spinner) view.findViewById(R.id.spinnerLoaiDiTich);
        btnLoc = (Button) view.findViewById(R.id.btnLocketqua);
        imgDong = (ImageView) view.findViewById(R.id.imgClose);
    }
    private void KhoiTao(){
        dsquanhuyen = new ArrayList<>();
        dsCapcongnhan = new ArrayList<>();
        dsLoaiDiTich = new ArrayList<>();
        Bundle bundle = getArguments();
        if(bundle!= null){
            dsCapcongnhan = bundle.getStringArrayList("DsCapcongnhan");
            dsquanhuyen = bundle.getStringArrayList("DsQuanhuyen");
            dsLoaiDiTich = bundle.getStringArrayList("DsLoaiDiTich");
            tenView = bundle.getString("tenView");
        }
    }
}
