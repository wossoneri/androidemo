package demo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDbHelper {
	private static final String TAG = "MyDB";
	private static final String DATABASE_NAME = "MyLoginDB.db";
	private static final int DATABASE_VERSION = 2;

	private final Context mContext;
	private DbHelper mDbHelper;
	private SQLiteDatabase mDb;

	public MyDbHelper(Context mc) {
		this.mContext = mc;
	}

	public MyDbHelper open() throws SQLException {
		mDbHelper = new DbHelper(mContext);
		mDb = mDbHelper.getWritableDatabase();

		return this;
	}

	public void close() {
		mDb.close();
		mDbHelper.close();
	}

	// 创建一个条目 插入到数据库
	public long createEntry(String account, String pwd, String ques, String ans, String path, long time) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(MyDbFields.COLUMN_ACCOUNT, account);
		initialValues.put(MyDbFields.COLUMN_PASSWORD, pwd);
		initialValues.put(MyDbFields.COLUMN_QUESTION, ques);
		initialValues.put(MyDbFields.COLUMN_ANSWER, ans);
		initialValues.put(MyDbFields.COLUMN_PATH, path);
		initialValues.put(MyDbFields.COLUMN_REGIST_TIME, time);

		return mDb.insert(MyDbFields.TABLE_NAME, null, initialValues);
	}

	// 通过account删除条目
	public boolean deleteEntry(String account) {
		return mDb.delete(MyDbFields.TABLE_NAME, MyDbFields.COLUMN_ACCOUNT + " = \"" + account + "\"", null) > 0;
	}

	// 改头像
	public boolean updateHeadPortrait(String account, String path) { //只能改密码和头像 
		ContentValues args = new ContentValues();
		args.put(MyDbFields.COLUMN_PATH, path);
		return mDb.update(MyDbFields.TABLE_NAME, args, MyDbFields.COLUMN_ACCOUNT + " = \"" + account + "\"", null) > 0;
	}
	
	// 改密码
	public boolean updatePwd(String account, String pwd){
		ContentValues args = new ContentValues();
		args.put(MyDbFields.COLUMN_PASSWORD, pwd);
		return mDb.update(MyDbFields.TABLE_NAME, args, MyDbFields.COLUMN_ACCOUNT + " = \"" + account + "\"", null) > 0;
	}
	
	// 查找所有条目
	public Cursor fetchAllEntries(){
		return mDb.query(MyDbFields.TABLE_NAME,//查找表
				new String[]{MyDbFields._ID, 
							 MyDbFields.COLUMN_ACCOUNT,
							 MyDbFields.COLUMN_PASSWORD,
							 MyDbFields.COLUMN_QUESTION,
							 MyDbFields.COLUMN_ANSWER,
							 MyDbFields.COLUMN_PATH,
							 MyDbFields.COLUMN_REGIST_TIME},//要获取的列，我取所有列
				null,null, null, null, null);	//全空为默认，取所有结果，不分组，不排序
	}
	
	// 查找某id
	public Cursor fetchEntry(String account) throws SQLException{
		Cursor mCursor = mDb.query(true, // 只取一个结果
				MyDbFields.TABLE_NAME,
				new String[]{MyDbFields._ID, 
							 MyDbFields.COLUMN_ACCOUNT,
							 MyDbFields.COLUMN_PASSWORD,
							 MyDbFields.COLUMN_QUESTION,
							 MyDbFields.COLUMN_ANSWER,
							 MyDbFields.COLUMN_PATH,
							 MyDbFields.COLUMN_REGIST_TIME},
				MyDbFields.COLUMN_ACCOUNT + " = \"" + account + "\"",
				null, null, null, null, null, null);
		
		if(mCursor != null){
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	
	
	/**
	 * 创建，升级数据库
	 */
	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) { // create DB
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + MyDbFields.TABLE_NAME + " (" 
					+ MyDbFields._ID + " INTEGER PRIMARY KEY," 
					+ MyDbFields.COLUMN_ACCOUNT + " TEXT,"
					+ MyDbFields.COLUMN_PASSWORD + " TEXT,"
					+ MyDbFields.COLUMN_QUESTION + " TEXT,"
					+ MyDbFields.COLUMN_ANSWER + " TEXT,"
					+ MyDbFields.COLUMN_PATH + " TEXT,"
					+ MyDbFields.COLUMN_REGIST_TIME + " INTEGER"
					+ ");");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
					+ ", which will destroy all old data");

			db.execSQL("DROP TABLE IF EXISTS " + MyDbFields.TABLE_NAME);

			onCreate(db);
		}

	}

}
