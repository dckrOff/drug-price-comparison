package com.a1tech.drugprice.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.a1tech.drugprice.Model.Drug;
import com.a1tech.drugprice.R;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DrugsAdapter extends RecyclerView.Adapter<DrugsAdapter.MyViewHolder> {

    private final LayoutInflater inflater;
    private final ArrayList<Drug> drugList;

    private final String pattern = "###,###,###.###";
    private final DecimalFormat decimalFormat = new DecimalFormat(pattern);

    public DrugsAdapter(Context context, ArrayList<Drug> drugList) {
        this.drugList = drugList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_category, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Drug drug = this.drugList.get(position);

        // formatter of number(1234567890)-- > (1 234 567 890)
        String formatPrice = decimalFormat.format(Double.valueOf(Integer.parseInt(drug.getDrugPrice())));

        holder.drugName.setText(drug.getDrugName());
        holder.drugPrice.setText("от " + formatPrice + " сум");
        Glide.with(inflater.getContext()).load(drug.getDrugImg()).into(holder.drugImage);
    }


    @Override
    public int getItemCount() {
        return drugList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView drugName, drugPrice;
        ImageView drugImage;

        public MyViewHolder(View itemView) {
            super(itemView);

            drugName = itemView.findViewById(R.id.tv_drug_name);
            drugPrice = itemView.findViewById(R.id.tv_drug_price);
            drugImage = itemView.findViewById(R.id.iv_drug_image);
        }
    }
}
