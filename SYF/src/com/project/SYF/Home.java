package com.project.SYF;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by annesohier on 26/04/2015.
 */

public class Home extends Activity implements View.OnClickListener{

    private Button scannerBtn;
    private Button favorisBtn;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home);

        scannerBtn = (Button)findViewById(R.id.scanlauncher_button);
        favorisBtn = (Button)findViewById(R.id.favoris_button);

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
}
