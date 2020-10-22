package com.example.dulichhaiphong.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dulichhaiphong.R;
import com.example.dulichhaiphong.ultil.CheckConNection;
import com.example.dulichhaiphong.ultil.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegiesterActivity extends AppCompatActivity {
    private EditText name,email,password,c_password,account;
    private Button btnRegister;
    private ProgressBar loading;
    private ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regiester);
        Anhxa();
        if(CheckConNection.haveNetwordConnection(getApplicationContext())){
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String mname = name.getText().toString().trim();
                    final String memail = email.getText().toString().trim();
                    final String mpassword = password.getText().toString().trim();
                    final String maccount = account.getText().toString().trim();
                    String cmpassword = c_password.getText().toString().trim();
                    if(mname.isEmpty()||memail.isEmpty()||mpassword.isEmpty()||cmpassword.isEmpty()||maccount.isEmpty()){
                        if(mname.isEmpty()){
                            name.setError("Mời bạn nhập họ tên");
                        }
                        if(memail.isEmpty()){
                            email.setError("Mời bạn nhập Email");
                        }
                        if(mpassword.isEmpty()){
                            password.setError("Mời bạn nhập Password");
                        }
                        if(cmpassword.isEmpty()){
                            c_password.setError("Mời bạn nhập lại Password");
                        }
                        if(maccount.isEmpty()){
                            account.setError("Mời bạn nhập tên tài khoản");
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
        }else{
            CheckConNection.ShowToast_Short(getApplicationContext(),"Mời bạn kiểm tra lại Internet!");
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegiesterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private boolean Kiemtraemail(String memail){
        if(Patterns.EMAIL_ADDRESS.matcher(memail).matches()){
            return true;
        }
        return false;
    }
    private void Anhxa(){
        loading = (ProgressBar) findViewById(R.id.loading);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        account = (EditText) findViewById(R.id.account);
        password = (EditText) findViewById(R.id.password);
        c_password = (EditText) findViewById(R.id.comfrim);
        btnRegister = (Button) findViewById(R.id.btnRegist);
        imgBack = (ImageView) findViewById(R.id.backLogin);
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
                        Toast.makeText(RegiesterActivity.this,"Đăng ký thành công!",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegiesterActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }else{
                        if(message.equals("trungtaikhoan")){
                            account.setError("Tài khoản đã tồn tại!");
                            loading.setVisibility(View.GONE);
                            btnRegister.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RegiesterActivity.this,"Đăng ký thất bại!" + e.toString(),Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    btnRegister.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegiesterActivity.this,"Đăng ký thất bại!",Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}