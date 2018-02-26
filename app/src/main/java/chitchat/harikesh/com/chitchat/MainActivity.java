package chitchat.harikesh.com.chitchat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    EditText roomName;
//    EditText password;
    Button createRoom;
    ListView roomList;
    ArrayList<String> roomArrayList;
    ArrayAdapter<String> roomAdapter;

    //DB
    DatabaseReference databaseReference;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assigning ids
        roomName = (EditText) findViewById(R.id.editText);
//        password = (EditText) findViewById(R.id.passEditText);
        createRoom = (Button) findViewById(R.id.button);
        roomList = (ListView) findViewById(R.id.roomListView);

        roomArrayList = new ArrayList<String>();
        roomAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, roomArrayList);

        roomList.setAdapter(roomAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot();

        request_name();

        createRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (roomName.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter a valid room name!", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(roomName.getText().toString(), "");
                    //map.put(password.getText().toString(),"");
                    databaseReference.updateChildren(map);
                    FirebaseDatabase.getInstance().getReference().child(roomName.getText().toString());
                    roomName.setText("");
//                    password.setText("");
                }
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();
                Set<String> set = new HashSet<String>();
                while (iterator.hasNext()) {
                    //GET NAMES OF ALL THE ROOMS ONE BY ONE FROM YOUR DATABASE
                    set.add((String) ((DataSnapshot) iterator.next()).getKey());
                }
                roomArrayList.clear();
                roomArrayList.addAll(set);
                roomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        roomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //String[] pss = new String[1];
//                String pss;
//                req_pass();
//                final String yourData = adapterView.getItemAtPosition(i).toString();
//                Log.d("room", yourData);
//                DatabaseReference dbr2 = FirebaseDatabase.getInstance().getReference().getRoot().child(yourData).child("pass");
/*

             public void call(DataSnapshot ds) {


            }
*/
               /* dbr2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        pss[0] = (String) dataSnapshot.child("pass").getValue();
                        Log.d("Password",pss[0]);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/
                //String pss = FirebaseDatabase.getInstance().getReference().child(yourData).child("pass").toString();
                // Log.d("pass", pss[0]);
/*
                if (pss.equals(pass)) {
                    Toast.makeText(MainActivity.this, "correct pas", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, Chat_room.class);
                    intent.putExtra("Room_name", ((TextView) view).getText().toString());
                    intent.putExtra("User_name", userName);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Enter correct pass", Toast.LENGTH_SHORT).show();
                }
*/              Intent intent = new Intent(MainActivity.this, Chat_room.class);
                intent.putExtra("Room_name", ((TextView) view).getText().toString());
                intent.putExtra("User_name", userName);
                startActivity(intent);
            }
        });

    }

  /*  private void req_pass() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Passowd");
        final EditText editText = new EditText(this);
        builder.setView(editText);
        builder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                pass = editText.getText().toString();
            }
        });
        builder.show();
    }
*/
    //username dialog box
    private void request_name() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter your name");
        final EditText editText = new EditText(this);
        builder.setView(editText);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                userName = editText.getText().toString().trim();
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(MainActivity.this, "Enter a valid name", Toast.LENGTH_SHORT).show();
                    request_name();
                }

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                Toast.makeText(MainActivity.this, "Enter a valid name", Toast.LENGTH_SHORT).show();
                request_name();
            }
        });
        builder.show();
    }
}
