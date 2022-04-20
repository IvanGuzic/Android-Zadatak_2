package com.guzic.myapplication.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLiteManager extends SQLiteOpenHelper {

    private static SQLiteManager sqLiteManager;
    private static final String DATABASE_NAME = "ClockDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "clock";
    private static final String COUNTER = "counter";
    private static final String ID_FIELD = "id";
    private static final String TITLE_FIELD = "title";
    private static final String CATEGORY_FIELD = "category";
    private static final String CREATED_AT_FIELD = "createdAt";
    private static final String IMAGE_FIELD = "bitmap";
    private static final String DELETED_FIELD = "deleted";

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

    public SQLiteManager(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    /**
     * https://www.youtube.com/watch?v=4k1ZMpO9Zn0&ab_channel=CodeWithCal
     * @param context
     * @return
     */
    public static SQLiteManager instanceOfDatabase(Context context) {

        if(sqLiteManager == null) {
            sqLiteManager = new SQLiteManager(context);
        }

        return sqLiteManager;

    }

    /**
     * https://www.youtube.com/watch?v=4k1ZMpO9Zn0&ab_channel=CodeWithCal
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        StringBuilder sql;
        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append("(")
                .append(COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(ID_FIELD)
                .append(" INT, ")
                .append(TITLE_FIELD)
                .append(" TEXT, ")
                .append(CATEGORY_FIELD)
                .append(" TEXT, ")
                .append(CREATED_AT_FIELD)//**
                .append(" DATE, ") //**
                .append(IMAGE_FIELD)//**
                .append(" BLOB, ") //**
                .append(DELETED_FIELD)
                .append(" BOOLEAN)"); //**
        sqLiteDatabase.execSQL(sql.toString());

    }

    /**
     * Implement (extends SQLiteOpenHelper)
     * @param sqLiteDatabase
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    /**
     * https://www.youtube.com/watch?v=4k1ZMpO9Zn0&ab_channel=CodeWithCal
     * @param clock
     */
    public void addClockToDatabase(ClockModel clock) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, clock.getId());
        contentValues.put(TITLE_FIELD, clock.getTitle());
        contentValues.put(CATEGORY_FIELD, clock.getCategory());
        contentValues.put(CREATED_AT_FIELD, getStringFromDate(clock.getCreatedAt()));
        contentValues.put(IMAGE_FIELD, imageBitmapToByte(clock.getBitmap()));
        contentValues.put(DELETED_FIELD, clock.getDeleted());
        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

    }

    /**
     * https://gist.github.com/crazy-diya/a4576c271ae01370ceca630078315976
     * @param bitmap
     * @return
     */
    public static byte[] imageBitmapToByte(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;

    }

    /**
     * https://www.youtube.com/watch?v=4k1ZMpO9Zn0&ab_channel=CodeWithCal
     */
    public void populateClockListArray() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try(Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + DELETED_FIELD + " = 0", null)) {

            if(result.getCount() != 0) {

                while(result.moveToNext()) {

                    try {
                        int id = result.getInt(1);
                        String title = result.getString(2);
                        String category = result.getString(3);
                        Date createdAt = getDateFromString(result.getString(4));
                        byte[] byteArray = result.getBlob(5);
                        boolean deleted = result.getInt(6) > 0;
                        Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                        ClockModel clock = new ClockModel(id, title, category, createdAt, bm, deleted);
                        ClockModel.clockArrayList.add(clock);
                    }
                    catch(Exception e) {
                        Log.i( "Exception: ", e.getMessage());
                    }

                }

            }

        }

    }

    /**
     * https://www.youtube.com/watch?v=4k1ZMpO9Zn0&ab_channel=CodeWithCal
     * @param clock
     */
    public void updateClockInDB(ClockModel clock) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, clock.getId());
        contentValues.put(TITLE_FIELD, clock.getTitle());
        contentValues.put(CATEGORY_FIELD, clock.getCategory());
        contentValues.put(CREATED_AT_FIELD, getStringFromDate(clock.getCreatedAt()));
        contentValues.put(IMAGE_FIELD, imageBitmapToByte(clock.getBitmap()));
        contentValues.put(DELETED_FIELD, clock.getDeleted());
        sqLiteDatabase.update(TABLE_NAME, contentValues, ID_FIELD + " =? ",
                new String[] {
                        String.valueOf(clock.getId())
        });

    }

    /**
     * https://www.youtube.com/watch?v=4k1ZMpO9Zn0&ab_channel=CodeWithCal
     * @param date
     * @return
     */
    private String getStringFromDate(Date date) {

        if(date == null) {
            return null;
        }

        return dateFormat.format(date);

    }

    /**
     * https://www.youtube.com/watch?v=4k1ZMpO9Zn0&ab_channel=CodeWithCal
     * @param string
     * @return
     */
    private Date getDateFromString(String string) {

        try {
            return dateFormat.parse(string);
        }
        catch(ParseException | NullPointerException e) {
            return null;
        }

    }

}