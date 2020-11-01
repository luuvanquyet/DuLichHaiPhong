package com.example.dulichhaiphong.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dulichhaiphong.Adapter.AnhAdapter;
import com.example.dulichhaiphong.Fragment.Fragment_Comment;
import com.example.dulichhaiphong.Fragment.Fragment_Danhgia;
import com.example.dulichhaiphong.Fragment.Fragment_login;
import com.example.dulichhaiphong.Model.AnhLienQuan;
import com.example.dulichhaiphong.Model.Baiviet;
import com.example.dulichhaiphong.Model.SessionManager;
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
    private CoordinatorLayout content;
    private RecyclerView recyclerView;
    private ImageView anhHeader;
    private WebView txtNoidung;
    private TextView txtTieude,txtNgaydang;
    private AnhAdapter adapterAnh;
    public static String idbaiviet;
    private FloatingActionButton fPlus,fView,fComment,fShare;
    public static FloatingActionButton fLike;
    public static boolean isDanhgia = false;
    boolean anHien = false;
    private String getId;
    private HashMap<String,String> user;
    public static boolean liekd = false;
    SessionManager sessionManager;
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
            sessionManager = new SessionManager(this);
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
                    if(sessionManager.isLoggin()){
                        if(liekd ==true){
                            Toast.makeText(ChitietBaiviet.this,"Bạn đã like bài viết này rồi!",Toast.LENGTH_SHORT).show();
                        }else{
                            HashMap<String,String> user = sessionManager.getUserDetail();
                            getId = user.get(SessionManager.ID);
                            Insert_like_baiviet(Server.url_insert_like_baiviet,idbaiviet,getId);
                        }
                    }else{
                        Fragment_login fragment_login = new Fragment_login();
                        fragment_login.show(getSupportFragmentManager(),"login");
                    }
                }
            });
            fComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   if(sessionManager.isLoggin()){
                       HashMap<String,String> user = sessionManager.getUserDetail();
                       getId = user.get(SessionManager.ID);
                       Fragment_Danhgia fragmentComment = new Fragment_Danhgia();
                       Bundle bundle = new Bundle();
                       bundle.putString("idbaiviet",idbaiviet);
                       bundle.putString("iduser",getId);
                       fragmentComment.setArguments(bundle);
                       fragmentComment.show(getSupportFragmentManager(),"Danhgia");
                   }else{
                       Fragment_login fragment_login = new Fragment_login();
                       fragment_login.show(getSupportFragmentManager(),"login");
                       isDanhgia = true;
                   }
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
        content = (CoordinatorLayout) findViewById(R.id.content);
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
    private void Insert_like_baiviet(String url, final String idbaiviet, final String iduser){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response!=null){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        if(success.equals("1")){
                            Toast.makeText(ChitietBaiviet.this,"Like bài viết thành công!",Toast.LENGTH_SHORT).show();
                            fLike.setImageResource(R.drawable.ic_liked);
                            liekd = true;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChitietBaiviet.this,"Lỗi like",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChitietBaiviet.this,"Lỗi like",Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void Check_like_baiviet(String url,final String idbaiviet,final String iduser){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response!=null){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        if(success.equals("1")){
                            fLike.setImageResource(R.drawable.ic_liked);
                            liekd = true;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ChitietBaiviet.this,"Lỗi kiểm tra like",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChitietBaiviet.this,"Lỗi kiểm tra like",Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
                                    if(id.equals("null")){
                                        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
                                        params.height = 10;
                                        recyclerView.setLayoutParams(params);
                                        recyclerView.setVisibility(View.GONE);
                                        final RelativeLayout.LayoutParams layoutparams = (RelativeLayout.LayoutParams)content.getLayoutParams();
                                        layoutparams.setMargins(0,0,0,10);
                                        content.setLayoutParams(layoutparams);

                                    }
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

    @Override
    protected void onResume() {
        super.onResume();
        if(sessionManager.isLoggin()){
            HashMap<String,String> user = sessionManager.getUserDetail();
            getId = user.get(SessionManager.ID);
            Check_like_baiviet(Server.url_check_like_baiviet,idbaiviet,getId);
        }
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