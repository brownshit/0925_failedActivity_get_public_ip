package com.practice.wpsactivity;

import static android.os.Looper.getMainLooper;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
public class public_ip_get {

    Handler handler;
    mThread mtThread;
    Message msg;

    public_ip_get(){ }
    public String get_ip(String Address){


        final BufferedReader[] br = {null};
        String getpIP;
        mtThread = new mThread(handler);
        mtThread.setDaemon(true);
        mtThread.start();
//todo+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        msg = new Message();
        msg.obj = (Object)Address;
        mtThread.temp_handler.sendMessage(msg);
            //이러면 주소가 넘어간다.msg Object 형태로

        Looper.prepare();
//todo+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //todo / ip_get객체 생성 후 get_ip함수의 mHandler의 getLine함수 가져오기.
        class mHandler extends Handler{
            String stringline;

            public String getLine(){
                return stringline;
            }
            @Override
            public void handleMessage(@NonNull Message msg){
                br[0] = (BufferedReader) msg.obj;
                String line = "0000";
                while(true){
                    try {
                        if (!((line = br[0].readLine()) != null)) break;
                        //br은 버퍼 리드 인스턴스이다.
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(line);
                    String public_ip = "http://"+line+":3000/";
                    /**
                     * System.out.println(line);
                     * System.out.println(line.getClass().getName());
                     * **/
                    System.out.println(public_ip);
                }
                //System.out.println(br.readLine());
                this.stringline= line;
            }
        };
        Looper.loop();
        /**
         * System.out.println(line);
         * System.out.println(line.getClass().getName());
         * **/

        /**
         String line = "0000";
         while(true){
         try {
         if (!((line = br[0].readLine()) != null)) break;
         //br은 버퍼 리드 인스턴스이다.
         } catch (IOException e) {
         e.printStackTrace();
         }
         System.out.println(line);
         String public_ip = "http://"+line+":3000/";

         System.out.println(public_ip);
         }
         //System.out.println(br.readLine());
         this.stringline= line;
         return line;
         * **/
        //todo #2 on thread

        /*
        todo 0923
          mHandler안의 함수를 밖으로 가지고 나와야 한다.
          interface or lambda를 사용해서 해결해야 한다.
        * */

        mHandler myhandler = new mHandler();
        getpIP = myhandler.getLine();

        return getpIP;
    }
}
class mThread extends Thread{
    Handler temp_handler;
    Handler temp2_handler;
    mThread(Handler handler){
        temp2_handler = handler;
    }
    public void run(){
        Looper.prepare();
        temp_handler = new Handler(getMainLooper()){

            private URL url;
            BufferedReader br = null;
            HttpURLConnection conn;
            String protocol = "GET";

            public void handleMessage(@NonNull Message msg){
                Message retmsg = new Message();
                try{
                    String Address = msg.toString();
                    url = new URL(Address);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod(protocol);
                    //MS949
                    //UTP-8
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
                    retmsg.obj = (Object) br;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }catch (IOException e) {
                    e.printStackTrace();
                }
                /**
                 try{
                 url = new URL(Address);
                 conn = (HttpURLConnection)url.openConnection();
                 conn.setRequestMethod(protocol);
                 //MS949
                 //UTP-8
                 br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
                 } catch (MalformedURLException e) {
                 e.printStackTrace();
                 }catch (IOException e) {
                 e.printStackTrace();
                 }
                 //todo #1 on thread
                 * **/
                temp2_handler.sendMessage(retmsg);
            }
        };
        Looper.loop();
    }
}
