package com.example.dulichhaiphong.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.example.dulichhaiphong.Activity.CapnhatPassActivity;
import com.example.dulichhaiphong.Activity.ComfirmEmailActivity;
import com.example.dulichhaiphong.R;
import com.example.dulichhaiphong.ultil.CheckConNection;
import com.example.dulichhaiphong.ultil.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Fragment_comfirm extends DialogFragment {
    private Button btnXacnhan;
    private EditText editAccount;
    private ImageView btnBack;
    private ProgressBar loading;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_quenpass,container,false);
        Anhxa();
        if(CheckConNection.haveNetwordConnection(getActivity())){
            btnXacnhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String maccount = editAccount.getText().toString().trim();
                    if(maccount.isEmpty()){
                        editAccount.setError("Mời bạn nhập tên tài khoản!");
                    }else{
                        checkAccount(maccount);
                    }
                }
            });
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment_login fragment_login = new Fragment_login();
                    fragment_login.show(getActivity().getSupportFragmentManager(),"login");
                    dismiss();
                }
            });
        }else{
            CheckConNection.ShowToast_Short(getActivity(),"Mời bạn kiểm tra lại Internet!");
        }
        return view;
    }
    private void checkAccount(final String maccount){
        loading.setVisibility(View.VISIBLE);
        btnXacnhan.setVisibility(View.GONE);;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.url_check_email, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    if(success.equals("1")){
                        String id = jsonObject.getString("id");
                        btnXacnhan.setVisibility(View.GONE);
                        Fragment_doipass fragment_doipass = new Fragment_doipass();
                        Bundle bundle = new Bundle();
                        bundle.putString("idtaikhoan",id);
                        fragment_doipass.setArguments(bundle);
                        fragment_doipass.show(getActivity().getSupportFragmentManager(),"doipass");
                        dismiss();
                    }else{
                        if(message.equals("Khongtontai")){
                            editAccount.setError("Tài khoản không tồn tại!");
                            btnXacnhan.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"Lỗi",Toast.LENGTH_SHORT).show();
                    btnXacnhan.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Lỗi",Toast.LENGTH_SHORT).show();
                btnXacnhan.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("account",maccount);
                params.put("key", Server.key);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
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
        btnXacnhan = (Button) view.findViewById(R.id.btnXacNhan);
        editAccount = (EditText) view.findViewById(R.id.account);
        btnBack = (ImageView) view.findViewById(R.id.back);
        loading = (ProgressBar) view.findViewById(R.id.loading);
    }
}
