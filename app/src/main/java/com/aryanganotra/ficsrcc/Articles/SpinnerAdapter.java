package com.aryanganotra.ficsrcc.Articles;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<Category> {

    private Context context;
    private List<Category> categories;


    public SpinnerAdapter(@NonNull Context context, int resource, List<Category> categories) {
        super(context, resource, categories);

        this.context=context;
        this.categories=categories;

    }

    public void setCategories(List<Category> categories){
        this.categories=categories;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (categories!=null){
            return categories.size();

        }
        else {
            return 0;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Nullable
    @Override
    public Category getItem(int position) {
        return categories.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.WHITE);
        label.setText(Html.fromHtml(categories.get(position).getName()));
        return label;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(Html.fromHtml(categories.get(position).getName()));

        return label;
    }
}
