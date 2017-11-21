package express.convert.convertexpressmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class About extends AppCompatActivity {

    private Intent i;

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                i = new Intent(this, Settings.class);
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

    public void closeAbout(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
