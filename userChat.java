package chat.chat.enduser;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;

public class userChat extends AppCompatActivity {

    private ListView menuList;
    private ArrayList<Integer> indexes;
    private final int chat = 1;
    private EditText message;
    private ImageButton send;
    private static boolean chatScreenAlive = false;
    private static boolean chatOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);

        initialize();
        onClick();

    }

    private void initialize(){

        menuList = (ListView) findViewById(R.id.messagesContainer);
        send     = (ImageButton) findViewById(R.id.chatSendButton);
        message  = (EditText) findViewById(R.id.messageEdit);

        initializeMenu();

        final long tenSeconds = (10 * 1000);

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {

                refreshMenu();

            }
        }, tenSeconds);

    }

    public void onResume(){

        super.onResume();
        chatScreenAlive = true;

    }

    public void onPause(){

        super.onPause();
        chatScreenAlive = false;

    }

    public void refreshMenu(){

        if(!chatScreenAlive) return;

        indexes = new ArrayList<Integer>();

        ArrayList<String> times    = new ArrayList<String>();
        ArrayList<String> messages = new ArrayList<String>();
        ArrayList<String> names    = new ArrayList<String>();
        ArrayList<Integer> types   = new ArrayList<Integer>();

        HashMap<Integer, ArrayList<String>> chatMessages = user.chatMessages(chat);
        TreeMap<Integer, ArrayList<String>> map = new TreeMap<Integer, ArrayList<String>>(chatMessages);

        for(TreeMap.Entry<Integer, ArrayList<String>> entry : map.entrySet()){

            times.add(entry.getValue().get(4));
            messages.add(entry.getValue().get(3));
            names.add(entry.getValue().get(2));
            int type = Integer.parseInt(entry.getValue().get(1)) == 0 ? 1 : 0 ;
            types.add(type);
            indexes.add(entry.getKey());
            if(Integer.parseInt(entry.getValue().get(0)) != user.ID()){
                user.setMessageSeen(entry.getKey());
            }

        }

        final mainMenuListMessage menuAdapter = new mainMenuListMessage(this,messages,times,names,types);

        menuList.setAdapter(menuAdapter);

        menuList.post(new Runnable() {
            @Override
            public void run() {
                menuList.setSelection(menuAdapter.getCount() - 1);
            }
        });

    }

    public void onClick(){

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String messageText = message.getText().toString();

                if(messageText.equals("") || messageText.equals(" ")) return;

                if(!user.sendMessage(chat, messageText)){
                    Toast.makeText(getBaseContext(), user.message(), Toast.LENGTH_LONG).show();
                    return;
                }

                initializeMenu();

                message.setText("");

            }
        });

    }

    public void close(){
        this.finish();
    }

}
