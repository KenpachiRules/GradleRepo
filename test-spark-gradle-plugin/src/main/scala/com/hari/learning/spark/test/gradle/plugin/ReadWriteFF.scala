package com.hari.learning.spark.test.gradle.plugin

import org.apache.spark.sql.SparkSession

object ReadWriteFF {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder.getOrCreate
    spark.read.json("source.json").write.json("target.json")
  }

}