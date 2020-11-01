package com.example.dulichhaiphong.Fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dulichhaiphong.Adapter.Adapter_danhmuc;
import com.example.dulichhaiphong.Model.DanhMuc;
import com.example.dulichhaiphong.Model.TimkiemTamlinh;
import com.example.dulichhaiphong.Model.TimkiemVinhDaoHangDong;
import com.example.dulichhaiphong.R;

import java.util.ArrayList;
import java.util.List;

public class Fragment_search_Dao extends DialogFragment {
    private View view;
    Spinner spDanhMuc;
    ImageView imgDong;
    Button btnLoc;
    Integer idDanhMuc;
    Adapter_danhmuc adapterDanhMuc;
    private List<DanhMuc> arrayDanhMuc;
    TimkiemVinhDaoHangDong timkiem;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_dao,container,false);
        Anhxa();
        Khoitao();
        adapterDanhMuc = new Adapter_danhmuc(getActivity(),R.layout.item_danhmuc,arrayDanhMuc);
        spDanhMuc.setAdapter(adapterDanhMuc);
        timkiem = (TimkiemVinhDaoHangDong) getActivity();
        imgDong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        spDanhMuc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idDanhMuc = arrayDanhMuc.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timkiem.Timkiem(idDanhMuc);
                dismiss();
            }
        });
        return view;
    }
    private void Anhxa(){
        spDanhMuc = (Spinner) view.findViewById(R.id.spinnerDiadanh);
        btnLoc = (Button) view.findViewById(R.id.btnLocketqua);
        imgDong = (ImageView) view.findViewById(R.id.imgClose);
    }
    private void Khoitao(){
        arrayDanhMuc = new ArrayList<>();
        Bundle bundle = getArguments();
        if(bundle!=null){
            arrayDanhMuc = (ArrayList<DanhMuc>) bundle.getSerializable("tenDanhMuc");
        }
    }

}
