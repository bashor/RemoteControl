package ru.spbau.remote.controls.touchpad;

interface TouchPadListener {

    void onDownLeftButton();
    void onUpLeftButton();

    void onDownRightButton();
    void onUpRightButton();

    void onMove(float dx, float dv);
}
