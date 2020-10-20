package com.example.dulichhaiphong.Activity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dulichhaiphong.Fragment.Fragment_Doimatkhau;
import com.example.dulichhaiphong.Model.SessionManager;
import com.example.dulichhaiphong.Model.TruyenDataPass;
import com.example.dulichhaiphong.R;
import com.example.dulichhaiphong.ultil.Server;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements TruyenDataPass {
    private TextView txtName,txtEmail,txtSdt,txtAccount;
    private Button btnLogout,btnPhotoUpload,btnDoimatkhau;
    SessionManager sessionManager;
    Fragment_Doimatkhau fragment_doimatkhau;
    String getId;
    private Menu action;
    private static String url_read = "http://dulichhaiphong.xyz/api/read_detail.php";
    private static String url_edit = "http://dulichhaiphong.xyz/api/edit_detail.php";
    private static String url_upload = "http://dulichhaiphong.xyz/api/upload.php";
    private static String url_update_pass = "http://dulichhaiphong.xyz/api/update_pass.php";
    private Bitmap bitmap;
    CircleImageView Anhprofile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Anhxa();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String,String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logout();
            }
        });
        btnPhotoUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosesFile();
            }
        });
        btnDoimatkhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment_doimatkhau = new Fragment_Doimatkhau();
                fragment_doimatkhau.show(getFragmentManager(),"Đổi mật khẩu");
            }
        });
    }
    private void Anhxa(){
        txtAccount = (TextView) findViewById(R.id.account);
        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnPhotoUpload = (Button) findViewById(R.id.btnphoto);
        Anhprofile = (CircleImageView) findViewById(R.id.profileImage);
        txtSdt = (TextView) findViewById(R.id.sodienthoai);
        btnDoimatkhau = (Button) findViewById(R.id.btnDoimatkhau);
        txtAccount.setFocusableInTouchMode(false);
    }
    private void getUserDetail(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_read, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Donghopthoai(progressDialog);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("read");
                    if(success.equals("1")){
                        for(int i = 0; i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            String strName = object.getString("name").trim();
                            String strAccount = object.getString("account").trim();
                            String strEmail = object.getString("email").trim();
                            String strImage = object.getString("image").trim();
                            String strSodienthoai = object.getString("sodienthoai").trim();
                            txtName.setText(strName);
                            txtEmail.setText(strEmail);
                            txtAccount.setText(strAccount);
                           if(!strImage.equals("")){
                               Log.d("AAA",strImage);
                               Picasso.get().load(strImage).networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE).into(Anhprofile);
                           }
                           if(!strSodienthoai.equals("")){
                               txtSdt.setText(strSodienthoai);
                           }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Donghopthoai(progressDialog);
                    Toast.makeText(HomeActivity.this,"Lỗi" + e.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Donghopthoai(progressDialog);
                Toast.makeText(HomeActivity.this,"Lỗi",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id",getId);
                params.put("key",Server.key);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void Donghopthoai(ProgressDialog progressDialog){
        if(progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        getUserDetail();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_action,menu);
        action = menu;
        action.findItem(R.id.save).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit:
                txtAccount.setFocusableInTouchMode(false);
                txtName.setFocusableInTouchMode(true);
                txtEmail.setFocusableInTouchMode(true);
                txtSdt.setFocusableInTouchMode(true);
                InputMethodManager inm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inm.showSoftInput(txtName, InputMethodManager.SHOW_IMPLICIT);
                action.findItem(R.id.edit).setVisible(false);
                action.findItem(R.id.save).setVisible(true);
                return true;
            case R.id.save:
                final String account = this.txtAccount.getText().toString().trim();
                final String name = this.txtName.getText().toString().trim();
                final String email = this.txtEmail.getText().toString().trim();
                final String sodienthoai = this.txtSdt.getText().toString().trim();
                final String id = getId;
                if(name.isEmpty()||email.isEmpty()||sodienthoai!=null&&!Kiemtrasodienthoai(sodienthoai)){
                    if(name.isEmpty()){
                        txtName.setError("Họ và tên không được để trống!");
                    }
                    if(email.isEmpty()){
                        txtEmail.setError("Email không được để trống!");
                    }
                    if(sodienthoai!=null&&!Kiemtrasodienthoai(sodienthoai)){
                        txtSdt.setError("Bạn nhập không phải số!");}
                } else{
                    if(Kiemtraemail(email)){
                        SaveEditDetail(id,name,account,email,sodienthoai);
                    }else{
                        txtEmail.setError("Bạn nhập sai định dạng của email!");
                    }

                }
                action.findItem(R.id.edit).setVisible(true);
                action.findItem(R.id.save).setVisible(false);
                txtAccount.setFocusableInTouchMode(false);
                txtName.setFocusableInTouchMode(false);
                txtEmail.setFocusableInTouchMode(false);
                txtSdt.setFocusableInTouchMode(false);
                txtAccount.setFocusable(false);
                txtName.setFocusable(false);
                txtEmail.setFocusable(false);
                txtSdt.setFocusable(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    private boolean Kiemtraemail(String memail){
        if(Patterns.EMAIL_ADDRESS.matcher(memail).matches()){
            return true;
        }
        return false;
    }
    private void SaveEditDetail(final String mid,final String mname, final String maccount,final String memail,final String msdt){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_edit, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Donghopthoai(progressDialog);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if(success.equals("1")){
                        Toast.makeText(HomeActivity.this,"Thành công!",Toast.LENGTH_SHORT).show();
                        sessionManager.createSession(maccount,memail,mid);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(HomeActivity.this,"Lỗi!" +e.toString(),Toast.LENGTH_SHORT).show();
                    Donghopthoai(progressDialog);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this,"Lỗi!",Toast.LENGTH_SHORT).show();
                Donghopthoai(progressDialog);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("name",mname);
                params.put("account",maccount);
                params.put("email",memail);
                params.put("sodienthoai",msdt);
                params.put("id",mid);
                params.put("key", Server.key);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private boolean Kiemtrasodienthoai(String msodienthoai){
        if(Patterns.PHONE.matcher(msodienthoai).matches()){
            return true;
        }
        return false;
    }
    private void chosesFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            Uri filtePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filtePath);
                Anhprofile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            UploadPicture(getId,getStringImage(bitmap));
        }

    }
    private void UploadPicture(final String id, final String photo){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_upload, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if(success.equals("1")){
                        getUserDetail();
                        Toast.makeText(HomeActivity.this,"Thành công!",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Donghopthoai(progressDialog);
                    Toast.makeText(HomeActivity.this,"Lỗi ở đây!",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Donghopthoai(progressDialog);
                Toast.makeText(HomeActivity.this,"Lỗi!",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id",id);
                params.put("photo",photo);
                params.put("key",Server.key);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream  byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,15,byteArrayOutputStream);
        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodeImage = Base64.encodeToString(imageByteArray,Base64.DEFAULT);
        return encodeImage;
    }

    @Override
    public void Doimatkhau(final String matkhau) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_update_pass, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Donghopthoai(progressDialog);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if(success.equals("1")){
                        Toast.makeText(HomeActivity.this,"Đổi mật khẩu thành công!",Toast.LENGTH_SHORT).show();
                        fragment_doimatkhau.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Donghopthoai(progressDialog);
                    Toast.makeText(HomeActivity.this,"Đổi mật khẩu thất bại!",Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Donghopthoai(progressDialog);
                Toast.makeText(HomeActivity.this,"Đổi mật khẩu thất bại!",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id",getId);
                params.put("password",matkhau);
                params.put("key",Server.key);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void DongDialog() {
        fragment_doimatkhau.dismiss();
    }
}