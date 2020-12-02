package com.github.pvasilev.napt;

import com.sun.source.util.JavacTask;
import com.sun.source.util.Plugin;

import static com.github.pvasilev.napt.ConstantsKt.PLUGIN_NAME;

public class NaptCompilerPlugin implements Plugin {

	@Override
	public String getName() {
		return PLUGIN_NAME;
	}

	@Override
	public void init(JavacTask task, String... args) {
	}
}
