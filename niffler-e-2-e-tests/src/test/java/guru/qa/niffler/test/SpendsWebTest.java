package guru.qa.niffler.test;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.jupiter.extension.GenerateCategoryExtension;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.test.web.BaseWebTest;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ExtendWith(GenerateCategoryExtension.class)
@Disabled
public class SpendsWebTest extends BaseWebTest {

    static {
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    void doLogin() {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue("stanislav.kovalev");
        $("input[name='password']").setValue("12345");
        $("button[type='submit']").click();
    }

    @ApiLogin(user = @GenerateUser(
            categories = @Category("Обучение"),
            spends = @GenerateSpend(
                    description = "QA GURU ADVANCED VOL 2",
                    currency = CurrencyValues.RUB,
                    amount = 52000.00
            )
    ))
    @AllureId("107")
    @Test
    void spendShouldBeDeletedByActionInTable(SpendJson spend) {
        Selenide.open(CFG.getFrontUrl() + "/main");

        $(".spendings-table tbody").$$("tr")
                .find(text(spend.getDescription()))
                .$("td")
                .scrollTo()
                .click();

        $$(".button_type_small").find(text("Delete selected"))
                .click();

        $(".spendings-table tbody")
                .$$("tr").filter(text(spend.getDescription()))
                .shouldHave(CollectionCondition.size(0));
    }
}
