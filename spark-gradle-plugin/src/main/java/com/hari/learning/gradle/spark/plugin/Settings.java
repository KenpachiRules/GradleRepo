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

	private String hadoopHome = ""; // location where hadoop related site files are placed.
	private String sparkHome;
	private String mainClass;
	private String appName = UUID.randomUUID().toString(); // provide a meaningful name if needed.
	private String master = "local[*]"; // default value is local mode.
	private String outRedirect;
	private String errRedirect;
	private String scalaVersion = "2.11";

	public String getScalaVersion() {
		return scalaVersion;
	}

	public void setScalaVersion(String scalaVersion) {
		this.scalaVersion = scalaVersion;
	}

	public String getOutRedirect() {
		return outRedirect;
	}

	public void setOutRedirect(String outRedirect) {
		this.outRedirect = outRedirect;
	}

	public String getErrRedirect() {
		return errRedirect;
	}

	public void setErrRedirect(String errRedirect) {
		this.errRedirect = errRedirect;
	}

	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getHadoopHome() {
		return hadoopHome;
	}

	public void setHadoopHome(String hadoopHome) {
		this.hadoopHome = hadoopHome;
	}

	public String getMainClass() {
		return mainClass;
	}

	public void setMainClass(String mainClass) {
		this.mainClass = mainClass;
	}

	public String getSparkHome() {
		return sparkHome;
	}

	public void setSparkHome(String sparkHome) {
		this.sparkHome = sparkHome;
	}

}
