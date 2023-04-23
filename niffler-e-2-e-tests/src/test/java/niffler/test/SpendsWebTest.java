package niffler.test;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import niffler.jupiter.GenerateCategory;
import niffler.jupiter.GenerateCategoryExtension;
import niffler.jupiter.GenerateSpend;
import niffler.jupiter.GenerateSpendExtension;
import niffler.model.CurrencyValues;
import niffler.model.SpendJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ExtendWith(GenerateCategoryExtension.class)
@ExtendWith(GenerateSpendExtension.class)
public class SpendsWebTest {

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

    @GenerateCategory(
            username = "stanislav.kovalev",
            category = "New category"
    )
    @GenerateSpend(
            username = "stanislav.kovalev",
            description = "QA GURU ADVANCED VOL 2",
            currency = CurrencyValues.RUB,
            amount = 52000.00,
            category = "New category"
    )
    @Test
    void spendShouldBeDeletedByActionInTable(SpendJson spend) {
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
