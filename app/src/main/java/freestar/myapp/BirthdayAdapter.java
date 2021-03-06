package freestar.myapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Calendar;
import java.util.List;

/**
 * Created by freestar on 2016/12/14.
 * 生日适配器
 */

public class BirthdayAdapter extends BaseQuickAdapter<Birthday, BaseViewHolder> {

    private final Context mContext;

    public BirthdayAdapter(int layoutResId, List<Birthday> data, Context context) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final Birthday item) {
        //设置数据
        holder.setText(R.id.tv_name, String.format("%s:%s", item.getName(), item.getTime()));
        TextView modify = holder.getView(R.id.tv_modify);
        TextView delete = holder.getView(R.id.tv_delete);
        //修改记录操作
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        String format = DateFormat.format("yyyy-MM-dd", calendar).toString();
                        //更新表中数据
                        MyDB myDB = new MyDB(mContext);
                        myDB.updateDate(format, item.getId());
                        //适配器数据源改变
                        item.setTime(format);
                        notifyDataSetChanged();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        //删除记录操作
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(holder.getAdapterPosition());
                MyDB myDB = new MyDB(mContext);
                myDB.deleteBirthday(item.getId());
            }
        });
    }
}
