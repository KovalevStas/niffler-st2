package guru.qa.niffler.condition;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SpendCondition {

    public static CollectionCondition spends(SpendJson... expectedSpends) {
        return new CollectionCondition() {
            @Override
            @ParametersAreNonnullByDefault
            public void fail(CollectionSource collection, @Nullable List<WebElement> elements, @Nullable Exception lastError, long timeoutMs) {
                if (elements == null || elements.isEmpty()) {
                    throw new ElementNotFound(collection, List.of("Can`t find elements"), lastError);
                } else if (elements.size() != expectedSpends.length) {
                    throw new SpendsSizeMismatch(collection, Arrays.asList(expectedSpends), bindElementsToSpends(elements), explanation, timeoutMs);
                } else {
                    throw new SpendsMismatch(collection, Arrays.asList(expectedSpends), bindElementsToSpends(elements), explanation, timeoutMs);
                }
            }

            @Override
            public boolean missingElementSatisfiesCondition() {
                return false;
            }

            @Override
            public boolean test(List<WebElement> elements) {
                if (elements.size() != expectedSpends.length) {
                    return false;
                }
                for (int i = 0; i < expectedSpends.length; i++) {
                    if (assertEqualsSpendToWebElement(elements.get(i), expectedSpends[i]))
                        return false;
                }
                return true;
            }

            private boolean assertEqualsSpendToWebElement(WebElement row, SpendJson expectedSpend) {
                DateFormat dateFormat = new SimpleDateFormat("dd MMM yy", new Locale("en", "EN"));
                return row.findElements(By.cssSelector("td")).get(1).getText().equals(dateFormat.format(expectedSpend.getSpendDate()))
                        && row.findElements(By.cssSelector("td")).get(2).getText().equals(expectedSpend.getAmount().toString())
                        && row.findElements(By.cssSelector("td")).get(3).getText().equals(expectedSpend.getCurrency().toString())
                        && row.findElements(By.cssSelector("td")).get(4).getText().equals(expectedSpend.getCategory())
                        && row.findElements(By.cssSelector("td")).get(5).getText().equals(expectedSpend.getDescription());
            }

            private List<SpendJson> bindElementsToSpends(List<WebElement> elements) {
                return elements.stream()
                        .map(e -> {
                            List<WebElement> cells = e.findElements(By.cssSelector("td"));
                            SpendJson actual = new SpendJson();
                            SimpleDateFormat formatter6 = new SimpleDateFormat("dd MMM yy", new Locale("en", "EN"));
                            try {
                                actual.setSpendDate(formatter6.parse(cells.get(1).getText()));
                            } catch (ParseException ex) {
                                throw new RuntimeException(ex);
                            }
                            actual.setAmount(Double.valueOf(cells.get(2).getText()));
                            actual.setCurrency(CurrencyValues.valueOf(cells.get(3).getText()));
                            actual.setCategory(cells.get(4).getText());
                            actual.setDescription(cells.get(5).getText());
                            return actual;
                        })
                        .collect(Collectors.toList());
            }
        };
    }
}
