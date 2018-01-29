package com.ellekay.lucie.diabetes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.models.ReminderRealm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.RealmResults;

/**
 * Created by lucie on 1/23/2018.
 */

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.MyViewHolder> {
    private RealmResults<ReminderRealm> mRealmObjects;
    private Context context;
    String TAG = "Adapter";

    public ReminderAdapter(RealmResults<ReminderRealm> mRealmObjects){
        this.mRealmObjects = mRealmObjects;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_list, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.realmObject = mRealmObjects.get(position);
        holder.reminder.setText(mRealmObjects.get(position).getReminder());
        Boolean isChecked;
        isChecked = mRealmObjects.get(position).getAlarm();
        holder.alarm.setChecked(isChecked);

        //holder.time.setText(mRealmObjects.get(position).getTime());
        String date = mRealmObjects.get(position).getTime();
        String format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        Log.d(TAG, mRealmObjects.toString());

//        try {
//            Date newDate = sdf.parse(date);
//            String format2 = "dd/MM/yyyy HH:mm";
//            SimpleDateFormat sdf2 = new SimpleDateFormat(format2, Locale.US);
//            holder.date.setText(sdf2.format(newDate));
//        } catch (ParseException e) {
//            e.printStackTrace();
//            Log.e("Retrofit", ""+e);
//        }

    }

    @Override
    public int getItemCount() {
        Log.d(TAG,"Item count"+ mRealmObjects.size());
        return mRealmObjects.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView reminder, date, time;
        public final View mView;
        public ReminderRealm realmObject;
        public Switch alarm;

        public MyViewHolder(View v) {

            super(v);
            mView = v;
            reminder = (TextView) v.findViewById(R.id.tv_reminder);
            date = (TextView) v.findViewById(R.id.tv_reminder_date);
            alarm = (Switch) v.findViewById(R.id.switch1);
            time = (TextView) v.findViewById(R.id.tv_reminder_date);
        }
    }

}
