package com.delta.pragyan16;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by lakshmanaram on 20/1/16.
 */
public class EventsAdapter {
    Context context;
    EventsHelper eventsHelper;
    ArrayList<String> Cluster = new ArrayList<>();
    ArrayList<String> Eventnames = new ArrayList<>();
    ArrayList<EventInfo> Events = new ArrayList<>();

    public EventsAdapter(Context contex)
    {
        this.context = contex;
        eventsHelper = new EventsHelper(context);
    }

    public ArrayList<String> getCluster(){                              //todo get cluster list
        Cluster.clear();
        SQLiteDatabase db = eventsHelper.getWritableDatabase();
        Cursor c = db.query(true, EventsHelper.TABLE_NAME, new String[]{EventsHelper.CLUSTER}, null, null, null, null, null, null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(EventsHelper.CLUSTER))!=null || !c.getString(c.getColumnIndex(EventsHelper.CLUSTER)).isEmpty()){
                Cluster.add(c.getString(c.getColumnIndex(EventsHelper.CLUSTER)));
            }
            c.moveToNext();
        }
        c.close();
        return Cluster;
    }

    public void utilgetAllEvents(){                                               //todo get all events utility function - also used to refresh the list
                Events.clear();
                SQLiteDatabase db = eventsHelper.getWritableDatabase();
                String[] columns = {EventsHelper.EVENTID,EventsHelper.NAME,EventsHelper.START_TIME,EventsHelper.END_TIME,EventsHelper.VENUE,EventsHelper.DATE,EventsHelper.CLUSTER,EventsHelper.DESCRIP,EventsHelper.LAST_UPDATE_TIME};
                Cursor cursor = db.query(true, EventsHelper.TABLE_NAME, columns, null, null, null, null, null, null);
                int index = cursor.getColumnIndex(EventsHelper.NAME);
                while(cursor.moveToNext()){
                        if(cursor.getString(index)!= null|| !cursor.getString(index).isEmpty()) {
                                EventInfo eventInfo = null;
                                eventInfo = new EventInfo();
                                eventInfo.name = cursor.getString(index);
                                eventInfo.id = cursor.getInt(cursor.getColumnIndex(EventsHelper.EVENTID));
                                eventInfo.cluster = cursor.getString(cursor.getColumnIndex(EventsHelper.CLUSTER));
                                eventInfo.start_time = cursor.getString(cursor.getColumnIndex(EventsHelper.START_TIME));
                                eventInfo.end_time = cursor.getString(cursor.getColumnIndex(EventsHelper.END_TIME));
                                eventInfo.last_update_time = cursor.getString(cursor.getColumnIndex(EventsHelper.LAST_UPDATE_TIME));
                                eventInfo.venue = cursor.getString(cursor.getColumnIndex(EventsHelper.VENUE));
                                eventInfo.date = cursor.getString(cursor.getColumnIndex(EventsHelper.DATE));
                                eventInfo.description = cursor.getString(cursor.getColumnIndex(EventsHelper.DESCRIP));
                                Events.add(eventInfo);
                            }
                    }
                cursor.close();
            }

                public ArrayList<EventInfo> getAllEvents(){                                     //todo get already existing events in the list. restores if the list is empty
                if(Events.isEmpty())
                        utilgetAllEvents();
                return Events;                                                              //todo returns null if the database is empty
            }


    public ArrayList<String> getEventnamesOfCluster(String cluster){                    //todo get events present in 1 cluster
        Eventnames.clear();
        SQLiteDatabase db = eventsHelper.getWritableDatabase();
        Cursor cursor = db.query(true, EventsHelper.TABLE_NAME, new String[]{EventsHelper.NAME}, EventsHelper.CLUSTER + " = ?", new String[]{cluster}, null, null, null, null);
        int index = cursor.getColumnIndex(EventsHelper.NAME);
        while(cursor.moveToNext()){
            if(cursor.getString(index)!= null|| !cursor.getString(index).isEmpty())
                Eventnames.add(cursor.getString(index));
        }
        cursor.close();
        return Eventnames;
    }

    public EventInfo getEventInfo(String eventName){                                    //todo get event info
        EventInfo eventInfo = null;
        SQLiteDatabase db = eventsHelper.getWritableDatabase();
        String[] columns = {EventsHelper.EVENTID,EventsHelper.NAME,EventsHelper.START_TIME,EventsHelper.END_TIME,EventsHelper.VENUE,EventsHelper.DATE,EventsHelper.CLUSTER,EventsHelper.DESCRIP,EventsHelper.LAST_UPDATE_TIME};
        Cursor cursor = db.query(true, EventsHelper.TABLE_NAME, columns, EventsHelper.NAME + " = ?", new String[]{eventName}, null, null, null, null);
        int index = cursor.getColumnIndex(EventsHelper.NAME);
        while(cursor.moveToNext()){
            if(cursor.getString(index)!= null|| !cursor.getString(index).isEmpty()) {
                eventInfo = new EventInfo();
                eventInfo.name = cursor.getString(index);
                eventInfo.id = cursor.getInt(cursor.getColumnIndex(EventsHelper.EVENTID));
                eventInfo.cluster = cursor.getString(cursor.getColumnIndex(EventsHelper.CLUSTER));
                eventInfo.start_time = cursor.getString(cursor.getColumnIndex(EventsHelper.START_TIME));
                eventInfo.end_time = cursor.getString(cursor.getColumnIndex(EventsHelper.END_TIME));
                eventInfo.last_update_time = cursor.getString(cursor.getColumnIndex(EventsHelper.LAST_UPDATE_TIME));
                eventInfo.venue = cursor.getString(cursor.getColumnIndex(EventsHelper.VENUE));
                eventInfo.date = cursor.getString(cursor.getColumnIndex(EventsHelper.DATE));
                eventInfo.description = cursor.getString(cursor.getColumnIndex(EventsHelper.DESCRIP));
            }
        }
        cursor.close();
        return eventInfo;                                                                           //todo returns null if no such event name exists
    }

    public void update_event(EventInfo eventInfo){
        if(eventInfo == null||eventInfo.name == null)
            return;
        SQLiteDatabase db = eventsHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(EventsHelper.EVENTID,eventInfo.id);
        cv.put(EventsHelper.CLUSTER,eventInfo.cluster);
        cv.put(EventsHelper.START_TIME,eventInfo.start_time);
        cv.put(EventsHelper.END_TIME,eventInfo.end_time);
        cv.put(EventsHelper.LAST_UPDATE_TIME,eventInfo.last_update_time);
        cv.put(EventsHelper.VENUE,eventInfo.venue);
        cv.put(EventsHelper.DATE,eventInfo.date);
        cv.put(EventsHelper.DESCRIP,eventInfo.description);
        int result = db.update(EventsHelper.TABLE_NAME,cv,EventsHelper.NAME+"=?",new String[]{eventInfo.name});
    }


    public boolean find_existing(EventInfo eventInfo){
        if(eventInfo == null|| eventInfo.name == null || eventInfo.name.isEmpty())
            return false;
        SQLiteDatabase db = eventsHelper.getWritableDatabase();
        Cursor cursor1 = db.query(EventsHelper.TABLE_NAME, new String[] {EventsHelper.EVENTID}, EventsHelper.NAME + " =? ", new String[]{eventInfo.name}, null, null, null);
        cursor1.moveToNext();
        int no_of_existing_records = cursor1.getCount();
        cursor1.close();
        return (no_of_existing_records==0);
    }

    public void add_event(EventInfo eventInfo){               //to add attendance
        if(eventInfo==null)
            return;
        SQLiteDatabase sqLiteDatabase = eventsHelper.getWritableDatabase();
        if(find_existing(eventInfo)){
            ContentValues cv = new ContentValues();
            cv.put(EventsHelper.EVENTID,eventInfo.id);
            cv.put(EventsHelper.NAME,eventInfo.name);
            cv.put(EventsHelper.CLUSTER,eventInfo.cluster);
            cv.put(EventsHelper.START_TIME,eventInfo.start_time);
            cv.put(EventsHelper.END_TIME,eventInfo.end_time);
            cv.put(EventsHelper.LAST_UPDATE_TIME,eventInfo.last_update_time);
            cv.put(EventsHelper.VENUE,eventInfo.venue);
            cv.put(EventsHelper.DATE,eventInfo.date);
            cv.put(EventsHelper.DESCRIP,eventInfo.description);
            sqLiteDatabase.insert(EventsHelper.TABLE_NAME, null, cv);
        }else{
            update_event(eventInfo);
        }
    }


    private static class EventsHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "pragyan.db";
        private static String TABLE_NAME = "events";
        Context context = null;
        private static final int DATABASE_VERSION = 5;
        private static final String ID = "_id";
        private static final String EVENTID = "eventid";
        private static final String NAME = "name";
        private static final String START_TIME = "start_time";
        private static final String END_TIME = "end_time";
        private static final String VENUE = "venue";
        private static final String DATE = "date";
        private static final String CLUSTER = "cluster";
        private static final String LAST_UPDATE_TIME = "last_update_time";
        private static final String DESCRIP = "description";
        private static final String create = "CREATE TABLE "+TABLE_NAME+" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+EVENTID+" INTEGER, "+NAME+" TEXT, "+START_TIME+" TEXT, "+END_TIME+" TEXT, "+VENUE+" TEXT, "+DATE+" TEXT, "+CLUSTER+" TEXT, "+DESCRIP+" TEXT, "+LAST_UPDATE_TIME+" TEXT);";
        private static final String drop = "DROP TABLE IF EXISTS "+TABLE_NAME+";";
        public EventsHelper(Context context)
        {
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            try {
                sqLiteDatabase.execSQL(create);
            } catch (SQLException e) {
                Log.d("created", e.toString());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            try {
                sqLiteDatabase.execSQL(drop);
                onCreate(sqLiteDatabase);
            } catch (SQLException e) {
                Log.d("upgraded",e.toString());
            }
        }
    }
}

