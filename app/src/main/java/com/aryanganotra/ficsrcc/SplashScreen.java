package com.aryanganotra.ficsrcc;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {

    Snackbar snackbar;



        @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





            //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

            snackbar= Snackbar.make(findViewById(android.R.id.content),"Connecting to FIC...",Snackbar.LENGTH_INDEFINITE);
            snackbar.show();



}

public void kill(){
    this.finish();
}

    @Override
    protected void onDestroy() {
        if (snackbar!=null&&snackbar.isShownOrQueued()){
            snackbar.dismiss();
        }
        super.onDestroy();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!Check.getInstance()){
            Toast.makeText(this,"Please check your internet connection",Toast.LENGTH_LONG).show();
            if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                startActivity(new Intent(SplashScreen.this,UserActivity.class));
                kill();

            }
            else {
                Intent intent=new Intent(SplashScreen.this, FirebaseAuthUI.class);
                startActivity(intent);
                kill();
            }
        }
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseDatabase.getInstance().getReference("contactus").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    Constants.PHONE = (String) dataSnapshot.child("phno").getValue();
                    Constants.EMAIL = (String) dataSnapshot.child("emailid").getValue();
                    Constants.WEBSITE = (String) dataSnapshot.child("website").getValue();
                    Constants.FACEBOOK = (String) dataSnapshot.child("fburl").getValue();
                    Constants.INSTA = (String) dataSnapshot.child("instaurl").getValue();
                    Constants.LINKEDIN = (String) dataSnapshot.child("linkedinurl").getValue();
                    Constants.TWITTER = (String) dataSnapshot.child("twitterurl").getValue();
                    Constants.VINTAGEAPI = (String) dataSnapshot.child("vintageapi").getValue();
                    Constants.INSTAAPI = (String) dataSnapshot.child("instaapi").getValue();
                    Constants.ABOUT = (String) dataSnapshot.child("aboutus").getValue();
                    if (!Check.getInstance()){

                        snackbar= Snackbar.make(findViewById(android.R.id.content),"No Internet Connection",Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                startActivity(new Intent(SplashScreen.this,SplashScreen.class));

                            }
                        });
                        snackbar.show();

                    }
                    else {
                        startActivity(new Intent(SplashScreen.this,UserActivity.class));
                        kill();


                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {

            Intent intent=new Intent(SplashScreen.this, FirebaseAuthUI.class);
            startActivity(intent);
            kill();


        }
    }

}
