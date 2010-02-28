/*
 * Copyright (c) 2010 Victor Volle.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * Contributors:
 *      Victor Volle
 */

package de.artive.vgraph.main;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import java.util.List;

/**
 * Created by IntelliJ IDEA. User: vivo Date: Feb 20, 2010 Time: 6:33:17 PM To change this template use File | Settings
 * | File Templates.
 */
public class Options {

  @Option(name = "-l", aliases = {"--loader"}, metaVar = "<loader>", usage = "the class of the source loader")
  private String loaderName;

  @Option(name = "-c",
          aliases = {"--conf"},
          metaVar = "<configuration string>",
          usage = "a configuration for the source loader (depends on the\nloader implementation)")
  private String loaderConf;

  @Option(name = "-t", aliases = {"--template"}, metaVar = "<template>", usage = "the Visio (TM) template to be used")
  private String template;

  @Option(name = "-f", aliases = {"--force"}, usage = "overwrite the target instead of merging")
  private boolean force;

  @Option(name = "-h", aliases = {"--help"}, usage = "display help")
  private boolean help;

  @Option(name = "-v", aliases = {"--version"}, usage = "display the version")
  private boolean version;

  @Argument
  private List<String> argument;

  public String getLoaderName() {
    return loaderName;
  }

  public void setLoaderName(String loaderName) {
    this.loaderName = loaderName;
  }

  public String getLoaderConf() {
    return loaderConf;
  }

  public void setLoaderConf(String loaderConf) {
    this.loaderConf = loaderConf;
  }

  public String getTemplate() {
    return template;
  }

  public void setTemplate(String template) {
    this.template = template;
  }

  public boolean isForce() {
    return force;
  }

  public void setForce(boolean force) {
    this.force = force;
  }

  public boolean isHelp() {
    return help;
  }

  public void setHelp(boolean help) {
    this.help = help;
  }

  public boolean isVersion() {
    return version;
  }

  public void setVersion(boolean version) {
    this.version = version;
  }

  public List<String> getArgument() {
    return argument;
  }

  public void setArgument(List<String> argument) {
    this.argument = argument;
  }


}
