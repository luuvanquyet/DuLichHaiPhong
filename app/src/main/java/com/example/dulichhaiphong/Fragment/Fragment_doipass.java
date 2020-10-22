package com.example.dulichhaiphong.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dulichhaiphong.Activity.CapnhatPassActivity;
import com.example.dulichhaiphong.Activity.LoginActivity;
import com.example.dulichhaiphong.Model.SessionManager;
import com.example.dulichhaiphong.R;
import com.example.dulichhaiphong.ultil.CheckConNection;
import com.example.dulichhaiphong.ultil.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Fragment_doipass extends DialogFragment {
    private Button btnXacnhan;
    private EditText eMatkhau,eXacnhanMK;
    private ImageView btnBack;
    private ProgressBar loading;
    private String idtaikhoan;
    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_doipass,container,false);
        AnhXa();
        if(CheckConNection.haveNetwordConnection(getActivity())){
            btnXacnhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String mMatkhau = eMatkhau.getText().toString().trim();
                    String mXacNhanMk = eXacnhanMK.getText().toString().trim();
                    if(mMatkhau.isEmpty()||mXacNhanMk.isEmpty()){
                        if(mMatkhau.isEmpty()){
                            eMatkhau.setError("Bạn chưa nhập mật khẩu!");
                        }
                        if(mXacNhanMk.isEmpty()){
                            eXacnhanMK.setError("Bạn chưa xác nhận mật khẩu!");
                        }
                    }else{
                        if(!mMatkhau.equals(mXacNhanMk)){
                            eXacnhanMK.setError("Bạn nhập mật khẩu chưa chính xác!");
                        }else{
                            CapnhatMatkhau(mMatkhau);
                        }
                    }
                }
            });
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment_login fragment_login = new Fragment_login();
                    fragment_login.show(getActivity().getSupportFragmentManager(),"Login");
                    dismiss();
                }
            });
        }else{
            CheckConNection.ShowToast_Short(getActivity(),"Mời bạn kiểm tra lại Internet!");
        }
        return view;
    }
    private void AnhXa(){
        btnXacnhan = (Button) view.findViewById(R.id.btnXacNhan);
        eMatkhau = (EditText) view.findViewById(R.id.matkhaumoi);
        eXacnhanMK = (EditText) view.findViewById(R.id.matkhauxacnhan);
        btnBack = (ImageView) view.findViewById(R.id.back);
        loading = (ProgressBar) view.findViewById(R.id.loading);
    }
    private void CapnhatMatkhau(final String mMatkhau){
        loading.setVisibility(View.VISIBLE);
        btnXacnhan.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.url_update_pass, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if(success.equals("1")){
                        Toast.makeText(getActivity(),"Đổi mật khẩu thành công!",Toast.LENGTH_SHORT).show();
                        btnXacnhan.setVisibility(View.GONE);
                        Fragment_login fragment_login = new Fragment_login();
                        fragment_login.show(getActivity().getSupportFragmentManager(),"Login");
                        dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"Lỗi",Toast.LENGTH_SHORT).show();
                    btnXacnhan.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Lỗi",Toast.LENGTH_SHORT).show();
                btnXacnhan.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id",idtaikhoan);
                params.put("password",mMatkhau);
                params.put("key", Server.key);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        Bundle bundle = getArguments();
        if(bundle!=null){
            idtaikhoan = bundle.getString("idtaikhoan");
        }
    }
}
