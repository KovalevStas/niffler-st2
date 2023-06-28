package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.dao.NifflerUsersDAO;
import guru.qa.niffler.db.dao.NifflerUsersDAOJdbc;
import guru.qa.niffler.db.entity.UserEntity;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.test.web.BaseWebTest;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

/*@Disabled*/
public class LoginNewUserTest extends BaseWebTest {

    @Test
    @AllureId("118")
    @ApiLogin(user = @GenerateUser())
    void loginTest(UserJson user) {
        Selenide.open(CFG.getFrontUrl() + "/main");
        $("a[href*='friends']").click();
        $(".main-content__section").should(visible).shouldHave(text("There are no friends yet!"));
    }

    @Test
    @AllureId("119")
    @GenerateUser()
    void userLifecycleTest(UserJson ue) {
        UserEntity user;

        final NifflerUsersDAO usersDAO = new NifflerUsersDAOJdbc();
        String userId = usersDAO.getUserId(ue.getUsername());
        UserEntity originalUser = usersDAO.getUser(userId);
        System.out.println(originalUser.toString());
        user = originalUser;
        user.setUsername("amanda.dietrich2");
        user.setPassword("12345");
        user.setAccountNonExpired(false);
        user.setEnabled(false);
        usersDAO.updateUser(user);
        System.out.println(usersDAO.getUser(userId).toString());
        usersDAO.removeUser(user);
    }
}
