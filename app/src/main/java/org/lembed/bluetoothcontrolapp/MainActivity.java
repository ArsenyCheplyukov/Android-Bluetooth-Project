package org.lembed.bluetoothcontrolapp;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends Activity {
    private static final String TAG = "bluetooth2";
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSIONS_REQUEST_CODE = 11;

    private Button[] buttons = new Button[10];
    private TextView txtArduino;
    private Handler handler;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private ConnectedThread connectedThread;
    private String address = "98:D3:21:FC:86:C5";

    private final int RECEIVE_MESSAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler(msg -> {
            if (msg.what == RECEIVE_MESSAGE) {
                byte[] readBuf = (byte[]) msg.obj;
                String strIncom = new String(readBuf, 0, msg.arg1);

                // Process received message

                return true;
            }
            return false;
        });

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();

        for (int i = 0; i < 10; i++) {
            final int buttonNumber = i + 1;
            int buttonId = getResources().getIdentifier("button" + buttonNumber, "id", getPackageName());
            buttons[i] = findViewById(buttonId);
            buttons[i].setOnClickListener(view -> sendCommand(String.valueOf(buttonNumber)));
            registerForContextMenu(buttons[i]);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        connectBluetooth();
    }

    @Override
    public void onPause() {
        super.onPause();
        disconnectBluetooth();
    }

    private void connectBluetooth() {
        // Connect to Bluetooth device
        // Implement your connection logic here
    }

    private void disconnectBluetooth() {
        // Disconnect from Bluetooth device
        // Implement your disconnection logic here
    }

    private void sendCommand(String command) {
        if (connectedThread != null) {
            connectedThread.write(command);
        }
    }

    private void checkBTState() {
        if (bluetoothAdapter == null) {
            errorExit("Fatal Error", "Bluetooth is unsupported");
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    private void errorExit(String title, String message) {
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }

    private class ConnectedThread extends Thread {
        // Implement your ConnectedThread class here
        // ...

        public void write(String message) {
            // Implement write logic here
            // ...
        }

        public void cancel() {
            // Implement cancel logic here
            // ...
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.bluetooth_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.enable_bluetooth) {
            // Enable Bluetooth
            enableBluetooth();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void enableBluetooth() {
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            Toast.makeText(this, "Bluetooth is already enabled", Toast.LENGTH_SHORT).show();
        }
    }
}
