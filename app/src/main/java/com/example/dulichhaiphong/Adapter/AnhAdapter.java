package com.example.dulichhaiphong.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dulichhaiphong.Activity.ChitietBaiviet;
import com.example.dulichhaiphong.Fragment.Anh_Bai_Viet_Fragment;
import com.example.dulichhaiphong.Model.AnhLienQuan;
import com.example.dulichhaiphong.R;
import com.example.dulichhaiphong.ultil.Server;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AnhAdapter extends RecyclerView.Adapter<AnhAdapter.ViewHolder>  {
    private Context context;
    ArrayList<AnhLienQuan> anhLienQuanArrayList;
    private ChitietBaiviet activity_baiviet;

    public AnhAdapter(Context context, ArrayList<AnhLienQuan> anhLienQuanArrayList, ChitietBaiviet activity_baiviet) {
        this.context = context;
        this.anhLienQuanArrayList = anhLienQuanArrayList;
        this.activity_baiviet = activity_baiviet;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_anh_baiviet,parent,false);
        return new ViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Picasso.get().load(Server.url_anh_lien_quan + anhLienQuanArrayList.get(position).getFile()).networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE).into(holder.imageView);
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", anhLienQuanArrayList);
                bundle.putInt("position", position);
                FragmentTransaction ft = activity_baiviet.getSupportFragmentManager().beginTransaction();
                final Anh_Bai_Viet_Fragment newFragment = Anh_Bai_Viet_Fragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }
        });

    }

    @Override
    public int getItemCount() {
        return anhLienQuanArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        RelativeLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imgBaiviet);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

}
