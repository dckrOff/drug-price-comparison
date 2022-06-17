package com.a1tech.drugprice.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.a1tech.drugprice.Activity.PharmsActivity;
import com.a1tech.drugprice.Activity.RouteActivity;
import com.a1tech.drugprice.Model.Drug;
import com.a1tech.drugprice.Model.Pharm;
import com.a1tech.drugprice.R;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PharmAdapter extends RecyclerView.Adapter<PharmAdapter.MyViewHolder> {

    private final LayoutInflater inflater;
    private final ArrayList<Pharm> pharms;
    private PharmsActivity pharmsActivity = new PharmsActivity();

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
//        holder.distance.setText(getDistance(pharms));
        holder.distance.setText("200");
        holder.thisDrugPrice.setText(formatPrice + " сум");
        Glide.with(inflater.getContext()).load(pharms.getDrugImage()).into(holder.drugImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(inflater.getContext(), RouteActivity.class);
                intent.putExtra("pharma_name", pharms.getPharmName());
                intent.putExtra("drug_image", pharms.getDrugImage());
                intent.putExtra("lat", pharms.getLat());
                intent.putExtra("lon", pharms.getLon());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                inflater.getContext().startActivity(intent);
            }
        });
    }

    private String getDistance(Pharm pharm) {
        float distance = pharmsActivity.calculateDistance(pharmsActivity.getCurrentLocation().latitude, pharmsActivity.getCurrentLocation().longitude, pharm.getLat(), pharm.getLon());
        String dis = null;
        if (distance > 999) {
            dis = (distance / 100) + "км от вас";
            return dis;
        } else {
            dis = distance + "м от вас";
            return dis;
        }
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
