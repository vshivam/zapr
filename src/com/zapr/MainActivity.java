package com.zapr;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	String response;
	String user_name;
	EditText uname;
	EditText pwd;
	Button login;
	String auth;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		response = "0";
		setContentView(R.layout.activity_main);
		
		uname = (EditText) findViewById(R.id.uname);
		pwd = (EditText) findViewById(R.id.pwd);
		login = (Button) findViewById(R.id.login);
		
		login.setOnClickListener(new View.OnClickListener() {

	         @Override
	         public void onClick(View v) {
	             // TODO Auto-generated method stub
	        	 ProfileData newtask = new ProfileData(getApplicationContext());
	     		newtask.execute("http://10.0.2.2/zapr/auth.php");
	         }
	     });
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	class ProfileData extends AsyncTask<String, Void, String> {
		
		Context mContext;
		ProfileData(Context context)
		{
			super();
	        this.mContext = context;
		}
		 ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
		
		@Override
	    protected void onPreExecute()
	    {
	    	
	    		Dialog.setMessage("Authenticating..");
	    		Dialog.show();
	        
	    }
	    @Override
	    protected String doInBackground(String... urls) 
	    {
			
	    
			for (String url : urls) {
				try {
					user_name = uname.getText().toString();
				     HttpClient client = new DefaultHttpClient();
				     String getURL = url;
				     HttpGet get = new HttpGet(getURL);
				     auth = Base64.encodeToString(((user_name+":"+pwd.getText()).getBytes()),Base64.NO_WRAP);
				     Log.d("auth", auth);
				     get.addHeader("Authorization","Basic "+auth );
				     if(get.containsHeader("Authorization"))
				     Log.d("get","yes");
				     HttpResponse responseGet = client.execute(get);
				     HttpEntity resEntityGet = responseGet.getEntity();
				     response = EntityUtils.toString(resEntityGet);
				     Log.d("response",response);
				     } catch (Exception e) {
				     e.printStackTrace();
				     }
				if(response.compareTo("1")==0)
				{
					Log.d("auth","Autheticated!");
				}
				else
				{
					Log.d("auth","Authetication failed!");
				}
	      }
			return response;
	    }

	    @Override
	    protected void onPostExecute(String result) 
	    {
	            	Dialog.dismiss();
	            	if(response.compareTo("1")==0)
					{
						Log.d("auth","Autheticated!");
						Intent show_profile = new Intent(mContext, profile.class);
						show_profile.putExtra("auth",auth);
						startActivity(show_profile);
					}
					else
					{
						Toast.makeText(mContext, "Authentication Failed!", Toast.LENGTH_LONG).show();
						Log.d("auth","Authetication failed! Please try again. ");
					}
	            }
	        }
	       

}


