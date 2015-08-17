package com.example.ohsu.material_parallaxdesign;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.example.ohsu.material_parallaxdesign.parallax.adapter.RecyleViewAdapter;
import com.example.ohsu.material_parallaxdesign.parallax.data.ViewData;
import com.example.ohsu.material_parallaxdesign.parallax.layout.ParallaxLayout;

import java.util.ArrayList;

public class RecyleViewExample extends AppCompatActivity {
    private ArrayList<ViewData> temList = new ArrayList<ViewData>();
    private ParallaxLayout mParallaxLayout;
    private RecyclerView m_list = null;
    private RecyleViewAdapter m_adapter = null;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyle_view_example);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mParallaxLayout = (ParallaxLayout)findViewById(R.id.contentLayout);
        mParallaxLayout.setToolbar(toolbar);
        m_list = (RecyclerView)findViewById(R.id.list_content);
        m_list.setLayoutManager(new LinearLayoutManager(this));
        m_list.setOverScrollMode(View.OVER_SCROLL_NEVER);
        m_list.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                // TODO Auto-generated method stub
                if (mParallaxLayout != null)
                    mParallaxLayout.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // TODO Auto-generated method stub
                if (mParallaxLayout != null)
                    mParallaxLayout.onScrolled(recyclerView, dx, dy);
            }

        });

        testList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recyle_view_example, menu);
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

    public void testList() {
        for(int i =0; i<20; i++) {
            ViewData data = new ViewData();
            data.setType(i);

            temList.add(data);
        }

        m_adapter = new RecyleViewAdapter(getApplicationContext(),temList);
        if(m_list !=null) {
            m_list.setAdapter(m_adapter);
            m_list.setItemAnimator(new DefaultItemAnimator());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if(mParallaxLayout !=null)
            mParallaxLayout.onTouchEvent_Gesture(ev);
        return super.dispatchTouchEvent(ev);
    }
}
