package com.gzoltar.maven;

import java.util.Locale;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;

/**
 * Base class for creating a fault localization report for tests of a single project in a defined
 * formats (e.g., TXT, and HTML).
 */
public abstract class AbstractReportMojo extends AbstractMavenReport {

  /**
   * Maven project.
   */
  @Parameter(property = "project", readonly = true)
  private MavenProject project;

  /**
   * Fault localization report format. Valid options are:
   * <ul>
   * <li>txt: text based fault localization reports (default)</li>
   * <li>html: graphical fault localization reports in HTML</li>
   * </ul>
   */
  @Parameter(property = "gzoltar.format", defaultValue = "txt")
  private String format;

  @Override
  public String getDescription(final Locale locale) {
    return this.getName(locale) + " Fault Localization Report.";
  }

  @Override
  public String getName(Locale locale) {
    return "GZoltar";
  }

  @Override
  public String getOutputName() {
    return "gzoltar/index";
  }

  @Override
  protected MavenProject getProject() {
    return this.project;
  }

  @Override
  public boolean canGenerateReport() {
    if (!this.canGenerateReportRegardingDataFiles()) {
      getLog().info("Skipping GZoltar execution due to missing execution data file.");
      return false;
    }
    return true;
  }

  protected abstract boolean canGenerateReportRegardingDataFiles();

  @Override
  public void execute() throws MojoExecutionException {
    if (!this.canGenerateReport()) {
      return;
    }

    try {
      this.executeReport(Locale.getDefault());
    } catch (final MavenReportException e) {
      throw new MojoExecutionException(
          "An error has occurred in " + this.getName(Locale.ENGLISH) + " report generation.", e);
    }
  }
}
