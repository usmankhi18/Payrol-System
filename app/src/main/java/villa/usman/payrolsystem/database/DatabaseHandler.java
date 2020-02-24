package villa.usman.payrolsystem.database;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import villa.usman.payrolsystem.common.Constants;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE employee " +
                "( EmployeeId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "FirstName TEXT,LastName TEXT, Gender TEXT, DateOfBirth TEXT, DateOfJoining TEXT,  " +
                "Salary TEXT,Designation TEXT ) ";

        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS employee";
        db.execSQL(sql);

        onCreate(db);
    }

}
