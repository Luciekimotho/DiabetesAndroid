package com.ellekay.lucie.diabetes.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.fragments.HistoryFragement;
import com.ellekay.lucie.diabetes.models.Glucose;
import com.ellekay.lucie.diabetes.models.Readings;
import com.ellekay.lucie.diabetes.views.DoctorDetail;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.RealmResults;

/**
 * Created by lucie on 11/16/2017.
 */

public class GlucoseAdapter  extends RecyclerView.Adapter<GlucoseAdapter.MyViewHolder>{
    private RealmResults<Glucose> mRealmObjects;
    private Context context;
    String TAG = "Adapter";

    public GlucoseAdapter(RealmResults<Glucose> mRealmObjects) {
        this.mRealmObjects = mRealmObjects;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reading_list_row, parent, false);
        context = parent.getContext();
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.mRealmObject = mRealmObjects.get(position);


        holder.glucoseLevel.setText(mRealmObjects.get(position).getGlucoseLevel().toString() + "mg/dL");
        Float glucose = Float.valueOf(mRealmObjects.get(position).getGlucoseLevel().toString());
        if (glucose < 80){
            holder.glucoseLevel.setTextColor(ContextCompat.getColor(context, R.color.hypo));
        }else if(glucose > 130){
            holder.glucoseLevel.setTextColor(ContextCompat.getColor(context, R.color.hyper));
        }else {
            holder.glucoseLevel.setTextColor(ContextCompat.getColor(context, R.color.ok));
        }

        holder.timePeriod.setText(mRealmObjects.get(position).getTimePeriod());
        //holder.date.setText(mRealmObjects.get(position).getTimeOfDay());

        Date date = mRealmObjects.get(position).getTimeOfDay();
        String format = "EEE MMM dd HH:mm:ss zzz yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        Log.d(TAG, date.toString());

        try {
            Date newDate = sdf.parse(date.toString());
            String format2 = "dd/MM/yyyy HH:mm";
            SimpleDateFormat sdf2 = new SimpleDateFormat(format2, Locale.US);
            holder.date.setText(sdf2.format(newDate));
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("Retrofit", ""+e);
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arg = new Bundle();
                arg.putString(DoctorDetail.ARG_NAME_ID, String.valueOf(holder.mRealmObject.getGlucoseLevel()));
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "Glucose adapter size: "+mRealmObjects.size());
        return mRealmObjects.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView glucoseLevel, timePeriod, date;
        public Glucose mRealmObject;
        public final View mView;

        public MyViewHolder(View v) {
            super(v);
            mView = v;
            glucoseLevel = (TextView) v.findViewById(R.id.glucose_level);
            timePeriod = (TextView) v.findViewById(R.id.timeperiod);
            date = (TextView) v.findViewById(R.id.date);
        }
    }
}
