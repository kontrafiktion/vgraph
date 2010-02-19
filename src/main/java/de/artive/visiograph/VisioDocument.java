package de.artive.visiograph;

import de.artive.visiograph.helper.Constants;
import de.artive.visiograph.helper.XmlHelper;
import nu.xom.*;
import nu.xom.Node;

import java.io.*;
import java.math.BigDecimal;

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
  public static final String _CONNECTS_CONTAINER = _PAGE + "/v:Connects";
  public static final String MAX_SHAPE_ID = "v:Shape[not(following-sibling::v:Shape/@ID > @ID) and not(preceding-sibling::v:Shape/@ID > @ID)]/@ID";


  int nextShapeId = 1;

  private Document document = null;
  private Element shapesContainer;


  public VisioDocument() throws IOException {
    this(DEFAULT_VISIO_TEMPLATE);
  }


  public VisioDocument(String templateName) throws IOException {

    if (templateName != null) {
      loadDocument(templateName);
    } else {
      loadDocument(DEFAULT_VISIO_TEMPLATE);
    }
  }

  private void loadDocument(String templateName) throws IOException {
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


  public static void main(String[] args) throws IOException {
    VisioDocument visioDocument = new VisioDocument();
    visioDocument.addShape(null);
  }

  // TODO: xmlElement or VisioShape?

  public void addShape(Element xmlNode) {
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

    String attrName = "ID";
    String attrValue = String.valueOf(nextShapeId);
    XmlHelper.setAttribute(xmlNode, attrName, attrValue);

    nextShapeId++;
    shapesContainer.appendChild(xmlNode);
    // System.out.println(document.toXML());
  }

  public BigDecimal getPageWidth() {
    String value = getValue(document.getRootElement(), _PAGE_WIDTH);
    if (value == null) {
      throw new VisioGraphException("could not determine page width: " + _PAGE_WIDTH);
    }
    return new BigDecimal(value).multiply(Constants.INCH);
  }

  public BigDecimal getPageHeight() {
    String value = getValue(document.getRootElement(), _PAGE_HEIGHT);
    if (value == null) {
      throw new VisioGraphException("could not determine page width: " + _PAGE_HEIGHT);
    }
    return new BigDecimal(value).multiply(Constants.INCH);
  }

  public Document getDocument() {
    return document;
  }

}
