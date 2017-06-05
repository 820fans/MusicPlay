package com.mp.music.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 宇 on 2015/7/16.
 */
public class SQLHelper extends SQLiteOpenHelper {
	
    // 构造函数：在此建立数据库，仅第一次打开程序建立（系统自动判断）
    public SQLHelper(Context context) {
        super(context, "music.db", null, 1);// music.db 为数据库的“文件名”
    }

    // 建表的方法
    public void onCreate(SQLiteDatabase db) {
        // 添加页面 activeUser
        db.execSQL("CREATE TABLE activeUser("
                + "id integer primary key autoincrement,"
                + "user_id STRING"
                + ");");

        db.execSQL("CREATE TABLE user("
                + "id integer primary key autoincrement,"
                + "account STRING,"
                + "introduce STRING,"
                + "avatar STRING"
                + ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS filedownlog (id integer primary key autoincrement, "+
                "downpath STRING, threadid INTEGER, downlength INTEGER)");
    }

    // 必须有的函数 - 升级数据库的函数
    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {

        db.execSQL("DROP TABLE IF EXISTS filedownlog");
    }
    // 数据库建表的时候要注意：
    // 1. 声明不要写错
    // 2. 每行末尾的","不要多了或者少了
}
