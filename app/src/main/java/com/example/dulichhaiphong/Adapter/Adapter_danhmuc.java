package com.example.dulichhaiphong.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dulichhaiphong.Model.DanhMuc;
import com.example.dulichhaiphong.R;

import java.util.List;

public class Adapter_danhmuc extends ArrayAdapter<DanhMuc> {
    LayoutInflater flater;

    public Adapter_danhmuc(Activity context, int resouceId, List<DanhMuc> list){
        super(context,resouceId, list);
        flater = context.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DanhMuc danhmuc = getItem(position);
        View rowview = flater.inflate(R.layout.item_danhmuc,null,true);
        TextView txtTitle = (TextView) rowview.findViewById(R.id.title);
        txtTitle.setText(danhmuc.getTenDanhMuc());
        return rowview;
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DanhMuc danhmuc = getItem(position);
        View rowview = flater.inflate(R.layout.item_danhmuc,null,true);
        TextView txtTitle = (TextView) rowview.findViewById(R.id.title);
        txtTitle.setText(danhmuc.getTenDanhMuc());
        return rowview;
    }
}

