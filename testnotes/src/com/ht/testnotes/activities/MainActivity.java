package com.ht.testnotes.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import com.ht.testnotes.R;
import com.ht.testnotes.adapters.MyAdapter;
import com.ht.testnotes.pojo.Notes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA
 * Project: com.ht.com.ht.mynote.activities
 * Author: 安诺爱成长
 * Email: 1399487511@qq.com
 * Date: 2015/5/1
 */
public class MainActivity extends Activity {

    private ListView listView;
    private ImageButton imageButton;
    private MyAdapter adapter;
    private int position;
    private List<Notes> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview);
        imageButton = (ImageButton) findViewById(R.id.add);
        registerForContextMenu(listView);
        list = getList();
        adapter = new MyAdapter(this, list);
        listView.setAdapter(adapter);


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("notes", list.get(i));
                startActivity(intent);
            }
        });
    }

    private List<Notes> getList() {
        //得到所有的表中的信息
        list = new ArrayList<>();
        ContentResolver resolver = getContentResolver();
        String uriStr = "content://com.ht.mynote.contentprovider.MyContentProvider/mynotes";
        Uri uri = Uri.parse(uriStr);
        Cursor cursor = resolver.query(uri, null, null, null, "time desc");

        while (cursor.moveToNext()) {
            Notes notes = new Notes();
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String content = cursor.getString(2);
            String time = cursor.getString(3);
            notes.setId(id);
            notes.setTitle(title);
            notes.setContent(content);
            notes.setTime(time);
            list.add(notes);
        }

        cursor.close();
        return list;
    }

    //这样就可以保证每次返回的时候，更新我们的主界面的listview
    @Override
    protected void onResume() {
        super.onResume();
        list = getList();
        adapter = new MyAdapter(this, list);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        position = 0;
        if (menuInfo instanceof AdapterView.AdapterContextMenuInfo) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            position = info.position;
        }
        menu.add("删除：" + list.get(position).getTitle());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Notes notes = list.get(position);
        ContentResolver resolver = getContentResolver();
        String uriStr = "content://com.ht.mynote.contentprovider.MyContentProvider/mynotes";
        Uri uri = Uri.parse(uriStr);
        resolver.delete(uri, "_id = ?", new String[]{notes.getId() + ""});
        list = getList();
        adapter = new MyAdapter(this, list);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        return true;
    }
}