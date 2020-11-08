package com.example.dulichhaiphong.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dulichhaiphong.Adapter.Slider_Adapter;
import com.example.dulichhaiphong.Fragment.Chua_Fragment;
import com.example.dulichhaiphong.Fragment.Den_fragment;
import com.example.dulichhaiphong.Fragment.Dinh_fragmnet;
import com.example.dulichhaiphong.Model.Anh_Slider_URL;
import com.example.dulichhaiphong.Model.Baiviet;
import com.example.dulichhaiphong.Model.TimkiemTamlinh;
import com.example.dulichhaiphong.Model.TruyenDataPass;
import com.example.dulichhaiphong.R;
import com.example.dulichhaiphong.ultil.CheckConNection;
import com.example.dulichhaiphong.ultil.Server;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DiaDiemTamLinh extends AppCompatActivity implements TimkiemTamlinh {
    BottomNavigationView bottomNavigationView;
    ArrayList<String> arrayCapcongnhan,arrayLoaiditich,arrayQuanHuyen;
    String tenCapCongNhan,tenLoaiDitich,tenQuanhuyen;
    Bundle bundle;
    Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dia_diem_tam_linh);
        if(CheckConNection.haveNetwordConnection(getApplicationContext())){
            Loaddulieu_Fragment();
            tenCapCongNhan ="";
            tenLoaiDitich="";
            tenQuanhuyen="";
            bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    fragment = null;
                    switch (item.getItemId()){
                        case R.id.den:
                            fragment = new Den_fragment();
                            bundle = new Bundle();
                            Bundle_Filter(bundle);
                            bundle.putString("TenCap",tenCapCongNhan);
                            bundle.putString("TenQuanHuyen",tenQuanhuyen);
                            bundle.putString("TenLoaiDitich",tenLoaiDitich);
                            bundle.putString("TenView","den");
                            break;
                        case  R.id.chua:
                            fragment = new Chua_Fragment();
                            bundle = new Bundle();
                            Bundle_Filter(bundle);
                            bundle.putString("TenCap",tenCapCongNhan);
                            bundle.putString("TenQuanHuyen",tenQuanhuyen);
                            bundle.putString("TenLoaiDitich",tenLoaiDitich);
                            bundle.putString("TenView","chua");
                            break;

                        case  R.id.dinh:
                            fragment = new Dinh_fragmnet();
                            bundle = new Bundle();
                            Bundle_Filter(bundle);
                            bundle.putString("TenCap",tenCapCongNhan);
                            bundle.putString("TenQuanHuyen",tenQuanhuyen);
                            bundle.putString("TenLoaiDitich",tenLoaiDitich);
                            bundle.putString("TenView","dinh");
                            break;
                    }
                    fragment.setArguments(bundle);
                    final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container,fragment).commit();
                    return true;
                }
            });
            bottomNavigationView.setSelectedItemId(R.id.chua);
        }else{
            CheckConNection.ShowToast_Short(getApplicationContext(),"Mời bạn kiểm tra lại Internet!");
            finish();
        }


    }
    private void Bundle_Filter(Bundle bundle){
        bundle.putStringArrayList("DsCapcongnhan",arrayCapcongnhan);
        bundle.putStringArrayList("DsQuanhuyen",arrayQuanHuyen);
        bundle.putStringArrayList("DsLoaiDiTich",arrayLoaiditich);
    }

    private void Loaddulieu_Fragment(){
        LoadFilter();

    }

    private void LoadFilter(){
        arrayCapcongnhan = new ArrayList<>();
        arrayCapcongnhan.add("Chọn tất cả");
        ReadFilter(Server.url_read_capcongnhan,arrayCapcongnhan,"Tencap");
        arrayQuanHuyen = new ArrayList<>();
        arrayQuanHuyen.add("Chọn tất cả");
        ReadFilter(Server.url_read_quanhuyen,arrayQuanHuyen,"TenQuan");
        arrayLoaiditich = new ArrayList<>();
        arrayLoaiditich.add("Chọn tất cả");
        ReadFilter(Server.url_read_loaiditich,arrayLoaiditich,"TenLoaiDiTich");
    }

    private void ReadFilter(String url_read_data_filter, final ArrayList<String> arrayList, final String mname){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_read_data_filter, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("read");
                    if(success.equals("1")){
                        for(int i = 0; i<jsonArray.length();i++){
                            JSONObject jsonten = jsonArray.getJSONObject(i);
                            String ten = jsonten.getString(mname);
                            arrayList.add(ten);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DiaDiemTamLinh.this,"Lỗi",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DiaDiemTamLinh.this,"Lỗi",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("key",Server.key);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void Timkiem(String tenView, String tenCap, String tenLoaiDiTich, String tenQuan) {
        if(tenView.equals("chua")){
            tenCapCongNhan = tenCap;
            tenQuanhuyen = tenQuan;
            tenLoaiDitich = tenLoaiDiTich;
            bottomNavigationView.setSelectedItemId(R.id.chua);
        }
        if(tenView.equals("dinh")){
            tenCapCongNhan = tenCap;
            tenQuanhuyen = tenQuan;
            tenLoaiDitich = tenLoaiDiTich;
            bottomNavigationView.setSelectedItemId(R.id.dinh);
        }
        if(tenView.equals("den")){
            tenCapCongNhan = tenCap;
            tenQuanhuyen = tenQuan;
            tenLoaiDitich = tenLoaiDiTich;
            bottomNavigationView.setSelectedItemId(R.id.den);
        }
    }
}