package com.sebrs3018.SmartSharing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<String> BookList = null;
    private ArrayList<String> arraylist;

    public ListViewAdapter(Context context, List<String> BookList) {
        mContext = context;
        this.BookList = BookList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(BookList);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return BookList.size();
    }

    @Override
    public String getItem(int position) {
        return BookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.search, null);
           /* // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(holder);*/
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
//        holder.name.setText(BookList.get(position).getAnimalName());
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        BookList.clear();
        if (charText.length() == 0) {
            BookList.addAll(arraylist);
        } else {
            for (String wp : arraylist) {
//                if (wp.getAnimalName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    BookList.add(wp);
//                }
            }
        }
        notifyDataSetChanged();
    }

}
