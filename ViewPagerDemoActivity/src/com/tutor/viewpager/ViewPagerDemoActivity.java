package com.tutor.viewpager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

/**
 * @author frankiewei ViewPager控件使用的Demo.
 */
public class ViewPagerDemoActivity extends Activity {

	/**
	 * 这里定义了相册的总数100条.
	 */
	private static final int ALBUM_COUNT = 100;

	/**
	 * 相册的资源,实战开发中都是网络数据或者本地相册.
	 */
	private static final int ALBUM_RES[] = { R.drawable.test1,
			R.drawable.test2, R.drawable.test3, R.drawable.test4,
			R.drawable.test5, R.drawable.test6 };

	private ViewPager mViewPager;

	/**
	 * 适配器.
	 */
	private ViewPagerAdapter mViewPagerAdapter;

	/**
	 * 数据源.
	 */
	private JSONArray mJsonArray;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		setupViews();
	}

	private void setupViews() {
		// 初始化JSonArray,给ViewPageAdapter提供数据源用.
		mJsonArray = new JSONArray();
		for (int i = 0; i < ALBUM_COUNT; i++) {
			JSONObject object = new JSONObject();
			try {
				object.put("resid", ALBUM_RES[i % ALBUM_RES.length]);
				object.put("name", "Album " + i);
				mJsonArray.put(object);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPagerAdapter = new ViewPagerAdapter(this, mJsonArray);
		mViewPager.setAdapter(mViewPagerAdapter);
	}
}