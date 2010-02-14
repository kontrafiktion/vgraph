package de.artive.visiograph;

import nu.xom.*;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: vivo
 * Date: Feb 11, 2010
 * Time: 4:39:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class VisioRectangle {
                
    public static String _SHAPE = "/Shape";
    public static String _XFORM = _SHAPE + "/XForm";
    public static String _PINX = _XFORM + "/PinX";
    public static String _PINY = _XFORM + "/PinY";
    public static String _WIDTH = _XFORM + "/Width";
    public static String _HEIGHT = _XFORM + "/Height";
    public static String _TEXT = _SHAPE + "/Text";
    public static String _EXT_ID = _SHAPE + "/Prop[Label='external_id']/Value";
    public static String _VISIO_ID = _SHAPE + "/@ID";


    private static final String DEFAULT_RECTANGLE = //
            "<Shape ID=\"${VisioID}\" Type=\"Shape\" LineStyle=\"3\" FillStyle=\"3\" TextStyle=\"3\">\n" +
                    "<XForm>\n" +
                    "    <PinX>${PinX}</PinX>\n" +
                    "    <PinY>${PinY}</PinY>\n" +
                    "    <Width>${Width}</Width>\n" +
                    "    <Height>${Height}</Height>\n" +
                    "    <LocPinX F=\"Width*0.5\"></LocPinX>\n" +
                    "    <LocPinY F=\"Height*0.5\"></LocPinY>\n" +
                    "    <Angle>0</Angle>\n" +
                    "    <FlipX>0</FlipX>\n" +
                    "    <FlipY>0</FlipY>\n" +
                    "    <ResizeMode>0</ResizeMode>\n" +
                    "</XForm>\n" +
                    "<Event>\n" +
                    "    <TheData F=\"No Formula\">0</TheData>\n" +
                    "    <TheText F=\"No Formula\">0</TheText>\n" +
                    "    <EventDblClick F=\"No Formula\">0</EventDblClick>\n" +
                    "    <EventXFMod F=\"No Formula\">0</EventXFMod>\n" +
                    "    <EventDrop F=\"No Formula\">0</EventDrop>\n" +
                    "</Event>\n" +
                    "<Misc>\n" +
                    "    <NoObjHandles F=\"Inh\">0</NoObjHandles>\n" +
                    "    <NonPrinting F=\"Inh\">0</NonPrinting>\n" +
                    "    <NoCtlHandles F=\"Inh\">0</NoCtlHandles>\n" +
                    "    <NoAlignBox F=\"Inh\">0</NoAlignBox>\n" +
                    "    <UpdateAlignBox F=\"Inh\">0</UpdateAlignBox>\n" +
                    "    <HideText F=\"Inh\">0</HideText>\n" +
                    "    <DynFeedback F=\"Inh\">0</DynFeedback>\n" +
                    "    <GlueType F=\"Inh\">0</GlueType>\n" +
                    "    <WalkPreference F=\"Inh\">0</WalkPreference>\n" +
                    "    <BegTrigger F=\"No Formula\">0</BegTrigger>\n" +
                    "    <EndTrigger F=\"No Formula\">0</EndTrigger>\n" +
                    "    <ObjType>1</ObjType>\n" +
                    "    <Comment F=\"Inh\"/>\n" +
                    "    <IsDropSource F=\"Inh\">0</IsDropSource>\n" +
                    "    <NoLiveDynamics F=\"Inh\">0</NoLiveDynamics>\n" +
                    "    <LocalizeMerge F=\"Inh\">0</LocalizeMerge>\n" +
                    "    <Calendar F=\"Inh\">0</Calendar>\n" +
                    "    <LangID F=\"Inh\">1031</LangID>\n" +
                    "    <ShapeKeywords F=\"Inh\"/>\n" +
                    "    <DropOnPageScale F=\"Inh\">1</DropOnPageScale>\n" +
                    "</Misc>\n" +
                    "<Prop ID=\"1\">\n" +
                    "    <Value Unit=\"STR\">${ExtId}</Value>\n" +
                    "    <Prompt F=\"No Formula\"/>\n" +
                    "    <Label>external_id</Label>\n" +
                    "    <Format F=\"No Formula\"/>\n" +
                    "    <SortKey F=\"No Formula\"/>\n" +
                    "    <Type>0</Type>\n" +
                    "    <Invisible F=\"No Formula\">0</Invisible>\n" +
                    "    <Verify F=\"No Formula\">0</Verify>\n" +
                    "    <LangID>1031</LangID>\n" +
                    "    <Calendar F=\"No Formula\">0</Calendar>\n" +
                    "</Prop>\n" +
                    "<Geom IX=\"0\">\n" +
                    "    <NoFill>0</NoFill>\n" +
                    "    <NoLine>0</NoLine>\n" +
                    "    <NoShow>0</NoShow>\n" +
                    "    <NoSnap>0</NoSnap>\n" +
                    "    <MoveTo IX=\"1\">\n" +
                    "        <X F=\"Width*0\">0</X>\n" +
                    "        <Y F=\"Height*0\">0</Y>\n" +
                    "    </MoveTo>\n" +
                    "    <LineTo IX=\"2\">\n" +
                    "        <X F=\"Width*1\"></X>\n" +
                    "        <Y F=\"Height*0\"></Y>\n" +
                    "    </LineTo>\n" +
                    "    <LineTo IX=\"3\">\n" +
                    "        <X F=\"Width*1\"></X>\n" +
                    "        <Y F=\"Height*1\"></Y>\n" +
                    "    </LineTo>\n" +
                    "    <LineTo IX=\"4\">\n" +
                    "        <X F=\"Width*0\"></X>\n" +
                    "        <Y F=\"Height*1\"></Y>\n" +
                    "    </LineTo>\n" +
                    "    <LineTo IX=\"5\">\n" +
                    "        <X F=\"Geometry1.X1\"></X>\n" +
                    "        <Y F=\"Geometry1.Y1\"></Y>\n" +
                    "    </LineTo>\n" +
                    "</Geom>\n" +
                    "<Text>${text}\n" +
                    "</Text>\n" +
                    "</Shape>";

    public static final String BASE_URI = "http://victorvolle.wordpress.com/";
    private Element xmlRoot;
    private static final BigDecimal DEFAULT_VALUE = new BigDecimal("1.00000000000000");

    public VisioRectangle(BigDecimal pinX, BigDecimal pinY, BigDecimal width, BigDecimal height, String extId, String text) {
        Builder xmlBuilder = new Builder();
        try {
            Document document = xmlBuilder.build(DEFAULT_RECTANGLE, BASE_URI);
            xmlRoot = document.getRootElement();
            setPinX(pinX);
            setPinY(pinY);
            setWidth(width);
            setHeight(height);
            setExtId(extId);
            setText(text);
            System.out.println(document.toXML());
        } catch (ParsingException e) {
            throw new VisioGraphException("Should not happen, probably a programming error: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new VisioGraphException("we are not doing IO here, why should there be an IO Exception:" + e.getMessage(), e);
        }
    }

    public VisioRectangle(String pinX, String pinY, String width, String height, String id, String text) {
        this(new BigDecimal(pinX), new BigDecimal(pinY), new BigDecimal(width), new BigDecimal(height), id, text);
    }

    public static void main(String[] args) {
        VisioRectangle vr = new VisioRectangle("42.0", "41.0", "40.0", "39.0", "x1", "A");
        System.out.println(vr.getVisioID());
    }


    public int getVisioID() {
        Attribute visioIDAttr = (Attribute) xmlRoot.query(_VISIO_ID).get(0);
        Integer visioID;
        try {
            visioID = Integer.valueOf(visioIDAttr.getValue());
        } catch (NumberFormatException e) {
            visioID = -1;
        }
        return visioID;
    }

    public void setVisioID(int visioID) {
        Attribute visioIDAttr = (Attribute) xmlRoot.query(_VISIO_ID).get(0);
        visioIDAttr.setValue(String.valueOf(visioID));
    }

    public BigDecimal getPinX() {
        return safeBigDecimal(getSingleElement(_PINX).getValue());
    }

    public void setPinX(BigDecimal pinX) {
        setValue(_PINX, pinX);
    }

    public BigDecimal getPinY() {
        return safeBigDecimal(getSingleElement(_PINY).getValue());
    }

    public void setPinY(BigDecimal pinY) {
        setValue(_PINY, pinY);
    }

    public BigDecimal getWidth() {
        return safeBigDecimal(getSingleElement(_WIDTH).getValue());
    }

    public void setWidth(BigDecimal width) {
        setValue(_WIDTH, width);
    }

    public BigDecimal getHeight() {
        return safeBigDecimal(getSingleElement(_HEIGHT).getValue());
    }

    public void setHeight(BigDecimal height) {
        setValue(_HEIGHT, height);
    }

    public String getExtId() {
        return getSingleElement(_EXT_ID).getValue();
    }

    public void setExtId(String id) {
        setValue(_EXT_ID, id);
    }

    public String getText() {
        return getSingleElement(_TEXT).getValue();
    }

    public void setText(String text) {
        setValue(_TEXT, text);
    }

    /**
     * Converts a String into a BigDecimal. If the conversion throws a NumberFormatException
     * the {@link #DEFAULT_VALUE} is returned. The XML templates contain placeholders (e.g. "${PinX})
     * because these templates ought to be used by simpler code using Velocity. Therefore retrieving
     * this String and converting it would throw an Exception.
     *
     * @param value -- a String that can be converted to a BigDecimal
     * @return the converted value or {@link #DEFAULT_VALUE} if the String could not be converted
     */
    public static BigDecimal safeBigDecimal(String value) {
        BigDecimal result;
        try {
            result = new BigDecimal(value);
        } catch (NumberFormatException e) {
            result = DEFAULT_VALUE;
        }
        return result;
    }

    /**
     * Helper method to retrieve an Element referenced by an xPath.
     *
     * @param xquery
     * @return
     * @throws VisioGraphException
     */
    private Element getSingleElement(String xquery) throws VisioGraphException {
        Nodes nodes = xmlRoot.query(xquery);
        if (nodes.size() != 1) {
            throw new VisioGraphException("xquery: \"" + xquery + "\" returned more than one Node");
        }
        return (Element) nodes.get(0);
    }

    /**
     * Sets the given value as Text in the Element referenced by the given xPath
     *
     * @param xPath -- the path in the document, that references the element, where the value should be set
     * @param value -- the value to be set
     */
    private void setValue(String xPath, BigDecimal value) {
        setValue(xPath, value.toString());
    }

    /**
     * Sets the given value as Text in the Element referenced by the given xPath
     *
     * @param xPath -- the path in the document, that references the element, where the value should be set
     * @param value -- the value to be set
     */
    private void setValue(String xPath, String value) {
        Element element = getSingleElement(xPath);
        element.removeChildren();
        element.appendChild(value);
    }



}
