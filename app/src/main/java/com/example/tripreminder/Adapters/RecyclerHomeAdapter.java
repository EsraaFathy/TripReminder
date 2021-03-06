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

public class RecyclerHomeAdapter extends RecyclerView.Adapter<RecyclerHomeAdapter.ViewHolder> {

    private Context context;
    private List<TripTable> trips;
    private OcCLickListenerAble ocCLickListenerAble;
    public static final String MENU="MENU";
    public static final String NOTES="NOTES";
    public static final String START="START";

    public void setTrips(List<TripTable> trips) {
        this.trips = trips;
    }

    public RecyclerHomeAdapter(Context context) {
        this.context = context;
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
//        private TextView repetition;
//        private TextView ways;
        private TextView from;
        private TextView to;
        private ImageView notes;
        private ImageView menu;
        private TextView startButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.TripNameHistoryItem);
            time = itemView.findViewById(R.id.timeTextViewHistoryItem);
            date = itemView.findViewById(R.id.dateTextViewHistoryItem);
            status = itemView.findViewById(R.id.statusHistoryItem);
            from = itemView.findViewById(R.id.fromHistoryItem);
            to = itemView.findViewById(R.id.toHistoryItem);
            notes = itemView.findViewById(R.id.notesHistoryItem);
            menu = itemView.findViewById(R.id.menueIconHistoryItem);
            startButton = itemView.findViewById(R.id.startButton);

            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ocCLickListenerAble.onItemClick(MENU,trips.get(getAdapterPosition()));
                }
            });

            notes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ocCLickListenerAble.onItemClick(NOTES,trips.get(getAdapterPosition()));
                }
            });
            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ocCLickListenerAble.onItemClick(START,trips.get(getAdapterPosition()));

                }
            });


        }
    }
    public void OnItemClickListener(OcCLickListenerAble ocCLickListenerAble)
    {
        this.ocCLickListenerAble=ocCLickListenerAble;
    }
    public interface OcCLickListenerAble{
        void onItemClick(String type,TripTable tripTable);
    }
}
