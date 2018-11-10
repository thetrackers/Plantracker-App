package ssu.groupname.baseapplication;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import ssu.groupname.baseapplication.models.Garden;

public class GardenViewHolder extends RecyclerView.ViewHolder{

    private Button myGardenImageButton;

    public GardenViewHolder(View UserView) {
        super(UserView);

        myGardenImageButton = UserView.findViewById(R.id.garden_button);
    }

    public void bindView(Garden garden) {
        myGardenImageButton.setText(garden.getName());

        myGardenImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(GardenSelectorActivity.this, ZoneViewActivity.class);

            }
        });
    }
}
