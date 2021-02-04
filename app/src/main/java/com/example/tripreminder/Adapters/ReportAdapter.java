package com.example.tripreminder.Adapters;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripreminder.R;
import com.example.tripreminder.RoomDataBase.TripTable;
import com.example.tripreminder.model.HistoryItem;

import java.util.ArrayList;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ItemViewHolder>{
    List<TripTable> items;
    public static String DROP="drop";
    Context context; //should be iniatized in the constructor with the list
    private OcCLickListenerAble ocCLickListenerAble;

    public ReportAdapter(Context context) {
        this.context=context;
    }

    public void setItems(List<TripTable> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.distance_item,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        final TripTable item = items.get(position);
        holder.tripName.setText(item.getTitle());
        holder.from.setText(item.getFrom());
        holder.to.setText(item.getTo());
        holder.distance.setText(item.getDistance()+"");




    }

    @Override
    public int getItemCount() {
        return items==null?0:items.size();
    }

    public void filterList(ArrayList<TripTable> filteredList){
        items=filteredList;
        notifyDataSetChanged();
    }



    class ItemViewHolder extends RecyclerView.ViewHolder{

        TextView tripName,from,to,distance;
        ImageView drop;
        LinearLayout hiddenView;
        CardView cardView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tripName=itemView.findViewById(R.id.heading);
            from=itemView.findViewById(R.id.from_tv);
            to=itemView.findViewById(R.id.to_tv);
            distance=itemView.findViewById(R.id.distance_tv);
            drop=itemView.findViewById(R.id.arrow_btn);
            hiddenView=itemView.findViewById(R.id.hidden_view);
            cardView=itemView.findViewById(R.id.base_cardview);

            drop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hiddenView.getVisibility() == View.VISIBLE) {
                        TransitionManager.beginDelayedTransition(cardView,
                                new AutoTransition());
                        hiddenView.setVisibility(View.GONE);
                    }

                    // If the CardView is not expanded, set its visibility
                    // to visible and change the expand more icon to expand less.
                    else {

                        TransitionManager.beginDelayedTransition(cardView,
                                new AutoTransition());
                        hiddenView.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }
    public void OnItemClickListener(OcCLickListenerAble ocCLickListenerAble) {
        this.ocCLickListenerAble = ocCLickListenerAble;
    }

    public interface OcCLickListenerAble {
        void onItemClick(String type, TripTable tripTable);
    }
}
