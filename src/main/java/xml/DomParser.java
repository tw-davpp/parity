package xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DomParser {

    public static final int TITLE_XPATH_SITE = 0;
    public static final int IMAGE_XPATH_SITE = 1;
    public static final int PRICE_XPATH_SITE = 2;
    public static final int SCORE_XPATH_SITE = 3;

    private final Document document;
    private String productTitleXPath;
    private String productImageXPath;
    private String productPriceXPath;
    private String productScoreXPath;
    private List<String> productsXPath;
    private String nextPageText;
    private String productScoreText;
    private String productImageSource;

    public DomParser(File file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        document = documentBuilder.parse(file);
    }

    public String getProductTitleXPath() {
        return productTitleXPath;
    }

    public void parse() {
        productsXPath = new ArrayList<String>();
        NodeList layout = document.getElementsByTagName("layout");
        for (int i = 0; i < layout.getLength(); i++) {
            productsXPath.add(getXpath(layout.item(i)));
        }

        NodeList product = document.getElementsByTagName("product");
        productTitleXPath = getXpath(product.item(TITLE_XPATH_SITE));
        productImageXPath = getXpath(product.item(IMAGE_XPATH_SITE));
        productImageSource = ((Element) product.item(IMAGE_XPATH_SITE)).getAttribute("source");
        productPriceXPath = getXpath(product.item(PRICE_XPATH_SITE));
        productScoreXPath = getXpath(product.item(SCORE_XPATH_SITE));
        productScoreText = ((Element) product.item(SCORE_XPATH_SITE)).getAttribute("text");

        Element nextPage = (Element) document.getElementsByTagName("nextPage").item(0);
        nextPageText = nextPage.getAttribute("text");
    }

    private String getXpath(Node item) {
        Element element = (Element) item;
        return element.getAttribute("xpath");
    }

    public String getProductImageXPath() {
        return productImageXPath;
    }

    public String getProductPriceXPath() {
        return productPriceXPath;
    }

    public String getProductScoreXPath() {
        return productScoreXPath;
    }

    public List<String> getProductsXPath() {
        return productsXPath;
    }

    public String getNextPageText() {
        return nextPageText;
    }

    public String getProductScoreText() {
        return productScoreText;
    }

    public String getproductImageSource() {
        return productImageSource;
    }
}
