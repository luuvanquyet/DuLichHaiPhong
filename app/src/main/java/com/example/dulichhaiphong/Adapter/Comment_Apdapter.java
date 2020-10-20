package com.example.dulichhaiphong.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dulichhaiphong.Model.Comment;
import com.example.dulichhaiphong.R;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Comment_Apdapter extends RecyclerView.Adapter<Comment_Apdapter.ViewHolder> {
    private Context context;
    private ArrayList<Comment> arrayListComment;

    public Comment_Apdapter(Context context, ArrayList<Comment> arrayListComment) {
        this.context = context;
        this.arrayListComment = arrayListComment;
    }


    @NonNull
    @Override
    public Comment_Apdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_binhluan,parent,false);
        return new Comment_Apdapter.ViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull Comment_Apdapter.ViewHolder holder, final int position) {
        Picasso.get().load(arrayListComment.get(position).getFilePhoto()).networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE).into(holder.circleImageView);
        holder.txtThoigian.setText(arrayListComment.get(position).getThoiGiandang());
        holder.txtBinhluan.setText(arrayListComment.get(position).getNoiDung());
        holder.txtTenNguoiDung.setText(arrayListComment.get(position).getTenNguoidung());
    }

    @Override
    public int getItemCount() {
        return arrayListComment.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView txtThoigian,txtBinhluan,txtTenNguoiDung;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.profileImage);
            txtThoigian = (TextView) itemView.findViewById(R.id.txtThoigiandang);
            txtBinhluan = (TextView) itemView.findViewById(R.id.txtBinhluan);
            txtTenNguoiDung = (TextView) itemView.findViewById(R.id.txtTenNguoiDung);
        }
    }
}
