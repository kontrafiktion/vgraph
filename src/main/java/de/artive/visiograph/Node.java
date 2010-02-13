package de.artive.visiograph;

/**
 * Created by IntelliJ IDEA.
 * User: vivo
 * Date: Feb 11, 2010
 * Time: 1:15:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class Node extends GraphElement {
    private VisioRectangle visioRectangle;

    protected Node(String extID, String text) {
        super(extID, text);
    }

    public VisioRectangle getVisioRectangle() {
        return visioRectangle;
    }

    public void setVisioRectangle(VisioRectangle visioRectangle) {
        this.visioRectangle = visioRectangle;
    }
}
