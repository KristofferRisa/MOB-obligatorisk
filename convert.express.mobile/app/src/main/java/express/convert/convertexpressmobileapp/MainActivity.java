package express.convert.convertexpressmobileapp;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.google.gson.Gson;
import android.content.Intent;
import org.json.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Convert.Express.Mobile";

    EditText input;
    ListView results;
    ArrayAdapter<String> adapter;
    ArrayList<String> listItems;
    private JSONObject myObject;
    private Intent i;
    private boolean saveHistory;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu_settings:
                input.setText("");
                i = new Intent(this, Settings.class);
                startActivity(i);
                return true;
            case R.id.history:
                i = new Intent(this, History.class);
                startActivity(i);
                return true;
            case R.id.menu_About:
                i = new Intent(this, About.class);
                startActivity(i);
                return true;
            default:
                return true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStart");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Log.i(TAG,"onCreate");
        adapter = null;

        //Laster inn settings
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("convert.express", android.content.Context.MODE_PRIVATE);
        saveHistory = preferences.getBoolean("SaveHistory", false);
        Log.i(TAG,"SaveHistory = " + saveHistory);

        Button goButton = (Button)findViewById(R.id.goButton);

        input = (EditText)findViewById(R.id.input);
        listItems = new ArrayList<String>();
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
                String query = input.getText().toString();
                Log.i(TAG, query);
                if(saveHistory){
                    HistoryDbHelper dbHelper = new HistoryDbHelper(getApplicationContext());
                    // Gets the data repository in write mode
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(HistoryDbContext.HistoryEntry.COLUMN_NAME_QUERY, query);
                    // Insert the new row, returning the primary key value of the new row
                    long newRowId = db.insert(HistoryDbContext.HistoryEntry.TABLE_NAME, null, values);
                    Log.i(TAG, "Saved query in SQLite");
                }

                new loadData().execute("https://convert.express/api/converter?q=" + input.getText().toString());
                findViewById(R.id.loading).setVisibility(View.VISIBLE);
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
                Gson gson = new Gson();
                ConvertExpressRespons[] response = gson.fromJson(buffer.toString(), ConvertExpressRespons[].class);

                for (int i =0;i<response.length;i++){
                    listItems.add(response[i].header + ": " + response[i].description);
                }
                return buffer.toString();

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
            // Skjuler spinner
            findViewById(R.id.loading).setVisibility(View.GONE);
            // Oppdatere liste
            adapter.notifyDataSetChanged();

        }
    }

}
