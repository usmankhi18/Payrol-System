package villa.usman.payrolsystem.common;

public class Constants {
    public static int Admin = 2;
    public static String Server = "http://10.0.7.109/PayrolService1/";
    public static String APIUserName = "admin";
    public static String APIPassword = "admin123";
    public static String EmployeeAPI = "api/Employee/";
    public static String InsertEmployeeCall = "InsertEmployee";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PayrolSystemDatabase";
    public static final String ConfigurationPREFERENCES = "ConfigurationPREFERENCES" ;
    //EmployeeTable Column Names
    public static final String EmployeeId = "EmployeeId";
    public static final String FirstName = "FirstName";
    public static final String LastName = "LastName";
    public static final String Gender = "Gender";
    public static final String DateOfBirth = "DateOfBirth";
    public static final String DateOfJoining = "DateOfJoining";
    public static final String Salary = "Salary";
    public static final String Designation = "Designation";
    //Configuration Shared Preference Keys
    public static final String ServerKey = "ServerKey";
    public static final String APIUserNameKey = "APIUserNameKey";
    public static final String APIPasswordKey = "APIPasswordKey";
    public static final String SyncAutomaticKey = "SyncAutomaticKey";
    public static final String SyncTimeKey = "SyncTimeKey";
}
