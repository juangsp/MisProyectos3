package es.tessier.carlos.misproyectos;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class GoogleJSON extends Activity {

    protected String[] mResults;
    public static int NUMBER_OF_POSTS = 5;
    public static String TAG  = MainActivity.class.getSimpleName();
    public String URL_JSON ="https://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
    public String SEARCH ="";
    private boolean networkAvailable;
    private JSONObject mResultsData;

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlejson);

    }

    public void actionSearch(View v){
        EditText search = (EditText) findViewById(R.id.search);
        SEARCH=search.getText().toString();

        if (isNetworkAvailable()) {
            GetBlogPostsTask getBlogPostsTask = new GetBlogPostsTask();
            getBlogPostsTask.execute();
        }
        else {
            Toast.makeText(this, R.string.no_connection_message, Toast.LENGTH_LONG).show();
        }
    }

    public boolean isNetworkAvailable() {

        boolean isAvailable = false;

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isAvailable()) {
            isAvailable = true;
        }

        return isAvailable;
    }

    private class GetBlogPostsTask extends AsyncTask <Object, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(Object[] params) {
            int responseCode = -1;
            JSONObject jsonResponse = null;
            try {
                URL blogFeedUrl = new URL(URL_JSON+SEARCH);
                HttpURLConnection connection  = (HttpURLConnection) blogFeedUrl.openConnection();
                responseCode = connection.getResponseCode();

                if(responseCode==HttpURLConnection.HTTP_OK){
                    InputStream inputStream = connection.getInputStream();
 
                    BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    StringBuilder responseStrBuilder = new StringBuilder();

                    String inputStr;
                    while ((inputStr = streamReader.readLine()) != null)
                        responseStrBuilder.append(inputStr);

                    String responseData = responseStrBuilder.toString();

                    jsonResponse = new JSONObject(responseData);

                }
                else Log.i(TAG, "Conexión fallida: " + responseCode + URL_JSON+SEARCH);

            } catch (MalformedURLException e) {
                Log.e(TAG,"exception caught:",e);
            } catch (IOException e) {
                Log.e(TAG, "exception caught:", e);
            } catch (Exception e) {
                Log.e(TAG, "exception caught:", e);
            }

            return jsonResponse;
        }

        @Override
        protected void onPostExecute(JSONObject result){
            mResultsData = result;
            updateList();
        }

    }

    private void updateList() {
        if (mResultsData == null) {
            // TODO: Manejar errores
        } else {
             Log.d(TAG,mResultsData.toString());

            try {
                JSONObject jsonFeed = mResultsData.getJSONObject("responseData");
                JSONArray jsonAentry = null;

                jsonAentry = jsonFeed.getJSONArray("results");

                mResults = new String[jsonAentry.length()];

                for (int i = 0; i < jsonAentry.length(); i++) {
                    JSONObject jsonPost =  jsonAentry.getJSONObject(i);
                    //String title = Html.escapeHtml(jsonPost.getString("$t"));

                    String title =  Html.fromHtml(jsonPost.getString("title")).toString();


                    mResults[i] = title;

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                            android.R.layout.simple_expandable_list_item_1,mResults);

                    ListView listView = (ListView) findViewById(R.id.listView);

                    listView.setAdapter(adapter);

                }
            } catch (JSONException e) {
                Log.e(TAG, "exception caught:", e);
            }
        }
    }
}
