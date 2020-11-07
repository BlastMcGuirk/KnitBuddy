package com.apps.bguirks.knitbuddy.database.handlers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.apps.bguirks.knitbuddy.database.DatabaseHelper;
import com.apps.bguirks.knitbuddy.database.dataobjects.Project;

import java.util.ArrayList;
import java.util.List;

public class ProjectHandler implements Handler<Project> {

    // Database access
    private DatabaseHelper dbHelper;

    // Table information
    public static final String TABLE_NAME = "PROJECTS";
    // Columns
    public static final String ID = "ID";
    public static final String PROJECT_NAME = "PROJECT";
    public static final String CATEGORY_ID = "CATEGORY_ID";
    public static final String NEEDLE_TYPE = "NEEDLE_TYPE";
    public static final String NEEDLE_SIZE = "NEEDLE_SIZE";
    public static final String YARN_BRAND = "YARN_BRAND";
    public static final String YARN_SIZE = "YARN_SIZE";

    public ProjectHandler(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public void createTable(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + PROJECT_NAME + " TEXT NOT NULL, " +
                CATEGORY_ID + " INTEGER, " + NEEDLE_TYPE + " TEXT, " + NEEDLE_SIZE +
                " TEXT, " + YARN_BRAND + " TEXT, " + YARN_SIZE + " TEXT, FOREIGN KEY(" +
                CATEGORY_ID + ") REFERENCES " + CategoryHandler.TABLE_NAME +
                "(" + CategoryHandler.ID + "));";
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
    public long add(Project project) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PROJECT_NAME, project.getProjectName());
        values.put(CATEGORY_ID, project.getCategoryId());
        values.put(NEEDLE_TYPE, project.getNeedleType());
        values.put(NEEDLE_SIZE, project.getNeedleSize());
        values.put(YARN_BRAND, project.getYarnBrand());
        values.put(YARN_SIZE, project.getYarnSize());

        long id = db.insert(TABLE_NAME, null, values);
        db.close();

        return id;
    }

    @Override
    public Project get(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] {ID, PROJECT_NAME, CATEGORY_ID,
                        NEEDLE_TYPE, NEEDLE_SIZE, YARN_BRAND, YARN_SIZE}, ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }

        Project project = new Project(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                cursor.getString(3), cursor.getString(4),
                cursor.getString(5), cursor.getString(6));
        cursor.close();
        db.close();
        return project;
    }

    @Override
    public List<Project> getAll() {
        List<Project> projectList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Project project = new Project();
                project.set_id(Integer.parseInt(cursor.getString(0)));
                project.setProjectName(cursor.getString(1));
                project.setCategoryId(Integer.parseInt(cursor.getString(2)));
                project.setNeedleType(cursor.getString(3));
                project.setNeedleSize(cursor.getString(4));
                project.setYarnBrand(cursor.getString(5));
                project.setYarnSize(cursor.getString(6));

                projectList.add(project);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return projectList;
    }

    public List<Project> getAll(long categoryID) {
        List<Project> projectList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + CATEGORY_ID + " = " +
                categoryID;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Project project = new Project();
                project.set_id(Integer.parseInt(cursor.getString(0)));
                project.setProjectName(cursor.getString(1));
                project.setCategoryId(Integer.parseInt(cursor.getString(2)));
                project.setNeedleType(cursor.getString(3));
                project.setNeedleSize(cursor.getString(4));
                project.setYarnBrand(cursor.getString(5));
                project.setYarnSize(cursor.getString(6));

                projectList.add(project);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return projectList;
    }

    @Override
    public long update(Project project) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PROJECT_NAME, project.getProjectName());
        values.put(CATEGORY_ID, project.getCategoryId());
        values.put(NEEDLE_TYPE, project.getNeedleType());
        values.put(NEEDLE_SIZE, project.getNeedleSize());
        values.put(YARN_BRAND, project.getYarnBrand());
        values.put(YARN_SIZE, project.getYarnSize());

        long id = db.update(TABLE_NAME, values, ID + "=?",
                new String[] { String.valueOf(project.get_id()) });
        db.close();

        return id;
    }

    @Override
    public void delete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_NAME, ID + "=?",
                new String[] { String.valueOf(id) });
        db.close();
    }

}
