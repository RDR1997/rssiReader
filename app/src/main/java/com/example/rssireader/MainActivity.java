package com.example.rssireader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MainActivity<x> extends AppCompatActivity {


	double d1, d2, d3;

	private static String TAG = "_RSSI";
	private static int INTERVAL_FOR_RSSI = 15000; //in mSec
	protected WifiManager mWifiManager;
	private TextView ssidTextView;
	private TextView rssiTextView;
	private TextView rssiTextView2;
	private TextView rssiTextView3;
	private TextView distanceTextView;
	private TextView distanceTextView2;
	private TextView distanceTextView3;
	private TextView xTextView;
	private TextView yTextView;
	private Button startButton;
	private Handler wifiScanHandler;
	private MyBroadcastReceiver myBroadcastReceiver;
	private IntentFilter intentFilter;
	private File outputFile;
	private FileOutputStream foo;
	private Runnable wifiScan = new Runnable() {
		@Override
		public void run() {
			if (startButton.getText().equals("Stop")) {
				scanForWifi();
				wifiScanHandler.postDelayed(wifiScan, INTERVAL_FOR_RSSI);
			}
		}
	};

	@SuppressLint("MissingInflatedId")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		String requiredpermissions[] = {
				Manifest.permission.ACCESS_COARSE_LOCATION,
				Manifest.permission.WRITE_EXTERNAL_STORAGE};
		permissions(this, requiredpermissions);

		ssidTextView = (TextView) findViewById(R.id.setSSID);
		rssiTextView = (TextView) findViewById(R.id.textViewRssi);
		rssiTextView2 = (TextView) findViewById(R.id.textViewRssi2);
		rssiTextView3 = (TextView) findViewById(R.id.textViewRssi3);
		distanceTextView = (TextView) findViewById(R.id.textViewDistance);
		distanceTextView2 = (TextView) findViewById(R.id.textViewDistance2);
		distanceTextView3 = (TextView) findViewById(R.id.textViewDistance3);
		xTextView = (TextView) findViewById(R.id.textViewX);
		yTextView = (TextView) findViewById(R.id.textViewY);
		startButton = (Button) findViewById(R.id.start_stop);
		wifiScanHandler = new Handler();
		intentFilter = new IntentFilter();
		intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

		myBroadcastReceiver = new MyBroadcastReceiver(this);
		registerReceiver(myBroadcastReceiver, intentFilter);

		outputFile = new File(
				"/storage/self/primary",
				"Rssi" + new Random().nextInt(100));
		if (!outputFile.exists()) {
			try {
				outputFile.createNewFile();
				Log.i(TAG,"File Created");
			} catch (IOException e) {
				e.printStackTrace();
				Log.i(TAG, "File not Created");
			}
		}
	}


	public void start_stop(View v) throws IOException {
		Log.i(TAG, String.valueOf(ssidTextView.getText()));
		if (startButton.getText().equals("Start")) {
			scanForWifi();
			wifiScanHandler.postDelayed(wifiScan, INTERVAL_FOR_RSSI);
			startButton.setText("Stop");

			foo = new FileOutputStream(outputFile);

		} else if (startButton.getText().equals("Stop")) {
			startButton.setText("Start");
			rssiTextView.setText("0");
			rssiTextView2.setText("0");
			rssiTextView3.setText("0");
			distanceTextView.setText("0");
			distanceTextView2.setText("0");
			distanceTextView3.setText("0");
			xTextView.setText("0");
			yTextView.setText("0");


			foo.close();
		}
	}

	public void scanForWifi() {
		mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		mWifiManager.startScan();

	}

	public void wifiScanSuccess() throws IOException {
		List<ScanResult> mWifiList = mWifiManager.getScanResults();
		boolean ssidfound = false;
		//String ssid = String.valueOf(ssidTextView.getText());
		String ssid = getString(R.string.TheLastKnght97);
		String ssid2 = getString(R.string.Pandula_B);
		String ssid3 = getString(R.string.sltgo);
		for (ScanResult scanResult : mWifiList) {
			if (scanResult.SSID.equals(ssid)) {
				ssidfound = true;
				rssiTextView.setText(String.valueOf(scanResult.level));
				if (scanResult.level <= -30 && scanResult.level >= -35){
					distanceTextView.setText("1");
					d1 = 1;
				}
				if (scanResult.level < -35 && scanResult.level >= -40) {
					distanceTextView.setText("2");
					d1 = 2;
				}
				if (scanResult.level < -40 && scanResult.level >= -45) {
					distanceTextView.setText("3");
					d1 = 3;
				}
				if (scanResult.level < -45 && scanResult.level >= -50) {
					distanceTextView.setText("4");
					d1 = 4;
				}
				if (scanResult.level < -50 && scanResult.level >= -55) {
					distanceTextView.setText("5");
					d1 = 5;
				}
				if (scanResult.level < -60 && scanResult.level >= -65){
					distanceTextView.setText("6");
					d1 = 6;
				}
				if (scanResult.level < -65 && scanResult.level >= -70) {
					distanceTextView.setText("7");
					d1 = 7;
				}
				if (scanResult.level < -75 && scanResult.level >= -80) {
					distanceTextView.setText("8");
					d1 = 8;
				}
				if (scanResult.level < -80) {
					distanceTextView.setText("9");
					d1 = 9;
				}


				DateFormat df = new SimpleDateFormat("yyyy-MM-dd G 'at' HH:mm:ss.SSSZ");
				String date = df.format(Calendar.getInstance().getTime());

				String data = date + " " + scanResult.SSID + " " + scanResult.level + "\n";

				foo.write(data.getBytes());

				Log.i(TAG, data);
			}

			if (scanResult.SSID.equals(ssid2)) {
				ssidfound = true;
				rssiTextView2.setText(String.valueOf(scanResult.level));

				if (scanResult.level <= -50 && scanResult.level >= -54){
					distanceTextView2.setText("1");
					d2 = 1;
				}
				if (scanResult.level < -54 && scanResult.level >= -57.3333) {
					distanceTextView2.setText("2");
					d2 = 2;
				}
				if (scanResult.level < -57.3333 && scanResult.level >= -60.6667) {
					distanceTextView2.setText("3");
					d2 = 3;
				}
				if (scanResult.level < -60.6667 && scanResult.level >= -63.6667) {
					distanceTextView2.setText("4");
					d2 = 4;
				}
				if (scanResult.level < -63.6667 && scanResult.level >= -66.6667) {
					distanceTextView2.setText("5");
					d2 = 5;
				}
				if (scanResult.level < -66.6667 && scanResult.level >= -68){
					distanceTextView2.setText("6");
					d2 = 6;
				}
				if (scanResult.level < -68 && scanResult.level >= -69.3333) {
					distanceTextView2.setText("7");
					d2 = 7;
				}
				if (scanResult.level < -69.3333 && scanResult.level >= -75) {
					distanceTextView2.setText("8");
					d2 = 8;
				}
				if (scanResult.level < -75) {
					distanceTextView2.setText("9");
					d2 = 9;
				}

				DateFormat df = new SimpleDateFormat("yyyy-MM-dd G 'at' HH:mm:ss.SSSZ");
				String date = df.format(Calendar.getInstance().getTime());

				String data = date + " " + scanResult.SSID + " " + scanResult.level + "\n";

				foo.write(data.getBytes());

				Log.i(TAG, data);
			}

			if (scanResult.SSID.equals(ssid3)) {
				ssidfound = true;
				rssiTextView3.setText(String.valueOf(scanResult.level));

				if (scanResult.level <= -50 && scanResult.level >= -54){
					distanceTextView3.setText("1");
					d3 = 1;
				}
				if (scanResult.level < -54 && scanResult.level >= -57.3333) {
					distanceTextView3.setText("2");
					d3 = 2;
				}
				if (scanResult.level < -57.3333 && scanResult.level >= -60.6667) {
					distanceTextView3.setText("3");
					d3 = 3;
				}
				if (scanResult.level < -60.6667 && scanResult.level >= -63.6667) {
					distanceTextView3.setText("4");
					d3 = 4;
				}
				if (scanResult.level < -63.6667 && scanResult.level >= -66.6667) {
					distanceTextView3.setText("5");
					d3 = 5;
				}
				if (scanResult.level < -66.6667 && scanResult.level >= -68){
					distanceTextView3.setText("6");
					d3 = 6;
				}
				if (scanResult.level < -68 && scanResult.level >= -69.3333) {
					distanceTextView3.setText("7");
					d3 = 7;
				}
				if (scanResult.level < -69.3333 && scanResult.level >= -75) {
					distanceTextView3.setText("8");
					d3 = 8;
				}
				if (scanResult.level < -75) {
					distanceTextView3.setText("9");
					d3 = 9;
				}

				DateFormat df = new SimpleDateFormat("yyyy-MM-dd G 'at' HH:mm:ss.SSSZ");
				String date = df.format(Calendar.getInstance().getTime());

				String data = date + " " + scanResult.SSID + " " + scanResult.level + "\n";

				foo.write(data.getBytes());

				Log.i(TAG, data);
			}

			double x1=0,x2=4,x3=2,y1=0,y2=0,y3=6;
			double A = 2*((x3-x1)*(y3-y2)-(y3-y1)*(x3-x2));

			int x = (int) ((y3-y2)*((d1*d1 - d3*d3 - x1*x1 +x3*x3 - y1*y1 + y3*y3) - (y3-y1)*(d2*d2 - d3*d3 - x2*x2 +x3*x3 - y2*y2 + y3*y3)) / A);
			int y = (int) ((x3-x1)*((d2*d2 - d3*d3 - x2*x2 +x3*x3 - y2*y2 + y3*y3) - (x3-x2)*(d1*d1 - d3*d3 - x1*x1 +x3*x3 - y1*y1 + y3*y3)) / A);

			xTextView.setText(String.valueOf(x));
			yTextView.setText(String.valueOf(y));


		}
		if (ssidfound == false) {
			startButton.setText("Start");
			rssiTextView.setText("0");
			rssiTextView2.setText("0");
			Log.i(TAG, ssid + " Not Found");

			foo.close();

			Toast.makeText(this, "SSID not Found", Toast.LENGTH_SHORT).show();
		}


	}



	public void permissions(MainActivity thisActivity, String[] requiredPermission) {
		// Here, thisActivity is the current activity
		LinkedList<String> premissionList = new LinkedList<>();
		for (int i = 0; i < requiredPermission.length; i++) {
			if (ContextCompat.checkSelfPermission(thisActivity,
					requiredPermission[i])
					!= PackageManager.PERMISSION_GRANTED) {
				premissionList.add(requiredPermission[i]);
			}
		}
		if (premissionList.size() != 0) {

			String[] temp = new String[premissionList.size()];
			premissionList.toArray(temp);
			ActivityCompat.requestPermissions(thisActivity,
					temp,
					111);
		}
	}


	@Override
	public void onRequestPermissionsResult(int requestCode,
	   String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case 111: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// permission was granted, yay! Do the
					// contacts-related task you need to do.
				} else {
					// permission denied, boo! Disable the
					// functionality that depends on this permission.
				}
				return;
			}


			// other 'case' lines to check for other
			// permissions this app might request.
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(myBroadcastReceiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(myBroadcastReceiver, intentFilter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (outputFile.exists()) {
			if (outputFile.length() == 0) {
				outputFile.delete();

			}
		}
	}



	}

class MyBroadcastReceiver extends BroadcastReceiver {

	private MainActivity mActivity;

	public MyBroadcastReceiver(MainActivity mActivity) {
		super();
		this.mActivity = mActivity;
	}

	@Override
	public void onReceive (Context context, Intent intent) {
		String key = intent.getAction();
		Log.i("_RSSI", key);
		if (mActivity.mWifiManager != null) {
			if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(key)) {
				boolean success = intent.getBooleanExtra(
						WifiManager.EXTRA_RESULTS_UPDATED, false);
				if (success) {
					Log.i("_RSSI", "Sucess");
					try {
						mActivity.wifiScanSuccess();
					} catch (IOException e) {
						e.printStackTrace();
					}

					}
				}
			}
		}
	}
