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

package de.artive.vgraph;

import de.artive.vgraph.graph.*;
import de.artive.vgraph.helper.XmlHelper;
import de.artive.vgraph.visio.*;
import nu.xom.Document;
import nu.xom.Element;

import java.math.*;
import java.util.*;

import static de.artive.vgraph.helper.VisioHelper.*;

/**
 * Created by IntelliJ IDEA. User: vivo Date: Feb 14, 2010 Time: 6:19:54 PM To change this template use File | Settings
 * | File Templates.
 */
public class Layout {

  // All sizes in Millimeter
  public static BigDecimal ONE_BORDER = new BigDecimal("20");
  public static BigDecimal BORDER = ONE_BORDER.multiply(TWO);
  public static BigDecimal NODE_WIDTH = new BigDecimal("15");
  public static BigDecimal NODE_HEIGHT = new BigDecimal("10");
  public static BigDecimal NODE_WIDTH_HALF = divideByTwo(NODE_WIDTH);
  public static BigDecimal NODE_HEIGHT_HALF = divideByTwo(NODE_HEIGHT);

  public static final BigDecimal X_FACTOR = new BigDecimal("4").divide(new BigDecimal("3"), MathContext.DECIMAL128);
  public static final BigDecimal Y_FACTOR = new BigDecimal("3").divide(new BigDecimal("4"), MathContext.DECIMAL128);
  private static BigDecimal ySpaceHalf;
  private static BigDecimal xSpaceHalf;


  public static final String DELETED_LAYER_XML =
      "<Layer xmlns=\"http://schemas.microsoft.com/visio/2003/core\" IX=\"1\">\n"
      + "    <Name>Deleted</Name>\n"
      + "    <Color>2</Color>\n"
      + "    <Status>0</Status>\n"
      + "    <Visible>1</Visible>\n"
      + "    <Print>1</Print>\n"
      + "    <Active>0</Active>\n"
      + "    <Lock>0</Lock>\n"
      + "    <Snap>1</Snap>\n"
      + "    <Glue>1</Glue>\n"
      + "    <NameUniv>Deleted</NameUniv>\n"
      + "    <ColorTrans>0</ColorTrans>\n"
      + "</Layer>\n";
  public static final String NEW_LAYER_XML =
      "<Layer xmlns=\"http://schemas.microsoft.com/visio/2003/core\" IX=\"2\">\n"
      + "    <Name>New</Name>\n"
      + "    <Color>9</Color>\n"
      + "    <Status>0</Status>\n"
      + "    <Visible>1</Visible>\n"
      + "    <Print>1</Print>\n"
      + "    <Active>0</Active>\n"
      + "    <Lock>0</Lock>\n"
      + "    <Snap>1</Snap>\n"
      + "    <Glue>1</Glue>\n"
      + "    <NameUniv>New</NameUniv>\n"
      + "    <ColorTrans>0</ColorTrans>\n"
      + "</Layer>";
  private static final String LAYER_IX = "IX";
  private static final String _L_NAME = "./v:Name";
  private static final String DELETED_LAYER_NAME = "Deleted";
  private static final String NEW_LAYER_NAME = "New";


  public void layout(VisioDocument visioDocument, Graph graph, boolean merge) {

    List<Node> nodesToLayout = new ArrayList<Node>(64);
    List<Edge> edgesToLayout = new ArrayList<Edge>(64);


    for (Node node : graph.getNodes()) {
      if (node.getVisioRectangle() == null) {
        nodesToLayout.add(node);
      }
    }

    layoutNodes(visioDocument, nodesToLayout, merge);

    for (Edge edge : graph.getEdges()) {
      if (edge.getVisioConnector() == null) {
        edgesToLayout.add(edge);
      }
    }

    layoutEdges(graph, visioDocument, edgesToLayout, merge);

  }

  private void layoutEdges(Graph graph, VisioDocument visioDocument, List<Edge> edgesToLayout, boolean merge) {
    for (Edge edge : edgesToLayout) {
      Node source = edge.getSource();
      Node target = edge.getTarget();

      BigDecimal sourcePinX = inch2MM(source.getVisioRectangle().getPinX());
      BigDecimal sourcePinY = inch2MM(source.getVisioRectangle().getPinY());
      BigDecimal targetPinX = inch2MM(target.getVisioRectangle().getPinX());
      BigDecimal targetPinY = inch2MM(target.getVisioRectangle().getPinY());

      BigDecimal xDelta = targetPinX.subtract(sourcePinX);
      // BigDecimal xSign = new BigDecimal(xDelta.signum());
      int ySign = targetPinY.subtract(sourcePinY).signum();

      BigDecimal beginX = sourcePinX;
      BigDecimal endX = targetPinX;

      BigDecimal width = endX.subtract(beginX); // yes, this is the same as xDelta 


      // (0,0) is in the lower left corner,
      // => if the target is at the right of the source, xSign is positive

      // => if the target is ABOVE the source, ySign is positive
      //   => beginY must be sourceY + NODE_HEIGHT_HALF
      // => if the target is BELOW the source, ySign is negative
      //   => beginY must be sourceY - NODE_HEIGHT_HALF


      BigDecimal beginY;
      BigDecimal endY;
      BigDecimal height;

      if (ySign != 0) { // different rows
        BigDecimal yDirectionMultiplier = new BigDecimal(ySign);

        beginY = sourcePinY.add(NODE_HEIGHT_HALF.multiply(yDirectionMultiplier));
        endY = targetPinY.add(NODE_HEIGHT_HALF.multiply(yDirectionMultiplier.negate()));

        height = targetPinY.subtract(sourcePinY);
      } else { // same row
        beginY = sourcePinY.add(NODE_HEIGHT_HALF);
        endY = beginY;
        height = VISIO_MINIMAL_SIZE;
      }
      BigDecimal widthHalf = divideByTwo(width);
      BigDecimal heightHalf = divideByTwo(height);

      VisioConnector visioConnector =
          new VisioConnector(mm2Inch(beginX),
                             mm2Inch(endX),
                             mm2Inch(beginY),
                             mm2Inch(endY),
                             edge.getExtID(),
                             edge.getText());


      if (merge) {
        markNew(visioDocument, visioConnector);
      }

      edge.setVisioConnector(visioConnector);

      // draw line segments
      // ------------------
      BigDecimal yDelta;
      BigDecimal yEnd;
      if (ySign != 0) { // different rows
        yDelta = heightHalf;
        yEnd = endY.subtract(beginY);
      } else { // same rows
        yDelta = ySpaceHalf;
        yEnd = VISIO_Y_DIST_MM;
      }

      System.out.print(edge.getText() + " (" + beginX + ", " + beginY + ")");
      visioConnector.addLine(ZERO,
                             mm2Inch(yDelta));
      visioConnector.addLine(mm2Inch(width),
                             mm2Inch(yDelta));
      visioConnector.addLine(mm2Inch(width),
                             mm2Inch(yEnd));
      System.out.println();

      visioDocument.addShape(visioConnector);
      visioDocument.addConnect(visioConnector.getVisioID(),
                               source.getVisioRectangle().getVisioID(),
                               target.getVisioRectangle().getVisioID());

    }
  }


  private void layoutNodes(VisioDocument visioDocument, List<Node> nodesToLayout, boolean merge) {

    BigDecimal width = visioDocument.getPageWidth();
    BigDecimal height = visioDocument.getPageHeight();
    BigDecimal widthWithoutBorder = width.subtract(BORDER);
    BigDecimal heightWithoutBorder = height.subtract(BORDER);

    int size = nodesToLayout.size();
    if (size > 0) {
      int columns = nodesOnAxis(X_FACTOR, size);
      int rows = nodesOnAxis(Y_FACTOR, size);
      int x = 0;
      int y = 0;
      int rest = size;

      BigDecimal columnsBD = new BigDecimal(columns);
      BigDecimal rowsBD = new BigDecimal(rows);
      BigDecimal xSpace = (widthWithoutBorder.subtract(NODE_WIDTH.multiply(columnsBD))).divide(columnsBD,
                                                                                               MathContext.DECIMAL32);
      xSpaceHalf = divideByTwo(xSpace);
      BigDecimal ySpace = (heightWithoutBorder.subtract(NODE_HEIGHT.multiply(rowsBD))).divide(rowsBD,
                                                                                              MathContext.DECIMAL32);
      ySpaceHalf = divideByTwo(ySpace);
      BigDecimal xPos;
      BigDecimal yPos = null;

      System.out.println("xSpace: " + xSpace.toPlainString() + " ySpace: " + ySpace.toPlainString());

      Collections.sort(nodesToLayout, new Comparator<Node>() {
        @Override
        public int compare(Node n1, Node n2) {
          String n1Text = (n1.getText() == null ? "" : n1.getText());
          String n2Text = (n2.getText() == null ? "" : n2.getText());
          return n1Text.compareTo(n2Text);
        }
      });

      for (Node node : nodesToLayout) {

        // if there are less nodes than calculated columns (only happens in the last row)
        // set the number of columns to the number of remaining nodes (the "rest")
        if (x == 0 && rest < columns) {
          columns = rest;

          // recalculate the horizontal space
          columnsBD = new BigDecimal(columns);
          xSpace = (widthWithoutBorder.subtract(NODE_WIDTH.multiply(columnsBD))).divide(columnsBD,
                                                                                        MathContext.DECIMAL32);
          xSpaceHalf = divideByTwo(xSpace);
        }
        xPos = (new BigDecimal(x).multiply(NODE_WIDTH.add(xSpace))).add(xSpaceHalf)
            .add(NODE_WIDTH_HALF)
            .add(ONE_BORDER);
        if (yPos == null) {
          yPos = height.subtract(ONE_BORDER).subtract(ySpaceHalf);
        }

        // TODO: calculate everything in Inches?
        VisioRectangle visioRectangle = new VisioRectangle(
            mm2Inch(xPos),
            mm2Inch(yPos),
            mm2Inch(NODE_WIDTH),
            mm2Inch(NODE_HEIGHT),
            node.getExtID(), node.getText());

        // TODO: Necessary?
        node.setVisioRectangle(visioRectangle);
        visioDocument.addShape(visioRectangle);

        if (merge) {
          markNew(visioDocument, visioRectangle);
        }

        System.out.print(node.getExtID() + " (" + xPos.toPlainString() + "," + yPos.toPlainString() + ") ");
        rest--;
        x++;

        // last element in the row?
        if (x >= columns) {
          x = 0;
          y++;
          System.out.println();
          yPos = yPos.subtract(NODE_HEIGHT).subtract(ySpace);
        }
      }

    }

  }

  public void markDeleted(VisioDocument visioDocument, VisioShape shape) {
    moveToLayer(visioDocument, shape, DELETED_LAYER_NAME, DELETED_LAYER_XML);
  }

  private void moveToLayer(VisioDocument visioDocument, VisioShape shape, String layerName, String layerXml) {
    String layerId = visioDocument.getLayerId(layerName);
    if (layerId == null) {
      Document layer = XmlHelper.visioBuild(layerXml);
      layerId = visioDocument.addLayer((Element) layer.getRootElement().copy());
    }
    shape.setLayer(layerId);
  }

  public void markNew(VisioDocument visioDocument, VisioShape shape) {
    moveToLayer(visioDocument, shape, NEW_LAYER_NAME, NEW_LAYER_XML);
  }

  private static int nodesOnAxis(BigDecimal factor, int size) {
    double xVal = Math.sqrt(factor.multiply(new BigDecimal(size)).doubleValue());
    return new BigDecimal(xVal).setScale(0, RoundingMode.CEILING).intValue();
  }


}
