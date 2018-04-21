package com.androstock.newsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import static com.androstock.newsapp.MainActivity.KEY_URL;

/**
 * Created by Saurabh Kumar Dwivedi on 04/20/2018.
 */

public class ListNewsAdapter extends RecyclerView.Adapter<ListNewsAdapter.ListNewsViewHolder> {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private Context mContext;

    public ListNewsAdapter(Context context, ArrayList<HashMap<String, String>> d) {
        this.mContext = context;
        this.data=d;
    }

    @NonNull
    @Override
    public ListNewsAdapter.ListNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflator = LayoutInflater.from(mContext);
        view = mInflator.inflate(R.layout.card_view_news_item,parent,false);
        return new ListNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListNewsAdapter.ListNewsViewHolder holder, final int position) {
        holder.galleryImage.setId(position);
        holder.author.setId(position);
        holder.title.setId(position);
        holder.sdetails.setId(position);
        holder.time.setId(position);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,DetailsActivity.class);
                intent.putExtra("url",data.get(position).get(KEY_URL));
                mContext.startActivity(intent);
            }
        });

        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);
        // Set the text or image to be displayed on respective views
        try{
            holder.author.setText(song.get(MainActivity.KEY_AUTHOR));
            holder.title.setText(song.get(MainActivity.KEY_TITLE));
            holder.time.setText(song.get(MainActivity.KEY_PUBLISHEDAT));
            holder.sdetails.setText(song.get(MainActivity.KEY_DESCRIPTION));

            if(song.get(MainActivity.KEY_URLTOIMAGE).toString().length() < 5)
            {
                holder.galleryImage.setVisibility(View.GONE);
            }else{
                Picasso.with(activity)
                        .load(song.get(MainActivity.KEY_URLTOIMAGE).toString())
                        .resize(300, 200)
                        .into(holder.galleryImage);
            }
        }catch(Exception e) {}
    }

    //Get the id of the item at a specific position
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    //This is an abstract class to hold the respective views
    public static class ListNewsViewHolder  extends RecyclerView.ViewHolder {
        ImageView galleryImage;
        TextView author, title, sdetails, time;
        CardView cardView;
        public ListNewsViewHolder(View itemView) {
            super(itemView);
            galleryImage = (ImageView) itemView.findViewById(R.id.galleryImage);
            author = (TextView) itemView.findViewById(R.id.author);
            title = (TextView) itemView.findViewById(R.id.showTitle);
            sdetails = (TextView) itemView.findViewById(R.id.sdetails);
            time = (TextView) itemView.findViewById(R.id.time);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }

    }
}
