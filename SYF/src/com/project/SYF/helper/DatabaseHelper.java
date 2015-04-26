package com.project.SYF.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.project.SYF.model.Food;
import com.project.SYF.model.Catalog;
import com.project.SYF.model.Recipe;

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
    private static final int DATABASE_VERSION = 3;
    // Database Name
    private static final String DATABASE_NAME = "foodManager";

    // tables names
    private static final String TABLE_FOODS = "Food";
    private static final String TABLE_CATALOG = "Catalog";
    private static final String TABLE_RECIPE = "Recipe";


    // Fridges Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_SCAN_ID = "scanId";
    private static final String KEY_NAME = "name";
    // Catalog Table Columns names
    private static final String KEY_CAT_ID = "id";
    private static final String KEY_CAT_SCAN_ID = "scanId";
    private static final String KEY_CAT_NAME = "name";
    // Recipe Table Columns names
    private static final String KEY_REC_ID = "id";
    private static final String KEY_REC_NAME = "name";
    private static final String KEY_REC_DETAILS = "details";
    private static final String KEY_REC_DESCRIPTION = "description";
    private static final String KEY_REC_HREF = "href";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Food table create statement
    private static final String CREATE_TABLE_FOOD = "CREATE TABLE " +
            TABLE_FOODS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SCAN_ID
            + " INTEGER," + KEY_NAME + " TEXT" + ")";

    // Catalog table create statement
    private static final String CREATE_TABLE_CATALOG = "CREATE TABLE " +
            TABLE_CATALOG + "(" + KEY_CAT_ID + " INTEGER PRIMARY KEY," + KEY_CAT_SCAN_ID
            + " INTEGER," + KEY_CAT_NAME + " TEXT" + ")";

    // Recipe table create statement
    private static final String CREATE_TABLE_RECIPE = "CREATE TABLE " +
            TABLE_RECIPE + "(" + KEY_REC_ID + " INTEGER PRIMARY KEY," +
            KEY_REC_NAME + " TEXT," + KEY_REC_DETAILS + " TEXT" +
            KEY_REC_DESCRIPTION + " TEXT" + KEY_REC_HREF + " TEXT" + ")";

    // Creating Table
    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_FOOD);
        db.execSQL(CREATE_TABLE_CATALOG);
        db.execSQL(CREATE_TABLE_RECIPE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOODS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATALOG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE);

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
                food.setScanId(cursor.getString(1));
                food.setName(cursor.getString(2));
                // Adding fridge to list
                foodList.add(food);
            } while (cursor.moveToNext());
        }

        // return fridge list
        return foodList;
    }

    // Getting single element in FOOD
    public String getFoodByScan(String scan) {
        SQLiteDatabase db = this.getReadableDatabase();
        String name = null;

        String selectQuery = "SELECT  * FROM " + TABLE_FOODS + " WHERE "
                + KEY_SCAN_ID + " = " + scan;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst())
        {
            name = c.getString(c.getColumnIndex(KEY_NAME));
        }

        return name;
    }



    // ------------------------ "catalog" table methods ----------------//

    // Adding new element to CATALOG
    public void addCatalog(Catalog cat) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_CAT_SCAN_ID, cat.getScanId());
        values.put(KEY_CAT_NAME, cat.getName());


        // Inserting Row
        db.insert(TABLE_CATALOG, null, values);
        db.close(); // Closing database connection
    }

    // Removing 1 element from CATALOG
    public void deleteCatalog(Catalog cat) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATALOG, KEY_CAT_ID + " = ?",
                new String[] { String.valueOf(cat.getId()) });
        db.close();
    }

    // Removing 1 element by name from CATALOG
    public void deleteCatalog(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATALOG, KEY_CAT_NAME + " = ?", new String[] { name
        });
        db.close();
    }

    // Getting All elements in CATALOG
    public List<Catalog> getAllInCatalog() {
        List<Catalog> catList = new ArrayList<Catalog>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CATALOG;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Catalog cat = new Catalog();
                cat.setId(Integer.parseInt(cursor.getString(0)));
                cat.setScanId(cursor.getString(1));
                cat.setName(cursor.getString(2));
                // Adding fridge to list
                catList.add(cat);
            } while (cursor.moveToNext());
        }

        // return fridge list
        return catList;
    }

    // Getting single catalog
    public String getCatalogByScan(String scan) {
        SQLiteDatabase db = this.getReadableDatabase();
        String name = null;

        String selectQuery = "SELECT  * FROM " + TABLE_CATALOG + " WHERE "
                + KEY_CAT_SCAN_ID + " = " + scan;

        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst())
            name = c.getString(c.getColumnIndex(KEY_CAT_NAME));

        return name;
    }


    // ------------------------ "Recipe" table methods ----------------//

    // Adding new element to RECIPE
    public void addRecipe(Recipe rec) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_REC_NAME, rec.getName());
        values.put(KEY_REC_DETAILS, rec.getDetails());
        values.put(KEY_REC_DESCRIPTION, rec.getDescription());
        values.put(KEY_REC_HREF, rec.getHref());

        // Inserting Row
        db.insert(TABLE_RECIPE, null, values);
        db.close(); // Closing database connection
    }

    // Getting All elements in RECIPE
    public List<Recipe> getAllInRecipe() {
        List<Recipe> recipeList = new ArrayList<Recipe>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RECIPE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Recipe reccipe = new Recipe();
                reccipe.setId(Integer.parseInt(cursor.getString(0)));
                reccipe.setName(cursor.getString(1));
                reccipe.setDescription(cursor.getString(2));
                reccipe.setDescription(cursor.getString(3));
                reccipe.setHref(cursor.getString(4));

                // Adding fridge to list
                recipeList.add(reccipe);
            } while (cursor.moveToNext());
        }
        // return fridge list
        return recipeList;
    }


    // Getting single recipe by name
    public String getRecipeByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_RECIPE + " WHERE "
                + KEY_REC_NAME + " = " + name;

        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst())
            name = c.getString(c.getColumnIndex(KEY_REC_NAME));

        return name;
    }





}
