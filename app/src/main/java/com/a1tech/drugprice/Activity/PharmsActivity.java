package com.a1tech.drugprice.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.a1tech.drugprice.Adapter.DrugsAdapter;
import com.a1tech.drugprice.Adapter.PharmAdapter;
import com.a1tech.drugprice.Model.Drug;
import com.a1tech.drugprice.Model.Pharm;
import com.a1tech.drugprice.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

public class PharmsActivity extends AppCompatActivity {

    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView rvPharma;
    private ArrayList<Pharm> pharms = new ArrayList<Pharm>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug);

        init();
        setList();
        setAdapter();

    }

    private void init() {
        rvPharma = findViewById(R.id.rv_pharma);

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null, false);
        BottomSheetBehavior.from(view).setState(BottomSheetBehavior.STATE_COLLAPSED);
        BottomSheetBehavior.from(view).setPeekHeight(280);
    }

    private void setList() {
        pharms.add(new Pharm("Gulora MED PLUS 5+", "79000", "200м от вас", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2FIngavirin-e1565687015128.jpg?alt=media&token=854fa891-08a5-4536-8ca7-9d996f9dbc62"));
        pharms.add(new Pharm("Gulora MED PLUS 5+", "79000", "200м от вас", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2FIngavirin-e1565687015128.jpg?alt=media&token=854fa891-08a5-4536-8ca7-9d996f9dbc62"));
        pharms.add(new Pharm("Gulora MED PLUS 5+", "79000", "200м от вас", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2FIngavirin-e1565687015128.jpg?alt=media&token=854fa891-08a5-4536-8ca7-9d996f9dbc62"));
        pharms.add(new Pharm("Gulora MED PLUS 5+", "79000", "200м от вас", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2FIngavirin-e1565687015128.jpg?alt=media&token=854fa891-08a5-4536-8ca7-9d996f9dbc62"));
        pharms.add(new Pharm("Gulora MED PLUS 5+", "79000", "200м от вас", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2FIngavirin-e1565687015128.jpg?alt=media&token=854fa891-08a5-4536-8ca7-9d996f9dbc62"));
        pharms.add(new Pharm("Gulora MED PLUS 5+", "79000", "200м от вас", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2FIngavirin-e1565687015128.jpg?alt=media&token=854fa891-08a5-4536-8ca7-9d996f9dbc62"));
        pharms.add(new Pharm("Gulora MED PLUS 5+", "79000", "200м от вас", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2FIngavirin-e1565687015128.jpg?alt=media&token=854fa891-08a5-4536-8ca7-9d996f9dbc62"));
        pharms.add(new Pharm("Gulora MED PLUS 5+", "79000", "200м от вас", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2FIngavirin-e1565687015128.jpg?alt=media&token=854fa891-08a5-4536-8ca7-9d996f9dbc62"));
        pharms.add(new Pharm("Gulora MED PLUS 5+", "79000", "200м от вас", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2FIngavirin-e1565687015128.jpg?alt=media&token=854fa891-08a5-4536-8ca7-9d996f9dbc62"));
    }

    private void setAdapter() {
        rvPharma.setLayoutManager(new GridLayoutManager(this, 1));
        PharmAdapter pharmAdapter = new PharmAdapter(getApplicationContext(), pharms);
        rvPharma.setAdapter(pharmAdapter); // set the Adapter to RecyclerView
    }
}