package com.metrafonic.nativeminecraftquery;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by mathias on 12/10/13.
 */
public class playerListActivity extends Activity
{
    public void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.playerlist);


        final Button refreshButton = (Button) findViewById(R.id.button);
        final TextView PlayerTextView= (TextView) findViewById(R.id.textViewlist);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String ip = extras.getString("ip");
            int port = extras.getInt("port");

        }


        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshButton.setClickable(false);
                final Handler updater1 = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        String text = (String)msg.obj;
                        try{
                            Bundle extras = getIntent().getExtras();
                            String ip = extras.getString("ip");
                            int port = extras.getInt("port");
                            MCQuery mcQuery = new MCQuery(ip, port);
                            String status;
                            QueryResponse response = mcQuery.fullStat();
                            ArrayList<String> players = response.getPlayerList();
                            int listSize = players.size();
                            PlayerTextView.setText("");
                            for (int i = 0; i<listSize; i++){
                                PlayerTextView.append(" - " + players.get(i) + "\n");
                            }

                        }catch (Exception e){
                            //final worker worker1 = new worker();
                            //final String trueorfalse = worker1.work();
                            PlayerTextView.setText("ERROR! go back and try Refresh Query");
                        };
                    }
                };
                String textToSend = /*inputCommand.getText().toString() + */"\n";
                //updater1.sendMessage(updater1.obtainMessage());
                Message msg = new Message();
                msg.obj = textToSend;
                updater1.sendMessage(msg);
                refreshButton.setClickable(true);

            }
        });
        refreshButton.callOnClick();

    }

}
