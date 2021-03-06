package com.example.api;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.api.persona.PersonaapiService;

import java.io.IOError;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
     private RecyclerView recyclerView;
     List<Persona> mData;
     private ListaPersonaAdapter listAdapter;
     private Retrofit retrofit;
     private SearchView search;
     private ImageView regresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setTheme(R.style.Theme_API);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

        recyclerView = findViewById(R.id.listrecyclerview);
        search = findViewById(R.id.search);
        retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        initListener();
        getPersona(this);

        regresar = (ImageView) findViewById(R.id.regresar);
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Splash.class);
                startActivity(i);
                //onBackPressed();
            }
        });
    }

    private void  initListener(){
        search.setOnQueryTextListener(this);
    }

    private void getPersona(Context context) {
        PersonaapiService personaapiService = retrofit.create(PersonaapiService.class);
        Call<List<Persona>> listCall = personaapiService.getPersona();
        listCall.enqueue(new Callback<List<Persona>>() {
            @Override
            public void onResponse(Call<List<Persona>> call, Response<List<Persona>> response) {
                if(response.isSuccessful()){
                    List<Persona> listPer = response.body();
                    showData(listPer, context);
                }
            }

            @Override
            public void onFailure(Call<List<Persona>> call, Throwable t) {

            }
        });

    }
    private void showData(List<Persona> listPersona, Context context){
        ListaPersonaAdapter listAdapter = new ListaPersonaAdapter(listPersona, context, new ListaPersonaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Persona item) {
                moveToDescription(item);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(listAdapter);
    }

    public void moveToDescription(Persona item){
        Intent i = new Intent(this, Splash.class);
        i.putExtra("Persona", item);
        startActivity(i);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //listAdapter.getFilter().filter(newText);
        return false;
    }
}