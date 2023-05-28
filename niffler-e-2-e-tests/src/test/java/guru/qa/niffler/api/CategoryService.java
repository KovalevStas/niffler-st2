package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface CategoryService {

    @GET("/categories")
    Call<List<CategoryJson>> getCategories(@Body String username);

    @POST("/category")
    Call<CategoryJson> addCategory(@Body CategoryJson category);
}
