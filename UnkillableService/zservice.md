###Services

&emsp;&emsp;服务是一个应用程序组件,可以在后台执行长时间运行的操作,不提供用户界面。一个应用程序组件可以启动一个服务,它将继续在后台运行,即使用户切换到另一个应用程序。此外,一个组件可以绑定到一个服务与它交互,甚至执行进程间通信(IPC)。例如,一个服务可能处理网络通信,播放音乐,执行文件I/O,或与一个内容提供者交互,都在后台执行。

一个服务本质上讲有两种形式：

Started 启动的
>`started`形式的服务是指当一个应用组件(比如`activity`)通过startService()方法开启的服务。一旦开启，该服务就可以<font color="red"><b>无限期</b></font>地在后台运行，哪怕开启它的组件被销毁掉。
>通常,开启的服务执行一个单独的操作且并不向调用者返回一个结果。
>比如，可能从网络进行下载或者上传一个文件。当任务完成，服务就该自我停止。

Bound 绑定的
>`bound`形式的服务是指一个应用组件通过调用 bindService() 方法与服务绑定。一个绑定的服务提供一个客户-服务端接口，以允许组件与服务交互，发送请求，获得结果，甚至执行进程间通信。一个绑定的服务只和与其绑定的组件同时运行。多个组件可以同时绑定到一个服务，但当全部接触绑定后，服务就被销毁。

&emsp;&emsp;虽然分这两类，但是一个服务可以同时使用这两种方式——可以用`started`无限期的运行，同时允许绑定。只需要在服务中实现两个回调方法：<i>onStartCommand()</i>允许组件开启服务，<i>onBind()</i>允许绑定。

&emsp;&emsp;不论应用程序是怎么起服务的，<font color="red"><b>任何</b></font>应用程序都可以用这个服务。同样的，任何组件可以使用一个`Activity`通过传递`Intent`开启服务。你也可以在配置文件设置服务为私有来防止其他应用访问该服务。

><font color="red"><b>注意：</b></font>一个服务在进程中的主线程运行——一个服务<b>不会</b>创建自己的线程，也<b>不会</b>在另外的进程运行(除非另外指定)。这意味着，如果服务需要做一些频繁占用CPU的工作或者会发生阻塞的操作，你需要在服务中另开线程执行任务。这可以降低产生ANR的风险，提高用户体验。

####基础
&emsp;&emsp;创建一个服务需要建立一个`Service`相关的子类，然后需要实现一些回调方法，好在不同的生命周期内做对应处理和绑定服务，比较重要的方法如下：
* <i>onStartCommand()</i>
	当其他组件，如 activity 请求服务启动时，系统会调用这个方法。一旦这个方法执行，服务就开始并且无限期的执行。如果实现这个方法，当这个服务完成任务后，需要你来调用 stopSelf() 或者 stopService() 停掉服务。如果只想提供绑定，不需要自己实现这个方法。
* <i>onBind()</i>
	当有其他组件想通过 bindService() 方法绑定这个服务时系统就会调用此方法。在实现的方法里面，必须添加一个供客户端使用的接口通过返回一个`IBinder`来与服务通信，这个方法必须实现。当然不想允许绑定的话，返回`null`即可。
* <i>onCreate()</i>
	服务第一次建立的时候会调用这个方法，执行一次性设置程序，在上面2个方法执行前调用。如果服务已存在，则不执行该方法。
* <i>onDestroy()</i>
	服务不再使用则使用该方法。服务应该实现这个方法来清理诸如线程，注册的监听器等资源。这是最后调用的方法。

&emsp;&emsp;安卓系统只会在内存占用很高，必须恢复系统资源供当前运行程序的情况下强制停掉一个运行中的服务。如果服务绑定在当前运行的程序中，就几乎不会被杀掉，如果服务声明了在前台运行（其实在后台，只是给系统一个错的信息来提高优先级），就几乎不会被杀掉。另外，如果一个服务正在运行，且运行了很久，系统就会根据运行时间把其排在后台任务列表的后面，则这个服务很容易被杀掉。根据<i>onStartCommand()</i> 的返回值设置，服务被杀掉后仍可以在资源充足的条件下立即重启。

><b>是用一个服务好还是开一个线程好</b>
>一个服务就是一个可以忽略交互，在后台独立运行的组件，如果你需要这样就用服务
>如果你需要在用户与程序交互时在主线程外执行任务，那就开个线程吧。
>比如想播放音乐，但只在程序运行时播放，你可能在 onCreate() 开一个线程，在 onStart() 中开启它，在 onStop() 停止它。也可以考虑使用`AsyncTask`或者`HandlerThread`取代一般的线程。
>记住，如果使用一个服务，它还是默认在主线程中运行，如果会发生阻塞，还是要在服务中另开线程的。

#####在 manifest 文件声明服务
要使用服务就必须在 `manifest` 文件声明要用的所有服务，只用在`<application>`标签内添加子标签`<service>`即可。
```xml
	<manifest ...>
    	...
        <application ...>
        	<service android:name=".ExampleService"
            	android:enabled=["true" | "false"]
         		android:exported=["true" | "false"]
         		android:isolatedProcess=["true" | "false"]
         		android:label="string resource"
         		android:icon="drawable resource"
         		android:permission="string"
         		android:process="string" >
    			...
			</service>
        </application>
    </manifest>
```
下面对`service`标签属性做说明
* <font color="green">android:name</font>
	你所编写的服务类的类名，可填写完整名称，包名+类名，如`com.example.test.ServiceA`，也可以忽略包名，用`.`开头，如`.ServiceA`，因为在`manifest`文件开头会定义包名，它会自己引用。

    一旦你发布应用，你就不能改这个名字(除非设置`android:exported="false"`)，另外`name`没有默认值，必须定义。
* <font color="green">android:enabled</font>
	是否可以被系统实例化，默认为`true`

    因为父标签`<application>`也有`enable`属性，所以必须两个都为默认值`true`的情况下服务才会被激活，否则不会激活。
* <font color="green">android:exported</font>
	其他应用能否访问该服务，如果不能，则只有本应用或有相同用户ID的应用能访问。当然除了该属性也可以在下面`permission`中限制其他应用访问本服务。

    这个默认值与服务是否包含意图过滤器`intent filters`有关。如果一个也没有则为`false`
* <font color="green">android:isolatedProcess</font>
	设置`true`意味着，服务会在一个特殊的进程下运行，这个进程与系统其他进程分开且没有自己的权限。与其通信的唯一途径是通过服务的API(binding and starting)。
* <font color="green">android:label</font>
	可以显示给用户的服务名称。如果没设置，就用`<application>`的`lable`。不管怎样，这个值是所有服务的意图过滤器的默认`lable`。定义尽量用对字符串资源的引用。
* <font color="green">android:icon</font>
	类似`label`，是图标，尽量用`drawable`资源的引用定义。
* <font color="green">android:permission</font>
	是一个实体必须要运行或绑定一个服务的权限。如果没有权限，`startService()`，`bindService()`或`stopService()`方法将不执行，`Intent`也不会传递到服务。

    如果属性未设置，会由`<application>`权限设置情况应用到服务。如果两者都未设置，服务就不受权限保护。
* <font color="green">android:process</font>
	服务运行所在的进程名。通常为默认为应用程序所在的进程，与包名同名。`<application>`元素的属性`process`可以设置不同的进程名，当然组件也可设置自己的进程覆盖应用的设置。

    如果名称设置为冒号`：`开头，一个对应用程序私有的新进程会在需要时和运行到这个进程时建立。如果名称为小写字母开头，服务会在一个相同名字的全局进程运行，如果有权限这样的话。这允许不同应用程序的组件可以分享一个进程，减少了资源的使用。

####创建“启动的”服务
&emsp;&emsp;启动的(started)服务由<i>startService(Intent)</i>方法启动，在服务中的<i>onStartCommand()</i>方法里获得`Intent`信息。关闭则由服务自己的方法<i>stopSelf()</i>或者由启动服务的地方调用<i>stopService(Intent)</i>方法来关闭。并不会因为启动服务的应用程序销毁而关闭。

&emsp;&emsp;示例，一个应用需要保存数据到远程数据库，这时启动一个服务，通过创建启动的服务给服务传递数据，由服务执行保存行为，行为结束再自我销毁。因为服务跟启动它的应用在一个进程的主线程中，所以对于耗时的操作要起一个新的线程去做。
```java
//activity中
Intent intent = new Intent(MainActivity.this, ServiceA.class);
intent.putExtra("name", strName);
startService(intent);

//service中
@Override
public int onStartCommand(Intent intent, int flags, int startId) {
	// TODO Auto-generated method stub
    // 获取数据
	String strName = intent.getStringExtra("name");
	// ... 数据库操作
    new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				耗时的操作
			}
		}).run();
	return Service.START_STICKY;
}
```

写服务有2种，继承`service`或者`IntentService`。后者是前者的子类。前者包含上面介绍的各种方法，用于普通的服务。后者可以自己开一个工作线程一个接一个处理多个请求。

#####继承IntentService
大多数服务不需要同时处理多个请求，继承`IntentService`是最好的选择

IntentService处理流程
* 创建默认的一个`worker`线程处理传递给<i>onStartCommand()</i>的所有`intent`，不占据应用的主线程
* 创建一个工作队列一次传递一个`intent`到你实现的<i>onHandleIntent()</i>方法，避免了多线程
* 在所以启动请求被处理后自动关闭服务，不需要调用<i>stopSelf()</i>
* 默认提供<i>onBind()</i>的实现，并返回`null`
* 默认提供<i>onStartCommand()</i>的实现，实现发送`intent`到工作队列再到你的<i>onHandleIntent()</i>方法实现。

这些都加入到`IntentService`中了，你需要做的就是实现构造方法和<i>onHandleIntent()</i>，如下：
```java
public class HelloIntentService extends IntentService {

  /**
   * A constructor is required, and must call the super IntentService(String)
   * constructor with a name for the worker thread.
   */
  public HelloIntentService() {
      super("HelloIntentService");
  }

  /**
   * The IntentService calls this method from the default worker thread with
   * the intent that started the service. When this method returns, IntentService
   * stops the service, as appropriate.
   */
  @Override
  protected void onHandleIntent(Intent intent) {
      // Normally we would do some work here, like download a file.
      // For our sample, we just sleep for 5 seconds.
      long endTime = System.currentTimeMillis() + 5*1000;
      while (System.currentTimeMillis() < endTime) {
          synchronized (this) {
              try {
                  wait(endTime - System.currentTimeMillis());
              } catch (Exception e) {
              }
          }
      }
  }
}
```
如果需要重写其他回调方法，如<i>onCreate()</i>,<i>onStartCommand()</i>等，一定要调用<i>super()</i>方法，保证`IntentService`正确处理`worker`线程，只有<i>onHandleIntent()</i>和<i>onBind()</i>不需要这样。如：
```java
@Override
public int onStartCommand(Intent intent, int flags, int startId) {
    Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
    return super.onStartCommand(intent,flags,startId);
}
```
#####继承Service
继承`Service`就可以实现对请求多线程的处理，前面介绍了`service`的生命周期，可以按照生命周期实现方法。就不放示例了。

<b><i>onStartCommand()</i>的返回值</b>
返回一个整型值，用来描述系统在杀掉服务后是否要继续启动服务，返回值有三种：
* <font color="#0099cc">START_NOT_STICKY</font>
	系统不重新创建服务，除非有将要传递来的`intent`。这是最安全的选项，可以避免在不必要的时候运行服务。
* <font color="#0099cc">START_STICKY</font>
	系统重新创建服务并且调用<i>onStartCommand()</i>方法，但并不会传递最后一次传递的`intent`，只是传递一个空的`intent`。除非存在将要传递来的`intent`，那么就会传递这些`intent`。这个适合播放器一类的服务，不需要执行命令，只需要独自运行，等待任务。
* <font color="#0099cc">START_REDELIVER_INTENT</font>
	系统重新创建服务并且调用<i>onStartCommand()</i>方法，传递最后一次传递的`intent`。其余存在的需要传递的intent会按顺序传递进来。这适合像下载一样的服务，立即恢复，积极执行。

如果想从服务获得结果，可以用广播来处理



####创建“绑定的”服务

* 用<i>bindService()</i>方法将应用组件绑定到服务，建立一个长时间保持的联系。
* 如果需要在`activity`或其他组件和服务交互或者通过进程间通信给其他应用程序提供本应用的功能，就需要绑定的服务。
* 建立一个绑定的服务需要实现<i>onBind()</i>方法返回一个定义了与服务通信接口的`IBinder`对象。其他应用程序组件可以调用<i>bindService()</i>方法获取接口并且调用服务上的方法。
* 创建一个绑定的服务，第一件事就是定义一个说明客户端与服务通信方式的接口。这个接口必须是`IBinder`的实现，并且必须要从<i>onBind()</i>方法返回。一旦客户端接收到了`IBinder`，就可以通过这个接口进行交互。
* 多个客户端可以绑定到一个服务，可以用<i>unbindService()</i>方法解除绑定，当没有组件绑定在服务上，这个服务就会被销毁。

```java
//activity中
private ServiceConnection connB = new ServiceConnection() {

	@Override
	public void onServiceDisconnected(ComponentName name) {
		// TODO Auto-generated method stub
		Log.v(tag, "Service B disconnected");
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		// TODO Auto-generated method stub
		Log.v(tag, "Service B connected");
		MyBinderB binder = (MyBinderB) service;
		ServiceB SB = binder.getService();
		SB.showLog();
	}
};
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

	findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent a = new Intent(MainActivity.this, ServiceB.class);
			bindService(a, connB, BIND_AUTO_CREATE);
	}
}


//ServiceB
public class ServiceB extends Service {
	public void showLog() {
		Log.i(tag, "serviceB-->showLog()");
	}

	public class MyBinderB extends Binder {

		public ServiceB getService() {
			return ServiceB.this;
		}
	}

	private MyBinderB myBinderB = new MyBinderB();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return myBinderB;
	}
}
```
#####启动前台服务
&emsp;&emsp;前台服务是被认为是用户已知的正在运行的服务，当系统需要释放内存时不会优先杀掉该进程。前台进程必须发一个`notification`在状态栏中显示，直到进程被杀死。因为前台服务会一直消耗一部分资源，但不像一般服务那样会在需要的时候被杀掉，所以为了能节约资源，保护电池寿命，一定要在建前台服务的时候发`notification`，提示用户。当然，系统提供的方法就是必须有`notification`参数的，所以不要想着怎么把`notification`隐藏掉。
```java
@Override
public int onStartCommand(Intent intent, int flags, int startId) {
	// TODO Auto-generated method stub
	Intent notificationIntent = new Intent(this, MainActivity.class);
	PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
	Notification noti = new Notification.Builder(this)
				.setContentTitle("Title")
				.setContentText("Message")
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentIntent(pendingIntent)
				.build();
	startForeground(12346, noti);
	return Service.START_STICKY;
}
```
<i>startForeground()</i>方法就是将服务设为前台服务。参数12346就是这个通知唯一的id，只要不为0即可。

#####服务的生命周期

![生命周期图][life_cycle]
* 启动的服务：
	startService()->onCreate()->onStartCommand()->running->stopService()/stopSelf()->onDestroy()->stopped
    其中，服务未运行时会调用一次onCreate()，运行时不调用。
* 绑定的服务：
	bindService()->onCreate()->onBind()->running->onUnbind()->onDestroy()->stopped

服务起始于<i>onCreate()</i>，终止于<i>onDestory()</i>
服务的开关过程，只有<i>onStartCommand()</i>可多次调用，其他在一个生命周期只调用一次。

这两个过程并不完全独立，也可以绑定一个由<i>startService()</i>启动过的服务

####关于怎样让服务不被杀死
&emsp;&emsp;这个倒是有点流氓软件的意思，但有些特定情况还是需要服务能保持开启不被杀死，当然这样做我还是在程序里添加了关闭服务的按钮，也就是开启了就杀不死，除非在软件里关闭。

服务不被杀死分3种来讨论
1.系统根据资源分配情况杀死服务
2.用户通过`settings`->`Apps`->`Running`->`Stop`方式杀死服务
3.用户通过`settings`->`Apps`->`Downloaded`->`Force Stop`方式杀死服务

<b>第一种情况：</b>
&emsp;&emsp;用户不干预，完全靠系统来控制，办法有很多。比如<i>onStartCommand()</i>方法的返回值设为`START_STICKY`，服务就会在资源紧张的时候被杀掉，然后在资源足够的时候再恢复。当然也可设置为前台服务，使其有高的优先级，在资源紧张的时候也不会被杀掉。

<b>第二种情况：</b>
&emsp;&emsp;用户干预，主动杀掉运行中的服务。这个过程杀死服务会通过服务的生命周期，也就是会调用<i>onDestory()</i>方法，这时候一个方案就是在<i>onDestory()</i>中发送广播开启自己。这样杀死服务后会立即启动。如下：
```java
@Override
public void onCreate() {
	// TODO Auto-generated method stub
	super.onCreate();

	mBR = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Intent a = new Intent(ServiceA.this, ServiceA.class);
			startService(a);
		}
	};
	mIF = new IntentFilter();
	mIF.addAction("listener");
	registerReceiver(mBR, mIF);
}

@Override
public void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();

	Intent intent = new Intent();
	intent.setAction("listener");
	sendBroadcast(intent);

	unregisterReceiver(mBR);
}
```
&emsp;&emsp;当然，从理论上来讲这个方案是可行的，实验一下也可以。但有些情况下，发送的广播在消息队列中排的靠后，就有可能服务还没接收到广播就销毁了(这是我对实验结果的猜想，具体执行步骤暂时还不了解)。所以为了能让这个机制完美运行，可以开启两个服务，相互监听，相互启动。服务A监听B的广播来启动B，服务B监听A的广播来启动A。经过实验，这个方案可行，并且用360杀掉后几秒后服务也还是能自启的。到这里再说一句，如果不是某些功能需要的服务，不建议这么做，会降低用户体验。

<b>第三种情况：</b>
&emsp;&emsp;强制关闭就没有办法。这个好像是从包的level去关的，并不走完整的生命周期。所以在服务里加代码是无法被调用的。处理这个情况的唯一方法是屏蔽掉`force stop`和`uninstall`按钮，让其不可用。方法自己去找吧。当然有些手机自带的清理功能就是从这个地方清理的，比如华为的清理。所以第三种情况我也没有什么更好的办法了。

&emsp;&emsp;最后再说一句，别在这上面太折腾，弄成流氓软件就不好了。我就是讨厌一些软件乱发通知，起服务才转而用iPhone的。不过下一代Android好像可以支持用户选择是否开启软件设置的权限了，倒是可以期待一下。
推荐一篇文章：[Diamonds Are Forever. Services Are Not.](http://www.androidguys.com/2009/09/09/diamonds-are-forever-services-are-not/ "Diamonds Are Forever. Services Are Not.")



[life_cycle]:http://images.cnblogs.com/cnblogs_com/rossoneri/682731/o_service_lifecycle.png