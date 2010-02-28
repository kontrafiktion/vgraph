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

package de.artive.vgraph.visio;

import nu.xom.Element;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA. User: vivo Date: Feb 11, 2010 Time: 4:39:35 PM To change this template use File | Settings
 * | File Templates.
 */
public class VisioRectangle extends VisioShape {


  private static final String RECTANGLE_TEMPLATE = //
      "<Shape xmlns=\"http://schemas.microsoft.com/visio/2003/core\" xml:base=\"\" ID=\"${VisioID}\" Type=\"Shape\" LineStyle=\"3\" FillStyle=\"3\" TextStyle=\"3\">\n"
      +
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

  public static final String DELETED_RECTANGLE_LINE =
      "<Line>\n"
      + "    <LineWeight>0.01</LineWeight>\n"
      + "    <LineColor>0</LineColor>\n"
      + "    <LinePattern>1</LinePattern>\n"
      + "    <Rounding F=\"Inh\">0</Rounding>\n"
      + "    <EndArrowSize F=\"Inh\">2</EndArrowSize>\n"
      + "    <BeginArrow F=\"Inh\">0</BeginArrow>\n"
      + "    <EndArrow F=\"Inh\">0</EndArrow>\n"
      + "    <LineCap F=\"Inh\">0</LineCap>\n"
      + "    <BeginArrowSize F=\"Inh\">2</BeginArrowSize>\n"
      + "    <LineColorTrans F=\"Inh\">0</LineColorTrans>\n"
      + "</Line>\n";
  public static final String DELETED_RECTANGLE_FILL =
      "<Fill>\n"
      + "    <FillForegnd>2</FillForegnd>\n"
      + "    <FillBkgnd>1</FillBkgnd>\n"
      + "    <FillPattern>2</FillPattern>\n"
      + "    <ShdwForegnd F=\"Inh\">0</ShdwForegnd>\n"
      + "    <ShdwBkgnd F=\"Inh\">1</ShdwBkgnd>\n"
      + "    <ShdwPattern F=\"Inh\">0</ShdwPattern>\n"
      + "    <FillForegndTrans F=\"Inh\">0</FillForegndTrans>\n"
      + "    <FillBkgndTrans F=\"Inh\">0</FillBkgndTrans>\n"
      + "    <ShdwForegndTrans F=\"Inh\">0</ShdwForegndTrans>\n"
      + "    <ShdwBkgndTrans F=\"Inh\">0</ShdwBkgndTrans>\n"
      + "    <ShapeShdwType F=\"Inh\">0</ShapeShdwType>\n"
      + "    <ShapeShdwOffsetX F=\"Inh\">0</ShapeShdwOffsetX>\n"
      + "    <ShapeShdwOffsetY F=\"Inh\">0</ShapeShdwOffsetY>\n"
      + "    <ShapeShdwObliqueAngle F=\"Inh\">0</ShapeShdwObliqueAngle>\n"
      + "    <ShapeShdwScaleFactor F=\"Inh\">1</ShapeShdwScaleFactor>\n"
      + "</Fill>";


  public VisioRectangle(Element xmlRoot) {
    super(xmlRoot);
  }

  public VisioRectangle(BigDecimal pinX, BigDecimal pinY, BigDecimal width, BigDecimal height, String extId, String text) {
    super(pinX, pinY, width, height, extId, text);
  }


  @Override
  protected String getTemplate() {
    return RECTANGLE_TEMPLATE;
  }
}
