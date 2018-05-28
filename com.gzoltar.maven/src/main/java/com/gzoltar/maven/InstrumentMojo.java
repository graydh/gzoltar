package com.gzoltar.maven;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.instr.InstrumentationLevel;
import com.gzoltar.core.instr.Instrumenter;

@Mojo(name = "instrument", defaultPhase = LifecyclePhase.PROCESS_CLASSES, threadSafe = true)
public class InstrumentMojo extends AbstractAgentMojo {

  /**
   * {@inheritDoc}
   */
  @Override
  protected File getDestFile() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void executeMojo() throws MojoExecutionException, MojoFailureException {
    final File projectClassesDir = new File(getProject().getBuild().getOutputDirectory());
    if (!projectClassesDir.exists()) {
      getLog()
          .info("Skipping GZoltar execution due to missing classes directory:" + projectClassesDir);
      return;
    }

    final File backupDir =
        new File(getProject().getBuild().getDirectory(), "gzoltar-backup-classes");
    backupDir.mkdirs();

    // backup all files
    try {
      FileUtils.copyDirectory(projectClassesDir, backupDir);
    } catch (IOException e) {
      throw new MojoExecutionException(e.getMessage(), e);
    }

    // configure instrumentation
    AgentConfigs agentConfigs = this.createAgentConfigurations();
    agentConfigs.setInstrumentationLevel(InstrumentationLevel.OFFLINE);
    Instrumenter instrumenter = new Instrumenter(agentConfigs);

    // instrument recursively
    try {
      instrumenter.instrumentRecursively(backupDir, projectClassesDir);
    } catch (Exception e) {
      throw new MojoExecutionException(e.getMessage(), e);
    }
  }

}
