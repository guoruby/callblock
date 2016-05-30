package com.guoruby.callblock;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PhoneReceiver extends BroadcastReceiver {
	String TAG = "PhoneReceiver";
    private static TelephonyManager manager;
    private static SharedPreferences sharedPreferences;
	@Override
	public void onReceive(Context context, Intent intent) {
        sharedPreferences = context.getSharedPreferences(MainActivity.ROLE_DATA, Context.MODE_PRIVATE);
		if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
			// 如果是去电（拨出）
		} else {
			 manager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
			// 设置一个监听器
			manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		}
	}
	
	PhoneStateListener listener = new PhoneStateListener() {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// state 当前状态 incomingNumber,貌似没有去电的API
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			//手机空闲了
			case TelephonyManager.CALL_STATE_IDLE:
				break;
				//电话被挂起
			case TelephonyManager.CALL_STATE_OFFHOOK:
				break;
				// 当电话呼入时
			case TelephonyManager.CALL_STATE_RINGING:
				Log.e(TAG, "来电号码是："+ incomingNumber);


                String role = sharedPreferences.getString(MainActivity.ROLE_KEY, "");
                 List<String> roleList = new ArrayList<>();
                if (!"".equals(role)) {
                    String[] strings = role.split(";");
                    Collections.addAll(roleList, strings);
                }
                // 如果该号码属于黑名单
                for (String roleKey: roleList){
                    if (incomingNumber.startsWith(roleKey)){
                        stopCall();
                        break;
                    }
                }
				break;
			}
		}
	};

	public void stopCall() {

        try {
            Method getITelephonyMethod = TelephonyManager.class
                    .getDeclaredMethod("getITelephony", (Class[]) null);
            getITelephonyMethod.setAccessible(true);
            ITelephony telephony = (ITelephony) getITelephonyMethod.invoke(manager,
                    (Object[]) null);
            // 拒接来电
            telephony.endCall();

        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}