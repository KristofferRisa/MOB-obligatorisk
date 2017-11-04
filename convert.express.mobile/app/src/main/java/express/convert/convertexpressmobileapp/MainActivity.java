package express.convert.convertexpressmobileapp;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Convert.Express.Mobile";

    EditText input;
    ListView results;
    ArrayAdapter<String> adapter;
    ArrayList<String> listItems;

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStart");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        new Currencies().execute("https://api.fixer.io/latest?base=NOK&symbols=USD");

        goButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                listItems.clear();
                Log.i(TAG, input.getText().toString());
                listItems.add(input.getText().toString());
                adapter.notifyDataSetChanged();
            }
        });



    }

    class Currencies extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String url_select = "http://api.fixer.io/latest"; //?base={searchParameters.BaseIso}&symbols={searchParameters.ToIso}";
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
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

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
            if (pd.isShowing()){
                pd.dismiss();
            }
        }
    }
}
