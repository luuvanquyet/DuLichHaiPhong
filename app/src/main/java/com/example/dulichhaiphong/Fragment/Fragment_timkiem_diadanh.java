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

import androidx.annotation.Nullable;

import com.example.dulichhaiphong.Adapter.Adapter_danhmuc;
import com.example.dulichhaiphong.Model.DanhMuc;
import com.example.dulichhaiphong.Model.Timkiemdiadanh;
import com.example.dulichhaiphong.R;

import java.util.ArrayList;

public class Fragment_timkiem_diadanh extends DialogFragment {
    private ImageView imgDong;
    private Button btnLoc;
    private View view;
    private Spinner spinLoaidiadanh,spinCapcongnhan,spinLoaiditich,spQuanhuyen,spinnerPhamvi;
    ArrayList<String> dsquanhuyen,dsCapcongnhan,dsLoaiDiTich;
    ArrayAdapter adapterQuanhuyen,adapterCapcongnhan,adapterLoaiDiTich;
    Adapter_danhmuc adapterLoaidiadanh, adapterPhamvi;
    ArrayList<DanhMuc> arrayDiadanh,arrayPhamvi;
    Timkiemdiadanh timkiemdiadanh;
    String tenCap,tenQuan,tenLoaiDitich,idDiadanh,phamVi;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_diadanh,container,false);
        Anhxa();
        arrayDiadanh = new ArrayList<>();
        arrayDiadanh.add(new DanhMuc(0,"Chọn tất cả"));
        arrayDiadanh.add(new DanhMuc(19,"Đình - Đền - Chùa"));
        arrayDiadanh.add(new DanhMuc(25,"Vịnh - Đảo - Hang động"));
        arrayDiadanh.add(new DanhMuc(32,"Di tích"));
        adapterLoaidiadanh = new Adapter_danhmuc(getActivity(),R.layout.item_danhmuc,arrayDiadanh);
        spinLoaidiadanh.setAdapter(adapterLoaidiadanh);
        arrayPhamvi = new ArrayList<>();
        arrayPhamvi.add(new DanhMuc(0,"Toàn bộ lãnh thổ"));
        arrayPhamvi.add(new DanhMuc(500,"500 m"));
        arrayPhamvi.add(new DanhMuc(1000,"1 km"));
        arrayPhamvi.add(new DanhMuc(2000,"2 km"));
        arrayPhamvi.add(new DanhMuc(5000,"5 km"));
        arrayPhamvi.add(new DanhMuc(10000,"10 km"));
        adapterPhamvi = new Adapter_danhmuc(getActivity(),R.layout.item_danhmuc,arrayPhamvi);
        spinnerPhamvi.setAdapter(adapterPhamvi);
        KhoiTao();
        adapterQuanhuyen = new ArrayAdapter(getActivity(),R.layout.support_simple_spinner_dropdown_item,dsquanhuyen);
        spQuanhuyen.setAdapter(adapterQuanhuyen);
        adapterCapcongnhan = new ArrayAdapter(getActivity(),R.layout.support_simple_spinner_dropdown_item,dsCapcongnhan);
        spinCapcongnhan.setAdapter(adapterCapcongnhan);
        adapterLoaiDiTich = new ArrayAdapter(getActivity(),R.layout.support_simple_spinner_dropdown_item,dsLoaiDiTich);
        spinLoaiditich.setAdapter(adapterLoaiDiTich);
        spinCapcongnhan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        spinLoaiditich.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        spinLoaidiadanh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(arrayDiadanh.get(i).getTenDanhMuc().equals("Chọn tất cả")){
                    idDiadanh = "";
                }else{
                    idDiadanh = arrayDiadanh.get(i).getId()+"";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerPhamvi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                phamVi = arrayPhamvi.get(i).getId()+"";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        imgDong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timkiemdiadanh.Timkiem(idDiadanh,tenCap,tenLoaiDitich,tenQuan,phamVi);
                dismiss();
            }
        });
        return view;
    }
    private void Anhxa(){
        spinCapcongnhan = (Spinner) view.findViewById(R.id.spinnerCapcongnhan);
        spinLoaiditich = (Spinner) view.findViewById(R.id.spinnerLoaiDiTich);
        spQuanhuyen = (Spinner) view.findViewById(R.id.spinnerQuanhuyen);
        spinLoaidiadanh = (Spinner) view.findViewById(R.id.spinDanhMuc);
        spinnerPhamvi = (Spinner) view.findViewById(R.id.spinnerPhamvi);
        imgDong = (ImageView) view.findViewById(R.id.imgClose);
        btnLoc = (Button) view.findViewById(R.id.btnLocketqua);
        timkiemdiadanh = (Timkiemdiadanh) getActivity();
    }
    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }
    private void KhoiTao() {
        dsquanhuyen = new ArrayList<>();
        dsCapcongnhan = new ArrayList<>();
        dsLoaiDiTich = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            dsCapcongnhan = bundle.getStringArrayList("DsCapcongnhan");
            dsquanhuyen = bundle.getStringArrayList("DsQuanhuyen");
            dsLoaiDiTich = bundle.getStringArrayList("DsLoaiDiTich");
        }
    }
}
