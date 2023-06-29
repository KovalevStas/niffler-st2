package guru.qa.niffler.soap;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.soap.converter.JaxbConverterFactory;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

public abstract class BaseSoapClient {

    protected static final Config CFG = Config.getConfig();

    protected final String serviceBaseUrl;
    protected final OkHttpClient httpClient;
    protected final Retrofit retrofit;

    public BaseSoapClient(String serviceBaseUrl) {
        this(serviceBaseUrl, false, null);
    }

    public BaseSoapClient(String serviceBaseUrl, boolean followRedirect) {
        this(serviceBaseUrl, followRedirect, null);
    }

    public BaseSoapClient(String serviceBaseUrl, boolean followRedirect, Interceptor... interceptors) {
        this.serviceBaseUrl = serviceBaseUrl;
        Builder builder = new Builder()
                .followRedirects(followRedirect);

        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                builder.addNetworkInterceptor(interceptor);
            }
        }

        builder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(BODY));
        this.httpClient = builder.build();

        try {
            this.retrofit = new Retrofit.Builder()
                    .client(httpClient)
                    .baseUrl(serviceBaseUrl)
                    .addConverterFactory(JaxbConverterFactory.create(JAXBContext.newInstance(
                            guru.qa.niffler.userdata.wsdl.CurrentUserRequest.class,
                            guru.qa.niffler.userdata.wsdl.CurrentUserResponse.class,
                            guru.qa.niffler.userdata.wsdl.User.class,
                            guru.qa.niffler.userdata.wsdl.Currency.class,
                            guru.qa.niffler.userdata.wsdl.FriendState.class
                    )))
                    .build();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
