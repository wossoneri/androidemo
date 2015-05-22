package demo.DataBaseDemo.View;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.example.android_database_demo.R;

public class head_portrait_menu_view {

	public interface CloseDlgInterface {
		public void closeDialog();
	}

	private CloseDlgInterface mCloseDlgInterface;

	public void setCloseInterface(CloseDlgInterface mIF) {
		mCloseDlgInterface = mIF;
	}

	Activity mActivity;
	ViewGroup mView;
	LayoutInflater mInflater;
	private static final int START_CAMERA = 1;

	public head_portrait_menu_view(Activity activity) {
		this.mActivity = activity;

		mInflater = LayoutInflater.from(mActivity);
		mView = (ViewGroup) mInflater.inflate(R.layout.change_head_portrait_menu, null);

		mView.findViewById(R.id.menu_take_photo).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 利用系统自带的相机应用:拍照
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				mActivity.startActivityForResult(intent, START_CAMERA);

				mCloseDlgInterface.closeDialog();
			}
		});

		mView.findViewById(R.id.menu_choose_from_photo).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCloseDlgInterface.closeDialog();
			}
		});

		mView.findViewById(R.id.menu_cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCloseDlgInterface.closeDialog();
			}
		});
		
	}

	
	
	public ViewGroup getViewGroup() {
		return mView;
	}

}
