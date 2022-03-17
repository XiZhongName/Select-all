package com.example.allselect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CheckBox checkbox;//全选
    private TextView selected;//已选的个数
    private LisAdapter adapter;
    private EventBus event;
    private boolean isChange = false;
    private ArrayList<Book> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        event = EventBus.getDefault();
        event.register(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        selected = (TextView) findViewById(R.id.selected);
    }

    private void initData() {
        for (int i = 0; i < 30; i++) {
            Book model = new Book();
            model.setId(i);
            model.setName("商品" + i);
            model.setDesc("描述" + i);
            list.add(model);
        }
        adapter = new LisAdapter(list, event);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    HashMap<Integer, Boolean> map = new HashMap<Integer, Boolean>();
                    int count = 0;
                    if (isChecked) {
                        isChange = false;
                    }
                    for (int i = 0, p = list.size(); i < p; i++) {
                        if (isChecked) {
                            map.put(i, true);
                            count++;
                        } else {
                            if (!isChange) {
                                map.put(i, false);
                                count = 0;
                            } else {
                                map = adapter.getMap();
                                count = map.size();
                            }
                        }
                    }
                    selected.setText("已选" + count + "项");
                    adapter.setMap(map);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        adapter.setOnItemClickListener(new LisAdapter.ItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int positon) {
                Log.e("onItemClick", "" + positon);
            }

            @Override
            public void onItemLongClick(final RecyclerView.ViewHolder holder, final int positon) {
                Log.e("onItemLongClick", "" + positon);
            }
        });
    }

    public void onEventMainThread(SelectEvent event) {
        int size = event.getSize();
        if (size < list.size()) {
            isChange = true;
            checkbox.setChecked(false);
        } else {
            checkbox.setChecked(true);
            isChange = false;
        }
        selected.setText("已选" + size + "项");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        event.unregister(this);
    }
}













