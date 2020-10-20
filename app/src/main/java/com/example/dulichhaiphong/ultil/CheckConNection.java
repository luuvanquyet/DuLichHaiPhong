package com.example.dulichhaiphong.ultil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class CheckConNection {
    public static boolean haveNetwordConnection(Context context){
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] network = cm.getAllNetworkInfo();
        for(NetworkInfo ni : network){
            if(ni.getTypeName().equalsIgnoreCase("WIFI"))
                if(ni.isConnected())
                    haveConnectedWifi = true;
            if(ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if(ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedMobile||haveConnectedWifi;
    }
    public static void ShowToast_Short(Context context,String thongbao){
        Toast.makeText(context,thongbao,Toast.LENGTH_SHORT).show();
    }
}
