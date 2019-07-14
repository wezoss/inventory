package com.example.android.inventory;

import android.provider.BaseColumns;

public class Contract {
    private Contract(){}
    public static class ContractN implements BaseColumns{
        static final String DB_NAME="inventory";
        static final String TABLE_NAME="product";
        static final String NAME="name";
        static final String PRICE="price";
        static final String QUANTITY="quantity";
        static final String THUMBNAIL="thumbnail";
        static final String DESC="description";


    }
}
