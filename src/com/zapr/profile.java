package com.zapr;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class profile extends Activity {
	String response;
	String auth;
	TextView name, snl;
	ListView l;
	Button show_users;
	String[] wall_posts;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		auth = getIntent().getStringExtra("auth");
 		
 		name = (TextView) findViewById(R.id.name);
 		snl = (TextView) findViewById(R.id.snl);
 		l = (ListView) findViewById(R.id.msgList);
 		try 
 		{

		     HttpClient client = new DefaultHttpClient();
		     String getURL = "http://10.0.2.2/zapr/profile.php";
		     HttpGet get = new HttpGet(getURL);
		     Log.d("auth", auth);
		     get.addHeader("Authorization","Basic "+auth );
		     HttpResponse responseGet = client.execute(get);
		     HttpEntity resEntityGet = responseGet.getEntity();
		     response = EntityUtils.toString(resEntityGet);
		     Log.d("response", response);
	     } 
 		catch (Exception e) 
 		{
		     e.printStackTrace();
		}
 		
 		Map<String , String> hm = new HashMap<String, String>();
		hm.put("M", "Male");
		hm.put("F", "Female");
		try
		{
			JSONArray jArray = new JSONArray(response);
			int len = jArray.length();
			JSONObject jObj = jArray.getJSONObject(0);
			name.setText("Hi!, "+jObj.getString("name"));
			snl.setText(hm.get(jObj.getString("gender"))+", "+jObj.getString("location"));
			
			if(len!=1)
			{
				wall_posts = new String[len-1];
				int count = 0;
				for(int i =1; i < len; i++)
				{
					JSONObject obj = jArray.getJSONObject(i);
					Log.d("from" , obj.get("from").toString() + "says : " + obj.get("msg").toString());
					wall_posts[count] = obj.get("from").toString() + " says : " + obj.get("msg").toString();
					count++;
				}
	
				
				
			}
			else
				Toast.makeText(this, "No Wall Posts Found", Toast.LENGTH_LONG).show();
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_view,wall_posts);
			
	 		l.setAdapter(adapter);
	}
	
	catch(Exception e)
	{
		e.printStackTrace();
	}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.show_users:
	        	Intent show_users = new Intent(profile.this, users.class);
				show_users.putExtra("auth",auth);
				startActivity(show_users);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	       
}


