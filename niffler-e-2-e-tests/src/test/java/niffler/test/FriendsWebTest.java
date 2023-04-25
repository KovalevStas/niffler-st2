package niffler.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.AllureId;
import niffler.jupiter.annotation.User;
import niffler.jupiter.extension.UsersQueueExtension;
import niffler.model.UserJson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static io.qameta.allure.Allure.step;
import static niffler.jupiter.annotation.User.UserType.INVITATION_SENT;
import static niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;

@ExtendWith(UsersQueueExtension.class)
public class FriendsWebTest extends BaseWebTest {

    @AllureId("102")
    @Test
    void friendsShouldBeVisible0(@User(userType = WITH_FRIENDS) UserJson user, @User(userType = WITH_FRIENDS) UserJson user2) {
        step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
        step("Проверка списка друзей под " + user.getUsername(), () -> {
            $("a[href*='redirect']").click();
            $("input[name='username']").setValue(user.getUsername());
            $("input[name='password']").setValue(user.getPassword());
            $("button[type='submit']").click();

            $("a[href*='friends']").click();
            $$(".table tbody tr").shouldHave(sizeGreaterThan(0));
            $(".header__logout").click();
        });
        step("Проверка списка друзей под " + user2.getUsername(), () -> {
            $("a[href*='redirect']").shouldBe(Condition.visible, Duration.ofSeconds(5)).click();
            $("input[name='username']").setValue(user2.getUsername());
            $("input[name='password']").setValue(user2.getPassword());
            $("button[type='submit']").click();

            $("a[href*='friends']").click();
            $$(".table tbody tr").shouldHave(sizeGreaterThan(0));
            $(".header__logout").click();
        });
    }

    @AllureId("103")
    @Test
    void friendsShouldBeVisible1(@User(userType = WITH_FRIENDS) UserJson user, @User(userType = INVITATION_SENT) UserJson user2) {
        step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
        step("Проверка списка друзей под " + user.getUsername(), () -> {
            $("a[href*='redirect']").click();
            $("input[name='username']").setValue(user.getUsername());
            $("input[name='password']").setValue(user.getPassword());
            $("button[type='submit']").click();

            $("a[href*='friends']").click();
            $$(".table tbody tr").shouldHave(sizeGreaterThan(0));
            $(".header__logout").click();
        });
        step("Проверка отправленного запроса в друзья у пользователя " + user.getUsername(), () -> {
            $("a[href*='redirect']").click();
            $("input[name='username']").setValue(user2.getUsername());
            $("input[name='password']").setValue(user2.getPassword());
            $("button[type='submit']").click();

            $("a[href*='people']").click();
            $$(".table tbody tr").find(Condition.text("Pending invitation"))
                    .should(Condition.visible);
        });
    }

}
