package com.example.dulichhaiphong.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dulichhaiphong.Adapter.AnhAdapter;
import com.example.dulichhaiphong.Fragment.Anh_Bai_Viet_Fragment;
import com.example.dulichhaiphong.Fragment.Fragment_Cautraloi;
import com.example.dulichhaiphong.Fragment.Fragment_Comment;
import com.example.dulichhaiphong.Model.AnhLienQuan;
import com.example.dulichhaiphong.Model.Baiviet;
import com.example.dulichhaiphong.R;
import com.example.dulichhaiphong.ultil.CheckConNection;
import com.example.dulichhaiphong.ultil.Server;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChitietBaiviet extends AppCompatActivity {
    private ArrayList<AnhLienQuan> anhLienQuanArrayList;
    private RecyclerView recyclerView;
    private ImageView anhHeader;
    private WebView txtNoidung;
    private TextView txtTieude,txtNgaydang;
    private AnhAdapter adapterAnh;
    private String idbaiviet;
    private FloatingActionButton fPlus,fView,fComment,fShare,fLike;
    boolean anHien = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet_baiviet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Bài viết");
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Anhxa();
        Intent intent = getIntent();
        if(intent!=null){
            Baiviet baiviet = (Baiviet) intent.getSerializableExtra("baiviet");
            khoiTaoContent(baiviet);
        }
        if(CheckConNection.haveNetwordConnection(getApplicationContext())){
            initView();
            fPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(anHien == false){
                        fPlus.setImageResource(R.drawable.cancel);
                        Show_Function();
                        anHien = true;
                    }else {
                        fPlus.setImageResource(R.drawable.plus);
                        Hide_Function();
                        anHien = false;
                    }
                }
            });
            fView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment_Comment fragmentComment = new Fragment_Comment();
                    Bundle bundle = new Bundle();
                    bundle.putString("idbaiviet",idbaiviet);
                    fragmentComment.setArguments(bundle);
                    fragmentComment.show(getSupportFragmentManager(),"Cautraloi");
                }
            });
            fLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(ChitietBaiviet.this,"Da chon Like",Toast.LENGTH_SHORT).show();
                }
            });
            fComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(ChitietBaiviet.this,"Da chon Comment",Toast.LENGTH_SHORT).show();
                }
            });
            fShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(ChitietBaiviet.this,"Da chon Share",Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            CheckConNection.ShowToast_Short(getApplicationContext(),"Mời bạn kiểm tra lại INTERNET!");
        }
    }
    private void khoiTaoContent(Baiviet baiviet){
        Picasso.get().load(Server.url_anh+baiviet.getAnhDaidien()).networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE).into(anhHeader);
        txtTieude.setText(baiviet.getTenBaiViet());
        txtNgaydang.setText(baiviet.getNgayDang());
        txtNoidung.getSettings().setJavaScriptEnabled(true);
        txtNoidung.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        txtNoidung.clearView();
        txtNoidung.measure(300, 300);
        txtNoidung.getSettings().setDomStorageEnabled(true);
        txtNoidung.getSettings().setLoadWithOverviewMode(true);
        txtNoidung.loadDataWithBaseURL("http://dulichhaiphong.xyz","<style>img{display: inline;height: auto;max-width: 100%;}</style>"+ baiviet.getNoiDung(),"text/html","utf-8",null);
        idbaiviet = baiviet.getIdBaiViet();
    }
    private void Anhxa(){
        anhHeader = (ImageView) findViewById(R.id.anhTitle);
        txtTieude = (TextView) findViewById(R.id.tieude_baiviet);
        txtNgaydang = (TextView) findViewById(R.id.txtNgaydang);
        txtNoidung = (WebView) findViewById(R.id.txtNoidung);
        fPlus = (FloatingActionButton) findViewById(R.id.btnPlus);
        fView = (FloatingActionButton) findViewById(R.id.btnView);
        fComment = (FloatingActionButton) findViewById(R.id.btnComment);
        fShare = (FloatingActionButton) findViewById(R.id.btnShare);
        fLike = (FloatingActionButton) findViewById(R.id.btnLike);
    }
    private void Show_Function(){
        fView.show();
        fComment.show();
        fShare.show();
        fLike.show();
    }
    private void Hide_Function(){
        fView.hide();
        fComment.hide();
        fShare.hide();
        fLike.hide();
    }
    private void Read_AnhLienQuan(String url, final ArrayList<AnhLienQuan> anhLienQuans){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if(response!=null){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("read");
                        if(success.equals("1")){
                            for(int i = 0; i<jsonArray.length();i++){
                                JSONObject anhlienquan = jsonArray.getJSONObject(i);
                                String id = anhlienquan.getString("Id");
                                String title = anhlienquan.getString("tenTitle");
                                String file = anhlienquan.getString("file");
                                anhLienQuans.add(new AnhLienQuan(id,title,file));
                            }
                            adapterAnh.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChitietBaiviet.this,"Lỗi load ảnh liên quan",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChitietBaiviet.this,"Lỗi load ảnh liên quan",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("key",Server.key);
                params.put("idbaiviet",idbaiviet);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void initView(){
        anhLienQuanArrayList = new ArrayList<>();
        Read_AnhLienQuan(Server.url_read_anh_lien_quan,anhLienQuanArrayList);
        recyclerView = (RecyclerView)findViewById(R.id.recycleview_anh_lien_quan);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapterAnh = new AnhAdapter( this,anhLienQuanArrayList,this);
        recyclerView.setAdapter(adapterAnh);
    }
}