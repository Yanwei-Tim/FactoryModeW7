/* Copyright Statement:
 *
 * This software/firmware and related documentation ("MediaTek Software") are
 * protected under relevant copyright laws. The information contained herein
 * is confidential and proprietary to MediaTek Inc. and/or its licensors.
 * Without the prior written permission of MediaTek inc. and/or its licensors,
 * any reproduction, modification, use or disclosure of MediaTek Software,
 * and information contained herein, in whole or in part, shall be strictly prohibited.
 */
/* MediaTek Inc. (C) 2010. All rights reserved.
 *
 * BY OPENING THIS FILE, RECEIVER HEREBY UNEQUIVOCALLY ACKNOWLEDGES AND AGREES
 * THAT THE SOFTWARE/FIRMWARE AND ITS DOCUMENTATIONS ("MEDIATEK SOFTWARE")
 * RECEIVED FROM MEDIATEK AND/OR ITS REPRESENTATIVES ARE PROVIDED TO RECEIVER ON
 * AN "AS-IS" BASIS ONLY. MEDIATEK EXPRESSLY DISCLAIMS ANY AND ALL WARRANTIES,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NONINFRINGEMENT.
 * NEITHER DOES MEDIATEK PROVIDE ANY WARRANTY WHATSOEVER WITH RESPECT TO THE
 * SOFTWARE OF ANY THIRD PARTY WHICH MAY BE USED BY, INCORPORATED IN, OR
 * SUPPLIED WITH THE MEDIATEK SOFTWARE, AND RECEIVER AGREES TO LOOK ONLY TO SUCH
 * THIRD PARTY FOR ANY WARRANTY CLAIM RELATING THERETO. RECEIVER EXPRESSLY ACKNOWLEDGES
 * THAT IT IS RECEIVER'S SOLE RESPONSIBILITY TO OBTAIN FROM ANY THIRD PARTY ALL PROPER LICENSES
 * CONTAINED IN MEDIATEK SOFTWARE. MEDIATEK SHALL ALSO NOT BE RESPONSIBLE FOR ANY MEDIATEK
 * SOFTWARE RELEASES MADE TO RECEIVER'S SPECIFICATION OR TO CONFORM TO A PARTICULAR
 * STANDARD OR OPEN FORUM. RECEIVER'S SOLE AND EXCLUSIVE REMEDY AND MEDIATEK'S ENTIRE AND
 * CUMULATIVE LIABILITY WITH RESPECT TO THE MEDIATEK SOFTWARE RELEASED HEREUNDER WILL BE,
 * AT MEDIATEK'S OPTION, TO REVISE OR REPLACE THE MEDIATEK SOFTWARE AT ISSUE,
 * OR REFUND ANY SOFTWARE LICENSE FEES OR SERVICE CHARGE PAID BY RECEIVER TO
 * MEDIATEK FOR SUCH MEDIATEK SOFTWARE AT ISSUE.
 *
 * The following software/firmware and/or related documentation ("MediaTek Software")
 * have been modified by MediaTek Inc. All revisions are subject to any receiver's
 * applicable license agreements with MediaTek Inc.
 */

package com.mediatek.factorymode;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.os.SystemProperties;

public class FactoryModeReceiver extends BroadcastReceiver {

    private final String TAG = "FACTORY/SECRET_CODE";
    // process *#66#
    private static final String SECRET_CODE_ACTION = "android.provider.Telephony.SECRET_CODE";
    private static final String SYSTEM_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
    Uri engineerUri = Uri.parse("android_secret_code://66");

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent.getAction().equals(SECRET_CODE_ACTION)) {
                Uri uri = intent.getData();
                Log.i(TAG, "getIntent success in if");
                if (uri.equals(engineerUri)) {
                    Intent i = new Intent(Intent.ACTION_MAIN);
                    i.setComponent(new ComponentName("com.mediatek.factorymode",
                            "com.mediatek.factorymode.FactoryMode"));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Log.i(TAG,"Before start FACTORY activity");
                    context.startActivity(i);
                } else {
                    Log.i(TAG,"No matched URI!");
                }
            } else if(intent.getAction().equals(SYSTEM_BOOT_COMPLETED)) {
                //Settings.System.putInt(context.getContentResolver(),  Settings.System.CENON_IGNORE_HOME_POWER, 0);
                if("1".equals(SystemProperties.get("ro.cenon_factorymode_feature", "0"))) {
                    Intent i = new Intent();
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setClassName("com.mediatek.factorymode", "com.mediatek.factorymode.FactoryMode");
                    context.startActivity(i);
                }
                Log.i(TAG,"after system boot up, must set CENON_IGNORE_HOME_POWER value is 0, because system shutdown expection");
            } else {
                Log.i(TAG,"Not SECRET_CODE_ACTION!");
            }
        } catch(Exception e) {
            e.printStackTrace();
            Log.w(TAG, "Package exception.");
        }
    }
}