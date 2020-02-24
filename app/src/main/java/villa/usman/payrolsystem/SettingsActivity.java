package villa.usman.payrolsystem;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;
import villa.usman.payrolsystem.common.Constants;

import static villa.usman.payrolsystem.common.Constants.APIPasswordKey;
import static villa.usman.payrolsystem.common.Constants.APIUserNameKey;
import static villa.usman.payrolsystem.common.Constants.ServerKey;
import static villa.usman.payrolsystem.common.Constants.SyncAutomaticKey;
import static villa.usman.payrolsystem.common.Constants.SyncTimeKey;

public class SettingsActivity extends Activity {
    TextView syncTime;
    EditText url,username,password;
    CheckBox isAutomatic;
    Button btnSaveSettings;
    TimePickerDialog picker;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        syncTime = (TextView)findViewById(R.id.et_SyncTime);
        btnSaveSettings = (Button)findViewById(R.id.btnSave);
        url = (EditText)findViewById(R.id.et_APIServer);
        username = (EditText)findViewById(R.id.et_APIUserName);
        password = (EditText)findViewById(R.id.et_Password);
        isAutomatic = (CheckBox)findViewById(R.id.chk_SyncAutomatic);
        sharedpreferences = getSharedPreferences(Constants.ConfigurationPREFERENCES, Context.MODE_PRIVATE);
        syncTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minute = cldr.get(Calendar.MINUTE);
                // date picker dialog
                picker = new TimePickerDialog(SettingsActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                syncTime.setText( selectedHour + ":" + selectedMinute);
                            }
                        }, hour, minute, true);
                picker.setTitle("Select Time");
                picker.show();
            }
        });
        btnSaveSettings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SaveSettings();
            }
        });
    }

    private void SaveSettings(){
        try{
            if(url.getText().toString().isEmpty()){
                Toasty.warning(getApplicationContext(),"Enter API URL",Toasty.LENGTH_LONG).show();
            }else if(username.getText().toString().isEmpty()){
                Toasty.warning(getApplicationContext(),"Enter API UserName",Toasty.LENGTH_LONG).show();
            }else if(password.getText().toString().isEmpty()){
                Toasty.warning(getApplicationContext(),"Enter API Password",Toasty.LENGTH_LONG).show();
            }else if(syncTime.getText().toString().isEmpty() && isAutomatic.isChecked()){
                Toasty.warning(getApplicationContext(),"Enter Automatic Time",Toasty.LENGTH_LONG).show();
            }else {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(ServerKey, url.getText().toString());
                editor.putString(APIUserNameKey, username.getText().toString());
                editor.putString(APIPasswordKey, password.getText().toString());
                editor.putString(SyncAutomaticKey, String.valueOf(isAutomatic.isChecked()));
                editor.putString(SyncTimeKey, syncTime.getText().toString());
                editor.commit();
                Toasty.success(getApplicationContext(), "Save Successfully", Toasty.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        }catch (Exception ex){
            Toasty.error(getApplicationContext(),ex.getMessage(),Toasty.LENGTH_LONG).show();
        }
    }
}
