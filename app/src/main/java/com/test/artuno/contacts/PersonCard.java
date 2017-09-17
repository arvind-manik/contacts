package com.test.artuno.contacts;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * Created by Artuno on 9/17/2017.
 */

public class PersonCard extends AppCompatActivity{
    String name;
    String sname;
    TextView call1;
    TextView call2;
    TextView n1,n2;
    TextView mail;
    ImageView img;
    ImageButton b1,b2,b3;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_card);
        Bundle b = getIntent().getExtras();
        name = b.getString("name");
        sname = b.getString("sname");

        n1 = (TextView)findViewById(R.id.name);
        n2 = (TextView)findViewById(R.id.sname);
        call1 = (TextView)findViewById(R.id.home);
        call2 = (TextView)findViewById(R.id.work);
        mail = (TextView)findViewById(R.id.email);
        b1 = (ImageButton)findViewById(R.id.call1);
        b2 = (ImageButton)findViewById(R.id.call2);
        b3 = (ImageButton)findViewById(R.id.mail);
        img = (ImageView)findViewById(R.id.profile_image);
        getDetails();
    }

    private void getDetails(){
        String url = getString(R.string.flaskapi);
        String endpoint = "/details?name="+name+"&sname="+sname;
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
        try{
            JSONObject jsonObject = new JSONObject(res);
            JSONArray arr = jsonObject.getJSONArray("details");
            JSONObject det = arr.getJSONObject(0);

            n1.setText(det.getString("name"));
            n2.setText(det.getString("sname"));
            call1.setText(det.getString("home"));
            call2.setText(det.getString("work"));
            if(call2.getText().toString().equals("")){
                b2.setVisibility(View.INVISIBLE);
            }
            mail.setText(det.getString("email"));
            if(mail.getText().toString().equals("")){
                b3.setVisibility(View.INVISIBLE);
            }
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("profile", Context.MODE_PRIVATE);
            if(!det.getString("imageid").equals("null")) {
                Bitmap thumbnail = BitmapFactory.decodeFile(directory.toString() + "/" + det.getString("imageid") + ".png");
                img.setImageBitmap(thumbnail);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void callHome(View v){
        String phone = call1.getText().toString();
        Log.v("test",phone);
        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(phoneIntent);
    }

    public void callWork(View v){
        String phone = call2.getText().toString();
        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(phoneIntent);
    }

    public void send(View v){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, mail.getText().toString());
        startActivity(Intent.createChooser(intent, ""));
    }
}
