package com.example.smartattend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    public static final String DATABASE_NAME = "AttendanceDB.db";
    public static final int DATABASE_VERSION = 1;

    // Faculty Table
    public static final String TABLE_FACULTY = "faculty";
    public static final String FACULTY_ID = "id";
    public static final String FACULTY_NAME = "name";
    public static final String FACULTY_USERNAME = "username";
    public static final String FACULTY_PASSWORD = "password";
    public static final String FACULTY_EMAIL = "email";
    public static final String FACULTY_CLASS = "class_name";

    // Student Table
    public static final String TABLE_STUDENT = "student";
    public static final String STUDENT_ID = "id";
    public static final String STUDENT_NAME = "name";
    public static final String STUDENT_USERNAME = "username";
    public static final String STUDENT_PASSWORD = "password";
    public static final String STUDENT_ROLL = "roll";
    public static final String STUDENT_EMAIL = "email";
    public static final String STUDENT_CLASS = "class_name";

    // Attendance Table
    public static final String TABLE_ATTENDANCE = "attendance";
    public static final String ATTENDANCE_ID = "id";
    public static final String ATTENDANCE_STUDENT_ROLL = "roll";
    public static final String ATTENDANCE_DATE = "date";
    public static final String ATTENDANCE_STATUS = "status"; // Present / Absent

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Faculty Table
        String createFacultyTable = "CREATE TABLE " + TABLE_FACULTY + " (" +
                FACULTY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FACULTY_NAME + " TEXT, " +
                FACULTY_USERNAME + " TEXT, " +
                FACULTY_PASSWORD + " TEXT, " +
                FACULTY_EMAIL + " TEXT, " +
                FACULTY_CLASS + " TEXT)";
        db.execSQL(createFacultyTable);

        // Create Student Table
        String createStudentTable = "CREATE TABLE " + TABLE_STUDENT + " (" +
                STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                STUDENT_NAME + " TEXT, " +
                STUDENT_USERNAME + " TEXT, " +
                STUDENT_PASSWORD + " TEXT, " +
                STUDENT_ROLL + " TEXT, " +
                STUDENT_EMAIL + " TEXT, " +
                STUDENT_CLASS + " TEXT)";
        db.execSQL(createStudentTable);

        // Create Attendance Table
        String createAttendanceTable = "CREATE TABLE " + TABLE_ATTENDANCE + " (" +
                ATTENDANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ATTENDANCE_STUDENT_ROLL + " TEXT, " +
                ATTENDANCE_DATE + " TEXT, " +
                ATTENDANCE_STATUS + " TEXT, "+
                "notified INTEGER DEFAULT 0, "+
                "UNIQUE(" + ATTENDANCE_STUDENT_ROLL + ", " + ATTENDANCE_DATE + "))";
        db.execSQL(createAttendanceTable);

        // Insert hardcoded faculty
        db.execSQL("INSERT INTO " + TABLE_FACULTY + " (name, username, password, email, class_name) VALUES " +
                "('Prof. Sharma', 'faculty1', 'pass1', 'sharma@college.com', 'Class A')," +
                "('Prof. Verma', 'faculty2', 'pass2', 'verma@college.com', 'Class B')");

        // Insert hardcoded students
        db.execSQL("INSERT INTO " + TABLE_STUDENT + " (name, username, password, roll, email, class_name) VALUES " +
                "('Alice', 'student1', 'pass1', '101', 'alice@student.com', 'Class A')," +
                "('Bob', 'student2', 'pass2', '102', 'bob@student.com', 'Class A')," +
                "('Charlie', 'student3', 'pass3', '201', 'charlie@student.com', 'Class B')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FACULTY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
        onCreate(db);
    }

    // Faculty login
    public boolean validateFaculty(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FACULTY +
                " WHERE email=? AND password=?", new String[]{email, password});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    // Student login
    public boolean validateStudent(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_STUDENT +
                " WHERE email=? AND password=?", new String[]{email, password});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    // Get all students from a particular class
    public Cursor getStudentsByClass(String className) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_STUDENT + " WHERE class_name=?", new String[]{className});
    }

    // Mark attendance
    public boolean markAttendance(String roll, String date, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ATTENDANCE +
                        " WHERE " + ATTENDANCE_STUDENT_ROLL + " = ? AND " + ATTENDANCE_DATE + " = ?",
                new String[]{roll, date});

        boolean alreadyMarked = cursor.moveToFirst();
        cursor.close();

        if (alreadyMarked) {
            Log.d("ATTENDANCE", "Already marked for roll " + roll + " on " + date);
            return false;
        } else {
            ContentValues values = new ContentValues();
            values.put(ATTENDANCE_STUDENT_ROLL, roll);
            values.put(ATTENDANCE_DATE, date);
            values.put(ATTENDANCE_STATUS, status);
            values.put("notified", 0);

            long result = db.insert(TABLE_ATTENDANCE, null, values);
            Log.d("DB_INSERT", "Inserted: " + result);
            return result != -1;
        }
    }

    //Update attendance status
    public void updateAttendanceNotificationStatus(String rollNo, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_ATTENDANCE +
                " SET notified = 1 WHERE roll = ? AND date = ?";
        db.execSQL(query, new Object[]{rollNo, date});
        db.close();
    }

    // View attendance by student roll number
    public Cursor getAttendanceByRoll(String roll) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ATTENDANCE + " WHERE roll=?", new String[]{roll});
    }

    public List<AttendanceModel> getAllAttendance() {
        List<AttendanceModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ATTENDANCE, null);

        if (cursor.moveToFirst()) {
            do {
                String roll = cursor.getString(cursor.getColumnIndexOrThrow(ATTENDANCE_STUDENT_ROLL));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(ATTENDANCE_DATE));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(ATTENDANCE_STATUS));

                list.add(new AttendanceModel(roll, date, status));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }
    /*

    public Cursor getStudentByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_STUDENT + " WHERE username=?", new String[]{username});
    }

    public List<AttendanceModel> getAttendanceByRollAsList(String roll) {
        List<AttendanceModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ATTENDANCE + " WHERE roll=?", new String[]{roll});

        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                list.add(new AttendanceModel(roll, date, status));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }
*/
    public Cursor getAttendanceForStudent(String roll) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM attendance WHERE roll = ?", new String[]{roll});
    }
    public Cursor getStudentByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_STUDENT + " WHERE email = ?", new String[]{email});
    }
    public List<StudentModel> getAllStudents() {
        List<StudentModel> students = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name, roll FROM " + TABLE_STUDENT, null);


        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String roll = cursor.getString(cursor.getColumnIndexOrThrow("roll"));
                students.add(new StudentModel(name, roll, false)); // default not present
            } while (cursor.moveToNext());
        }

        cursor.close();
        return students;
    }


}