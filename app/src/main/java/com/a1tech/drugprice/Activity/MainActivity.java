package com.a1tech.drugprice.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.a1tech.drugprice.Adapter.DrugsAdapter;
import com.a1tech.drugprice.Model.Drug;
import com.a1tech.drugprice.R;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    private RecyclerView rvMain;
    private ArrayList<Drug> drugList = new ArrayList<Drug>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setDrugList();
        setAdapter();
    }

    private void init() {
        Objects.requireNonNull(getSupportActionBar()).setTitle("Лекарства");  // provide compatibility to all the versions

        rvMain = findViewById(R.id.rv_main);
    }

    private void setDrugList() {
        drugList.add(new Drug("Ингавирин", "79000", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2FIngavirin-e1565687015128.jpg?alt=media&token=854fa891-08a5-4536-8ca7-9d996f9dbc62", 1));
        drugList.add(new Drug("Климадинон", "88000", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2F2382a661d5cc2229c514c21f33c1bade.jpg?alt=media&token=205eb0e5-927a-4529-ba7b-d02d23c2e002", 2));
        drugList.add(new Drug("Детрифорс", "67900", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2F26132.jpg?alt=media&token=78b90239-d8cd-4bc5-a1a9-ab92cae7ba84", 3));
        drugList.add(new Drug("Эргоферон", "47000", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2F%D0%91%D0%B5%D0%B7%20%D0%BD%D0%B0%D0%B7%D0%B2%D0%B0%D0%BD%D0%B8%D1%8F.png?alt=media&token=d14bcd41-465e-48f6-a972-b68bd271e5f9", 4));
        drugList.add(new Drug("Канефрон", "87000", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2Fcanephron-n-dragees-120-st-768x768.jpg?alt=media&token=38f36b34-3f49-4f00-beb9-a2948923acc4", 5));
        drugList.add(new Drug("Беневрон", "57900", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2F3.jpg?alt=media&token=ae2905aa-7ec9-4500-be48-eef8d0b13cd1", 6));
        drugList.add(new Drug("ФАВИПИРАВИР", "60000", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2FFavipiravir-1.jpg?alt=media&token=a40ee953-4471-4bba-8754-029cefaa8381", 7));
    }

    private void setAdapter() {
        rvMain.setLayoutManager(new GridLayoutManager(this, 2));
        DrugsAdapter drugsAdapter = new DrugsAdapter(getApplicationContext(), drugList);
        rvMain.setAdapter(drugsAdapter); // set the Adapter to RecyclerView
    }
}