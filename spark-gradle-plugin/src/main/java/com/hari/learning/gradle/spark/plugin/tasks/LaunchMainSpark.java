package com.hari.learning.gradle.spark.plugin.tasks;

import java.io.File;
import java.io.IOException;

import org.apache.spark.launcher.SparkLauncher;

/**
 * The main class which would execute the SparkJob via SparkLauncher.
 * 
 * @author harim
 *
 */

public class LaunchMainSpark {

	/**
	 * @param args[0] - Name of the spark app which is executed.
	 * @param args[1] - Master (can be local , cluster)
	 * @param args[2] - Jar containing the spark application to be run , added as appResource.
	 * @param args[3] - The class which contains the spark job representing the application.
	 * @param args[4] - classPath for driver and executor.
	 * @param args[5] - File to which standard error will be re-directed to.
	 * @param args[6] - File to which standard out will be re-directed to.
	 * @param args[7] - Spark Home location.
	 */
	public static void main(String args[]) {
		SparkLauncher launcher = new SparkLauncher();
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
		} catch (IOException io) {
			System.out.println("LaunchMainSpark exited with an error " + io.getLocalizedMessage());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
