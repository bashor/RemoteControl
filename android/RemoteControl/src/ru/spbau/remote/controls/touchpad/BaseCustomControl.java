package ru.spbau.remote.controls.touchpad;

import java.util.LinkedList;
import java.util.List;

import ru.spbau.remote.R;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.View.OnTouchListener;

public abstract class BaseCustomControl implements OnTouchListener {
	private final List<IPointerDeviceListener> myListeners = new LinkedList<IPointerDeviceListener>();
	private final TypedArray myAttrs;
	private RectF myBorder;
	
	public BaseCustomControl(TypedArray attrs, RectF border) {
		myAttrs = attrs;
		myBorder = border;
		Log.d(getClass().getCanonicalName(), "widget created");
	}
	
	public BaseCustomControl(TypedArray attrs) {
		this(attrs, new RectF());
	}
	
	public abstract void draw(Canvas canvas);

	public void resize(RectF rect) {
		myBorder = rect;
		Log.d(getClass().getCanonicalName(), "resized to ("
				+ myBorder.width() + "," + myBorder.height() + ")");
	}
	
	public int mainButtonColor() {
		return myAttrs.getColor(R.styleable.TouchPadStyle_buttonMainColor, Color.LTGRAY);
	}
	
	public int subButtonColor() {
		return myAttrs.getColor(R.styleable.TouchPadStyle_buttonSubColor, Color.GREEN);
	}
	
	public int borderColor() {
		return myAttrs.getColor(R.styleable.TouchPadStyle_borderColor, Color.BLACK);
	}
	
	public int panelColor() {
		return myAttrs.getColor(R.styleable.TouchPadStyle_touchPanelColor, Color.LTGRAY);
	}
	
	public int borderWidth() {
		return myAttrs.getInteger(R.styleable.TouchPadStyle_borderWidth, 2);
	}
	
	public float width() {
		return myBorder.width();
	}
	
	public float height() {
		return myBorder.height();
	}
	
	public float left() {
		return myBorder.left;
	}
	
	public float top() {
		return myBorder.top;
	}
	
	public Rect rect() {
		return new Rect((int)left(), (int)top(),
				(int)(left() + width()), (int)(top() + height()));
	}
	
	public void addListener(IPointerDeviceListener listener) {
		myListeners.add(listener);
	}
	
	public void removeListener(IPointerDeviceListener listener) {
		myListeners.remove(listener);
	}
	
	public void buttonDown() {
		Log.d(getClass().getCanonicalName(), "button down");
		for (IPointerDeviceListener listener : myListeners) {
			listener.buttonDown(this);
		}
	}
	
	public void buttonUp() {
		Log.d(getClass().getCanonicalName(), "button up");
		for (IPointerDeviceListener listener : myListeners) {
			listener.buttonUp(this);
		}
	}
	
	public void move(float dx, float dy) {
		Log.d(getClass().getCanonicalName(), "move(" + dx + "," + dy + ")");
		for (IPointerDeviceListener listener : myListeners) {
			listener.move(this, dx, dy);
		}
	}
}
