package com.example.dulichhaiphong.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dulichhaiphong.Adapter.Adapter_lichsuphattrien;
import com.example.dulichhaiphong.Adapter.FAQAdapter;
import com.example.dulichhaiphong.Model.Baiviet;
import com.example.dulichhaiphong.Model.FAQ;
import com.example.dulichhaiphong.Model.Timkiemditichlichsu;
import com.example.dulichhaiphong.R;
import com.example.dulichhaiphong.ultil.CheckConNection;
import com.example.dulichhaiphong.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LichsuphattrienActivity extends AppCompatActivity {
    RecyclerView recyclerViewLichsu;
    Toolbar toolbar;
    Adapter_lichsuphattrien adapter_lichsuphattrien;
    ImageView back;
    ArrayList<Baiviet> arrayListBaiviet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lichsuphattrien);
        Anhxa();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if(CheckConNection.haveNetwordConnection(getApplicationContext())){
            HienLRecyclerView();
        }else {
            CheckConNection.ShowToast_Short(getApplicationContext(),"Mời bạn kiểm tra lại Internet!");
            finish();
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }
    private void Anhxa(){
        recyclerViewLichsu = findViewById(R.id.recyclerview_lichsuphattrien);
        toolbar = (Toolbar) findViewById(R.id.toolbar_faq);
        back = (ImageView) findViewById(R.id.back);
    }
    private void HienLRecyclerView(){
        arrayListBaiviet = new ArrayList<>();
        Timkiem_baiviet(Server.url_read_lichsuphattrien,arrayListBaiviet);
        recyclerViewLichsu.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewLichsu.setLayoutManager(layoutManager);
        adapter_lichsuphattrien = new Adapter_lichsuphattrien(this,arrayListBaiviet,this);
        recyclerViewLichsu.setAdapter(adapter_lichsuphattrien);
    }
    private void Timkiem_baiviet(final String url_read_danhsach_baiviet, final ArrayList<Baiviet> danhSachBaiviet){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_read_danhsach_baiviet, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if(response!=null){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("baiviet");
                        if(success.equals("1")){
                            for(int i = 0; i<jsonArray.length();i++){
                                JSONObject jsonBaiviet = jsonArray.getJSONObject(i);
                                String id = jsonBaiviet.getString("Id");
                                String tenBaiviet = jsonBaiviet.getString("TenBaiViet");
                                String tomtat = jsonBaiviet.getString("TomTat");
                                String anhDaidien = jsonBaiviet.getString("AnhDaiDien");
                                String soLike = jsonBaiviet.getString("SoLike");
                                String ngayDang = jsonBaiviet.getString("NgayDang");
                                String noiDung = jsonBaiviet.getString("Noidung");
                                danhSachBaiviet.add(new Baiviet(id,tenBaiviet,tomtat,soLike,ngayDang,anhDaidien,noiDung));

                            }
                            adapter_lichsuphattrien.notifyDataSetChanged();
                            if(danhSachBaiviet.size() ==0){
                                Toast.makeText(LichsuphattrienActivity.this,"Không tìm có bài viết nào!",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(LichsuphattrienActivity.this,"Có " + danhSachBaiviet.size()+" bài viết!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LichsuphattrienActivity.this,"Lỗi load dữ liệu danh sách bài viết",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LichsuphattrienActivity.this,"Lỗi load dữ liệu danh sách bài viết",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("key",Server.key);
                return  params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu_search_faq, menu);
        MenuItem search_item = menu.findItem(R.id.search_view);
        SearchView searchView = (SearchView) search_item.getActionView();
        searchView.setFocusable(false);
        searchView.setBackgroundResource(R.drawable.bg_white_rounded);
        searchView.setQueryHint("Tìm kiếm bài viết");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter_lichsuphattrien.getFilter().filter(s);
                return true;
            }
        });
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.search_view:
                return true;
        }
        return true;
    }
}