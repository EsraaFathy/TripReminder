package com.example.tripreminder.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripreminder.R;
import com.example.tripreminder.RoomDataBase.TripTable;
import com.example.tripreminder.model.Trip;

import java.util.List;

public class RecyclerHomeAdapter extends RecyclerView.Adapter<RecyclerHomeAdapter.ViewHolder> {

    private Context context;
    List<TripTable> trips;

    public RecyclerHomeAdapter(Context context, List<TripTable> trips) {
        this.context = context;
        this.trips = trips;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_trip, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(trips.get(position).getTitle());
        holder.time.setText(trips.get(position).getTime());
        holder.date.setText(trips.get(position).getDate());
        if (trips.get(position).getStatus())
        holder.status.setText("available");
        else
        holder.status.setText("not available");

        holder.from.setText(trips.get(position).getFrom());
        holder.to.setText(trips.get(position).getTo());

    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView time;
        private TextView date;
        private TextView status;
//        private TextView repetition;
//        private TextView ways;
        private TextView from;
        private TextView to;
        private ImageView notes;
        private ImageView menu;
        private TextView startButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.TripNameTripItem);
            time = itemView.findViewById(R.id.timeTextViewTripItem);
            date = itemView.findViewById(R.id.dateTextViewTripItem);
            status = itemView.findViewById(R.id.statusTripItem);
//            repetition = itemView.findViewById(R.id.rep);
//            ways = itemView.findViewById();
            from = itemView.findViewById(R.id.fromTripItem);
            to = itemView.findViewById(R.id.toTripItem);
            notes = itemView.findViewById(R.id.notesTripItem);
            menu = itemView.findViewById(R.id.menueIconTripItem);
            startButton = itemView.findViewById(R.id.startButton);
        }
    }
}
