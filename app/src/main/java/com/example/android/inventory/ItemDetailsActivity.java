package com.example.android.inventory;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ItemDetailsActivity extends AppCompatActivity {
    List<DataSet> items;
    TextView name, price, description, quantity;
    ImageView thumbnail;
    SqliteHelper sqliteHelper;
    DataSet item;
    ImageView increase, decrease;
    Button order, delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        name = findViewById(R.id.product_name);
        price = findViewById(R.id.price);
        description = findViewById(R.id.description);
        thumbnail = findViewById(R.id.thumbnail);
        quantity = findViewById(R.id.quantity);
        items = new ArrayList<>();
        sqliteHelper = new SqliteHelper(this);
        increase=findViewById(R.id.increase);
        decrease=findViewById(R.id.decrease);
        order=findViewById(R.id.order);
        delete=findViewById(R.id.delete);

        final int position = getIntent().getIntExtra("position", 0);
        readAt();
        init(position);

        //---------------------
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int q=Integer.parseInt(quantity.getText()+"");
                q++;
                quantity.setText(q+"");
                sqliteHelper.update(q,items.get(position).getId());
            }
        });
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int q=Integer.parseInt(quantity.getText()+"");
                if(q!=0){
                    q--;
                quantity.setText(q+"");
                sqliteHelper.update(q,items.get(position).getId());}
                else
                    Toast.makeText(ItemDetailsActivity.this, "quantity can't be less than zero", Toast.LENGTH_LONG).show();


            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent=new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:01013586982"));
            startActivity(intent);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqliteHelper.delete(items.get(position).getId());
                goToMain();
            }
        });
    }

    public void readAt() {
        Cursor dataHolder = sqliteHelper.readALL();

        try {
            while(dataHolder.moveToNext()) {
                int id=dataHolder.getInt(0);
                String name = dataHolder.getString(1);
                float price = dataHolder.getFloat(2);
                int quantity = dataHolder.getInt(3);
                String description = dataHolder.getString(4);
                byte[] thumbnail = dataHolder.getBlob(5);
                items.add(new DataSet(id,name, description, price, quantity, thumbnail));
            }

        } catch (Exception ex) {
            Toast.makeText(this,"exception",Toast.LENGTH_LONG).show();
            ex.printStackTrace();

        } finally {
            dataHolder.close();
        }

    }

    public void goToMain(){
        Intent intent=new Intent(ItemDetailsActivity.this,MainActivity.class);
        startActivity(intent);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToMain();
    }

    public void init(int position) {

        name.setText(items.get(position).getProductName());
        price.setText(items.get(position).getPrice() + "$");
        byte[] image = items.get(position).getThumbnail();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        thumbnail.setImageBitmap(bitmap);
        description.setText(items.get(position).getDescription());
        quantity.setText(items.get(position).getQuantity() + "");
    }
}
