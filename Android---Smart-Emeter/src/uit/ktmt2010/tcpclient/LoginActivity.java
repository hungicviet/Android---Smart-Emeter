package uit.ktmt2010.tcpclient;


import uit.ktmt2010.utils.HD_ConstantsManager;
import uit.ktmt2010.utils.HD_DialogManager;
import uit.ktmt2010.utils.HD_WifiManager;
import android.R.fraction;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends Activity {
	
	private Button btn_Login;
	private EditText edt_Pass;
	private CheckBox chk_save;
	private HD_WifiManager mHD_WifiManager = null;
	private HD_DialogManager mHD_DialogManager = null;
	private HD_ConstantsManager mHD_ConstantsManager = null;
	
	public static final String PASSWORD = "KTMT2010";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		/*ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setDisplayUseLogoEnabled(false);*/
		
		btn_Login = (Button)findViewById(R.id.button_login);
		edt_Pass  = (EditText)findViewById(R.id.editText_matkhau);
		chk_save = (CheckBox)findViewById(R.id.checkBox_luu_matkhau);
		
		mHD_WifiManager = new HD_WifiManager(LoginActivity.this);
		mHD_DialogManager = new HD_DialogManager(LoginActivity.this);
		mHD_ConstantsManager = new HD_ConstantsManager();
		
		restorePassword();
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		btn_Login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (edt_Pass.getText().toString().equals(PASSWORD))
				{
					/*Intent mIntent = new Intent(LoginActivity.this, MainActivity.class);
					Bundle bundle = new Bundle();
					String pass = edt_Pass.getText().toString();
					String network_type = "local";*/
					
					AlertDialog.Builder mDialog =  new Builder(LoginActivity.this);
					mDialog.setTitle("CHỌN KIỂU KẾT NỐI");
					mDialog.setNegativeButton("Mạng gia đình", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent mIntent = new Intent(LoginActivity.this, MainActivity.class);
							Bundle bundle = new Bundle();
							bundle.putString("network", "local");
							bundle.putString("password", edt_Pass.getText().toString());
							mIntent.putExtra("MyPackage", bundle);
							startActivity(mIntent);
						}
					});
					
					mDialog.setPositiveButton("Mạng khác", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent mIntent = new Intent(LoginActivity.this, MainActivity.class);
							Bundle bundle = new Bundle();
							bundle.putString("network", "other");
							bundle.putString("password", edt_Pass.getText().toString());
							mIntent.putExtra("MyPackage", bundle);
							startActivity(mIntent);
						}
					});
					mDialog.show();
					savePassword();
					//LoginActivity.this.finish();
					
					/*if(mHD_WifiManager.isWifiConnected()==false){
						startActivity(mIntent);
						LoginActivity.this.finish();
					}
					*/
					/*else
					{
						AlertDialog.Builder mDialog =  new Builder(LoginActivity.this);
						mDialog.setTitle("KẾT NỐI WIFI");
						mDialog.setMessage("Vui lòng kiểm tra kết nối WIFI");
						mDialog.setNegativeButton("OK", null);
						mDialog.setPositiveButton("Cài đặt WIFI", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								Intent mWifiIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
								startActivity(mWifiIntent);
							}
						});
						
						mDialog.setPositiveButton("Bật WIFI", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								mHD_WifiManager.OpenWifi();
							}
						});
						mDialog.show();
					}*/
				}
				else
				{
					mHD_DialogManager.showAlertDialog(mHD_ConstantsManager.DLG_PASSWORD);
				}
			}
		});
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		savePassword();
	}
	
	
	public void onChechBoxClicked(View v){
		if(v.getId()==R.id.checkBox_luu_matkhau){
			savePassword();
		}
	}
	
	 public void savePassword() {
	    	SharedPreferences prePass = getSharedPreferences("m_matkhau", MODE_PRIVATE);
	    	SharedPreferences.Editor editor = prePass.edit();
	    	String pw = edt_Pass.getText().toString();
	    	if(chk_save.isChecked())
	    	{
	    		editor.putString("Password", pw);
	    		editor.putBoolean("checked", chk_save.isChecked());
	    	}
	    	else
	    		editor.clear();
	    	editor.commit();
		}
	 
	 public void restorePassword(){
	    	SharedPreferences prePass = getSharedPreferences("m_matkhau", MODE_PRIVATE);
	    	boolean bchk = prePass.getBoolean("checked", false);
	    	if(bchk)
	    	{
	    		edt_Pass.setText(prePass.getString("Password", ""));
	    	}
	    	chk_save.setChecked(bchk);
	    }
	 
}
