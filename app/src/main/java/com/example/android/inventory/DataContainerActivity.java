package com.example.android.inventory;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.InputDevice;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class DataContainerActivity extends AppCompatActivity {
    SqliteHelper sqlHelper = new SqliteHelper(this);
    EditText name, price, quantity, description;
    ImageView thumbnail;
    Button done;
    TextView addPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding
        setContentView(R.layout.activity_data_container);
        name = findViewById(R.id.product_name);
        price = findViewById(R.id.price);
        quantity = findViewById(R.id.quantity);
        description = findViewById(R.id.description);
        thumbnail = findViewById(R.id.thumbnail);
        done = findViewById(R.id.done);
        addPhoto = findViewById(R.id.add_photo);

        //---------------------
        thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(DataContainerActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 999);

            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] bitmapdata;
                try {
                    Drawable d = thumbnail.getDrawable();
                    Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    bitmapdata = stream.toByteArray();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(DataContainerActivity.this, "please add a photo", Toast.LENGTH_LONG).show();
                    thumbnail.setBackgroundResource(R.drawable.error_edittext);
                    return;
                }
                if (validate()) {
                    //insert data
                    sqlHelper.insert(name.getText() + "", Float.parseFloat(price.getText() + ""), Integer.parseInt(quantity.getText() + ""), bitmapdata, description.getText() + "");
                    Intent intent = new Intent(DataContainerActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        });


    }

    public boolean validate() {
        boolean doValidate = true;
        if ((name.getText() + "").equals("") || (price.getText() + "").equals("") || (quantity.getText() + "").equals("") || (description.getText() + "").equals("")) {
            Toast.makeText(this, "please fill in the blank", Toast.LENGTH_LONG).show();
            if ((name.getText() + "").equals("")) {
                name.setBackgroundResource(R.drawable.error_edittext);
                doValidate = false;
            }
            if ((price.getText() + "").equals("")) {
                price.setBackgroundResource(R.drawable.error_edittext);
                doValidate = false;
            }
            if ((quantity.getText() + "").equals("")) {
                quantity.setBackgroundResource(R.drawable.error_edittext);
                doValidate = false;
            }
            if ((description.getText() + "").equals("")) {
                description.setBackgroundResource(R.drawable.error_edittext);
                doValidate = false;
            }
            return doValidate;
        }
        if (price.getText().toString().length() > 8) {
            Toast.makeText(this, "price is too high!", Toast.LENGTH_LONG).show();
            price.setBackgroundResource(R.drawable.error_edittext);
            doValidate = false;
        }
        if (quantity.getText().toString().length() > 5) {
            Toast.makeText(this, "quantity is too big!", Toast.LENGTH_LONG).show();
            quantity.setBackgroundResource(R.drawable.error_edittext);
            doValidate = false;
        }

        return doValidate;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 999) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 999);
                addPhoto.setVisibility(View.GONE);
            } else {
                Toast.makeText(getBaseContext(), "You're not allowed to access photos", Toast.LENGTH_LONG).show();
            }
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap image = BitmapFactory.decodeStream(inputStream);
                thumbnail.setImageBitmap(image);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();

            }
        }
    }
}
