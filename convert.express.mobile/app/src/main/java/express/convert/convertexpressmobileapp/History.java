package express.convert.convertexpressmobileapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {

    public static final String TAG = "Convert.Express.Mobile";
    private Intent i;

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                i = new Intent(this, Settings.class);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        HistoryDbHelper dbHelper = new HistoryDbHelper(getApplicationContext());
        //Leser fra database
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                HistoryDbContext.HistoryEntry._ID,
                HistoryDbContext.HistoryEntry.COLUMN_NAME_QUERY
        };

        // Cursor som henter data fra databse
        Cursor cursor = db.query(
                HistoryDbContext.HistoryEntry.TABLE_NAME,
                projection,null,null,
                null,null,null
        );

        List queries = new ArrayList<>();
        while(cursor.moveToNext()) {
            String query = cursor.getString(
                    cursor.getColumnIndexOrThrow(HistoryDbContext.HistoryEntry.COLUMN_NAME_QUERY));
            queries.add(query);
            Log.i(TAG, "Added " + query + " to list from db.");
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1
                , queries);
        ListView results = (ListView) findViewById(R.id.historyResults);

        results.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void closeHistory(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
