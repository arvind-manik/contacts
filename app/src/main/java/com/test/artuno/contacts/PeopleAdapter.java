package com.test.artuno.contacts;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Artuno on 9/14/2017.
 */

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder>{

    private Context context;
    private List<People> peopleList;

    public PeopleAdapter(List<People> list, Context context){
        super();
        this.peopleList = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        People people = peopleList.get(position);

        holder.name.setText(people.getName());
        holder.sname.setText(people.getSname());
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("profile", Context.MODE_PRIVATE);
        //Log.v("image",people.getImageId());
        //Log.v("curr_dir",directory.toString());
        if(!people.getImageId().equals("null")) {
            Bitmap thumbnail = BitmapFactory.decodeFile(directory.toString() + "/" + people.getImageId() + ".png");
            //Log.v("files",directory.toString() + "/" + people.getImageId() + ".png");
            holder.img.setImageBitmap(thumbnail);
        }
    }

    @Override
    public int getItemCount() {

        return peopleList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public MyViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View view) {
            ((ContactsView) context).onClickOfElement(peopleList.get(getAdapterPosition()));
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView sname;
        public ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.name);
            sname = (TextView)itemView.findViewById(R.id.sname);
            img = (ImageView)itemView.findViewById(R.id.profile_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Intent i = new Intent(v.getContext(),PersonCard.class);
                    Bundle b = new Bundle();
                    b.putString("name",peopleList.get(getAdapterPosition()).getName());
                    b.putString("sname",peopleList.get(getAdapterPosition()).getSname());
                    i.putExtras(b);
                    v.getContext().startActivity(i);
                }
            });
        }

    }

    public interface OnActionDone{
        public void onClick(People person);
    }

}
