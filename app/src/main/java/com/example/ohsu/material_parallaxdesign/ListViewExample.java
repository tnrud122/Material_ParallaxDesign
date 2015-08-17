package com.example.ohsu.material_parallaxdesign;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.ohsu.material_parallaxdesign.parallax.adapter.ListViewAdapter;
import com.example.ohsu.material_parallaxdesign.parallax.data.ViewData;
import com.example.ohsu.material_parallaxdesign.parallax.layout.ParallaxLayout;

import java.util.ArrayList;

public class ListViewExample extends AppCompatActivity implements AbsListView.OnScrollListener {
    private ListView m_list = null;
    private ListViewAdapter m_adapter = null;
    private ArrayList<ViewData> temList = new ArrayList<ViewData>();
    private ParallaxLayout	mParallaxLayout;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_example);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mParallaxLayout = (ParallaxLayout)findViewById(R.id.contentLayout);
        mParallaxLayout.setToolbar(toolbar);
        m_list = (ListView)findViewById(R.id.list_content);
        m_list.setOverScrollMode(View.OVER_SCROLL_NEVER);
        m_list.setOnScrollListener(this);
        testList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_view_example, menu);
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

        m_adapter = new ListViewAdapter(getApplicationContext(),temList);
        if(m_list !=null)
            m_list.setAdapter(m_adapter);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(mParallaxLayout !=null)
            mParallaxLayout.onScrollStateChanged(view, scrollState);
    }
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
        if(mParallaxLayout !=null)
            mParallaxLayout.onScroll(view, firstVisibleItem,visibleItemCount,totalItemCount);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if(mParallaxLayout !=null)
            mParallaxLayout.onTouchEvent_Gesture(ev);
        return super.dispatchTouchEvent(ev);
    }
}
