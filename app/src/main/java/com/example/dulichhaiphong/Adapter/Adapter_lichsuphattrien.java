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
import com.example.dulichhaiphong.Activity.FaQActivity;
import com.example.dulichhaiphong.Activity.LichsuphattrienActivity;
import com.example.dulichhaiphong.Fragment.Fragment_Cautraloi;
import com.example.dulichhaiphong.Fragment.Fragment_chitiet_baiviet_lichsu;
import com.example.dulichhaiphong.Fragment.Fragment_search_ditichlichsu;
import com.example.dulichhaiphong.Model.Baiviet;
import com.example.dulichhaiphong.R;
import com.example.dulichhaiphong.ultil.Server;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_lichsuphattrien extends RecyclerView.Adapter<Adapter_lichsuphattrien.MyViewHolder> implements Filterable {
    private Context mContext;
    private List<Baiviet> baivietList;
    private ArrayList<Baiviet> baivietFilter;
    private Adapter_lichsuphattrien.ValueFilter valueFilter;
    private LichsuphattrienActivity lichsuphattrienActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tenBaiviet, tomTat, soLike, ngayDang;
        public CircleImageView anhdaidien;
        public CardView ItemBaiViet;

        public MyViewHolder(View view) {
            super(view);
            tenBaiviet = (TextView) view.findViewById(R.id.tenBaiviet);
            anhdaidien = (CircleImageView) view.findViewById(R.id.imgAnhBaiviet);
            ItemBaiViet = (CardView) view.findViewById(R.id.itembaiviet);
        }

    }

    public Adapter_lichsuphattrien(Context mContext, List<Baiviet> baivietList, LichsuphattrienActivity lichsuphattrienActivity) {
        this.mContext = mContext;
        this.baivietList = baivietList;
        this.lichsuphattrienActivity = lichsuphattrienActivity;
        baivietFilter = (ArrayList<Baiviet>) baivietList;
    }

    @Override
    public int getItemCount() {
        return baivietList.size();
    }

    @Override
    public Adapter_lichsuphattrien.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lichsu_phatrien, parent, false);
        return new Adapter_lichsuphattrien.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Adapter_lichsuphattrien.MyViewHolder holder, final int position) {
        final Baiviet baiviet = baivietList.get(position);
        holder.tenBaiviet.setText(baiviet.getTenBaiViet());
        ;
        if (!baiviet.getAnhDaidien().equals("")) {
            Picasso.get().load(Server.url_anh + baiviet.getAnhDaidien()).networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE).into(holder.anhdaidien);
        }
        holder.ItemBaiViet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_chitiet_baiviet_lichsu fragment_chitiet_baiviet_lichsu = new Fragment_chitiet_baiviet_lichsu();
                Bundle bundle = new Bundle();
                bundle.putString("Noidung",baiviet.getNoiDung());
                fragment_chitiet_baiviet_lichsu.setArguments(bundle);
                fragment_chitiet_baiviet_lichsu.show(lichsuphattrienActivity.getFragmentManager(),"Noi dung");
            }
        });
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new Adapter_lichsuphattrien.ValueFilter();
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
                                .getTenBaiViet(), baivietFilter.get(i).getTomTat(), baivietFilter.get(i).getSoLike(), baivietFilter.get(i)
                                .getNgayDang(), baivietFilter.get(i).getAnhDaidien(), baivietFilter.get(i).getNoiDung(),baivietFilter.get(i).getCode());
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
