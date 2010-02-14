package de.artive.visiograph;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import static de.artive.visiograph.helper.XmlHelper.getValue;

/**
 * Created by IntelliJ IDEA. User: vivo Date: Feb 14, 2010 Time: 4:20:50 PM To change this template use File | Settings
 * | File Templates.
 */
public class VisioDocumentTemplate {

    public static final BigDecimal INCH = new BigDecimal("25.4");

    public static final String DEFAULT_VISIO_TEMPLATE = "visio_blank_a4_landscape.vdx";

    public static final String _ROOT = "/v:VisioDocument";
    public static final String _PAGE = _ROOT + "/v:Pages/v:Page[@ID=\"0\"]";
    public static final String _PAGE_PROPERTIES = _PAGE + "/v:PageSheet/v:PageProps";
    public static final String _PAGE_WIDTH = _PAGE_PROPERTIES + "/v:PageWidth";
    public static final String _PAGE_HEIGHT = _PAGE_PROPERTIES + "/v:PageHeight";


    private Document document = null;

    public VisioDocumentTemplate() throws VisioGraphException {
        Builder builder = new Builder();
        InputStream docStream = null;
        docStream = VisioDocumentTemplate.class.getClassLoader().getResourceAsStream(DEFAULT_VISIO_TEMPLATE);
        Exception originalException = null;
        try {
            document = builder.build(docStream);
        } catch (ParsingException e) {
            originalException = e;
            throw new VisioGraphException("error parsing: " + DEFAULT_VISIO_TEMPLATE, e);
        } catch (IOException e) {
            originalException = e;
            throw new VisioGraphException("could not load: " + DEFAULT_VISIO_TEMPLATE, e);
        } finally {
            try {
                docStream.close();
            } catch (IOException e) {
                if (originalException == null) {
                    throw new VisioGraphException("error closing stream for: " + DEFAULT_VISIO_TEMPLATE, e);
                } else {
                    e.printStackTrace();
                }
            }

        }
    }

    public BigDecimal getPageWidth() {
        String value = getValue(document.getRootElement(), _PAGE_WIDTH);
        if (value == null) {
            throw new VisioGraphException("could not determine page width: " + _PAGE_WIDTH);
        }
        return new BigDecimal(value).multiply(INCH);
    }

    public BigDecimal getPageHeight() {
        String value = getValue(document.getRootElement(), _PAGE_HEIGHT);
        if (value == null) {
            throw new VisioGraphException("could not determine page width: " + _PAGE_HEIGHT);
        }
        return new BigDecimal(value).multiply(INCH);
    }

    public static void main(String[] args) {
        VisioDocumentTemplate template = new VisioDocumentTemplate();
        System.out.println(template.getPageWidth().toPlainString());
        System.out.println(template.getPageHeight().toPlainString());
    }
}
