package demo.DataBaseDemo.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android_database_demo.R;

import demo.DataBaseDemo.DBHelper.MyDbHelper;
import demo.DataBaseDemo.View.head_portrait_menu_view.CloseDlgInterface;

public class RegistActivity extends Activity implements OnClickListener, CloseDlgInterface {

	// private head_portrait_view mPortrait;
	private EditText[] mEt;
	private String mToast;
	private String path = null;
	// private EditText mEtPwd;
	// private EditText mEtPwdConfirm;
	// private EditText mEtQues;
	// private EditText mEtAns;

	// private Button mBtnSubmit;
	private head_portrait_menu_view mMenuView;
	private MyDbHelper mDbHelper;
	private AlertDialog mMenuDlg;

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

		mMenuView = new head_portrait_menu_view(this);
		mMenuView.setCloseInterface(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.regist_my_portrait:
			if (mMenuDlg == null)
				createMenuDialog();
			mMenuDlg.show();

			// 获得屏幕尺寸
			DisplayMetrics dm = new DisplayMetrics();
			this.getWindowManager().getDefaultDisplay().getMetrics(dm);
			// 设置宽度
			WindowManager.LayoutParams lp = mMenuDlg.getWindow().getAttributes();
			lp.width = dm.widthPixels;
			mMenuDlg.getWindow().setAttributes(lp);

			break;
		case R.id.regist_btn_submit: // 还需要判断一下输入的 account 必须唯一
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
			long id = mDbHelper.createEntry(mEt[0].getText().toString(), mEt[1].getText()
					.toString(), mEt[3].getText().toString(), mEt[4].getText().toString(), path,
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

	private void createMenuDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
		builder.setCancelable(true);
		builder.setView(mMenuView.getViewGroup());
		mMenuDlg = builder.create();
		Window w = mMenuDlg.getWindow();
		w.setGravity(Gravity.BOTTOM);

	}

	// /////////////////////////////////////////////////////////////////////////

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// mDbHelper.close();
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

	@Override
	public void closeDialog() {
		// TODO Auto-generated method stub
		if (mMenuDlg.isShowing())
			mMenuDlg.dismiss();
	}

}
