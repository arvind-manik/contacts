package com.test.artuno.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ContactsView extends AppCompatActivity {

    public List<People> peopleList;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    PeopleAdapter.OnActionDone oad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_item);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        peopleList = new ArrayList<>();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ContactsView.this,AddContact.class));
                adapter.notifyDataSetChanged();
            }
        });

        getNames();
    }

    private void getNames(){
        String url = getString(R.string.flaskapi);
        String endpoint = "/names";
        String result;
        HttpGetRequest req = new HttpGetRequest();
        try {
            result = req.execute(url,endpoint).get();
            parseJson(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void parseJson(String res){
        try {
            JSONObject jsonObject = new JSONObject(res);
            JSONArray arr = jsonObject.getJSONArray("people");
            peopleList.clear();
            for(int i=0; i<arr.length(); i++){
                People person = new People();
                JSONObject jObj = null;

                try{
                    jObj = arr.getJSONObject(i);
                    person.setName(jObj.getString("name"));
                    person.setSname(jObj.getString("sname"));
                    person.setImageId(jObj.getString("imageid"));
                }catch(JSONException e){
                    e.printStackTrace();
                }
                peopleList.add(person);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new PeopleAdapter(peopleList,this);
        recyclerView.setAdapter(adapter);
    }

    public void onClickOfElement(People person){
        Intent i = new Intent(this,PersonCard.class);
        Bundle b = new Bundle();
        b.putString("name",person.getName());
        b.putString("sname",person.getSname());
        i.putExtras(b);
        startActivity(i);
    }

    @Override
    public void onResume(){
        super.onResume();
        getNames();
        adapter.notifyDataSetChanged();
        //this.onCreate(null);
    }




    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/


}
