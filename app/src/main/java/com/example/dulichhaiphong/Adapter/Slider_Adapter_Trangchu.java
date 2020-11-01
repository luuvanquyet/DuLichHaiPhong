package com.example.dulichhaiphong.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.dulichhaiphong.Activity.DiaDiemTamLinh;
import com.example.dulichhaiphong.Activity.DitichlichsuActivity;
import com.example.dulichhaiphong.Activity.Vinh_Dao_HangdongActivity;
import com.example.dulichhaiphong.Model.Slider_Item_Trangchu;
import com.example.dulichhaiphong.R;

import java.util.List;

public class Slider_Adapter_Trangchu extends RecyclerView.Adapter<Slider_Adapter_Trangchu .SliderViewHolder> {
    private List<Slider_Item_Trangchu> sliderItemList;
    private ViewPager2 viewPager2;

    public Slider_Adapter_Trangchu(List<Slider_Item_Trangchu> sliderItemList, ViewPager2 viewPager2) {
        this.sliderItemList = sliderItemList;
        this.viewPager2 = viewPager2;
    }

    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item_trangchu,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, final int position) {
        holder.SetItemView(sliderItemList.get(position));
        holder.cardparent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position){
                    case 0:
                        Intent intentTamlinh = new Intent(view.getContext(), DiaDiemTamLinh.class);
                        view.getContext().startActivity(intentTamlinh);
                        break;
                    case 1:
                        Intent intentDitich = new Intent(view.getContext(), DitichlichsuActivity.class);
                        view.getContext().startActivity(intentDitich);
                        break;
                    case 2:
                         Intent intentVinh = new Intent(view.getContext(), Vinh_Dao_HangdongActivity.class);
                        view.getContext().startActivity(intentVinh);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return sliderItemList.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder{
        private ImageView imge;
        private TextView txtImage;
        private CardView cardparent;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imge = (ImageView) itemView.findViewById(R.id.imgSlider);
            txtImage = (TextView) itemView.findViewById(R.id.txtSlider);
            cardparent = (CardView) itemView.findViewById(R.id.parentSlider);
        }
        void SetItemView(Slider_Item_Trangchu sliderItem){
            imge.setImageResource(sliderItem.getImge());
            txtImage.setText(sliderItem.getTenImge());
        }
    }

}
