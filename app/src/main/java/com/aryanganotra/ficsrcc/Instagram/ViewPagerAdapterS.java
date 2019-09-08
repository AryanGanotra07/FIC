package com.aryanganotra.ficsrcc.Instagram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aryanganotra.ficsrcc.R;
import com.bumptech.glide.Glide;

public class ViewPagerAdapterS extends PagerAdapter {
    private InstagramModel instaData;
    private int pos;

    public ViewPagerAdapterS(InstagramModel instData, int pos){

        this.instaData=instData;
        this.pos=pos;

    }

    @Override
    public int getCount() {
              if (instaData.getData().get(pos).getCarousel_media()!=null) {
                  return instaData.getData().get(pos).getCarousel_media().size();
              }
              else if(instaData.getData().get(pos).getType().equals("image") && instaData.getData().get(pos).getImages()!=null&&instaData.getData().get(pos).getImages()!=null&& instaData.getData().get(pos).getImages().getStandard_resolution().getUrl()!=null)  {
                  return 1;
              }
              else {
                  return 0;
              }


    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater=(LayoutInflater)container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.custom_layout,null);
        ViewPager vp=(ViewPager)container;
        vp.addView(view);

        ImageView imageView=(ImageView)view.findViewById(R.id.imageinsta);
        if (instaData.getData().get(pos).getCarousel_media()!=null) {
            Glide.with(container.getContext().getApplicationContext()).load(instaData.getData().get(pos).getCarousel_media().get(position).getImages().getStandard_resolution().getUrl()).into(imageView);
        }
        else if (instaData.getData().get(pos).getType().equals("image") && instaData.getData().get(pos).getImages()!=null&& instaData.getData().get(pos).getImages().getStandard_resolution().getUrl()!=null){
            Glide.with(container.getContext().getApplicationContext()).load(instaData.getData().get(pos).getImages().getStandard_resolution().getUrl()).into(imageView);
        }


        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp=(ViewPager)container;
        View v=(View)object;
        vp.removeView(v);
    }
}

