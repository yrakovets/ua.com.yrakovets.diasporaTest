package pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.hasText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.thoughtworks.selenium.SeleneseTestNgHelper.assertEquals;

/**
 * Created by Legatus on 17.08.2016.
 */
public class DiasporaPage {

    public final String ASPECT_PUBLIC = "public";
    public final String ASPECT_ALL = "all_aspects";
    public final String DIASPORA_DOMAIN = "@diasp.eu";

    public ElementsCollection wall =  $$(".stream_element.loaded");
    public ElementsCollection searchResults = $$("#people_stream .media.stream_element");

    public void login(String login, String password){
        open("https://diasp.eu/users/sign_in");
        $("input#user_username").setValue(login);
        $("input#user_password").setValue(password);
        $("input[name='commit']").click();
    }

    public void findUser(String login){
        $("#q").click();
        $("#q").setValue(login).pressEnter();
        searchResults.find(text(login)).$(".hovercardable").click();
    }

    public void addFriendFromProfilePage() {
        $(".aspect_membership_dropdown button").click();
        $(".aspect_selector").click();

    }

    public void addFriendFromProfilePage(String aspectName) {
        $(".aspect_membership_dropdown button").click();
        $$(".aspect_selector").find(text(aspectName)).click();

    }

    public void logout() {
        $(".user-menu-more-indicator").click();
        $("a[href *= 'sign_out']").click();
        //user can have option to not ask for confirmation, so
        //this switching i get in try-catch
        try{
            switchTo().alert().accept();
        }catch(Throwable e){}
    }

    public String AddAspect(String aspectName){
        //create
        $("#stream_selection .all_aspects a").click();
        $("#stream_selection #aspects_list .selectable.new_aspect").click();
        $("#newAspectModal #aspect_name").setValue(aspectName);
        $("#newAspectModal .btn.creation").click();
        //and find ID of created aspect and return it
        $(".header-nav [href *= 'stream']").click();
        $("#stream_selection .all_aspects a").click();
        return $$("#stream_selection #aspects_list li").find(text(aspectName)).getAttribute("data-aspect_id");
    }


    public void addPost(String postText, String aspect) {
        $(".diaspora_header_logo.logos-header-logo").click();
        $("#status_message_fake_text").click();
        $("#status_message_fake_text").setValue(postText);
        $("button.btn.btn-default.dropdown-toggle").click();
        //deselect all selected aspects
        // note - it will be needed in multiple choise only
        for(SelenideElement aspectSelected: $$("ul.dropdown-menu.pull-right li.aspect_selector.selected")){
            aspectSelected.$(".status_indicator").click();
        }
        //and activate needed
        if (aspect.equals(ASPECT_ALL) || aspect.equals(ASPECT_PUBLIC)) {
            $$("ul.dropdown-menu.pull-right li").find(Condition.attribute("data-aspect_id", aspect)).click();

        } else {
            $$("ul.dropdown-menu.pull-right li").find(Condition.text(aspect)).click();
        }
        $("#submit").click();

    }

    public void assertPostIsPublished(String postText) {
        String user = $("#home_user_badge a").getText();
        wall.get(0).find(".author-name").shouldHave(text(user));
        wall.get(0).find(".markdown-content").shouldHave(text(postText));
    }

    public void assertPostIsInStream(String postText, String userLogin){
        wall.filter(text(postText)).filter(text(userLogin)).get(0).shouldBe(exist);
    }

    public void assertPostIsNotInStream(String postText, String userLogin){
        wall.filter(text(postText)).filter(text(userLogin)).shouldBe(CollectionCondition.empty);
    }

    public void unfriendAll(){
        $(".user-menu-more-indicator").click();
        $("a[href *= 'contacts']").click();
        ElementsCollection contacts = $$("#contact_stream [data-template = 'contact']");
        for(SelenideElement contact: contacts){
            contact.$("button[data-toggle='dropdown']").click();
            ElementsCollection aspects = contact.$$(".aspect_membership.dropdown-menu.pull-right li.aspect_selector.selected a");
            for(SelenideElement aspect: aspects){
                aspect.click();
            }
        }
    }

    public void deleteAspect(String aspectName){
        $(".header-nav [href *= 'stream']").click();
        $("#stream_selection .all_aspects a").click();
        $$("#stream_selection #aspects_list li").find(text(aspectName)).hover().$("a i.entypo.pencil").click();
        $("a#delete_aspect").click();
        switchTo().alert().accept();
    }

    public void deleteAllUsersPosts(){
        $(".header-nav [href *= 'activity']").click();
        //String author = $("#home_user_badge").getText();
        for(SelenideElement post:wall){
            deletePost(post.$(".post-content .markdown-content").getText());
        }
        $(".header-nav [href *= 'stream']").click();
    }

    public void deletePost(String postText) {
        wall.find(text(postText)).$(".media .bd .control-icons").hover().$(".delete .trash").click();
        switchTo().alert().accept();
    }


    public void addPost(String postText) {
        $(".diaspora_header_logo.logos-header-logo").click();
        $("#status_message_fake_text").click();
        $("#status_message_fake_text").setValue(postText);
        $("#submit").click();

    }

    public void assertPostContainsFormattedText(String text, String tag){
        String user = $("#home_user_badge a").getText();
        wall.filter(hasText(user)).get(0).$$(tag).filter(text(text)).shouldHaveSize(1);
    }

    public SelenideElement findPost(String login, String postText){
        return wall.filter(text(login+DIASPORA_DOMAIN)).find(text(postText));
    }

    public void assertIsLogined(String login) {
        assertEquals($("#home_user_badge h4 a").getText(), login);
    }

    public void assertUserPage(String login) {
        $("#author_info #name").shouldHave(text(login));
    }

    public void assertFriendInList(String login, boolean b) {

    }

    public void sendMessage(String text, String adresser, String theme){
        $("#conversations_badge .badge_link").click();
        $("#contact_ids").setValue(adresser);
        $$("#as-results-contact_ids li").find(text(adresser)).click();
        $("#conversation_subject").setValue(theme);
        $("#conversation_text").setValue(text);
        $(".btn.btn-primary.creation").click();
    }

    public void assertDialogIsInBox(String login, String theme){
        $("#conversations_badge .badge_link").click();
        $$(".stream.conversations .conversation-wrapper").filter(text(login)).find(text(theme)).shouldBe(exist);
    }

    public void commentPost(String login, String postText, String commentText){
        SelenideElement post = findPost(login, postText);
        post.$(".focus_comment_textarea").click();
        post.$(".comment_box").setValue(commentText);
        post.$(".submit_button input").click();
    }

    public void likePost(String login, String postText){
        findPost(login, postText).$(".like").click();
    }
}
