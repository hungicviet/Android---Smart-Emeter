package uit.ktmt2010.tcpclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Formatter;

import static java.lang.System.out;
import android.R.integer;
import android.support.v7.app.ActionBarActivity;
import android.text.Layout;
import android.media.audiofx.BassBoost.Settings;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.util.Log;
import uit.ktmt2010.tcpclient.*;
import uit.ktmt2010.utils.HD_ConstantsManager;
import uit.ktmt2010.utils.HD_DialogManager;
import uit.ktmt2010.utils.HD_WifiManager;



public class MainActivity extends ActionBarActivity {
	
	private HD_WifiManager mHD_WifiManager = null;
	private HD_DialogManager mHD_DialogManager = null;
	private HD_ConstantsManager mHD_ConstantsManager = null;
	
	TextView textResponse, textIP, textPort, textVol, textAmpe, textW, textWh, textHz;
	EditText editTextAddr, editTextPort, editTextPass, editOverA;
	ToggleButton tglConnect, tglControlPower, tglControlDv1, tglControlDv2;
	Handler updateDataHandler, updateConnectHandler, updateStateHandler;
	CheckBox checkBoxPass;
	View layout_voltage;
	int overA=5000;
	private Socket msocket, msocketrcv;
	public String SERVERIP;
	public int SERVERPORT, SERVERPORTRCV;
	public String PASSWORD;
	public String network_type;
	public boolean isSaveServer = false;
	
	String tag = "TEST MODULE";
/*	 private static final String SERVERIP = "192.168.3.12";
	 private static final int SERVERPORT = 6666;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        
       
        mHD_WifiManager = new HD_WifiManager(MainActivity.this);
        mHD_DialogManager = new HD_DialogManager(MainActivity.this);
        mHD_ConstantsManager = new HD_ConstantsManager();
        
        textResponse = (TextView)findViewById(R.id.txt_response);
        textIP = (TextView)findViewById(R.id.textView_IP);
        textPort = (TextView)findViewById(R.id.textView_Port);
        textVol = (TextView)findViewById(R.id.value_vol);
        textAmpe = (TextView)findViewById(R.id.value_current);
        textW = (TextView)findViewById(R.id.value_power);
        textWh = (TextView)findViewById(R.id.value_kWh);
        textHz = (TextView)findViewById(R.id.value_Hz);
        updateDataHandler = new Handler();
        updateConnectHandler = new Handler();
        updateStateHandler =  new Handler();
        tglConnect = (ToggleButton)findViewById(R.id.tgl_Connect);
        tglControlPower = (ToggleButton)findViewById(R.id.tgl_Control_On_Off);
        tglControlDv1 = (ToggleButton)findViewById(R.id.tgl_dv1);
        tglControlDv2 = (ToggleButton)findViewById(R.id.tgl_dv2);
        
        layout_voltage = (View)findViewById(R.id.layout_V);
        
        tglConnect.setOnCheckedChangeListener(tgl_event);
        tglControlPower.setOnCheckedChangeListener(tgl_Power_event);
        tglControlDv1.setOnCheckedChangeListener(tgl_ctrl1_event);
        tglControlDv2.setOnCheckedChangeListener(tgl_ctrl2_event);
        
        layout_voltage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent test =  new Intent(MainActivity.this, LoginActivity.class);
				startActivity(test);
				
			}
		});
        
        
       
        
        Intent LoginIntent = getIntent();
        Bundle packageLogin = LoginIntent.getBundleExtra("MyPackage");
        PASSWORD = packageLogin.getString("password");
        network_type = packageLogin.getString("network");
        if(network_type.equals("local"))
        	restoringPreferences();
        else
        	if(network_type.equals("other"))
        {
        	textIP.setText("IP: testcc3000.ddns.net");
			textPort.setText("PORT: 2010");
			SERVERIP = "testcc3000.ddns.net";
			SERVERPORT = 2010;
        }
        
    }
    @Override
    protected void onPause() {
    	super.onPause();
    	if(network_type.equals("local"))
    		savingPreferences();
		
	}
    
    @Override
    protected void onStop(){
    	super.onStop();
    	SendtoCC3K("DIST", msocket);
    }
    
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	SendtoCC3K("DIST", msocket);
    }
    
	
	
	
	
	OnCheckedChangeListener tgl_event = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
			
			if(network_type.equals("local") && mHD_WifiManager.isWifiConnected()==false){
				// TODO Auto-generated method stub
				//restoringPreferences();
				if(isChecked){
					AlertDialog.Builder mDialog =  new Builder(MainActivity.this);
					mDialog.setTitle("KẾT NỐI WIFI");
					mDialog.setMessage("Vui lòng kiểm tra kết nối WIFI");
					mDialog.setNegativeButton("OK", null);
					mDialog.setPositiveButton("Bật WIFI", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							mHD_WifiManager.OpenWifi();
						}
					});
					mDialog.show();
					tglConnect.setChecked(false);
				}
			}
			
			if(network_type.equals("other")&& mHD_WifiManager.isWifiConnected()==false && mHD_WifiManager.isMobileConnected() ==false){
				// TODO Auto-generated method stub
				/*textIP.setText("IP: testcc3000.ddns.net");
				textPort.setText("PORT: 2010");
				SERVERIP = "testcc3000.ddns.net";
				SERVERPORT = 2010;*/
				if(isChecked){
					AlertDialog.Builder mDialog =  new Builder(MainActivity.this);
					mDialog.setTitle("KẾT NỐI");
					mDialog.setMessage("Vui lòng bật 3G hoặc WIFI");
					mDialog.setNegativeButton("OK",null);
					mDialog.show();
					tglConnect.setChecked(false);
				}
			}
			
			if(isChecked)
			{
				new Thread(new ClientThread()).start();
			}
			else
				SendtoCC3K("DIST", msocket);}
				
		
	};
	
	OnCheckedChangeListener tgl_Power_event = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
			// TODO Auto-generated method stub
			if(isChecked)
				SendtoCC3K("PwrN", msocket);
			else
				SendtoCC3K("PwrF", msocket);
				
		}
	};
	
	OnCheckedChangeListener tgl_ctrl1_event = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
			// TODO Auto-generated method stub
			if(isChecked) 
				SendtoCC3K("ctrlD1ON", msocket);
			else
				SendtoCC3K("ctrlD1OF", msocket);
			
		}
	};
	
	OnCheckedChangeListener tgl_ctrl2_event = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
			// TODO Auto-generated method stub
			if(isChecked) 
				SendtoCC3K("ctrlD2ON", msocket);
			else
				SendtoCC3K("ctrlD2OF", msocket);
			
		}
	};
	
	
	
	
	class ClientThread implements Runnable{
		@Override
		public void run() {
			char[] buffer = new char[50];
			try{
				InetAddress ServerAddress = InetAddress.getByName(SERVERIP);
				msocket = new Socket(ServerAddress, SERVERPORT);
				
				while(!Thread.currentThread().isInterrupted()){
					BufferedReader input = new BufferedReader(new InputStreamReader(msocket.getInputStream()));
					input.read(buffer,0,50);
					String data = new String(buffer);
					String arr[] = data.split("%");
					if(arr[2].equals("EDATA"))		
						updateDataHandler.post(new update_Data(arr[3], arr[4], arr[5], arr[6], arr[7]));
					if(arr[2].equals("CNNCT"))
						updateConnectHandler.post(new update_Connect(arr[3]));
					if(arr[2].equals("STATE"))
						updateStateHandler.post(new update_State(arr[3], arr[4], arr[5]));
				}
			}
			catch(UnknownHostException e){
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
		
	class update_Data implements Runnable{
		//private String msg;
		private String mV, mA, mW, mWh, mHz;
		private String txt_V, txt_A, txt_W, txt_Wh, txt_Hz;
		int val_V, val_A, val_W, val_Wh, val_Hz;
		public update_Data(String mVol, String mAmpe, String mWatt, String mWattH, String mFreq) {
			// TODO Auto-generated constructor stub
			mV = mVol.trim();
			mA = mAmpe.trim();
			mW = mWatt.trim();
			mWh = mWattH.trim();
			mHz = mFreq.trim();
			
			val_V = Integer.parseInt(mV);
			val_A = Integer.parseInt(mA);
			val_W = Integer.parseInt(mW);
			val_Wh= Integer.parseInt(mWh);
			val_Hz= Integer.parseInt(mHz);
			
			DecimalFormat fm = new DecimalFormat("0.000");
			
			/*if(val_A > overA) SendtoCC3K("PwrF", msocket);
			else
				if(val_A < overA - 100) SendtoCC3K("PwrN", msocket);*/
			float fvol = (float)val_V/100;
			//txt_V = String.valueOf(fvol);
			txt_V = fm.format(fvol);
			
			float fcur = (float)val_A/1000;
			//txt_A = String.valueOf(fcur);
			txt_A = fm.format(fcur);
			
			float fcap = (float)val_W/1000;
			//txt_W = String.valueOf(fcap);
			txt_W = fm.format(fcap);
			
			float fkwh = (float)val_Wh/1000;
			//txt_Wh = String.valueOf(fkwh);
			txt_Wh = fm.format(fkwh);
			
			float ffre = (float)val_Hz/100;
			txt_Hz = fm.format(ffre);
			
		}
		@Override
		public void run() {
			//textResponse.setText("Đang gửi dữ liệu");
			textVol.setText(txt_V);
			textAmpe.setText(txt_A);
			textW.setText(txt_W);
			textWh.setText(txt_Wh);
			textHz.setText(txt_Hz);
		}
	}
	
	class update_Connect implements Runnable{
		private String mstate;
		private String text, pass;
		private boolean isSavepass;
		private int flag = 0;
		private int flagtime=0;
		private String strdate;
		public update_Connect(String state) {
			// TODO Auto-generated constructor stub
			mstate = state;
			if(mstate.equals("PASS")) {
				text = "Vui lòng nhập mật khẩu!";
				flag = 1;
			}
			else
				if(mstate.equals("CRCT")) {
					text = "Kết nối thành công";
					Calendar date = Calendar.getInstance();
					SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
					strdate = sdf.format(date.getTime());
					flagtime = 1;
					//SendtoCC3K("TIME" + strdate, msocket);
					
				}
				else
					if(mstate.equals("DIST")) text = "Đã ngắt kết nối";
			
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			textResponse.setText(text);
			if(flag == 1)
			{
				flag = 0;
				SendtoCC3K("PASS"+PASSWORD.trim(), msocket);
			}
			if(flagtime==1)
			{
				flagtime = 0;
				SendtoCC3K("TIME" + strdate, msocket);
			}
		}
	}
	
	class update_State implements Runnable{
		private String mCTL, mDV1, mDV2;
		private boolean bCTL, bDV1, bDV2;
		private String txtCTL, txtDV1, txtxDV2;
		
		public update_State(String CTL, String DV1, String DV2) {
			// TODO Auto-generated constructor stub
			mCTL = CTL;
			mDV1 = DV1;
			mDV2 = DV2;
			if(mCTL.equals("CTL_1")){
				txtCTL = "Nguồn đã bật";
				bCTL = true;
			}
			if(mCTL.equals("CTL_0")){
				txtCTL = "Nguồn đã tắt";
				bCTL = false;
			}
			
			if(mDV1.equals("DV1_1")){
				txtDV1 = "TB1 đã bật";
				bDV1 = true;
			}
			if(mDV1.equals("DV1_0")){
				txtDV1 = "TB1 đã tắt";
				bDV1 = false;
			}
			
			if(mDV2.equals("DV2_1")){
				txtxDV2 = "TB2 đã bật";
				bDV2 = true;
			}
			if(mDV2.equals("DV2_0")){
				txtxDV2 = "TB2 đã tắt";
				bDV2 = false;
			}
					
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			textResponse.setText(txtCTL + " " + txtDV1 + " " + txtxDV2);
			tglControlPower.setChecked(bCTL);
			tglControlDv1.setChecked(bDV1);
			tglControlDv2.setChecked(bDV2);
		}
	}
	


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.item_config_server) {
        	ServerConfig.OnConfigServer mServerConfig = new ServerConfig.OnConfigServer() {
        		@Override
        		public void ConfigServerEvent(String IPAddr, int Port, boolean isSave) {
        			// TODO Auto-generated method stub
        			SERVERIP = IPAddr;
        			SERVERPORT = Port;
        			isSaveServer = isSave;
        			textIP.setText("IP: " + SERVERIP);
        			textPort.setText("PORT: " + String.valueOf(SERVERPORT));
       		 	}
       		};
        	ServerConfig dialog = new ServerConfig(this, mServerConfig);
        	dialog.setTitle("Tùy chỉnh Server");
        	dialog.show();
            return true;
        }
        if(id == R.id.item_set_time){
        	Calendar date = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
			String strdate = sdf.format(date.getTime());
			SendtoCC3K("TIME" + strdate, msocket);
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void savingPreferences() {
		SharedPreferences pre = getSharedPreferences("my_data", MODE_PRIVATE);
		SharedPreferences.Editor editor = pre.edit();
		String IPAddress = SERVERIP;
		int Port = SERVERPORT;
		if(isSaveServer == true)
		{
			editor.putString("IP", IPAddress);
			editor.putInt("PORT", Port);
			editor.putBoolean("checked", isSaveServer);
		}
		else 
			editor.clear();
		
		editor.commit();
		
	}
    
    public void restoringPreferences() {
    	SharedPreferences pre = getSharedPreferences("my_data", MODE_PRIVATE);
    	SERVERIP = pre.getString("IP", "");
    	SERVERPORT = pre.getInt("PORT", 2010);
		isSaveServer = true;
		textIP.setText("IP: " + SERVERIP);
        textPort.setText("PORT: " + Integer.toString(SERVERPORT));
	}
    
    
    public void SendtoCC3K(String DATA, Socket sck) {
    	try{
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sck.getOutputStream())),true);
			out.println(DATA);
		}
		catch(UnknownHostException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
	}
}
