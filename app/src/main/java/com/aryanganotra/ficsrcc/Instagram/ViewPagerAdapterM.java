package com.aryanganotra.ficsrcc.Instagram;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.aryanganotra.ficsrcc.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ViewPagerAdapterM extends PagerAdapter {
    private InstagramModel instaData;
    private ArrayList<String> images = new ArrayList<>();

    public ViewPagerAdapterM(){


    }

    public void addImage(String url) {
        images.add(url);
        notifyDataSetChanged();
    }
    public int getCount() {
        return images.size();
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
        Glide.with(container.getContext().getApplicationContext()).load(images.get(position)).into(imageView);


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
