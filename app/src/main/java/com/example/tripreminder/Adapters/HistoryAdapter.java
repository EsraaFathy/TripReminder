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

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ItemViewHolder>{
    List<TripTable> items;
    public static String DROP="drop";
    public static String DELETE="delete";
    Context context; //should be iniatized in the constructor with the list
    private OcCLickListenerAble ocCLickListenerAble;

    public HistoryAdapter(Context context) {
        this.items = items;
        this.context=context;
    }

    public void setItems(List<TripTable> items) {
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
        final TripTable item = items.get(position);
        holder.tripName.setText(item.getTitle());
        holder.from.setText(item.getFrom());
        holder.to.setText(item.getTo());
        holder.date.setText(item.getDate());
        holder.time.setText(item.getTime());
        holder.status.setText(item.getStatus());


//        holder.delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
////                builder.setCancelable(false);
////                builder.setMessage("Are you sure?");
////                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialogInterface, int i) {
////                        TripTable historyItem=items.get(position);
////                        items.remove(position);
////                        notifyDataSetChanged();
////                        dialogInterface.dismiss();
////                    }
////                });
////                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialogInterface, int i) {
////                        dialogInterface.dismiss();
////                    }
////                });
////                final AlertDialog dialog = builder.create();
////                dialog.show();
////                dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.colorPrim));
////                dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.colorPrim));
//            }
//        });

//        holder.drop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (holder.hiddenView.getVisibility() == View.VISIBLE) {
//
//                    TransitionManager.beginDelayedTransition(holder.cardView,
//                            new AutoTransition());
//                    holder.hiddenView.setVisibility(View.GONE);
//                    holder.drop.setImageResource(R.mipmap.ic_dropdown);
//                }
//
//                // If the CardView is not expanded, set its visibility
//                // to visible and change the expand more icon to expand less.
//                else {
//
//                    TransitionManager.beginDelayedTransition(holder.cardView,
//                            new AutoTransition());
//                    holder.hiddenView.setVisibility(View.VISIBLE);
//                    holder.drop.setImageResource(R.mipmap.ic_dropup);
//                }
//
//            }
//        });

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

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ocCLickListenerAble.onItemClick(DELETE,items.get(getAdapterPosition()));

                }
            });

            drop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hiddenView.getVisibility() == View.VISIBLE) {
                        TransitionManager.beginDelayedTransition(cardView,
                                new AutoTransition());
                        hiddenView.setVisibility(View.GONE);
                        drop.setImageResource(R.mipmap.ic_dropdown);
                    }

                    // If the CardView is not expanded, set its visibility
                    // to visible and change the expand more icon to expand less.
                    else {

                        TransitionManager.beginDelayedTransition(cardView,
                                new AutoTransition());
                        hiddenView.setVisibility(View.VISIBLE);
                        drop.setImageResource(R.mipmap.ic_dropup);
                        ocCLickListenerAble.onItemClick(DROP,items.get(getAdapterPosition()));
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
