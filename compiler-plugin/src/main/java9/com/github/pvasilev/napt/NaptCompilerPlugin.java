package com.github.pvasilev.napt;

import com.sun.source.util.JavacTask;
import com.sun.source.util.Plugin;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.main.Arguments;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Options;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static com.github.pvasilev.napt.Constants.KT_CLASSES_DIR;
import static com.github.pvasilev.napt.Constants.PLUGIN_NAME;

public class NaptCompilerPlugin implements Plugin {

	@Override
	public String getName() {
		return PLUGIN_NAME;
	}

	@Override
	public void init(JavacTask task, String... args) {
		Context context = ((BasicJavacTask) task).getContext();
		Log logger = Log.instance(context);
		Options options = Options.instance(context);
		Arguments arguments = Arguments.instance(context);

		Path rootDir = new File(options.get(KT_CLASSES_DIR)).toPath();

		try {
			String[] classNames = Files.walk(rootDir, Integer.MAX_VALUE)
				.filter(path -> !path.toFile().isDirectory())
				.map(rootDir::relativize)
				.map(Path::toString)
				.map(path -> {
					String separator = System.getProperty("file.separator");
					String delimiter = ".";
					return path.substring(0, path.lastIndexOf(delimiter)).replaceAll(separator, delimiter);
				})
				.toArray(String[]::new);
			arguments.getClassNames().addAll(Arrays.asList(classNames));
		} catch (IOException e) {
			e.printStackTrace();
		}

		logger.printRawLines("Java9 NaptCompilerPlugin");
	}
}
