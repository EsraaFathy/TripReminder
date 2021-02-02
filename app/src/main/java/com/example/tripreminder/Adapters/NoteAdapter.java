package com.example.tripreminder.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripreminder.R;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewModel> {
    List<String> notesList;
    private SetOnClickListener setOnClickListener;


    public void setNotesList(List<String> notesList) {
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public ViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.note_item_design, parent, false);
        return new ViewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewModel holder, int position) {
        holder.textview.setText(notesList.get(position));

    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class ViewModel extends RecyclerView.ViewHolder {
        TextView textview;
        ImageView imageView;

        public ViewModel(@NonNull View itemView) {
            super(itemView);
            textview = itemView.findViewById(R.id.editNoteItemDesign);
            imageView = itemView.findViewById(R.id.deleteNotesItemDesign);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setOnClickListener.onClickListener(getAdapterPosition(),"delete");
                }
            });

        }
    }

    public void OnItemClickListener(SetOnClickListener setOnClickListener) {
        this.setOnClickListener = setOnClickListener;
    }

    public interface SetOnClickListener {
        void onClickListener(int position ,String viewType);
    }
}
