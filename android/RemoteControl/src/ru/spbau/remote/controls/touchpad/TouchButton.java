package ru.spbau.remote.controls.touchpad;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class TouchButton extends BaseCustomControl {
	private boolean myPushedFlag = false;
	
	public TouchButton(TypedArray attrs) {
		super(attrs);
	}
	
	public void draw(Canvas canvas) {
		Paint painter = new Paint();
		painter.setColor(!myPushedFlag ? mainButtonColor() : subButtonColor());
		canvas.drawRect(rect(), painter);
	}

	public boolean onTouch(View v, MotionEvent event) {
		boolean changed = false;
		if ((event.getAction() == MotionEvent.ACTION_CANCEL
				|| event.getAction() == MotionEvent.ACTION_UP) && myPushedFlag) {
			changed = true;
		} else if (!myPushedFlag) {
			for (int p = 0; p < event.getPointerCount() && !changed; ++p) {
				if (rect().contains((int)event.getX(p), (int)event.getY(p))) {
					changed = true;
				}
			}
		} else {
			for (int p = 0; p < event.getPointerCount(); ++p) {
				if (rect().contains((int)event.getX(p), (int)event.getY(p))) {
					return true;
				}
			}
			changed = true;
		}
		if (changed) {
			myPushedFlag = !myPushedFlag;
			if (myPushedFlag) {
				buttonDown();
			} else {
				buttonUp();
			}
			v.invalidate(rect());
		}
		return true;
	}
}