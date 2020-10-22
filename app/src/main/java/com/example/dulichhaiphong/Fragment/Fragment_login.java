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
import android.widget.TextView;
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
import com.example.dulichhaiphong.Activity.ChitietBaiviet;
import com.example.dulichhaiphong.Activity.LoginActivity;
import com.example.dulichhaiphong.Activity.Trangchu;
import com.example.dulichhaiphong.Model.SessionManager;
import com.example.dulichhaiphong.R;
import com.example.dulichhaiphong.ultil.CheckConNection;
import com.example.dulichhaiphong.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Fragment_login extends DialogFragment {
    private View view;
    private EditText account,password;
    private Button btnLogin;
    private TextView linkRegist,linkDoimatkhau;
    private ProgressBar loading;
    private ImageView back;
    SessionManager sessionManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.fragment_login,container,false);
       Anhxa();
       if(CheckConNection.haveNetwordConnection(getContext())){
           btnLogin.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   String mAccount = account.getText().toString().trim();
                   String mPass = password.getText().toString().trim();
                   if(mAccount.isEmpty()||mPass.isEmpty()){
                       if(mAccount.isEmpty()){
                           account.setError("Mời bạn nhập email");
                       }
                       if(mPass.isEmpty()){
                           password.setError("Mời bạn nhập password");
                       }
                   }else{
                       Login(mAccount,mPass);
                   }
               }
           });
           linkDoimatkhau.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Fragment_comfirm fragment_comfirm = new Fragment_comfirm();
                   fragment_comfirm.show(getActivity().getSupportFragmentManager(),"comfirm");
                   dismiss();
               }
           });
           linkRegist.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Fragment_register fragment_register = new Fragment_register();
                   fragment_register.show(getActivity().getSupportFragmentManager(),"Register");
                   dismiss();
               }
           });
       }else{
           Toast.makeText(getContext(),"Mời bạn kiểm tra lại Internet!",Toast.LENGTH_SHORT).show();
       }
       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               ChitietBaiviet.isDanhgia = false;
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
        sessionManager = new SessionManager(getActivity());
    }
    private void Check_like_baiviet(String url,final String idbaiviet,final String iduser){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response!=null){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        if(success.equals("1")) {
                            ChitietBaiviet.fLike.setImageResource(R.drawable.ic_liked);
                            ChitietBaiviet.liekd = true;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(),"Lỗi kiểm tra like",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Lỗi kiểm tra like",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("key",Server.key);
                params.put("idUser",iduser);
                params.put("idBaiviet",idbaiviet);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    private void Insert_like_baiviet(String url, final String idbaiviet, final String iduser){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response!=null){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        if(success.equals("1")){
                            ChitietBaiviet.fLike.setImageResource(R.drawable.ic_liked);
                            ChitietBaiviet.liekd = true;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(),"Lỗi like",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Lỗi like",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("key",Server.key);
                params.put("idUser",iduser);
                params.put("idBaiviet",idbaiviet);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    private void Anhxa(){
        loading = (ProgressBar) view.findViewById(R.id.loading);
        account = (EditText) view.findViewById(R.id.account);
        password = (EditText) view.findViewById(R.id.password);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        linkRegist = (TextView) view.findViewById(R.id.linkRegist);
        linkDoimatkhau = (TextView) view.findViewById(R.id.linkQuenpass);
        back = (ImageView) view.findViewById(R.id.imgClose);
    }
    private void Login(final String mAccount, final String mpass){
        loading.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.url_login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    JSONArray jsonArray = jsonObject.getJSONArray("login");
                    if(success.equals("1")){
                        for (int i = 0; i<jsonArray.length();i++){
                            JSONObject jsonOb = jsonArray.getJSONObject(i);
                            String account = jsonOb.getString("account").trim();
                            String id = jsonOb.getString("id").trim();
                            String email = jsonOb.getString("email").trim();
                            sessionManager.createSession(account,email,id);
                            Check_like_baiviet(Server.url_check_like_baiviet,ChitietBaiviet.idbaiviet,id);
                            if(ChitietBaiviet.isDanhgia == true){
                                Fragment_Danhgia fragmentComment = new Fragment_Danhgia();
                                Bundle bundle = new Bundle();
                                bundle.putString("idbaiviet",ChitietBaiviet.idbaiviet);
                                bundle.putString("iduser",id);
                                fragmentComment.setArguments(bundle);
                                fragmentComment.show(getActivity().getSupportFragmentManager(),"Danhgia");
                                dismiss();
                                ChitietBaiviet.isDanhgia = false;
                                loading.setVisibility(View.GONE);
                            }else{
                                if(ChitietBaiviet.liekd == false){
                                    Insert_like_baiviet(Server.url_insert_like_baiviet,ChitietBaiviet.idbaiviet,id);
                                }
                                loading.setVisibility(View.GONE);
                                dismiss();
                            }
                        }
                    }else{
                        if(message.equals("Matkhausai")){
                            password.setError("Sai mai mật khẩu!");
                            loading.setVisibility(View.GONE);
                            btnLogin.setVisibility(View.VISIBLE);
                        }
                        if(message.equals("Khongtontai")){
                            account.setError("Tài khoản không tồn tại!");
                            loading.setVisibility(View.GONE);
                            btnLogin.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"Đăng nhập thất bại! ",Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    btnLogin.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Đăng nhập thất bại!",Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("account",mAccount);
                params.put("password",mpass);
                params.put("key", Server.key);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}
