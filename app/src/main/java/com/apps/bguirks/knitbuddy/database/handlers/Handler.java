package com.apps.bguirks.knitbuddy.database.handlers;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public interface Handler<T> {

    void createTable(SQLiteDatabase db);
    void dropTable(SQLiteDatabase db);
    int getTableCount();
    long add(T obj);
    T get(long id);
    List<T> getAll();
    long update(T obj);
    void delete(long id);

}
