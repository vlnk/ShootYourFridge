package com.project.SYF.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.project.SYF.model.Food;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mimmy on 25/04/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "foodManager";
    // Fridges table name
    private static final String TABLE_FOODS = "Food";

    // Fridges Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_SCAN_ID = "scanId";
    private static final String KEY_NAME = "name";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Fridge table create statement
    private static final String CREATE_TABLE_FOOD = "CREATE TABLE " +
            TABLE_FOODS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SCAN_ID
            + " INTEGER," + KEY_NAME + " TEXT" + ")";

    // Creating Table
    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_FOOD);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOODS);

        // Create tables again
        onCreate(db);
    }


    // ------------------------ "food" table methods ----------------//

    // Adding new element to FOOD
    public void addFood(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SCAN_ID, food.getScanId());
        values.put(KEY_NAME, food.getName());

        // methods not added yet !!
        //values.put(KEY_SCAN_ID, food.getBrand());

        // Inserting Row
        db.insert(TABLE_FOODS, null, values);
        db.close(); // Closing database connection
    }

    // Removing 1 element from FOOD
    public void deleteAliment(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FOODS, KEY_ID + " = ?",
                new String[] { String.valueOf(food.getId()) });
        db.close();
    }

    // Removing 1 element by name from FOOD
    public void deleteAliment(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FOODS, KEY_NAME + " = ?", new String[] { name });
        db.close();
    }

    // Getting All elements in FOOD
    public List<Food> getAllInFood() {
        List<Food> foodList = new ArrayList<Food>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FOODS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Food food = new Food();
                food.setId(Integer.parseInt(cursor.getString(0)));
                food.setScanId(cursor.getInt(1));
                food.setName(cursor.getString(2));
                // Adding fridge to list
                foodList.add(food);
            } while (cursor.moveToNext());
        }

        // return fridge list
        return foodList;
    }


}
