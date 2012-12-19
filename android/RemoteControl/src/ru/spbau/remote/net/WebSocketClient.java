package ru.spbau.remote.net;

import java.io.Closeable;
import java.net.URI;

import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.WebSocket;

public class WebSocketClient implements Closeable {
    private org.java_websocket.client.WebSocketClient myWebSocketClient;
    private MessageListener myMessageListener;
    private StatusListener myStatusListener;

    public void connect(URI uri) {
        myWebSocketClient = new org.java_websocket.client.WebSocketClient(uri) {
            public void onOpen(ServerHandshake serverHandshake) {
                if (myStatusListener != null) {
                    myStatusListener.onOpen();
                }
            }

            public void onMessage(String message) {
                if (myMessageListener != null) {
                    myMessageListener.onMessage(message);
                }
            }

            public void onClose(int i, String s, boolean b) {
                if (myStatusListener != null) {
                    myStatusListener.onClose();
                }
            }

            public void onError(Exception e) {
                if (myStatusListener != null) {
                    myStatusListener.onError(e);
                }
            }
        };
        if (myWebSocketClient != null) {
        	myWebSocketClient.connect();
        }
    }

    public void close() {
        try {
        	if (myWebSocketClient != null && (myWebSocketClient.getReadyState() == WebSocket.READY_STATE_OPEN)) {
        		myWebSocketClient.close();
        	}
        } catch (Exception e) {
        	if (myStatusListener != null) {
        		myStatusListener.onError(e);
        	}
        }
    }

    public void send(String message) {
    	try {
    		if (myWebSocketClient != null && (myWebSocketClient.getReadyState() == WebSocket.READY_STATE_OPEN)) {
    			myWebSocketClient.send(message);
    		}
        } catch (Exception e) {
        	if (myStatusListener != null) {
        		myStatusListener.onError(e);
        	}
        }
    }

    public void setMessageListener(MessageListener messageListener) {
        myMessageListener = messageListener;
    }

    public void setStatusListener(StatusListener statusListener) {
        myStatusListener = statusListener;
    }

    public interface MessageListener {
        void onMessage(String message);
    }

    public interface StatusListener {
        public void onOpen();
        public void onClose();
        public void onError(Exception e);
    }
}
