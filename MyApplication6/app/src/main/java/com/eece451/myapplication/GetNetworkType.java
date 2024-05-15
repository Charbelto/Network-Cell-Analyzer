package com.eece451.myapplication;

import android.telephony.TelephonyManager;

public class GetNetworkType {
    public String getNetworkTypeString(int networkType) {
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return "Unknown";
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "2G (GPRS)";
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "2G+ (EDGE)";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "3G (UMTS)";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "3G (HSDPA)";
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "3G (HSUPA)";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "3G (HSPA)";
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G+ (HSPA+)";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G (LTE)";
            case TelephonyManager.NETWORK_TYPE_NR:
                return "5G";
            default:
                return "N/A";
        }
    }
}
