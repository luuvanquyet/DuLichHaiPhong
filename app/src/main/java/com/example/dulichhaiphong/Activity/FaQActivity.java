package com.example.dulichhaiphong.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.dulichhaiphong.Adapter.FAQAdapter;
import com.example.dulichhaiphong.Model.FAQ;
import com.example.dulichhaiphong.R;
import com.example.dulichhaiphong.ultil.CheckConNection;
import com.example.dulichhaiphong.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FaQActivity extends AppCompatActivity {
    RecyclerView recyclerViewFAQ;
    Toolbar toolbar;
    FAQAdapter faqAdapter;
    ImageView back;
    ArrayList<FAQ> arrayListFAQ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fa_q);
        Anhxa();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if(CheckConNection.haveNetwordConnection(getApplicationContext())){
            HienLRecyclerView();
        }else {
            CheckConNection.ShowToast_Short(getApplicationContext(),"Mời bạn kiểm tra lại Internet!");
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FaQActivity.this,Trangchu.class);
                startActivity(intent);
            }
        });
    }
    private void Anhxa(){
        recyclerViewFAQ = findViewById(R.id.recyclerview_FAQ);
        toolbar = (Toolbar) findViewById(R.id.toolbar_faq);
        back = (ImageView) findViewById(R.id.back);
    }
    private void HienLRecyclerView(){
        arrayListFAQ = new ArrayList<>();
        Read_FAQ(Server.url_read_FAQ);
        recyclerViewFAQ.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewFAQ.setLayoutManager(layoutManager);
        faqAdapter = new FAQAdapter(getApplicationContext(),this,arrayListFAQ);
        recyclerViewFAQ.setAdapter(faqAdapter);
    }
    private void Read_FAQ(String url){
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
                                JSONObject faq = jsonArray.getJSONObject(i);
                                String id = faq.getString("Id");
                                String tenCauhoi = faq.getString("tenCauhoi");
                                String cauTraLoi = faq.getString("cauTraLoi");
                                arrayListFAQ.add(new FAQ(id,tenCauhoi,cauTraLoi));
                            }
                            faqAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Toast.makeText(FaQActivity.this,"Lỗi tải dữ liệu",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(FaQActivity.this,"Lỗi tải dữ liệu",Toast.LENGTH_SHORT).show();
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
               faqAdapter.getFilter().filter(s);
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