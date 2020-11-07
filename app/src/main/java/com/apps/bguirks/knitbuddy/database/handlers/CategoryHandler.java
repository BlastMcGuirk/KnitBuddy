package com.apps.bguirks.knitbuddy.database.handlers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.apps.bguirks.knitbuddy.database.DatabaseHelper;
import com.apps.bguirks.knitbuddy.database.dataobjects.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryHandler implements Handler<Category> {

    // Database access
    private DatabaseHelper dbHelper;

    // Table information
    public static final String TABLE_NAME = "CATEGORIES";
    // Columns
    public static final String ID = "ID";
    public static final String CATEGORY_NAME = "CATEGORY_NAME";

    public CategoryHandler(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public void createTable(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + CATEGORY_NAME + " TEXT NOT NULL);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    @Override
    public int getTableCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count;
    }

    @Override
    public long add(Category category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CATEGORY_NAME, category.getCategory());

        long id = db.insert(TABLE_NAME, null, values);
        db.close();

        return id;
    }

    @Override
    public Category get(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] {ID, CATEGORY_NAME}, ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }

        Category category = new Category(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1));
        cursor.close();
        db.close();

        return category;
    }

    @Override
    public List<Category> getAll() {
        List<Category> categoryList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.set_id(Integer.parseInt(cursor.getString(0)));
                category.setCategory(cursor.getString(1));

                categoryList.add(category);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return categoryList;
    }

    @Override
    public long update(Category category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CATEGORY_NAME, category.getCategory());

        long id = db.update(TABLE_NAME, values, ID + "=?",
                new String[] { String.valueOf(category.get_id()) });
        db.close();

        return id;
    }

    @Override
    public void delete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_NAME, ID + "=?", new String[] { String.valueOf(id) });
        db.close();
    }
}
