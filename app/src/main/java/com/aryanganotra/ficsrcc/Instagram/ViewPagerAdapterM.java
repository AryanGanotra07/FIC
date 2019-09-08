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

public class ViewPagerAdapterM extends PagerAdapter {
    private InstagramModel instaData;

    public ViewPagerAdapterM(InstagramModel instadata){
        this.instaData=instadata;

    }
    public int getCount() {
        return instaData.getData().size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater=(LayoutInflater)container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.custom_layout,null);

        ImageView imageView=(ImageView)view.findViewById(R.id.imageinsta);
        Glide.with(container.getContext().getApplicationContext()).load(instaData.getData().get(position).getImages().getStandard_resolution().getUrl()).into(imageView);


        ViewPager vp=(ViewPager)container;
       vp.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp=(ViewPager)container;
        View view=(View)object;
        vp.removeView(view);

    }
}
