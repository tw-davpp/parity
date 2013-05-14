package xml;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;

public class DomParserTest {
    private DomParser domParser;

    @Before
    public void set_up() throws ParserConfigurationException, IOException, SAXException {
        File file = new File("amazon.xml");
        domParser = new DomParser(file);
    }

    @Test
    public void test_dom() {
        domParser.parse();
        assertEquals("//div[contains(@id,'atfResults')]", domParser.getProductsXPath().get(0));
        assertEquals("//div[contains(@id,'btfResults')]", domParser.getProductsXPath().get(1));

        assertEquals("h3[contains(@class,'newaps')]/a", domParser.getProductTitleXPath());
        assertEquals("div[contains(@class,'image imageContainer']/a/img", domParser.getProductImageXPath());
        assertEquals("src", domParser.getproductImageSource());
        assertEquals("ul/li[contains(@class,'newp')]/span", domParser.getProductPriceXPath());
        assertEquals("ul/li/span/a/img", domParser.getProductScoreXPath());
        assertEquals("alt", domParser.getProductScoreText());

        assertEquals("下一页", domParser.getNextPageText());
    }
}
