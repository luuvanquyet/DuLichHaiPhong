package com.example.dulichhaiphong.Fragment;

import android.app.ProgressDialog;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dulichhaiphong.Adapter.Baiviet_Adapter;
import com.example.dulichhaiphong.Adapter.Slider_Adapter;
import com.example.dulichhaiphong.Model.Anh_Slider_URL;
import com.example.dulichhaiphong.Model.Baiviet;
import com.example.dulichhaiphong.R;
import com.example.dulichhaiphong.ultil.CheckConNection;
import com.example.dulichhaiphong.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Dinh_fragmnet extends Fragment {
    private Toolbar toolbar;
    private ViewPager2 viewPager2;
    private Baiviet_Adapter baiviet_adapter;
    private Slider_Adapter slider_url_apdapter;
    private ArrayList<Baiviet> DanhsachBaiViet;
    private RecyclerView recyclerView;
    private View view;
    private ImageView imgDammayLeft;
    private ImageView imgDammayRight;
    private Handler sliderHander = new Handler();
    private ImageView imgHeader,back;
    private String tenView,tenCap,tenQuanHuyen,tenLoaiditich;
    private ArrayList<String> arrayTencap,arrayTenquanhuyen,arrayLoaiDitich;
    private ArrayList<Anh_Slider_URL> arraySlider;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chua_, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_frament);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        if(CheckConNection.haveNetwordConnection(view.getContext())){
            Anhxa();
            HienLRecyclerView();
            Hieuungmay();
            Read_anhSlider(Server.url_read_Slider,arraySlider,"Đình ");
            HienSilder();
        }else{
            Toast.makeText(getActivity(),"Mời bạn kiểm tra lại Internet!",Toast.LENGTH_SHORT).show();
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
                getActivity().finish();
            }
        });
        return view;
    }

    private void Read_anhSlider(String url_read_Slider, final ArrayList<Anh_Slider_URL> arrayAnh, final String tenTilte){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_read_Slider, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    if(response!=null){
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("read");
                        if(success.equals("1")){
                            for(int i=0 ; i< jsonArray.length();i++){
                                JSONObject objectAnh = jsonArray.getJSONObject(i);
                                String tenFile = objectAnh.getString("tenFile");
                                arrayAnh.add(new Anh_Slider_URL(tenFile));
                            }
                            slider_url_apdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"Lỗi load du lieu ảnh",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Lỗi load du lieu ảnh",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("tenDanhMuc",tenTilte);
                params.put("key",Server.key);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    private void Timkiem_baiviet(final String url_read_danhsach_baiviet_chua, final ArrayList<Baiviet> danhSachBaiviet, final String tenDanhmuc, final String tenCapcongnhan, final String tenLoaiDiTich, final String tenQuanhuyen){
        final ProgressDialog progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_read_danhsach_baiviet_chua, new Response.Listener<String>() {
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
                            baiviet_adapter.notifyDataSetChanged();
                            if(danhSachBaiviet.size() ==0){
                                Toast.makeText(getActivity(),"Không tìm có bài viết nào!",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(),"Có " + danhSachBaiviet.size()+" bài viết!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(),"Lỗi load dữ liệu danh sách bài viết",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Lỗi load dữ liệu danh sách bài viết",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("tenDanhMuc",tenDanhmuc);
                params.put("tenCapCongNhan",tenCapcongnhan);
                params.put("tenLoaiDiTich",tenLoaiDiTich);
                params.put("tenQuanHuyen",tenQuanhuyen);
                params.put("key",Server.key);
                return  params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


    private void Anhxa(){
        viewPager2 = (ViewPager2) view.findViewById(R.id.viewPageSlider);
        recyclerView = view.findViewById(R.id.recyclerview_Baiview);
        imgDammayLeft = view.findViewById(R.id.imgDamMayLeft);
        imgDammayRight = view.findViewById(R.id.imgDamMayRight);
        imgHeader = view.findViewById(R.id.imgHeader);
        imgHeader.setImageResource(R.drawable.backgroud_dinh);
        back = (ImageView) view.findViewById(R.id.back);
    }
    private void Hieuungmay(){
        Animation mayBayTrai = AnimationUtils.loadAnimation(view.getContext(),R.anim.translate_left);
        imgDammayLeft.startAnimation(mayBayTrai);
        Animation mayBayPhai = AnimationUtils.loadAnimation(view.getContext(),R.anim.translate_right);
        imgDammayRight.startAnimation(mayBayPhai);
    }
    private void HienLRecyclerView(){
        DanhsachBaiViet = new ArrayList<>();
        Timkiem_baiviet(Server.url_read_danhsach_baiviet,DanhsachBaiViet,"Đình",tenCap,tenLoaiditich,tenQuanHuyen);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        baiviet_adapter = new Baiviet_Adapter(view.getContext(),DanhsachBaiViet);
        recyclerView.setAdapter(baiviet_adapter);
        baiviet_adapter.notifyDataSetChanged();
    }
    private void HienSilder(){
        slider_url_apdapter = new Slider_Adapter(arraySlider,viewPager2);
        viewPager2.setAdapter(slider_url_apdapter);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f+r*0.15f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.setCurrentItem(1);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHander.removeCallbacks(sliderRunable);
                sliderHander.postDelayed(sliderRunable,3500);
            }
        });

    }
    private Runnable sliderRunable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        sliderHander.removeCallbacks(sliderRunable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHander.postDelayed(sliderRunable,3500);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        arrayTencap = new ArrayList<>();
        arrayTenquanhuyen = new ArrayList<>();
        arrayLoaiDitich = new ArrayList<>();
        tenQuanHuyen ="";
        tenLoaiditich ="";
        tenCap ="";
        arraySlider = new ArrayList<>();
        Bundle bundle = getArguments();
        if(bundle!= null){
            arrayTencap = bundle.getStringArrayList("DsCapcongnhan");
            arrayTenquanhuyen = bundle.getStringArrayList("DsQuanhuyen");
            arrayLoaiDitich = bundle.getStringArrayList("DsLoaiDiTich");
            tenCap = bundle.getString("TenCap");
            tenQuanHuyen = bundle.getString("TenQuanHuyen");
            tenLoaiditich = bundle.getString("TenLoaiDitich");
            tenView = bundle.getString("TenView");
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search,menu);
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
                baiviet_adapter.getFilter().filter(s);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case R.id.search_view:
                return true;
            case R.id.filter_view:
                Fragment_search_tamlinh fragment_search_tamlinh = new Fragment_search_tamlinh();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("DsCapcongnhan",arrayTencap);
                bundle.putStringArrayList("DsQuanhuyen",arrayTenquanhuyen);
                bundle.putStringArrayList("DsLoaiDiTich",arrayLoaiDitich);
                bundle.putString("tenView",tenView);
                fragment_search_tamlinh.setArguments(bundle);
                fragment_search_tamlinh.show(getActivity().getFragmentManager(),"Bộ Lọc");
                return true;
        }
        return true;
    }
}
