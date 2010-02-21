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
