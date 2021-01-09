package com.aryanganotra.ficsrcc.Articles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aryanganotra.ficsrcc.BuildConfig;
import com.aryanganotra.ficsrcc.Constants;
import com.aryanganotra.ficsrcc.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RecyclerAdapter1 extends RecyclerView.Adapter<RecyclerAdapter1.ViewHolder> {
    private List<Article> articles;
    private Context context;
    private int lastPosition=-1;
    private List<Article> articlesCopy;
    private ArticlesListFragment.onItemSelectedListener callback;
    private ArticleService service;
    private static final String websiteurl="";
    private boolean filtering=false;



    public RecyclerAdapter1(Context context,ArticlesListFragment.onItemSelectedListener callback,ArticleService service){
        this.context=context;
        this.callback=callback;
        this.service=service;
    }

    public void setArticles(List<Article> articles){
        this.articles=articles;
        this.articlesCopy=articles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.article_layout, viewGroup, false);
        ViewHolder vh=new ViewHolder(v);

return vh;


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {


if (articles!=null&&articles.get(i)!=null&&articles.get(i).getTitle()!=null) {
    viewHolder.title.setText(Html.fromHtml(articles.get(i).getTitle().getRendered()));
    viewHolder.date.setText(Html.fromHtml(toDate(articles.get(i).getDate())));
    viewHolder.content.setText(Html.fromHtml(articles.get(i).getContent().getRendered()));
    if (articles.get(i).get_embedded()!=null&&articles.get(i).get_embedded().getWpfeaturemedia()!=null) {
        Glide.with(context.getApplicationContext()).load(articles.get(i).get_embedded().getWpfeaturemedia().get(0).getSource_url()).into(viewHolder.image);
    }
    else {
        Glide.with(context.getApplicationContext()).load("").into(viewHolder.image);
    }


}
        Animation animation = AnimationUtils.loadAnimation(context,
                (i > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
        viewHolder.itemView.startAnimation(animation);
        lastPosition = i;


    }

    @Override
    public int getItemCount() {
        if (articles!=null) {
            return articles.size();
        }
        else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;
        public TextView date;
        public TextView content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.image);
            title=(TextView)itemView.findViewById(R.id.title);
            date=(TextView)itemView.findViewById(R.id.date);
            content=(TextView)itemView.findViewById(R.id.content);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    callback.OnArticleSelected(articles.get(getAdapterPosition()));

                }
            });

            ((TextView)itemView.findViewById(R.id.viewarticle)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    callback.OnArticleSelected(articles.get(getAdapterPosition()));
                }
            });

            ((Button)itemView.findViewById(R.id.sharebutton)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

DynamicLinks(getAdapterPosition());
                }
            });





        }
    }

    private void DynamicLinks(int position){


        if (Constants.WEBSITE!=null) {

            Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLink(Uri.parse(Constants.WEBSITE + "query?id=" + articles.get(position).getId()))
                    .setDomainUriPrefix("https://ficsrcc.page.link")
                    .setAndroidParameters(new DynamicLink.AndroidParameters.Builder(BuildConfig.APPLICATION_ID)
                            .build())
                    .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
                    .addOnCompleteListener((Activity) context, new OnCompleteListener<ShortDynamicLink>() {
                        @Override
                        public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                            //Log.i("Erroris",task.getException().getMessage());
                            if (task.isSuccessful()) {
                                Uri shortLink = task.getResult().getShortLink();
                                shareIntent(shortLink.toString(), position);
                            } else {
                                shareIntent(articles.get(position).getLink(), position);

                            }

                        }
                    });

        }

    }

    private void shareIntent(String string,int position){

        StringBuilder sb = new StringBuilder();
        sb.append("Check Out This Article!");
        sb.append("\n");
        sb.append("Title:-");
        sb.append(Html.fromHtml(articles.get(position).getTitle().getRendered()));
        sb.append("\n");
        sb.append(string);

        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, articles.get(position).getTitle().getRendered());
        share.putExtra(Intent.EXTRA_TEXT,sb.toString());

        context.startActivity(Intent.createChooser(share, "Share link!"));
    }

    public List<Article> getArticles(){
        return articles;
    }




    private Filter exampleFilter=new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {


          List<Article> articlesFilter=new ArrayList<>();
            String charstring = constraint.toString();
            if (constraint == null || constraint.length() == 0||charstring.isEmpty()||charstring==null){
                articlesFilter=articlesCopy;
                Log.i("Executed","1");
            }
            else {
                String charstringtrimmed=charstring.toLowerCase().trim();
                Log.i("CharString",charstringtrimmed);
                Log.i("Executed","3");
                for (Article article:articlesCopy) {
                    Log.i("Title",article.getTitle().getRendered());
                    if (article.getTitle().getRendered().toLowerCase().contains(charstringtrimmed)) {
                        articlesFilter.add(article);
                        Log.i("Executed","2");
                    }
                }
            }

            FilterResults results=new FilterResults();
            results.values=articlesFilter;
            Log.i("Results",results.values.toString());
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            articles=((ArrayList<Article>)results.values);
            notifyDataSetChanged();
        }
    };

    public Filter getExampleFilter() {
        return exampleFilter;
    }


    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    private void getImage(int medidId, ViewHolder vh, int position){






    }


    public String toDate(String dateString){
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date myDate=null;
        try {
            myDate=dateFormat.parse(dateString);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat reqDateFormat=new SimpleDateFormat("dd-MM-yyyy");
        String finalDate=reqDateFormat.format(myDate);
        return finalDate;
    }


}
