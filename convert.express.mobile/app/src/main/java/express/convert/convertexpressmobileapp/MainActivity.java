package express.convert.convertexpressmobileapp;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Convert.Express.Mobile";

    EditText input;
    ListView results;
    ArrayAdapter<String> adapter;
    ArrayList<String> listItems;
    private JSONObject myObject;

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStart");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Log.i(TAG,"onCreate");
        adapter = null;

        Button goButton = (Button)findViewById(R.id.goButton);

        input = (EditText)findViewById(R.id.input);
        listItems = new ArrayList<String>();
        //listItems.add("TEST");
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1
                , listItems);

        results = (ListView) findViewById(R.id.results);

        results.setAdapter(adapter);


        goButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                listItems.clear();
                Log.i(TAG, input.getText().toString());
                new loadData().execute("https://convert.express/api/converter?q=" + input.getText().toString());
            }
        });
    }


    class loadData extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                Log.i(TAG,"URL = " + params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                }

                Log.i(TAG,buffer.toString());

                Gson gson = new Gson();
                ConvertExpressRespons[] response = gson.fromJson(buffer.toString(), ConvertExpressRespons[].class);

                //Log.i(TAG,response[0].header);
                //Log.i(TAG,response[0].description);

                for (int i =0;i<response.length;i++){
                    listItems.add(response[i].header + ": " + response[i].description);
                    //listItems.add(response[0].description);
                }
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            if (pd.isShowing()){
//                pd.dismiss();
//            }

            adapter.notifyDataSetChanged();

        }
    }

}
