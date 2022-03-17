package com.example.allselect;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

public class LisAdapter extends RecyclerView.Adapter<LisAdapter.ItemViewHolder> {

    private List<Book> mItems;
    private List<Book> selected;
    public HashMap<Integer, Boolean> map;
    private EventBus eventBus;

    public LisAdapter(List<Book> mItems, EventBus eventBus) {
        this.mItems = mItems;
        this.eventBus = eventBus;
        map = new HashMap<>();
        selected = new ArrayList<>();
        init();
    }

    private void init() {
        if (null == mItems || mItems.size() <= 0) {
            return;
        }
        for (int i = 0, p = mItems.size(); i < p; i++) {
            map.put(i, false);
        }
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_item, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (null == mItems || mItems.size() <= 0) {
            return;
        }
        holder.name.setText(mItems.get(position).getName());
       // holder.desc.setText(mItems.get(position).getDesc());

        holder.checkBox.setTag(new Integer(position));//防止划回来时选中消失

        if (map != null) {
            ((ItemViewHolder) holder).checkBox.setChecked((map.get(position)));
        } else {
            ((ItemViewHolder) holder).checkBox.setChecked(false);
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mFlags = (Integer) view.getTag();
                if (map != null) {
                    if (map.get(position)) {
                        map.put(position, false);
                        eventBus.post(new SelectEvent(selected(map)));
                    } else {
                        map.put(mFlags, Boolean.TRUE);
                        eventBus.post(new SelectEvent(selected(map)));
                    }
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(holder,holder.getAdapterPosition());
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mItemClickListener.onItemLongClick(holder,holder.getAdapterPosition());
                return true;
            }
        });
    }

    private int selected(HashMap<Integer, Boolean> map) {
        int size = 0;
        for (Integer key : map.keySet()) {
            if(map.get(key)){
                size++;
            }
        }
        return size;
    }
    @Override
    public int getItemCount() {
        return mItems == null? 0 :mItems.size();
    }
    public static class ItemViewHolder extends RecyclerView.ViewHolder{

        public final CheckBox checkBox;
        public final TextView name;
        //public final TextView desc;

        public ItemViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            //desc = (TextView) itemView.findViewById(R.id.tv_desc);
        }
    }

    public HashMap<Integer, Boolean> getMap() {
        return map;
    }

    public void setMap(HashMap<Integer, Boolean> map) {
        this.map = map;
        notifyDataSetChanged();
    }

    /**
     * 点击事件和长按事件
     */
    public interface ItemClickListener{
        void onItemClick(RecyclerView.ViewHolder holder , int position);
        void onItemLongClick(RecyclerView.ViewHolder holder , int position);
    }

    private ItemClickListener mItemClickListener;
    public void setOnItemClickListener(ItemClickListener listener){
        this.mItemClickListener=listener;
    }
}


















