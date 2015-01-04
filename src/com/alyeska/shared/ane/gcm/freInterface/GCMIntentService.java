/*
 * Copyright 2014 Bree Alyeska.
 *
 * This file is part of GCM Native Air Extension, developed in conjunction with and distributed with iHAART.
 * Development of this ANE was greatly aided by the open source Google Cloud Messaging ANE project from Afterisk Inc.,
 * available here:
 * https://github.com/AfteriskInc/AirGCM
 * and here:
 * http://www.afterisk.com/2012/09/22/the-only-free-and-fully-functional-android-gcm-native-extension-for-adobe-air/
 *
 *
 * gcmANE and iHAART are free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later  version.
 *
 * gcmANE and iHAART are distributed in the hope that they will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with gcmANE and iHAART. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package com.alyeska.shared.ane.gcm.freInterface;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import android.os.Vibrator;
import android.util.Log;
import com.adobe.fre.FREContext;
import com.distriqt.extension.util.Resources;
import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService
{

	@Override
	public void onRegistered(Context context, String regId)
	{
		FREContext freContext = GCMPushExtension.context;
		freContext.dispatchStatusEventAsync("registered", regId);
	}

	@Override
	public void onUnregistered(Context context, String regId)
	{
		FREContext freContext = GCMPushExtension.context;
		freContext.dispatchStatusEventAsync("unregistered", regId);
	}

	@Override
	public void onMessage(Context context, Intent intent)
	{
		Bundle ex = intent.getExtras();
		String title = ex.getString("title");
		String alert = ex.getString("alert");
		String type = ex.getString("type");
		String id = ex.getString("id");
		String payload = "title~~~" + title + ",alert~~~" + alert + ",type~~~" + type + ",id~~~" + id;
		
		FREContext freContext = GCMPushExtension.context;
		
		Context appContext;
		if(freContext != null)
		{
			Log.i("GCMExtension", "about to dispatch foregroundmessage event...");
			freContext.dispatchStatusEventAsync("foregroundMessage", payload);
			appContext = freContext.getActivity();
		}
		else
		{
			appContext = context.getApplicationContext();
		}

		if(freContext != null)
		{
			freContext.dispatchStatusEventAsync("message", payload);
		}

		if(!isAppInForeground(appContext))
		{
			if (freContext != null)
			{
				Log.i("GCMExtension", "about to dispatch message event...");
				freContext.dispatchStatusEventAsync("message", payload);
			}

			String ns = Context.NOTIFICATION_SERVICE;
			NotificationManager mNotificationManager = (NotificationManager) appContext.getSystemService(ns);
			
			int iconSmall = Resources.getResourceIdByName(appContext.getPackageName(), "drawable", "notifysmall");
			int iconLarge = Resources.getResourceIdByName(appContext.getPackageName(), "drawable", "notifylarge"); //not currently used
			CharSequence tickerText = alert;
			long when = System.currentTimeMillis();

			CharSequence contentTitle = title == null ? "Incoming Notification" : title;
			CharSequence contentText = alert;

			// define sound URI, the sound to be played when there's a notification
			Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

			long[] vibratePattern = new long[60];
			long duration = 2000;
			for (int i = 0; i < 60; i = i + 1)
			{
				long mod = i % 2;
				if (mod > 0) {
					vibratePattern[i] = duration;
				}
				else {
					vibratePattern[i] = 200;
				}
			}

			try
			{
				Intent notificationIntent = new Intent(appContext, 
						Class.forName(appContext.getPackageName() + ".AppEntry"));
				notificationIntent.putExtra("data", payload);

				Notification builder = new Notification.Builder(appContext)
						.setContentTitle("IHAART Notification")
						.setContentText(alert)
						.setTicker(tickerText)
						.setSmallIcon(iconSmall)
						.setWhen(when)
						.setPriority(Notification.PRIORITY_MAX)
						.setVibrate(vibratePattern)
						.setLights(Color.RED, 500, 500)
						.setDefaults(Notification.DEFAULT_SOUND)
						.setContentIntent(PendingIntent.getActivity(appContext, 0, notificationIntent,
																	PendingIntent.FLAG_UPDATE_CURRENT))
						.build();
				//PendingIntent contentIntent = PendingIntent.getActivity(appContext, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

				//notification.setLatestEventInfo(appContext, contentTitle, contentText, contentIntent);
				
				int HELLO_ID = 1;
//				todo bree make tag dynamic
				String tag = "iHAART";
				//mNotificationManager.notify(HELLO_ID, notification);
				mNotificationManager.notify(tag, HELLO_ID, builder);
			}
			catch (IllegalStateException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (ClassNotFoundException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onError(Context context, String errorId)
	{
		FREContext freContext = GCMPushExtension.context;
		freContext.dispatchStatusEventAsync("error", errorId);
	}

	@Override
	public boolean onRecoverableError(Context context, String errorId)
	{
		FREContext freContext = GCMPushExtension.context;
		freContext.dispatchStatusEventAsync("recoverableError", errorId);
		return super.onRecoverableError(context, errorId);
	}
	
	private boolean isAppInForeground(Context context) 
    {
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses != null) 
        {
        	final String packageName = context.getPackageName();
            for (RunningAppProcessInfo appProcess : appProcesses) 
            {
				Log.i("GCMExtensionProcessList", "running process name = " + appProcess.processName);
            	if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) 
            	{
            		return true;
            	}
            }
        }
        
        return false;
    }
}