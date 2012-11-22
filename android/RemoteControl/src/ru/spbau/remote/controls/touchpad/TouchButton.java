package ru.spbau.remote.controls.touchpad;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class TouchButton extends BaseCustomControl {
	private boolean myPushedFlag;
	
	public TouchButton(TypedArray attrs) {
		super(attrs);
	}
	
	public void draw(Canvas canvas) {
		Paint painter = new Paint();
		painter.setColor(!myPushedFlag ? mainButtonColor() : subButtonColor());
		canvas.drawRect(rect(), painter);
	}

	public void fingerDown(Event event) {
		if (!fingers().isEmpty() && !myPushedFlag) {
			myPushedFlag = true;
			event.getView().invalidate(rect());
			Log.d(getClass().getCanonicalName(), "button down");
		}
	}
	
	public void fingerUp(Event event) {
		if (fingers().isEmpty() && myPushedFlag) {
			myPushedFlag = false;
			event.getView().invalidate(rect());
			Log.d(getClass().getCanonicalName(), "button up");
		}
	}
}