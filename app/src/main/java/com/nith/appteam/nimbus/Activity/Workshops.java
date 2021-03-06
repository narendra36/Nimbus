package com.nith.appteam.nimbus.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nith.appteam.nimbus.R;
import com.nith.appteam.nimbus.Utils.ApiInterface;
import com.nith.appteam.nimbus.Utils.Util;
import com.nith.appteam.nimbus.Model.WorkshopItem;
import com.nith.appteam.nimbus.Model.WorkshopListResponse;
import com.nith.appteam.nimbus.Adapter.WorkshopsAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Workshops extends AppCompatActivity {
    private ProgressBar bar;
    RecyclerView workshopsRv;
    WorkshopsAdapter workshopsAdapter;
    Toolbar workshopstb;
    ArrayList<WorkshopItem> workshop_item;
    private int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshops);
       // String BASE_URL = "https://github.com/";
         workshopsRv = (RecyclerView) findViewById(R.id.workshops_view);

        workshopsAdapter = new WorkshopsAdapter(Workshops.this);
        workshopsRv.setAdapter(workshopsAdapter);


        //workshopsRv.setAdapter(WorkshopsAdapter);

        //retrofit................
        bar=(ProgressBar)findViewById(R.id.progress);

        bar.setVisibility(View.VISIBLE);
        retrofit();


        workshopstb = (Toolbar) findViewById(R.id.workshops_toolbar);
        workshopstb.setTitle("Workshops");
//        setSupportActionBar(workshopstb);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    public void retrofit(){

        ApiInterface apiservice= Util.getRetrofitService();
        Call<WorkshopListResponse> call=apiservice.getAllWorkshop();

        call.enqueue(new Callback<WorkshopListResponse>() {
            @Override
            public void onResponse(Call<WorkshopListResponse> call, Response<WorkshopListResponse> response) {
                bar.setVisibility(View.GONE);

                WorkshopListResponse model=response.body();
                int status=response.code();

                if(model!=null && response.isSuccess()){
                    workshopsRv.setVisibility(View.VISIBLE);
                    //Log.v("Connection " ,"response : "+response.body());

                    ArrayList<WorkshopItem> workshop_item=model.getWorkshops();

                    if(workshop_item!=null){
                    Log.v("Size response ",workshop_item.size()+"");
                    LinearLayoutManager lvmanager = new LinearLayoutManager(Workshops.this);
                    lvmanager.setOrientation(LinearLayoutManager.VERTICAL);
                    workshopsRv.setLayoutManager(lvmanager);

                    workshopsAdapter.refresh(workshop_item);
                    }
                    else{
                        Toast.makeText(Workshops.this,"Unable to fetch data!!",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(Workshops.this,"Some error occurred!!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WorkshopListResponse> call, Throwable t) {
                bar.setVisibility(View.GONE);
                Toast.makeText(Workshops.this,"Some error occurred!!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
