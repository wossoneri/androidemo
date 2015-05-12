###Services

&emsp;&emsp;服务是一个应用程序组件,可以在后台执行长时间运行的操作,不提供用户界面。一个应用程序组件可以启动一个服务,它将继续在后台运行,即使用户切换到另一个应用程序。此外,一个组件可以绑定到一个服务与它交互,甚至执行进程间通信(IPC)。例如,一个服务可能处理网络通信,播放音乐,执行文件I / O,或与一个内容提供者交互,都在后台执行。

一个服务本质上讲有两种形式：

Started
>"started"形式的服务是指当一个应用组件(比如 activity)通过 startService() 方法开启的服务。一旦开启，该服务就可以<font color="red"><b>无限期</b></font>地在后台运行，哪怕开启它的组件被销毁掉。
>通常,开启的服务执行一个单独的操作且并不向调用者返回一个结果。
>比如，可能从网络进行下载或者上传一个文件。当任务完成，服务就该自我停止。

Bound
>"bound"形式的服务是指一个应用组件通过调用 bindService() 方法与服务绑定。一个绑定的服务提供一个客户-服务端接口，以允许组件与服务交互，发送请求，获得结果，甚至执行进程间通信。一个绑定的服务只和与其绑定的组件同时运行。多个组件可以同时绑定到一个服务，但当全部接触绑定后，服务就被销毁。

&emsp;&emsp;虽然分这两类，但是一个服务可以同时使用这两种方式——可以用 started 无限期的运行，同时允许绑定。只需要在服务中实现两个回调方法： onStartCommand() 允许组件开启服务， onBind() 允许绑定。

&emsp;&emsp;不论应用程序是怎么起服务的，<font color="red"><b>任何</b></font>应用程序都可以用这个服务。同样的，任何组件可以使用一个 activity 通过传递 Intent 开启服务。你也可以在配置文件设置服务为私有来防止其他应用访问该服务。

><font color="red"><b>注意：</b></font>一个服务在进程中的主线程运行——一个服务<b>不会</b>创建自己的线程，也<b>不会</b>在另外的进程运行(除非另外指定)。这意味着，如果服务需要做一些频繁占用CPU的工作或者会发生阻塞的操作，你需要在服务中另开线程执行任务。这可以降低产生ANR的风险，提高用户体验。

####基础
&emsp;&emsp;创建一个服务需要建立一个 Service 相关的子类，然后需要实现一些回调方法，好在不同的生命周期内做对应处理和绑定服务，比较重要的方法如下：
* onStartCommand()
	当其他组件，如 activity 请求服务启动时，系统会调用这个方法。一旦这个方法执行，服务就开始并且无限期的执行。如果实现这个方法，当这个服务完成任务后，需要你来调用 stopSelf() 或者 stopService() 停掉服务。如果只想提供绑定，不需要自己实现这个方法。
* onBind()
	当有其他组件想通过 bindService() 方法绑定这个服务时系统就会调用此方法。在实现的方法里面，必须添加一个供客户端使用的接口通过返回一个 IBinder 来与服务通信，这个方法必须实现。当然不想允许绑定的话，返回 null 即可。
* onCreate()
	服务第一次建立的时候会调用这个方法，执行一次性设置程序，在上面2个方法执行前调用。如果服务已存在，则不执行该方法。
* onDestroy()
	服务不再使用则使用该方法。服务应该实现这个方法来清理诸如线程，注册的监听器等资源。这是最后调用的方法。

&emsp;&emsp;安卓系统只会在内存占用很高，必须恢复系统资源供当前运行程序的情况下强制停掉一个运行中的服务。如果服务绑定在当前运行的程序中，就几乎不会被杀掉，如果服务声明了在前台运行（其实在后台，只是给系统一个错的信息来提高优先级），就几乎不会被杀掉。另外，如果一个服务正在运行，且运行了很久，系统就会根据运行时间把其排在后台任务列表的后面，则这个服务很容易被杀掉。根据 onStartCommand() 的返回值设置，服务被杀掉后仍可以在资源充足的条件下立即重启。

><b>是用一个服务好还是开一个线程好</b>
>一个服务就是一个可以忽略交互，在后台独立运行的组件，如果你需要这样就用服务
>如果你需要在用户与程序交互时在主线程外执行任务，那就开个线程吧。
>比如想播放音乐，但只在程序运行时播放，你可能在 onCreate() 开一个线程，在 onStart() 中开启它，在 onStop() 停止它。也可以考虑使用 AsyncTask 或者 HandlerThread 取代一般的线程。
>记住，如果使用一个服务，它还是默认在主线程中运行，如果会发生阻塞，还是要在服务中另开线程的。

