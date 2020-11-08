package com.example.dulichhaiphong.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dulichhaiphong.Chiduong.FetchURL;
import com.example.dulichhaiphong.Chiduong.TaskLoadedCallback;
import com.example.dulichhaiphong.Fragment.Fragment_Chitietdiadanh;
import com.example.dulichhaiphong.Fragment.Fragment_timkiem_diadanh;
import com.example.dulichhaiphong.Model.DiaDanh;
import com.example.dulichhaiphong.Model.Timkiemdiadanh;
import com.example.dulichhaiphong.R;

import com.example.dulichhaiphong.ultil.CheckConNection;
import com.example.dulichhaiphong.ultil.Server;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BandoActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback, Timkiemdiadanh {
    private GoogleMap map;
    private ArrayList<DiaDanh> arrayDiadanh;
    private Toolbar toolbar;
    private MapFragment mapFragment;
    private Button btnChiduong, btnXemchitiet;
    private LatLng vitriHientai;
    private Polyline currentPolyline;
    private FusedLocationProviderClient client;
    private MarkerOptions markerHientai;
    private MarkerOptions markerDiemden;
    private String tenDiadanh;
    private ArrayList<String> arrayCapcongnhan, arrayLoaiditich, arrayQuanHuyen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bando);
        Anhxa();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        if(CheckConNection.haveNetwordConnection(getApplicationContext())){
            mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mymap);
            mapFragment.getMapAsync(this);
            arrayDiadanh = new ArrayList<>();
            client = LocationServices.getFusedLocationProviderClient(this);
            if (ActivityCompat.checkSelfPermission(BandoActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                ActivityCompat.requestPermissions(BandoActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }
            btnChiduong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new FetchURL(BandoActivity.this).execute(getUrl(markerHientai.getPosition(), markerDiemden.getPosition(), "driving"), "driving");
                }
            });
            btnXemchitiet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment_Chitietdiadanh fragment_chitietdiadanh = new Fragment_Chitietdiadanh();
                    Bundle bundle = new Bundle();
                    bundle.putString("tenDiadanh", tenDiadanh);
                    fragment_chitietdiadanh.setArguments(bundle);
                    fragment_chitietdiadanh.show(getFragmentManager(), "Chitietdiadanh");
                }
            });
        }else{
            CheckConNection.ShowToast_Short(getApplicationContext(),"Mời bạn kiểm tra lại Internet!");
            finish();
        }

    }

    private void Load_fiter() {
        arrayCapcongnhan = new ArrayList<>();
        arrayCapcongnhan.add("Chọn tất cả");
        ReadFilter(Server.url_read_capcongnhan, arrayCapcongnhan, "Tencap");
        arrayQuanHuyen = new ArrayList<>();
        arrayQuanHuyen.add("Chọn tất cả");
        ReadFilter(Server.url_read_quanhuyen, arrayQuanHuyen, "TenQuan");
        arrayLoaiditich = new ArrayList<>();
        arrayLoaiditich.add("Chọn tất cả");
        ReadFilter(Server.url_read_loaiditich, arrayLoaiditich, "TenLoaiDiTich");
        Fragment_timkiem_diadanh fragment_timkiem_diadanh = new Fragment_timkiem_diadanh();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("DsCapcongnhan", arrayCapcongnhan);
        bundle.putStringArrayList("DsQuanhuyen", arrayQuanHuyen);
        bundle.putStringArrayList("DsLoaiDiTich", arrayLoaiditich);
        fragment_timkiem_diadanh.setArguments(bundle);
        fragment_timkiem_diadanh.show(getFragmentManager(), "Tim kiem dia danh");
    }

    private void ReadFilter(String url_read_data_filter, final ArrayList<String> arrayList, final String mname) {
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
                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonten = jsonArray.getJSONObject(i);
                            String ten = jsonten.getString(mname);
                            arrayList.add(ten);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(BandoActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BandoActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("key", Server.key);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getCurrentLocation() {
        @SuppressLint("MissingPermission") Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                if(location != null){
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            vitriHientai = new LatLng(location.getLatitude(),location.getLongitude());
                            markerHientai = new MarkerOptions().position(vitriHientai).title("Vị trí hiện tại!").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_my_position));
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(vitriHientai,10));
                            googleMap.addMarker(markerHientai);
                        }
                    });
                }
            }
        });
    }
    private void Anhxa(){
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        btnChiduong = (Button) findViewById(R.id.btnChiduong);
        btnXemchitiet = (Button) findViewById(R.id.btnXemchitiet);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(!marker.getTitle().equals("Vị trí hiện tại!")){
                    btnChiduong.setVisibility(View.VISIBLE);
                    btnXemchitiet.setVisibility(View.VISIBLE);
                    markerDiemden = new MarkerOptions().position(marker.getPosition()).title(marker.getTitle());
                    tenDiadanh = marker.getTitle();
                }else{
                    btnChiduong.setVisibility(View.GONE);
                    btnXemchitiet.setVisibility(View.GONE);
                }
                return false;
            }
        });
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                btnChiduong.setVisibility(View.GONE);
                btnXemchitiet.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu_search_bando, menu);
        MenuItem search_item = menu.findItem(R.id.search_view);
        SearchView searchView = (SearchView) search_item.getActionView();
        searchView.setFocusable(false);
        searchView.setBackgroundResource(R.drawable.bg_white_rounded);
        searchView.setQueryHint("Tìm kiếm địa danh");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                s = s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase();
                arrayDiadanh.clear();
                Timkiem_diadanh_theoten(Server.url_search_diadanh_theoten,arrayDiadanh,s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                btnChiduong.setVisibility(View.GONE);
                btnXemchitiet.setVisibility(View.GONE);
                return false;
            }
        });
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.search_view:
                return true;
            case R.id.filter_view:
                Load_fiter();
                btnChiduong.setVisibility(View.GONE);
                btnXemchitiet.setVisibility(View.GONE);
                return true;
        }
        return true;
    }
    private void Timkiem_diadanh_theoten(String url,final ArrayList<DiaDanh> danhsachDiadanh,final String tenDiadanh){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
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
                                JSONObject jsonBaiviet = jsonArray.getJSONObject(i);
                                String id = jsonBaiviet.getString("Id");
                                String tenDiaDanh = jsonBaiviet.getString("TenDiaDanh");
                                String tomtat = jsonBaiviet.getString("TomTat");
                                String noidung = jsonBaiviet.getString("MoTaChiTiet");
                                String toado = jsonBaiviet.getString("ToaDo");
                                String[] parts = toado.split(",");
                                Double toadoX = Double.parseDouble(parts[0].trim());
                                Double toadoY = Double.parseDouble(parts[1].trim());
                                Toast.makeText(BandoActivity.this,toado,Toast.LENGTH_SHORT).show();
                                LatLng latLng = new LatLng(toadoX,toadoY);
                                String anhDaidien = jsonBaiviet.getString("AnhDaiDien");
                                danhsachDiadanh.add(new DiaDanh(id,tenDiaDanh,tomtat,noidung,latLng,anhDaidien));
                            }
                            if(danhsachDiadanh.size()>0){
                                map.clear();
                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(vitriHientai,10));
                                map.addMarker(markerHientai);
                                for(int i = 0 ; i< danhsachDiadanh.size();i++){
                                    map.addMarker(new MarkerOptions().position(danhsachDiadanh.get(i).getToaDo()).title(danhsachDiadanh.get(i).getTenDiadanh()));
                                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(danhsachDiadanh.get(i).getToaDo(),12), 2000, null);
                                }
                                Toast.makeText(BandoActivity.this,"Tìm thấy " + danhsachDiadanh.size()+" địa danh!",Toast.LENGTH_SHORT).show();
                            }else{
                                map.clear();
                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(vitriHientai,10));
                                map.addMarker(markerHientai);
                                Toast.makeText(BandoActivity.this,"Không tìm thấy " + danhsachDiadanh.size()+" địa danh nào!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(BandoActivity.this,"Lỗi load dữ liệu danh sách địa danh",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BandoActivity.this,"Lỗi load dữ liệu danh sách địa danh",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("tenDiaDanh",tenDiadanh);
                params.put("key",Server.key);
                return  params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void Timkiem_diadanh(final String url_read_danhsach_baiviet, final ArrayList<DiaDanh> danhsachDiadanh, final String idDanhmuc, final String capCongNhan, final String loaiDiTich, final String quanHuyen, final String phamVi){
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
                        JSONArray jsonArray = jsonObject.getJSONArray("read");
                        if(success.equals("1")){
                            for(int i = 0; i<jsonArray.length();i++){
                                JSONObject jsonBaiviet = jsonArray.getJSONObject(i);
                                String id = jsonBaiviet.getString("Id");
                                String tenDiaDanh = jsonBaiviet.getString("TenDiaDanh");
                                String tomtat = jsonBaiviet.getString("TomTat");
                                String noidung = jsonBaiviet.getString("MoTaChiTiet");
                                String toado = jsonBaiviet.getString("ToaDo");
                                String[] parts = toado.split(",");
                                Double toadoX = Double.parseDouble(parts[0].trim());
                                Double toadoY = Double.parseDouble(parts[1].trim());
                                Toast.makeText(BandoActivity.this,toado,Toast.LENGTH_SHORT).show();
                                LatLng latLng = new LatLng(toadoX,toadoY);
                                String anhDaidien = jsonBaiviet.getString("AnhDaiDien");
                                danhsachDiadanh.add(new DiaDanh(id,tenDiaDanh,tomtat,noidung,latLng,anhDaidien));
                            }
                            if(danhsachDiadanh.size() == 0){
                                Toast.makeText(BandoActivity.this,"Không tìm có địa danh nào!",Toast.LENGTH_SHORT).show();

                            }else{
                                if(phamVi.equals("0")){
                                    map.clear();
                                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(vitriHientai,10));
                                    map.addMarker(markerHientai);
                                    Toast.makeText(BandoActivity.this,"Có " + danhsachDiadanh.size()+" địa danh!",Toast.LENGTH_SHORT).show();
                                    for (int i = 0; i < danhsachDiadanh.size(); i++) {
                                        map.addMarker(new MarkerOptions().position(danhsachDiadanh.get(i).getToaDo()).title(danhsachDiadanh.get(i).getTenDiadanh()));
                                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(danhsachDiadanh.get(i).getToaDo(), 12), 2000, null);
                                    }
                                }else {
                                    map.clear();
                                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(vitriHientai,10));
                                    map.addMarker(markerHientai);
                                    Integer dem = 0;
                                    float results[] = new float[10];
                                    for (int i = 0; i < danhsachDiadanh.size(); i++) {
                                        Location.distanceBetween(vitriHientai.latitude, vitriHientai.longitude, danhsachDiadanh.get(i).getToaDo().latitude, danhsachDiadanh.get(i).getToaDo().longitude, results);
                                        if (results[0] < 10000) {
                                            dem++;
                                            map.addMarker(new MarkerOptions().position(danhsachDiadanh.get(i).getToaDo()).title(danhsachDiadanh.get(i).getTenDiadanh()));
                                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(danhsachDiadanh.get(i).getToaDo(), 12), 2000, null);
                                        }
                                    }
                                    Toast.makeText(BandoActivity.this,"Có " + dem +" địa danh!",Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(BandoActivity.this,"Lỗi load dữ liệu danh sách địa danh",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BandoActivity.this,"Lỗi load dữ liệu danh sách địa danh",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("idDanhMuc",idDanhmuc);
                params.put("capCongNhan",capCongNhan);
                params.put("loaiDitich",loaiDiTich);
                params.put("quanHuyen",quanHuyen);
                params.put("key",Server.key);
                return  params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 44){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
    }
    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.map_api_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = map.addPolyline((PolylineOptions) values[0]);
    }

    @Override
    public void Timkiem(String idDiadanh, String tenCap, String tenLoaiDiTich, String tenQuan, String phamVi) {
        arrayDiadanh.clear();
        Timkiem_diadanh(Server.url_search_diadanh,arrayDiadanh,idDiadanh,tenCap,tenLoaiDiTich,tenQuan,phamVi);
    }
}
