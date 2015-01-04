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

import android.content.Context;

public class GCMBroadcastReceiver extends com.google.android.gcm.GCMBroadcastReceiver
{

	@Override
	protected java.lang.String getGCMIntentServiceClassName(Context context)
	{
		String intentClassName = super.getGCMIntentServiceClassName(context);
		intentClassName = "com.alyeska.shared.ane.gcm.freInterface.GCMIntentService";
		//FREContext freContext = GCMPushExtension.context;
		//freContext.dispatchStatusEventAsync("intentClassName", intentClassName);
		return intentClassName;
	}
}