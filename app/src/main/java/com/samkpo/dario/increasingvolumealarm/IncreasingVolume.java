/*
 * Copyright (C) 2016 Aguilera Dario
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.samkpo.dario.increasingvolumealarm;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class IncreasingVolume implements IXposedHookLoadPackage {
    private static String APP_TO_HACK = "com.android.deskclock";
    private boolean mIncreasingVolumeDefault = true;
    private boolean mVibratePhoneDefault = false;

    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        if(!lpparam.packageName.equals(APP_TO_HACK))
        	return;

        XposedBridge.log("Loaded app: " + lpparam.packageName + ". We are in!!");
        XposedBridge.log("Version: " + (BuildConfig.DEBUG ? BuildConfig.VERSION_NAME + " - " + BuildConfig.VERSION_CODE
                : BuildConfig.VERSION_NAME));

        //Preferences
        //SharedPreferences pref = getSharedPreferences("user_settings", MODE_WORLD_READABLE);
        XSharedPreferences pref = new XSharedPreferences("com.samkpo.dario.increasingvolumealarm",
                "user_settings");

        //Increasing volume alarm
        mIncreasingVolumeDefault = pref.getBoolean("increasing_volume", true);
        //Vibrate cell on alarm
        mVibratePhoneDefault = pref.getBoolean("vibrate_alarm", false);

        //Hook the constructor
        XposedHelpers.findAndHookConstructor("com.android.deskclock.provider.Alarm", lpparam.classLoader, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if(mIncreasingVolumeDefault)
                    XposedHelpers.setObjectField(param.thisObject, "increasingVolume", mIncreasingVolumeDefault);

                if(mVibratePhoneDefault)
                    XposedHelpers.setObjectField(param.thisObject, "vibrate", mVibratePhoneDefault);
            }
        });

    }
}
