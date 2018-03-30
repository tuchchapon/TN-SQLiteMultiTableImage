package com.example.user.tn_sqlitemultitableimage;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteHelper extends SQLiteOpenHelper {
      private static SQLiteHelper sqLiteDB;
      private static final String DB_NAME = "db_product";
      private static final int DB_VERSION = 1;

      private SQLiteHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
      }

      public static synchronized SQLiteHelper getInstance(Context context) {
            if(sqLiteDB == null) {
                  sqLiteDB = new SQLiteHelper(context.getApplicationContext());
            }
            return sqLiteDB;
      }

      @Override
      public void onCreate(SQLiteDatabase db) {
            String sql =
                    "CREATE TABLE IF NOT EXISTS product(" +
                            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "pro_name TEXT, " +
                            "price INTEGER)";
            db.execSQL(sql);

            sql =
                    "CREATE TABLE IF NOT EXISTS characteristic(" +
                            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "pro_id INTEGER, " +
                            "pro_characteristic TEXT)";
            db.execSQL(sql);

            sql =
                    "CREATE TABLE IF NOT EXISTS image(" +
                            "pro_id INTEGER, " +
                            "pro_image BLOB)";
            db.execSQL(sql);
      }

      @Override
      public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {

      }
}

