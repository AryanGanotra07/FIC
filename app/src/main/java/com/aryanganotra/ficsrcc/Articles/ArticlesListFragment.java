package com.aryanganotra.ficsrcc.Articles;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aryanganotra.ficsrcc.AboutPage;
import com.aryanganotra.ficsrcc.Check;
import com.aryanganotra.ficsrcc.Constants;
import com.aryanganotra.ficsrcc.ContactPage;
import com.aryanganotra.ficsrcc.Discover;
import com.aryanganotra.ficsrcc.FirebaseAuthUI;
import com.aryanganotra.ficsrcc.R;
import com.aryanganotra.ficsrcc.SavedArticles;
import com.aryanganotra.ficsrcc.UserActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.victor.loading.book.BookLoading;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticlesListFragment extends Fragment{

    private RecyclerView recyclerView;
    private Spinner spinner;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private ArticleService service;
    private RecyclerAdapter1 adapter1;
    private onItemSelectedListener callback;
    private List<Article> articles=new ArrayList<>();
    private List<Category> categories=new ArrayList<>();
    private BookLoading bookLoading;
    private ProgressBar progressBarH;
    private Context context;
    private SpinnerAdapter spinnerAdapter;
    private Snackbar snackbar;
    private static String BASE_URL;


    public void setOnItemSelectedListener(onItemSelectedListener callback){
        this.callback=callback;
    }


    public interface onItemSelectedListener{
        public void OnArticleSelected(Article article);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_user, container, false);
        return v;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (mToggle.onOptionsItemSelected(item)) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }

    private void LaunchDrawer() {



        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawerlayout);
        mToggle = new ActionBarDrawerToggle(this.getActivity(), drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) getActivity().findViewById(R.id.nv1);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.navigation_username);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            navUsername.setText("Hello, " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        }
        if (isAdded()&&context!=null) {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()) {
                        case R.id.home:

                            startActivity(new Intent(context, UserActivity.class));
                            drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                        case R.id.about:

                            startActivity(new Intent(context, AboutPage.class));
                            drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                        case R.id.logout:

                            if (Check.getInstance()) {

                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(context, FirebaseAuthUI.class));
                                // kill();
                            } else {
                                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            }

                            drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                        case R.id.contact:

                            startActivity(new Intent(context, ContactPage.class));
                            drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                        case R.id.savedarticles:
                            startActivity(new Intent(context, SavedArticles.class));
                            drawerLayout.closeDrawer(GravityCompat.START);
                            break;
                        case R.id.discover:
                            startActivity(new Intent(context, Discover.class));
                            drawerLayout.closeDrawer(GravityCompat.START);
                            break;

                    }


                    return true;
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (snackbar!=null&&snackbar.isShownOrQueued()){
            snackbar.dismiss();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setHasOptionsMenu(true);







    }

    private void initiateservice(){
        FirebaseDatabase.getInstance().getReference("contactus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Constants.WEBSITE = (String) dataSnapshot.child("website").getValue();
                if (Constants.WEBSITE!=null){
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.WEBSITE).addConverterFactory(GsonConverterFactory.create()).build();
                    service = retrofit.create(ArticleService.class);
                    adapter1=new RecyclerAdapter1(context,callback,service);
                    recyclerView.setAdapter(adapter1);
                    getCategories(service, spinnerAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void kill() {
        getActivity().finish();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        snackbar = Snackbar.make(getView(), "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(context, UserActivity.class));

            }
        });

        if (!Check.getInstance()){
            snackbar.show();
        }




        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)getView().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


        LaunchDrawer();


        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        SnapHelper snapHelper = new PagerSnapHelper();
        recyclerView.setLayoutManager(layoutManager);
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter1);




       bookLoading=(BookLoading)getView().findViewById(R.id.bookloading);
       bookLoading.start();

        bookLoading.setVisibility(View.VISIBLE);
        progressBarH=(ProgressBar)getView().findViewById(R.id.progressBarH);



        spinner = (Spinner) getView().findViewById(R.id.spinner);
        spinnerAdapter=new SpinnerAdapter(context,android.R.layout.simple_spinner_item,categories);
       spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category category=spinnerAdapter.getItem(position);
                progressBarH.setVisibility(View.VISIBLE);
                        getArticles(category.getId());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (Constants.WEBSITE!=null) {


            Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.WEBSITE).addConverterFactory(GsonConverterFactory.create()).build();
            service = retrofit.create(ArticleService.class);
            adapter1=new RecyclerAdapter1(context,callback,service);
            recyclerView.setAdapter(adapter1);
            getCategories(service, spinnerAdapter);
        }
        else {
            initiateservice();
        }
















        android.support.v7.widget.SearchView searchView= (android.support.v7.widget.SearchView)getView().findViewById(R.id.searchbutton);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (adapter1!=null && adapter1.getArticles()!=null) {
                    (adapter1).getExampleFilter().filter(s);
                }
                return false;
            }
        });








    }

    private void getCategories(ArticleService service,SpinnerAdapter spinnerAdapter){
        if (adapter1==null){
            adapter1=new RecyclerAdapter1(context,callback,service);
            recyclerView.setAdapter(adapter1);
        }
        Call<List<Category>> categoriesC=service.getCategories();
        categoriesC.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {

                if(snackbar!=null&&snackbar.isShown()){
                    snackbar.dismiss();
                }


                 categories=response.body();
                 Category category=new Category("All",-1,10);
                 categories.add(0,category);

                 bookLoading.animate().translationY(200).setDuration(500).start();

                 spinnerAdapter.setCategories(categories);

            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {




                getCategories(service,spinnerAdapter);

            }
        });
    }


    private void getArticles(int id) {
        if (adapter1==null){
            adapter1=new RecyclerAdapter1(context,callback,service);
            recyclerView.setAdapter(adapter1);
        }

        Category category= (Category) spinner.getSelectedItem();
        if (category!=null){
            id=category.getId();
        }


        if (id!=-1) {

            Call<List<Article>> articlesCall = service.getArticles(String.valueOf(id), 100,"");
            articlesCall.enqueue(new Callback<List<Article>>() {
                @Override
                public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {

                   if(snackbar!=null&&snackbar.isShown()){
                        snackbar.dismiss();
                    }


                   bookLoading.setVisibility(View.GONE);
                    progressBarH.setVisibility(View.GONE);
                    articles = response.body();
                    if (category.getId()==((Category)spinner.getSelectedItem()).getId()) {


                            adapter1.setArticles(articles);

                    }
                    else {
                        getArticles(((Category)spinner.getSelectedItem()).getId());
                    }


                }

                @Override
                public void onFailure(Call<List<Article>> call, Throwable t) {
                    Category category= (Category) spinner.getSelectedItem();
                   getArticles(category.getId());

                }
            });

        }
        else if (id==-1){
            Call<List<Article>> callArticles=service.getAllArticles(100,"");
            callArticles.enqueue(new Callback<List<Article>>() {
                @Override
                public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {


                    if(snackbar!=null&&snackbar.isShown()){
                        snackbar.dismiss();
                    }



                    bookLoading.setVisibility(View.GONE);
                    progressBarH.setVisibility(View.GONE);
                    articles = response.body();
                    if (category.getId()==((Category)spinner.getSelectedItem()).getId()) {
                        adapter1.setArticles(articles);
                    }
                    else {
                        getArticles(((Category)spinner.getSelectedItem()).getId());
                    }
                }

                @Override
                public void onFailure(Call<List<Article>> call, Throwable t) {
                    Category category= (Category) spinner.getSelectedItem();
                    getArticles(category.getId());


                }
            });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);

    }




        }





