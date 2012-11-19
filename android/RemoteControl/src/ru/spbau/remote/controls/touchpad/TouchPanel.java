package ru.spbau.remote.controls.touchpad;

import static java.lang.Math.abs;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

public class TouchPanel extends BaseCustomControl {
	private static final float THRESHOLD = 0.01f;
	private static final float RESPONCE = 1.5f;
	
	private boolean myPushedFlag;
	private int myTouchId;
	private float myX;
	private float myY;
	
	public TouchPanel(TypedArray attrs, RectF border) {
		super(attrs, border);
	}
	
	public TouchPanel(TypedArray attrs) {
		super(attrs);
	}
	
	public void draw(Canvas canvas) {
		Paint painter = new Paint();
		painter.setColor(panelColor());
		canvas.drawRect(rect(), painter);
	}
	
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN && !myPushedFlag) {
			for (int p = 0; p < event.getPointerCount(); ++p) {
				if (rect().contains((int) event.getX(p), (int) event.getY(p))) {
					myTouchId = event.getPointerId(p);
					myX = event.getX(p);
					myY = event.getY(p);
					myPushedFlag = true;
					break;
				}
			}
		} else if ((event.getAction() == MotionEvent.ACTION_UP
				|| event.getAction() == MotionEvent.ACTION_CANCEL)
				&& myPushedFlag) {
			for (int p = 0; p < event.getPointerCount(); ++p) {
				if (myTouchId == event.getPointerId(p)) {
					myPushedFlag = false;
					break;
				}
			}
		} else if (event.getAction() == MotionEvent.ACTION_MOVE && myPushedFlag) {
			for (int i = 0; i < event.getPointerCount(); ++i) {
				if (event.getPointerId(i) == myTouchId) {
					float dx = RESPONCE * (event.getX(i) - myX) / width();
					float dy = RESPONCE * (event.getY(i) - myY) / height();
					if (abs(dx) > THRESHOLD || abs(dy) > THRESHOLD) {
						myX = event.getX(i);
						myY = event.getY(i);
						move(dx, dy);
					}
					break;
				}
			}
		}
		return true;
	}
}
