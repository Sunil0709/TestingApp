package verixos.com.testingapp;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static verixos.com.testingapp.R.id.buttonAddArtist;

public class MainActivity extends AppCompatActivity {


    ProgressDialog progressbar;
    //view objects
    EditText editTextName,sec;

    Button buttonAdd;
    ListView listViewArtists;

    //a list to store all the artist from firebase database
    List<Artist> artists;

    //our database reference object
    DatabaseReference databaseArtists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting the reference of artists node
        databaseArtists = FirebaseDatabase.getInstance().getReference("artists");

        //getting views
        editTextName = (EditText) findViewById(R.id.editTextName);
        sec = (EditText) findViewById(R.id.sec);
        listViewArtists = (ListView) findViewById(R.id.listViewArtists);

        buttonAdd = (Button) findViewById(buttonAddArtist);

        //list to store artists
        artists = new ArrayList<>();


        //adding an onclicklistener to button
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addArtist()
                //the method is defined below
                //this method is actually performing the write operation
                addArtist();
            }
        });
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(mReceiver, filter);
    }

    /*
    * This method is saving a new artist to the
    * Firebase Realtime Database
    * */
    private void addArtist() {
        //getting the values to save
        String name = editTextName.getText().toString().trim();
        String genre = sec.getText().toString().trim();

        //checking if the value is provided
        if (!TextUtils.isEmpty(name)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Artist
            String id = databaseArtists.push().getKey();

            //creating an Artist Object
            Artist artist = new Artist(id, name, genre);

            //Saving the Artist
            databaseArtists.child(id).setValue(artist);

            //setting edittext to blank again
            editTextName.setText("");

            //displaying a success toast
            Toast.makeText(this, "Data added", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a Data", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();



progressbar = new ProgressDialog(this, R.style.Theme_MyDialog);
        progressbar.show();

        databaseArtists.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                artists.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Artist artist = postSnapshot.getValue(Artist.class);
                    //adding artist to the list
                    artists.add(artist);
                }

                //creating adapter
                ArtistList artistAdapter = new ArtistList(MainActivity.this, artists);
                //attaching adapter to the listview
                listViewArtists.setAdapter(artistAdapter);
                progressbar.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressbar.dismiss();
            }
        });
    }
    //The BroadcastReceiver that listens for bluetooth broadcasts
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                editTextName.setText(device.getName());
                sec.setText(device.getAddress());
            }
            else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                editTextName.setText(device.getName());
                sec.setText(device.getAddress());
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                editTextName.setText(device.getName());
                sec.setText(device.getAddress());
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                editTextName.setText(device.getName());
                sec.setText(device.getAddress());
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                editTextName.setText(device.getName());
                sec.setText(device.getAddress());
            }
        }
    };
}