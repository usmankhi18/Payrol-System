package villa.usman.payrolsystem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
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
import villa.usman.payrolsystem.entities.Employee;
import villa.usman.payrolsystem.database.tables.EmployeeTableController;

import static villa.usman.payrolsystem.common.Constants.EmployeeAPI;
import static villa.usman.payrolsystem.common.Constants.InsertEmployeeCall;
import static villa.usman.payrolsystem.common.Constants.ServerKey;

public class InsertEmployeeActivity extends Activity {

    Button btnRegister;
    EditText et_firstname, et_lastname, et_Salary, et_Designation;
    TextView textViewRecordCount, tv_doj, tv_dob;
    Spinner sp_gender;
    ProgressBar progressBar;
    String firstName;
    String lastName;
    String gender;
    String DOB;
    String DOJ;
    Double Salary;
    String Designation;
    int ActionBy;
    String ActionOn;
    DatePickerDialog picker;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_employee);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        tv_dob = (TextView) findViewById(R.id.et_dob);
        tv_doj = (TextView) findViewById(R.id.et_doj);
        et_firstname = (EditText) findViewById(R.id.et_firstName);
        et_lastname = (EditText) findViewById(R.id.et_lastName);
        et_Salary = (EditText) findViewById(R.id.et_Salary);
        et_Designation = (EditText) findViewById(R.id.et_designation);
        sp_gender = (Spinner) findViewById(R.id.spinnerGender);
        textViewRecordCount = (TextView) findViewById(R.id.textViewRecordCount);
        sharedpreferences = getSharedPreferences(Constants.ConfigurationPREFERENCES, Context.MODE_PRIVATE);
        countRecords();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterEmployee();
            }
        });
        tv_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(InsertEmployeeActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                int month = monthOfYear + 1;
                                String monthFormat = String.valueOf(month);
                                monthFormat = new CommonMethods().padLeftZeros(monthFormat, 2);
                                tv_dob.setText(year + "-" + monthFormat + "-" + new CommonMethods().padLeftZeros(String.valueOf(dayOfMonth), 2));
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        tv_doj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(InsertEmployeeActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                int month = monthOfYear + 1;
                                String monthFormat = String.valueOf(month);
                                monthFormat = new CommonMethods().padLeftZeros(monthFormat, 2);
                                tv_doj.setText(year + "-" + monthFormat + "-" + new CommonMethods().padLeftZeros(String.valueOf(dayOfMonth), 2));
                            }
                        }, year, month, day);
                picker.show();
            }
        });
    }

    public void RegisterEmployee() {
        firstName = et_firstname.getText().toString();
        gender = sp_gender.getSelectedItem().toString();
        DOB = tv_dob.getText().toString();
        Date dtDOB = new CommonMethods().DateConvertFromString(DOB, getApplicationContext());
        DOJ = tv_doj.getText().toString();
        Date dtDOJ = new CommonMethods().DateConvertFromString(DOJ, getApplicationContext());
        if (!et_Salary.getText().toString().isEmpty()) {
            Salary = Double.parseDouble(et_Salary.getText().toString());
        } else {
            Salary = 0.0;
        }
        lastName = et_lastname.getText().toString();
        Designation = et_Designation.getText().toString();
        ActionBy = Constants.Admin;
        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        ActionOn = df.format(now);
        SimpleDateFormat tf = new SimpleDateFormat("hh:mm:ss");
        String ActionTime = tf.format(now);
        ActionOn = ActionOn + "T" + ActionTime;
        try {
            if (firstName.isEmpty()) {
                Toasty.warning(this, "Enter First Name", Toasty.LENGTH_LONG).show();
            } else if (lastName.isEmpty()) {
                Toasty.warning(this, "Enter Last Name", Toasty.LENGTH_LONG).show();
            } else if (gender.isEmpty()) {
                Toasty.warning(this, "Enter Gender", Toasty.LENGTH_LONG).show();
            } else if (DOB.isEmpty()) {
                Toasty.warning(this, "Enter Date of Birth", Toasty.LENGTH_LONG).show();
            } else if (DOJ.isEmpty()) {
                Toasty.warning(this, "Enter Date of Joining", Toasty.LENGTH_LONG).show();
            } else if (dtDOJ.before(dtDOB)) {
                Toasty.warning(this, "Date of Joining should be greater than Date of Birth", Toasty.LENGTH_LONG).show();
            } else if (new CommonMethods().getDiffYears(dtDOB, now) < 18) {
                Toasty.warning(this, "Employee should be greater than 18 Years.", Toasty.LENGTH_LONG).show();
            } else if (!ActionOn.isEmpty() && ActionBy == Constants.Admin) {
                if (new CommonMethods().isOnline(getApplicationContext())) {
                    CallAPI();
                } else {
                    Toasty.error(getApplicationContext(), "Internet Not Connected", Toasty.LENGTH_LONG).show();
                    InsertLocally();
                }
            }
        } catch (Exception ex) {
            Toasty.error(this, ex.getMessage(), Toasty.LENGTH_LONG).show();
        }
    }

    public void CallAPI() {
        try {
            progressBar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(this);
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
                                    progressBar.setVisibility(View.INVISIBLE);
                                    JSONObject responseData = response.getJSONObject("responseData");
                                    int EmployeeID = responseData.getInt("employeeID");
                                    Toasty.success(getApplicationContext(), "Employee Created ID " + EmployeeID, Toasty.LENGTH_LONG).show();
                                    ClearData();
                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toasty.error(getApplicationContext(), responseMessage, Toasty.LENGTH_LONG).show();
                                    InsertLocally();
                                }
                            } catch (JSONException e) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toasty.error(getApplicationContext(), "Exception" + e.getMessage(), Toasty.LENGTH_LONG).show();
                                InsertLocally();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toasty.error(getApplicationContext(), error.getMessage(), Toasty.LENGTH_LONG).show();
                    InsertLocally();
                }
            }) {
            };
            queue.add(jsonRequest);
        } catch (Exception ex) {
            Toasty.error(this, ex.getMessage(), Toasty.LENGTH_LONG).show();
        }
    }

    private void ClearData(){
        et_firstname.setText("");
        et_Designation.setText("");
        tv_doj.setText("Select Date");
        tv_dob.setText("Select Date");
        et_lastname.setText("");
        et_Salary.setText("");
    }

    public void countRecords() {
        int recordCount = new EmployeeTableController(getApplicationContext()).count();
        textViewRecordCount.setText(recordCount + " records found.");
    }

    public void InsertLocally() {
        try {
            Employee employee = new Employee();
            employee.FirstName = firstName;
            employee.LastName = lastName;
            employee.Gender = gender;
            employee.DateOfBirth = DOB;
            employee.DateOfJoining = DOJ;
            employee.Salary = Salary.toString();
            employee.Designation = Designation;
            boolean createSuccessful = new EmployeeTableController(getApplicationContext()).create(employee);
            if (createSuccessful) {
                Toasty.success(getApplicationContext(), "Employee information was saved.", Toasty.LENGTH_LONG).show();
                et_firstname.setText("");
                et_Designation.setText("");
                tv_doj.setText("Select Date");
                tv_dob.setText("Select Date");
                et_lastname.setText("");
                et_Salary.setText("");
                countRecords();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            } else {
                Toasty.error(getApplicationContext(), "Unable to save employee information.", Toasty.LENGTH_LONG).show();
                et_firstname.setText("");
                et_Designation.setText("");
                tv_doj.setText("Select Date");
                tv_dob.setText("Select Date");
                et_lastname.setText("");
                et_Salary.setText("");
                countRecords();
            }
        } catch (Exception ex) {
            Toasty.error(this, ex.getMessage(), Toasty.LENGTH_LONG).show();
        }
    }
}
