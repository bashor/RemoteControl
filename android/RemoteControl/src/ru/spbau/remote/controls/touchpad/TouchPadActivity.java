package ru.spbau.remote.controls.touchpad;

import ru.spbau.remote.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import ru.spbau.remote.WebSocketClient;
import ru.spbau.remote.settings.ApplicationSettingsSource;

import java.net.URI;

public class TouchPadActivity extends Activity implements TouchPadListener, WebSocketClient.StatusListener {
    private static final String TAG = TouchPadActivity.class.getCanonicalName();

    ApplicationSettingsSource settings;
    WebSocketClient webSocketClient = new WebSocketClient();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        Log.d(getClass().getCanonicalName(), "TouchPadActivity onCreate");

		setContentView(R.layout.touchpad);

        TouchPadView touchpad = (TouchPadView) findViewById(R.id.touchpad_id);

        touchpad.addListener(this);

        settings = new ApplicationSettingsSource(this);

        URI uri = settings.getURI();
        if (uri != null) {
            webSocketClient.connect(uri);
            webSocketClient.setStatusListener(this);
        }
	}

    @Override
    protected void onDestroy() {
        webSocketClient.close();
    }

    @Override
    public void onClickLeftButton() {
        webSocketClient.send("onClickLeftButton");
    }

    @Override
    public void onClickRightButton() {
        webSocketClient.send("onClickRightButton");
    }

    @Override
    public void onDownLeftButton() {
        webSocketClient.send("onDownLeftButton");
    }

    @Override
    public void onUpLeftButton() {
        webSocketClient.send("onUpLeftButton");
    }

    @Override
    public void onDownRightButton() {
        webSocketClient.send("onDownRightButton");
    }

    @Override
    public void onUpRightButton() {
        webSocketClient.send("onUpRightButton");
    }

    @Override
    public void onMove(float dx, float dy) {
        webSocketClient.send(String.format("onMove\tdx=%.2f\tdy=%.2f", dx, dy));
    }

    @Override
    public void onOpen() {
        Log.d(TAG, "ws opened");
    }

    @Override
    public void onClose() {
        Log.d(TAG, "ws closed");
    }

    @Override
    public void onError(String e) {
        Log.e(TAG, "ws error: " + e);
    }
}
