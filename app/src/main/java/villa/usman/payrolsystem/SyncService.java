package villa.usman.payrolsystem;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import villa.usman.payrolsystem.common.CommonMethods;
import villa.usman.payrolsystem.common.Constants;
import villa.usman.payrolsystem.database.tables.EmployeeTableController;
import villa.usman.payrolsystem.entities.Employee;

import static villa.usman.payrolsystem.common.Constants.EmployeeAPI;
import static villa.usman.payrolsystem.common.Constants.InsertEmployeeCall;
import static villa.usman.payrolsystem.common.Constants.ServerKey;

public class SyncService extends Service {
    SharedPreferences sharedpreferences;
    int LocalEmployeeID = 0;
    String firstName;
    String lastName;
    String gender;
    String DOB;
    String DOJ;
    Double Salary;
    String Designation;
    int ActionBy;
    String ActionOn;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sharedpreferences = getSharedPreferences(Constants.ConfigurationPREFERENCES, Context.MODE_PRIVATE);
        checkConnectivity();
        return START_STICKY;
    }

    private void checkConnectivity(){
        if (new CommonMethods().isOnline(getApplicationContext())) {
            SyncData();
        } else {
            Toasty.error(getApplicationContext(), "Internet Not Connected", Toasty.LENGTH_LONG).show();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            onDestroy();
        }
    }

    private void SyncData(){
        try{
            int count = countRecords();
            if(count>0) {
                LoadTopEmployee();
                CallAPI();
            }else{
                onDestroy();
            }
        }catch (Exception ex){
            Toasty.error(this, ex.getMessage(), Toasty.LENGTH_LONG).show();
        }
    }

    private void CallAPI() {
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = new CommonMethods().GetConstantValue(getApplicationContext(),ServerKey,sharedpreferences)+EmployeeAPI+InsertEmployeeCall;
            //String url = Server+EmployeeAPI+InsertEmployeeCall;
            JSONObject postparams = new JSONObject();
            postparams.put("APIUserName", new CommonMethods().GetConstantValue(getApplicationContext(),Constants.APIUserNameKey,sharedpreferences));
            postparams.put("APIPassword", new CommonMethods().GetConstantValue(getApplicationContext(),Constants.APIPasswordKey,sharedpreferences));
            postparams.put("FirstName", firstName);
            postparams.put("LastName", lastName);
            postparams.put("Gender", gender);
            postparams.put("DateOfBirth", DOB);
            postparams.put("DateOfJoining", DOJ);
            postparams.put("Salary", Salary.toString());
            postparams.put("Designation", Designation);
            postparams.put("ActionBy", String.valueOf(ActionBy));
            postparams.put("ActionOn", ActionOn);
            JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, postparams,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String responseCode = response.getString("responseCode");
                                String responseMessage = response.getString("responseMessage");
                                if (responseCode.equalsIgnoreCase("00")) {
                                    JSONObject responseData = response.getJSONObject("responseData");
                                    int EmployeeID = responseData.getInt("employeeID");
                                    Toasty.success(getApplicationContext(), "Employee Created ID " + EmployeeID, Toasty.LENGTH_LONG).show();
                                    DeleteRecord();
                                    ClearData();
                                } else {
                                    Toasty.error(getApplicationContext(), responseMessage, Toasty.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                Toasty.error(getApplicationContext(), "Exception" + e.getMessage(), Toasty.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toasty.error(getApplicationContext(), error.getMessage(), Toasty.LENGTH_LONG).show();
                }
            }) {
            };
            queue.add(jsonRequest);
        } catch (Exception ex) {
            Toasty.error(this, ex.getMessage(), Toasty.LENGTH_LONG).show();
        }
    }

    private void ClearData(){
        LocalEmployeeID = 0;
        firstName = "";
        lastName = "";
        gender = "";
        DOB = "";
        DOJ = "";
        Salary = 0.00;
        Designation = "";
        ActionBy = 0;
        ActionOn = "";
    }

    private void DeleteRecord(){
        try{
            new EmployeeTableController(getApplicationContext()).delete(LocalEmployeeID);
            SyncData();
        }catch(Exception ex){
            Toasty.error(this, ex.getMessage(), Toasty.LENGTH_LONG).show();
        }
    }

    private void LoadTopEmployee(){
        try{
            Employee emp = new EmployeeTableController(getApplicationContext()).read();
            LocalEmployeeID = emp.EmployeeID;
            firstName = emp.FirstName;
            gender = emp.Gender;
            DOB = emp.DateOfBirth;
            DOJ = emp.DateOfJoining;
            Salary = Double.parseDouble(emp.Salary);
            lastName = emp.LastName;
            Designation = emp.Designation;
            ActionBy = Constants.Admin;
            Date now = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            ActionOn = df.format(now);
            SimpleDateFormat tf = new SimpleDateFormat("hh:mm:ss");
            String ActionTime = tf.format(now);
            ActionOn = ActionOn + "T" + ActionTime;
        }catch (Exception ex){
            Toasty.error(this, ex.getMessage(), Toasty.LENGTH_LONG).show();
        }
    }

    private int countRecords() {
        int recordCount = new EmployeeTableController(getApplicationContext()).count();
        return recordCount;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
