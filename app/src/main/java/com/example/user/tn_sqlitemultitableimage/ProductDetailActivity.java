package com.example.user.tn_sqlitemultitableimage;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.text.NumberFormat;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductDetailActivity extends AppCompatActivity {

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_product_detail);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                  }
            });
            fab.hide();
      }

      @Override
      public void onStart() {
            super.onStart();
            Intent intent = getIntent();
            String _id = intent.getStringExtra("_id");

            SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance(getBaseContext());
            SQLiteDatabase db = sqLiteHelper.getReadableDatabase();

            String sql =
                    "SELECT product.*, image.pro_image FROM product " +
                        "LEFT JOIN image " +
                        "ON product._id = image.pro_id " +
                        "WHERE product._id = " + _id;

            Cursor cursor = db.rawQuery(sql, null);

            ImageView imageView = (ImageView)findViewById(R.id.image_view);
            TextView textName = (TextView)findViewById(R.id.text_name);
            TextView textPrice = (TextView)findViewById(R.id.text_price);
            WebView webView = (WebView)findViewById(R.id.web_view);

            if(cursor.moveToNext()) {
                  imageView.setImageBitmap(getImage(cursor.getBlob(3)));
                  textName.setText(cursor.getString(1));
                  NumberFormat numFormat = NumberFormat.getInstance();   //่java.text.NumberFormat
                  int price = cursor.getInt(2);
                  textPrice.setText("ราคา: " + numFormat.format(price) + " บาท");
            }
            cursor.close();

            sql = "SELECT pro_characteristic FROM characteristic WHERE pro_id = " + _id;
            cursor = db.rawQuery(sql, null);

            String html = "<ul>";
            while(cursor.moveToNext()) {
                  html += "<li>" + cursor.getString(0) + "</li>";
            }
            html += "</ul>";
            webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);

            cursor.close();
      }

      public Bitmap getImage(byte[] image) {
            return BitmapFactory.decodeByteArray(image, 0, image.length);
      }
}
