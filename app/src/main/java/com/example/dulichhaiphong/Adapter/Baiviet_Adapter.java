package com.example.dulichhaiphong.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dulichhaiphong.Activity.ChitietBaiviet;
import com.example.dulichhaiphong.Activity.DiaDiemTamLinh;
import com.example.dulichhaiphong.Model.Baiviet;
import com.example.dulichhaiphong.R;
import com.example.dulichhaiphong.ultil.Server;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Baiviet_Adapter extends RecyclerView.Adapter<Baiviet_Adapter.MyViewHolder> implements Filterable {
    private Context mContext;
    private List<Baiviet> baivietList;
    private ArrayList<Baiviet> baivietFilter;
    private ValueFilter valueFilter;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tenBaiviet,tomTat,soLike,ngayDang;
        public ImageView anhdaidien;
        public View overflow;
        public CardView ItemBaiViet;
        public MyViewHolder(View view) {
            super(view);
            tenBaiviet = (TextView) view.findViewById(R.id.txtTenBaiviet);
            tomTat = (TextView) view.findViewById(R.id.txtTomTat);
            soLike = (TextView) view.findViewById(R.id.txtSolike);
            ngayDang = (TextView) view.findViewById(R.id.txtNgaydang);
            anhdaidien = (ImageView) view.findViewById(R.id.grid_imageView);
            ItemBaiViet = (CardView) view.findViewById(R.id.itembaiviet);
        }

    }
    public Baiviet_Adapter(Context mContext, List<Baiviet> baivietList) {
        this.mContext = mContext;
        this.baivietList = baivietList;
        baivietFilter = (ArrayList<Baiviet>) baivietList;
    }
    @Override
    public int getItemCount() {
        return baivietList.size();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_baiviet_recyclerview, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {
        final Baiviet baiviet = baivietList.get(position);
        holder.tenBaiviet.setText(baiviet.getTenBaiViet());
        holder.tomTat.setText(baiviet.getTomTat());
        holder.soLike.setText(baiviet.getSoLike());
        holder.ngayDang.setText(baiviet.getNgayDang());
        if(!baiviet.getAnhDaidien().equals("")){
            Picasso.get().load(Server.url_anh+baiviet.getAnhDaidien()).networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE).into(holder.anhdaidien);
        }
        holder.ItemBaiViet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ChitietBaiviet.class);
                intent.putExtra("baiviet",baiviet);
                view.getContext().startActivity(intent);;
            }
        });
    }
    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new Baiviet_Adapter.ValueFilter();
        }
        return valueFilter;
    }
    public class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<Baiviet> filterList = new ArrayList<Baiviet>();
                for (int i = 0; i < baivietFilter.size(); i++) {
                    if ((baivietFilter.get(i).getTenBaiViet().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {
                        Baiviet diadiem = new Baiviet(baivietFilter.get(i)
                                .getIdBaiViet(), baivietFilter.get(i)
                                .getTenBaiViet(),baivietFilter.get(i).getTomTat(),baivietFilter.get(i).getSoLike(), baivietFilter.get(i)
                                .getNgayDang(),baivietFilter.get(i).getAnhDaidien(),baivietFilter.get(i).getNoiDung());
                        filterList.add(diadiem);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = baivietFilter.size();
                results.values = baivietFilter;
            }
            return results;
        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            baivietList = (ArrayList<Baiviet>) filterResults.values;
            notifyDataSetChanged();
        }
    }

}

