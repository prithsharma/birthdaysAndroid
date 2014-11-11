package com.example.CS654;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class AddBirthday extends Activity implements OnClickListener{
	private int day;
	private int month;
	private int year;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_birthday);
		
		ImageButton ib;
		Calendar cal;
		
		EditText et;
		
		ib = (ImageButton) findViewById(R.id.imageButton1);
		cal = Calendar.getInstance();
		day = cal.get(Calendar.DAY_OF_MONTH);
		month = cal.get(Calendar.MONTH);
		year = cal.get(Calendar.YEAR);
		et = (EditText) findViewById(R.id.bday);
		ib.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		showDialog(0);
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		return new DatePickerDialog(this, datePickerListener, year, month, day);
	}
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) 
		{
			EditText et=(EditText) findViewById(R.id.bday);
			et.setText(selectedDay + "-" + (selectedMonth + 1) + "-"
					+ selectedYear);
		}
	};
	/** Called when the user clicks the Add Birthday button */
    public void addBirthday(View view) {
    	EditText fieldName = (EditText) findViewById(R.id.name);
    	EditText fieldBday = (EditText) findViewById(R.id.bday);
    	
    	String name = fieldName.getText().toString().trim();
    	String bday = fieldBday.getText().toString().trim();
    	
    	if(name.matches("") || bday.matches(""))
    		Toast.makeText(getBaseContext(), "Please enter the fields!", Toast.LENGTH_LONG).show();
    	else
    	{
    		new HttpAsyncTask(name,bday).execute("http://172.27.22.32/android");
    	}
    }
    public static String POST(String url, String name, String bday){
        InputStream inputStream = null;
        String result = "";
        try {
        	
        	// Create a new HttpClient and Post Header
    		HttpClient httpclient = new DefaultHttpClient();
 
    		// Creating HTTP Post
    		HttpPost httpPost = new HttpPost(url);
    		
    		JSONObject json = new JSONObject();
    		// JSON data:
            json.put("name", name);
            json.put("birthday", bday);
 
            JSONArray postjson=new JSONArray();
            postjson.put(json);
 
            // Post the data:
            httpPost.setHeader("json",json.toString());
            httpPost.getParams().setParameter("jsonpost",postjson);
 
            
            try {
            	// Execute HTTP Post Request
                System.out.print(json);
                HttpResponse response = httpclient.execute(httpPost);
                // receive response as inputStream
                inputStream = response.getEntity().getContent();
     
                // convert input stream to string
                if(inputStream != null)
                {    
                	result = convertInputStreamToString(inputStream);
                	//result = inputStream.toString();
                }
                else
                    result = "Did not work!";
                // writing response to log
                Log.d("Http Response:", response.toString());
            } catch (ClientProtocolException e) {
                // writing exception to log
                e.printStackTrace();
            } catch (IOException e) {
                // writing exception to log
                e.printStackTrace();
            }
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
 
        return result;
    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
 
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
    	private String name, bday;
        public HttpAsyncTask(String name,String bday){
		    this.name = name;
		    this.bday = bday;
        }
        @Override
        protected String doInBackground(String... urls){
            return POST(urls[0], name, bday);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
        	EditText fieldName = (EditText) findViewById(R.id.name);
        	EditText fieldBday = (EditText) findViewById(R.id.bday);
        	Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        	fieldName.setText("");
        	fieldBday.setText("");
        	setContentView(R.layout.activity_add_birthday);
       }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_birthday, menu);
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
	}
}
