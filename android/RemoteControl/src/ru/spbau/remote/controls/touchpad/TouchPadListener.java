package ru.spbau.remote.controls.touchpad;

public interface TouchPadListener {

    void onClickLeftButton();
    void onClickRightButton();

    void onDownLeftButton();
    void onUpLeftButton();

    void onDownRightButton();
    void onUpRightButton();

    void onMove(float dx, float dv);
}
