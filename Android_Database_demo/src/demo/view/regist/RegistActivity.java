package demo.view.regist;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android_database_demo.R;

import demo.database.MyDbHelper;

public class RegistActivity extends Activity implements OnClickListener {

	// private head_portrait_view mPortrait;
	private EditText[] mEt;
	private String mToast;
	private String path = null;
	// private EditText mEtPwd;
	// private EditText mEtPwdConfirm;
	// private EditText mEtQues;
	// private EditText mEtAns;

	// private Button mBtnSubmit;

	private MyDbHelper mDbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regist);

		getActionBar().hide();

		mToast = getResources().getString(R.string.regist_toast);
		// mPortrait = (head_portrait_view)
		// findViewById(R.id.regist_my_portrait);
		mEt = new EditText[5];
		mEt[0] = (EditText) findViewById(R.id.regist_et_account);
		mEt[1] = (EditText) findViewById(R.id.regist_et_pwd);
		mEt[2] = (EditText) findViewById(R.id.regist_et_pwd_confirm);
		mEt[3] = (EditText) findViewById(R.id.regist_et_question);
		mEt[4] = (EditText) findViewById(R.id.regist_et_answer);

		findViewById(R.id.regist_my_portrait).setOnClickListener(this);
		findViewById(R.id.regist_btn_submit).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.regist_my_portrait:

			break;
		case R.id.regist_btn_submit:	//还需要判断一下输入的 account 必须唯一
			for (int i = 0; i < 5; i++) {
				if (mEt[i].getText().length() == 0) {
					showToast(i);
					return;
				}
			}

			if (!mEt[2].getText().toString().equals(mEt[1].getText().toString())) {
				Toast.makeText(this, "The two password are different", Toast.LENGTH_SHORT).show();
				mEt[1].setText(null);
				mEt[2].setText(null);
				mEt[1].requestFocus();
				return;
			}

			mDbHelper = new MyDbHelper(this);
			mDbHelper.open();
			long id = mDbHelper.createEntry(mEt[0].getText().toString(),
											mEt[1].getText().toString(),
											mEt[3].getText().toString(),
											mEt[4].getText().toString(),
											path,
											System.currentTimeMillis());

			if (-1 == id) {
				Toast.makeText(this, "Regist failed", Toast.LENGTH_SHORT).show();
				return;
			} else {
				Toast.makeText(this, "Regist success", Toast.LENGTH_SHORT).show();
				this.finish();
			}

			break;

		default:
			break;
		}
	}

	private void showToast(int n) {
		String str = "";
		switch (n) {
		case 0:
			str = String.format(mToast, "account");
			break;

		case 1:
			str = String.format(mToast, "password");
			break;

		case 2:
			str = String.format(mToast, "confirm password");
			break;

		case 3:
			str = String.format(mToast, "question");
			break;

		case 4:
			str = String.format(mToast, "answer");
			break;

		default:
			break;
		}
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	// /////////////////////////////////////////////////////////////////////////

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		mDbHelper.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.regist, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
