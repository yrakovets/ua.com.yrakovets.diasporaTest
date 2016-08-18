package categories;

import core.DiasporaTest;
import org.junit.After;
import org.junit.Test;
import pages.DiasporaPage;

/**
 * Created by Legatus on 18.08.2016.
 */
public class EndToEndTest extends DiasporaTest {

    DiasporaPage page = new DiasporaPage();

    @Test
    public void endToEnd(){

        page.login(user2.getLogin(), user2.getPassword());
        page.assertIsLogined(user2.getLogin());

        page.findUser(user1.getLogin());
        page.assertUserPage(user1.getLogin());

        page.addFriendFromProfilePage();
        page.assertFriendInList(user1.getLogin(), true);

        page.logout();

        page.login(user1.getLogin(), user1.getPassword());

        //assertNotification(assertTextInNotifications(),user2.getLogin() +DIASPORA_DOMAIN);

        page.findUser(user2.getLogin());
        page.addFriendFromProfilePage();

        String messageText = "Hi!";
        page.sendMessage(messageText, user2.getLogin() + page.DIASPORA_DOMAIN, messageText);
        page.assertDialogIsInBox(user1.getLogin(), messageText);

        String postText = "This is Sparta!!";
        page.addPost(postText);
        page.assertPostIsPublished(postText);

        page.logout();

        page.login(user2.getLogin(),user2.getPassword());

        String commentText = "Great!";
        page.commentPost(user1.getLogin(), postText, commentText);
        //assertPostIsCommented(user1.getLogin(), postText);

        page.likePost(user1.getLogin(), postText);
        //assertPostIsLiked(user1.getLogin(), postText);

        page.logout();

        page.login(user1.getLogin(),user1.getPassword());

        page.deletePost(postText);

        page.logout();
    }

    @After
    public void teardown(){
        try{
            page.logout();
        }catch(Throwable e){}

        page.login(user1.getLogin(), user1.getPassword());
        page.unfriendAll();
        page.logout();

        page.login(user2.getLogin(), user2.getPassword());
        page.unfriendAll();
        page.logout();
    }



}



