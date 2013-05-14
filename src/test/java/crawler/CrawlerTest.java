package crawler;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class CrawlerTest {
    String website = "http://www.amazon.cn/s/ref=nb_sb_noss_1?__mk_zh_CN=%E4%BA%9A%E9%A9%AC%E9%80%8A%E7%BD%91%E7%AB%99&url=search-alias%3Daps&field-keywords=ipad";
    private HtmlPage page;
    private WebClient client;

    @Before
    public void set_up() throws IOException {
        client = new WebClient(BrowserVersion.CHROME);
        page = client.getPage(website);
    }

    @Test
    public void test_amazon_product_original() {
        String productListXPath = "//div[contains(@class,'list results apsListAligned')]";
        List<HtmlElement> elementList = (List<HtmlElement>) page.getByXPath(productListXPath);

        HtmlElement htmlElement = elementList.get(0);
        DomNodeList<DomNode> childNodes = htmlElement.getChildNodes();

        String xpath = "ul/li[contains(@class,'newp')]/del";
        assertEquals("￥268.00", showPruductInfo(childNodes.get(1), xpath));
    }


    @Test
    public void test_amazon_next_page() throws IOException {
        page = client.getPage("http://www.amazon.cn/b/ref=sd_allcat_baby_l3_b1982062051?ie=UTF8&node=1982062051");
        HtmlAnchor htmlAnchor = (HtmlAnchor) page.getElementById("pagnNextLink");
        HtmlPage nextPage = htmlAnchor.click();
        assertEquals("婴幼儿尿裤 - 母婴用品 - 亚马逊", nextPage.getTitleText());
    }

    @Test
    public void test_amazon_product_website() {
        String productListXPath = "//div[contains(@class,'results')]";
        List<HtmlElement> elementList = (List<HtmlElement>) page.getByXPath(productListXPath);

        for (HtmlElement htmlElement : elementList) {
            DomNodeList<DomNode> childNodes = htmlElement.getChildNodes();
            for (DomNode domNode : childNodes) {
                String pruductTitleXPath = "h3[contains(@class,'newaps')]/a";
                showPruductInfo(domNode, pruductTitleXPath);
            }
        }
    }

    private String showPruductInfo(DomNode domNode, String pruductTitleXPath) {
        List<HtmlElement> productTitle = (List<HtmlElement>) domNode.getByXPath(pruductTitleXPath);
        if (productTitle.size() > 0) {
            HtmlElement htmlElement = productTitle.get(0);
            System.out.println(((HtmlAnchor)htmlElement).getHrefAttribute());
            return htmlElement.asText();
        }
        return "";
    }
}

