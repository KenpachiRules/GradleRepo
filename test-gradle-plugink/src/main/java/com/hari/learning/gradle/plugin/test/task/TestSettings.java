package com.hari.learning.gradle.plugin.test.task;

/***
 * POJO to represent settings required for running spark-gradle-plugin.
 * 
 * @author harim
 *
 */

public class TestSettings {

	String userdeps;
	String sparkDeps = "org.apache.spark:spark-sql_2.11:2.4.0";
}
