package com.hari.learning.gradle.spark.plugin.tasks;

import java.io.File;

import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.file.CopySpec;
import org.gradle.api.tasks.TaskAction;

/**
 * Copies spark and other required dependencies into
 * {@literal "build/sparkDeps"}
 * 
 * @author harim
 *
 */

public class CopyDepsTask extends DefaultTask {

	public static final String JOB_DEPS_FILE_SUFFIX = "jobDeps";

	@TaskAction
	public void copyDep() {
		Project p = getProject();
		// download all compile time dependencies into folder ${BUILD_DIR/jobDeps}
		final Configuration deps = p.getConfigurations().getByName("compile");
		p.copy(new Action<CopySpec>() {
			@Override
			public void execute(CopySpec copySpec) {
				copySpec.into(p.getBuildDir().toPath().toString() + File.separator + JOB_DEPS_FILE_SUFFIX);
				copySpec.from(deps);
			}
		});
	}

}
