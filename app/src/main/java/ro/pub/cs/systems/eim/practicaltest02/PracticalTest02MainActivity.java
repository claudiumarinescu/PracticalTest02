package ro.pub.cs.systems.eim.practicaltest02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends AppCompatActivity {

    private EditText serverPort;
    private Button serverBtn;

    private Button clientBtn;
    private EditText clientURL;
    private TextView resultTextView;

    private ServerThread serverThread;
    private ClientThread clientThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serverPort = findViewById(R.id.serverPort);
        serverBtn = findViewById(R.id.serverBtn);

        clientBtn = findViewById(R.id.clientBtn);
        clientURL = findViewById(R.id.clientURL);
        resultTextView = findViewById(R.id.resultTextView);

        serverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String port = serverPort.getText().toString();
                if (port == null || port.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                    return;
                }
                serverThread = new ServerThread(Integer.parseInt(port));
                if (serverThread.getServerSocket() == null) {
                    Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                    return;
                }
                serverThread.start();

                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server started!", Toast.LENGTH_SHORT).show();
            }
        });


        clientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = clientURL.getText().toString();
                if (url == null || url.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] URL should be filled!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (serverThread == null || !serverThread.isAlive()) {
                    Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                    return;
                }

                clientThread = new ClientThread(
                        url,
                        serverThread.getPort(),
                        resultTextView);

                clientThread.start();
           }
        });

    }

    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}
