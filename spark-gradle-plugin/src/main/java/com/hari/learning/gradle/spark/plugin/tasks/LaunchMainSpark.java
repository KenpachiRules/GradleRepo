package com.hari.learning.gradle.spark.plugin.tasks;

import static com.hari.learning.gradle.spark.plugin.tasks.LaunchSparkTask.HADOOP_HOME;
import static com.hari.learning.gradle.spark.plugin.tasks.LaunchSparkTask.HADOOP_USER_NAME;
import static com.hari.learning.gradle.spark.plugin.tasks.LaunchSparkTask.YARN_CONF_DIR;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.spark.launcher.SparkLauncher;

/**
 * The main class which would execute the SparkJob via SparkLauncher.
 * 
 * @author harim
 *
 */

public class LaunchMainSpark {

	/**
	 * @param args[0]
	 *            - Name of the spark app which is executed.
	 * @param args[1]
	 *            - Master (can be local , cluster)
	 * @param args[2]
	 *            - Jar containing the spark application to be run , added as
	 *            appResource.
	 * @param args[3]
	 *            - The class which contains the spark job representing the
	 *            application.
	 * @param args[4]
	 *            - classPath for driver and executor.
	 * @param args[5]
	 *            - File to which standard error will be re-directed to.
	 * @param args[6]
	 *            - File to which standard out will be re-directed to.
	 * @param args[7]
	 *            - Spark Home location.
	 */
	public static void main(String args[]) {
		if (args == null || args.length != 8)
			throw new IllegalArgumentException(" Not enough arguments to launch SparkLauncher ");
		String appName = args[0];
		String master = args[1];
		String jarPath = args[2];
		String mainClass = args[3];
		String classPath = args[4];
		String errFile = args[5];
		String outFile = args[6];
		String sparkHome = args[7];
		// look for if env vars YARN_CONF_DIR and HADOOP_HOME have been set
		// if so it would be needed to submit application to yarn.
		Map<String, String> envs = Environment.toMap(
				Environment.newInstance(YARN_CONF_DIR, System.getenv(YARN_CONF_DIR)),
				Environment.newInstance(HADOOP_HOME, System.getenv(HADOOP_HOME)),
				Environment.newInstance(HADOOP_USER_NAME, System.getenv(HADOOP_USER_NAME)));
		SparkLauncher launcher = new SparkLauncher(envs);
		try {
			Process launch = launcher.setAppName(appName).setMaster(master).setMainClass(mainClass)
					.setConf(SparkLauncher.DRIVER_EXTRA_CLASSPATH, classPath).setSparkHome(sparkHome)
					.setConf(SparkLauncher.EXECUTOR_EXTRA_CLASSPATH, classPath).setAppResource(jarPath)
					.redirectError(new File(errFile)).redirectOutput(new File(outFile)).setVerbose(true).launch();
			launch.waitFor();
			int exit = launch.exitValue();
			if (exit == 0)
				System.out.println("Spark job completed successfully ");
			else if (exit == 1)
				System.out.println("Spark job terminated with errors , please check stdErr.txt and stdOut.txt ");
		} catch (IOException | InterruptedException e) {
			System.out.println("LaunchMainSpark exited with an error " + e.getLocalizedMessage());
		}
	}

	private static class Environment {
		final String name;
		final String value;

		private Environment(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public static Environment newInstance(String name, String value) {
			return new Environment(name, value);
		}

		public static Map<String, String> toMap(Environment... env) {
			return asList(env).stream().filter(en -> en.name != null && en.value != null)
					.collect(Collectors.toMap(e -> e.name, e -> e.value));
		}

	}

}
