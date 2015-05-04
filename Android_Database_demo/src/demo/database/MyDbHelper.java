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
	private static final String DATABASE_NAME = "MyTestDB.db";
	private static final int DATABASE_VERSION = 2;

	private final Context mContext;
	private DbOpenHelper mDbHelper;
	private SQLiteDatabase mDb;

	public MyDbHelper(Context mc) {
		this.mContext = mc;
	}

	public MyDbHelper open() throws SQLException {
		mDbHelper = new DbOpenHelper(mContext);
		mDb = mDbHelper.getWritableDatabase();// 拿到外面去 每次操作数据库前调用这句

		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public long createEntry(String account, String pwd) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(MyDbFields.COLUMN_ACCOUNT, account);
		initialValues.put(MyDbFields.COLUMN_PASSWORD, pwd);

		return mDb.insert(MyDbFields.TABLE_NAME, null, initialValues);
	}

	public boolean deleteEntry(long id) {
		return mDb.delete(MyDbFields.TABLE_NAME, MyDbFields._ID + "=" + id, null) > 0;
	}

	public boolean updateTable(long id, String account, String pwd) {
		ContentValues args = new ContentValues();
		args.put(MyDbFields.COLUMN_ACCOUNT, account);
		args.put(MyDbFields.COLUMN_PASSWORD, pwd);

		return mDb.update(MyDbFields.TABLE_NAME, args, MyDbFields._ID + "=" + id, null) > 0;
	}
	
	public Cursor fetchAllEntries(){
		return mDb.query(MyDbFields.TABLE_NAME,//
				new String[]{MyDbFields._ID, MyDbFields.COLUMN_ACCOUNT, MyDbFields.COLUMN_PASSWORD},
				null,null, null, null, null);
	}
	
	public Cursor fetchEntry(long id) throws SQLException{
		Cursor mCursor = mDb.query(true,
				MyDbFields.TABLE_NAME, new String[]{MyDbFields._ID, MyDbFields.COLUMN_ACCOUNT,MyDbFields.COLUMN_PASSWORD},
				MyDbFields._ID+"=" + id,
				null, null, null, null, null, null);
		
		if(mCursor != null){
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	
	
	
	private static class DbOpenHelper extends SQLiteOpenHelper {

		public DbOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + MyDbFields.TABLE_NAME + " (" 
					+ MyDbFields._ID + " INTEGER PRIMARY KEY," 
					+ MyDbFields.COLUMN_ACCOUNT + " TEXT,"
					+ MyDbFields.COLUMN_PASSWORD + " TEXT," 
					+ MyDbFields.COLUMN_REGIST_TIME
					+ " INTEGER" + ");");
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
