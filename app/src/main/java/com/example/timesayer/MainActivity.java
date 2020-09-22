package com.example.timesayer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {


    ListFrag listFrag;
    TextView seeActivities;
    FragmentManager fragmentManager;
    RelativeLayout textLay;
     private String todo = "";
     private String time ="";
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textLay = findViewById(R.id.textLay);
        fragmentManager = this.getSupportFragmentManager();
        listFrag = (ListFrag) fragmentManager.findFragmentById(R.id.fragment);


        seeActivities = findViewById(R.id.allActivities);
        seeActivities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showData();
            }
        });

        boolean loyal = LayRef();
        if (!loyal)
            textLay.setVisibility(View.GONE);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 LayoutInflater layoutInflater = LayoutInflater.from(context);
                View promptsView = layoutInflater.inflate(R.layout.pop_up, null);

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

                alertBuilder.setView(promptsView);

                final EditText input1 = promptsView.findViewById(R.id.firstTextField);
                final TimePicker input2 = promptsView.findViewById(R.id.timeView);


//                timeView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        showDialog();
//                    }
//                });


                alertBuilder.setCancelable(false).setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                         todo = input1.getText().toString().trim();
                        Calendar calendar = Calendar.getInstance();
                        if (android.os.Build.VERSION.SDK_INT >= 23) {
                            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                    input2.getHour(), input2.getMinute(), 0);
                        } else {
                            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                                    input2.getCurrentHour(), input2.getCurrentMinute(), 0);
                        }
                        time = input2.getCurrentHour() + ":" +
                                input2.getCurrentMinute() +
                                showTime(input2.getCurrentHour()) +
                                " ";

                        if (todo.isEmpty()|| time.isEmpty()){
                            Toast.makeText(MainActivity.this, "Enter All Fields", Toast.LENGTH_LONG).show();
                        }
                        else {
                           save();
                            listFrag.refresh();
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alertDialog = alertBuilder.create();
                alertDialog.show();

                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                   //     .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void save(){
        try {
            TodoDatabase db = new TodoDatabase(MainActivity.this);
            db.open();
            db.createEntry(todo, time);
            db.close();


            Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
        }
        catch (SQLException e){
                e.getMessage();
        }
    }

    public void showData(){
        startActivity(new Intent(this, ShowInfo.class));
    }

//    public void refreshFrag(Fragment fragment){
//        getSupportFragmentManager()
//                .beginTransaction()
//                .detach(fragment)
//                .attach(fragment)
//                .commit();
//    }

    public boolean LayRef(){
        try {
            TodoDatabase db = new TodoDatabase(MainActivity.this);
            if (!(db.checkDB()))
                return  false;

        }


        catch (SQLException e){
            e.printStackTrace();
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT ).show();
        }
        return true;
    }

    public String showTime(int hour) {
        String format;
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        return format;
    }
}
