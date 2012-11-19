package ru.spbau.remote.controls.touchpad;

public interface IPointerDeviceListener {
	public void buttonDown(Object button);
	public void buttonUp(Object button);
	public void move(Object panel, float dx, float dy);
}
