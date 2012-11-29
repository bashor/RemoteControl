package ru.spbau.remote;

import org.java_websocket.handshake.ServerHandshake;

import java.io.Closeable;
import java.net.URI;

public class WebSocketClient implements Closeable {
    private org.java_websocket.client.WebSocketClient webSocketClient;
    private MessageListener messageListener;
    private StatusListener statusListener;

    public void connect(URI uri) {
        webSocketClient = new org.java_websocket.client.WebSocketClient(uri) {
            public void onOpen(ServerHandshake serverHandshake) {
                if (statusListener != null) {
                    statusListener.onOpen();
                }
            }

            public void onMessage(String message) {
                if (messageListener != null) {
                    messageListener.onMessage(message);
                }
            }

            public void onClose(int i, String s, boolean b) {
                if (statusListener != null) {
                    statusListener.onClose();
                }
                webSocketClient = null;
            }

            public void onError(Exception e) {
                if (statusListener != null) {
                    statusListener.onError(e.getLocalizedMessage());
                }
            }
        };

        webSocketClient.connect();
    }

    public void close() {
        if (webSocketClient != null) {
            webSocketClient.close();
            webSocketClient = null;
        }
        //else todo exception?
    }

    public void send(String message) {
        if (webSocketClient != null) {
            webSocketClient.send(message);
        }
        //else todo exception?
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public void setStatusListener(StatusListener statusListener) {
        this.statusListener = statusListener;
    }

    public interface MessageListener {
        void onMessage(String message);
    }

    public interface StatusListener {
        public void onOpen();
        public void onClose();
        public void onError(String errorMessage);
    }
}
