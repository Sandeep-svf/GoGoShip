package com.gogoship.gogoship.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gogoship.gogoship.R;
import com.gogoship.gogoship.response.HomeFragChildList;
import com.gogoship.gogoship.response.HomeFragParentList;


import java.util.ArrayList;


public class ParentRecyclerViewAdapter extends RecyclerView.Adapter<ParentRecyclerViewAdapter.MyViewHolder> {
    ArrayList<HomeFragParentList> parentLists;
    public Context cxt;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView category;
        public RecyclerView childRecyclerView;

        public MyViewHolder(View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.Movie_category);
            childRecyclerView = itemView.findViewById(R.id.Child_RV);
        }
    }

    public ParentRecyclerViewAdapter(ArrayList<HomeFragParentList> parentLists, Context context) {
        this.parentLists = parentLists;
        this.cxt = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_recyclerview_items, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
      //  Log.e("parent_list","     "+parentLists.size());
        return parentLists.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        //  ParentModel currentItem = parentModelArrayList.get(position);
        holder.childRecyclerView.setHasFixedSize(true);
        holder.category.setText(parentLists.get(position).getCountry());
        ArrayList<HomeFragChildList> homeFragChildLists = new ArrayList<HomeFragChildList>();
        homeFragChildLists= (ArrayList<HomeFragChildList>) parentLists.get(position).getHomeFragChildListList();
        ChildRecyclerViewAdapter childRecyclerViewAdapter = new ChildRecyclerViewAdapter(homeFragChildLists,holder.childRecyclerView.getContext());
        holder.childRecyclerView.setAdapter(childRecyclerViewAdapter);
    }
}

// added in second child row
