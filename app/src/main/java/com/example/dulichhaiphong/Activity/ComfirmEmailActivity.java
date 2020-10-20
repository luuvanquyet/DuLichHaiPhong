package com.example.dulichhaiphong.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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
import com.example.dulichhaiphong.ultil.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ComfirmEmailActivity extends AppCompatActivity {
    private Button btnXacnhan;
    private EditText editAccount;
    private ImageView btnBack;
    private ProgressBar loading;
    private static String url_check_email = "http://dulichhaiphong.xyz/api/check_account.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comfirm_email);
        Anhxa();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ComfirmEmailActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnXacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maccount = editAccount.getText().toString().trim();
                if(maccount.isEmpty()){
                    editAccount.setError("Mời bạn nhập tên tài khoản!");
                }else{
                    checkAccount(maccount);
                }
            }
        });
    }
    private void checkAccount(final String maccount){
        loading.setVisibility(View.VISIBLE);
        btnXacnhan.setVisibility(View.GONE);;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_check_email, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    if(success.equals("1")){
                        String id = jsonObject.getString("id");
                        btnXacnhan.setVisibility(View.GONE);
                        Intent intent = new Intent(ComfirmEmailActivity.this,CapnhatPassActivity.class);
                        intent.putExtra("id",id);
                        startActivity(intent);
                        finish();
                    }else{
                        if(message.equals("Khongtontai")){
                            editAccount.setError("Tài khoản không tồn tại!");
                            btnXacnhan.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ComfirmEmailActivity.this,"Lỗi",Toast.LENGTH_SHORT).show();
                    btnXacnhan.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ComfirmEmailActivity.this,"Lỗi",Toast.LENGTH_SHORT).show();
                btnXacnhan.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("account",maccount);
                params.put("key", Server.key);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private boolean Kiemtradinhdangemail(String memail){
        if(Patterns.EMAIL_ADDRESS.matcher(memail).matches()){
            return true;
        }
        return false;
    }
    private void Anhxa(){
        btnXacnhan = (Button) findViewById(R.id.btnXacNhan);
        editAccount = (EditText) findViewById(R.id.account);
        btnBack = (ImageView) findViewById(R.id.back);
        loading = (ProgressBar) findViewById(R.id.loading);
    }
}