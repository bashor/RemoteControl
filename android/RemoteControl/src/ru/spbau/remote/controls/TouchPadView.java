package ru.spbau.remote.controls;

import ru.spbau.remote.R;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

/**
 * Draws touchpad and coolects touchpad events.
 * 
 * TODO: now touchpad is panel and two push buttons, it's work,
 * but there are some issues, for example, we cannot move cursor with
 * button pushed, because if gesture begins under button (panel), then
 * only button (panel) receives touch (multitouch) events. Possible solution
 * is do not use standard buttons and draw our own buttons with panel.
 * 
 * @version 1.0
 * @author kmu
 */
public class TouchPadView extends FrameLayout implements PositionListener {
	private TouchPadPanel myPanel;
	private Button myLeftButton;
	private Button myRightButton;

	public TouchPadView(Context context) {
		super(context);
		init(context);
	}
	
	public TouchPadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public TouchPadView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View child = inflater.inflate(R.layout.touchpad, null);
		addView(child);
		myPanel = (TouchPadPanel) child.findViewById(R.id.touchpad_panel);
		myPanel.addListener(this);
		
		myLeftButton = (Button) child.findViewById(R.id.left_button);
		myLeftButton.setOnTouchListener(new OnTouchListener() {	
			public boolean onTouch(View v, MotionEvent event) {
				v.onTouchEvent(event);
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					Log.d(this.getClass().getName(), "left button down");
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					Log.d(this.getClass().getName(), "left button up");
				}
				return true;
			}
		});
		
		myRightButton = (Button) child.findViewById(R.id.right_button);
		myRightButton.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				v.onTouchEvent(event);
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					Log.d(this.getClass().getName(), "right button down");
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					Log.d(this.getClass().getName(), "right button up");
				}
				return true;
			}
		});
	}

	public void positionEvent(float x, float y) {
		Log.d(this.getClass().getName(), "move (" + x + "," + y + ")");
	}
}
