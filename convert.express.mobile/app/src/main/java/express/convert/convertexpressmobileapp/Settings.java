package express.convert.convertexpressmobileapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class Settings extends AppCompatActivity {

    public static final String TAG = "Convert.Express.Mobile";
    private Intent i;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        //Henter verdi
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("convert.express", android.content.Context.MODE_PRIVATE);
        boolean value = preferences.getBoolean("SaveHistory", false);

        CheckBox checkbox = (CheckBox) findViewById(R.id.keepHistoryCheckBox);
        checkbox.setChecked(value);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences preferences = getApplicationContext().getSharedPreferences("convert.express", android.content.Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("SaveHistory", isChecked);
                editor.commit();
                Log.i(TAG,"onCheckedChanged");
                Log.i(TAG,"SaveHistory = " + isChecked);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_About:
                i = new Intent(this, About.class);
                startActivity(i);
                return true;
            case R.id.history:
                i = new Intent(this, History.class);
                startActivity(i);
                return true;
            default:
                return true;
        }
    }

    public void deleteHistory(View view){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == DialogInterface.BUTTON_POSITIVE)
                {
                    HistoryDbHelper dbHlper = new HistoryDbHelper(getApplicationContext());
                    SQLiteDatabase db = dbHlper.getWritableDatabase();
                    db.delete(HistoryDbContext.HistoryEntry.TABLE_NAME,null,null);
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void closeSettings(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
