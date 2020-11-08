package com.example.custompullmenu.business;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 登录历史增删改查
 */

public class AccountDao {
    public final static String TABLE_NAME = "account";
    private MyHelper helper;
    private String phone;

    public AccountDao(Context context){
        helper=new MyHelper(context);
    }

    public void insert(HistoryInfo info){
        SQLiteDatabase db=helper.getWritableDatabase();
        //根据手机号判断去重
        String[] colum = {"phone"};
        String where = "phone" + "= ?";
        String[] whereValue = {info.getPhone()};
        Cursor cursor = db.query(TABLE_NAME, colum, where, whereValue, null, null, null);
        while (cursor.moveToNext()){
            phone = cursor.getString(cursor.getColumnIndex("phone"));
        }
        cursor.close();
        ContentValues values=new ContentValues();
        values.put("phone",info.getPhone());
        values.put("name",info.getName());
        values.put("time",info.getTime());
        if(!TextUtils.isEmpty(phone)){
            db.update(TABLE_NAME,values,"phone" + "=?",new String[]{phone});
        }else {
            db.insert(TABLE_NAME,null,values);
        }
        db.close();
    }
    public int delete(String phone){
        SQLiteDatabase db=helper.getWritableDatabase();
        int count=db.delete(TABLE_NAME,"phone=?",new String[]{phone +""});
        db.close();
        return count;
    }
    public List<HistoryInfo> queryAll(){
        SQLiteDatabase db=helper.getWritableDatabase();
        Cursor cursor=db.query(TABLE_NAME,null,null,null,null,null,null);
        List<HistoryInfo> list=new ArrayList();
            while (cursor.moveToNext()) {
                HistoryInfo historyInfo = new HistoryInfo();
                historyInfo.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                historyInfo.setName(cursor.getString(cursor.getColumnIndex("name")));
                historyInfo.setTime(cursor.getLong(cursor.getColumnIndex("time")));
                list.add(historyInfo);
            }
            db.close();
            cursor.close();
            return list;
    }
}
