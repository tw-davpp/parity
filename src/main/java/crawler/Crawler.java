package crawler;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import database.ImageDownload;
import database.ProductStore;
import org.xml.sax.SAXException;
import xml.DomParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Crawler {

    String website = "http://www.amazon.cn/s/ref=sr_pg_1?rh=n%3A664978051%2Cn%3A665002051&bbn=665002051&ie=UTF8&qid=1366386905";
    private HtmlPage page;
    private final DomParser domParser;

    public Crawler() throws IOException, ParserConfigurationException, SAXException, InterruptedException {
        WebClient client = new WebClient(BrowserVersion.CHROME);
        client.setJavaScriptEnabled(false);

        page = client.getPage(website);
        domParser = new DomParser(new File("amazon.xml"));
        domParser.parse();

        while (true) {
            pageParser();
            nextPage();
        }
    }

    private void nextPage() throws IOException {
        HtmlAnchor htmlAnchor = page.getAnchorByText(domParser.getNextPageText());
        page = htmlAnchor.click();
    }

    public void pageParser() {
        List<String> resultXPathList = domParser.getProductsXPath();
        for (String resultXPath : resultXPathList) {
            List<HtmlElement> elementList = (List<HtmlElement>) page.getByXPath(resultXPath);

            for (HtmlElement htmlElement : elementList) {
                DomNodeList<DomNode> childNodes = htmlElement.getChildNodes();
                for (DomNode domNode : childNodes) {
                    String title = getPruductInfo(domNode, domParser.getProductTitleXPath());
                    if (!title.equals("")) {
                        String url = getPruductInfo(domNode, domParser.getProductTitleXPath(), "href");
                        String imageSource = getPruductInfo(domNode, domParser.getProductImageXPath(), domParser.getproductImageSource());
                        String price = getPruductInfo(domNode, domParser.getProductPriceXPath());
                        String score = getPruductInfo(domNode, domParser.getProductScoreXPath(), domParser.getProductScoreText());

                        String fileName = null;
                        try {
                            fileName = ImageDownload.download(imageSource);
                            ProductStore productStore = new ProductStore();
                            productStore.store(title, url, fileName, price, score);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            cleanFile(fileName);
                            continue;
                        }
                    }
                }
            }
        }
    }

    private void cleanFile(String fileName) {
        File file = new File(fileName);
        if (file.exists())
            file.delete();
    }

    private String getPruductInfo(DomNode domNode, String xpath, String attribute) {
        List<HtmlElement> elementList = (List<HtmlElement>) domNode.getByXPath(xpath);
        if (elementList.size() > 0)
            return elementList.get(0).getAttribute(attribute);
        return "";
    }

    private String getPruductInfo(DomNode domNode, String xpath) {
        List<HtmlElement> productTitle = (List<HtmlElement>) domNode.getByXPath(xpath);
        if (productTitle.size() > 0)
            return productTitle.get(0).asText();
        return "";
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, InterruptedException {
        Crawler crawler = new Crawler();
    }
}
