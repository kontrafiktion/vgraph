package de.artive.visiograph;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.Serializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static de.artive.visiograph.helper.CreateInstance.createInstance;

/**
 * Created by IntelliJ IDEA. User: vivo Date: Feb 11, 2010 Time: 1:12:16 PM To change this template use File | Settings
 * | File Templates.
 */
public class Dummy {

  // TODO: mapping from some node type to shapes
  // TODO: configuration (file) GraphXPathProvider
  // TODO: KNOWN_LOADERS to abbreviate class names
  // TODO: Shape Templates (i) full Visio Document (ii) shape only
  // TODO: Text Templates

  // usage: visiograph [OPTIONS] <source> <target>
  // -l --loader <loader_name>     the class name of the loader to be used
  // -c --loaderConf <loader_conf> some configuration string for the loader (e.g. a URL)
  // -t --template <template_name> the name of a Visio template file
  // -f --force                    overwrite the target instead of merging
  //

  public static Map<String, SourceLoader> KNOWN_LOADERS = new HashMap<String, SourceLoader>() {{
    // put("simple", new SimpleXmlSourceLoader() 
  }};

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

      Serializer serializer = new Serializer(System.out, "UTF-8");
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

  public void merge(String source, String loaderName, String loaderConf, String target, String template, boolean force) throws IOException {
    SourceLoader loader;
    if (loaderName == null) {
      loader = new SimpleXmlSourceLoader();
    } else {
      loader = createInstance(loaderName);
    }

    final File targetFile = new File(target);
    if (force && targetFile.exists()) {
      if (!targetFile.delete()) {
        throw new VisioGraphException("could not delete: " + targetFile);
      }
    }

    Graph graph = new Graph();

    loader.load(source, graph, loaderConf);

    VisioDocument visioDocument = new VisioDocument(template);
    Layout layout = new Layout();
    layout.layout(visioDocument, graph);


    OutputStream outputStream = System.out;
    if (target != null) {
      outputStream = new FileOutputStream(target);
    }
    Serializer serializer = new Serializer(outputStream, "UTF-8");
    serializer.setIndent(2);
    serializer.setMaxLength(30);
    serializer.setPreserveBaseURI(true);
    serializer.setLineSeparator("\r\n");
    serializer.write(visioDocument.getDocument());
    serializer.flush();


  }


  public static void main(String[] args) throws IOException, ParsingException {

    Dummy d = new Dummy();
    d.merge("src/test/resources/SimpleGraph.xml", null, null, "firstVisio.vdx", null, true);

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
