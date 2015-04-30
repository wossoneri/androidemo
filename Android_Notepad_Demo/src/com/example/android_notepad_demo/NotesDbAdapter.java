package com.example.android_notepad_demo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 * 简单的记事本数据库访问帮助类。定义基本增删改查操作，并可以列出所有笔记条目或指定条目
 * 
 */
public class NotesDbAdapter {

	// 表的3个字段
	public static final String KEY_TITLE = "title";
	public static final String KEY_BODY = "body";
	public static final String KEY_ROWID = "_id";

	// 建表的sql语句
	private static final String DATABASE_CREATE = "create table notes (_id integer primary key autoincrement, title text not null, body text not null);";

	private static final String TAG = "NotesDbAdapter";
	private static final String DATABASE_NAME = "data"; // 数据库名
	private static final String DATABASE_TABLE = "notes"; // 表名
	private static final int DATABASE_VERSION = 2;

	private final Context mContext;
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) { // 在数据库第一次建立的时候调用
			// TODO Auto-generated method stub
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { // 更新，需要做删除表再新建表，也可以加其他操作
			// TODO Auto-generated method stub
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
					+ ", which will destory all old data");
			db.execSQL("DROP TABLE IF EXIST notes");
			onCreate(db);
		}

	}

	/**
	 * Constructor takes the context to allow the database to be opened/created
	 * 构造获取上下文
	 */
	public NotesDbAdapter(Context mContext) {
		this.mContext = mContext;
	}

	/**
	 * Open the notes database. If it cannot be opened, try to create a new
	 * instance of the database. If it cannot be created, throw an exception to
	 * signal the failure 打开notes数据库，如果不能打开，试着建立新数据库实例，如果不能建立，抛出异常
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public NotesDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mContext);
		mDb = mDbHelper.getWritableDatabase();
		// Create and/or open a database that will be used for reading and
		// writing. The first time this is called, the database will be opened
		// and onCreate, onUpgrade and/or onOpen will be called.
		// 新建/打开一个用来读写的数据库。第一次调用，数据库会被打开，onCreate,onUpgrade和/或onOpen方法会被调用
		// Once opened successfully, the database is cached, so you can call
		// this method every time you need to write to the database. (Make sure
		// to call close when you no longer need the database.) Errors such as
		// bad permissions or a full disk may cause this method to fail, but
		// future attempts may succeed if the problem is fixed.
		// 一旦成功打开，数据库就会进入缓存，你就可以在任何要写入数据的时候调用这个方法（要保证在不需要数据库的时候关闭它）
		// 像一些权限出错或者磁盘空间不足会导致方法调用失败，但修复这些问题后就能正常调用
		// Database upgrade may take a long time, you should not call this
		// method from the application main thread, including from
		// ContentProvider.onCreate().
		// 数据库更新可能会花费很长时间，所以不应该在应用的主线程调用它，包括从ContentProvider.onCreate().调用

		return this;

	}

	public void close() {
		mDbHelper.close(); // 关闭打开的数据库，释放连接的相关资源
	}

	/**
	 * Create a new note using the title and body provided. If the note is
	 * successfully created return the new rowId for that note, otherwise return
	 * a -1 to indicate failure.
	 * 
	 * 用传递进来的内容新建一个条目。创建成功返回这个条目的id，失败返回-1
	 * 
	 * @param title
	 *            the title of the note
	 * @param body
	 *            the body of the note
	 * @return rowId or -1 if failed
	 */
	public long createNotes(String title, String body) {
		ContentValues initialValues = new ContentValues();
		// This class is used to store a set of values that the ContentResolver
		// can process.
		initialValues.put(KEY_TITLE, title);
		initialValues.put(KEY_BODY, body);

		return mDb.insert(DATABASE_TABLE, null, initialValues);

	}

	/**
	 * Delete the note with the given rowId
	 * 
	 * @param rowId
	 *            id of note to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteNote(long rowId) {
		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
		// delete() returns the number of rows affected if a whereClause is
		// passed in, 0 otherwise.
		// To remove all rows and get a count pass "1" as the whereClause.
	}

	/**
	 * Return a Cursor over the list of all notes in the database
	 * 
	 * @return Cursor over all notes
	 */
	public Cursor fetchAllNotes() {
		return mDb.query(DATABASE_TABLE, // 要查询的表
				new String[] { KEY_ROWID, KEY_TITLE, KEY_BODY },// 需要返回的列
				null, null, null, null, null);// 全null表示要返回所有数据
		// 返回一个指针而不是数据集。这使得android更有效的使用资源，而不是直接把大量数据放在内存中让指针读取/释放数据。
		// 对于有很多行数据的表来说这样能明显提升效率
	}

	/**
	 * Return a Cursor positioned at the note that matches the given rowId
	 * 
	 * @param rowId
	 *            id of note to retrieve
	 * @return Cursor positioned to matching note, if found
	 * @throws SQLException
	 *             if note could not be found/retrieved
	 */
	public Cursor fetchNote(long rowId) throws SQLException {
		Cursor mCursor = mDb.query(true, // true表示提取一行指定的内容
				DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE, KEY_BODY }, KEY_ROWID + "="
						+ rowId,// 指定查询条件
				null, null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	/**
	 * Update the note using the details provided. The note to be updated is
	 * specified using the rowId, and it is altered to use the title and body
	 * values passed in
	 * 
	 * @param rowId
	 *            id of note to update
	 * @param title
	 *            value to set note title to
	 * @param body
	 *            value to set note body to
	 * @return true if the note was successfully updated, false otherwise
	 */
	public boolean updateNote(long rowId, String title, String body) {
		ContentValues args = new ContentValues();
		args.put(KEY_TITLE, title);
		args.put(KEY_BODY, body);

		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}

}
