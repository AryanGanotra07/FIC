package com.aryanganotra.ficsrcc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.aryanganotra.ficsrcc.Articles.Article;
import com.aryanganotra.ficsrcc.Articles.ArticleFragment;
import com.aryanganotra.ficsrcc.Articles.ArticleService;
import com.aryanganotra.ficsrcc.Articles.ArticlesListFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserActivity extends AppCompatActivity implements ArticlesListFragment.onItemSelectedListener{


    private Snackbar snackbar;



    private FragmentTransaction transaction;
    private FragmentManager manager;



    @SuppressLint({"RestrictedApi", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

        snackbar = Snackbar.make(findViewById(android.R.id.content), "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(UserActivity.this, UserActivity.class));

            }
        });


        getdlink();

        ArticlesListFragment fragment = new ArticlesListFragment();

        manager = getSupportFragmentManager();

        transaction = manager.beginTransaction();
        if (manager.findFragmentByTag("listfragment") == null && manager.findFragmentByTag("articlefragment") == null) {

            transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);
            //  transaction.setCustomAnimations(R.anim.fui_slide_in_right,R.anim.fui_slide_out_left);
            transaction.add(R.id.layout, fragment, "listfragment");
            Log.i("Executed", "Useractivity-Oncreate");
            transaction.commit();

        }



    }



    private void getdlink() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)

                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            Uri deepLink = null;


                            if (pendingDynamicLinkData != null) {
                                deepLink = pendingDynamicLinkData.getLink();
                                int aId = Integer.parseInt(deepLink.getQueryParameter("id"));
                                Log.i("Id", String.valueOf(aId));
                                if (Constants.WEBSITE!=null) {
                                    DownloadArticle(aId);
                                }
                                else {
                                    FirebaseDatabase.getInstance().getReference("contactus").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            Constants.WEBSITE=(String)dataSnapshot.child("website").getValue();
                                            if (Constants.WEBSITE!=null){
                                                DownloadArticle(aId);
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }


                            }
                        } else {
                            startActivity(new Intent(UserActivity.this, FirebaseAuthUI.class));

                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (!Check.getInstance()) {
                            if (snackbar!=null) {
                                snackbar.show();
                            }
                        }
                    }
                });


    }


    private void DownloadArticle(int id) {


            Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.WEBSITE).addConverterFactory(GsonConverterFactory.create()).build();
            ArticleService service = retrofit.create(ArticleService.class);
            Call<Article> articleCall = service.getArticle(id,"");
            articleCall.enqueue(new Callback<Article>() {
                @Override
                public void onResponse(Call<Article> call, Response<Article> response) {

                    if (response.body()!=null) {

                        LoadArticleFragment(response.body());

                    }


                }

                @Override
                public void onFailure(Call<Article> call, Throwable t) {
                    DownloadArticle(id);
                }
            });



    }

    @Override
    public void OnArticleSelected(Article article) {

        LoadArticleFragment(article);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof ArticlesListFragment) {
            ArticlesListFragment articlesListFragment = (ArticlesListFragment) fragment;
            articlesListFragment.setOnItemSelectedListener(this);
        }


    }

    private void LoadArticleFragment(Article article) {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putParcelable("article", article);
        ArticleFragment articleFragment = new ArticleFragment();
        articleFragment.setArguments(bundle);

        transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);
        transaction.add(R.id.layout, articleFragment, "articlefragment");
        //transaction.setCustomAnimations(R.anim.fui_slide_in_right,R.anim.fui_slide_out_left);
        //transaction.setTransition(R.anim.down_from_top);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getdlink();
        snackbar = Snackbar.make(findViewById(android.R.id.content), "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(UserActivity.this, UserActivity.class));

            }
        });



    }




    @Override
    protected void onDestroy() {

       if (snackbar!=null&&snackbar.isShownOrQueued()){
           snackbar.dismiss();
       }

        super.onDestroy();
    }






}
