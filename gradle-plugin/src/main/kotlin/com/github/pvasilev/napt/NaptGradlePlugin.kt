package com.github.pvasilev.napt

import com.github.pvasilev.napt.Constants.PLUGIN_NAME
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile

class NaptGradlePlugin : Plugin<Project> {

	companion object {
		const val COMPILE_ONLY_CONFIGURATION = "compileOnly"
	}

	@Suppress("UnstableApiUsage")
	override fun apply(project: Project) {
		project.tasks.withType(JavaCompile::class.java).configureEach { task ->
			task.options.compilerArgs.add("-Xplugin:$PLUGIN_NAME")
		}

		val compileOnlyDeps = project.configurations.getByName(COMPILE_ONLY_CONFIGURATION).dependencies
		val compilerPlugin = project.dependencies.create("com.github.pvasilev:napt-compiler-plugin:1.0.0")
		compileOnlyDeps.add(compilerPlugin)
	}
}