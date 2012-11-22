package ru.spbau.remote.controls.touchpad;

import ru.spbau.remote.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class TouchPadView extends View {
	private static final int DEFAULT_MARGIN = 5;
	private static final int DEFAULT_PADDING = 5;
	private static final int DEFAULT_BUTTON_HEIGHT = 20;
	
	private int myWidth;
	private int myHeight;
	private TypedArray myAttrs;
	private TouchPanel myPanel;
	private TouchButton myLeft;
	private TouchButton myRight;
	
	public TouchPadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}
	
	public TouchPadView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}
	
	private void init(Context context, AttributeSet attrs) {
		myAttrs = context.obtainStyledAttributes(attrs, R.styleable.TouchPadStyle);
		myPanel = new TouchPanel(myAttrs);
		//myPanel.addListener(this);
		myLeft = new TouchButton(myAttrs);
		//myLeft.addListener(this);
		myRight = new TouchButton(myAttrs);
		//myRight.addListener(this);
	}
	
	private RectF panelRect() {
		float left = leftMargin();
		float top = topMargin();
		float right = left + panelWidth();
		float bottom = top + panelHeight();
		return new RectF(left, top, right, bottom);
	}
	
	private float leftMargin() {
		return myAttrs.getInteger(R.styleable.TouchPadStyle_leftMargin, DEFAULT_MARGIN);
	}
	
	private float rightMargin() {
		return myAttrs.getInteger(R.styleable.TouchPadStyle_rightMargin, DEFAULT_MARGIN);
	}
	
	private float topMargin() {
		return myAttrs.getInteger(R.styleable.TouchPadStyle_topMargin, DEFAULT_MARGIN);
	}
	
	private float bottomMargin() {
		return myAttrs.getInteger(R.styleable.TouchPadStyle_bottomMargin, DEFAULT_MARGIN);
	}
	
	private float padding() {
		return myAttrs.getInteger(R.styleable.TouchPadStyle_padding, DEFAULT_PADDING);
	}
	
	private float buttonWidth() {
		return (myWidth - leftMargin() - rightMargin() - padding()) / 2.0f;
	}
	
	private float buttonHeight() {
		float percent = myAttrs.getFraction(R.styleable.TouchPadStyle_buttonHeight,
				1, 1, DEFAULT_BUTTON_HEIGHT) / 100.0f;
		return (myHeight - topMargin() - bottomMargin() - padding()) * percent;
	}
	
	private float panelWidth() {
		return myWidth - leftMargin() - rightMargin();
	}
	
	private float panelHeight() {
		return myHeight - buttonHeight() - topMargin() - bottomMargin() - padding();
	}
	
	private RectF leftRect() {
		float left = leftMargin();
		float right = left + buttonWidth();
		float top = topMargin() + panelHeight() + padding();
		float bottom = top + buttonHeight();
		return new RectF(left, top, right, bottom);
	}

	private RectF rightRect() {
		RectF leftRect = leftRect();
		float top = leftRect.top;
		float bottom = leftRect.bottom;
		float left = leftRect.right + padding();
		float right = left + buttonWidth();
		return new RectF(left, top, right, bottom);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		myPanel.onTouch(this, event);
		myLeft.onTouch(this, event);
		myRight.onTouch(this, event);
		return true;
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		myWidth = w; myHeight = h;
		update();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		myPanel.draw(canvas);
		myLeft.draw(canvas);
		myRight.draw(canvas);
	}
	
	private void update() {
		myPanel.resize(panelRect());
		myLeft.resize(leftRect());
		myRight.resize(rightRect());
		invalidate();
	}

	public void buttonDown(Object button) {
		if (button == myLeft) {
			Log.d(getClass().getCanonicalName(), "left button down");
		} else {
			Log.d(getClass().getCanonicalName(), "right button down");
		}
	}

	public void buttonUp(Object button) {
		if (button == myLeft) {
			Log.d(getClass().getCanonicalName(), "left button up");
		} else {
			Log.d(getClass().getCanonicalName(), "right button up");
		}
	}

	public void move(Object panel, float dx, float dy) {
	}
}
