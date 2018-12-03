package ssu.groupname.baseapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ssu.groupname.baseapplication.models.Garden;
import ssu.groupname.baseapplication.models.User;
import ssu.groupname.baseapplication.network.UserSearchAsyncTask;

public class GardenSelectorActivity extends AppCompatActivity {

    private RecyclerView myGardenRecyclerView;
    private Button viewGardensButton;
    private EditText myUsernameEditText;
    private User myUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.garden_selector);

        //Set background image so that it does not resize on keyboard pop-up
        getWindow().setBackgroundDrawableResource(R.drawable.vines);

        myUsernameEditText = findViewById(R.id.enter_username_edittext);

        //Here we use UserSearchAsyncTask to get the info from the server
        myGardenRecyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        myGardenRecyclerView.setLayoutManager(layoutManager);
        //Button to get garden info, later we will do this automatically
        //with the stored userId or have the user enter their id
        viewGardensButton = findViewById(R.id.show_gardens_button);
        viewGardensButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSearchAsyncTask task = new UserSearchAsyncTask();
                task.setListener(new UserSearchAsyncTask.UserCallbackListener() {
                    @Override
                    public void onUserCallback(User user) {
                        GardenViewAdapter adapter = new GardenViewAdapter(user);
                        myGardenRecyclerView.setAdapter(adapter);
                        myUser = user;
                    }
                });
                task.execute(myUsernameEditText.getText().toString());
            }
        });
    }




    //Nested recycler view holder class
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
                    Intent intent = new Intent(GardenSelectorActivity.this, ZoneViewActivity.class);
                    intent.putExtra("GARDEN_ID", garden.getId());
                    startActivity(intent);
                }
            });
        }
    }

    //Nested recycler view adapter class
    public class GardenViewAdapter extends RecyclerView.Adapter<GardenViewHolder> {

        private User myUser;

        public GardenViewAdapter(User user) {myUser = user;}

        @NonNull
        @Override
        public GardenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View inflatedView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_list_item, parent, false);
            return new GardenViewHolder(inflatedView);
        }

        @Override
        public void onBindViewHolder(@NonNull GardenViewHolder gardenViewHolder, int position) {
            Garden garden = myUser.getGardens().get(position);
            gardenViewHolder.bindView(garden);
        }

        @Override
        public int getItemCount() {return myUser.getGardens().size();}
    }
}

