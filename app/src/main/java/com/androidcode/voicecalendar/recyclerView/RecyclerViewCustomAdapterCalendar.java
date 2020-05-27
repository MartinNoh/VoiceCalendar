package com.androidcode.voicecalendar.recyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcode.voicecalendar.R;
import com.androidcode.voicecalendar.db.DBHelper;

import java.util.ArrayList;


public class RecyclerViewCustomAdapterCalendar extends RecyclerView.Adapter<RecyclerViewCustomAdapterCalendar.CustomViewHolder> {

    private ArrayList<RecyclerViewDictionary> mList;
    private Context mContext;
    private String time;

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        // 리스너 추가
        protected TextView item;


        public CustomViewHolder(View view) {
            super(view);
            this.item = (TextView) view.findViewById(R.id.tv_list_item);

            view.setOnCreateContextMenuListener(this); // 리스너 등록
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {}
    }

        public RecyclerViewCustomAdapterCalendar(Context context, ArrayList<RecyclerViewDictionary> list) {
            mList = list;
            mContext = context;
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.recycler_view_calendar_item_list, viewGroup, false);

            CustomViewHolder viewHolder = new CustomViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

            viewholder.item.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            viewholder.item.setText("" + (position+1) + ". " + mList.get(position).getContent().trim());
        }

        @Override
        public int getItemCount() {
            return (null != mList ? mList.size() : 0);
        }
}