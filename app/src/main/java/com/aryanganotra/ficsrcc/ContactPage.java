 package com.aryanganotra.ficsrcc;

import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

 public class ContactPage extends AppCompatActivity implements View.OnClickListener {


     private DrawerLayout drawerLayout;
     private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;



     private String phno="";
     private String emailid="";
     private String  websiteurl="";
     private String fburl="";
     private String instaurl="";
     private String linkedinurl="";
     private String twitterurl="";



     public void kill(){
         ContactPage.this.finish();
     }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_contact);


loadfirebased();



        drawerLayout=(DrawerLayout) findViewById(R.id.drawerlayoutcontact);



        mToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView=(NavigationView)findViewById(R.id.nv3);
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

                        startActivity(new Intent(ContactPage.this,UserActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.about:

                        startActivity(new Intent(ContactPage.this,AboutPage.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.logout:
                        if (Check.getInstance()) {

                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(ContactPage.this, FirebaseAuthUI.class));
                           kill();
                        }
                        else {
                            Toast.makeText(ContactPage.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                        }

                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.contact:

                        startActivity(new Intent(ContactPage.this,ContactPage.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.discover:

                        startActivity(new Intent(ContactPage.this,Discover.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.savedarticles:
                        startActivity(new Intent(ContactPage.this,SavedArticles.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                }


                return true;
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



     @Override
     public void onClick(View v) {

        switch (v.getId()){

            case R.id.callbutton:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phno, null));
                startActivity(intent);
                break;

            case R.id.emailbutton:
                Intent intent1 = new Intent(Intent.ACTION_SENDTO);
                intent1.setData(Uri.parse(emailid)); // only email apps should handle this
               // intent1.putExtra(Intent.EXTRA_EMAIL, addresses);
               // intent1.putExtra(Intent.EXTRA_SUBJECT, subject);
                if (intent1.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent1);
                }
                break;

            case R.id.websitebutton:
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(websiteurl));
                startActivity(i);
                break;


            case R.id.facebook:
                Intent i6= new Intent(Intent.ACTION_VIEW,
                        Uri.parse(fburl));
                startActivity(i6
                );
                break;

            case R.id.linkedin:
                Intent i2 = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(linkedinurl));
                startActivity(i2);
                break;

            case R.id.instagram:
                Intent i3 = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(instaurl));
                startActivity(i3);
                break;

            case R.id.twitter:
                Intent i4 = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(twitterurl));
                startActivity(i4);
                break;






        }



     }

     public void loadfirebased(){

         if (Constants.PHONE!=null){
             phno=Constants.PHONE;

         }
         else {
             phno="";
         }
         if (Constants.EMAIL!=null){
             emailid=Constants.EMAIL;
         }
         else {
             emailid="";
         }
         if (Constants.WEBSITE!=null){
             websiteurl=Constants.WEBSITE;
         }
         else {
             websiteurl="";
         }
         if (Constants.INSTA!=null){
             instaurl=Constants.INSTA;
         }
         else {
             instaurl="";
         }
         if (Constants.FACEBOOK!=null){
             fburl=Constants.FACEBOOK;
         }
         else {
             fburl="";
         }
         if (Constants.LINKEDIN!=null){
             linkedinurl=Constants.LINKEDIN;
         }
         else {
             linkedinurl="";
         }
         if (Constants.TWITTER!=null){
             twitterurl=Constants.TWITTER;
         }
         else {
             twitterurl="";
         }










     }

     @Override
     protected void onStart() {
         super.onStart();
         loadfirebased();

     }
 }
