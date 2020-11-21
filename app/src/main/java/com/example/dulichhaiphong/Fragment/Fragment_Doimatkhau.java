package com.example.dulichhaiphong.Fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.dulichhaiphong.Model.TruyenDataPass;
import com.example.dulichhaiphong.R;

public class Fragment_Doimatkhau extends DialogFragment {
    EditText editMatkhau,cMatkhau;
    Button btnDoimatkhau;
    ImageView btnCancel;
    TruyenDataPass truyenDataPass;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_doimatkhau,container,false);
        Anhxa();
        truyenDataPass = (TruyenDataPass) getActivity();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                truyenDataPass.DongDialog();
            }
        });
        btnDoimatkhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mMatkhau = editMatkhau.getText().toString().trim();
                String mCmatkhau = cMatkhau.getText().toString().trim();
                if(mCmatkhau.isEmpty()||mCmatkhau.isEmpty()||mMatkhau.length()<8){
                    if(mMatkhau.equals("")){
                        editMatkhau.setError("Mời bạn nhập mật khẩu!");
                    }
                    if(mMatkhau.length()<8){
                        editMatkhau.setError("Mời bạn nhập ít nhất là 8 ký tự");
                    }
                    if(mCmatkhau.equals("")){
                        cMatkhau.setError("Mời bạn nhập lại mật khẩu!");
                    }
                }else{
                    if(mMatkhau.equals(mCmatkhau)){
                        truyenDataPass.Doimatkhau(mMatkhau);
                    }else{
                        cMatkhau.setError("Mật khẩu xác nhận sai");
                    }
                }
            }
        });
        return view;
    }
    private void Anhxa(){
        editMatkhau = (EditText) view.findViewById(R.id.matkhaumoi);
        cMatkhau = (EditText) view.findViewById(R.id.cmatkhaumoi);
        btnDoimatkhau = (Button) view.findViewById(R.id.btnDoimatkhau);
        btnCancel = (ImageView) view.findViewById(R.id.btnCancel);
    }
}
