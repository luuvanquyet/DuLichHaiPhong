package com.example.dulichhaiphong.Fragment;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dulichhaiphong.R;
import com.example.dulichhaiphong.ultil.CheckConNection;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_Chitietdiadanh extends DialogFragment {
    private View view;
    private TextView txtTendiadanh;
    private ImageView imgBack;
    private CircleImageView imgDaiDien;
    private WebView noidungchitiet;
    private String url_search_diadanh_theoten = "http://dulichhaiphong.xyz/api/search_diadanh_theoten.php";
    private String url_link_anh = "http://dulichhaiphong.xyz/images/anhdaidien/";
    public static String key ="$2y$10$0.koPVA8iTxiIYoD2765lOx6kUknRuM8SYRWzJsp3aRmHZSDoreiq";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chitiet_diadanh,container,false);
        Anhxa();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;
    }
    private void Anhxa(){
        txtTendiadanh = (TextView) view.findViewById(R.id.txtTendiadanh);
        imgBack = (ImageView) view.findViewById(R.id.imgClose);
        imgDaiDien = (CircleImageView) view.findViewById(R.id.imgAnhdaidien);
        noidungchitiet = (WebView) view.findViewById(R.id.txtNoidung);
    }
    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        Bundle bundle = getArguments();
        if(bundle!=null){
            String tenDiadanh = bundle.getString("tenDiadanh");
            if(CheckConNection.haveNetwordConnection(getActivity())){
                Read_Chitiet_Diadanh(url_search_diadanh_theoten,tenDiadanh);
            }else {
                CheckConNection.ShowToast_Short(getActivity(),"Mời bạn kiểm tra lại Internet!");
            }
        }

    }
    private void Read_Chitiet_Diadanh(String url,final String tenDiadanh){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
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
                                String tenDiaDanh = jsonBaiviet.getString("TenDiaDanh");
                                String noidung = jsonBaiviet.getString("MoTaChiTiet");
                                txtTendiadanh.setText(tenDiaDanh);
                                String linkAnh = jsonBaiviet.getString("AnhDaiDien");
                                Picasso.get().load(url_link_anh+linkAnh).networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE).into(imgDaiDien);
                                noidungchitiet.getSettings().setJavaScriptEnabled(true);
                                noidungchitiet.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                                noidungchitiet.clearView();
                                noidungchitiet.measure(300, 300);
                                noidungchitiet.getSettings().setDomStorageEnabled(true);
                                noidungchitiet.getSettings().setLoadWithOverviewMode(true);
                                noidungchitiet.loadDataWithBaseURL("http://dulichhaiphong.xyz","<style>img{display: inline;height: auto;max-width: 100%;}</style>"+ noidung,"text/html","utf-8",null);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(),"Lỗi load dữ liệu danh sách địa danh",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Lỗi load dữ liệu danh sách địa danh",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("tenDiaDanh",tenDiadanh);
                params.put("key",key);
                return  params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}
