package com.androidcode.voicecalendar.ui.Repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.androidcode.voicecalendar.recyclerViewRepository.RecyclerViewCustomAdapterRepository;
import com.androidcode.voicecalendar.recyclerViewRepository.RecyclerViewDictionaryRepository;

import java.util.ArrayList;

public class RepositoryFragment extends Fragment {

    private ArrayList<RecyclerViewDictionaryRepository> mArrayList;
    private RecyclerViewCustomAdapterRepository mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_repository, container, false);

        // Recycler View
        RecyclerView mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerview_main_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mArrayList = new ArrayList<>();

        mAdapter = new RecyclerViewCustomAdapterRepository(getActivity(), mArrayList);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        // SQLite
        DBHelper helper = new DBHelper(getActivity());
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select _id, date, content from tb_memo order by _id desc", null);

        while(cursor.moveToNext()){
            RecyclerViewDictionaryRepository data = new RecyclerViewDictionaryRepository(cursor. getInt(0), cursor.getString(1), cursor.getString(2));
            mArrayList.add(data);
        }
        db.close();
        mAdapter.notifyDataSetChanged();

        return root;
    }
}