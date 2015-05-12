####start service after force stop
使用settings->application->manage application->my app->force stop
并没有办法让其启动

you cant do this by starting two services coz force stop will close all your services related to the particular app which you are force stopping.So start the service when user again opens the app.

a service if Force Stopped can not be restarted automatically

As per the Android document

	Starting from Android 3.1, the system's package manager keeps track of applications that are in a stopped state and provides a means of controlling their launch from background processes and other applications.

	Note that an application's stopped state is not the same as an Activity's stopped state. The system manages those two stopped states separately.

	Note that the system adds FLAG_EXCLUDE_STOPPED_PACKAGES to all broadcast intents. It does this to prevent broadcasts from background services from inadvertently or unnecessarily launching components of stoppped applications. A background service or application can override this behavior by adding the FLAG_INCLUDE_STOPPED_PACKAGES flag to broadcast intents that should be allowed to activate stopped applications.

FLAG_EXCLUDE_STOPPED_PACKAGES
FLAG_INCLUDE_STOPPED_PACKAGES

一个应用没有activity，在API < 3.1的版本是可以的接受广播，3.1以后就不可以了。

从3.1开始出于安全的考虑，一个应用必须至少运行一次才能接受广播，意思是说用户知道这个应用运行过，它才可以接受广播。如果非要试试没有activity的话，可以第一次安装有activity，然后，删除activity和xml对应的配置信息再次安装。不过这样实际意义不大。

普通的App如果没有启动默认都是停止状态的，停止状态的App默认是接受不到任何广播的。不过发送广播时如果添加指定标记，也可以使停止状态的应用接受这个的广播。

广播默认不会发给停止状态的应用，除非设置过FLAG_INCLUDE_STOPPED_PACKAGES标记，但是系统（保守说大多数）广播是没有设置这个标记的，也就是说停止状态系统的应用接受不到系统广播。普通应用自定义的广播可以随便设置标记。

在3.1之后，系统的package manager增加了对处于“stopped state”应用的管理，这个stopped和Activity生命周期中的stop状态是完全两码事，指的是安装后从来没有启动过和被用户手动强制停止的应用，与此同时系统增加了2个Flag：FLAG_INCLUDE_STOPPED_PACKAGES和FLAG_EXCLUDE_STOPPED_PACKAGES ，来标识一个intent是否激活处于“stopped state”的应用。当2个Flag都不设置或者都进行设置的时候，采用的是FLAG_INCLUDE_STOPPED_PACKAGES的效果。
    有了上面的新机制之后，google觉得给所有的广播intent默认加上FLAG_EXCLUDE_STOPPED_PACKAGES会非常的Cooooool，能在一定程度上避免流氓软件、病毒啊干坏事，还能提高效率，就导致了本文题目中说的问题，RECEIVE_BOOT_COMPLETED广播如果用户没有运行过应用，就不会响应了。
    不过google还是留了点余地，对于自定义的广播我们可以通过
setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);这个方法来唤醒处于“stopped state”的程序，也就是用户自己写的广播intent可以控制这个机制，但是系统自带的广播intent，由于不能修改，就只能接受这个现实了
例如：
    Intent startIntent = new Intent();
    startIntent.putExtra("pkg", getPackageName());
    startIntent.setAction("com.lenovo.speechcamera.start");
    startIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
    sendBroadcast(startIntent);


int	FLAG_INCLUDE_STOPPED_PACKAGES	If set, this intent will always match any components in packages that are currently stopped.

####Launch controls on stopped applications

Starting from Android 3.1, the system's package manager keeps track of applications that are in a stopped state and provides a means of controlling their launch from background processes and other applications.

Note that an application's stopped state is not the same as an Activity's stopped state. The system manages those two stopped states separately.

The platform defines two new intent flags that let a sender specify whether the Intent should be allowed to activate components in stopped application.

FLAG_INCLUDE_STOPPED_PACKAGES — Include intent filters of stopped applications in the list of potential targets to resolve against.
FLAG_EXCLUDE_STOPPED_PACKAGES — Exclude intent filters of stopped applications from the list of potential targets.
When neither or both of these flags is defined in an intent, the default behavior is to include filters of stopped applications in the list of potential targets.

Note that the system adds FLAG_EXCLUDE_STOPPED_PACKAGES to all broadcast intents. It does this to prevent broadcasts from background services from inadvertently or unnecessarily launching components of stoppped applications. A background service or application can override this behavior by adding the FLAG_INCLUDE_STOPPED_PACKAGES flag to broadcast intents that should be allowed to activate stopped applications.

Applications are in a stopped state when they are first installed but are not yet launched and when they are manually stopped by the user (in Manage Applications).