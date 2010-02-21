/*
 * Copyright 2010 Victor Volle
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.artive.visiograph;

/**
 * Created by IntelliJ IDEA. User: vivo Date: Feb 11, 2010 Time: 1:20:22 PM To change this template use File | Settings
 * | File Templates.
 */
public class GraphElement {
  private String extID;

  private String text;

  public String getExtID() {
    return extID;
  }

  public void setExtID(String extID) {
    this.extID = extID;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }


  protected GraphElement(String extID, String text) {
    this.extID = extID;
    this.text = text;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof GraphElement)) return false;

    GraphElement that = (GraphElement) o;

    //noinspection RedundantIfStatement
    if (!extID.equals(that.extID)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return extID.hashCode();
  }

  @Override
  public String toString() {
    return text + " [" + extID + "]";
  }
}
