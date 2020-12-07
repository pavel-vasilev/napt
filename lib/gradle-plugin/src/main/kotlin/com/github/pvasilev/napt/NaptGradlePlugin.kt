package com.github.pvasilev.napt

import com.github.pvasilev.napt.Constants.KT_CLASSES_DIR
import com.github.pvasilev.napt.Constants.PLUGIN_NAME
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.File

class NaptGradlePlugin : Plugin<Project> {

	companion object {
		const val JAVA = "Java"
		const val JAVA_WITH_JAVAC = "JavaWithJavac"
		const val KOTLIN = "Kotlin"
		const val APT_CONFIGURATION = "annotationProcessor"
	}

	@Suppress("UnstableApiUsage")
	override fun apply(project: Project) {
		val taskNameToOutputs = mutableMapOf<String, File>()

		project.tasks.withType(KotlinCompile::class.java).configureEach { task ->
			taskNameToOutputs[task.name] = task.outputs.files.singleFile
		}

		project.tasks.withType(JavaCompile::class.java).configureEach { task ->
			val ktCompileTask = if (task.name.endsWith(JAVA_WITH_JAVAC)) {
				task.name.replace(JAVA_WITH_JAVAC, KOTLIN)
			} else {
				task.name.replace(JAVA, KOTLIN)
			}
			val outputs = taskNameToOutputs[ktCompileTask] ?: return@configureEach
			task.options.compilerArgs.addAll(
				listOf(
					"-Xplugin:$PLUGIN_NAME",
					"-XD$KT_CLASSES_DIR=${outputs.toRelativeString(project.rootDir)}"
				)
			)
		}

		val compilerPlugin = project.dependencies.create("com.github.pvasilev:napt-compiler-plugin:1.0.0")
		project.configurations
			.filter { it.name.contains(APT_CONFIGURATION, ignoreCase = true) }
			.map { it.dependencies }
			.forEach { it.add(compilerPlugin) }
	}
}