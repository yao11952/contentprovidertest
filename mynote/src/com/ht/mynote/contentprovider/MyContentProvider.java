package com.ht.mynote.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.ht.mynote.dao.NotesSQLiteOpenHelper;

/**
 * Created by IntelliJ IDEA
 * Project: com.ht.mynote.contentprovider
 * Author: 安诺爱成长
 * Email: 1399487511@qq.com
 * Date: 2015/5/7
 */
public class MyContentProvider extends ContentProvider {
    private static UriMatcher matcher;
    private static final String AUTHORITY = "com.ht.mynote.contentprovider.MyContentProvider";

    static {
        matcher = new UriMatcher(-1);
        matcher.addURI(AUTHORITY, "mynotes", 1);
    }

    private NotesSQLiteOpenHelper helper;


    @Override
    public boolean onCreate() {
        helper = new NotesSQLiteOpenHelper(getContext());
        return true;
    }

    //得到一个所有的对象的集合
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int code = matcher.match(uri);
        Cursor ret = null;
        switch (code) {
            case 1://mynotes
                ret = db.query("mynotes",
                        strings,
                        s,
                        strings1,
                        null,
                        null,
                        s1
                );
                break;
            case -1:
                break;
        }
        return ret;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    //插入一个对象
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Uri ret = null;
        SQLiteDatabase db = helper.getWritableDatabase();
        int code = matcher.match(uri);
        switch (code) {
            case 1:
                long rowId = db.insert("mynotes",
                        null,
                        contentValues
                );
                if (rowId > -1) {
                    uri.getScheme();
                    uri.getAuthority();
                    uri.getPath();
                    ret = Uri.withAppendedPath(uri, Long.toString(rowId));
                }
                break;
            case -1:
                break;
        }
        return ret;
    }

    //删除一个对象
    @Override
    public int delete(Uri uri, String s, String[] strings) {
        int ret = 0;
        SQLiteDatabase db = helper.getWritableDatabase();
        int code = matcher.match(uri);
        switch (code) {
            case 1:
                ret = db.delete("mynotes",
                        s,
                        strings);
                db.close();
                break;
            case -1:
                break;
        }
        return ret;
    }


    //更新一个对象
    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        int ret = 0;
        int code = matcher.match(uri);
        SQLiteDatabase db = helper.getWritableDatabase();
        switch (code) {
            case 1:
                ret = db.update("mynotes",
                        contentValues,
                        s,
                        strings
                        );
                db.close();
                break;
            case -1:
                break;
        }
        return ret;
    }
}
