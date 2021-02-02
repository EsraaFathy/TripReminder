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

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private Context context;
    private List<TripTable> trips;
    private RecyclerHomeAdapter.OcCLickListenerAble ocCLickListenerAble;

    public void setTrips(List<TripTable> trips) {
        this.trips = trips;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(trips.get(position).getTitle());
        holder.time.setText(trips.get(position).getTime());
        holder.date.setText(trips.get(position).getDate());
        holder.status.setText(trips.get(position).getStatus());
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
        private TextView from;
        private TextView to;
        private ImageView notes;
        private ImageView menu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.TripNameHistory);
            time = itemView.findViewById(R.id.timeTextViewHistory);
            date = itemView.findViewById(R.id.dateTextViewHistory);
            status = itemView.findViewById(R.id.statusHistory);
            from = itemView.findViewById(R.id.fromHistory);
            to = itemView.findViewById(R.id.toHistory);
            notes = itemView.findViewById(R.id.notesHistory);
            menu = itemView.findViewById(R.id.menueIconHistory);
        }
    }

    public void OnItemClickListener(RecyclerHomeAdapter.OcCLickListenerAble ocCLickListenerAble) {
        this.ocCLickListenerAble = ocCLickListenerAble;
    }

    public interface OcCLickListenerAble {
        void onItemClick(String type, TripTable tripTable);
    }
}
