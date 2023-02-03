package com.medical.medicalmap.assist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class Helper extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    public static String dbName = "MyDB";
    public static int dbVersion = 2;
    private static Helper instance;

    public Helper(Context context) {
        super(context, dbName, null, dbVersion);
    }

    public static Helper getInstance(Context context) {
        if (instance == null) {
            instance = new Helper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE Medi " +
                "(ITEM_KEY INTEGER PRIMARY KEY AUTOINCREMENT " +
                ", gb VARCHAR(10)" +
                ", name VARCHAR(20)" +
                ", address VARCHAR(200)" +
                ", tel VARCHAR(20));";
        db.execSQL(create);

        String create2 = "CREATE TABLE Medical " +
                "(ITEM_KEY INTEGER PRIMARY KEY AUTOINCREMENT " +
                ", gb2 INTEGER" +
                ", name2 VARCHAR(20)" +
                ", address2 VARCHAR(200)" +
                ", tel2 VARCHAR(20));";
        db.execSQL(create2);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop = "DROP TABLE IF EXISTS Medi;";
        String drop2 = "DROP TABLE IF EXISTS Medical;";
        db.execSQL(drop);
        db.execSQL(drop2);
        onCreate(db);
    }

    public void insert(String gb, String name, String address, String tel) {
        db = getWritableDatabase();
        String insert = "INSERT INTO Medi (gb, name, address, tel) VALUES ('"+gb+"', '"+name+"', '"+address+"', '"+tel+"');";
        db.execSQL(insert);
    }

    public void update(int itemKey, String gb) {
        db = getWritableDatabase();
        String update = "UPDATE Medi SET GB_1 = '"+gb+"' WHERE ITEM_KEY = "+itemKey;
        db.execSQL(update);
    }

    public void delete() {
        db = getWritableDatabase();
        String delete = "DELETE FROM Medi";
        db.execSQL(delete);
    }

    public ArrayList<Item> select(String addr) {
        db = getReadableDatabase();
        String select = "SELECT ITEM_KEY, gb, name, address, tel FROM Medi WHERE address like '%"+addr+"'";
        Cursor cursor = db.rawQuery(select, null);

//        Item item = new Item();
//       while (cursor.moveToNext()) {
//            item.setItemKey(cursor.getInt(0));
//            item.setGb(cursor.getString(1));
//            item.setName(cursor.getString(2));
//            item.setAddress(cursor.getString(3));
//            item.setTel(cursor.getString(4));
//        }
        ArrayList<Item> dataList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Item item = new Item();
            item.setItemKey(cursor.getInt(0));
            item.setGb(cursor.getString(1));
            item.setName(cursor.getString(2));
            item.setAddress(cursor.getString(3));
            item.setTel(cursor.getString(4));
            dataList.add(item);
        }
        return dataList;
//        return item;
    }

    public ArrayList<Item> selectAll(int start, int end) {
        db = getReadableDatabase();
        String selectAll = "SELECT ITEM_KEY, gb, name, address, tel FROM Medi LIMIT "+end+" OFFSET "+start;
        Cursor cursor = db.rawQuery(selectAll, null);

        ArrayList<Item> dataList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Item item = new Item();
            item.setItemKey(cursor.getInt(0));
            item.setGb(cursor.getString(1));
            item.setName(cursor.getString(2));
            item.setAddress(cursor.getString(3));
            item.setTel(cursor.getString(4));
            dataList.add(item);
        }

        return dataList;
    }


    public void insert2(int gb2, String name2, String address2, String tel2) {
        db = getWritableDatabase();
        String insert = "INSERT INTO Medical (gb2, name2, address2, tel2) VALUES ("+gb2+", '"+name2+"', '"+address2+"', '"+tel2+"');";
        db.execSQL(insert);
    }

    public void update2(int itemKey2, int gb2) {
        db = getWritableDatabase();
        String update = "UPDATE Medical SET GB_2 = "+gb2+" WHERE ITEM_KEY = "+itemKey2;
        db.execSQL(update);
    }

    public void delete2(int itemKey2) {
        db = getWritableDatabase();
        String delete = "DELETE FROM Medical";
        db.execSQL(delete);
    }
    public ArrayList<Item2> select2(String addr) {
        db = getReadableDatabase();
        String select = "SELECT ITEM_KEY, gb2, name2, address2, tel2 FROM Medical WHERE address2 like '%"+addr+"'";
        Cursor cursor2 = db.rawQuery(select, null);

/*
        Item item = new Item();
        while (cursor.moveToNext()) {
            item.setItemKey(cursor.getInt(0));
            item.setItem1(cursor.getString(1));
            item.setItem2(cursor.getInt(2));
            item.setItem3(cursor.getString(3));
            item.setItem4(cursor.getString(4));
        }

        return item;
    }
*/

        ArrayList<Item2> dataList = new ArrayList<>();
        while (cursor2.moveToNext()) {
            Item2 item2 = new Item2();
            item2.setItemKey(cursor2.getInt(0));
            item2.setGb(cursor2.getInt(1));
            item2.setName(cursor2.getString(2));
            item2.setAddress(cursor2.getString(3));
            item2.setTel(cursor2.getString(4));
            dataList.add(item2);
        }
        return dataList;
//        return item;
    }

    public ArrayList<Item2> selectAll2(int start, int end) {
        db = getReadableDatabase();
        String selectAll = "SELECT ITEM_KEY, gb2, name2, address2, tel2 FROM Medical LIMIT "+end+" OFFSET "+start;
        Cursor cursor2 = db.rawQuery(selectAll, null);

        ArrayList<Item2> dataList = new ArrayList<>();
        while (cursor2.moveToNext()) {
            Item2 item2 = new Item2();
            item2.setItemKey(cursor2.getInt(0));
            item2.setGb(cursor2.getInt(1));
            item2.setName(cursor2.getString(2));
            item2.setAddress(cursor2.getString(3));
            item2.setTel(cursor2.getString(4));
            dataList.add(item2);
        }

        return dataList;
    }
}
