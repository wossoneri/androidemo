package demo.DataBaseDemo.DBHelper;

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

	public static final String TABLE_NAME = "myLogin";

	/**
	 * user name to login <b> Type: text </b>
	 */
	public static final String COLUMN_ACCOUNT = "account";

	/**
	 * password for account <b> Type: text </b>
	 */
	public static final String COLUMN_PASSWORD = "pwd";

	/**
	 * question for account <b> Type: text </b>
	 */
	public static final String COLUMN_QUESTION = "question";

	/**
	 * answer to the question <b> Type: text </b>
	 */
	public static final String COLUMN_ANSWER = "answer";

	/**
	 * head portrait image path <b> Type: text </b>
	 */
	public static final String COLUMN_PATH = "path";

	/**
	 * regist time <b> Type: INTEGER (long from System.currentTimeMillis()) </b>
	 */
	public static final String COLUMN_REGIST_TIME = "created";

}
