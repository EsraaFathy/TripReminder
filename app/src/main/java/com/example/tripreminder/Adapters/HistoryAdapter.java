package com.example.tripreminder.Adapters;

import android.content.ClipData;
import android.content.Context;
import android.text.Layout;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripreminder.R;
import com.example.tripreminder.model.HistoryItem;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ItemViewHolder>{
    List<HistoryItem> items;
    Context context; //should be iniatized in the constructor with the list

    public HistoryAdapter(List<HistoryItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        final HistoryItem item = items.get(position);
        holder.tripName.setText(item.getTitle());
        holder.from.setText(item.getFrom());
        holder.to.setText(item.getTo());
        holder.date.setText(item.getDate());
        holder.time.setText(item.getTime());
        holder.status.setText(item.getStatus());


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistoryItem historyItem=items.get(position);
                items.remove(position);
                notifyDataSetChanged();

            }
        });

        holder.drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.hiddenView.getVisibility() == View.VISIBLE) {

                    TransitionManager.beginDelayedTransition(holder.cardView,
                            new AutoTransition());
                    holder.hiddenView.setVisibility(View.GONE);
                    holder.drop.setImageResource(R.mipmap.ic_dropup);
                }

                // If the CardView is not expanded, set its visibility
                // to visible and change the expand more icon to expand less.
                else {

                    TransitionManager.beginDelayedTransition(holder.cardView,
                            new AutoTransition());
                    holder.hiddenView.setVisibility(View.VISIBLE);
                    holder.drop.setImageResource(R.mipmap.ic_dropdown);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return items==null?0:items.size();
    }

    public void filterList(ArrayList<HistoryItem> filteredList){
        items=filteredList;
        notifyDataSetChanged();
    }



    class ItemViewHolder extends RecyclerView.ViewHolder{

        TextView tripName,from,to,time,date,status;
        ImageView delete, drop;
        LinearLayout hiddenView;
        CardView cardView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tripName=itemView.findViewById(R.id.heading);
            from=itemView.findViewById(R.id.from_tv);
            to=itemView.findViewById(R.id.to_tv);
            date=itemView.findViewById(R.id.date_tv);
            time=itemView.findViewById(R.id.time_tv);
            status=itemView.findViewById(R.id.status_tv);
            delete=itemView.findViewById(R.id.delete_btn);
            drop=itemView.findViewById(R.id.arrow_btn);
            hiddenView=itemView.findViewById(R.id.hidden_view);
            cardView=itemView.findViewById(R.id.base_cardview);
        }
    }

}
