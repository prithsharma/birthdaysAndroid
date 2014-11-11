package com.example.CS654;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    TextView tvIsConnected;
    
    //ArrayAdapter<String> simpleAdpt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // get reference to the views
        //etResponse = (EditText) findViewById(R.id.etResponse);
        tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);
 
        // check if you are connected or not
        if(isConnected()){
            tvIsConnected.setBackgroundColor(0xFF00CC00);
            tvIsConnected.setText("You are connected");
        }
        else{
            tvIsConnected.setText("You are NOT connected");
        }
 
        // call AsynTask to perform network operation on separate thread
        //new HttpAsyncTask().execute("http://192.168.2.3:5555");
        //new HttpAsyncTask().execute("http://192.168.2.3/android");
        

    }
    /** Called when the user clicks the Show Birthdays button */
    public void showBirthdays(View view) {
    	if(!isConnected())
    		Toast.makeText(getBaseContext(), "Check network connection and restart app!", Toast.LENGTH_LONG).show();
    	else
    	{
    		Intent intent = new Intent(this, ShowBirthdays.class);
    		startActivity(intent);
    	}
    }
    
    /** Called when the user clicks the Add Birthday button */
    public void addBirthday(View view) {
    	if(!isConnected())
    		Toast.makeText(getBaseContext(), "Check network connection!", Toast.LENGTH_LONG).show();
    	else
    	{
	    	Intent intent = new Intent(this, AddBirthday.class);
	    	startActivity(intent);
    	}
    }
 
    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) 
                return true;
            else
                return false;   
    }
    

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
