package com.ellekay.lucie.diabetes.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ellekay.lucie.diabetes.R;
import com.ellekay.lucie.diabetes.adapters.ChatAdapter;
import com.ellekay.lucie.diabetes.adapters.DoctorAdapter;
import com.ellekay.lucie.diabetes.adapters.MessagesAdapter;
import com.ellekay.lucie.diabetes.models.Chat;
import com.ellekay.lucie.diabetes.models.ChatRealm;
import com.ellekay.lucie.diabetes.models.Doctor;
import com.ellekay.lucie.diabetes.models.DoctorRealm;
import com.ellekay.lucie.diabetes.rest.ApiClient;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorDetail extends AppCompatActivity {
    private RealmConfiguration mRealmConfig;
    private Realm mRealm;
    String TAG = "Doctor";
    TextView tvPhone, tvEmail, tvName;
    Button send;
    public static final String ARG_NAME_ID = "item_name_id";
    Doctor doctor;
    RecyclerView recyclerView;
    Context mContext;
    Bundle extras;
    int id;
    private List<Chat> chatList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detail);

        tvName = (TextView) findViewById(R.id.tv_doctor_name);
        tvPhone= (TextView) findViewById(R.id.tv_doctor_phone);
        tvEmail = (TextView) findViewById(R.id.tv_doctor_email);
        recyclerView = (RecyclerView) findViewById(R.id.chatRecycler);

        send = (Button) findViewById(R.id.button_chatbox_send);

        mRealmConfig = new RealmConfiguration
                .Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(mRealmConfig);
        mRealm = Realm.getDefaultInstance();

        extras = getIntent().getExtras();
        id = (int) extras.getSerializable(ARG_NAME_ID);

        getDoctorInfo(id);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final RealmResults<ChatRealm> chatRealms = getRealmResults();
        //getChats(recyclerView);
        if (chatRealms.size() == 0){
            getChats(recyclerView);
        }else {
            setupRecyclerView(recyclerView, chatRealms);
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void getDoctorInfo(int id){
        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.getDoctor(id).enqueue(new Callback<Doctor>() {
            @Override
            public void onResponse(Call<Doctor> call, Response<Doctor> response) {
                if (response.isSuccessful()){
                    Log.d(TAG,""+ response.body());
                    doctor = response.body();
                    Log.d(TAG, doctor.getName().toString());
                    tvName.setText(doctor.getName().toString());
                    tvEmail.setText(doctor.getEmail().toString());
                    tvPhone.setText(doctor.getPhone().toString());
                    doctor.getNotes().toString();

                }else {
                    Log.d(TAG, "Error "+response.toString());
                }
            }

            @Override
            public void onFailure(Call<Doctor> call, Throwable t) {
                Log.d(TAG, ""+t.getMessage());

            }
        });
    }

    private void getChats(final View recyclerView){
        ApiClient apiClient = ApiClient.Factory.getInstance(mContext);
        apiClient.getChats().enqueue(new Callback<List<Chat>>() {
            @Override
            public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                if (response.isSuccessful()){
                    response.body();
                    chatList = response.body();
                    executeRealmWrite(chatList);
                    Log.d(TAG,""+ chatList);
                }else {
                    Log.d(TAG, "Error "+response.toString());
                }
            }
            @Override
            public void onFailure(Call<List<Chat>> call, Throwable t) {

                Log.d(TAG, ""+t.getMessage());
            }
        });
    }

    private void executeRealmWrite(final List<Chat> chats){
        mRealm.executeTransactionAsync(new Realm.Transaction() {
                                           @Override
                                           public void execute(Realm realm) {
               for (int i=0; i<chats.size(); i++){
                   ChatRealm chat = realm.createObject(ChatRealm.class);
                   chat.setId(chats.get(i).getId());
                   chat.setAuthor(chats.get(i).getAuthor());
                   chat.setRecepient(chats.get(i).getRecepient());
                   chat.setMessage(chats.get(i).getMessage());
                   chat.setTime(chats.get(i).getTime());
                   Log.d(TAG, chat.toString());
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
        Log.d(TAG,"Chat size is:" + chats.size());
    }

    private RealmResults<ChatRealm> getRealmResults(){
        RealmResults<ChatRealm> sortedChats = mRealm.where(ChatRealm.class)
                .findAllSorted("id", Sort.DESCENDING).distinct("id");
        return sortedChats;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, RealmResults<ChatRealm> results){
        recyclerView.setAdapter(new MessagesAdapter(results));
    }

    private void newMessage(){

    }

}
