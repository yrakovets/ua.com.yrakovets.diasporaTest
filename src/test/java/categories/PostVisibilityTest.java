package categories;

import core.DiasporaTest;
import org.junit.After;
import org.junit.Test;
import pages.DiasporaPage;

/**
 * Created by Legatus on 17.08.2016.
 */

//we will test aspects and visibility in one class, because this two effects are too connected

public class PostVisibilityTest extends DiasporaTest {

    DiasporaPage page = new DiasporaPage();
    final String ASPECT1 = "AspectTest1";
    final String ASPECT2 = "AspectTest2";

    @Test
    public void postVisibilityTest(){

        //GIVEN
        //User 2 is friend of user 1 by aspect ASPECT1
        page.login(user2.getLogin(), user2.getPassword());
        page.findUser(user1.getLogin());
        page.addFriendFromProfilePage();
        page.logout();
        page.login(user1.getLogin(), user1.getPassword());
        //add aspect ASPECT1
        //adding return id of this aspect, so separate assertion is unnecessary
        page.AddAspect(ASPECT1);
        page.AddAspect(ASPECT2);
        page.findUser(user2.getLogin());
        page.addFriendFromProfilePage(ASPECT1);

        //WHEN
        //user1 writes 4 posts with different visibility settings:
        //1 - for all friend categories
        //2 - for category ASPECT1
        //3 - for category ASPECT2
        //4 - public

        //WHEN1
        String postForAllCategories = "This post for all categories";
        page.addPost(postForAllCategories, page.ASPECT_ALL);
        //THEN1
        page.assertPostIsPublished(postForAllCategories);

        //WHEN2
        String postForAspect1 = "This post for aspect1";
        page.addPost(postForAspect1, ASPECT1);
        //THEN2
        page.assertPostIsPublished(postForAllCategories);

        //WHEN3
        String postForAspect2 = "This post for aspect2";
        page.addPost(postForAspect2, ASPECT2);
        //THEN3
        page.assertPostIsPublished(postForAspect2);

        //WHEN4
        String postForPublic = "This post public";
        page.addPost(postForPublic, page.ASPECT_PUBLIC);
        //THEN4
        page.assertPostIsPublished(postForPublic);

        page.logout();
        page.login(user2.getLogin(),user2.getPassword());

        //THEN1
        page.assertPostIsInStream(postForAllCategories, user1.getLogin());
        //THEN2
        page.assertPostIsInStream(postForAspect1, user1.getLogin());
        //THEN3
        page.assertPostIsInStream(postForPublic, user1.getLogin());
        //THEN4
        page.assertPostIsNotInStream(postForAspect2, user1.getLogin());

        page.logout();
    }


    @After
    public void teardown(){

        //we don't know, test finished successfully or no, the logout was be done or no
        //so we try to logout always
        try{
            page.logout();
        }catch(Throwable e){}

        page.login(user1.getLogin(), user1.getPassword());
        page.unfriendAll();
        page.deleteAspect(ASPECT1);
        page.deleteAspect(ASPECT2);
        page.deleteAllUsersPosts();
        page.logout();

        page.login(user2.getLogin(), user2.getPassword());
        page.unfriendAll();
        page.logout();
    }


}
