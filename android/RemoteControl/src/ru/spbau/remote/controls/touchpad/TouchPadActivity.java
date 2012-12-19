package ru.spbau.remote.controls.touchpad;

import ru.spbau.remote.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import ru.spbau.remote.net.WebSocketClient;
import ru.spbau.remote.settings.ApplicationSettingsSource;

import java.net.URI;

public class TouchPadActivity extends Activity
		implements TouchPadListener, WebSocketClient.StatusListener {
    ApplicationSettingsSource mySettings;
    WebSocketClient myWebSocketClient = new WebSocketClient();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        Log.d(getClass().getCanonicalName(), "TouchPadActivity onCreate");
		setContentView(R.layout.touchpad);
        TouchPadView touchpad = (TouchPadView) findViewById(R.id.touchpad_id);
        touchpad.setListener(this);
        mySettings = new ApplicationSettingsSource(this);
        URI uri = mySettings.getURI();
        if (uri != null) {
            myWebSocketClient.connect(uri);
            myWebSocketClient.setStatusListener(this);
        }
	}

    @Override
    protected void onDestroy() {
    	super.onDestroy();
        myWebSocketClient.close();
    }

    public void onDownLeftButton() {
        myWebSocketClient.send("{ \"type\":\"onDownLeftButton\" }");
    }

    public void onUpLeftButton() {
        myWebSocketClient.send("{ \"type\":\"onUpLeftButton\" }");
    }

    public void onDownRightButton() {
        myWebSocketClient.send("{ \"type\":\"onDownRightButton\" }");
    }

    public void onUpRightButton() {
        myWebSocketClient.send("{ \"type\":\"onUpRightButton\" }");
    }

    public void onMove(float dx, float dy) {
        myWebSocketClient.send(String.format("{ \"type\":\"onMove\", \"dx\":\"%.2f\", \"dy\":\"%.2f\"}", dx, dy));
    }

    public void onOpen() {
        Log.d(getClass().getCanonicalName(), "ws opened");
    }

    public void onClose() {
        Log.d(getClass().getCanonicalName(), "ws closed");
        Log.d(getClass().getCanonicalName(), "ws reconnecting...");

        mySettings = new ApplicationSettingsSource(this);
        URI uri = mySettings.getURI();
        if (uri != null) {
            myWebSocketClient.connect(uri);
        }
    }

    public void onError(Exception e) {
        Log.e(getClass().getCanonicalName(), "ws error: " + e.getLocalizedMessage());
    }
}
