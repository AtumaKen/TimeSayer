package com.example.timesayer;


import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFrag extends Fragment {

    ArrayList<Todo> scrolls;
    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
    View view;

    public ListFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = view.findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);

        scrolls = new ArrayList<>();
        myAdapter = new TodoAdapter(this.getActivity(), ApplicationClass.todos );
        recyclerView.setAdapter(myAdapter);

    }

//    public ArrayList<Todo> fetchData(){
//        ArrayList<Todo> res = new ArrayList<>();
//        try{
//            TodoDatabase db = new TodoDatabase(getContext());
//            db.open();
//            res = db.retrieveData();
//            db.close();
//
//        }
//        catch (SQLException e){
//            e.getMessage();
//        }
//        return res;
//    }

    public void refresh(){
        ApplicationClass.todos.clear();

        TodoDatabase db = new TodoDatabase(getContext());
        db.open();
        Cursor c=db.getRefresh();


        while (c.moveToNext()){
            String id = c.getString(0);
            String name = c.getString(1);

            Todo td = new Todo();
            td.setActivityName(id);
            td.setActivityTime(name);

            ApplicationClass.todos.add(td);
        }
        db.close();
        myAdapter.notifyDataSetChanged();
    }
}
