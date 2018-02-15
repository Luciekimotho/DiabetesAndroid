package com.ellekay.lucie.diabetes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.models.Readings;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by lucie on 9/30/2017.
 */

public class ReadingAdapter extends RecyclerView.Adapter<ReadingAdapter.MyViewHolder> {

    private List<Readings> readingList;
    private Context context;
    String TAG = "Diabetes";

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView glucoseLevel, timePeriod, date;

        public MyViewHolder(View v) {
            super(v);
            glucoseLevel = (TextView) v.findViewById(R.id.glucose_level);
            timePeriod = (TextView) v.findViewById(R.id.timeperiod);
            date = (TextView) v.findViewById(R.id.date);
        }
    }

    public ReadingAdapter(List<Readings> readingList){
        this.readingList = readingList;
    }

    @Override
    public ReadingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reading_list_row, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReadingAdapter.MyViewHolder holder, int position) {
        Readings readings = readingList.get(position);
        holder.glucoseLevel.setText(readings.getGlucoseLevel().toString() + " mg/dL");
        holder.timePeriod.setText(readings.getTimePeriod());

        Date date= readings.getTimeOfDay();
        String format = "EEE MMM dd HH:mm:ss zzz yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        Log.d("Retrofit date",sdf.toString());

        try {
            Date newDate = sdf.parse(date.toString());
            String format2 = "dd/MM/yyyy HH:mm";
            SimpleDateFormat sdf2 = new SimpleDateFormat(format2, Locale.US);
            holder.date.setText(sdf2.format(newDate));
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, ""+e);
        }
    }

    @Override
    public int getItemCount() {
        Log.d("SIZE", "Retrofit size"+readingList.size());
        return readingList == null ?0 : readingList.size();
    }
}




