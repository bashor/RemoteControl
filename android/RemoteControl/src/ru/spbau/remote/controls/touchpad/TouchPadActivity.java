package ru.spbau.remote.controls.touchpad;

import ru.spbau.remote.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import ru.spbau.remote.net.WebSocketClient;
import ru.spbau.remote.settings.ApplicationSettingsActivity;
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
        myWebSocketClient.setStatusListener(this);
        connectWC();
	}

    @Override
    protected void onDestroy() {
    	super.onDestroy();
        myWebSocketClient.close();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.touchpad_icon_menu, menu);
    	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Log.d(getClass().getCanonicalName(), "menu item selected");
    	switch (item.getItemId()) {
    	case R.id.quit:
    		Log.d(getClass().getCanonicalName(), "quit");
    		moveTaskToBack(true);
    		break;
    	case R.id.reconnect:
    		Log.d(getClass().getCanonicalName(), "reconnect");
    		disconnectWC();
    		connectWC();
    		break;
    	case R.id.settings:
    		Log.d(getClass().getCanonicalName(), "settings");
    		openSettings();
    		break;
    	}
    	return true;
    }
    
    private void connectWC() {
    	mySettings = new ApplicationSettingsSource(this);
        URI uri = mySettings.getURI();
        Log.d(getClass().getCanonicalName(), "connecting to " + uri);
        if (uri != null) {
            myWebSocketClient.connect(uri);
        }
    }
    
    private void disconnectWC() {
    	myWebSocketClient.close();
    }
    
    private void openSettings() {
    	Intent intent = new Intent(this, ApplicationSettingsActivity.class);
    	startActivity(intent);
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
        connectWC();
    }

    public void onError(Exception e) {
        Log.e(getClass().getCanonicalName(), "ws error: " + e.getLocalizedMessage());
        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}
