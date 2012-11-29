package ru.spbau.remote.controls.touchpad;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import ru.spbau.remote.R;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public abstract class BaseCustomControl implements OnTouchListener {
	private final Set<Integer> myPointers = new HashSet<Integer>();
	private final TypedArray myAttrs;
	private RectF myBorder;
	
	public BaseCustomControl(TypedArray attrs, RectF border) {
		myAttrs = attrs;
		myBorder = border;
	}
	
	public BaseCustomControl(TypedArray attrs) {
		this(attrs, new RectF());
	}
	
	public abstract void draw(Canvas canvas);
	
	public void fingerDown(Event event) {}
	public void fingerUp(Event event) {}
	public void fingerMove(Event event) {}

	public void resize(RectF rect) {
		myBorder = rect;
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
	
	public int panelMainColor() {
		return myAttrs.getColor(R.styleable.TouchPadStyle_touchPanelMainColor, Color.LTGRAY);
	}

	public int panelSubColor() {
		return myAttrs.getColor(R.styleable.TouchPadStyle_touchPanelSubColor, Color.GREEN);
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
	
	public Set<Integer> fingers() {
		return Collections.unmodifiableSet(myPointers);
	}
	
	public boolean onTouch(View v, MotionEvent source) {
		int index = source.getActionIndex();
		Event main = new Event(v, source, source.getPointerId(index),
						(int)source.getX(index), (int)source.getY(index),
						source.getPressure(index));
		switch (source.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
			if (rect().contains(main.getX(), main.getY())) {
				myPointers.add(main.getId());
				fingerDown(main);
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_POINTER_UP:
			myPointers.remove(main.getId());
			fingerUp(main);
			break;
		case MotionEvent.ACTION_MOVE:
			for (int p = 0; p < source.getPointerCount(); ++p) {
				Event event = new Event(v, source, source.getPointerId(p),
						(int)source.getX(p), (int)source.getY(p),
						source.getPressure(p));
				if (myPointers.contains(event.getId())) {
					fingerMove(event);
					if (!rect().contains(event.getX(), event.getY())) {
						myPointers.remove(event.getId());
						fingerUp(event);
					}
				} else if (rect().contains(event.getX(), event.getY())) {
					myPointers.add(event.getId());
					fingerDown(event);
				}
			}
		}
		return true;
	}
}
