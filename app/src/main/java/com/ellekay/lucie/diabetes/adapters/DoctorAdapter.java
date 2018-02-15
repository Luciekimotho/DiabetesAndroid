package com.ellekay.lucie.diabetes.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.models.DoctorRealm;
import com.ellekay.lucie.diabetes.views.DoctorDetail;

import io.realm.RealmResults;

/**
 * Created by lucie on 1/21/2018.
 */
public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.MyViewHolder>{
    private RealmResults<DoctorRealm> mRealmObjects;
    private Context context;
    String TAG = "Adapter";

    public DoctorAdapter(RealmResults<DoctorRealm> mRealmObjects) {
        this.mRealmObjects = mRealmObjects;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_list, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.mRealmObject = mRealmObjects.get(position);
        holder.name.setText(mRealmObjects.get(position).getName());
        holder.phone.setText( mRealmObjects.get(position).getPhone());
        holder.email.setText(mRealmObjects.get(position).getEmail());

        //holder.date.setText(mRealmObjects.get(position).getNotes());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "id sent is:"+ holder.mRealmObject.getId());

                Context context = v.getContext();
                Intent intent = new Intent(context, DoctorDetail.class);
                intent.putExtra(DoctorDetail.ARG_NAME_ID, (holder.mRealmObject.getId()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "Doctor adapter size: "+mRealmObjects.size());
        return mRealmObjects.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name, phone, email;
        public DoctorRealm mRealmObject;
        public final View mView;

        public MyViewHolder(View v) {
            super(v);
            mView = v;
            name = (TextView) v.findViewById(R.id.tv_doc_name);
            phone = (TextView) v.findViewById(R.id.tv_doc_phone);
            email = (TextView) v.findViewById(R.id.tv_doc_email);
        }
    }
}

