package com.hari.learning.gradle.spark.plugin;

import java.net.URI;
import java.net.URISyntaxException;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;

import com.hari.learning.gradle.spark.plugin.tasks.CopyDepsTask;
import com.hari.learning.gradle.spark.plugin.tasks.LaunchSparkTask;

/**
 * Gradle plugin to build and launch a spark application , this was created out
 * of the necessity of skipping so many mundane repetitive steps to deploy a
 * spark application especially in clusters. Intends to support three modes. 1)
 * local - Drivers and executors run on the same node. 2) client-mode - Driver
 * runs in the node launching the application and executors run in the cluster
 * nodes. 3) cluster-mode - Driver runs in one of the nodes in the cluster and
 * executors run in the cluster as well.
 * 
 * @author harim
 *
 */

public class SparkPluginProject implements Plugin<Project> {

	private static final String GROUP = "Spark Runner";

	@Override
	public void apply(Project p) {
		p.getPluginManager().apply(org.gradle.api.plugins.scala.ScalaPlugin.class);
		p.getRepositories().add(p.getRepositories().mavenCentral());
		p.getRepositories().add(p.getRepositories().mavenLocal());
		p.getRepositories().add(p.getRepositories().maven(new Action<MavenArtifactRepository>() {

			@Override
			public void execute(MavenArtifactRepository customRepo) {
				try {
					customRepo.setUrl(new URI("http://repo.gradle.org/gradle/libs-releases-local"));
				} catch (URISyntaxException urise) {
					System.out.println("Incorrect URL provided ");
				}
			}

		}));
		p.getExtensions().create("settings", Settings.class);
		Task copyDeps = p.getTasks().create("copyDeps", CopyDepsTask.class);
		copyDeps.setDescription("Copies all dependencies required to run spark job");
		copyDeps.setGroup(GROUP);
		copyDeps.dependsOn("build");
		Task launch = p.getTasks().create("launch", LaunchSparkTask.class);
		launch.setDescription("Launch a spark-job with overrided settings");
		launch.setGroup(GROUP);
		launch.dependsOn(copyDeps);
	}

}
