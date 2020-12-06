package com.github.pvasilev.napt;

import com.sun.source.util.JavacTask;
import com.sun.source.util.Plugin;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.main.Main;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Options;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.github.pvasilev.napt.Constants.KT_CLASSES_DIR;
import static com.github.pvasilev.napt.Constants.PLUGIN_NAME;

public class NaptCompilerPlugin implements Plugin {

	private static final String COMPILER_MAIN_FIELD = "compilerMain";
	private static final String ARGS_FIELD = "args";

	@Override
	public String getName() {
		return PLUGIN_NAME;
	}

	@Override
	public void init(JavacTask task, String... args) {
		Context context = ((BasicJavacTask) task).getContext();
		Log logger = Log.instance(context);
		Options options = Options.instance(context);

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

			Main main = getDeclaredField(task, COMPILER_MAIN_FIELD);
			String[] arguments = getDeclaredField(task, ARGS_FIELD);
			main.processArgs(arguments, classNames);
		} catch (NoSuchFieldException | IllegalAccessException | IOException e) {
			e.printStackTrace();
		}

		logger.printRawLines("Java8 NaptCompilerPlugin");
	}

	private <T> T getDeclaredField(Object object, String name) throws NoSuchFieldException, IllegalAccessException {
		Class<?> clazz = object.getClass();
		Field field = clazz.getDeclaredField(name);
		field.setAccessible(true);
		return (T) field.get(object);
	}
}
