package com.example.dulichhaiphong.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dulichhaiphong.R;
import com.example.dulichhaiphong.ultil.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.cert.CertificateRevokedException;
import java.util.HashMap;
import java.util.Map;

public class Fragment_Danhgia extends DialogFragment {
    private View view;
    private EditText editDanhgia;
    private Button btnSend;
    private ImageView imgClose;
    private String txtDanhgia,idbaiviet,iduser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_danhgia,container,false);
        Anhxa();
        Khoitao();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtDanhgia = editDanhgia.getText().toString().trim();
                if(txtDanhgia.equals("")){
                    editDanhgia.setError("Mời bạn nhập bình luận!");
                }else{
                    Send_Binhluan(Server.url_send_binhluan,txtDanhgia,iduser,idbaiviet);
                }
            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;
    }
    private void Send_Binhluan(String url,final String txtDanhgia,final String iduser,final String idbaiviet){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.dismiss();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if(response!=null){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        if(success.equals("1")){
                            Toast.makeText(getActivity(),"Gửi bình luận thành công!", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }else {
                            Toast.makeText(getActivity(),"Gửi bình luận thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(),"Gửi bình luận thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Gửi bình luận thất bại!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("key",Server.key);
                params.put("idUser",iduser);
                params.put("idBaiviet",idbaiviet);
                params.put("binhluan",txtDanhgia);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    private void Khoitao(){
        Bundle bundle = getArguments();
        if(bundle!=null){
            idbaiviet = bundle.getString("idbaiviet");
            iduser = bundle.getString("iduser");
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }
    private void Anhxa(){
        editDanhgia = (EditText) view.findViewById(R.id.editBinhluan);
        btnSend = (Button) view.findViewById(R.id.btnSend);
        imgClose = (ImageView) view.findViewById(R.id.imgClose);
    }
}
