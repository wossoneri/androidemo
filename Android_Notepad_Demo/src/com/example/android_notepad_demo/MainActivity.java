package com.example.android_notepad_demo;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends ListActivity {

	public static final int INSERT_ID = Menu.FIRST;
	private int mNoteNumber = 1; // used to create numbered note titles
	private NotesDbAdapter mDbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notepad_list);
		mDbHelper = new NotesDbAdapter(this);
		mDbHelper.open();
		fillData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, INSERT_ID, 0, R.string.menu_insert);
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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

	private void createNote() {
		String noteName = "Note " + mNoteNumber++;
		mDbHelper.createNotes(noteName, "");
		fillData();
	}

	private void fillData() {
		Cursor c = mDbHelper.fetchAllNotes();
		startManagingCursor(c);

		String[] from = new String[] { NotesDbAdapter.KEY_TITLE };
		int[] to = new int[]{R.id.text1};
		
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.notes_row, c, from, to);
		setListAdapter(notes);

	}
}
