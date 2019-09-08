package com.aryanganotra.ficsrcc;

import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.TooltipCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aryanganotra.ficsrcc.Instagram.InstagramModel;
import com.aryanganotra.ficsrcc.Instagram.InstagramService;
import com.aryanganotra.ficsrcc.Instagram.ViewPagerAdapterM;
import com.aryanganotra.ficsrcc.Instagram.ViewPagerAdapterS;
import com.aryanganotra.ficsrcc.alphavantagestockapp.AlphaVantageAPIService;
import com.google.android.gms.common.util.Strings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Discover extends AppCompatActivity {


    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private ViewPager viewPager;

    private ViewPager viewPager1;


    private Snackbar snackbar;
    private int n;
    private static String instaurl="";


    private LinearLayout.LayoutParams params1;

    private InstagramModel instaData;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_discover);





        snackbar= Snackbar.make(findViewById(android.R.id.content),"No Internet Connection",Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Discover.this,Discover.class));

            }
        });





        if (!Check.getInstance()){
            if (snackbar!=null&&!snackbar.isShown()) {
                snackbar.show();
            }
        }




       viewPager=(ViewPager)findViewById(R.id.viewpager);

        viewPager.setPadding(100,0,100,0);
        viewPager.setClipToPadding(false);
        viewPager.setPageMargin(20);

        viewPager1=(ViewPager)findViewById(R.id.viewpager1);


        viewPager1.setClipToPadding(false);
        viewPager1.setPadding(280,0,280,0);
        viewPager1.setPageMargin(20);

        getData();



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (instaData.getData().get(i).getCarousel_media()!=null) {
                    n = instaData.getData().get(i).getCarousel_media().size();
                }
                else if (instaData.getData().get(i).getType().equals("image") && instaData.getData().get(i).getImages()!=null&& instaData.getData().get(i).getImages().getStandard_resolution().getUrl()!=null){
                    n=1;

                }
                else {
                    n=0;
                }
               viewPager1.setAdapter(new ViewPagerAdapterS(instaData,i));

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)viewPager.getLayoutParams();
        int lHeight=params.height;

        params1=(LinearLayout.LayoutParams)viewPager1.getLayoutParams();
        int sHeight=params1.height;










        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (!(viewPager1.getHeight() < lHeight)) {
                    params1.height = sHeight;
                    viewPager1.setLayoutParams(params1);
                    viewPager1.setPadding(280, 0, 280, 0);
                   // animate(sHeight,280);
                }
                    return false;

            }
        });

        viewPager1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (viewPager1.getHeight()<lHeight){

                    params1.height=lHeight;
                    viewPager1.setLayoutParams(params1);
                    viewPager1.setPadding(100,0,100,0);

                }

                return false;
            }
        });



        Timer timer=new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(),2000,4000);


        Button follow=(Button)findViewById(R.id.followbutton);
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constants.INSTA!=null) {
                    Intent i3 = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(Constants.INSTA));
                    startActivity(i3);
                }
                else {
                    Intent i3 = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(instaurl));
                    startActivity(i3);
                }


            }
        });







        drawerLayout=(DrawerLayout) findViewById(R.id.drawerlayoutdiscover);
        mToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView=(NavigationView)findViewById(R.id.nv4);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.navigation_username);
        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            navUsername.setText("Hello, "+FirebaseAuth.getInstance().getCurrentUser().getDisplayName());}
         navigationView.getHeaderView(0).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.home:

                        startActivity(new Intent(Discover.this,UserActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.about:

                        startActivity(new Intent(Discover.this,AboutPage.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.logout:
                        if (Check.getInstance()) {

                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(Discover.this, FirebaseAuthUI.class));
                            kill();
                        }
                        else {
                            Toast.makeText(Discover.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                        }

                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.contact:

                        startActivity(new Intent(Discover.this,ContactPage.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.discover:

                        startActivity(new Intent(Discover.this,Discover.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.savedarticles:

                        drawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(Discover.this,SavedArticles.class));
                        break;

                }


                return true;
            }
        });
    }

    private void getData() {

        if (Constants.INSTAAPI != null) {

            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.instagram.com").addConverterFactory(GsonConverterFactory.create()).build();
            InstagramService service = retrofit.create(InstagramService.class);
            Call<InstagramModel> callInsta = service.getInstaData(Constants.INSTAAPI);
            callInsta.enqueue(new Callback<InstagramModel>() {
                @Override
                public void onResponse(Call<InstagramModel> call, Response<InstagramModel> response) {

                    if (snackbar != null && snackbar.isShown()) {
                        snackbar.dismiss();

                    }


                    instaData = response.body();
                    for (int i = 0; i < instaData.getData().size(); i++) {
                        if (instaData.getData().get(i).getType().equals("video")) {
                            instaData.getData().remove(i);
                        }

                    }
                    if(instaData.getData().get(0).getCarousel_media()!=null) {
                        n = instaData.getData().get(0).getCarousel_media().size();
                    }
                    else if (instaData.getData().get(0).getType().equals("image") && instaData.getData().get(0).getImages()!=null&& instaData.getData().get(0).getImages().getStandard_resolution().getUrl()!=null){
                        n=1;

                    }
                    else {
                        n=0;
                    }
                    if (viewPager != null) {
                        viewPager.setAdapter(new ViewPagerAdapterM(instaData));
                    }
                    if (viewPager1 != null) {
                        Toast.makeText(Discover.this, "Touch the view to enlarge", Toast.LENGTH_LONG).show();
                        viewPager1.setAdapter(new ViewPagerAdapterS(instaData, 0));
                    }

                }

                @Override
                public void onFailure(Call<InstagramModel> call, Throwable t) {

                    getData();
                }
            });

        }
        else {
            FirebaseDatabase.getInstance().getReference("contactus").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Constants.INSTAAPI = (String) dataSnapshot.child("instaapi").getValue();
                    Constants.INSTA=(String)dataSnapshot.child("instaurl").getValue();
                    if (Constants.INSTAAPI!=null){
                        getData();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void kill(){
        Discover.this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();


        snackbar= Snackbar.make(findViewById(android.R.id.content),"No Internet Connection",Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Discover.this,Discover.class));

            }
        });









    }






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)){

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            Discover.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (viewPager1.getHeight() != viewPager.getHeight()) {

                        if (viewPager1.getCurrentItem() != n - 1) {
                            viewPager1.setCurrentItem(viewPager1.getCurrentItem() + 1);
                        } else {
                            viewPager1.setCurrentItem(0);
                        }

                    }
                }

            });

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


            if (snackbar!=null&&snackbar.isShownOrQueued()){
            snackbar.dismiss();
            }





    }




}

