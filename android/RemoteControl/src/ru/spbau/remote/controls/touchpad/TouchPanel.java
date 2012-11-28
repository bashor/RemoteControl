package ru.spbau.remote.controls.touchpad;

import static java.lang.Math.abs;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

public class TouchPanel extends BaseCustomControl {
	private static final float THRESHOLD = 0.01f;
	private static final float RESPONCE = 1.5f;
	
	private boolean myPushedFlag;
	private int myTouchId;
	private float myX;
	private float myY;
    private TouchPanelListener myListener;

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
	
	public void fingerDown(Event event) {
		if (!fingers().isEmpty() && !myPushedFlag) {
			myPushedFlag = true;
			myTouchId = event.getId();
			myX = event.getX();
			myY = event.getY();
			event.getView().invalidate(rect());
			Log.d(getClass().getCanonicalName(), "finger down");
		}		
	}
	
	public void fingerUp(Event event) {
		if (myPushedFlag && myTouchId == event.getId()) {
			myPushedFlag = false;
			event.getView().invalidate(rect());
			Log.d(getClass().getCanonicalName(), "finger up");
		}		
	}
	
	public void fingerMove(Event event) {
		if (myPushedFlag && event.getId() == myTouchId) {
			float dx = RESPONCE * (event.getX() - myX) / width();
			float dy = RESPONCE * (event.getY() - myY) / height();
			if (abs(dx) > THRESHOLD || abs(dy) > THRESHOLD) {
				myX = event.getX();
				myY = event.getY();
				Log.d(getClass().getCanonicalName(), "finger move(" + dx + "," + dy + ")");
                myListener.onMove(this, dx, dy);
            }
		}
	}

    public void setListener(TouchPanelListener listener) {
        myListener = listener;
    }
}
