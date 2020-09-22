package com.example.timesayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.SyncStateContract;

import java.security.Key;
import java.util.ArrayList;

public class TodoDatabase {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "todo_name";
    public static final String KEY_TIME = "_time";

    private final String DATABASE_NAME = "TodoDB";
    private final String DATABASE_TABLE = "TodoTable";
    private final int DATABASE_VERSION = 1;

    private DBHelper ourHelper;
    private final Context ourOContext;
    private SQLiteDatabase ourDatabase;

    public  TodoDatabase(Context context){
        ourOContext = context;
    }

    private class DBHelper extends SQLiteOpenHelper{

        public DBHelper (Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

            String query = "CREATE TABLE " + DATABASE_TABLE + " (" +
                    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_NAME + " TEXT NOT NULL , " +
                    KEY_TIME + " TEXT NOT NULL);";

            sqLiteDatabase.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(sqLiteDatabase);
        }
    }

    public TodoDatabase open() throws SQLException{
      ourHelper = new DBHelper(ourOContext);
      ourDatabase = ourHelper.getWritableDatabase();
      return this;
    }

    public void close(){
        ourHelper.close();
    }

    public long createEntry(String todoName, String time){
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, todoName);
        contentValues.put(KEY_TIME, time);
        return ourDatabase.insert(DATABASE_TABLE, null, contentValues);
    }

    public String getData(){
        String[] columns = new String[] {KEY_ROWID, KEY_NAME, KEY_TIME};
        Cursor cursor = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
        String result = "";

        int iRowId = cursor.getColumnIndex(KEY_ROWID);
        int iName = cursor.getColumnIndex(KEY_NAME);
        int iTime = cursor.getColumnIndex(KEY_TIME);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            result = result + cursor.getString(iRowId) + ": " + cursor.getString(iName) + " " +
            cursor.getString(iTime) + "\n";
        }
        cursor.close();
        return result;
    }

    public Cursor getRefresh(){
        String[] columns = new String[] { KEY_NAME, KEY_TIME};
        return  ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);

    }
    public ArrayList<Todo> retrieveData(){
        ArrayList<Todo> datas = new ArrayList<>();

        try {
             ourDatabase = ourHelper.getWritableDatabase();
            String[] columns = new String[] {KEY_ROWID, KEY_NAME, KEY_TIME};
            Cursor cursor = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);

            Todo todo;
            datas.clear();

            while (cursor.moveToNext()){
                String s_name = cursor.getString(1);
                String time = cursor.getString(2);

                todo = new Todo();
                todo.setActivityName(s_name);
                todo.setActivityTime(time);
                datas.add(todo);
            }
            cursor.close();
        }catch (SQLException e){
            e.printStackTrace();
        } finally {
            ourHelper.close();

        }
        return datas;
    }

    public boolean checkDB(){
        ourHelper = new DBHelper(ourOContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return DatabaseUtils.queryNumEntries(ourDatabase, DATABASE_TABLE) == 0;
    }


    public long deleteEntry(String rowId){
        return ourDatabase.delete(DATABASE_TABLE, KEY_ROWID + "=?", new String[]{rowId});
    }

    public long updateEntry(String rowID, String name, String time){
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_TIME, time);

        return ourDatabase.update(DATABASE_TABLE, contentValues, KEY_ROWID + "=?", new String[]{rowID});
    }
}
