package com.apps.bguirks.knitbuddy.database.handlers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.apps.bguirks.knitbuddy.database.DatabaseHelper;
import com.apps.bguirks.knitbuddy.database.dataobjects.Instruction;

import java.util.ArrayList;
import java.util.List;

public class InstructionHandler implements Handler<Instruction> {

    // Database access
    private DatabaseHelper dbHelper;

    // Table information
    public static final String TABLE_NAME = "INSTRUCTIONS";
    // Columns
    public static final String ID = "ID";
    public static final String PROJECT_ID = "PROJECT";
    public static final String STEP_NUMBER = "STEP_NUMBER";
    public static final String INSTRUCTION = "INSTRUCTION";
    public static final String COMPLETED = "COMPLETED";
    public static final String COUNTER = "COUNTER";

    public InstructionHandler(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public void createTable(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + PROJECT_ID + " INTEGER NOT NULL, " +
                STEP_NUMBER + " INTEGER NOT NULL, " + INSTRUCTION + " TEXT NOT NULL, " + COMPLETED +
                " INTEGER NOT NULL, " + COUNTER + " INTEGER NOT NULL, FOREIGN KEY(" + PROJECT_ID +
                ") REFERENCES " + ProjectHandler.TABLE_NAME + "(" + ProjectHandler.ID + "));";
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
    public long add(Instruction instruction) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PROJECT_ID, instruction.getProjectId());
        values.put(STEP_NUMBER, instruction.getStepNumber());
        values.put(INSTRUCTION, instruction.getInstruction());
        values.put(COMPLETED, instruction.getCompleted());
        values.put(COUNTER, instruction.getCounter());

        long id = db.insert(TABLE_NAME, null, values);
        db.close();

        return id;
    }

    @Override
    public Instruction get(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] {ID, PROJECT_ID, STEP_NUMBER,
                        INSTRUCTION, COMPLETED}, ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            return null;
        }

        Instruction instruction = new Instruction(Integer.parseInt(cursor.getString(0)),
                Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)),
                cursor.getString(3), Integer.parseInt(cursor.getString(4)),
                Integer.parseInt(cursor.getString(5)));
        cursor.close();
        db.close();
        return instruction;
    }

    @Override
    public List<Instruction> getAll() {
        List<Instruction> instructionList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Instruction instruction = new Instruction();
                instruction.set_id(Integer.parseInt(cursor.getString(0)));
                instruction.setProjectId(Integer.parseInt(cursor.getString(1)));
                instruction.setStepNumber(Integer.parseInt(cursor.getString(2)));
                instruction.setInstruction(cursor.getString(3));
                instruction.setCompleted(Integer.parseInt(cursor.getString(4)));
                instruction.setCounter(Integer.parseInt(cursor.getString(5)));

                instructionList.add(instruction);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return instructionList;
    }

    public List<Instruction> getAll(long projectId) {
        List<Instruction> instructionList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + PROJECT_ID + " = " +
                projectId;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Instruction instruction = new Instruction();
                instruction.set_id(Integer.parseInt(cursor.getString(0)));
                instruction.setProjectId(Integer.parseInt(cursor.getString(1)));
                instruction.setStepNumber(Integer.parseInt(cursor.getString(2)));
                instruction.setInstruction(cursor.getString(3));
                instruction.setCompleted(Integer.parseInt(cursor.getString(4)));
                instruction.setCounter(Integer.parseInt(cursor.getString(5)));

                instructionList.add(instruction);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return instructionList;
    }

    @Override
    public void update(Instruction instruction) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PROJECT_ID, instruction.getProjectId());
        values.put(STEP_NUMBER, instruction.getStepNumber());
        values.put(INSTRUCTION, instruction.getInstruction());
        values.put(COMPLETED, instruction.getCompleted());
        values.put(COUNTER, instruction.getCounter());

        db.update(TABLE_NAME, values, ID + "=?",
                new String[] { String.valueOf(instruction.get_id()) });
        db.close();
    }

    @Override
    public void delete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_NAME, ID + "=?",
                new String[] { String.valueOf(id) });
        db.close();
    }
}
