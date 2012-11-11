package ru.spbau.remote.controls;

import java.util.ArrayList;
import java.util.List;
import static java.lang.Math.abs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * TouchPad panel collects touch screen events and generates relative coordinates
 * events (dx, dy)
 * 
 * @version 1.0
 * @author kmu
 */
public class TouchPadPanel extends View {
	private static final float THRESHOLD = 0.01f;
	private static final float RESPONCE = 1.5f;
	
	private float myX;
	private float myY;
	private int myTouchId;
	private boolean myPushedFlag;
	
	private final List<PositionListener> myListeners = new ArrayList<PositionListener>(); 

	public TouchPadPanel(Context context) {
		super(context);
	}
	
	public TouchPadPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public TouchPadPanel(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int actionCode = event.getAction() & MotionEvent.ACTION_MASK;
		if (actionCode == MotionEvent.ACTION_DOWN && !myPushedFlag) {
			myPushedFlag = true;
			myTouchId = event.getPointerId(0);
			myX = event.getX(0);
			myY = event.getY(0);
		} else if ((actionCode == MotionEvent.ACTION_DOWN || actionCode == MotionEvent.ACTION_CANCEL)
				&& myPushedFlag) {
			myPushedFlag = false;
		} else if (actionCode == MotionEvent.ACTION_MOVE && myPushedFlag) {
			for (int i = 0; i < event.getPointerCount(); ++i) {
				if (event.getPointerId(i) == myTouchId) {
					float dx = RESPONCE * (event.getX(i) - myX) / getWidth();
					float dy = RESPONCE * (event.getY(i) - myY) / getHeight();
					if (abs(dx) > THRESHOLD || abs(dy) > THRESHOLD) {
						myX = event.getX(i);
						myY = event.getY(i);
						notifyListeners(dx, dy);
					}
				}
			}
		}
		return true;
	}
	
	public void addListener(PositionListener listener) {
		myListeners.add(listener);
	}
	
	public void removeListener(PositionListener listener) {
		myListeners.remove(listener);
	}
	
	private void notifyListeners(float dx, float dy) {
		for (PositionListener listener: myListeners) {
			listener.positionEvent(dx, dy);
		}
	}
}
