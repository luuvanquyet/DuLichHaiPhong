package com.example.dulichhaiphong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class CapnhatPassActivity extends AppCompatActivity {
    private  static String getId;
    private Button btnXacnhan;
    private EditText eMatkhau,eXacnhanMK;
    private ImageView btnBack;
    private ProgressBar loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capnhat_pass);
        final Intent intent = getIntent();
        getId = intent.getStringExtra("id");
        AnhXa();
        if(CheckConNection.haveNetwordConnection(getApplicationContext())){
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CapnhatPassActivity.this,ComfirmEmailActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            btnXacnhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String mMatkhau = eMatkhau.getText().toString().trim();
                    String mXacNhanMk = eXacnhanMK.getText().toString().trim();
                    if(mMatkhau.isEmpty()||mXacNhanMk.isEmpty()||mMatkhau.length()<8){
                        if(mMatkhau.isEmpty()){
                            eMatkhau.setError("Bạn chưa nhập mật khẩu!");
                        }
                        if(mMatkhau.length()<8){
                            eMatkhau.setError("Mời bạn nhập ít nhất là 8 ký tự");
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
        }else{
            CheckConNection.ShowToast_Short(getApplicationContext(),"Mời bạn kiểm tra lại Internet!");
        }

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
                        Toast.makeText(CapnhatPassActivity.this,"Đổi mật khẩu thành công!",Toast.LENGTH_SHORT).show();
                        btnXacnhan.setVisibility(View.GONE);
                        Intent intent = new Intent(CapnhatPassActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(CapnhatPassActivity.this,"Lỗi",Toast.LENGTH_SHORT).show();
                    btnXacnhan.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CapnhatPassActivity.this,"Lỗi",Toast.LENGTH_SHORT).show();
                btnXacnhan.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id",getId);
                params.put("password",mMatkhau);
                params.put("key", Server.key);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void AnhXa(){
        btnXacnhan = (Button) findViewById(R.id.btnXacNhan);
        eMatkhau = (EditText) findViewById(R.id.matkhaumoi);
        eXacnhanMK = (EditText) findViewById(R.id.matkhauxacnhan);
        btnBack = (ImageView) findViewById(R.id.back);
        loading = (ProgressBar) findViewById(R.id.loading);
    }
}