package com.zapr;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class users extends Activity {
	String response;
	String auth;
	ListView l;
	String[] user_list;
	String[] id_list;
	String new_wall_post;
	int send_to;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.users);
		l = (ListView) findViewById(R.id.usersList);
		auth = getIntent().getStringExtra("auth");
		
		try 
 		{

		     HttpClient client = new DefaultHttpClient();
		     String getURL = "http://10.0.2.2/zapr/user_list.php";
		     HttpGet get = new HttpGet(getURL);
		     get.addHeader("Authorization","Basic "+auth );
		     HttpResponse responseGet = client.execute(get);
		     HttpEntity resEntityGet = responseGet.getEntity();
		     response = EntityUtils.toString(resEntityGet);
	     } 
 		catch (Exception e) 
 		{
		     e.printStackTrace();
		}

		try
		{
			JSONArray jArray = new JSONArray(response);
			int len = jArray.length();
			
			if(len!=0)
			{
				user_list = new String[len];
				id_list = new String[len];
				for(int i =0; i < len; i++)
				{
					JSONObject obj = jArray.getJSONObject(i);
					user_list[i] = obj.get("name").toString();
					id_list[i] = obj.getString("id").toString();
				}
			}
			else
				Toast.makeText(this, "No Users Found", Toast.LENGTH_LONG).show();
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_view,user_list);
			l.setAdapter(adapter);
			l.setOnItemClickListener(new OnItemClickListener(){ 
				@Override 
				public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3) 
				{ 
					send_to = position;
					final EditText input = new EditText(users.this);
					new AlertDialog.Builder(users.this)
				    .setTitle("New Wall Post")
				    .setMessage("Enter Your Message")
				    .setView(input)
				    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int whichButton) {
				            new_wall_post = input.getText().toString(); 
				            WallPost new_post = new WallPost();
				            new_post.execute("http://10.0.2.2/zapr/add_post.php");
				        }
				    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int whichButton) {
				            // Do nothing.
				        }
				    }).show();
					}
				
				 class WallPost extends AsyncTask<String, Void, String> {
			    	
			    	private ProgressDialog Dialog = new ProgressDialog(users.this);
			    	
			    	@Override
			        protected void onPreExecute()
			        {
			        	
			        		Dialog.setMessage("Sending your wall post..");
			        		Dialog.show();
			            
			        }
			        @Override
			        protected String doInBackground(String... urls) {
			        	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
						nameValuePairs.add(new BasicNameValuePair("uname", id_list[send_to]));
						nameValuePairs.add(new BasicNameValuePair("post", new_wall_post));
						
				    
						for (String url : urls) {
					    try
						{
							HttpClient httpclient = new DefaultHttpClient();
							HttpPost httppost = new HttpPost(url);
							httppost.addHeader("Authorization","Basic "+auth );
							httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
							HttpResponse response = httpclient.execute(httppost);
							HttpEntity entity = response.getEntity();
							Log.d("tag",EntityUtils.toString(entity));
						}
						catch(Exception e)
						{
							Toast.makeText(getBaseContext(),"Error in http connection "+e.toString(), Toast.LENGTH_LONG).show();
						}
						finally
						{
						
						}
			          
			          }
						return response;
			        }

			        @Override
			        protected void onPostExecute(String result) 
			        {
			                	Dialog.dismiss();            	
			                }
			            }
				
				}
			
					); 
			
		
	}
	
	catch(Exception e)
	{
		e.printStackTrace();
	}
		
	}
}


