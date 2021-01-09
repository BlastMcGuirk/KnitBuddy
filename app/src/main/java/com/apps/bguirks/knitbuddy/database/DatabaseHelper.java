package com.apps.bguirks.knitbuddy.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.apps.bguirks.knitbuddy.database.handlers.CategoryHandler;
import com.apps.bguirks.knitbuddy.database.handlers.InstructionHandler;
import com.apps.bguirks.knitbuddy.database.handlers.ProjectHandler;

public class DatabaseHelper extends SQLiteOpenHelper {

    // ----------------------------------------
    // Database Information
    // ----------------------------------------
    // Database Version
    static final int DB_VERSION = 2;
    // Database Information
    static final String DB_NAME = "KNIT_BUDDY.DB";

    // ----------------------------------------
    // Table Handlers
    // ----------------------------------------
    public CategoryHandler categories;
    public ProjectHandler projects;
    public InstructionHandler instructions;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        categories = new CategoryHandler(this);
        projects = new ProjectHandler(this);
        instructions = new InstructionHandler(this);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createAllTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropAllTables(db);
        onCreate(db);
    }

    private void createAllTables(SQLiteDatabase db) {
        categories.createTable(db);
        projects.createTable(db);
        instructions.createTable(db);
    }

    private void dropAllTables(SQLiteDatabase db) {
        categories.dropTable(db);
        projects.dropTable(db);
        instructions.dropTable(db);
    }

}
