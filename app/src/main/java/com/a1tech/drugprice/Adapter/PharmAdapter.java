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
import com.a1tech.drugprice.Model.Pharm;
import com.a1tech.drugprice.R;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PharmAdapter extends RecyclerView.Adapter<PharmAdapter.MyViewHolder> {

    private final LayoutInflater inflater;
    private final ArrayList<Pharm> pharms;

    private final String pattern = "###,###,###.###";
    private final DecimalFormat decimalFormat = new DecimalFormat(pattern);

    public PharmAdapter(Context context, ArrayList<Pharm> pharms) {
        this.pharms = pharms;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_pharmacies, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Pharm pharms = this.pharms.get(position);

        // formatter of number(1234567890)-- > (1 234 567 890)
        String formatPrice = decimalFormat.format(Double.valueOf(Integer.parseInt(pharms.getThisDrugPrice())));

        holder.pharmName.setText(pharms.getPharmName());
        holder.distance.setText(pharms.getDistance());
        holder.thisDrugPrice.setText(formatPrice + " сум");
        Glide.with(inflater.getContext()).load(pharms.getDrugImage()).into(holder.drugImage);
    }


    @Override
    public int getItemCount() {
        return pharms.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView pharmName, thisDrugPrice, distance;
        ImageView drugImage;

        public MyViewHolder(View itemView) {
            super(itemView);

            pharmName = itemView.findViewById(R.id.tv_pharma_name);
            thisDrugPrice = itemView.findViewById(R.id.tv_drug_price_pharma);
            distance = itemView.findViewById(R.id.tv_distance);
            drugImage = itemView.findViewById(R.id.iv_drug_image_pharma);
        }
    }
}