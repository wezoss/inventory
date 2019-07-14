package com.example.android.inventory;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnItemClickListener {
    SqliteHelper sqlHelper = new SqliteHelper(this);
    TextView inventoryCase;
    List<DataSet> items;
    FloatingActionButton add;
    RecyclerViewAdapter adapter;
    GridLayoutManager gridLayoutManager;
    LinearLayoutManager linearLayoutManager;
    ImageButton listView, gridView;
    ImageView cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list_view);
        gridView = findViewById(R.id.grid_view);
        add = findViewById(R.id.fab);
        inventoryCase = findViewById(R.id.inventory_case);
        items = new ArrayList<>();
        cart=findViewById(R.id.cart);

        gridView.getDrawable().setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        putData();
        if (!items.isEmpty()) {
            initGrid();
        }
        gridView.getDrawable().setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        gridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridView.getDrawable().setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                listView.getDrawable().setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.black), PorterDuff.Mode.SRC_IN);
                initGrid();
            }
        });
        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.getDrawable().setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                gridView.getDrawable().setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.black), PorterDuff.Mode.SRC_IN);
                initList();

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DataContainerActivity.class);
                startActivity(intent);
            }
        });

    }


    public void initGrid() {
        adapter = new RecyclerViewAdapter(this, items, false, this);
        RecyclerView recycler = findViewById(R.id.recycler);
        gridLayoutManager = new GridLayoutManager(this, numberPossible());

        recycler.setLayoutManager(gridLayoutManager);
        recycler.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        if(items.size()>0){
            cart.setVisibility(View.GONE);
        if (items.size() == 1)
            inventoryCase.setText(items.size() + " item");
        else
            inventoryCase.setText(items.size() + " items");
        }
    }

    public void initList() {
        adapter = new RecyclerViewAdapter(this, items, true, this);
        RecyclerView recycler = findViewById(R.id.recycler);
        linearLayoutManager = new LinearLayoutManager(this);

        recycler.setLayoutManager(linearLayoutManager);
        recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public int numberPossible() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpwidth = displayMetrics.widthPixels / displayMetrics.density;
        int numberPossible = (int) (dpwidth / 170);
        return numberPossible;
    }

    public void putData() {
        Cursor dataHolder = sqlHelper.readALL();
        if (dataHolder.getCount() == 0) {
            inventoryCase.setText(R.string.null_data_count);
            return;
        }
        try {
            while (dataHolder.moveToNext()) {
                int id=dataHolder.getInt(0);
                String name = dataHolder.getString(1);
                float price = dataHolder.getFloat(2);
                int quantity = dataHolder.getInt(3);
                byte[] thumbnail = dataHolder.getBlob(5);
                items.add(new DataSet(id,name," ", price, quantity, thumbnail));

            }
        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            dataHolder.close();
        }

    }

    @Override
    public void onItemClick(int position) {
        Intent intent=new Intent(MainActivity.this,ItemDetailsActivity.class);
        intent.putExtra("position",position);
        startActivity(intent);
   }
}
