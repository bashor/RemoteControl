package ru.spbau.remote.controls.touchpad;

public interface TouchPanelListener {
    void onMove(Object panel, float dx, float dy);
}
