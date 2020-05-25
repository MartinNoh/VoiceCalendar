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


public class RecyclerViewCustomAdapterRepository extends RecyclerView.Adapter<RecyclerViewCustomAdapterRepository.CustomViewHolder> {

    private ArrayList<RecyclerViewDictionary> mList;
    private Context mContext;

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        // 리스너 추가
        protected TextView content;


        public CustomViewHolder(View view) {
            super(view);
            this.content = (TextView) view.findViewById(R.id.content_listitem);

            view.setOnCreateContextMenuListener(this); // 리스너 등록
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {  // 메뉴 추가
            MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "편집");
            MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");
            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);

        }

        // 컨텍스트 메뉴 클릭시 동작을 설정
        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 1001:

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        View view = LayoutInflater.from(mContext).inflate(R.layout.edit_box, null, false);
                        builder.setView(view);
                        final Button ButtonSubmit = (Button) view.findViewById(R.id.button_dialog_submit);
                        final EditText editTextDate = (EditText) view.findViewById(R.id.edittext_dialog_date);
                        final EditText editTextContent = (EditText) view.findViewById(R.id.edittext_dialog_content);

                        editTextDate.setText(mList.get(getAdapterPosition()).getDate());
                        editTextContent.setText(mList.get(getAdapterPosition()).getContent());

                        final AlertDialog dialog = builder.create();
                        dialog.show();

                        ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                int intId = mList.get(getAdapterPosition()).getId();
                                String strDate = editTextDate.getText().toString();
                                String strContent = editTextContent.getText().toString();

                                RecyclerViewDictionary dict = new RecyclerViewDictionary(intId, strDate, strContent);
                                update(intId, strDate, strContent);
                                mList.set(getAdapterPosition(), dict);
                                notifyItemChanged(getAdapterPosition());

                                dialog.dismiss();
                            }
                        });

                        break;

                    case 1002:
                        delete(mList.get(getAdapterPosition()).getId(), mList.get(getAdapterPosition()).getDate(), mList.get(getAdapterPosition()).getContent());
                        mList.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), mList.size());

                        break;

                }
                return true;
            }
        };
    }

        public RecyclerViewCustomAdapterRepository(Context context, ArrayList<RecyclerViewDictionary> list) {
            mList = list;
            mContext = context;
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.recycler_view_repository_item_list, viewGroup, false);

            CustomViewHolder viewHolder = new CustomViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
            String[] time = mList.get(position).getDate().split("/");
            String year = time[0];
            String month = time[1].length() == 2 ? time[1] : "0" + time[1];
            String day = time[2].length() == 2 ? time[2] : "0" + time[2];
            viewholder.content.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            viewholder.content.setText(year + "/" + month + "/" + day + "\t" + mList.get(position).getContent());
        }

        @Override
        public int getItemCount() {
            return (null != mList ? mList.size() : 0);
        }

    public void update(int id, String date, String content) {
        DBHelper helper = new DBHelper(mContext);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("UPDATE tb_memo SET date = '" + date + "', content = '" + content + "'WHERE _id = '" + id + "';");
        db.close();
    }

    public void delete(int id, String date, String content) {
        DBHelper helper = new DBHelper(mContext);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM tb_memo WHERE _id = '" + id + "';");
        db.close();
    }
}