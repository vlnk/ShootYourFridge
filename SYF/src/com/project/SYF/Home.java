package com.project.SYF;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by annesohier on 26/04/2015.
 */

@SuppressWarnings("DefaultFileTemplate")
public class Home extends Activity implements View.OnClickListener{

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home);

        Button scannerBtn = (Button) findViewById(R.id.scanlauncher_button);
        Button favorisBtn = (Button) findViewById(R.id.favoris_button);

        scannerBtn.setOnClickListener(this);
        favorisBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.scanlauncher_button){
            Intent validateIntent = new Intent(this, Main.class);
            startActivity(validateIntent);
        }
        if(v.getId()==R.id.favoris_button){
            Intent validateIntent = new Intent(this, Favoris.class);
            startActivity(validateIntent);
        }
    }

    @Override
     public boolean onCreateOptionsMenu(Menu topMenu) {
        //inflate the menu to use in the action bar
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,topMenu);
        return super.onCreateOptionsMenu(topMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_home:
            case R.id.action_scanner: Intent validateIntent = new Intent(this, Main.class);
                                    startActivity(validateIntent);
            default:

            return super.onOptionsItemSelected(item);
        }
    }
}
