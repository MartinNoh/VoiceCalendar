package com.androidcode.voicecalendar.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcode.voicecalendar.R;
import com.androidcode.voicecalendar.db.DBHelper;
import com.androidcode.voicecalendar.decorators.EventDecorator;
import com.androidcode.voicecalendar.recyclerView.RecyclerViewCustomAdapterCalendar;
import com.androidcode.voicecalendar.recyclerView.RecyclerViewDictionary;

import com.androidcode.voicecalendar.decorators.EventDecorator;
import com.androidcode.voicecalendar.decorators.OneDayDecorator;
import com.androidcode.voicecalendar.decorators.SaturdayDecorator;
import com.androidcode.voicecalendar.decorators.SundayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

public class CalendarFragment extends Fragment {

    MaterialCalendarView materialCalendarView;
    private ArrayList<RecyclerViewDictionary> mArrayList;
    private ArrayList<String> arrayList;
    private RecyclerViewCustomAdapterCalendar mAdapter;
    private String time;
    DBHelper helper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        materialCalendarView = (MaterialCalendarView) root.findViewById(R.id.calendarView);
        RecyclerView mRecyclerView = (RecyclerView) root.findViewById(R.id.rv_list);
        mArrayList = new ArrayList<>();
        arrayList = new ArrayList<>();

        // SQLite
        helper = new DBHelper(getActivity());

        // Calendar
        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                new OneDayDecorator());

        getListCursor();
        String[] strings = new String[arrayList.size()];
        for(int i=0; i<arrayList.size(); i++){
            strings[i] = arrayList.get(i);
        }

        new ApiSimulator(strings).executeOnExecutor(Executors.newSingleThreadExecutor());

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

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        String[] Time_Result;

        ApiSimulator(String[] Time_Result){
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ArrayList<CalendarDay> dates = new ArrayList<>();

            /*특정날짜 달력에 점표시해주는곳*/
            /*월은 0이 1월 년,일은 그대로*/
            //string 문자열인 Time_Result 을 받아와서 /를 기준으로짜르고 string을 int 로 변환
            for(int i = 0 ; i < Time_Result.length ; i ++){

                String[] time = Time_Result[i].split("/");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int day = Integer.parseInt(time[2]);

                CalendarDay date = CalendarDay.from(year, month, day);
                dates.add(date);
            }

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (getActivity().isFinishing()) {
                return;
            }

            materialCalendarView.addDecorator(new EventDecorator(Color.RED, calendarDays, getActivity()));
        }
    }

    private void getListCursor()
    {
        arrayList.clear();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT date FROM tb_memo", null);

        while(cursor.moveToNext()){
            arrayList.add(cursor.getString(0));
        }
        db.close();
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
