package demo.database;

import android.provider.BaseColumns;

public final class MyDbFields implements BaseColumns {// BaseColumns defines 2
														// constant:_id and
														// _count, in order to
														// not write it wrong
														// when dealing with
														// database, so do this
														// class

	// public static final class Fields implements BaseColumns{
	//
	// }

	private MyDbFields() {
		// TODO Auto-generated constructor stub
	}

	public static final String TABLE_NAME = "regist";

	/**
	 * user name to login <b> Type: text </b>
	 */
	public static final String COLUMN_ACCOUNT = "account";
	/**
	 * password for account <b> Type: text </b>
	 */
	public static final String COLUMN_PASSWORD = "pwd";
	/**
	 * regist time <b> Type: INTEGER (long from System.currentTimeMillis()) </b>
	 */
	public static final String COLUMN_REGIST_TIME = "registed";

}