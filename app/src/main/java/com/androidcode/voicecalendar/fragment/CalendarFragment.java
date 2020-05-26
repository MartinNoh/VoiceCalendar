package com.androidcode.voicecalendar.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcode.voicecalendar.R;
import com.androidcode.voicecalendar.db.DBHelper;
import com.androidcode.voicecalendar.recyclerView.RecyclerViewCustomAdapterCalendar;
import com.androidcode.voicecalendar.recyclerView.RecyclerViewDictionary;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;


import java.util.ArrayList;

public class CalendarFragment extends Fragment {

    private ArrayList<RecyclerViewDictionary> mArrayList;
    private RecyclerViewCustomAdapterCalendar mAdapter;
    private String time;
    DBHelper helper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        MaterialCalendarView materialCalendarView = (MaterialCalendarView) root.findViewById(R.id.calendarView);
        RecyclerView mRecyclerView = (RecyclerView) root.findViewById(R.id.rv_list);
        mArrayList = new ArrayList<>();

        // SQLite
        helper = new DBHelper(getActivity());

        // Calendar
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                time = "" + date.getYear() + "/" + date.getMonth() + "/" + date.getDay();
                getMemoCursor();
            }
        });

        // Recycler View
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mAdapter = new RecyclerViewCustomAdapterCalendar(getActivity(), mArrayList);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        return root;
    }

    private void getMemoCursor()
    {
        mArrayList.clear();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id, date, content FROM tb_memo WHERE date = '" + time + "' ORDER BY _id DESC", null);

        while(cursor.moveToNext()){
            RecyclerViewDictionary data = new RecyclerViewDictionary(cursor. getInt(0), cursor.getString(1), cursor.getString(2));
            mArrayList.add(data);
        }
        db.close();
        mAdapter.notifyDataSetChanged();
    }
}
