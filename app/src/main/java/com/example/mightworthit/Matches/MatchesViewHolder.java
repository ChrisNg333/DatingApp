package com.example.mightworthit.Matches;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mightworthit.Chat.ChatActivity;
import com.example.mightworthit.R;

public class MatchesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView mMatchID, mMatchName;
    public ImageView mMatchImage;
    public MatchesViewHolder(View itemView){
        super(itemView);
        itemView.setOnClickListener(this);

        mMatchID = (TextView) itemView.findViewById(R.id.MatchID);
        mMatchName = (TextView) itemView.findViewById(R.id.MatchName);
        mMatchImage = (ImageView) itemView.findViewById(R.id.MatchImage);
    }

    @Override
    public  void onClick(View view){
        Intent intent = new Intent(view.getContext(), ChatActivity.class);
        Bundle b = new Bundle();
        b.putString("MatchID", mMatchID.getText().toString());
        intent.putExtras(b);
        view.getContext().startActivity(intent);
    }


}
