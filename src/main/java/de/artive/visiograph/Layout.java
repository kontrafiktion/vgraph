package de.artive.visiograph;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: vivo Date: Feb 14, 2010 Time: 6:19:54 PM To change this template use File | Settings
 * | File Templates.
 */
public class Layout {

    private static final BigDecimal TWO = new BigDecimal("2");

    // All sizes in Millimeter
    public static BigDecimal ONE_BORDER = new BigDecimal("20");
    public static BigDecimal BORDER = ONE_BORDER.multiply(TWO);
    public static BigDecimal NODE_WIDTH = new BigDecimal("15");
    public static BigDecimal NODE_HEIGHT = new BigDecimal("10");
    public static BigDecimal NODE_WIDTH_HALF = NODE_WIDTH.divide(TWO);
    public static BigDecimal NODE_HEIGHT_HALF = NODE_HEIGHT.divide(TWO);

    public static final BigDecimal X_FACTOR = new BigDecimal("4").divide(new BigDecimal("3"), MathContext.DECIMAL128);
    public static final BigDecimal Y_FACTOR = new BigDecimal("3").divide(new BigDecimal("4"), MathContext.DECIMAL128);

    public void layout(VisioDocumentTemplate visioDocumentTemplate, Graph graph) {

        List<Node> nodesToLayout = new ArrayList<Node>(64);


        for (Node node : graph.getNodes()) {
            if (node.getVisioRectangle() == null) {
                nodesToLayout.add(node);
            }
        }

        BigDecimal width = visioDocumentTemplate.getPageWidth().subtract(BORDER);
        BigDecimal height = visioDocumentTemplate.getPageHeight().subtract(BORDER);

        layout(nodesToLayout, width, height);

    }

    public static void main(String[] args) {
        List<Node> nodes = new ArrayList<Node>(100);
        for (int outer = 0; outer < 1; outer ++ ) {
            for (int inner = 0; inner < 30; inner ++) {
                nodes.add(new Node(String.valueOf(outer) + "_" + String.valueOf(inner), ""));
            }
            System.out.println("================================================================================");
            layout(nodes, new BigDecimal("290").subtract(BORDER), new BigDecimal("217").subtract(BORDER));
            System.out.println("================================================================================");
        }
    }

    private static void layout(List<Node> nodesToLayout, BigDecimal width, BigDecimal height) {
        int size = nodesToLayout.size();
        if (size > 0) {
            int columns = nodesOnAxis(X_FACTOR, size);
            int rows = nodesOnAxis(Y_FACTOR, size);
            int x = 0;
            int y = 0;
            int rest = size;

            BigDecimal columnsBD = new BigDecimal(columns);
            BigDecimal rowsBD = new BigDecimal(rows);
            BigDecimal xSpace = (width.subtract(NODE_WIDTH.multiply(columnsBD))).divide(columnsBD, MathContext.DECIMAL32);
            BigDecimal xSpaceHalf = xSpace.divide(TWO);
            BigDecimal ySpace = (height.subtract(NODE_HEIGHT.multiply(rowsBD))).divide(rowsBD, MathContext.DECIMAL32);
            BigDecimal ySpaceHalf = ySpace.divide(TWO);
            BigDecimal xPos;
            BigDecimal yPos = null;

            System.out.println("xSpace: " + xSpace.toPlainString() + " ySpace: " + ySpace.toPlainString());

            for (Node node : nodesToLayout) {

                // if there are less nodes than calculated columns (only happens in the last row)
                // set the number of columns to the number of remaining nodes (the "rest")
                if ( x == 0 && rest < columns ) {
                    columns = rest;

                    // recalculated the horizontal space
                    columnsBD = new BigDecimal(columns);
                    xSpace = (width.subtract(NODE_WIDTH.multiply(columnsBD))).divide(columnsBD, MathContext.DECIMAL32);
                    xSpaceHalf = xSpace.divide(TWO);
                }
                xPos = (new BigDecimal(x).multiply(NODE_WIDTH.add(xSpace))).add(xSpaceHalf).add(NODE_WIDTH_HALF).add(ONE_BORDER);
                if (yPos == null) {
                    yPos = ONE_BORDER.add(ySpaceHalf);
                }

                System.out.print(node.getExtID() + " (" + xPos.toPlainString() + "," + yPos.toPlainString() + ") ");
                rest--;
                x++;

                // last element in the row?
                if ( x >= columns ) {
                    x = 0;
                    y++;
                    System.out.println();
                    yPos = (new BigDecimal(y).multiply(NODE_HEIGHT.add(ySpace))).add(ySpaceHalf).add(NODE_HEIGHT_HALF).add(ONE_BORDER);
                }
            }

        }
    }

    private static int nodesOnAxis(BigDecimal factor, int size) {
        double xVal = Math.sqrt(factor.multiply(new BigDecimal(size)).doubleValue());
        return new BigDecimal(xVal).setScale(0, RoundingMode.CEILING).intValue();
    }

}
