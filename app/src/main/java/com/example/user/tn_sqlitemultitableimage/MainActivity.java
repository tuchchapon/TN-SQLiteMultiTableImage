package com.example.user.tn_sqlitemultitableimage;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this, AddProductActivity.class));
                  }
            });

      }

      @Override
      public void onStart() {
            super.onStart();

            LayoutInflater inflater = getLayoutInflater();
            LinearLayout root = (LinearLayout) findViewById(R.id.main_layout);
            root.removeAllViewsInLayout();

            SQLiteHelper sqlite = SQLiteHelper.getInstance(this);
            SQLiteDatabase db = sqlite.getReadableDatabase();

            String sql =
                    "SELECT product.*, image.pro_image FROM product " +
                        "LEFT JOIN image " +
                        "ON product._id = image.pro_id";

            Cursor cursor = db.rawQuery(sql, null);

            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            param.bottomMargin = 50;

            int i = 0;
            while(cursor.moveToNext()) {
                  final View item = inflater.inflate(R.layout.item_layout, null);
                  item.setLayoutParams(param);

                  ImageView imageView = (ImageView)item.findViewById(R.id.image_view);
                  imageView.setImageBitmap(getImage(cursor.getBlob(3)));

                  TextView textName = (TextView)item.findViewById(R.id.text_name);
                  textName.setText(cursor.getString(1));
                  item.setTag(cursor.getString(0));
                  item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                              Intent intent  = new Intent(MainActivity.this, ProductDetailActivity.class);
                              intent.putExtra("_id", item.getTag().toString());
                              startActivity(intent);
                        }
                  });

                  root.addView(item, i);
                  i++;
            }

            cursor.close();
      }

      public Bitmap getImage(byte[] image) {
            return BitmapFactory.decodeByteArray(image, 0, image.length);
      }

      @Override
      public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
      }

      @Override
      public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if(id == R.id.action_settings) {
                  return true;
            }

            return super.onOptionsItemSelected(item);
      }
}
