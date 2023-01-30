package com.gogoship.gogoship.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gogoship.gogoship.FilterItemClick;
import com.gogoship.gogoship.R;
import com.gogoship.gogoship.response.FromWhereResponse;

import java.util.List;

public class CountryBottomAdapter extends RecyclerView.Adapter<CountryBottomAdapter.VH> {

    Context context;
    View view=null;
    List<FromWhereResponse> fromWhereResponseList;
    FilterItemClick filterItemClick;
    int groupPosition=0;
    public CountryBottomAdapter(Context context, List<FromWhereResponse> fromWhereResponseList, FilterItemClick filterItemClick, int groupPosition) {
        this.context=context;
        this.fromWhereResponseList=fromWhereResponseList;
        this.filterItemClick=filterItemClick;
        this.groupPosition=groupPosition;
    }

    @NonNull
    @Override
    public CountryBottomAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_bottom_adapter, parent, false);
        return new CountryBottomAdapter.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryBottomAdapter.VH holder, int position) {
      holder.name.setText(fromWhereResponseList.get(position).getName());

     holder.itemView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             groupPosition = position;
             filterItemClick.onItemClick(position,fromWhereResponseList.get(position).getName());
             notifyDataSetChanged();
         }
     });

        if (groupPosition == position) {
           holder.name.setTextColor(Color.WHITE);
        } else {
            holder.name.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return fromWhereResponseList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView name;
        public VH(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.category_text);
        }
    }
}
