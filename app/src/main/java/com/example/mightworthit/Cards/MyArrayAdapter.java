package com.example.mightworthit.Cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mightworthit.Cards.Cards;
import com.example.mightworthit.R;

import java.util.List;

public class MyArrayAdapter extends android.widget.ArrayAdapter<Cards> {
    Context context;
    public MyArrayAdapter(Context context, int resourceID, List<Cards> items){
        super(context,resourceID,items);
    }

    public View getView (int position, View convertView, ViewGroup parent){
        Cards card_item = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        name.setText(card_item.getName());
        switch (card_item.getProfileImageURL()){
            case "default":
                Glide.with(convertView.getContext()).load(R.mipmap.ic_launcher).into(image);
            default:
                Glide.with(convertView.getContext()).clear(image);
                Glide.with(getContext()).load(card_item.getProfileImageURL()).into(image);
                break;
        }

        return convertView;
    }
}
