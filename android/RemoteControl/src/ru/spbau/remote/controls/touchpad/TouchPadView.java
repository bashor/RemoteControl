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

public class TouchPadView extends View implements TouchPanelListener, TouchButtonListener {
    private static final String TAG = TouchPadView.class.getCanonicalName();

	private static final int DEFAULT_MARGIN = 5;
	private static final int DEFAULT_PADDING = 5;
	private static final int DEFAULT_BUTTON_HEIGHT = 20;
	
	private int myWidth;
	private int myHeight;
	private TypedArray myAttrs;
	private TouchPanel myPanel;
	private TouchButton myLeft;
	private TouchButton myRight;
    private TouchPadListener myListener;
    private boolean myLeftIsDown = false;
    private boolean myRightIsDown = false;

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
		myPanel.setListener(this);
		myLeft = new TouchButton(myAttrs);
		myLeft.setListener(this);
		myRight = new TouchButton(myAttrs);
		myRight.setListener(this);
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

    @Override
    public void onMove(Object panel, float dx, float dy) {
        Log.d(TAG, "move");
        if (myListener != null) {
            myListener.onMove(dx, dy);
        }
    }

    @Override
    public void onDown(Object button) {
        if (button == myLeft) {
            Log.d(TAG, "left button down");
            if (myListener != null) {
                myListener.onDownLeftButton();
                myLeftIsDown = true;
            }
        } else {
            Log.d(TAG, "right button down");
            if (myListener != null) {
                myListener.onDownRightButton();
                myRightIsDown = true;
            }
        }
    }

    @Override
    public void onUp(Object button) {
        if (button == myLeft) {
            Log.d(TAG, "left button up");
            if (myListener != null) {
                myListener.onUpLeftButton();
                if (myLeftIsDown)
                    myListener.onClickLeftButton();
            }
        } else {
            Log.d(TAG, "right button up");
            if (myListener != null) {
                myListener.onUpRightButton();
                if (myRightIsDown)
                    myListener.onClickRightButton();
            }
        }
    }

    public void addListener(TouchPadListener listener) {
        myListener = listener;
    }
}
