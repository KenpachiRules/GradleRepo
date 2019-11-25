package com.hari.learning.gradle.spark.plugin.tasks;

import static com.hari.learning.gradle.spark.plugin.Settings.SETTINGS_EXTN;
import static com.hari.learning.gradle.spark.plugin.SparkRunMode.getRunMode;
import static com.hari.learning.gradle.spark.plugin.tasks.CopyDepsTask.JOB_DEPS_FILE_SUFFIX;
import static com.hari.learning.gradle.spark.plugin.tasks.LaunchSparkTask.JAR_FILTER;
import static java.util.Arrays.asList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;

import com.hari.learning.gradle.spark.plugin.Settings;
import com.hari.learning.gradle.spark.plugin.SparkRunMode;

public class PrepareForClusterSubmit extends DefaultTask {

	static final String YARN_LIB_ZIP_FILE = "yarn_libs.zip";
	static final FilenameFilter SITE_FILE_FILTER = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String file) {
			return file.endsWith(".xml");
		}
	};

	@TaskAction
	public void zipAndUploadToCluster() throws Exception {
		final Project p = getProject();
		Settings settings = (Settings) p.getExtensions().getByName(SETTINGS_EXTN);

		final SparkRunMode runMode = getRunMode.apply(settings.getMaster()).apply(settings.getMode());
		if (runMode == SparkRunMode.YARN_CLIENT || runMode == SparkRunMode.YARN_CLUSTER) {
			// Require HADOOP_CONF_DIR to be set to run in cluster.
			if (settings.getHadoopHome() == null || settings.getHadoopHome().length() == 0)
				throw new IllegalArgumentException("Invalid hadoopHome property value");
			// compress all the needed jars into a zip file before uploading it to cluster.
			// the required jars are not more than what is required for driver/executor
			// classpath. The zip file would be created under ${PROJECT_BUILD_DIR} as
			// ${YARN_LIB_ZIP_FILE}
			final String hadoopHome = settings.getHadoopHome();
			final String classPath = p.getBuildDir().toPath() + File.separator + JOB_DEPS_FILE_SUFFIX + File.separator;
			final String zipPath = p.getBuildDir().toPath().toString() + File.separator + YARN_LIB_ZIP_FILE;
			final String destJarPath = settings.getJarZipDestPath();
			try (final ZipOutputStream zipOS = new ZipOutputStream(new FileOutputStream(zipPath))) {
				List<File> sparkJars = asList(new File(classPath).listFiles(JAR_FILTER));
				sparkJars.forEach(jar -> {
					try {
						zipOS.putNextEntry(new ZipEntry(jar.toPath().toString()));
						byte[] bytes = Files.readAllBytes(Paths.get(jar.toURI()));
						zipOS.write(bytes, 0, bytes.length);
						zipOS.closeEntry();
					} catch (IOException io) {
						System.out.println("Failed while creating the yarn_libs zip file. ");
					}
				});
			}
			// Post successful creation of archive file it needs to be uploaded to HDFS
			if (!Files.exists(Paths.get(zipPath)))
				throw new FileNotFoundException("yarn_libs not found , hence failing the task");

			int result = ToolRunner.run(new HDFSCopier(), new String[] { hadoopHome, zipPath, destJarPath });
			if (result != 0) {
				System.out.println("Failed to copy yarn_libs zip to HDFS , hence failing the task");
				throw new RuntimeException("HDFS copy operation failed with exit code" + result);
			}

		}

	}

	/**
	 * {@link org.apache.hadoop.util.Tool} implementation for copying spark deps as
	 * zipped file from local FS to Hadoop FS. Arguments passed would be args[0] -
	 * Hadoop Home args[1] - Input path in local FS. args[2] - Output path in remote
	 * FS.
	 * 
	 */

	class HDFSCopier extends Configured implements Tool {

		@Override
		public int run(String[] args) throws Exception {
			final Configuration hadoopConf = new Configuration();
			String hadoopHome = args[0];
			String inputPath = args[1];
			String outPath = args[2];
			File siteFiles = new File(hadoopHome);
			asList(siteFiles.listFiles(SITE_FILE_FILTER)).stream()
					.forEach(site -> hadoopConf.addResource(site.toPath().toString()));
			final FileSystem hadoopFs = FileSystem.get(hadoopConf);
			OutputStream os = hadoopFs.create(new Path(outPath));
			InputStream is = new FileInputStream(new File(inputPath));
			IOUtils.copyBytes(is, os, hadoopConf);
			// close all i/o resources.
			is.close();
			os.close();
			hadoopFs.close();
			return 0;
		}

	}
}
