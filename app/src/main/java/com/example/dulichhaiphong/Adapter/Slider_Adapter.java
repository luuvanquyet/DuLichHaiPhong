package com.example.dulichhaiphong.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.dulichhaiphong.Model.AnhSlider;
import com.example.dulichhaiphong.Model.Anh_Slider_URL;
import com.example.dulichhaiphong.R;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Slider_Adapter extends RecyclerView.Adapter<Slider_Adapter.SliderViewHolder> {
    private List<Anh_Slider_URL> sliderItemList;
    private ViewPager2 viewPager2;
    private String url_anh ="http://dulichhaiphong.xyz/images/sliders/";

    public Slider_Adapter(List<Anh_Slider_URL> sliderItemList, ViewPager2 viewPager2) {
        this.sliderItemList = sliderItemList;
        this.viewPager2 = viewPager2;
    }


    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item_baiviet,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, final int position) {
        holder.SetItemView(sliderItemList.get(position));
        if(position==sliderItemList.size()-2){
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return sliderItemList.size();
    }
    class SliderViewHolder extends RecyclerView.ViewHolder{
        private ImageView imge;
        private CardView cardparent;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imge = (ImageView) itemView.findViewById(R.id.imgSlider);
            cardparent = (CardView) itemView.findViewById(R.id.parentSlider);
        }
        void SetItemView(Anh_Slider_URL sliderItem){
            Picasso.get().load(url_anh+sliderItem.getFileAnh()).networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE).into(imge);
        }
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sliderItemList.addAll(sliderItemList);
            notifyDataSetChanged();
        }
    };

}
