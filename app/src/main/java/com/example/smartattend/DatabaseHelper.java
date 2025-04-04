package com.example.smartattend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "StudentDB";
    private static final String TABLE_NAME = "students";
    private static final String COL_ROLL = "roll_no";
    private static final String COL_NAME = "name";
    private static final String COL_MARKS = "marks";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ATTENDANCE_TABLE = "CREATE TABLE attendance ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "roll_no TEXT, "
                + "date TEXT, " // Store the date when attendance is marked
                + "status TEXT)"; // Present/Absent

        db.execSQL(CREATE_ATTENDANCE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertStudent(String rollNo, String name, String marks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ROLL, rollNo);
        contentValues.put(COL_NAME, name);
        contentValues.put(COL_MARKS, marks);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getAllStudents() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public boolean markAttendance(String rollNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM attendance WHERE roll_no=?", new String[]{rollNo});

        if (cursor.getCount() == 0) {
            db.execSQL("INSERT INTO attendance (roll_no, total_days) VALUES (?, 1)", new String[]{rollNo});
        } else {
            db.execSQL("UPDATE attendance SET total_days = total_days + 1 WHERE roll_no=?", new String[]{rollNo});
        }
        return true;
    }

    public Cursor getAttendance() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM attendance", null);
    }

    public boolean insertTestScore(String rollNo, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("roll_no", rollNo);
        values.put("score", score);
        return db.insert("performance", null, values) != -1;
    }

    public Cursor getPerformance() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT roll_no, AVG(score) FROM performance GROUP BY roll_no", null);
    }

}
