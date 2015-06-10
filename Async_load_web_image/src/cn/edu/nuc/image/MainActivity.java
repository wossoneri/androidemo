package cn.edu.nuc.image;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	private AsyncImageLoader loader = new AsyncImageLoader();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        loadImage("http://www.android.com/images/icon-partners.png", R.id.imageView01);
        loadImage("http://www.android.com/images/icon-dev.png", R.id.imageView02);
        loadImage("http://www.android.com/images/icon-market.png", R.id.imageView03);
    }
    //url：下载图片的URL
    //id：ImageView控件的ID
    private void loadImage(final String url, final int id) {
		// 如果缓存过就会从缓存中取出图像，ImageCallback接口中方法也不会被执行
    	ImageView imageView = (ImageView)findViewById(id);
    	CallbackImpl callbackImpl = new CallbackImpl(imageView);
    	Drawable cacheImage = 
    		loader.loadDrawable(url, callbackImpl);
		if (cacheImage != null) {
			imageView.setImageDrawable(cacheImage);
		}
	}
}