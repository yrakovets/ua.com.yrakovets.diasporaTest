package categories;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import core.DiasporaTest;
import org.junit.Before;
import org.junit.Test;
import pages.DiasporaPage;

/**
 * Created by Legatus on 18.08.2016.
 */
public class MentionAndFormattingTest extends DiasporaTest {
    final String numberedListTag = "ol";
    final String itemTag = "li";
    final String bulletedListTag = "ul";
    final String quoteTag = "blockquote";
    final String header11lvlTag = "h1";
    final String header12lvlTag = "h2";
    final String header13lvlTag = "h3";
    DiasporaPage page = new DiasporaPage();


    @Before
    public void loginDiasp(){
        page.login(user1.getLogin(), user1.getPassword());
    }

    @Test
    public void headings(){
        String header1lvlText = "Text for header 1 lvl";
        String header2lvlText = "Text for header 2 lvl";
        String header3lvlText = "Text for header 3 lvl";

        //WHEN
        page.addPost("# " + header1lvlText + "\n" + "## " + header2lvlText + "\n" + "### " + header3lvlText);

        //THEN
        page.assertPostContainsFormattedText(header1lvlText, header11lvlTag);
        page.assertPostContainsFormattedText(header2lvlText, header12lvlTag);
        page.assertPostContainsFormattedText(header3lvlText, header13lvlTag);

        page.deletePost(header1lvlText);
    }

    @Test
    public void bulletedList(){
        String listHeader = "This will be list 1 case";
        String item1 = " item 1";
        String item2 = " item 2";
        String item3 = " item 3";
        String itemSymbol = "*";

        //WHEN
        page.addPost(listHeader + "\n" +
                itemSymbol + item1 + "\n" +
                itemSymbol + item2 + "\n" +
                itemSymbol + item3
        );

        //THEN
        SelenideElement post = page.findPost(user1.getLogin(),listHeader);
        post.$$(itemTag).shouldHaveSize(3);
        post.$(bulletedListTag).shouldBe(Condition.exist);

        page.deletePost(listHeader);
    }

}
