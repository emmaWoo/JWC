package com.ichg.jwc.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMSReceiver extends BroadcastReceiver {

	public static final String GET_MESSAGE = "get_meg";

	private static final String queryString = "(?i)(JWC)";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		if (bundle == null) {
			return;
		}

		Object[] pdus = (Object[]) bundle.get("pdus");
		for (int i = 0; i < pdus.length; i++) {
			SmsMessage message = SmsMessage.createFromPdu((byte[]) pdus[i]);
			String msg = message.getMessageBody();

			Pattern pattern = Pattern.compile(queryString);
			Matcher matcher = pattern.matcher(msg);
			if (matcher.find()) {
				String body = message.getMessageBody().toString();
				context.sendBroadcast(new Intent("SmsMessage.intent.MAIN").putExtra(GET_MESSAGE, body));
			}
		}

	}

}