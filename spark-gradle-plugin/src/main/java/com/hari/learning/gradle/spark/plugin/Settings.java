package com.hari.learning.gradle.spark.plugin;

import java.util.UUID;

/**
 * Settings required for launching spark job. Contains some default values as
 * well.
 * 
 * @author harim
 *
 */

public class Settings {

	public static final String SETTINGS_EXTN = "settings";

	private String hadoopHome = ""; // location where hadoop related site files are placed.
	private String sparkHome;
	private String mainClass;
	private String appName = UUID.randomUUID().toString(); // provide a meaningful name if needed.
	private String master = "local[*]"; // default value is local mode.
	private String mode; // default values is null since in local mode (default mode) it is NA.
	private String outRedirect;
	private String errRedirect;
	private String scalaVersion = "2.11";
	private String sparkConfig; // Add or override any spark configuration if required or else we
	// fall back to defaults.
	private String jarZipDestPath = "/tmp/spark_gradle_plugin/deps"; // default path where Yarn will
	// maintain a distributed cache of spark jar dependencies.

	public String getSparkConfig() {
		return sparkConfig;
	}

	public String getScalaVersion() {
		return scalaVersion;
	}

	public String getOutRedirect() {
		return outRedirect;
	}

	public String getErrRedirect() {
		return errRedirect;
	}

	public String getMaster() {
		return master;
	}

	public String getAppName() {
		return appName;
	}

	public String getHadoopHome() {
		return hadoopHome;
	}

	public String getMainClass() {
		return mainClass;
	}

	public String getSparkHome() {
		return sparkHome;
	}

	public String getMode() {
		return mode;
	}

	public String getJarZipDestPath() {
		return jarZipDestPath;
	}

}
