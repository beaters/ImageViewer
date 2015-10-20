package com.lxg.root.imageviewer;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.DropBoxManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    /**
    * 保存路径和图片的Map
    * */
    private Map<String,List<String>> mDirPaths=new HashMap<String,List<String>>();

    /**
    * 保存路径和图片数量的临时set
    * */
    private Set<Map.Entry<String,List<String>>> set;

    /**
     * 所有的图片
     */
    private List<String> mImgs;
    private ProgressDialog mProgressDialog;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getImages();
        listView=(ListView)findViewById(R.id.listview);
        listView.setAdapter(new MyAdapter());

    }


    public class MyAdapter extends BaseAdapter
    {
        @Override
        public int getCount() {
            return mDirPaths.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=getLayoutInflater().inflate(R.layout.list_main,parent);
            TextView path_parent=(TextView)findViewById(R.id.text_parent);
            String path=getPath(set)[position];
            String text=path;
            path_parent.setText(text);
            mImgs =mDirPaths.get(path);
            view.setTag(position);
            view.setOnClickListener(new MyListener());
            return view;
        }
    }





    //UI handler
    private Handler mHandler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            mProgressDialog.dismiss();

        }
    };


    private void getImages()
    {
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            Toast.makeText(getApplicationContext(),"暂无外部存储",Toast.LENGTH_LONG).show();
            return;
        }

        // 显示进度条
        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
        new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = MainActivity.this
                        .getContentResolver();

                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[] { "image/jpeg", "image/png" },
                        MediaStore.Images.Media.DATE_MODIFIED);

                while (mCursor.moveToNext())
                {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));
                    // 获取该图片的父路径名
                    File parentFile = new File(path).getParentFile();
                    String dirPath = parentFile.getAbsolutePath();

                    //利用一个HashSet防止多次扫描同一个文件夹
                    Set<String> set=new HashSet<String>();
                    if(set.contains(dirPath))
                    {
                        continue;
                    }
                    else
                    {
                        set.add(dirPath);
                    }


                    for(String dirpath:set)
                    {
                        List<String> list=Arrays.asList(new File(dirpath).list());
                        mDirPaths.put(dirpath,list);
                    }

                }
                mCursor.close();
                // 通知Handler扫描图片完成
                mHandler.sendEmptyMessage(0x110);

            }
        }).start();
    }

    private String [] getPath(Set<Map.Entry<String,List<String>>> set1)
    {
        String [] path=new String [set1.size()];
        int i=0;
        for(Map.Entry<String,List<String>> entry:set1)
        {
            path[i++]=entry.getKey();
        }
        return path;
    }

    public class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View view)
        {
            DATA data=new DATA();
            int position=(int)view.getTag();
            String path=getPath(set)[position];
            List<String> names=mDirPaths.get(position);
            data.setPosition(position);
            data.setNames(names);
            data.setPath(path);
            Intent intent=new Intent(MainActivity.this,SecondActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("data",data);
            intent.putExtras(bundle);
            startActivity(intent);
        }
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
}
