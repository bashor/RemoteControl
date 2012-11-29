package ru.spbau.remote.controls.touchpad;

import android.view.MotionEvent;
import android.view.View;

class Event {
	private MotionEvent mySource;
	private View myView;
	private int myId;
	private int myX;
	private int myY;
	private float myPressure;
	
	Event(View view, MotionEvent source, int id) {
		for (int p = 0; p < source.getPointerCount(); ++p) {
			if (source.getPointerId(p) == myId) {
				init(view, source, id, (int)source.getX(p), (int)source.getY(p),
						source.getPressure(p));
				break;
			}
		}
	}
	
	Event(View view, MotionEvent source, int id, int x, int y, float pressure) {
		init(view, source, id, x, y, pressure);
	}
	
	private void init(View view, MotionEvent source, int id, int x, int y, float pressure) {
		mySource = source;
		myView = view;
		myId = id;
		myX = x;
		myY = y;
		myPressure = pressure;
	}
	
	MotionEvent getSourceEvent() {
		return mySource;
	}
	
	View getView() {
		return myView;
	}
	
	int getId() {
		return myId;
	}
	
	int getX() {
		return myX;
	}
	
	int getY() {
		return myY;
	}
	
	float getPressure() {
		return myPressure;
	}
}
