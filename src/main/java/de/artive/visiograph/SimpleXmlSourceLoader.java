package de.artive.visiograph;

import de.artive.visiograph.helper.XmlHelper;
import nu.xom.*;
import nu.xom.Node;

import java.io.IOException;
import java.util.Iterator;

import static de.artive.visiograph.helper.NodesIterator.nodesIterator;

/**
 * Simple loader for XML based models. It uses a {@link de.artive.visiograph.GraphXPathProvider} to provide XPath
 * expressions to extract the Nodes, Edges and their attributes.
 * <p/>
 * User: vivo Date: Feb 13, 2010 Time: 7:09:31 PM
 */
public class SimpleXmlSourceLoader implements SourceLoader {
    private GraphXPathProvider graphXPathProvider;
    private Document document;

    /**
     * Create a new SimpleXmlSourceLoader from the given document using the given {@link
     * de.artive.visiograph.GraphXPathProvider} The model can then be loaded into a Graph using {@link
     * #loadInto(Graph)}.
     * <p/>
     * You could also use the static factory method {@link #load(nu.xom.Document, GraphXPathProvider)} to create a new
     * Graph from scratch
     *
     * @param document
     * @param graphXPathProvider
     */
    public SimpleXmlSourceLoader(Document document, GraphXPathProvider graphXPathProvider) {
        this.document = document;
        this.graphXPathProvider = graphXPathProvider;
    }

    public static Graph load(Document document, GraphXPathProvider graphXPathProvider) {
        SimpleXmlSourceLoader simpleXmlSourceLoader = new SimpleXmlSourceLoader(document, graphXPathProvider);
        Graph graph = new Graph();
        simpleXmlSourceLoader.loadInto(graph);
        return graph;
    }

    @Override
    public void loadInto(Graph graph) {

        Nodes xmlNodes = document.query(graphXPathProvider.getNodes());
        for (Iterator<Node> it = nodesIterator(xmlNodes); it.hasNext();) {
            Element xmlNode = (Element) it.next();
            String nodeExtId = XmlHelper.getValue(xmlNode, graphXPathProvider.getNodeExtId());
            String nodeText = XmlHelper.getValue(xmlNode, graphXPathProvider.getNodeText());
            graph.addNode(nodeExtId, nodeText);
        }
        Nodes xmlEdges = document.query(graphXPathProvider.getEdges());
        for (Iterator<Node> it = nodesIterator(xmlEdges); it.hasNext();) {
            Element xmlEdge = (Element) it.next();
            String edgeExtId = XmlHelper.getValue(xmlEdge, graphXPathProvider.getEdgeExtId());
            String edgeText = XmlHelper.getValue(xmlEdge, graphXPathProvider.getEdgeText());
            String edgeSourceExtId = XmlHelper.getValue(xmlEdge, graphXPathProvider.getEdgeSourceExtId());
            String edgeTargetExtId = XmlHelper.getValue(xmlEdge, graphXPathProvider.getEdgeTargetExtId());
            de.artive.visiograph.Node sourceNode = graph.getNode(edgeSourceExtId);
            de.artive.visiograph.Node targetNode = graph.getNode(edgeTargetExtId);
            if (sourceNode == null) {
                throw new VisioGraphException("could not find connect node with ID: " + edgeSourceExtId + " referenced by connection: " + edgeExtId);
            }
            if (targetNode == null) {
                throw new VisioGraphException("could not find connect node with ID: " + edgeTargetExtId + " referenced by connection: " + edgeExtId);
            }
            Edge edge = new Edge(edgeExtId, edgeText, sourceNode, targetNode);

        }
    }

    public static void main(String[] args) throws IOException, ParsingException {
        Builder builder = new Builder();
        Document document = builder.build("src/test/resources/SimpleGraph.xml");
        Graph graph = new Graph();
        SimpleXmlSourceLoader loader = new SimpleXmlSourceLoader(document, new SimpleXMLGraphXPathProvider());
        loader.loadInto(graph);
    }
}
