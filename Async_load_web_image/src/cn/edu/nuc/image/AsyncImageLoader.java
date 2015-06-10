package cn.edu.nuc.image;

import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

//该类的主要作用是实现图片的异步加载
public class AsyncImageLoader {
	//图片缓存对象
	//键是图片的URL，值是一个SoftReference对象，该对象指向一个Drawable对象
	private Map<String, SoftReference<Drawable>> imageCache = 
		new HashMap<String, SoftReference<Drawable>>();
	
	//实现图片的异步加载
	public Drawable loadDrawable(final String imageUrl,final ImageCallback callback){
		//查询缓存，查看当前需要下载的图片是否已经存在于缓存当中
		if(imageCache.containsKey(imageUrl)){
			SoftReference<Drawable> softReference=imageCache.get(imageUrl);
			if(softReference.get() != null){
				return softReference.get();
			}
		}
		
		final Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				callback.imageLoaded((Drawable) msg.obj);
			}
		};
		//新开辟一个线程，该线程用于进行图片的下载
		new Thread(){
			public void run() {
				Drawable drawable=loadImageFromUrl(imageUrl);
				imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			};
		}.start();
		return null;
	}
	//该方法用于根据图片的URL，从网络上下载图片
	protected Drawable loadImageFromUrl(String imageUrl) {
		try {
			//根据图片的URL，下载图片，并生成一个Drawable对象
			return Drawable.createFromStream(new URL(imageUrl).openStream(), "src");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	//回调接口
	public interface ImageCallback{
		public void imageLoaded(Drawable imageDrawable);
	}
}
