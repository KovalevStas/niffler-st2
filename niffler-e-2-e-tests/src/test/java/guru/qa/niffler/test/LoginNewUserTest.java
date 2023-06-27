package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.dao.NifflerUsersDAO;
import guru.qa.niffler.db.dao.NifflerUsersDAOJdbc;
import guru.qa.niffler.db.entity.UserEntity;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
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
    @GenerateUser()
    void loginTest(UserJson user) {
        Allure.step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue(user.getPassword());
        $("button[type='submit']").click();

        $("a[href*='friends']").click();
        $(".header").should(visible).shouldHave(text("Niffler. The coin keeper."));
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
