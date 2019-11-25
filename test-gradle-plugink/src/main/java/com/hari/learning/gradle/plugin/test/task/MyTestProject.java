package com.hari.learning.gradle.plugin.test.task;

import javax.inject.Inject;

import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.file.CopySpec;
import org.gradle.api.tasks.TaskAction;

public class MyTestProject implements Plugin<Project> {

	@Override
	public void apply(Project p) {
		// apply scala plugin.
		p.getPluginManager().apply(org.gradle.api.plugins.JavaPlugin.class);
		p.getExtensions().create("testSettings", TestSettings.class);
		final RepositoryHandler repoHandler = p.getRepositories();
		repoHandler.add(repoHandler.mavenCentral());
		// create a configuration from the custom extensions
		Task copyDeps = p.getTasks().create("copyDeps", CopyTask.class);
		copyDeps.setDescription("Downloads dependencies for spark-job to run");
		copyDeps.setGroup("sparkRunner");
		copyDeps.dependsOn("jar");
		p.getTasks().add(copyDeps);
	}

	static class CopyTask extends DefaultTask {

		@Inject
		public CopyTask() {
		}

		@TaskAction
		void copyDeps() {
			// create a copy action.
			final Project p = getProject();
			TestSettings testSets = (TestSettings) p.getExtensions().findByName("testSettings");
			final Configuration depsConfig = p.getConfigurations().create("sparkDeps");
			final Configuration userDepsConfig = p.getConfigurations().create("userDeps");
			p.getDependencies().add(depsConfig.getName(), testSets.sparkDeps);
			p.getDependencies().add(userDepsConfig.getName(), testSets.userdeps);
			depsConfig.setTransitive(true);
			userDepsConfig.setTransitive(true);
			p.copy(new Action<CopySpec>() {
				@Override
				public void execute(CopySpec copySpec) {
					copySpec.into(p.getBuildDir());
					copySpec.from(depsConfig, userDepsConfig);
				}
			});
		}
	}

}
