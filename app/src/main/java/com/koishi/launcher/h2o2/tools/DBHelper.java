package com.koishi.launcher.h2o2.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.koishi.launcher.h2o2.tools.DBUser.User;

/**
 * 操作数据库辅助类
 * 
 * @author 2012-8-12
 */
public class DBHelper {
	public static final int DB_VERSION = 1;
	public static final String DB_NAME = "example.db";
	public static final String USER_TABLE_NAME = "user_table";
	public static final String[] USER_COLS = { User.USERNAME, User.PASSWORD,
			User.ISSAVED };

	private SQLiteDatabase db;
	private DBOpenHelper dbOpenHelper;

	public DBHelper(Context context) {
		this.dbOpenHelper = new DBOpenHelper(context);
		establishDb();
	}

	/**
	 * 打开数据库
	 */
	private void establishDb() {
		if (this.db == null) {
			this.db = this.dbOpenHelper.getWritableDatabase();
		}
	}

	/**
	 * 插入一条记录
	 * 
	 * @param map
	 *            要插入的记录
	 * @param tableName
	 *            表名
	 * @return 插入记录的id -1表示插入不成功
	 */
	public long insertOrUpdate(String userName, String password, int isSaved) {
		boolean isUpdate = false;
		String[] usernames = queryAllUserName();
		for (int i = 0; i < usernames.length; i++) {
			if (userName.equals(usernames[i])) {
				isUpdate = true;
				break;
			}
		}
		long id = -1;
		if (isUpdate) {
			id = update(userName, password, isSaved);
		} else {
			if (db != null) {
				ContentValues values = new ContentValues();
				values.put(User.USERNAME, userName);
				values.put(User.PASSWORD, password);
				values.put(User.ISSAVED, isSaved);
				id = db.insert(USER_TABLE_NAME, null, values);
			}
		}
		return id;
	}

	/**
	 * 删除一条记录
	 * 
	 * @param userName
	 *            用户名
	 * @param tableName
	 *            表名
	 * @return 删除记录的id -1表示删除不成功
	 */
	public long delete(String userName) {
		long id = db.delete(USER_TABLE_NAME, User.USERNAME + " = '" + userName
				+ "'", null);
		return id;
	}

	/**
	 * 更新一条记录
	 * 
	 * @param
	 * 
	 * @param tableName
	 *            表名
	 * @return 更新过后记录的id -1表示更新不成功
	 */
	public long update(String username, String password, int isSaved) {
		ContentValues values = new ContentValues();
		values.put(User.USERNAME, username);
		values.put(User.PASSWORD, password);
		values.put(User.ISSAVED, isSaved);
		long id = db.update(USER_TABLE_NAME, values, User.USERNAME + " = '"
				+ username + "'", null);
		return id;
	}

	/**
	 * 根据用户名查询密码
	 * 
	 * @param username
	 *            用户名
	 * @param tableName
	 *            表名
	 * @return Hashmap 查询的记录
	 */
	public String queryPasswordByName(String username) {
		String sql = "select * from " + USER_TABLE_NAME + " where "
				+ User.USERNAME + " = '" + username + "'";
		Cursor cursor = db.rawQuery(sql, null);
		String password = "";
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			password = cursor.getString(cursor.getColumnIndex(User.PASSWORD));
		}

		return password;
	}

	/**
	 * 记住密码选项框是否被选中
	 * 
	 * @param username
	 * @return
	 */
	public int queryIsSavedByName(String username) {
		String sql = "select * from " + USER_TABLE_NAME + " where "
				+ User.USERNAME + " = '" + username + "'";
		Cursor cursor = db.rawQuery(sql, null);
		int isSaved = 0;
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			isSaved = cursor.getInt(cursor.getColumnIndex(User.ISSAVED));
		}
		return isSaved;
	}

	/**
	 * 查询所有用户名
	 * 
	 * @param tableName
	 *            表名
	 * @return 所有记录的list集合
	 */
	public String[] queryAllUserName() {
		if (db != null) {
			Cursor cursor = db.query(USER_TABLE_NAME, null, null, null, null,
					null, null);
			int count = cursor.getCount();
			String[] userNames = new String[count];
			if (count > 0) {
				cursor.moveToFirst();
				for (int i = 0; i < count; i++) {
					userNames[i] = cursor.getString(cursor
							.getColumnIndex(User.USERNAME));
					cursor.moveToNext();
				}
			}
			return userNames;
		} else {
			return new String[0];
		}

	}

	/**
	 * 关闭数据库
	 */
	public void cleanup() {
		if (this.db != null) {
			this.db.close();
			this.db = null;
		}
	}

	/**
	 * 数据库辅助类
	 */
	private static class DBOpenHelper extends SQLiteOpenHelper {

		public DBOpenHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("create table " + USER_TABLE_NAME + " (" + User._ID
					+ " integer primary key," + User.USERNAME + " text, "
					+ User.PASSWORD + " text, " + User.ISSAVED + " INTEGER)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
			onCreate(db);
		}

	}

}
