/*
 * Copyright 2010 Victor Volle
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.artive.visiograph;

import de.artive.visiograph.helper.*;
import nu.xom.*;
import nu.xom.Node;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import static de.artive.visiograph.helper.XmlHelper.getValue;

/**
 * Created by IntelliJ IDEA. User: vivo Date: Feb 14, 2010 Time: 4:20:50 PM To change this template use File | Settings
 * | File Templates.
 */
public class VisioDocument {

  public static final String DEFAULT_VISIO_TEMPLATE = "visio_blank_a4_landscape.vdx";

  public static final String _ROOT = "/v:VisioDocument";
  public static final String _PAGE = _ROOT + "/v:Pages/v:Page[@ID=\"0\"]";
  public static final String _PAGE_SHEET = _PAGE + "/v:PageSheet";
  public static final String _PAGE_PROPERTIES = _PAGE_SHEET + "/v:PageProps";
  public static final String _PAGE_WIDTH = _PAGE_PROPERTIES + "/v:PageWidth";
  public static final String _PAGE_HEIGHT = _PAGE_PROPERTIES + "/v:PageHeight";
  public static final String _SHAPES_CONTAINER = _PAGE + "/v:Shapes";
  public static final String _SHAPES = _PAGE + "/v:Shapes/v:Shape";
  public static final String CONNECTS_CONTAINER = "Connects";
  public static final String _CONNECTS_CONTAINER = _PAGE + "/v:" + CONNECTS_CONTAINER;
  public static final String _CONNECTS = _CONNECTS_CONTAINER + "/v:Connect";
  public static final String _C_CONNECT = ".";
  public static final String _C_CONNECT_FROM_SHEET = _C_CONNECT + "/@FromSheet";
  public static final String _C_CONNECT_TO_SHEET = _C_CONNECT + "/@ToSheet";

  public static final String MAX_SHAPE_ID = "v:Shape[not(following-sibling::v:Shape/@ID > @ID) and not(preceding-sibling::v:Shape/@ID > @ID)]/@ID";

  public static final String CONNECT_TEMPLATE_BEGIN =
      "       <Connect  xmlns=\"http://schemas.microsoft.com/visio/2003/core\" FromSheet=\"${ConnectorVisioID}\" FromCell=\"BeginX\" FromPart=\"9\" ToSheet=\"${sourceNodeVisioID}\" ToCell=\"PinX\" ToPart=\"3\"/>\n";
  public static final String CONNECT_TEMPLATE_END =
      "       <Connect  xmlns=\"http://schemas.microsoft.com/visio/2003/core\" FromSheet=\"${ConnectorVisioID}\" FromCell=\"EndX\" FromPart=\"12\" ToSheet=\"${targetNodeVisioID}\" ToCell=\"PinX\" ToPart=\"3\"/>\n";

  int nextShapeId = 1;

  private Document document = null;
  private Element shapesContainer;
  private Element connectEndTemplate;
  private Element connectBeginTemplate;

  private Graph graph = null;


  public VisioDocument() throws IOException {
    this(DEFAULT_VISIO_TEMPLATE);
  }


  public VisioDocument(String templateName) throws IOException {

    if (templateName != null) {
      loadDocument(templateName);
    } else {
      loadDocument(DEFAULT_VISIO_TEMPLATE);
    }

    Builder builder = new Builder();
    try {
      Document doc = builder.build(CONNECT_TEMPLATE_BEGIN, "");
      // I don't know why XOM insist on setting a base ... but Visio won't like it
      doc.setBaseURI("");
      connectBeginTemplate = doc.getRootElement();
    } catch (ParsingException e) {
      throw new VisioGraphException("error parsing: " + CONNECT_TEMPLATE_BEGIN, e);
    }
    try {
      Document doc = builder.build(CONNECT_TEMPLATE_END, "");
      // I don't know why XOM insist on setting a base ... but Visio won't like it
      doc.setBaseURI("");
      connectEndTemplate = doc.getRootElement();
    } catch (ParsingException e) {
      throw new VisioGraphException("error parsing: " + CONNECT_TEMPLATE_END, e);
    }
  }

  private void loadDocument(String templateName) throws IOException {

    initDocument(templateName);

    graph = new Graph();
    Element root = document.getRootElement();
    Nodes allShapes = root.query(_SHAPES, XmlHelper.VISIO_XPATH_CONTEXT);

    List<VisioConnector> visioConnectors = new ArrayList<VisioConnector>();

    for (Iterator<Node> it = NodesIterator.nodesIterator(allShapes); it.hasNext();) {
      Element shape = (Element) it.next();
      Node extIdNode = XmlHelper.getSingleNode(shape, VisioShape._VS_EXT_ID, false);
      if (extIdNode != null) {
        String extId = extIdNode.getValue();

        // System.out.println(extId);
        if (XmlHelper.getSingleNode(shape, VisioConnector._VS_XFORM1D, true) != null) {
          VisioConnector visioConnector = new VisioConnector(shape);
          visioConnectors.add(visioConnector);


          System.out.println("CONNECTOR:" + extId);
        } else {
          VisioRectangle visioRectangle = new VisioRectangle(shape);
          de.artive.visiograph.Node graphNode = new de.artive.visiograph.Node(visioRectangle.getExtId(),
                                                                              visioRectangle.getText());
          graphNode.setVisioRectangle(visioRectangle);
          graph.addNode(graphNode);
        }
      }

    }

    // now that we have all nodes, we can create the edges
    for (VisioConnector visioConnector : visioConnectors) {
      createGraphEdge(graph, root, visioConnector);
    }

  }

  /**
   * loads the visio document (either from the file system or the classpath into the instance variable {@link
   * #document})
   *
   * @param templateName the name of the template
   * @throws IOException
   */
  private void initDocument(String templateName) throws IOException {
    final File templateFile = new File(templateName);

    if (templateFile.exists()) {
      (new ResourceHandler<Reader>(templateFile.getPath()) {

        @Override
        protected Reader open() throws IOException {
          return new FileReader(templateFile);
        }

        @Override
        protected void doWithCloseable(Reader resource) throws Exception {
          Builder builder = new Builder();
          document = builder.build(resource);
        }

      }).execute();

    } else {
      final InputStream stream = VisioDocument.class.getClassLoader().getResourceAsStream(templateName);
      if (stream != null) {
        (new ResourceHandler<InputStream>(templateFile.getPath()) {

          @Override
          protected void doWithCloseable(InputStream stream) throws Exception {
            Builder builder = new Builder();
            document = builder.build(stream);
          }

        }).execute(stream);
      }
    }
  }

  /**
   * create a new {@link de.artive.visiograph.Node} from the given VisioConnector and add it to to the given
   * <code>graph</code>
   *
   * @param graph          the graph that must already contain all nodes that the edge might be connected to
   * @param root           the root of the visio document
   * @param visioConnector the visio connector xml element
   */
  private void createGraphEdge(Graph graph, Element root, VisioConnector visioConnector) {

    int visioId = visioConnector.getVisioID();

    // TODO: check performance
    // Connect FromSheet="6" FromCell="BeginX" FromPart="9" ToSheet="4" ToCell="PinX" ToPart="3"
    Element beginConnect = XmlHelper.getSingleElement(root,
                                                      _CONNECTS + "[@FromSheet='" + visioId +
                                                      "'][@FromCell='BeginX']",
                                                      true);
    Element endConnect = XmlHelper.getSingleElement(root,
                                                    _CONNECTS + "[@FromSheet='" + visioId +
                                                    "'][@FromCell='EndX']",
                                                    true);
    if (beginConnect == null || endConnect == null) {
      // TODO: log
      System.err.println(
          "could not determine connecting node: " + visioConnector + " perhaps the connector is disconnected?");
      return;
    }
    final String TO_SHEET = "@ToSheet";
    String beginNode = getValue(beginConnect, TO_SHEET);
    String endNode = getValue(endConnect, TO_SHEET);

    String beginExtId = extIdForVisioId(root, beginNode);
    String endExtId = extIdForVisioId(root, endNode);

    de.artive.visiograph.Node source = graph.getNode(beginExtId);
    de.artive.visiograph.Node target = graph.getNode(endExtId);

    if (source == null || target == null) {
      System.err.println("could not find existing graph nodes for connector: "
                         + " perhaps the connector has been connected to a node that has not been created by VisioGraph?");
    }

    Edge newEdge = new Edge(visioConnector.getExtId(), visioConnector.getText(), source, target);
    newEdge.setVisioConnector(visioConnector);
    graph.addEdge(newEdge);
  }

  private String extIdForVisioId(Element root, String visioId) {
    return getValue(root,
                    _SHAPES + "[" + VisioShape.VS_VISIO_ID + "='" + visioId + "']"
                    + VisioShape.PROP_EXTERNAL_ID_VALUE);
  }


  public static void main(String[] args) throws IOException, ParsingException {
    VisioGraph.main("--force", "src/test/resources/SimpleGraph.xml", "firstVisio.vdx");
    VisioGraph.main("src/test/resources/SimpleGraphNew.xml", "firstVisio.vdx");
  }


  private static abstract class ResourceHandler<T extends Closeable> {
    String resourceName;

    protected ResourceHandler(String resourceName) {
      this.resourceName = resourceName;
    }

    protected T open() throws IOException {
      throw new IOException("'open' not implemented");
    }

    protected abstract void doWithCloseable(T resource) throws Exception;

    public void execute() throws IOException {
      T resource = open();
      execute(resource);
    }

    public void execute(T resource) throws IOException {
      if (resource != null) {
        IOException exception = null;
        try {
          doWithCloseable(resource);
        } catch (Exception e) {
          exception = new IOException("problem loadeing: " + resourceName, e);
        } finally {
          try {
            resource.close();
          } catch (IOException e) {
            if (exception == null) {
              exception = new IOException("colud not close: " + resourceName, e);
            } else {
              e.printStackTrace();
            }
            throw exception;
          }
        }
      } else {
        throw new FileNotFoundException("could not open resource: " + resourceName);
      }

    }


  }

  public void addShape(VisioShape visioShape) {
    Element newXmlNode = visioShape.getShapeElement();

    if (shapesContainer == null) {
      Nodes nodes = document.getRootElement().query(_SHAPES_CONTAINER, XmlHelper.VISIO_XPATH_CONTEXT);
      if (nodes.size() == 0) {
        Element page = XmlHelper.getSingleElement(document.getRootElement(), _PAGE);
        Element pageSheet = XmlHelper.getSingleElement(document.getRootElement(), _PAGE_SHEET);
        int posPageSheet = page.indexOf(pageSheet);
        shapesContainer = new Element("Shapes", XmlHelper.VISIO_SCHEMA);
        page.insertChild(shapesContainer, posPageSheet + 1);
      } else {
        shapesContainer = (Element) nodes.get(0);
        Node maxShapeId = shapesContainer.query(MAX_SHAPE_ID, XmlHelper.VISIO_XPATH_CONTEXT).get(0);
        if (maxShapeId != null) {
          nextShapeId = Integer.valueOf(maxShapeId.getValue()) + 1;
        }
      }
    }

    visioShape.setVisioID(nextShapeId);
    nextShapeId++;
    shapesContainer.appendChild(newXmlNode);
  }


  public void addConnect(int connectorVisioID, int sourceNodeVisioID, int targetNodeVisioID) {
    Element connectBegin = (Element) connectBeginTemplate.copy();
    Element connectEnd = (Element) connectEndTemplate.copy();
    XmlHelper.setValue(connectBegin, _C_CONNECT_FROM_SHEET, String.valueOf(connectorVisioID));
    XmlHelper.setValue(connectBegin, _C_CONNECT_TO_SHEET, String.valueOf(sourceNodeVisioID));

    XmlHelper.setValue(connectEnd, _C_CONNECT_FROM_SHEET, String.valueOf(connectorVisioID));
    XmlHelper.setValue(connectEnd, _C_CONNECT_TO_SHEET, String.valueOf(targetNodeVisioID));

    Element connectsContainer = (Element) XmlHelper.getSingleNode(document.getRootElement(), _CONNECTS_CONTAINER, true);
    if (connectsContainer == null) {
      Element page = XmlHelper.getSingleElement(document.getRootElement(), _PAGE);
      connectsContainer = new Element(CONNECTS_CONTAINER, XmlHelper.VISIO_SCHEMA);
      page.appendChild(connectsContainer);
    }
    // Element connectsContainer = XmlHelper.getSingleElement(document.getRootElement(), _CONNECTS_CONTAINER);
    connectsContainer.appendChild(connectBegin);
    connectsContainer.appendChild(connectEnd);

  }

  public BigDecimal getPageWidth() {
    String value = getValue(document.getRootElement(), _PAGE_WIDTH);
    if (value == null) {
      throw new VisioGraphException("could not determine page width: " + _PAGE_WIDTH);
    }
    return new BigDecimal(value).multiply(VisioHelper.INCH);
  }

  public BigDecimal getPageHeight() {
    String value = getValue(document.getRootElement(), _PAGE_HEIGHT);
    if (value == null) {
      throw new VisioGraphException("could not determine page width: " + _PAGE_HEIGHT);
    }
    return new BigDecimal(value).multiply(VisioHelper.INCH);
  }

  public Document getDocument() {
    return document;
  }

  public Graph getGraph() {
    return graph;
  }


}
