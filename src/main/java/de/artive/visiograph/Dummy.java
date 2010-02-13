package de.artive.visiograph;

import nu.xom.*;

import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: vivo
 * Date: Feb 11, 2010
 * Time: 1:12:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class Dummy {

    // VisioDocument
    //   DocumentProperties
    //     TimeCreated  :  2010-02-08T20:47:14
    //     TimeSaved
    //     TimeEdited
    //  Pages/Page[ID="0"]
    //    Shapes
    //      GraphElement
    //        XForm
    //          Pinx : mm / 25.4 decimal point!
    //          PinY : mm /25.4
    //          Width
    //          Height
    //          LocPinX F="Width*0.5">0.1968503937007873</LocPinX>
    //          LocPinY F="Height*0.5">0.1968503937007873</LocPinX>
    //        Prop ID="1">
    //                        <Value Unit="STR">A2</Value>
    //                        <Prompt F="No Formula"/>
    //                        <Label>ID</Label>
    //                        <Format F="No Formula"/>
    //                        <SortKey F="No Formula"/>
    //                        <Type>0</Type>
    //                        <Invisible F="No Formula">0</Invisible>
    //                        <Verify F="No Formula">0</Verify>
    //                        <LangID>1031</LangID>
    //                        <Calendar F="No Formula">0</Calendar>
    //                    </Prop>
    //    Connects
    //      Connect

    public void merge(String source, String target) {


    }

    public void merge(Graph source, File target) throws IOException, ParsingException {
        if (target.exists()) {
            Builder builder = new Builder();
            Document document = builder.build(target);

            Serializer serializer = new Serializer(System.out, "UTF-8") {
                @Override
                protected void writeEndTag(Element element) throws IOException {
                    super.writeEndTag(element);
                }
            };
            serializer.setIndent(2);
            serializer.setMaxLength(30);
            serializer.setPreserveBaseURI(true);
            // serializer.set;
            serializer.setLineSeparator("\n");
            serializer.write(document);
            serializer.flush();

            // System.out.println(document.toXML());
        }
    }

    public static void main(String[] args) throws IOException, ParsingException {

        Dummy d = new Dummy();
        d.merge(null, new File("src/test/work/visio.vdx"));

        // load source, create sourceModel

        // check if target exists
        // NO:
        // for each Node create a GraphElement in Visio, each distributed evenly on the page
        //   + map source model "kind" to GraphElement template (either in a master or a file containing placeholders)
        // for each Edge create a GraphElement in Visio connected to the Node GraphElement
        //   + map source model "kind" to GraphElement template (either in a master or a file containing placeholders)


        // YES
        // find all missing Nodes and edges in target (Add), find all missing nodes and edges in source (Delete)
        // for each added node, see NO above (but on the "New" layer)
        // for each node to be deleted, put it on the "Delete" layer
        // for each edge, check that the connected nodes are correct, if not log error


    }
}
