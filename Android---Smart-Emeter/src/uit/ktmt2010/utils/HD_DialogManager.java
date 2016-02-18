package uit.ktmt2010.utils;
import android.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.sax.StartElementListener;
import android.app.Activity;

public class HD_DialogManager implements OnClickListener {
	
	private Context mContext = null;
	
	private AlertDialog.Builder mAlertDialog = null;
	
	public HD_DialogManager(Context mContext) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
	}
	
	public void showAlertDialog (int dialogType){
		
		mAlertDialog = new AlertDialog.Builder(mContext);
		switch (dialogType) {
		case HD_ConstantsManager.DLG_NO_WIFI_AVAILABLE:
			mAlertDialog.setTitle("KẾT NỐI");
			mAlertDialog.setMessage("Vui lòng kiểm tra kết nối wifi");
			break;
			
		case HD_ConstantsManager.DLG_PASSWORD:
			mAlertDialog.setTitle("ĐĂNG NHẬP KHÔNG THÀNH CÔNG");
			mAlertDialog.setMessage("Vui lòng kiểm tra mật khẩu");
			break;
		case HD_ConstantsManager.DLG_NULL_SERVER:
			mAlertDialog.setTitle("THÔNG TIN SERVER");
			mAlertDialog.setMessage("Vui lòng kiểm tra IP và PORT");
			break;
		}
		
		mAlertDialog.setPositiveButton("OK", this);
		mAlertDialog.show();
	}
	
	@Override
	public void onClick(DialogInterface dialog, int dialogType)
	{
		if(dialogType == DialogInterface.BUTTON_POSITIVE)
		{
			dialog.dismiss();
		}
	}
}
