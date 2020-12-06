package com.github.pvasilev.napt.sample;

public class AppComponentProvider {

	public static AppComponent getComponent() {
		return DaggerAppComponent.create();
	}
}
