package com.hari.learning.spark.test.gradle.plugin

import org.apache.spark.sql.SparkSession

object ReadWriteFF {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder.getOrCreate
    spark.read.json("/tmp/test_spark_gradle_plugin/source.json").write.mode("overwrite").json("/tmp/test_spark_gradle_plugin/target1.json")
    //spark.read.json("source.json").write.mode("overwrite").json("target.json")
  }

}