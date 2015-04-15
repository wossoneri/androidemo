package slidemenu;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

public class SlideMenu extends ViewGroup {
	public static final int SCREEN_MENU = 0;
	public static final int SCREEN_MAIN = 1;
	private static final int SCREEN_INVALID = -1;

	private int mCurrentScreen;
	private int mNextScreen = SCREEN_INVALID;

	private Scroller mScroller;
	/**
	 * 跟踪触摸事件的手势速度的类，对 fling 一类的手势使用。 需要开始跟踪的时候用 obtain 方法获取一个新的实例。用
	 * addMovement(MotionEvent) 方法传入你获取到的运动事件。 当你想确定速度，调用
	 * computeCurrentVelocity(int)，然后调用 getXVelocity(int) 和 getYVelocity(int)
	 * 获取每个指针id的速度 使用完后一定要 recycle 掉，通常你只需要维护一个活动对象来跟踪速度，recycle 会返回这个
	 * VelocityTracker 对象 这个对象可以在任何地方复用
	 */
	private VelocityTracker mVelocityTracker;
	private int mTouchSlop;

	private float mLastMotionX;
	private float mLastMotionY;

	private final static int TOUCH_STATE_REST = 0;
	private final static int TOUCH_STATE_SCROLLING = 1;
	private static final int SNAP_VELOCITY = 1000;

	public int mTouchState = TOUCH_STATE_REST;
	private boolean mLocked;
	private boolean mAllowLongPress;

	public SlideMenu(Context context) {
		this(context, null, 0);
	}

	public SlideMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlideMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mScroller = new Scroller(getContext());
		mCurrentScreen = SCREEN_MAIN;
		// Distance in pixels a touch can wander before we think the user is
		// scrolling
		// int 相当于一个手指滑动的阈值
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}

	// 测量 view 和其内容来决定尺寸 重载时需要调用 setMeasuredDimension() 保存测量出的宽高
	// 基类 measure 默认尺寸为背景 除非设定了更大的尺寸
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		measureViews(widthMeasureSpec, heightMeasureSpec);
	}

	public void measureViews(int widthMeasureSpec, int heightMeasureSpec) {
		View menuView = getChildAt(0); // 按顺序的话第一个是layout_menu
		menuView.measure(
				menuView.getLayoutParams().width + menuView.getLeft() + menuView.getRight(),
				heightMeasureSpec);

		View contentView = getChildAt(1);
		contentView.measure(widthMeasureSpec, heightMeasureSpec);
	}

	// 当这个 view 需要为它每个 children 分配尺寸和位置时 从 layout 调用
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childCount = getChildCount();
		if (childCount != 2) {
			throw new IllegalStateException("The childCount of SlidingMenu must be 2");
		}

		View menuView = getChildAt(0);
		final int width = menuView.getMeasuredWidth();
		menuView.layout(-width, 0, 0, menuView.getMeasuredHeight()); // 把 menu
																		// 放到屏幕外面

		View contentView = getChildAt(1);
		contentView.layout(0, 0, contentView.getMeasuredWidth(), contentView.getMeasuredHeight());
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		View child;
		for (int i = 0; i < getChildCount(); i++) {
			child = getChildAt(i);
			child.setFocusable(true);
			child.setClickable(true);
		}
	}

	// 拦截所有 touch 事件，截获的事件顺序如下
	// 1 获得 down 事件
	// 2 down 事件会被这个 viewGroup 的一个 child 处理或者送到 onTouchEvent 方法处理，这意味着你需要实现
	// onTouchEvent 返回 true 才可以看到剩下的手势，
	// 同时你在 onInterceptTouchEvent 不会收到任何后续事件，事件都在 onTouchEvent 发生
	// 3 这里一旦返回 false 后面的每个事件都先被送到这里然后在发到对应的 onTouchEvent
	// 4 返回 true 你不会收到任何后续事件，目标 view 会收到另外带有 ACTION_CANCEL 的相同的事件，并且后续事件直接发到
	// onTouchEvent 而不经过这里
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (mLocked) {
			return true;
		}

		final int action = ev.getAction();
		if ((action == MotionEvent.ACTION_MOVE) && (mTouchState != TOUCH_STATE_REST)) {
			return true;
		}

		final float x = ev.getX();
		final float y = ev.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			// Remember location of down touch
			mLastMotionX = x;
			mLastMotionY = y;
			mAllowLongPress = true;

			mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;

			break;
		case MotionEvent.ACTION_MOVE:

			final int xDiff = (int) Math.abs(x - mLastMotionX);
			final int yDiff = (int) Math.abs(y - mLastMotionY);

			final int touchSlop = mTouchSlop; // 阈值
			boolean xMoved = xDiff > touchSlop;
			boolean yMoved = yDiff > touchSlop;

			if (xMoved || yMoved) {

				if (xMoved) {
					// Scroll if the user moved far enough along the X axis
					mTouchState = TOUCH_STATE_SCROLLING;
					enableChildrenCache();
				}
				// Either way, cancel any pending longpress
				if (mAllowLongPress) {
					mAllowLongPress = false;
					// Try canceling the long press. It could also have been
					// scheduled
					// by a distant descendant, so use the mAllowLongPress flag
					// to block everything
					final View currentScreen = getChildAt(mCurrentScreen);
					// 取消悬而未决的长按。如果你希望菜单在某一个地方保持长按时弹出，同时希望在按下然后移动手指一定距离时产生滚动时不弹出，就可以用它
					currentScreen.cancelLongPress();
				}
			}
			break;

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			// Release the drag
			clearChildrenCache();
			mTouchState = TOUCH_STATE_REST;
			mAllowLongPress = false;
			break;
		}

		/*
		 * The only time we want to intercept motion events is if we are in the
		 * drag mode. at that time, we return true?
		 */
		return mTouchState != TOUCH_STATE_REST;
	}

	void enableChildrenCache() {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View layout = (View) getChildAt(i);
			layout.setDrawingCacheEnabled(true); // 配合 getDrawingCache
													// 把view绘在位图中
		}
	}

	void clearChildrenCache() {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View layout = (View) getChildAt(i);
			layout.setDrawingCacheEnabled(false);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mLocked) {
			return true;
		}

		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);

		final int action = ev.getAction();
		final float x = ev.getX();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			/*
			 * If being flinged and user touches, stop the fling. isFinished
			 * will be false if being flinged.
			 */
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}

			// Remember where the motion event started
			mLastMotionX = x;
			break;
		case MotionEvent.ACTION_MOVE:
			if (mTouchState == TOUCH_STATE_SCROLLING) {
				// Scroll to follow the motion event
				final int deltaX = (int) (mLastMotionX - x);
				mLastMotionX = x;

				if (deltaX < 0) { // scroll to right

					Log.v("Scroll", "deltaX = " + deltaX + "  getScrollX() = " + getScrollX());
					if (deltaX + getScrollX() >= -getChildAt(0).getWidth()) {
						scrollBy(deltaX, 0);
					}

				} else if (deltaX > 0) { // scroll to left
					// final int availableToScroll = getChildAt(getChildCount()
					// -
					// 1).getRight() - getScrollX() - getWidth();
					final int availableToScroll = -getScrollX();
					Log.v("Scroll", "deltaX = " + deltaX + "  getScrollX() = " + getScrollX()
							+ "   right = " + getChildAt(getChildCount() - 1).getRight()
							+ "  width = " + getWidth());

					if (availableToScroll > 0) {
						scrollBy(Math.min(availableToScroll, deltaX), 0);
					}
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mTouchState == TOUCH_STATE_SCROLLING) {	// 让他滚动完剩余距离
				final VelocityTracker velocityTracker = mVelocityTracker;
				velocityTracker.computeCurrentVelocity(1000);
				int velocityX = (int) velocityTracker.getXVelocity();

				if (velocityX > SNAP_VELOCITY && mCurrentScreen == SCREEN_MAIN) {	// 就是手指快速滑动一小段距离即可让其完全滑动
					// Fling hard enough to move left
					snapToScreen(SCREEN_MENU);
				} else if (velocityX < -SNAP_VELOCITY && mCurrentScreen == SCREEN_MENU) {
					// Fling hard enough to move right
					snapToScreen(SCREEN_MAIN);
				} else {
					snapToDestination();
				}

				if (mVelocityTracker != null) {
					mVelocityTracker.recycle();
					mVelocityTracker = null;
				}
			}
			mTouchState = TOUCH_STATE_REST;
			break;
		case MotionEvent.ACTION_CANCEL:
			mTouchState = TOUCH_STATE_REST;
		}

		return true;
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
		} else if (mNextScreen != SCREEN_INVALID) {
			mCurrentScreen = Math.max(0, Math.min(mNextScreen, getChildCount() - 1));
			mNextScreen = SCREEN_INVALID;
			clearChildrenCache();
		}
	}

	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
		postInvalidate();
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		final int scrollX = getScrollX();
		super.dispatchDraw(canvas);
		canvas.translate(scrollX, 0);
	}

	@Override
	public boolean dispatchUnhandledMove(View focused, int direction) {
		if (direction == View.FOCUS_LEFT) {
			if (getCurrentScreen() > 0) {
				snapToScreen(getCurrentScreen() - 1);
				return true;
			}
		} else if (direction == View.FOCUS_RIGHT) {
			if (getCurrentScreen() < getChildCount() - 1) {
				snapToScreen(getCurrentScreen() + 1);
				return true;
			}
		}
		return super.dispatchUnhandledMove(focused, direction);
	}

	protected void snapToScreen(int whichScreen) {

		enableChildrenCache();

		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1)); // 有用么？没懂
		boolean changingScreens = whichScreen != mCurrentScreen;

		mNextScreen = whichScreen;

		View focusedChild = getFocusedChild();	// 测试这里判断逻辑从哪来
		if (focusedChild != null && changingScreens && focusedChild == getChildAt(mCurrentScreen)) {
			focusedChild.clearFocus();
		}

		final int newX = (whichScreen - 1) * getChildAt(0).getWidth();
		final int delta = newX - getScrollX();
		mScroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta) * 2);
		invalidate();
	}

	protected void snapToDestination() {
		if (getScrollX() == 0) {
			return;
		}
		final int screenWidth = getChildAt(0).getWidth();
		final int whichScreen = (screenWidth + getScrollX() + (screenWidth / 2)) / screenWidth;
		snapToScreen(whichScreen);
	}

	public int getCurrentScreen() {
		return mCurrentScreen;
	}

	public boolean isMainScreenShowing() {
		return mCurrentScreen == SCREEN_MAIN;
	}

	public void openMenu() {
		mCurrentScreen = SCREEN_MENU;
		snapToScreen(mCurrentScreen);
	}

	public void closeMenu() {
		mCurrentScreen = SCREEN_MAIN;
		snapToScreen(mCurrentScreen);
	}

	public void unlock() {
		mLocked = false;
	}

	public void lock() {
		mLocked = true;
	}

}
