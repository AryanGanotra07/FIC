package com.aryanganotra.ficsrcc.Articles;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ArticleService {
    @GET("/wp-json/wp/v2/posts")
    Call<List<Article>> getArticles(@Query("categories") String category,@Query("per_page")int per_page,@Query("_embed")String empty);
    @GET("/wp-json/wp/v2/categories")
    Call<List<Category>> getCategories();
    @GET("/wp-json/wp/v2/posts/{path}")
    Call<Article> getArticle(@Path("path")int id,@Query("_embed") String embed);
    @GET("/wp-json/wp/v2/posts")
    Call<List<Article>> getAllArticles(@Query("per_page")int per_page,@Query("_embed")String empty);

}
