package com.example.tripreminder.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripreminder.R;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewModel> {
    List<String> notesList;

    public void setNotesList(List<String> notesList) {
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public ViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.note_item_design,parent,false);
        return new ViewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewModel holder, int position) {

    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public static class ViewModel extends RecyclerView.ViewHolder {
        EditText editText;
        ImageView imageView;
        public ViewModel(@NonNull View itemView) {
            super(itemView);
            editText=itemView.findViewById(R.id.editNoteItemDesign);
            imageView=itemView.findViewById(R.id.deleteNotesItemDesign);
        }
    }
}
