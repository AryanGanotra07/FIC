package com.aryanganotra.ficsrcc.Articles;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aryanganotra.ficsrcc.AboutPage;
import com.aryanganotra.ficsrcc.BuildConfig;
import com.aryanganotra.ficsrcc.Check;
import com.aryanganotra.ficsrcc.ContactPage;
import com.aryanganotra.ficsrcc.Discover;
import com.aryanganotra.ficsrcc.FirebaseAuthUI;
import com.aryanganotra.ficsrcc.R;
import com.aryanganotra.ficsrcc.SavedArticles;
import com.aryanganotra.ficsrcc.UserActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;


public class ArticleFragment extends Fragment {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private Article article;
    private static final String websiteurl="http://www.ficsrcc.com/";
    private Context context;
    private SeekBar seekBar;
    private String style="<style>img{display: inline;height: auto;max-width: 100%;}*{font: 22px/40px Cormorant, serif; }</style>";
    private boolean isChecked=false;

    public ArticleFragment(){

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context=getContext();
        return inflater.inflate(R.layout.fragment_article,container,false);

    }



    private void LaunchDrawer() {


        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawerlayout);
        mToggle = new ActionBarDrawerToggle(this.getActivity(), drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        // ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) getActivity().findViewById(R.id.nv1);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.navigation_username);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            navUsername.setText("Hello, " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        }

         //   navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);

        }

        private NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=new NavigationView.OnNavigationItemSelectedListener() {
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
                        if (isAdded()) {
                            startActivity(new Intent(context, SavedArticles.class));
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.discover:
                        startActivity(new Intent(context, Discover.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                }

                return true;
            }
        };



    @Override
    public void onPause() {
        super.onPause();
    }

 /*   @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;

    }
    */


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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        article=getArguments().getParcelable("article");

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


      //  LaunchDrawer();


        ScrollView scrollView=(ScrollView)view.findViewById(R.id.scrollView);





            WebView webView=(WebView) view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        loadwebView(article.getContent().getRendered(),webView,style);








       ImageView imageView=(ImageView)view.findViewById(R.id.image);
       if (article.get_embedded()!=null&&article.get_embedded().getWpfeaturemedia()!=null) {

           Glide.with(getContext().getApplicationContext()).load(article.get_embedded().getWpfeaturemedia().get(0).getSource_url()).into(imageView);
           imageView.setVisibility(View.VISIBLE);

       }


        TextView title=(TextView)view.findViewById(R.id.title);
        title.setText(Html.fromHtml(article.getTitle().getRendered()));


        CardView cardView=(CardView) view.findViewById(R.id.card_view);





        FloatingActionButton shareButton=(FloatingActionButton)view.findViewById(R.id.sharebutton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DynamicLinks();

            }
        });

        FloatingActionButton textsize=(FloatingActionButton)view.findViewById(R.id.textsize);
        textsize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (seekBar.getVisibility()==View.VISIBLE){
                    seekBar.setVisibility(View.GONE);
                    scrollView.setAlpha(1f);
                }
                else {
                    seekBar.setVisibility(View.VISIBLE);
                    scrollView.setAlpha(0.5f);
                }




            }
        });

        FloatingActionButton readmode=(FloatingActionButton)view.findViewById(R.id.readmode);
        readmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isChecked){

                    Drawable sbuttonDrawable = shareButton.getBackground();
                    sbuttonDrawable = DrawableCompat.wrap(sbuttonDrawable);
                    DrawableCompat.setTint(sbuttonDrawable, Color.BLACK);
                    shareButton.setBackground(sbuttonDrawable);
                    Drawable tbuttonDrawable = textsize.getBackground();
                    tbuttonDrawable = DrawableCompat.wrap(tbuttonDrawable);
                    DrawableCompat.setTint(tbuttonDrawable, Color.BLACK);
                    textsize.setBackground(tbuttonDrawable);
                    Drawable rbuttonDrawable = readmode.getBackground();
                    rbuttonDrawable = DrawableCompat.wrap(rbuttonDrawable);
                    DrawableCompat.setTint(rbuttonDrawable, Color.BLACK);
                    readmode.setBackground(rbuttonDrawable);
                    title.setTextColor(getResources().getColor(R.color.text));
                    cardView.setCardBackgroundColor(getResources().getColor(R.color.cardback));
                    style="<style>body {background:#FBF0D9; color:#5F4B32;}img{display: inline;height: auto;max-width: 100%;}*{font: "+seekBar.getProgress()+"px/40px Cormorant, serif; }</style>";
                    isChecked=true;


                }
                else {

                    Drawable sbuttonDrawable = shareButton.getBackground();
                    sbuttonDrawable = DrawableCompat.wrap(sbuttonDrawable);
                    DrawableCompat.setTint(sbuttonDrawable,getResources().getColor( R.color.colorPrimaryDark));
                    shareButton.setBackground(sbuttonDrawable);
                    Drawable tbuttonDrawable = textsize.getBackground();
                    tbuttonDrawable = DrawableCompat.wrap(tbuttonDrawable);
                    DrawableCompat.setTint(tbuttonDrawable,getResources().getColor( R.color.colorPrimaryDark));
                    textsize.setBackground(tbuttonDrawable);
                    Drawable rbuttonDrawable = readmode.getBackground();
                    rbuttonDrawable = DrawableCompat.wrap(rbuttonDrawable);
                    DrawableCompat.setTint(rbuttonDrawable, getResources().getColor( R.color.colorPrimaryDark));
                    readmode.setBackground(rbuttonDrawable);
                    title.setTextColor(getResources().getColor(R.color.black));
                    cardView.setCardBackgroundColor(getResources().getColor(R.color.white));

                    style="<style>img{display: inline;height: auto;max-width: 100%;}*{font: "+seekBar.getProgress()+"px/40px Cormorant, serif; }</style>";
                    isChecked=false;

                }

                loadwebView(article.getContent().getRendered(),webView,style);



            }
        });





        seekBar=(SeekBar)view.findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("Progress",String.valueOf(progress));

                if (!isChecked) {

                    style = "<style>img{display: inline;height: auto;max-width: 100%;}*{font: " + progress + "px/40px Cormorant, serif; }</style>";
                }
                else {

                    style="<style>body {background:#FBF0D9; color:#5F4B32;}img{display: inline;height: auto;max-width: 100%;}*{font: "+progress+"px/40px Cormorant, serif; }</style>";


                }

                loadwebView(article.getContent().getRendered(),webView,style);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBar.setVisibility(View.GONE);
                scrollView.setAlpha(1f);
            }
        });



    }

    public void loadwebView(String data, WebView webView,String style){
        String pre="<html>\n" +
                "<head>\n" +
                "<style>\n" +
                "p.has-drop-cap {\n" +
                "  line-height: 1.7;\n" +
                "}\n"  +
                "</style>\n" +
                "</head>\n" +
                "<body>";

        String post="</body>\n" +
                "</html>\n";
        webView.getSettings().setJavaScriptEnabled(true);
        webView.canGoBack();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                super.onPageFinished(view, url);

            }
        });

        webView.loadDataWithBaseURL(null, style + data, "text/html", "UTF-8", null);
    }

    //body {background:#FBF0D9; color:#5F4B32;}


    private void DynamicLinks(){

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(websiteurl+"query?id="+article.getId()))
                .setDomainUriPrefix("https://ficsrcc.page.link")
                .setAndroidParameters( new DynamicLink.AndroidParameters.Builder(BuildConfig.APPLICATION_ID)
                        .build())
                .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)


                .addOnCompleteListener(getActivity(), new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        //Log.i("Erroris",task.getException().getMessage());
                        if (task.isSuccessful()){
                            Uri shortLink = task.getResult().getShortLink();
                            shareIntent(shortLink.toString());
                        }
                        else {
                            shareIntent(article.getLink());

                        }

                    }
                });

    }

    private void shareIntent(String string){
        StringBuilder sb = new StringBuilder();
        sb.append("Check Out This Article!");
        sb.append("\n");
        sb.append("Title:-");
        sb.append(Html.fromHtml(article.getTitle().getRendered()));
        sb.append("\n");
        sb.append(string);

        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, article.getTitle().getRendered());
        share.putExtra(Intent.EXTRA_TEXT, sb.toString());
        startActivity(Intent.createChooser(share, "Share link!"));
    }


}
