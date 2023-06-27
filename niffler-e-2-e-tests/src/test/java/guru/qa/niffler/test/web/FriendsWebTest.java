package guru.qa.niffler.test.web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.Friend;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.PeoplePage;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static guru.qa.niffler.condition.FriendsCondition.friends;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SENT;
import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;
import static io.qameta.allure.Allure.step;

public class FriendsWebTest extends BaseWebTest {

    @GenerateUser(
            friends = @Friend
    )
    @AllureId("102")
    @Test
    void friendsShouldBeVisible0(UserJson user) {
        final UserJson friend = user.getFriends().get(0);

        Allure.step("open page", () -> Selenide.open(CFG.getFrontUrl() + "/main"));
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue(user.getPassword());
        $("button[type='submit']").click();

        $("a[href*='friends']").click();
        $$(".table tbody tr").should(friends(friend));
    }

    @GenerateUser(
            outcomeInvitations = @Friend
    )
    @AllureId("103")
    @Test
    void friendsShouldBeVisible1(@User(userType = INVITATION_SENT) UserJson user) {
        final UserJson outcomeInvitation = user.getOutcomeInvitations().get(0);

        Allure.step("open page", () -> Selenide.open(CFG.getFrontUrl() + "/main"));
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(user.getUsername());
        $("input[name='password']").setValue(user.getPassword());
        $("button[type='submit']").click();

        $("a[href*='people']").click();
        $$(".table tbody tr")
                .find(Condition.text(outcomeInvitation.getUsername()))
                .should(Condition.text("Pending invitation"));
    }

    @AllureId("104")
    @GenerateUser(
            friends = @Friend
    )
    @Test
    void AddFriend(@User(userType = WITH_FRIENDS) UserJson user) {
        step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
        step("Проверка списка друзей под " + user.getUsername(), () -> {
            $("a[href*='redirect']").click();
            $("input[name='username']").setValue(user.getUsername());
            $("input[name='password']").setValue(user.getPassword());
            $("button[type='submit']").click();
        });
        $("a[href*='people']").click();
        PeoplePage peoplePage = new PeoplePage();

        peoplePage.checkThatPageLoaded()
                .addUserToFriends("bill")
                .getHeader()
                .logout();

    }
}
