package com.example.CS654;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowBirthdays extends ActionBarActivity {
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_birthdays);
 
        // call AsynTask to perform network operation on separate thread
        //new HttpAsyncTask().execute("http://192.168.2.3:5555");
        new HttpAsyncTask().execute("http://172.27.22.32/android");
	}
	public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {
 
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
 
            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
 
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
 
            // convert input stream to string
            if(inputStream != null)
            {    
            	result = convertInputStreamToString(inputStream);
            	//result = inputStream.toString();
            }
            else
                result = "Did not work!";
//        	URL url1 = new URL("http://192.168.2.3:5555");
//        	HttpURLConnection con = (HttpURLConnection) url1.openConnection();
//        	readStream(con.getInputStream());
        	
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
 
        return result;
    }
	public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) 
                return true;
            else
                return false;   
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
        @Override
        protected String doInBackground(String... urls) {
 
            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
        	
        	
            try
            {
            	//setContentView(R.layout.activity_main);
              
              
              //String jsonInput = "[\"one\",\"two\",\"three\",\"four\",\"five\",\"six\",\"seven\",\"eight\",\"nine\",\"ten\"]";
              JSONArray jsonArray = new JSONArray(result);
              int length = jsonArray.length();
              List<String> listContents = new ArrayList<String>(length);
              for (int i = 0; i < length; i++)
              {
            	JSONObject jsonObject = jsonArray.getJSONObject(i);
            	listContents.add(jsonObject.getString("name")+"\n"+jsonObject.getString("birthday"));
              }

              ListView myListView = (ListView) findViewById(R.id.listView);
              myListView.setAdapter(new ArrayAdapter<String>(ShowBirthdays.this, android.R.layout.simple_list_item_1, listContents));
            }
            catch (Exception e)
            {
              // this is just an example
            }
            //ListAdapter adapter = new SimpleAdapter(MainActivity.this, android.R.layout.simple_list_item_1,listContents);
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            //etResponse.setText(result);
       }
    }
	
	

/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_birthdays, menu);
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
