package com.aryanganotra.ficsrcc.StockService;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aryanganotra.ficsrcc.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockArrayAdapter extends ArrayAdapter<StockResultModel.ResultSetClass.Results> implements Filterable {

    private Context context;
    private List<StockResultModel.ResultSetClass.Results> stocks;
    private StockServiceClass serviceClass;



    public StockArrayAdapter(@NonNull Context context, StockServiceClass serviceClass) {
        super(context, R.layout.stock_list_layout);
        this.context=context;
        this.serviceClass=serviceClass;
        stocks=new ArrayList<>();

    }




    @Override
    public int getCount() {
        if (stocks!=null&&stocks.size()>0 ) {
            return stocks.size();
        }
        else
            return 0;
    }


    @Override
    public StockResultModel.ResultSetClass.Results getItem(int position) {
        return stocks.get(position);
    }



    @Override
    public Filter getFilter() {
        Filter filter=new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {

                    Call<StockResultModel> stockCall = serviceClass.getStocks(constraint.toString(), "en-UK");
                    try {


                        stockCall.enqueue(new Callback<StockResultModel>() {
                            @Override
                            public void onResponse(Call<StockResultModel> call, Response<StockResultModel> response) {


                              stocks=response.body().getResultSet().getResult();
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(Call<StockResultModel> call, Throwable t) {

                            }
                        });


                    } catch (Exception e) {

                    }
                    filterResults.values = stocks;
                    filterResults.count = stocks.size();
                }

                    return filterResults;



            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }

            }
        };
        return filter;
    }





    public class ViewHolder{
        public TextView name;
        public TextView exchDisplay;
        public TextView ticker;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       ViewHolder viewHolder=null;
       if (convertView==null) {

           convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_list_layout, parent, false);
           viewHolder = new ViewHolder();
           viewHolder.name = (TextView) convertView.findViewById(R.id.name);
           viewHolder.exchDisplay = (TextView) convertView.findViewById(R.id.exchDisplay);
           viewHolder.ticker = (TextView) convertView.findViewById(R.id.ticker);

           convertView.setTag(viewHolder);
       }
       else {

           viewHolder=(ViewHolder)convertView.getTag();
       }

if (stocks!=null&&stocks.size()>0) {
    viewHolder.name.setText(stocks.get(position).getName());
    viewHolder.ticker.setText(stocks.get(position).getSymbol());
    viewHolder.exchDisplay.setText(stocks.get(position).getExchDisp());
}


       return convertView;

    }
}
