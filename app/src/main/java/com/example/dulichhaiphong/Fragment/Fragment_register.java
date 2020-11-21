package com.example.dulichhaiphong.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.example.dulichhaiphong.Activity.LoginActivity;
import com.example.dulichhaiphong.Activity.RegiesterActivity;
import com.example.dulichhaiphong.Model.SessionManager;
import com.example.dulichhaiphong.R;
import com.example.dulichhaiphong.ultil.CheckConNection;
import com.example.dulichhaiphong.ultil.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Fragment_register extends DialogFragment {
    private View view;
    private EditText name,email,password,c_password,account;
    private Button btnRegister;
    private ProgressBar loading;
    private ImageView imgBack;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register,container,false);
        Anhxa();
        if(CheckConNection.haveNetwordConnection(getActivity())){
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String mname = name.getText().toString().trim();
                    final String memail = email.getText().toString().trim();
                    final String mpassword = password.getText().toString().trim();
                    final String maccount = account.getText().toString().trim();
                    String cmpassword = c_password.getText().toString().trim();
                    if(mname.isEmpty()||memail.isEmpty()||mpassword.isEmpty()||cmpassword.isEmpty()||maccount.isEmpty()||mpassword.length()<8||maccount.length() < 8){
                        if(mname.isEmpty()){
                            name.setError("Mời bạn nhập họ tên");
                        }
                        if(memail.isEmpty()){
                            email.setError("Mời bạn nhập Email");
                        }
                        if(mpassword.isEmpty()){
                            password.setError("Mời bạn nhập Password");
                        }
                        if(mpassword.length()<8){
                            password.setError("Mời bạn nhập ít nhất là 8 ký tự");
                        }
                        if(cmpassword.isEmpty()){
                            c_password.setError("Mời bạn nhập lại Password");
                        }
                        if(maccount.isEmpty()){
                            account.setError("Mời bạn nhập tên tài khoản");
                        }
                        if(maccount.length() < 8){
                            account.setError("Mời bạn nhập ít nhất là 8 ký tự");
                        }
                    }else{
                        if(cmpassword.equals(mpassword)&& Kiemtraemail(memail)){
                            Regist(mname,memail,mpassword,maccount);
                        }else{
                            if(!Kiemtraemail(memail)){
                                email.setError("Email không hợp lệ!");
                            }
                            if(!cmpassword.equals(mpassword)){
                                c_password.setError("Mời bạn nhập lại Password");
                            }

                        }
                    }
                }
            });
            imgBack.setOnClickListener(new View.OnClickListener() {
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
    private boolean Kiemtraemail(String memail){
        if(Patterns.EMAIL_ADDRESS.matcher(memail).matches()){
            return true;
        }
        return false;
    }
    private void Anhxa(){
        loading = (ProgressBar) view.findViewById(R.id.loading);
        name = (EditText) view.findViewById(R.id.name);
        email = (EditText) view.findViewById(R.id.email);
        account = (EditText) view.findViewById(R.id.account);
        password = (EditText) view.findViewById(R.id.password);
        c_password = (EditText) view.findViewById(R.id.comfrim);
        btnRegister = (Button) view.findViewById(R.id.btnRegist);
        imgBack = (ImageView) view.findViewById(R.id.backLogin);
    }
    private void Regist(final String mname,final String memail,final String mpassword,final String maccount){
        loading.setVisibility(View.VISIBLE);
        btnRegister.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.url_register, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String sucess = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    if(sucess.equals("1")){
                        Fragment_login fragment_login = new Fragment_login();
                        fragment_login.show(getActivity().getSupportFragmentManager(),"Login");
                        dismiss();
                    }else{
                        if(message.equals("trungtaikhoan")){
                            account.setError("Tài khoản đã tồn tại!");
                            loading.setVisibility(View.GONE);
                            btnRegister.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"Đăng ký thất bại!" + e.toString(),Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    btnRegister.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Đăng ký thất bại!",Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
                btnRegister.setVisibility(View.VISIBLE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("account",maccount);
                params.put("name",mname);
                params.put("email",memail);
                params.put("password",mpassword);
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
    }
}
