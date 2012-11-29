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
	private static final float SCALE = 0.5f;
	private static final int WIDTH = 2;
	
	private boolean myPushedFlag;
	private int myTouchId;
	private int myX;
	private int myY;
	private float myPressure;
	private TouchPanelListener myListener;
	
    public TouchPanel(TypedArray attrs, RectF border) {
		super(attrs, border);
	}
	
	public TouchPanel(TypedArray attrs) {
		super(attrs);
	}
	
	public void draw(Canvas canvas) {
		Paint painter = new Paint();
		painter.setColor(panelMainColor());
		canvas.drawRect(rect(), painter);
		if (myPushedFlag) {
			int r = rect().height() < rect().width() ? rect().height() : rect().width();
			float scaled = SCALE * myPressure * r;
			for (int radius = (int)scaled; radius > WIDTH; radius /= 2) {
				painter.setColor(panelSubColor());
				canvas.drawCircle(myX, myY, radius, painter);
				painter.setColor(panelMainColor());
				canvas.drawCircle(myX, myY, radius - WIDTH, painter);
			}
		}
	}
	
	public void fingerDown(Event event) {
		if (!fingers().isEmpty() && !myPushedFlag) {
			myPushedFlag = true;
			myTouchId = event.getId();
			myX = event.getX();
			myY = event.getY();
			myPressure = event.getPressure();
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
			myPressure = event.getPressure();
			float dx = RESPONCE * (event.getX() - myX) / width();
			float dy = RESPONCE * (event.getY() - myY) / height();
			if (abs(dx) > THRESHOLD || abs(dy) > THRESHOLD) {
				myX = event.getX();
				myY = event.getY();
				Log.d(getClass().getCanonicalName(), "finger move(" + dx + "," + dy + ")");
                myListener.onMove(this, dx, dy);
            }
			event.getView().invalidate(rect());
		}
	}

    public void setListener(TouchPanelListener listener) {
        myListener = listener;
    }
}
