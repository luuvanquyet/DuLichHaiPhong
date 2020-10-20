package com.example.dulichhaiphong.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dulichhaiphong.Adapter.Comment_Apdapter;
import com.example.dulichhaiphong.Model.Comment;
import com.example.dulichhaiphong.R;
import com.example.dulichhaiphong.ultil.CheckConNection;
import com.example.dulichhaiphong.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Fragment_Comment extends DialogFragment {
    View view;
    TextView txtThongbao;
    Comment_Apdapter comment_apdapter;
    ArrayList<Comment> arrayListCommet;
    RecyclerView recyclerViewComment;
    ImageView imgBack;
    String idbaiviet;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_comment,container,false);
        Anhxa();
        if(CheckConNection.haveNetwordConnection(view.getContext())){
            Khoitao();
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }else {
            Toast.makeText(getActivity(),"Mời bạn kiểm tra lại INTERNET",Toast.LENGTH_SHORT).show();
        }
        return view;
    }
    private void Khoitao(){
        Bundle bundle = getArguments();
        if(bundle!= null){
            idbaiviet = bundle.getString("idbaiviet");
            arrayListCommet = new ArrayList<>();
            Read_Comment(Server.url_read_comment,arrayListCommet);
            recyclerViewComment.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
            recyclerViewComment.setLayoutManager(layoutManager);
            comment_apdapter = new Comment_Apdapter(getActivity(),arrayListCommet);
            recyclerViewComment.setAdapter(comment_apdapter);
        }
    }
    private void Read_Comment(String url,ArrayList<Comment> comments){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
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
                            for(int i = 0 ; i<jsonArray.length();i++){
                                JSONObject comment = jsonArray.getJSONObject(i);
                                String id = comment.getString("Id");
                                String tenNguoidung = comment.getString("tenNguoidung");
                                String fileAnh = comment.getString("fileAnh");
                                String ngayDang = comment.getString("ngayDang");
                                String noiDung = comment.getString("noiDung");
                                arrayListCommet.add(new Comment(id,tenNguoidung,fileAnh,ngayDang,noiDung));
                            }
                            if(arrayListCommet.size() == 0){
                                txtThongbao.setText("Chưa có bình luận nào.");
                            }else {
                                txtThongbao.setText("Có "+arrayListCommet.size()+" bình luận.");
                                comment_apdapter.notifyDataSetChanged();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(),"Lỗi load comment",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Lỗi load comment",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("key", Server.key);
                params.put("idbaiviet",idbaiviet);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    private void Anhxa(){
        txtThongbao = (TextView) view.findViewById(R.id.txtThongbao);
        recyclerViewComment = (RecyclerView) view.findViewById(R.id.recyclerview_Comment);
        imgBack = (ImageView) view.findViewById(R.id.imgClose);
    }
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }
}
