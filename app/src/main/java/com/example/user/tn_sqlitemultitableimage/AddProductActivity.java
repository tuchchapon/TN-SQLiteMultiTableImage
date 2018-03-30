package com.example.user.tn_sqlitemultitableimage;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.luseen.simplepermission.permissions.Permission;
import com.luseen.simplepermission.permissions.PermissionActivity;
import com.luseen.simplepermission.permissions.PermissionUtils;
import com.luseen.simplepermission.permissions.SinglePermissionCallback;
import com.vlk.multimager.activities.GalleryActivity;
import com.vlk.multimager.utils.Constants;
import com.vlk.multimager.utils.Image;
import com.vlk.multimager.utils.Params;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AddProductActivity extends PermissionActivity {

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_product);
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

            Button btSelectImg = (Button)findViewById(R.id.button_image);
            btSelectImg.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        showGallery();
                  }
            });

            Button buttonSave = (Button)findViewById(R.id.button_save);
            buttonSave.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        saveData();
                  }
            });

      }

      private void showGallery() {
            //ส่งอินเทนต์เพื่อเลือกภาพ
            Intent intent = new Intent(this, GalleryActivity.class);
            Params params = new Params();
            params.setPickerLimit(1);   	//จำนวนภาพที่เลือกได้
            intent.putExtra(Constants.KEY_PARAMS, params);
            startActivityForResult(intent, Constants.TYPE_MULTI_PICKER);
      }

      //หลังเลือกภาพเสร็จ ให้นำภาพนั้นไปแสดงใน ImageView ให้เห็นด้วย
      @Override
      public void onActivityResult(int rqCode, int rsCode, Intent intent)  {
            if(rqCode != Constants.TYPE_MULTI_PICKER && rsCode != RESULT_OK) {
                  return;
            }

            ArrayList<Image> images = intent.getParcelableArrayListExtra(
                    Constants.KEY_BUNDLE_LIST);

            Uri uri = images.get(0).uri;
            ImageView imageView = (ImageView)findViewById(R.id.image_view);
            imageView.setImageURI(uri);
            imageView.setVisibility(View.VISIBLE);
      }

      //เมธอดนี้จะถูกเรียกเมื่อคลิกปุ่มบันทึกข้อมูล
      private void saveData() {
            ImageView imageView = (ImageView)findViewById(R.id.image_view);
            if(imageView.getDrawable() == null) {
                  showToast("ต้องมีรูปภาพ");
                  return;
            }

            EditText editName = (EditText)findViewById(R.id.edit_name);
            if(editName.getText().toString().trim().equals("")) {
                  showToast("ต้องกำหนดชื่อ");
                  return;
            }

            SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance(this);
            SQLiteDatabase db = sqLiteHelper.getWritableDatabase();

            EditText editPrice = (EditText)findViewById(R.id.edit_price);
             String name = editName.getText().toString();
            String price = editPrice.getText().toString();

            ContentValues cv = new ContentValues();
            cv.put("pro_name", name);
            cv.put("price", price);
            long pro_id = db.insert("product", null, cv);

            int[] edits = {R.id.edit_char1, R.id.edit_char2, R.id.edit_char3, R.id.edit_char4, R.id.edit_char5};
            for(int i = 0; i < edits.length; i++) {
                  String ch = ((EditText)findViewById(edits[i])).getText().toString();
                  if(ch.trim().equals("")) {
                        continue;
                  }
                  cv = new ContentValues();
                  cv.put("pro_id", pro_id);
                  cv.put("pro_characteristic", ch);
                  db.insert("characteristic", null, cv);
            }

            BitmapDrawable bitmapDrawable = (BitmapDrawable)imageView.getDrawable();
            byte[] imgContent = getBytes(bitmapDrawable.getBitmap());

            cv = new ContentValues();
            cv.put("pro_id", pro_id);
            cv.put("pro_image", imgContent);
            db.insert("image", null, cv);

            showToast("บันทึกข้อมูลเสร็จแล้ว");
      }

      public byte[] getBytes(Bitmap bitmap) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            return stream.toByteArray();
      }

      private void showToast(String msg) {
            Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
      }
}
