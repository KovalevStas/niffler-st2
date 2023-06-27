package guru.qa.niffler.jupiter.extension;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.jupiter.annotation.GqlRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.io.IOException;
import java.io.InputStream;

public class GqlResourceFileResolver implements ParameterResolver {

    private final ClassLoader cl = this.getClass().getClassLoader();
    private static final ObjectMapper om = new ObjectMapper();

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(JsonNode.class);
    }

    @Override
    public JsonNode resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        String pathToRequestFile = parameterContext.getParameter().getAnnotation(GqlRequest.class).value();
        try (InputStream is = cl.getResourceAsStream(pathToRequestFile)) {
            return om.readValue(is, JsonNode.class);
        } catch (IOException e) {
            throw new ParameterResolutionException(e.getMessage());
        }
    }
}
