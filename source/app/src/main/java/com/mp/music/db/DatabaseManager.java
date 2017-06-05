package com.mp.music.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {

    private SQLHelper dbHelper;
    public static DatabaseManager instance = null;
    private SQLiteDatabase sqLiteDatabase;

    private DatabaseManager(Context context) {
        sqLiteDatabase = (new SQLHelper(context)).getWritableDatabase();//得到可写的数据库
    }

    /**
     * 获取本类对象实例
     */
    public static final DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context);
        }
        if (instance.sqLiteDatabase.isOpen() == false) {
            instance.sqLiteDatabase =
                    instance.
                            dbHelper.
                            getWritableDatabase();
        }
        return instance;
    }

    public void clearActiveUser(){
        if (sqLiteDatabase.isOpen())
        sqLiteDatabase.delete("activeUser","id=1",null);
    }

    public void insertActiveUser(int user_id){

        ContentValues values = new ContentValues();
        values.put("user_id", user_id);
        if (sqLiteDatabase.isOpen())
            sqLiteDatabase.insert("activeUser", null, values);
//        sqLiteDatabase.close();
    }

    public String getActiveUser(){
        String user_id = "";
        String[] column=new String[1];
        column[0]="user_id";
        if (sqLiteDatabase.isOpen()) {
            Cursor cursor = sqLiteDatabase.query("activeUser", column, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    user_id=cursor.getString(cursor.getColumnIndex("user_id"));
                } while (cursor.moveToNext());
            }
            cursor.close();
//            sqLiteDatabase.close();
            return user_id;
        }
        return user_id;
    }

    public void close() {
        if (sqLiteDatabase.isOpen())
            sqLiteDatabase.close();
        if (dbHelper != null)
            dbHelper.close();
        if (instance != null)
            instance = null;
    }

    public int delete_thing(String table, String whereClause, String[] whereArgs) {
        int result = 0;
        if (sqLiteDatabase.isOpen()) {
            result = sqLiteDatabase.delete(table, whereClause, whereArgs);
        }
        return result;
    }
    //TODO Just to remember
    //TODO 找出所有的 "==" 改成 equals
    //TODO 没把握，不要写 .get.add， 用add最好
    //TODO 给二维数组 “赋大值”的时候，那个临时变量 要 new 啊！！！
    //TODO 是否处理过某件事了：“标记”要做好啊！！


    //todo 当地点为空时，并未合理归档为一种条件，当没有一件事情的时候，还没有设置提醒 和 界面跳转
    public void insertBusiness(ContentValues cv) {
        sqLiteDatabase.insert("business", null, cv);
    }

    public void deleteBusiness( int id){
        sqLiteDatabase.delete("business", "_id=?", new String[]{String.valueOf(id)});

    }

    /**
     * 获取每条线程已经下载的文件长度
     *
     * @param path
     * @return
     */
    public Map<Integer, Integer> getData(String path) {
        if (sqLiteDatabase.isOpen()) {
            Cursor cursor = sqLiteDatabase
                    .rawQuery(
                            "select threadid, downlength from filedownlog where downpath=?",
                            new String[] { path });
            Map<Integer, Integer> data = new HashMap<Integer, Integer>();
            while (cursor.moveToNext()) {
                data.put(cursor.getInt(0), cursor.getInt(1));
            }
            cursor.close();
//            sqLiteDatabase.close();
            return data;
        }
        return null;
    }

    /**
     * 保存每条线程已经下载的文件长度
     *
     * @param path
     * @param map
     */
    public void save(String path, Map<Integer, Integer> map) {// int threadid,
        // int position
        if (sqLiteDatabase.isOpen()) {
//            try {
                for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                    sqLiteDatabase.execSQL(
                            "insert into filedownlog(downpath, threadid, downlength) values(?,?,?)",
                            new Object[]{path, entry.getKey(), entry.getValue()});
                }
//                sqLiteDatabase.setTransactionSuccessful();
//            } finally {
//                sqLiteDatabase.endTransaction();
//            }
//        db.close();
        }
        return;
    }

    /**
     * 实时更新每条线程已经下载的文件长度
     *
     * @param path
     */
    public void update(String path, int threadId, int pos) {
//        SQLiteDatabase db = openHelper.getWritableDatabase();
        if (sqLiteDatabase.isOpen())
            sqLiteDatabase.execSQL(
                "update filedownlog set downlength=? where downpath=? and threadid=?",
                new Object[] { pos, path, threadId });
//        sqLiteDatabase.close();
    }

    /**
     * 当文件下载完成后，删除对应的下载记录
     *
     * @param path
     */
    public void delete(String path) {
//        SQLiteDatabase db = openHelper.getWritableDatabase();
        if (sqLiteDatabase.isOpen())
            sqLiteDatabase.execSQL("delete from filedownlog where downpath=?",
                new Object[] { path });
//        db.close();
    }
}
