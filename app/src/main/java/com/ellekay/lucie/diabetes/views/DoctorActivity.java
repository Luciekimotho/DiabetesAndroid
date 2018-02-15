package com.ellekay.lucie.diabetes.views;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.adapters.DoctorAdapter;
import com.ellekay.lucie.diabetes.models.Doctor;
import com.ellekay.lucie.diabetes.models.DoctorRealm;
import com.ellekay.lucie.diabetes.auth.SessionManagement;
import com.ellekay.lucie.diabetes.rest.ApiClient;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorActivity extends AppCompatActivity {
    private RealmConfiguration mRealmConfig;
    private Realm mRealm;
    String TAG = "Doctor";
    SessionManagement session;

    private Context mContext;
    private List<Doctor> doctorsList = new ArrayList<>();

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRealmConfig = new RealmConfiguration
                .Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(mRealmConfig);
        mRealm = Realm.getDefaultInstance();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorActivity.this, NewDoctor.class));
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(7), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final RealmResults<DoctorRealm> doctorRealms = getRealmResults();
       initiateApi(recyclerView);
        if (doctorRealms.size() == 0){
            initiateApi(recyclerView);
        }else {
            setupRecyclerView(recyclerView, doctorRealms);
        }
    }

    private void initiateApi(final View recyclerView){
        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.getDoctors().enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if (response.isSuccessful()){
                    response.body();
                    doctorsList = response.body();
                    executeRealmWrite(doctorsList);

                    Log.d(TAG,""+ doctorsList);
                }else {
                    Log.d(TAG, "Error "+response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {

                Log.d(TAG, ""+t.getMessage());
            }
        });
    }

    private void executeRealmWrite(final List<Doctor> doctorReadings){
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (int i=0; i<doctorReadings.size(); i++){
                    DoctorRealm doctor = realm.createObject(DoctorRealm.class);
                    doctor.setId(doctorReadings.get(i).getId());
                    doctor.setName(doctorReadings.get(i).getName());
                    doctor.setEmail(doctorReadings.get(i).getEmail());
                    doctor.setPhone(doctorReadings.get(i).getPhone());
                    doctor.setNotes(doctorReadings.get(i).getNotes());
                    Log.d(TAG, doctor.toString());
                }

            }
           }, new Realm.Transaction.OnSuccess(){
               @Override
               public void onSuccess() {
                   Log.d(TAG,"savedRealmObjects");
               }
           },new Realm.Transaction.OnError(){
               @Override
               public void onError(Throwable error) {
                   Log.d(TAG,"Not saving error: " +error.toString());
               }
           }
        );
        Log.d(TAG,"Doctor size is:" + doctorReadings.size());
    }

    private RealmResults<DoctorRealm> getRealmResults(){
        RealmResults<DoctorRealm> sortedDoc = mRealm.where(DoctorRealm.class)
                .findAllSorted("id", Sort.DESCENDING).distinct("id");
        return sortedDoc;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, RealmResults<DoctorRealm> results){
        recyclerView.setAdapter(new DoctorAdapter(results));
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration{
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}




