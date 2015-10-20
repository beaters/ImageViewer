package com.lxg.root.imageviewer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * Created by root on 15-10-20.
 */
public class SecondActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        GridView myGridView=(GridView)findViewById(R.id.id_gridView);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        DATA data=(DATA)bundle.getSerializable("data");
        String path=data.getPath();
        List<String> names=data.getNames();
        File file=new File(path);
        int num=file.list().length;
        myGridView.setAdapter(new MyAdapter(getApplicationContext(),names,path));
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
