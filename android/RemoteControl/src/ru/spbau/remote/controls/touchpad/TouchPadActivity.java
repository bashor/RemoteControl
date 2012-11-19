package ru.spbau.remote.controls.touchpad;

import ru.spbau.remote.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class TouchPadActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.touchpad);
		Log.d(getClass().getCanonicalName(), "TouchPadActivity onCreate");
	}
}
