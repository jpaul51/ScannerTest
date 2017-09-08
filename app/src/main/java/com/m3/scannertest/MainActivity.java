package com.m3.scannertest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.m3.scannerlib.Barcode;
import com.m3.scannerlib.BarcodeListener;
import com.m3.scannerlib.BarcodeManager;
import com.m3.scannerlib.Barcode.Symbology;


public class MainActivity extends Activity implements OnClickListener {

	// lib
	private Barcode mBarcode = null;
	private BarcodeListener mListener = null;
	private BarcodeManager mManager = null;
	private Symbology mSymbology = null;
	
	//ui
	private TextView mTvResult = null;
	private EditText edSymNum = null;
	private EditText edValNum = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mBarcode = new Barcode(this);
		mManager = new BarcodeManager(this);
		mSymbology = mBarcode.getSymbologyInstance();
		mBarcode.setScanner(true);
		
		mainScreen();
		
		mListener = new BarcodeListener() {
			
			@Override
			public void onBarcode(String strBarcode) {
				Log.i("ScannerTest","result="+strBarcode);
				mTvResult.setText(strBarcode);				
			}

			@Override
			public void onGetSymbology(int nSymbol, int nVal) {
//				Log.i("ScannerTest - onGetSymbology","result="+nSymbol + ", "+ nVal);
				edSymNum.setText(Integer.toString(nSymbol));	
				edValNum.setText(Integer.toString(nVal));			
				
			}
			
			
		};

		mManager.addListener(mListener);
		
	}

	@Override
	public void onPause() {
		try {
		mManager.removeListener(mListener);
		mManager.dismiss();
		mBarcode.setScanner(false);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG);
		}
		super.onPause();
	}

	@Override
	protected void onStop() {
		try {
			mManager.removeListener(mListener);
			mManager.dismiss();
			mBarcode.setScanner(false);
		}catch (Exception e)
		{
			e.printStackTrace();
			Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG);
		}
		super.onStop();
	}

	@Override
	protected void onDestroy() {

		mManager.removeListener(mListener);
		mManager.dismiss();
		mBarcode.setScanner(false);

		super.onDestroy();
	}

	protected void mainScreen()
	{
		Button btnStart = (Button)findViewById(R.id.startread);
		btnStart.setOnClickListener(this);        
		Button btnStop = (Button)findViewById(R.id.stopread);
		btnStop.setOnClickListener(this);
		Button btnGetSym = (Button)findViewById(R.id.buttonGet);
		btnGetSym.setOnClickListener(mGetParamListener);
		Button btnSetSym = (Button)findViewById(R.id.buttonSet);
		btnSetSym.setOnClickListener(mSetParamListener);
		
		mTvResult = (TextView)findViewById(R.id.scanresult);
		edSymNum = (EditText)findViewById(R.id.editPnum);
		edValNum = (EditText)findViewById(R.id.editPval);
	}
	

	@Override
	public void onClick(View vw) {
		int id = vw.getId();

		if(id == R.id.startread){
			mBarcode.scanStart();
		}else if(id == R.id.stopread){
			mBarcode.scanDispose();
		}
		
	}


	// ------------------------------------------------------
	// callback Get Param for button press
	OnClickListener mGetParamListener = new OnClickListener()
	{
		public void onClick(View v)
		{
			getParam();
		}
	};

	// ------------------------------------------------------
	// callback for Set Param button press
	OnClickListener mSetParamListener = new OnClickListener()
	{
		public void onClick(View v)
		{
			setParam();
		}
	};
	// ----------------------------------------
	private void getParam()
	{	

		// get param #
		String s = edSymNum.getText().toString();
				
		try
		{
			int nValue = mSymbology.getSymbology(Integer.parseInt(s));
			
			// edValNum.setText(Integer.toString(nValue));
			
		}
		catch (NumberFormatException nx)
		{
			nx.printStackTrace();
		}
	}

	// ----------------------------------------
	private void setParam()
	{
		// get param #
		String sn = edSymNum.getText().toString();
		String sv = edValNum.getText().toString();
		try
		{
			int num = Integer.parseInt(sn);
			int val = Integer.parseInt(sv);
				
			mSymbology.setSymbology(num,  val);
			
			
		}
		catch (NumberFormatException nx)
		{
			nx.printStackTrace();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
