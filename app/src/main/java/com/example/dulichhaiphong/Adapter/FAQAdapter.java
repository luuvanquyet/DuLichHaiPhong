package com.example.dulichhaiphong.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dulichhaiphong.Activity.FaQActivity;
import com.example.dulichhaiphong.Fragment.Fragment_Cautraloi;
import com.example.dulichhaiphong.Fragment.Fragment_search_tamlinh;
import com.example.dulichhaiphong.Model.FAQ;
import com.example.dulichhaiphong.R;


import java.util.ArrayList;
import java.util.List;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.MyViewHolder> implements Filterable {
    private Context mContext;
    private FaQActivity activity_faq;
    private List<FAQ> faqlist;
    private ArrayList<FAQ> faqFilter;
    private FAQAdapter.ValueFilter valueFilter;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tenCauhoi;
        public CardView itemFAQ;
        public MyViewHolder(View view) {
            super(view);
            tenCauhoi = (TextView) view.findViewById(R.id.tenCauhoi);
            itemFAQ = (CardView) view.findViewById(R.id.itemFAQ);
        }

    }

    public FAQAdapter(Context mContext, FaQActivity activity_faq, List<FAQ> faqlist) {
        this.mContext = mContext;
        this.activity_faq = activity_faq;
        this.faqlist = faqlist;
        faqFilter = (ArrayList<FAQ>) faqlist;
    }

    @Override
    public int getItemCount() {
        return faqlist.size();
    }
    @Override
    public FAQAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_faq, parent, false);
        return new FAQAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final FAQAdapter.MyViewHolder holder, final int position) {
        final FAQ faq = faqlist.get(position);
        holder.tenCauhoi.setText(faq.getTenCauHoi());
        holder.itemFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_Cautraloi fragment_cautraloi = new Fragment_Cautraloi();
                Bundle bundle = new Bundle();
                bundle.putString("cauTraloi",faq.getCauTraLoi());
                fragment_cautraloi.setArguments(bundle);
                fragment_cautraloi.show(activity_faq.getFragmentManager(),"Cautraloi");
            }
        });
    }
    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new FAQAdapter.ValueFilter();
        }
        return valueFilter;
    }
    public class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<FAQ> filterList = new ArrayList<FAQ>();
                for (int i = 0; i < faqFilter.size(); i++) {
                    if ((faqFilter.get(i).getTenCauHoi().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {
                        FAQ cauhoi = new FAQ(faqFilter.get(i)
                                .getId(), faqFilter.get(i)
                                .getTenCauHoi(),faqFilter.get(i).getCauTraLoi());
                        filterList.add(cauhoi);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = faqFilter.size();
                results.values = faqFilter;
            }
            return results;
        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            faqlist = (ArrayList<FAQ>) filterResults.values;
            notifyDataSetChanged();
        }
    }

}
