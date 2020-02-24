package villa.usman.payrolsystem.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;

import java.util.Calendar;
import static java.util.Calendar.*;
import static villa.usman.payrolsystem.common.Constants.APIPassword;
import static villa.usman.payrolsystem.common.Constants.APIPasswordKey;
import static villa.usman.payrolsystem.common.Constants.APIUserName;
import static villa.usman.payrolsystem.common.Constants.APIUserNameKey;
import static villa.usman.payrolsystem.common.Constants.Server;
import static villa.usman.payrolsystem.common.Constants.ServerKey;
import static villa.usman.payrolsystem.common.Constants.SyncAutomaticKey;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class CommonMethods {
    ConnectivityManager connectivityManager;
    boolean connected = false;

    public boolean isOnline(Context context) {
        try {
            connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;


        } catch (Exception e) {
            Toasty.error(context,"CheckConnectivity Exception: " + e.getMessage(),Toasty.LENGTH_LONG).show();
            //System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }

    public Date DateConvertFromString(String strdate, Context context){
        Date date = Calendar.getInstance().getTime();
        try{
            date = new SimpleDateFormat("yyyy-MM-DD").parse(strdate);
        }catch (Exception ex){
            Toasty.error(context,ex.getMessage(),Toasty.LENGTH_LONG).show();
        }
        return date;
    }

    public static String GetConstantValue(Context context,String Key,SharedPreferences sharedpreferences){
        String constant= "";
        try{
            constant = LoadPreferences(context,Key,sharedpreferences);
        }catch(Exception ex){
            Toasty.error(context,ex.getMessage(),Toasty.LENGTH_LONG).show();
        }
        return constant;
    }

    private static String LoadPreferences(Context context,String Key,SharedPreferences sharedpreferences ){
        String value = "";
        try{
            if(sharedpreferences.contains(Key)){
                value = (sharedpreferences.getString(Key, ""));
            }else{
                switch(Key) {
                    case ServerKey:
                        value = Server;
                        break;
                    case APIUserNameKey:
                        value = APIUserName;
                        break;
                    case APIPasswordKey:
                        value = APIPassword;
                        break;
                    default:
                        // code block
                }
            }
        }catch(Exception ex){
            Toasty.error(context,ex.getMessage(),Toasty.LENGTH_LONG).show();
        }
        return value;
    }

    public static int getDiffYears(Date from, Date to) {
        Calendar start = getCalendar(from);
        Calendar end = getCalendar(to);
        int diff = end.get(YEAR) - start.get(YEAR);
        if (start.get(MONTH) > end.get(MONTH) ||
                (start.get(MONTH) == end.get(MONTH) && start.get(DATE) > end.get(DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

    public String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }
}
