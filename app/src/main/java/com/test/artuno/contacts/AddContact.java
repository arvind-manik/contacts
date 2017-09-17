package com.test.artuno.contacts;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by Artuno on 9/14/2017.
 */

public class AddContact extends AppCompatActivity{

    EditText name;
    EditText sname;
    EditText company;
    EditText email;
    EditText home;
    EditText work;
    TextView fileStatus;
    Long iid = 16l;
    //StringBuilder imgId = new StringBuilder();
    String imageId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);
        name = (EditText)findViewById(R.id.name);
        sname = (EditText)findViewById(R.id.sname);
        company = (EditText)findViewById(R.id.company);
        email = (EditText)findViewById(R.id.email);
        home = (EditText)findViewById(R.id.home);
        work = (EditText)findViewById(R.id.work);
        fileStatus = (TextView) findViewById(R.id.file);
    }

    public void pickImage(View v){
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(name.getText().toString().matches("") || sname.getText().toString().matches("")){
            Snackbar sb = Snackbar.make(v,"Fill name fields to add image",Snackbar.LENGTH_LONG);
            sb.show();
        }
        else if(fileStatus.getText().toString().equals("No file selected")){
            //imgId.append(name.getText().charAt(0));
            //imgId.append(sname.getText().charAt(0));
            //imgId.append(company.getText().charAt(0));
            //imgId.append(email.getText().charAt(0));
            //imgId.append(home.getText().charAt(0));
            //imgId.append(work.getText().charAt(0));
            //imgId.append(".png");
            //intent.setType("image/*");
            //intent.setAction("INTENT.ACTION_GET_CONTENT");
            startActivityForResult(intent,1);
        }
    }

    public void save(View v) throws ExecutionException, InterruptedException {
        String url = getString(R.string.flaskapi);

        if(!iid.equals(16l)) {
            imageId = String.valueOf(iid);
        }
        else imageId = null;
        if(name.getText().toString().matches("") || sname.getText().toString().matches("") || home.getText().toString().matches("")){
            Snackbar sb = Snackbar.make(v,"Fill name fields, add one number to save",Snackbar.LENGTH_LONG);
            sb.show();
        }
        else {
            String endpoint = "/add?name=" + name.getText().toString() + "&sname=" + sname.getText().toString() + "&company=" + company.getText().toString()
                    + "&email=" + email.getText().toString() + "&home=" + home.getText().toString() + "&work=" + work.getText().toString() + "&iid=" + imageId;
            String result;
            HttpGetRequest req = new HttpGetRequest();
            result = (req.execute(url, endpoint)).get();
            Log.v("addcontact", result);
            if (result != null) {
                Snackbar.make(v, "Contact added", Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(v, "Error encountered, try again", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        iid = iid*5 + name.getText().toString().hashCode();
        iid = iid*6 + sname.getText().toString().hashCode();
        imageId = String.valueOf(iid);
        fileStatus.setText("Selected");
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri uri = data.getData();
            Bitmap thumb = null;
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                thumb = Bitmap.createScaledBitmap(bitmap,64,64,false);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("profile", Context.MODE_PRIVATE);
            if (!directory.exists()) {
                directory.mkdir();
            }
            File myPath = new File(directory, imageId+".png");
            Log.v("file",myPath.toString());

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(myPath);
                //fileStatus.setText(imgId);
                thumb.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
            } catch (Exception e) {
                Log.e("SAVE_IMAGE", e.getMessage(), e);
            }
        }
    }

}
