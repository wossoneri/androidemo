package com.example.android_notepad_demo;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class NoteEdit extends Activity {

	private EditText mTitleText;
	private EditText mBodyText;
	private Button mConfirmButton;
	private Long mRowId;

	private NotesDbAdapter mDbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mDbHelper = new NotesDbAdapter(this);
		mDbHelper.open();

		setContentView(R.layout.note_edit);
		setTitle(R.string.edit_note);

		mTitleText = (EditText) findViewById(R.id.title);
		mBodyText = (EditText) findViewById(R.id.body);
		mConfirmButton = (Button) findViewById(R.id.confirm);

		// mRowId = null;
		// Bundle extras = getIntent().getExtras();
		// if (extras != null) {
		// String title = extras.getString(NotesDbAdapter.KEY_TITLE);
		// String body = extras.getString(NotesDbAdapter.KEY_BODY);
		// mRowId = extras.getLong(NotesDbAdapter.KEY_ROWID);

		// if (title != null) {
		// mTitleText.setText(title);
		// }
		//
		// if (body != null) {
		// mBodyText.setText(body);
		// }
		// }
		mRowId = (savedInstanceState == null) ? null : (Long) savedInstanceState
				.getSerializable(NotesDbAdapter.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(NotesDbAdapter.KEY_ROWID) : null;
		}

		populateFields();

		mConfirmButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Bundle bundle = new Bundle();
				//
				// bundle.putString(NotesDbAdapter.KEY_TITLE,
				// mTitleText.getText().toString());
				// bundle.putString(NotesDbAdapter.KEY_BODY,
				// mBodyText.getText().toString());
				// if (mRowId != null) {
				// bundle.putLong(NotesDbAdapter.KEY_ROWID, mRowId);
				// }
				//
				// Intent mIntent = new Intent();
				// mIntent.putExtras(bundle);
				// setResult(RESULT_OK, mIntent);
				setResult(RESULT_OK);
				finish();
			}
		});

	}

	private void populateFields() {
		if (mRowId != null) {
			Cursor note = mDbHelper.fetchNote(mRowId);
			startManagingCursor(note);
			mTitleText
					.setText(note.getString(note.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
			mBodyText.setText(note.getString(note.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putSerializable(NotesDbAdapter.KEY_ROWID, mRowId);
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}

	@Override
	protected void onResume() {
		super.onResume();
		populateFields();
	}

	private void saveState() {
		String title = mTitleText.getText().toString();
		String body = mBodyText.getText().toString();

		if (mRowId == null) {
			long id = mDbHelper.createNotes(title, body);
			if (id > 0) {
				mRowId = id;
			}
		} else {
			mDbHelper.updateNote(mRowId, title, body);
		}
	}
}
