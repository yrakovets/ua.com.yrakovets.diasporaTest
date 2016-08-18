package core;

import org.junit.Before;

import static com.codeborne.selenide.Selenide.open;

/**
 * Created by Legatus on 17.08.2016.
 */
public class DiasporaTest extends BaseTest {

    public DiaUser user1 = new DiaUser("yrakovets","last15");
    public DiaUser user2 = new DiaUser("tasjtest","last15");

    @Before
    public void openDiaspora(){
        open("https://diasp.eu/");
    }

    public class DiaUser {

        private String login;
        private String password;

        public DiaUser(String login, String password){
            this.login = login;
            this.password = password;
        }

        public String getLogin(){
            return login;
        }

        public String getPassword(){
            return password;
        }

    }

}
