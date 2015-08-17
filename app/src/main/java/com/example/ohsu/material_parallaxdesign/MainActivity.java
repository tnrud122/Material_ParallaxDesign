package com.example.ohsu.material_parallaxdesign;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_list = (Button) findViewById(R.id.btn_list);
        button_list.setOnClickListener(this);

        Button button_recyler = (Button) findViewById(R.id.btn_recyler);
        button_recyler.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_list:
                {
                    addAactivity(1);
                }
            break;
            case R.id.btn_recyler:
                {
                    addAactivity(2);
                }
            break;
        }
    }

    public void addAactivity(int type) {
        if(type == 1) {
            Intent intent = new Intent(this, ListViewExample.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, RecyleViewExample.class);
            startActivity(intent);
        }
    }

}
