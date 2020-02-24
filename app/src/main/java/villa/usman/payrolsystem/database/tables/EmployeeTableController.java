package villa.usman.payrolsystem.database.tables;

import android.content.Context;

import villa.usman.payrolsystem.common.Constants;
import villa.usman.payrolsystem.database.DatabaseHandler;
import villa.usman.payrolsystem.entities.Employee;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;

public class EmployeeTableController extends DatabaseHandler {

    public EmployeeTableController(Context context) {
        super(context);
    }

    public boolean create(Employee objectEmployee) {

        ContentValues values = new ContentValues();
        values.put("FirstName", objectEmployee.FirstName);
        values.put("LastName", objectEmployee.LastName);
        values.put("Gender", objectEmployee.Gender);
        values.put("DateOfBirth", objectEmployee.DateOfBirth);
        values.put("DateOfJoining", objectEmployee.DateOfJoining);
        values.put("Salary", objectEmployee.Salary);
        values.put("Designation",objectEmployee.Designation);

        SQLiteDatabase db = this.getWritableDatabase();

        boolean createSuccessful = db.insert("employee", null, values) > 0;
        db.close();

        return createSuccessful;
    }

    public int count() {

        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT * FROM employee";
        int recordCount = db.rawQuery(sql, null).getCount();
        db.close();

        return recordCount;

    }

    public Employee read() {

        Employee employee = new Employee();

        String sql = "SELECT * FROM employee LIMIT 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                employee.EmployeeID = cursor.getInt(cursor.getColumnIndex(Constants.EmployeeId));
                employee.FirstName = cursor.getString(cursor.getColumnIndex(Constants.FirstName));
                employee.LastName = cursor.getString(cursor.getColumnIndex(Constants.LastName));
                employee.Gender = cursor.getString(cursor.getColumnIndex(Constants.Gender));
                employee.DateOfBirth = cursor.getString(cursor.getColumnIndex(Constants.DateOfBirth));
                employee.DateOfJoining = cursor.getString(cursor.getColumnIndex(Constants.DateOfJoining));
                employee.Salary = cursor.getString(cursor.getColumnIndex(Constants.Salary));
                employee.Designation = cursor.getString(cursor.getColumnIndex(Constants.Designation));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return employee;
    }

    public boolean delete(int id) {
        boolean deleteSuccessful = false;

        SQLiteDatabase db = this.getWritableDatabase();
        deleteSuccessful = db.delete("employee", "EmployeeId ='" + id + "'", null) > 0;
        db.close();

        return deleteSuccessful;

    }

}
