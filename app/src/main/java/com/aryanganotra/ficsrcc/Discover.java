package com.aryanganotra.ficsrcc;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aryanganotra.ficsrcc.Instagram.InstagramModel;
import com.aryanganotra.ficsrcc.Instagram.InstagramService;
import com.aryanganotra.ficsrcc.Instagram.ViewPagerAdapterM;
import com.aryanganotra.ficsrcc.Instagram.ViewPagerAdapterS;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
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
    private ViewPager viewPager, vp1, vp2;

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





        viewPager=findViewById(R.id.viewpager);
//        viewPager.setPadding(100,0,100,0);
//        viewPager.setClipToPadding(false);
        viewPager.setPageMargin(20);

//        vp1=findViewById(R.id.vp_2);
////        vp1.setPadding(100,0,100,0);
////        vp1.setClipToPadding(false);
////        vp1.setPageMargin(20);
//
//        vp2=findViewById(R.id.vp_3);
//        vp2.setPadding(100,0,100,0);
//        vp2.setClipToPadding(false);
//        vp2.setPageMargin(20);

        getImages();
//
//        viewPager1=(ViewPager)findViewById(R.id.viewpager1);
//
//
//        viewPager1.setClipToPadding(false);
//        viewPager1.setPadding(280,0,280,0);
//        viewPager1.setPageMargin(20);

//        getData();
//
//
//
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
//
//            }
//
//            @Override
//            public void onPageSelected(int i) {
//                if (instaData.getData().get(i).getCarousel_media()!=null) {
//                    n = instaData.getData().get(i).getCarousel_media().size();
//                }
//                else if (instaData.getData().get(i).getType().equals("image") && instaData.getData().get(i).getImages()!=null&& instaData.getData().get(i).getImages().getStandard_resolution().getUrl()!=null){
//                    n=1;
//
//                }
//                else {
//                    n=0;
//                }
//               viewPager1.setAdapter(new ViewPagerAdapterS(instaData,i));
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//
//            }
//        });
//
//        LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)viewPager.getLayoutParams();
//        int lHeight=params.height;
//
//        params1=(LinearLayout.LayoutParams)viewPager1.getLayoutParams();
//        int sHeight=params1.height;
//
//
//
//
//
//
//
//
//
//
//        viewPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                if (!(viewPager1.getHeight() < lHeight)) {
//                    params1.height = sHeight;
//                    viewPager1.setLayoutParams(params1);
//                    viewPager1.setPadding(280, 0, 280, 0);
//                   // animate(sHeight,280);
//                }
//                    return false;
//
//            }
//        });
//
//        viewPager1.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (viewPager1.getHeight()<lHeight){
//
//                    params1.height=lHeight;
//                    viewPager1.setLayoutParams(params1);
//                    viewPager1.setPadding(100,0,100,0);
//
//                }
//
//                return false;
//            }
//        });
//
//
//
//        Timer timer=new Timer();
//        timer.scheduleAtFixedRate(new MyTimerTask(),2000,4000);
//
//
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
//
//
//
//



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

    private void getImages() {
        ViewPagerAdapterM adapterM = new ViewPagerAdapterM();
        ViewPagerAdapterM adapterM2 = new ViewPagerAdapterM();
        ViewPagerAdapterM adapterM3 = new ViewPagerAdapterM();
//        LinearLayoutManager lm = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
//        LinearLayoutManager lm2 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
//        LinearLayoutManager lm3 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
//        viewPager.setLayoutManager(lm);
//        vp1.setLayoutManager(lm2);
//        vp2.setLayoutManager(lm3);
        viewPager.setAdapter(adapterM);
//        vp1.setAdapter(adapterM2);
//        vp2.setAdapter(adapterM3);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        ArrayList<String> images1 = new ArrayList<>();
        ArrayList<String> images2 = new ArrayList<>();
        ArrayList<String> images3 = new ArrayList<>();
        storage.getReference("images").listAll().addOnCompleteListener(new OnCompleteListener<ListResult>() {
            @Override
            public void onComplete(@NonNull Task<ListResult> task) {
                task.getResult().getItems().get(0).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Log.d("MediaGallery", task.getResult().toString());
                    }
                });

                if (task.getResult() != null) {
                    List<StorageReference> items = task.getResult().getItems();
                    if (items != null) {
                        for (StorageReference item : items) {
                            item.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Log.d("MediaGallery",task.getResult().toString());
                                    adapterM.addImage(task.getResult().toString());
                                }
                            });
                        }
                    }
                }

            }



        });

//        storage.getReference("images2").listAll().addOnCompleteListener(new OnCompleteListener<ListResult>() {
//            @Override
//            public void onComplete(@NonNull Task<ListResult> task) {
//                task.getResult().getItems().get(0).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Uri> task) {
//                        Log.d("MediaGallery", task.getResult().toString());
//                    }
//                });
//
//                if (task.getResult() != null) {
//                    List<StorageReference> items = task.getResult().getItems();
//                    if (items != null) {
//                        for (StorageReference item : items) {
//                            item.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Uri> task) {
//                                    Log.d("MediaGallery",task.getResult().toString());
//                                    adapterM2.addImage(task.getResult().toString());
//                                }
//                            });
//                        }
//                    }
//                }
//
//            }
//
//
//
//        });
//
//        storage.getReference("images3").listAll().addOnCompleteListener(new OnCompleteListener<ListResult>() {
//            @Override
//            public void onComplete(@NonNull Task<ListResult> task) {
//                task.getResult().getItems().get(0).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Uri> task) {
//                        Log.d("MediaGallery", task.getResult().toString());
//                    }
//                });
//
//                if (task.getResult() != null) {
//                    List<StorageReference> items = task.getResult().getItems();
//                    if (items != null) {
//                        for (StorageReference item : items) {
//                            item.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Uri> task) {
//                                    Log.d("MediaGallery",task.getResult().toString());
//                                    adapterM3.addImage(task.getResult().toString());
//                                }
//                            });
//                        }
//                    }
//                }
//
//            }
//
//
//
//        });
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
//                        viewPager.setAdapter(new ViewPagerAdapterM(instaData));
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

