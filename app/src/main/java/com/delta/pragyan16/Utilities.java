package com.delta.pragyan16;

import android.content.SharedPreferences;
import android.graphics.Color;

public class Utilities {


    public static String[] strcolors={"#BF360C","#F4511E","#FF7043","#EF6C00","#FB8C00","#FFA726","#FFA000","#FFC107","#FFD54F","#FDD835",
            "#FFEE58","#F4FF81","#DCE775","#CDDC39","#AED581","#8BC34A","#689F38","#4CAF50",
            "#00C853","#00E676","#1DE9B6","#00BFA5","#00B8D4","#0091EA","#0288D1","#01579B","#1565C0",
            "#5C6BC0","#3949AB","#7E57C2","#512DA8","#311B92","#6A1B9A","#8E24AA","#AB47BC","#CE93D8","#D500F9","#FF4081",
            "#EC407A","#D81B60","#AD1457","#880E4F"};
    public static int colors[];
    public static SharedPreferences sp;
                                            //Fields in Shared Preferences getSharedPreferences("llep")
                                                //1. offset [For main landing page]
                                                //2. status
                                                    // 0 - Not logged in/registered --> redirect to loginActivity
                                                    // 1 - Logged in                --> redirect to MainActivity
                                                //3. pragyan_mail
                                                //4. pragyan_pass
    public static int status = 0;
    public static String pragyan_mail;
    public static String pragyan_pass;

    public static String url_auth = "";//"https://api.festember.com/user/auth";

    public static String url_reg = "https://api.pragyan.org/user/register";
    public static String url_qr = "http://api.pragyan.org/tshirt/qrcode";
    public static String url_gcm = "http://415b1a40.ngrok.com/festember-app-gcm/register.php";



    public static void init_colors()
    {
        colors=new int[strcolors.length];
        for(int i=0;i<strcolors.length;i++)
        {
            colors[i]=Color.parseColor(strcolors[i]);
        }
    }

}
