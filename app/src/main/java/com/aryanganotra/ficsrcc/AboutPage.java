package com.aryanganotra.ficsrcc;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AboutPage extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
   private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_about);



      //  TextView text1=(TextView)findViewById(R.id.text1);
        TextView text2=(TextView)findViewById(R.id.text2);


        if (Constants.ABOUT!=null){
            text2.setText(Constants.ABOUT);
        }
        else {
            text2.setText("");
        }







        drawerLayout=(DrawerLayout) findViewById(R.id.drawerlayoutabout);
        mToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView=(NavigationView)findViewById(R.id.nv2);
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

                        startActivity(new Intent(AboutPage.this,UserActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.about:

                        startActivity(new Intent(AboutPage.this,AboutPage.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.discover:
                        startActivity(new Intent(AboutPage.this,Discover.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.logout:
                        if (Check.getInstance()) {

                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(AboutPage.this, FirebaseAuthUI.class));
                            kill();
                        }
                        else {
                            Toast.makeText(AboutPage.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                        }

                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.contact:
                        startActivity(new Intent(AboutPage.this,ContactPage.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.savedarticles:
                        startActivity(new Intent(AboutPage.this,SavedArticles.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                }


                return true;
            }
        });
    }

    public void kill(){
        AboutPage.this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)){

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
