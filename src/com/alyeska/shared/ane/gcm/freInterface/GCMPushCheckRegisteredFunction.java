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

import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREInvalidObjectException;
import com.adobe.fre.FREObject;
import com.adobe.fre.FRETypeMismatchException;
import com.adobe.fre.FREWrongThreadException;
import com.google.android.gcm.GCMRegistrar;

public class GCMPushCheckRegisteredFunction implements FREFunction
{

	@Override
	public FREObject call(FREContext context, FREObject[] args)
	{
		FREObject result;

		String message;
		String senderID = "";

		Log.i("GCMExtension", "Checking device registration...");

		try
		{
			senderID = args[0].getAsString();
		} catch (IllegalStateException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FRETypeMismatchException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FREInvalidObjectException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FREWrongThreadException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		GCMRegistrar.checkDevice(context.getActivity().getApplication());

		final String regId = GCMRegistrar.getRegistrationId(context.getActivity().getApplication());

		if (regId.equals(""))
		{
			message = "GCMServer: device not yet registered for senderID: " + senderID + "   ,false";
		} else
		{
			message = "GCMServer: registrationID:" + regId + "   ,true";
		}

		try
		{
			result = FREObject.newObject(message);
			return result;
		} catch (FREWrongThreadException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}