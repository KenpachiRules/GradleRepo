package com.hari.learning.gradle.spark.plugin.tasks;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;
import org.gradle.process.JavaExecSpec;

import com.hari.learning.gradle.spark.plugin.Settings;

/**
 * Task to launch spark application via SparkLauncher.
 * Depends on the "CopyDepsTask" task to download
 * all dependencies needed for driver and executor
 * classpath. SparkLauncher is then invoked by 
 * launching a separate java process and the main class
 * that would be invoked is {@link com.hari.learning.gradle.spark.plugin.tasks.LaunchMainSpark } }.
 * 
 * @author harim
 *
 */

public class LaunchSparkTask extends DefaultTask {

	private static final FilenameFilter JAR_FILTER = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".jar");
		}
	};

	@TaskAction
	public void launch() throws IOException {
		final Project p = getProject();
		Settings settings = (Settings) p.getExtensions().getByName("settings");
		String mainClass = settings.getMainClass();
		// mainClass cannot be empty.
		if (mainClass == null || mainClass.isEmpty())
			throw new IllegalArgumentException(" Main class cannot be empty");
		File libFolder = new File(p.getBuildDir().toPath().toString() + File.separator + "libs");
		File[] files = libFolder.listFiles(JAR_FILTER);
		if (files == null || files.length == 0)
			throw new IllegalArgumentException("Build failed with generating the output jar.");
		if (files.length != 1)
			throw new IllegalArgumentException("More than one version of the generated output jar");
		String sparkHome = settings.getSparkHome();
		if (sparkHome == null || sparkHome.isEmpty())
			throw new IllegalArgumentException("Spark home cannot be empty ");

		final String classPath = p.getBuildDir().toPath() + File.separator + "jobDeps" + File.separator + "*";

		File errFile = (settings.getErrRedirect() != null && !settings.getErrRedirect().isEmpty())
				? new File(settings.getErrRedirect())
				: new File(p.getBuildDir().toPath() + File.separator + "stdErr.txt");
		File outFile = (settings.getErrRedirect() != null && !settings.getErrRedirect().isEmpty())
				? new File(settings.getErrRedirect())
				: new File(p.getBuildDir().toPath() + File.separator + "stdOut.txt");
		p.javaexec(new Action<JavaExecSpec>() {
			@Override
			public void execute(JavaExecSpec spec) {
				spec.setClasspath(p.files(p.getLayout().getProjectDirectory().dir(classPath)));
				spec.args(settings.getAppName(), settings.getMaster(), files[0].toPath().toString(), mainClass,
						classPath, errFile, outFile, sparkHome);
				spec.getEnvironment().put("SPARK_SCALA_VERSION", settings.getScalaVersion());
				spec.setMain(LaunchMainSpark.class.getCanonicalName());
			}
		});
	}

}