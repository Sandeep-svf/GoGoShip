package com.mobapps.gogoship.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mobapps.gogoship.R;
import com.mobapps.gogoship.response.HomeFragChildList;
import com.mobapps.gogoship.webview.WebsiteWebviewActivity;

import java.util.ArrayList;


public class ChildRecyclerViewAdapter extends RecyclerView.Adapter<ChildRecyclerViewAdapter.MyViewHolder> {
    ArrayList<HomeFragChildList> homeFragChildLists;
    Context cxt;
    private int mRequestCode = 100;
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView heroImage;
        public TextView movieName;

        public MyViewHolder(View itemView) {
            super(itemView);
            heroImage = itemView.findViewById(R.id.hero_image);
            // movieName = itemView.findViewById(R.id.movie_name);

        }
    }

    public ChildRecyclerViewAdapter(ArrayList<HomeFragChildList> homeFragChildLists, Context mContext) {
        this.cxt = mContext;
        this.homeFragChildLists = homeFragChildLists;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_recyclerview_items, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Glide.with(cxt).load(homeFragChildLists.get(position).getWebsiteLogo())
                .thumbnail(0.5f)
                .into(holder.heroImage);       // holder.movieName.setText(currentItem.getMovieName());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cxt, WebsiteWebviewActivity.class);
                intent.putExtra("webview_url",homeFragChildLists.get(position).getWebsiteLink());
                intent.putExtra("type",homeFragChildLists.get(position).getType());
                //cxt.startActivity(intent);
                ((Activity) cxt).startActivityForResult(intent,mRequestCode);

            }
        });

    }

    @Override
    public int getItemCount() {
        return homeFragChildLists.size();
    }
}