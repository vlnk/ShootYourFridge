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
    private static final int DATABASE_VERSION = 5;
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
            KEY_REC_NAME + " TEXT," + KEY_REC_DETAILS + " TEXT," +
            KEY_REC_DESCRIPTION + " TEXT," + KEY_REC_HREF + " TEXT" + ")";

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
    @SuppressWarnings("unused")
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

    // Getting single element in FOOD by scan ID
    @SuppressWarnings("unused")
    public String getFoodByScan(String scan) {
        SQLiteDatabase db = this.getReadableDatabase();
        String name = null;

        String selectQuery = "SELECT  * FROM " + TABLE_FOODS + " WHERE "
                + KEY_SCAN_ID + " = " + scan;

        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst())
            name = c.getString(c.getColumnIndex(KEY_NAME));

        return name;
    }

    // Getting single element in FOOD by name
    public Food getFoodByName(String nameToSearch) {
        SQLiteDatabase db = this.getReadableDatabase();


        String selectQuery = "SELECT  * FROM " + TABLE_FOODS + " WHERE "
                + KEY_NAME + " = " + nameToSearch;

        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery("SELECT  * FROM " + TABLE_FOODS + " WHERE "
                + KEY_NAME + " = ?", new String[] {nameToSearch});
        if (c != null)
            c.moveToFirst();

        Food result = new Food();
        assert c != null;
        result.setId(Integer.parseInt(c.getString(0)));
        result.setScanId(c.getString(1));
        result.setName(c.getString(2));

        return result;
    }

    // Updating single FOOD element
    @SuppressWarnings("UnusedReturnValue")
    public int updateAliment(Food fd, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, newName);

        // updating row
        return db.update(TABLE_FOODS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(fd.getId()) });
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
    @SuppressWarnings("unused")
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

    // Updating single CATALOG element
    @SuppressWarnings("unused")
    public int updateCatalogElement(Catalog alimentToModify, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CAT_NAME, newName);

        // updating row
        return db.update(TABLE_CATALOG, values, KEY_CAT_ID + " = ?",
                new String[] { String.valueOf(alimentToModify.getId()) });
    }


    // Getting single element in CATALOG by name
    public Catalog getCatalogByName(String nameToSearch) {
        SQLiteDatabase db = this.getReadableDatabase();
        Catalog result = new Catalog();

        String selectQuery = "SELECT  * FROM " + TABLE_CATALOG + " WHERE "
                + KEY_CAT_NAME + " = " + nameToSearch;

        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery("SELECT  * FROM " + TABLE_CATALOG + " WHERE "
                + KEY_CAT_NAME + " = ?", new String[] {nameToSearch});
        if (c.moveToFirst())
        {
            result.setId(Integer.parseInt(c.getString(0)));
            result.setScanId(c.getString(1));
            result.setName(c.getString(2));
        }
        return result;
    }

    // Updating single CATALOG element
    @SuppressWarnings("UnusedReturnValue")
    public int updateCatalog(Catalog cat, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CAT_NAME, newName);

        // updating row
        return db.update(TABLE_CATALOG, values, KEY_CAT_ID + " = ?",
                new String[] { String.valueOf(cat.getId()) });
    }


    // ------------------------ "Recipe" table methods ----------------//

    // Adding new element to RECIPE
    public boolean addRecipe(Recipe rec) {
        boolean ret = isAlreadyIn(rec);
        if (!ret){
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

        return ret;
    }

    // Removing 1 element by name from RECIPE
    public void deleteRecipe(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECIPE, KEY_REC_NAME + " = ?", new String[]{name});
        db.close();
    }

    public boolean isAlreadyIn(Recipe rec){
        boolean ret = false;
        String nameToSearch = rec.getName();
        String descriptionToSearch = rec.getDescription();
        List<Recipe> listRecipes = getAllInRecipe();
        for (Recipe currentRec : listRecipes){
            if (nameToSearch.compareTo(currentRec.getName()) == 0
                    && descriptionToSearch.compareTo(currentRec.getDescription()) == 0){
                ret = true;
            }
        }

        return ret;
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
                Recipe recipe = new Recipe();
                recipe.setId(Integer.parseInt(cursor.getString(0)));
                recipe.setName(cursor.getString(1));
                recipe.setDescription(cursor.getString(2));
                recipe.setDescription(cursor.getString(3));
                recipe.setHref(cursor.getString(4));

                // Adding fridge to list
                recipeList.add(recipe);
            } while (cursor.moveToNext());
        }
        // return fridge list
        return recipeList;
    }


    // Getting single recipe by name
    @SuppressWarnings("unused")
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
