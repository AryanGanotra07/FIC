package com.aryanganotra.ficsrcc;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.util.ExtraConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.List;

public class FirebaseAuthUI extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    Snackbar snackbar;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_firebase_auth_ui);



        snackbar= Snackbar.make(findViewById(android.R.id.content),"No Internet Connection",Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(FirebaseAuthUI.this,FirebaseAuthUI.class));


            }
        });

     if (!Check.getInstance()){

         snackbar.show();
      }









    AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
            .Builder(R.layout.activity_firebase_auth_ui)
            .setGoogleButtonId(R.id.googlesign)
            .setEmailButtonId(R.id.emailsign)
            .setTosAndPrivacyPolicyId(R.id.tos)



            .build();


    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),

            new AuthUI.IdpConfig.GoogleBuilder().build());

// Create and launch sign-in intent
    startActivityForResult(
            AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setAuthMethodPickerLayout(customLayout)
                    .setTosAndPrivacyPolicyUrls("","")

                    .setTheme(R.style.GreenTheme)
                    .build(),
            RC_SIGN_IN);


//Emaildynamiclink

    if (AuthUI.canHandleIntent(getIntent())) {
        if (getIntent().getExtras() == null) {
            return;
        }
        String link = getIntent().getExtras().getString(ExtraConstants.EMAIL_LINK_SIGN_IN);
        if (link != null) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setEmailLink(link)
                            .setTheme(R.style.GreenTheme)
                            .setAvailableProviders(providers)
                            .setTosAndPrivacyPolicyUrls("","")
                            .setAuthMethodPickerLayout(customLayout)
                            .build(),
                    RC_SIGN_IN);
        }
    }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
               /* startActivity(new Intent(FirebaseAuthUI.this,UserActivity.class));
                finish();
 */
                snackbar = Snackbar.make(findViewById(android.R.id.content), "Signing in...", Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
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

                                    startActivity(new Intent(FirebaseAuthUI.this,SplashScreen.class));

                                }
                            });

                        }
                        else {
                            startActivity(new Intent(FirebaseAuthUI.this,UserActivity.class));
                            kill();

                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                // ...
            } else {


            }
        }

    }

    public void kill(){
        FirebaseAuthUI.this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        if (snackbar!=null&&snackbar.isShownOrQueued()){
            snackbar.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        kill();
    }
}

