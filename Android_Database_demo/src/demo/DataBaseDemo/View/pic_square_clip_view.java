package demo.DataBaseDemo.View;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.View;

public class pic_square_clip_view extends View {

	private Context	mContext;

	public pic_square_clip_view(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	private DisplayMetrics getCenterSquare() {
		// 获得屏幕尺寸
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		return dm;
	}

	// class view method ///////////////////////////////////////////////////////////////
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		
		
		
	}

}
