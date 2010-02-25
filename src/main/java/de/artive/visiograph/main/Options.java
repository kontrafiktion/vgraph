/*
 * Copyright (c) 2010, Victor Volle
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials provided
 *       with the distribution.
 *     * Neither the name of the visiograph nor the names
 *       of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written
 *       permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package de.artive.visiograph.main;

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
