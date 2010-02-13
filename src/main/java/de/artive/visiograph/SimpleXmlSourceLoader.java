package de.artive.visiograph;

import nu.xom.*;
import nu.xom.Node;

import java.io.IOException;
import java.util.Iterator;

import static de.artive.visiograph.helper.NodesIterator.nodesIterator;

/**
 * Created by IntelliJ IDEA.
 * User: vivo
 * Date: Feb 13, 2010
 * Time: 7:09:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleXmlSourceLoader implements SourceLoader {
    private GraphXPathProvider graphXPathProvider;
    private Document document;

    public SimpleXmlSourceLoader(Document document, GraphXPathProvider graphXPathProvider) {
        this.document = document;
        this.graphXPathProvider = graphXPathProvider;
    }

    @Override
    public void loadInto(Graph graph) {

        Nodes xmlNodes = document.query(graphXPathProvider.getNodes());
        for (Iterator<Node> it = nodesIterator(xmlNodes); it.hasNext();) {
            Element xmlNode = (Element) it.next();
            String nodeExtId = xmlNode.query(graphXPathProvider.getNodeExtId()).get(0).getValue();
            String nodeText = xmlNode.query(graphXPathProvider.getNodeText()).get(0).getValue();
            graph.addNode(nodeExtId, nodeText);
        }
        Nodes xmlEdges = document.query(graphXPathProvider.getEdges());
        for (Iterator<Node> it = nodesIterator(xmlEdges); it.hasNext();) {
            Element xmlEdge = (Element) it.next();
            String edgeExtId = xmlEdge.query(graphXPathProvider.getEdgeExtId()).get(0).getValue();
            String edgeText = xmlEdge.query(graphXPathProvider.getEdgeText()).get(0).getValue();
            String edgeSourceExtId = xmlEdge.query(graphXPathProvider.getEdgeSourceExtId()).get(0).getValue();
            String edgeTargetExtId = xmlEdge.query(graphXPathProvider.getEdgeTargetExtId()).get(0).getValue();
            de.artive.visiograph.Node sourceNode = graph.getNode(edgeSourceExtId);
            de.artive.visiograph.Node targetNode = graph.getNode(edgeTargetExtId);
            if ( sourceNode == null ) {
                throw new VisioGraphException("could not find connect node with ID: " + edgeSourceExtId + " referenced by connection: " + edgeExtId);
            }
            if ( targetNode == null ) {
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
