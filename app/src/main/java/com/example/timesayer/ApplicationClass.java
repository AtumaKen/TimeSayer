package com.example.timesayer;

import android.app.Activity;
import android.app.Application;
import android.database.SQLException;

import java.util.ArrayList;

public class ApplicationClass extends Application {
    public static ArrayList<Todo> todos;

    @Override
    public void onCreate() {
        super.onCreate();

       todos = new ArrayList<>();
        try{
            TodoDatabase db = new TodoDatabase(this);
            db.open();
            todos = db.retrieveData();
            db.close();

        }
        catch (SQLException e){
            e.getMessage();
        }
    }
}
