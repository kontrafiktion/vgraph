package de.artive.visiograph;

import de.artive.visiograph.helper.VisioHelper;
import de.artive.visiograph.helper.XmlHelper;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;

import java.io.IOException;
import java.math.BigDecimal;

import static de.artive.visiograph.helper.VisioHelper.divideByTwo;


/**
 * Created by IntelliJ IDEA. User: vivo Date: Feb 18, 2010 Time: 4:41:27 PM To change this template use File | Settings
 * | File Templates.
 */
public class VisioConnector extends VisioShape {

  public static final String _VS_GEOM = _VS_SHAPE + "/v:Geom";
  public static final String _VS_MOVETO_Y = _VS_GEOM + "/v:MoveTo/v:Y";


  public static final String _VS_LOCPINX = _VS_XFORM + "/v:LocPinX";
  public static final String _VS_LOCPINY = _VS_XFORM + "/v:LocPinY";
  public static final String _VS_XFORM1D = _VS_SHAPE + "/v:XForm1D";
  public static final String _VS_XFORM1D_BEGINX = _VS_XFORM1D + "/v:BeginX";
  public static final String _VS_XFORM1D_BEGINY = _VS_XFORM1D + "/v:BeginY";
  public static final String _VS_XFORM1D_ENDX = _VS_XFORM1D + "/v:EndX";
  public static final String _VS_XFORM1D_ENDY = _VS_XFORM1D + "/v:EndY";
  public static final String _VS_HEIGHT_FORMULA = _VS_HEIGHT + "/@F";

  public static final BigDecimal HEIGHT_VALUE_SAME_HEIGHT = new BigDecimal("0.25");
  public static final String HEIGHT_FORMULA_SAME_HEIGHT = "GUARD(0.25DL)";
  public static final String HEIGHT_FORMULA_DIFFERENT_HEIGHT = "GUARD(EndY-BeginY)";

  public static final String CONNECTOR_TEMPLATE =
      "<Shape xmlns=\"http://schemas.microsoft.com/visio/2003/core\" ID=\"${VisioID}\" NameU=\"Dynamic connector\" Type=\"Shape\" Master=\"0\">\n" +
          "    <XForm>\n" +
          "        <PinX F=\"Inh\">${PinX}</PinX>\n" +
          "        <PinY F=\"Inh\">${PinY}</PinY>\n" +
          "        <Width F=\"GUARD(EndX-BeginX)\"></Width>\n" +
          "        <Height F=\"GUARD(0.25DL)\">0.25</Height>\n" +
          "        <LocPinX F=\"GUARD(Width*0.5)\"></LocPinX>\n" +
          "        <LocPinY F=\"GUARD(Height*0.5)\"></LocPinY>\n" +
          "        <Angle F=\"Inh\">0</Angle>\n" +
          "        <FlipX F=\"Inh\">0</FlipX>\n" +
          "        <FlipY F=\"Inh\">0</FlipY>\n" +
          "        <ResizeMode F=\"Inh\">0</ResizeMode>\n" +
          "    </XForm>\n" +
          "    <XForm1D>\n" +
          "        <BeginX F=\"_WALKGLUE(BegTrigger,EndTrigger,WalkPreference)\"></BeginX>\n" +
          "        <BeginY F=\"_WALKGLUE(BegTrigger,EndTrigger,WalkPreference)\"></BeginY>\n" +
          "        <EndX F=\"_WALKGLUE(EndTrigger,BegTrigger,WalkPreference)\"></EndX>\n" +
          "        <EndY F=\"_WALKGLUE(EndTrigger,BegTrigger,WalkPreference)\"></EndY>\n" +
          "    </XForm1D>\n" +
          "    <LayerMem>\n" +
          "        <LayerMember>0</LayerMember>\n" +
          "    </LayerMem>\n" +
          "    <Event>\n" +
          "        <TheData F=\"No Formula\">0</TheData>\n" +
          "        <TheText F=\"No Formula\">0</TheText>\n" +
          "        <EventDblClick F=\"No Formula\">0</EventDblClick>\n" +
          "        <EventXFMod F=\"No Formula\">0</EventXFMod>\n" +
          "        <EventDrop F=\"No Formula\">0</EventDrop>\n" +
          "    </Event>\n" +
          "    <Misc>\n" +
          "        <NoObjHandles F=\"Inh\">0</NoObjHandles>\n" +
          "        <NonPrinting F=\"Inh\">0</NonPrinting>\n" +
          "        <NoCtlHandles F=\"Inh\">0</NoCtlHandles>\n" +
          "        <NoAlignBox F=\"Inh\">1</NoAlignBox>\n" +
          "        <UpdateAlignBox F=\"Inh\">0</UpdateAlignBox>\n" +
          "        <HideText F=\"Inh\">0</HideText>\n" +
          "        <DynFeedback F=\"Inh\">2</DynFeedback>\n" +
          "        <GlueType F=\"Inh\">2</GlueType>\n" +
          "        <WalkPreference F=\"Inh\">0</WalkPreference>\n" +
          "        <BegTrigger F=\"_XFTRIGGER(Sheet.1!EventXFMod)\">2</BegTrigger>\n" +
          "        <EndTrigger F=\"_XFTRIGGER(Sheet.4!EventXFMod)\">2</EndTrigger>\n" +
          "        <ObjType F=\"Inh\">2</ObjType>\n" +
          "        <Comment F=\"Inh\"/>\n" +
          "        <IsDropSource F=\"Inh\">0</IsDropSource>\n" +
          "        <NoLiveDynamics F=\"Inh\">1</NoLiveDynamics>\n" +
          "        <LocalizeMerge F=\"Inh\">0</LocalizeMerge>\n" +
          "        <Calendar F=\"Inh\">0</Calendar>\n" +
          "        <LangID F=\"Inh\">1031</LangID>\n" +
          "        <ShapeKeywords F=\"Inh\"/>\n" +
          "        <DropOnPageScale F=\"Inh\">1</DropOnPageScale>\n" +
          "    </Misc>\n" +
          "    <Prop ID=\"1\">\n" +
          "       <Value Unit=\"STR\">${ExtId}</Value>\n" +
          "       <Prompt F=\"No Formula\"/>\n" +
          "       <Label>external_id</Label>\n" +
          "       <Format F=\"No Formula\"/>\n" +
          "       <SortKey F=\"No Formula\"/>\n" +
          "       <Type>0</Type>\n" +
          "       <Invisible F=\"No Formula\">0</Invisible>\n" +
          "       <Verify F=\"No Formula\">0</Verify>\n" +
          "       <LangID>1031</LangID>\n" +
          "       <Calendar F=\"No Formula\">0</Calendar>\n" +
          "    </Prop>\n" +
          "    <TextXForm>\n" +
          "        <TxtPinX F=\"Inh\">0</TxtPinX>\n" +
          "        <TxtPinY F=\"Inh\">-1.8487535</TxtPinY>\n" +
          "        <TxtWidth F=\"Inh\">0.5555555555555556</TxtWidth>\n" +
          "        <TxtHeight F=\"Inh\">0.2444939358181424</TxtHeight>\n" +
          "        <TxtLocPinX F=\"Inh\">0.2777777777777778</TxtLocPinX>\n" +
          "        <TxtLocPinY F=\"Inh\">0.1222469679090712</TxtLocPinY>\n" +
          "        <TxtAngle F=\"Inh\">0</TxtAngle>\n" +
          "    </TextXForm>\n" +
          "    <Layout>\n" +
          "        <ShapePermeableX F=\"Inh\">0</ShapePermeableX>\n" +
          "        <ShapePermeableY F=\"Inh\">0</ShapePermeableY>\n" +
          "        <ShapePermeablePlace F=\"Inh\">0</ShapePermeablePlace>\n" +
          "        <ShapeFixedCode F=\"Inh\">0</ShapeFixedCode>\n" +
          "        <ShapePlowCode F=\"Inh\">0</ShapePlowCode>\n" +
          "        <ShapeRouteStyle F=\"Inh\">0</ShapeRouteStyle>\n" +
          "        <ConFixedCode>3</ConFixedCode>\n" +
          "        <ConLineJumpCode F=\"Inh\">0</ConLineJumpCode>\n" +
          "        <ConLineJumpStyle F=\"Inh\">0</ConLineJumpStyle>\n" +
          "        <ConLineJumpDirX F=\"Inh\">0</ConLineJumpDirX>\n" +
          "        <ConLineJumpDirY F=\"Inh\">0</ConLineJumpDirY>\n" +
          "        <ShapePlaceFlip F=\"Inh\">0</ShapePlaceFlip>\n" +
          "        <ConLineRouteExt F=\"Inh\">0</ConLineRouteExt>\n" +
          "        <ShapeSplit F=\"Inh\">0</ShapeSplit>\n" +
          "        <ShapeSplittable F=\"Inh\">1</ShapeSplittable>\n" +
          "    </Layout>\n" +
          "    <Line>\n" +
          "        <LineWeight Unit=\"PT\" F=\"Inh\">0.003333333333333333</LineWeight>\n" +
          "        <LineColor F=\"Inh\">0</LineColor>\n" +
          "        <LinePattern F=\"Inh\">1</LinePattern>\n" +
          "        <Rounding F=\"Inh\">0</Rounding>\n" +
          "        <EndArrowSize F=\"Inh\">2</EndArrowSize>\n" +
          "        <BeginArrow F=\"Inh\">0</BeginArrow>\n" +
          "        <EndArrow>0</EndArrow>\n" +
          "        <LineCap F=\"Inh\">0</LineCap>\n" +
          "        <BeginArrowSize F=\"Inh\">2</BeginArrowSize>\n" +
          "        <LineColorTrans F=\"Inh\">0</LineColorTrans>\n" +
          "    </Line>" +
          "    <Control NameU=\"TextPosition\" ID=\"1\">\n" +
          "        <X>1.6607613</X>\n" +
          "        <Y>0.6250000000000014</Y>\n" +
          "        <XDyn F=\"Inh\">1.6607613</XDyn>\n" +
          "        <YDyn F=\"Inh\">0.6250000000000014</YDyn>\n" +
          "        <XCon F=\"Inh\">0</XCon>\n" +
          "        <YCon F=\"Inh\">0</YCon>\n" +
          "        <CanGlue F=\"Inh\">0</CanGlue>\n" +
          "        <Prompt F=\"Inh\">Reposition Text</Prompt>\n" +
          "    </Control>" +
          "    <Geom IX=\"0\">\n" +
          "        <NoFill>1</NoFill>\n" +
          "        <NoLine>0</NoLine>\n" +
          "        <NoShow>0</NoShow>\n" +
          "        <NoSnap>0</NoSnap>\n" +
          "        <MoveTo IX=\"1\">\n" +
          "            <X>0</X>\n" +
          "            <Y>0.125</Y>\n" +
          "        </MoveTo>\n" +
          "    </Geom>\n" +
          "    <Text>ADfurblbarbfl\n" +
          "</Text>\n" +
          "</Shape>\n";


  public final static String LINE_TO_TEMPLATE =
          "    <LineTo xmlns=\"http://schemas.microsoft.com/visio/2003/core\" IX=\"\">\n" +
          "        <X></X>\n" +
          "        <Y></Y>\n" +
          "    </LineTo>\n";


  private int nextLineId = 2;



  public VisioConnector(BigDecimal beginX, BigDecimal endX, BigDecimal beginY, BigDecimal endY, String extId, String text) {
    XmlHelper.setValue(xmlRoot, _VS_XFORM1D_BEGINX, beginX);
    XmlHelper.setValue(xmlRoot, _VS_XFORM1D_ENDX, endX);
    XmlHelper.setValue(xmlRoot, _VS_XFORM1D_BEGINY, beginY);
    XmlHelper.setValue(xmlRoot, _VS_XFORM1D_ENDY, endY);

    boolean sameRow = beginY.compareTo(endY) == 0;
    BigDecimal height;
    if ( sameRow ) {
      height = HEIGHT_VALUE_SAME_HEIGHT;
      XmlHelper.setValue(xmlRoot, _VS_HEIGHT_FORMULA, HEIGHT_FORMULA_SAME_HEIGHT);
      setPinY(beginY);
      setValue(_VS_MOVETO_Y, VisioHelper.VISIO_Y_DIST);
    } else {
      height = endY.subtract(beginY);
      XmlHelper.setValue(xmlRoot, _VS_HEIGHT_FORMULA, HEIGHT_FORMULA_DIFFERENT_HEIGHT);
      setPinY(beginY.add(divideByTwo(height)));
      setValue(_VS_MOVETO_Y, VisioHelper.ZERO);
    }
    setHeight(height);
    BigDecimal width = endX.subtract(beginX);
    setWidth(width);

    setPinX(beginX.add(divideByTwo(width)));

    setExtId(extId);

    setText(text);


    // super(pinX, pinY, width, height, extId, text);
    
  }


  public void addLine(BigDecimal xDelta, BigDecimal yDelta) {
    try {
      Document lineToTemplate = new Builder().build(LINE_TO_TEMPLATE, "");
      Element line = lineToTemplate.getRootElement();
      XmlHelper.setValue(line, "/v:LineTo/@IX", String.valueOf(nextLineId));
      nextLineId++;
      XmlHelper.setValue(line, "/v:LineTo/v:X", xDelta);
      XmlHelper.setValue(line, "/v:LineTo/v:Y", yDelta);
      System.out
          .print("  -> (" + xDelta.multiply(VisioHelper.INCH).toPlainString() + ", " +
              yDelta.multiply(VisioHelper.INCH).toPlainString() + ")");
      getSingleElement(_VS_GEOM).appendChild(line.copy());
      // System.out.println(xmlRoot.toXML());
    } catch (ParsingException e) {
      throw new VisioGraphException("error parsing: " + LINE_TO_TEMPLATE);
    } catch (IOException e) {
      throw new VisioGraphException("error parsing: " + LINE_TO_TEMPLATE);
    }

  }


  @Override
  protected String getTemplate() {
    return CONNECTOR_TEMPLATE;
  }


}
