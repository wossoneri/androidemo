package demo.view.login;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android_database_demo.R;

import demo.database.MyDbHelper;
import demo.view.regist.RegistActivity;

public class LoginActivity extends Activity implements OnClickListener {

	private MyDbHelper mDbHelper;
	private Cursor mCursor;
	
	private head_portrait_view mPortrait;
	private Button mBtnLogin;
	private Button mBtnRegist;
	private TextView mTvFindPwd;
	private EditText mEtAccount;
	private EditText mEtPwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		getActionBar().hide();

		mPortrait = (head_portrait_view) findViewById(R.id.login_my_portrait);
		mEtAccount = (EditText) findViewById(R.id.login_et_account);
		mEtPwd = (EditText) findViewById(R.id.login_et_pwd);

		findViewById(R.id.login_btn_login).setOnClickListener(this);
		findViewById(R.id.login_btn_regist).setOnClickListener(this);
		findViewById(R.id.login_tv_forget_pwd).setOnClickListener(this);
		
		mDbHelper = new MyDbHelper(this);
		mDbHelper.open();
		

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login_btn_login:

			break;

		case R.id.login_btn_regist:
			Intent i = new Intent(this, RegistActivity.class);
			startActivity(i);
			break;

		case R.id.login_tv_forget_pwd:

			break;

		default:

			break;

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
