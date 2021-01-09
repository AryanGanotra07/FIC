package com.aryanganotra.ficsrcc;


import android.content.Intent;

import android.os.Build;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aryanganotra.ficsrcc.StockService.StockArrayAdapter;
import com.aryanganotra.ficsrcc.StockService.StockResultModel;
import com.aryanganotra.ficsrcc.StockService.StockServiceClass;
import com.aryanganotra.ficsrcc.alphavantagestockapp.AlphaVantageAPIService;
import com.aryanganotra.ficsrcc.alphavantagestockapp.DailyResponse;
import com.aryanganotra.ficsrcc.alphavantagestockapp.DateValueFormatter;

import com.aryanganotra.ficsrcc.alphavantagestockapp.RetrofitClient;
import com.aryanganotra.ficsrcc.alphavantagestockapp.StockDailyInfo;
import com.aryanganotra.ficsrcc.alphavantagestockapp.StockData;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SavedArticles extends AppCompatActivity implements RetrofitClient.RetrofitListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;

    public static final String BASE_URL = "https://www.alphavantage.co";

    private LineChart mLineChart;

    private AutoCompleteTextView tickertext;
    private Button enterticker;
    private String ticker;
    private TextView maintext;
    private Snackbar snackbar;


    private StockArrayAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_saved);

        Retrofit retrofit=new Retrofit.Builder().baseUrl("http://d.yimg.com").addConverterFactory(GsonConverterFactory.create()).build();
        StockServiceClass serviceClass=retrofit.create(StockServiceClass.class);











        snackbar= Snackbar.make(findViewById(android.R.id.content),"No Internet Connection",Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SavedArticles.this,SavedArticles.class));

            }
        });


        if (!Check.getInstance()){
            snackbar.show();
        }


        mLineChart = (LineChart)findViewById(R.id.example_chart);
        mLineChart.getXAxis().setValueFormatter(new DateValueFormatter());

        mLineChart.setFocusableInTouchMode(true);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            mLineChart.setTooltipText("Zoom out to see values");

        }



        //  mLineChart.setFocusable(true);

        maintext=(TextView)findViewById(R.id.MainText);


        ticker="BSE";


        fetchData(SavedArticles.this);



        tickertext=(AutoCompleteTextView)findViewById(R.id.tickertext);
        enterticker=(Button) findViewById(R.id.tickerenter);

       ArrayList<StockResultModel.ResultSetClass.Results> stocks = new ArrayList<>();


       StockResultModel.ResultSetClass.Results results=new StockResultModel.ResultSetClass.Results("lupin","LUPIN","lup");
       stocks.add(results);

        adapter=new StockArrayAdapter(this,serviceClass);
        tickertext.setAdapter(adapter);

        enterticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Check.getInstance()) {
                    if (!tickertext.getText().toString().isEmpty()) {
                        ticker = tickertext.getText().toString();
                        fetchData(SavedArticles.this);
                    } else {
                        tickertext.setError("Can't be blank");
                    }
                }
                else {
                    snackbar= Snackbar.make(findViewById(android.R.id.content),"No Internet Connection",Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            startActivity(new Intent(SavedArticles.this,SavedArticles.class));

                        }
                    });

                    snackbar.show();
                }
            }
        });




        tickertext.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StockResultModel.ResultSetClass.Results result=adapter.getItem(position);
                tickertext.setText("");
                if (result!=null&&result.getName()!=null) {
                    tickertext.setText(result.getSymbol());
                    ticker = result.getSymbol();
                    fetchData(SavedArticles.this);
                    tickertext.clearFocus();
                }
            }
        });




    drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayoutsaved);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.nv4);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.navigation_username);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            navUsername.setText("Hello, " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        }
        navigationView.getHeaderView(0).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.home:
                        //Toast.makeText(SavedArticles.this, "Home", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SavedArticles.this, UserActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.about:
                        //Toast.makeText(SavedArticles.this, "About", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SavedArticles.this, AboutPage.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.logout:
                        if (Check.getInstance()) {
                          //  Toast.makeText(SavedArticles.this, "Logout", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(SavedArticles.this, FirebaseAuthUI.class));
                            kill();
                        } else {
                            Toast.makeText(SavedArticles.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }

                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.discover:
                        startActivity(new Intent(SavedArticles.this, Discover.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.contact:
                        //Toast.makeText(SavedArticles.this, "Contact", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SavedArticles.this, ContactPage.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.savedarticles:
                        startActivity(new Intent(SavedArticles.this, SavedArticles.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                }


                return true;
            }
        });

    }






    private void fetchData(final RetrofitClient.RetrofitListener listener) {
        mLineChart.getXAxis().setValueFormatter(new DateValueFormatter());
        final List<Entry> entries = new ArrayList<>();
        if (Constants.VINTAGEAPI!=null) {

            AlphaVantageAPIService service = RetrofitClient.getDailyClient(BASE_URL).create(AlphaVantageAPIService.class);

                service.getStockData(ticker, Constants.VINTAGEAPI).enqueue(new Callback<DailyResponse>() {
                    @Override
                    public void onResponse(Call<DailyResponse> call, Response<DailyResponse> response) {


                        if (response.isSuccessful()) {
                            if (snackbar != null && snackbar.isShown()) {
                                snackbar.dismiss();
                            }
                            StockDailyInfo stockDailyInfo = response.body().getDailyResults();
                            try {
                                if (stockDailyInfo.getDailyInfoList() != null) {
                                    for (int i = stockDailyInfo.getDailyInfoList().size() - 1; i >= 0; i--) {
                                        StockData dayData = stockDailyInfo.getDailyInfoList().get(i);
                                        float time = Long.valueOf(dayData.getDate().getTime()).floatValue();
                                        Float value = dayData.getStockValue(StockData.StockValue.CLOSE);
                                        entries.add(new Entry(time, value));
                                        //  Log.i("Value", String.valueOf(time));
                                    }

                                    TextView amount = (TextView) findViewById(R.id.mainvalue);
                                    TextView changeamount = (TextView) findViewById(R.id.changeValue);
                                    float value = entries.get(entries.size() - 1).getY();
                                    DecimalFormat df = new DecimalFormat("#.##");
                                    amount.setText(String.valueOf(value));
                                    float diff = entries.get(entries.size() - 1).getY() - entries.get(entries.size() - 2).getY();

                                    maintext.setText(ticker);

                                    ImageView arrow = (ImageView) findViewById(R.id.arrow);
                                    if (diff >= 0) {
                                        changeamount.setText("+" + String.valueOf(df.format(diff)));
                                        changeamount.setTextColor(getResources().getColor(R.color.colorPrimary));
                                        arrow.setImageResource(R.drawable.arrowup);
                                    } else {

                                        changeamount.setText(String.valueOf(df.format(diff)));
                                        changeamount.setTextColor(getResources().getColor(R.color.red));
                                        arrow.setImageResource(R.drawable.arrowdown);

                                    }
                                    float div = diff / (int) entries.get(entries.size() - 2).getY();
                                    float percent = Math.abs(div * 100);

                                    TextView changepercent = (TextView) findViewById(R.id.changePercent);
                                    changepercent.setText("(" + String.valueOf(df.format(percent)) + "%)");
                                    if (diff >= 0) {
                                        changepercent.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    } else {
                                        changepercent.setTextColor(getResources().getColor(R.color.red));
                                    }


                                    listener.onData(entries);
                                }
                            } catch (Exception e) {

                                tickertext.setError("Invalid ticker");

                            }

                        } else {
                            //Log.w(TAG, "Response was not successful");
                        }


                    }


                    @Override
                    public void onFailure(Call<DailyResponse> call, Throwable t) {
                        //  Log.w(TAG, "AlphaVantageAPI Service Call Failed");
                        if (Check.getInstance()) {
                            tickertext.setError("Invalid ticker");
                        } else {
                            snackbar.show();
                            if (ticker != null && ticker.isEmpty()) {


                                ticker = "BSE";

                            }
                            fetchData(SavedArticles.this);
                        }
                    }
                });



        }
        else {
            FirebaseDatabase.getInstance().getReference("contactus").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Constants.VINTAGEAPI = (String) dataSnapshot.child("vintageapi").getValue();
                    if (Constants.VINTAGEAPI!=null){
                        ticker = "BSE";
                        fetchData(SavedArticles.this);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onData(List<Entry> entries) {

        LineDataSet dataSet = new LineDataSet(entries, ticker);
        mLineChart.setData(new LineData(dataSet));
        mLineChart.invalidate();

    }

    public void kill(){
        SavedArticles.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (snackbar!=null&&snackbar.isShownOrQueued()){
            snackbar.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

}












