package de.artive.visiograph;

import de.artive.visiograph.main.Options;
import nu.xom.*;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.*;
import java.util.*;

import static de.artive.visiograph.helper.CreateInstance.createInstance;

/**
 * Created by IntelliJ IDEA. User: vivo Date: Feb 11, 2010 Time: 1:12:16 PM To change this template use File | Settings
 * | File Templates.
 */
public class VisioGraph {

  private static final String VERSION = "0.1";

  private static final int ERR_SOURCE_AND_TARGET_MISSING = 1;
  private static final int ERR_TARGET_MISSING = 2;
  private static final int ERR_TOO_MANY_ARGUMENTS = 3;
  private static final int ERR_PARSING_EXCEPTION = 4;

  // FIXME: merge

  // TODO: TextPosition same row 
  // TODO: connections to self
  // TODO: override text?
  // TODO: strict and lenient parsing
  // TODO: ERROR CODES

  // TODO: shape.height() instead of NODE_HEIGHT
  // TODO: mapping from some node type to shapes
  // TODO: configuration (file) GraphXPathProvider
  // TODO: KNOWN_LOADERS to abbreviate class names
  // TODO: Shape Templates (i) full Visio Document (ii) shape only
  // TODO: Text Templates

  // TODO: BegTrigger/EndTrigger?

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

  public static void merge(String source, String target) {


  }

  public static void merge(Graph source, File target) throws IOException, ParsingException {
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

  public static void merge(String source, String loaderName, String loaderConf, String target, String template, boolean force) throws IOException {
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

    Graph newGraph = new Graph();

    loader.load(source, newGraph, loaderConf);


    VisioDocument visioDocument;
    if (targetFile.exists()) {
      visioDocument = new VisioDocument(target);
    } else {
      visioDocument = new VisioDocument(template);
    }

    Graph existingGraph = visioDocument.getGraph();

    // move the visio shapes from the existing graph to the new graph
    for (Node newNode : newGraph.getNodes()) {
      Node existingNode = existingGraph.getNode(newNode.getExtID());
      if (existingNode != null) {
        newNode.setVisioRectangle(existingNode.getVisioRectangle());
        existingNode.setVisioRectangle(null);
      }
    }

    for (Edge newEdge : newGraph.getEdges()) {
      Edge existingEdge = existingGraph.getEdge(newEdge.getExtID());
      if (existingEdge != null) {
        newEdge.setVisioConnector(existingEdge.getVisioConnector());
        existingEdge.setVisioConnector(null);
      }
    }

    // now all nodes/edges in the new graph, that have no visio counterpart are _new_ elements
    // and all nodes/edges in the existing graph that still have visio counterparts are obsolete

    for (Node existingNode : existingGraph.getNodes()) {
      if (existingNode.getVisioRectangle() != null) {
        System.out.println("OBSOLETE: " + existingNode);
      } else {
        System.out.println("exists: " + existingNode);
      }
    }

    for (Edge existingEdge : existingGraph.getEdges()) {
      if (existingEdge.getVisioConnector() != null) {
        System.out.println("OBSOLETE: " + existingEdge);
      } else {
        System.out.println("exists: " + existingEdge);
      }
    }

    for (Node newNode : newGraph.getNodes()) {
      if (newNode.getVisioRectangle() == null) {
        System.out.println("NEW: " + newNode);
      }
    }

    for (Edge newEdge : newGraph.getEdges()) {
      if (newEdge.getVisioConnector() == null) {
        System.out.println("NEW: " + newEdge);
      }
    }


    Layout layout = new Layout();
    layout.layout(visioDocument, newGraph);


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

  public static void main(String... args) throws IOException, ParsingException {

    // args = new String[]{"source"};
    // args = new String[]{"src/test/resources/SimpleGraph.xml", "firstVisio.vdx"};
    // args = new String[]{"-x"};
    // args = new String[]{"-h"};


    int exitCode = 0;

    String source = null;
    String target = null;

    Options options = new Options();
    CmdLineParser cmdLineParser = new CmdLineParser(options);

    try {
      cmdLineParser.parseArgument(args);
    } catch (CmdLineException e) {
      System.err.println();
      System.err.println(e.getMessage());
      options.setHelp(true);
      exitCode = ERR_PARSING_EXCEPTION;
    }

    if (options.isVersion()) {
      System.out.println("VisioGraph (C) 2010 Victor Volle. Version: " + VERSION);
      System.exit(0);
    }

    List<String> nonOptionArguments = options.getArgument();
    int size = (nonOptionArguments == null ? 0 : nonOptionArguments.size());
    if (!options.isHelp()) {
      switch (size) {
        case 0:
          System.err.println("\nneither source nor target given");
          options.setHelp(true);
          exitCode = ERR_SOURCE_AND_TARGET_MISSING;
          break;
        case 1:
          System.err.println("\nsource or target not given");
          options.setHelp(true);
          exitCode = ERR_TARGET_MISSING;
          // visioGraph.setSource(nonOptionArguments.get(0));
          break;
        case 2:
          source = nonOptionArguments.get(0);
          target = nonOptionArguments.get(1);
          break;
        default:
          System.err.println("\nmore than 2 arguments: '" + nonOptionArguments + "'");
          options.setHelp(true);
          exitCode = ERR_TOO_MANY_ARGUMENTS;
      }
    }

    if (options.isHelp() || args.length == 0) {
      cmdLineParser.setUsageWidth(100);

      PrintStream out = (exitCode == 0 ? System.out : System.err);

      out.println("\nUsage: java -jar visiograph.jar [OPTIONS] <source> <target>");
      out.println("OPTIONS: ");
      cmdLineParser.printUsage(out);
      System.exit(exitCode);
    }


    System.out.println("source: " + source + " target: " + target);

    VisioGraph.merge(source,
                     options.getLoaderName(),
                     options.getLoaderConf(),
                     target,
                     options.getTemplate(),
                     options.isForce());

    // VisioGraph.merge("src/test/resources/SimpleGraph.xml", null, null, "firstVisio.vdx", null, true);

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
