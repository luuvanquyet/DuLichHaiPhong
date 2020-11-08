package com.example.dulichhaiphong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dulichhaiphong.Model.SessionManager;
import com.example.dulichhaiphong.R;
import com.example.dulichhaiphong.ultil.CheckConNection;
import com.example.dulichhaiphong.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText account,password;
    private Button btnLogin;
    private TextView linkRegist,linkDoimatkhau,linkBoqua;
    private ProgressBar loading;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Anhxa();

        sessionManager = new SessionManager(this);
        if(sessionManager.isLoggin()){
            Intent intent = new Intent(LoginActivity.this,Trangchu.class);
            startActivity(intent);
            finish();
        }
        if(CheckConNection.haveNetwordConnection(getApplicationContext())){
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
        }else{
            CheckConNection.ShowToast_Short(getApplicationContext(),"Mời bạn kiểm tra lại Internet!");
            finish();
        }

        linkRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(LoginActivity.this,RegiesterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        linkDoimatkhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(LoginActivity.this,ComfirmEmailActivity.class);
                startActivity(intent);
                finish();
            }
        });
        linkBoqua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,Trangchu.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void Anhxa(){
        loading = (ProgressBar) findViewById(R.id.loading);
        account = (EditText) findViewById(R.id.account);
        password = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        linkRegist = (TextView) findViewById(R.id.linkRegist);
        linkDoimatkhau = (TextView) findViewById(R.id.linkQuenpass);
        linkBoqua = (TextView) findViewById(R.id.linkBoqua);
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
                            Intent intent = new Intent(LoginActivity.this,Trangchu.class);
                            startActivity(intent);
                            finish();
                            loading.setVisibility(View.GONE);
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
                    Toast.makeText(LoginActivity.this,"Đăng nhập thất bại! ",Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    btnLogin.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this,"Đăng nhập thất bại!",Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}