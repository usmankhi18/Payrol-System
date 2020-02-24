package villa.usman.payrolsystem;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import es.dmoral.toasty.Toasty;
import villa.usman.payrolsystem.common.Constants;
import villa.usman.payrolsystem.database.tables.EmployeeTableController;

import static villa.usman.payrolsystem.common.Constants.SyncAutomaticKey;

public class MainActivity extends Activity {
    Button btnCreateEmployee,btnSyncEmployee,btnConfiguration;
    TextView textViewRecordCount;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent().hasExtra("pushnotification")) {
            Intent intent = new Intent(this, SyncEmployeeActivity.class);
            startActivity(intent);
        }
        btnCreateEmployee = (Button)findViewById(R.id.btnCreateEmployee);
        btnSyncEmployee = (Button)findViewById(R.id.btnSyncEmployees);
        btnConfiguration = (Button)findViewById(R.id.btnConfiguration);
        textViewRecordCount = (TextView) findViewById(R.id.textViewRecordCount);
        sharedpreferences = getSharedPreferences(Constants.ConfigurationPREFERENCES, Context.MODE_PRIVATE);
        countRecords();
        checkPreferences();
        btnCreateEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),InsertEmployeeActivity.class);
                startActivity(intent);
            }
        });
        btnSyncEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SyncEmployeeActivity.class);
                startActivity(intent);
            }
        });
        btnConfiguration.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().hasExtra("pushnotification")) {
            Intent intent = new Intent(this, SyncEmployeeActivity.class);
            startActivity(intent);
        }
        sharedpreferences = getSharedPreferences(Constants.ConfigurationPREFERENCES, Context.MODE_PRIVATE);
        countRecords();
        checkPreferences();
    }

    private void checkPreferences(){
        try{
            if(sharedpreferences.contains(SyncAutomaticKey)){
                String SyncAutomaticKeyVal = (sharedpreferences.getString(SyncAutomaticKey, ""));
                if(SyncAutomaticKeyVal.equalsIgnoreCase("true")){
                    btnSyncEmployee.setVisibility(View.GONE);
                }
            }
        }catch(Exception ex){
            Toasty.error(getApplicationContext(),ex.getMessage(),Toasty.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void countRecords() {
        int recordCount = new EmployeeTableController(getApplicationContext()).count();
        textViewRecordCount.setText(recordCount + " records found.");
    }

}
