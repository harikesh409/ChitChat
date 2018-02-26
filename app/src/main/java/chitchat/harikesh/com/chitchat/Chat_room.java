package chitchat.harikesh.com.chitchat;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Chat_room extends AppCompatActivity {

    //Fields
    Button sendBtn;
    TextView receivedMsg;
    EditText sendMsg;

    //Database Reference
    DatabaseReference rootRoomName;

    //String fields
    String roomName;
    String userName;
    private String chatUserName;
    private String chatMessage;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        //Refernces for fields
        sendBtn = (Button) findViewById(R.id.sendMsgBtn);
        receivedMsg = (TextView) findViewById(R.id.receivedMsg);
        sendMsg = (EditText) findViewById(R.id.sendMsgEdit);

        //Get Intent Extras
        roomName = getIntent().getExtras().get("Room_name").toString();
        userName = getIntent().getExtras().get("User_name").toString();

        //Set title to room name
        setTitle(roomName);

        rootRoomName = FirebaseDatabase.getInstance().getReference().getRoot().child(roomName);

        //onClickListeners
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sendMsg.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Chat_room.this, "Enter some message!!", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference childRoot = rootRoomName.push();
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("name", userName);
                    map.put("message", sendMsg.getText().toString());
                    childRoot.updateChildren(map);
                    sendMsg.setText("");
                }
            }
        });

        rootRoomName.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                update_Message(dataSnapshot);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                update_Message(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Loading messages
    private void update_Message(DataSnapshot dataSnapshot) {

        chatUserName = (String) dataSnapshot.child("name").getValue();
        chatMessage = (String) dataSnapshot.child("message").getValue();

        receivedMsg.append(chatUserName + " : " + chatMessage + "\n\n");
    }
}
