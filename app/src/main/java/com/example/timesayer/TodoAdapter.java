package com.example.timesayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private ArrayList<Todo> todoActivities;

    public TodoAdapter (Context context, ArrayList<Todo> todos){
        todoActivities = todos;
    }

    @NonNull
    @Override
    public TodoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(todoActivities.get(position));
        holder.name.setText(todoActivities.get(position).getActivityName());
        holder.time.setText(todoActivities.get(position).getActivityTime());
    }

    @Override
    public int getItemCount() {
        return todoActivities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name, time;
        ImageView imageIcon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewName);
            time = itemView.findViewById(R.id.textViewTime);
            imageIcon = itemView.findViewById(R.id.imageIcon);
        }
    }
}
