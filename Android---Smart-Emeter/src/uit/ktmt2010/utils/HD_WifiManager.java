/**
 * 
 */
package uit.ktmt2010.utils;

import java.lang.reflect.Method;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * @author Steven
 *
 */
public class HD_WifiManager {
	
	private WifiManager mWifiManager = null;
	
	private ConnectivityManager mConnectivityManager = null;
	
	private WifiInfo mWifiInfo = null;
	
	private Context mContext = null;
	
	private static final int BUILD_VERSION_JELLYBEAN=17;
	
	public HD_WifiManager(Context mcoContext) {
		// TODO Auto-generated constructor stub
		this.mContext = mcoContext;
		mWifiManager = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);
		mConnectivityManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		mWifiInfo = mWifiManager.getConnectionInfo();
	}
	
	public boolean isWifiConnected()
	{
		ConnectivityManager connManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(mWifi.isConnected())
			return true;
		else
		{
			//mWifiManager.setWifiEnabled(true);
			return false;
		}
	}
	public void OpenWifi()
	{
		if(isWifiConnected()==false) mWifiManager.setWifiEnabled(true);
	}
	
	public boolean isMobileConnected()
	{
		ConnectivityManager connManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mMobileDta = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if(mMobileDta.isConnected())
			return true;
		else
		{
			return false;
		}
	}
	/*
	public boolean isMobileConnected()
	{
		boolean mobileDataEnabled = false;
		ConnectivityManager mMobileDataManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		try{
			Class mClass = Class.forName(mMobileDataManager.getClass().getName());
			Method method = mClass.getDeclaredMethod("getMobileDataEnabled");
			method.setAccessible(true);
			mobileDataEnabled = (Bo)
		}
	}
	public void OpenMobileData()
	{
		if(isMobileConnected()==false) 
	}*/

}
