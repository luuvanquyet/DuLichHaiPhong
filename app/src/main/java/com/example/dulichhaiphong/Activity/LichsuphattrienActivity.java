package com.example.dulichhaiphong.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
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

import com.example.dulichhaiphong.Model.Baiviet;

import com.example.dulichhaiphong.R;
import com.example.dulichhaiphong.ultil.CheckConNection;
import com.example.dulichhaiphong.ultil.Server;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;

import com.google.android.youtube.player.YouTubePlayerFragment;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.dulichhaiphong.R.*;

public class LichsuphattrienActivity extends AppCompatActivity {
    RecyclerView recyclerViewLichsu;
    Toolbar toolbar;
    Adapter_lichsuphattrien adapter_lichsuphattrien;
    ArrayList<Baiviet> arrayListBaiviet;
    private YouTubePlayer youTubePlayer;
    public static final String DEVELOPER_KEY = "AIzaSyBJRCwSTMUDmf8T35MGvMEVny3j4RIOK3o";
    private static final String VIDEO_ID = "rjKD2j3qQz0";
    private YouTubePlayerFragment youTubePlayerFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_lichsuphattrien);
        Anhxa();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Lịch sử phát triển");
        toolbar.setTitleTextColor(Color.WHITE);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        if(CheckConNection.haveNetwordConnection(getApplicationContext())){
            HienLRecyclerView();
            initializeYoutubePlayer();
        }else {
            CheckConNection.ShowToast_Short(getApplicationContext(),"Mời bạn kiểm tra lại Internet!");
            finish();
        }
    }
    private void Anhxa(){
        recyclerViewLichsu = findViewById(id.recyclerview_lichsuphattrien);
        toolbar = (Toolbar) findViewById(id.toolbar_faq);

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
                                String code = jsonBaiviet.getString("Code");
                                danhSachBaiviet.add(new Baiviet(id,tenBaiviet,tomtat,soLike,ngayDang,anhDaidien,noiDung,code));
                            }
                            Toast.makeText(LichsuphattrienActivity.this,response,Toast.LENGTH_LONG).show();
                            adapter_lichsuphattrien.notifyDataSetChanged();
                            if(danhSachBaiviet.size() ==0){
                                Toast.makeText(LichsuphattrienActivity.this,"Không tìm có bài viết nào!",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(LichsuphattrienActivity.this,"Có " + danhSachBaiviet.size()+" bài viết!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LichsuphattrienActivity.this,"Không đọc được dữ liệu",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("AAA", error.toString());
                Toast.makeText(LichsuphattrienActivity.this,"Lỗi load dữ liệu bài viết",Toast.LENGTH_SHORT).show();
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
        MenuItem search_item = menu.findItem(id.search_view);
        SearchView searchView = (SearchView) search_item.getActionView();
        searchView.setFocusable(false);
        searchView.setBackgroundResource(drawable.bg_white_rounded);
        searchView.setQueryHint("Tìm kiếm bài viết");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter_lichsuphattrien.getFilter().filter(s);
                youTubePlayer.pause();
                return true;
            }
        });
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case id.search_view:
                return true;
        }
        return true;
    }

    private void initializeYoutubePlayer() {

        youTubePlayerFragment = (YouTubePlayerFragment) getFragmentManager()
                .findFragmentById(id.youtube_fragment);

        if (youTubePlayerFragment == null)
            return;

        youTubePlayerFragment.initialize(DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                                boolean wasRestored) {
                if (!wasRestored) {
                    youTubePlayer = player;
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    youTubePlayer.loadVideo(VIDEO_ID);
                    youTubePlayer.play();
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                Toast.makeText(LichsuphattrienActivity.this, "Lỗi tải video", Toast.LENGTH_SHORT).show();
            }
        });

    }

}