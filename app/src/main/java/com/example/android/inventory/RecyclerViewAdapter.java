package com.example.android.inventory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    Context mContext;
    List<DataSet> items;
    boolean isListView;
    OnItemClickListener onItemClickListener;
    SqliteHelper sqliteHelper;
    int id;


    public RecyclerViewAdapter(Context mContext, List<DataSet> data, boolean isListView, OnItemClickListener onItemClickListener) {
        this.mContext = mContext;
        items = data;
        this.isListView = isListView;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        if (isListView)
            view = layoutInflater.inflate(R.layout.item_listview, viewGroup, false);
        else
            view = layoutInflater.inflate(R.layout.item_view, viewGroup, false);
        return new MyViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        try {
            myViewHolder.quantity.setText(items.get(i).getQuantity() + "");
            if (items.get(i).getQuantity() == 0) {
                myViewHolder.sale.setBackgroundResource(R.color.isolator);
                myViewHolder.sale.setClickable(false);
            }
            myViewHolder.name.setText(items.get(i).getProductName());
            myViewHolder.price.setText(items.get(i).getPrice() + "$");
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        byte[] image = items.get(i).getThumbnail();
        Bitmap bitImage = BitmapFactory.decodeByteArray(image, 0, image.length);
        myViewHolder.thumbnail.setImageBitmap(bitImage);

    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView thumbnail;
        TextView name, price, quantity;
        Button sale;
        OnItemClickListener onItemClickListener;
        CardView cardView;


        public MyViewHolder(@NonNull View itemView, OnItemClickListener itemClick) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantity);
            sale = itemView.findViewById(R.id.sale);
            onItemClickListener = itemClick;
            sqliteHelper = new SqliteHelper(mContext);
            cardView = itemView.findViewById(R.id.alpha);
            sale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int q = Integer.parseInt(quantity.getText() + "");
                    if (q != 0) {
                        q--;
                        quantity.setText(q + "");
                        id=items.get(getAdapterPosition()).getId();
                        items.get(getAdapterPosition()).setQuantity(q);
                        sqliteHelper.update(q, id);
                        if (q == 0) {
                            sale.setBackgroundResource(R.color.isolator);
                            sale.setClickable(false);
                        }

                    } else
                        Toast.makeText(mContext, "quantity can't be less than zero", Toast.LENGTH_LONG).show();


                }
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }


    }

    interface OnItemClickListener {
        void onItemClick(int position);
    }

}
