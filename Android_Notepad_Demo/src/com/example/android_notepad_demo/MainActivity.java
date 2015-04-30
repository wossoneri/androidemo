package com.example.android_notepad_demo;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends ListActivity {

	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;

	private static final int INSERT_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST + 1;
//	private int mNoteNumber = 1; // used to create numbered note titles
	private NotesDbAdapter mDbHelper;
	private Cursor mNotesCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notepad_list);
		mDbHelper = new NotesDbAdapter(this);
		mDbHelper.open();
		fillData();

		// In order for each list item in the ListView to register for the
		// context menu, we call registerForContextMenu() and pass it our
		// ListView.
		registerForContextMenu(getListView());
	}

	private void createNote() {
		Intent i = new Intent(this, NoteEdit.class);
		startActivityForResult(i, ACTIVITY_CREATE);
	}

	private void fillData() {
		// Get all of the rows from the database and create the item list
		mNotesCursor = mDbHelper.fetchAllNotes();
		startManagingCursor(mNotesCursor);

		// Create an array to specify the fields we want to display in the list
		// (only TITLE)
		String[] from = new String[] { NotesDbAdapter.KEY_TITLE };

		// and an array of the fields we want to bind those fields to (in this
		// case just text1)
		int[] to = new int[] { R.id.text1 };

		// Now create a simple cursor adapter and set it to display
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.notes_row, mNotesCursor,
				from, to);
		setListAdapter(notes);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		// l:点击的listview对象
		// v:点击的listview的item对象
		// position:item的位置
		// id:item的id

		super.onListItemClick(l, v, position, id);

//		Cursor c = mNotesCursor;
//		c.moveToPosition(position);

		Intent i = new Intent(this, NoteEdit.class);
		i.putExtra(NotesDbAdapter.KEY_ROWID, id);
//		i.putExtra(NotesDbAdapter.KEY_TITLE,
//				c.getString(c.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
//		i.putExtra(NotesDbAdapter.KEY_BODY,
//				c.getString(c.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));

		startActivityForResult(i, ACTIVITY_EDIT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// requestCode:我们在startActivityForResult设置的第二个指定参数
		// resultCode: 调用结果，正常是0
		// 之前putExtra放入的数据
		super.onActivityResult(requestCode, resultCode, data);
		fillData();
//		Bundle extras = data.getExtras();
//
//		switch (requestCode) {
//		case ACTIVITY_CREATE:
//			String title = extras.getString(NotesDbAdapter.KEY_TITLE);
//			String body = extras.getString(NotesDbAdapter.KEY_BODY);
//			mDbHelper.createNotes(title, body);
//			fillData();
//			break;
//
//		case ACTIVITY_EDIT:
//			Long mRowId = extras.getLong(NotesDbAdapter.KEY_ROWID);
//			if (mRowId != null) {
//				String editTitle = extras.getString(NotesDbAdapter.KEY_TITLE);
//				String editBody = extras.getString(NotesDbAdapter.KEY_BODY);
//				mDbHelper.updateNote(mRowId, editTitle, editBody);
//			}
//			fillData();
//			break;
//
//		default:
//			break;
//		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		// add a menu item to delete a note
		menu.add(0, DELETE_ID, 0, R.string.menu_delete);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			mDbHelper.deleteNote(info.id);
			fillData();
			return true;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) { // 填充menu
		// Inflate the menu; this adds items to the action bar if it is present.
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, INSERT_ID, 0, R.string.menu_insert);
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) { // 处理menu事件
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case INSERT_ID:
			createNote();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
