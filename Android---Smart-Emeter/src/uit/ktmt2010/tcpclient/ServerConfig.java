package uit.ktmt2010.tcpclient;

import uit.ktmt2010.utils.HD_DialogManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
public class ServerConfig extends AlertDialog.Builder
{
	private HD_DialogManager mHDialogManager = null;
	public interface OnConfigServer{
		public void ConfigServerEvent(String IPAddr, int Port, boolean isSave);
	}
	
	public ServerConfig(Context context, final OnConfigServer listener){
		super(context);
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.activity_server_config, null);
		final EditText IPAddrConfig = (EditText)view.findViewById(R.id.editText_configAddr);
		final EditText PortConfig = (EditText)view.findViewById(R.id.editText_configPort);
		final CheckBox ckbSave = (CheckBox)view.findViewById(R.id.checkBox_saveServer);
		//RadioGroup RG = (RadioGroup)view.findViewById(R.id.radioGroup_mang);
		RadioButton RB_local = (RadioButton)view.findViewById(R.id.radio_local);
		RadioButton RB_other = (RadioButton)view.findViewById(R.id.radio_other);
		
		
		
		IPAddrConfig.setRawInputType(Configuration.KEYBOARD_12KEY);
		PortConfig.setRawInputType(Configuration.KEYBOARD_12KEY);
		//RG.clearCheck();
		RB_other.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 IPAddrConfig.setText("testcc3000.ddns.net");
				 PortConfig.setText("2010");
				 IPAddrConfig.setEnabled(false);
			}
		});
		
		RB_local.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(IPAddrConfig.getText().toString().equals("testcc3000.ddns.net"))
					IPAddrConfig.setText("");
				 PortConfig.setText("2010");
				 IPAddrConfig.setEnabled(true);
			}
		});
		setView(view);
		setPositiveButton("OK",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				String IP = IPAddrConfig.getText().toString();
				int PORT = Integer.parseInt(PortConfig.getText().toString());
				boolean isSaveServer;
				if(ckbSave.isChecked()) 
					isSaveServer = true;
				else
					isSaveServer = false;
				listener.ConfigServerEvent(IP, PORT, isSaveServer);
			}
		});
		
	}
}
