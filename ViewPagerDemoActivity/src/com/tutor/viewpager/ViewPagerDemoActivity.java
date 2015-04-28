package com.tutor.viewpager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

/**
 * @author frankiewei ViewPager�ؼ�ʹ�õ�Demo.
 */
public class ViewPagerDemoActivity extends Activity {

	/**
	 * ���ﶨ������������100��.
	 */
	private static final int ALBUM_COUNT = 100;

	/**
	 * ������Դ,ʵս�����ж����������ݻ��߱������.
	 */
	private static final int ALBUM_RES[] = { R.drawable.test1,
			R.drawable.test2, R.drawable.test3, R.drawable.test4,
			R.drawable.test5, R.drawable.test6 };

	private ViewPager mViewPager;

	/**
	 * ������.
	 */
	private ViewPagerAdapter mViewPagerAdapter;

	/**
	 * ����Դ.
	 */
	private JSONArray mJsonArray;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		setupViews();
	}

	private void setupViews() {
		// ��ʼ��JSonArray,��ViewPageAdapter�ṩ����Դ��.
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