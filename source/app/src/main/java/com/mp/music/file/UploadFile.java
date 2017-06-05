package com.mp.music.file;

import android.content.Context;
import android.util.Log;

import com.mp.music.entity.UpParams;
import com.mp.music.listener.HttpListener;
import com.mp.music.utils.NetUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by 张伟 on 2016-04-08.
 */
public class UploadFile implements Runnable{

    private Context context;
    /** http访问结果监听器 */
    private HttpListener listener;
    /** 当前访问线程 */
    private Thread currentRequest = null;
    /** 访问链接 */
    HttpURLConnection conn = null;
    /** 拿到的流 */
    InputStream input = null;
    /** 文件上传用到的流传输 **/
    FileInputStream fileInputStream;

    private static final String ENCODING = "UTF-8";
    private static final String CHARSET = "UTF-8"; //设置编码
    private static final int TIME_OUT = 40 * 1000;

    private static String targetURL=null;
    private static ArrayList<UpParams> list=null;


    /**
     * 构造函数
     * @param context
     * @param listener
     * @param targetURL
     * @param list
     */
    public UploadFile(Context context,HttpListener listener,String targetURL,ArrayList<UpParams> list){
        this.context=context;
        this.listener=listener;
        this.targetURL=targetURL;
        this.list=list;
    }

    /**
     * 传输文件(多媒体文件 和 多键值同时上传)
     * @return
     */
    public void startUpload() {

        DataOutputStream dos = null;

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary =  UUID.randomUUID().toString();

        try {
            //开启URL连接
            URL url = new URL(targetURL);
            conn = (HttpURLConnection) url.openConnection();

            //设置连接超时
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);

            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy

            //设置请求头信息
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            //  conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("Charset", CHARSET);

            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            //Adding Parameters
            for(int i=0;i<list.size();i++){

                if(!list.get(i).getFileTag()) {

                    String key = list.get(i).getKey();
                    String value = list.get(i).getValue();

                    dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
//                    dos.writeBytes("Content-Type: application/x-www-form-urlencoded; charset=utf-8" + lineEnd);
                    //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
                    dos.writeBytes(lineEnd);

                    //写入内容经过URL编码
                    dos.write(EncodeStr(value).getBytes(CHARSET));

                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                }
                else {

                    //文件的键值对，对应服务器需要接收的文件的名称、以及文件在本地的路径
                    String key = list.get(i).getKey();  //服务器接收文件名称
                    String value = list.get(i).getValue(); //本地文件的路径，即filePath

                    int bytesRead, bytesAvailable, bufferSize;
                    byte[] buffer;
                    int maxBufferSize = 1 * 1024 * 1024;
                    File sourceFile = new File(value);

                    //确认是一个文件
                    if(!sourceFile.isFile()){
                        Log.e("File exist error ","读入文件路径并非一个文件");
                        return;
                    }

                    String fileName=sourceFile.getName();

                    fileInputStream = new FileInputStream(sourceFile);
                    dos.writeBytes("Content-Disposition: form-data; name=\""+key+"\";filename=\""+
                            fileName + "\"" + lineEnd);
                    dos.writeBytes(lineEnd);

                    // create a buffer of maximum size
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    //将File读入buffer,并写入请求头
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0)
                    {
                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }

                    //写入分隔符
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary);
                }
            }

            //请求头结束
            dos.writeBytes(twoHyphens + lineEnd);

            int responseCode = conn.getResponseCode();

            if (responseCode == 200) {
                input = conn.getInputStream();
                if (input != null) {
                    listener.action(HttpListener.EVENT_GET_DATA_SUCCESS,
                            readStream(input));
                }
            } else if (responseCode == 404) {
                input = conn.getErrorStream();
                if (input != null) {
                    listener.action(HttpListener.EVENT_GET_DATA_SUCCESS,
                            readStream(input));
                } else {
                    listener.action(HttpListener.EVENT_NETWORD_EEEOR,
                            null);
                }
            } else {
                listener.action(HttpListener.EVENT_NETWORD_EEEOR, null);
            }

            // close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (SocketException e) {
            e.printStackTrace();
            listener.action(HttpListener.EVENT_CLOSE_SOCKET, null);
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            listener.action(HttpListener.EVENT_NETWORD_EEEOR, null);
        } catch (IOException e) {
            e.printStackTrace();
            listener.action(HttpListener.EVENT_GET_DATA_EEEOR, null);
        } catch (Exception e) {
            e.printStackTrace();
            listener.action(HttpListener.EVENT_NETWORD_EEEOR, null);
        }

    }

    /**
     * 对请求的字符串进行编码
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String EncodeStr(String requestStr)
            throws UnsupportedEncodingException {
        return URLEncoder.encode(requestStr, ENCODING);
    }

    /**
     * @Title: postRequest
     * @Description:Post请求触发
     * @throws
     */
    public void postRequest() {
        currentRequest = new Thread(this);
        currentRequest.start();
    }

    /**
     * 发送请求
     */
    public void run() {
        // 判断是否有网络
        boolean netType = NetUtils.isNetworkAvailable(context);
        if (netType) {
            startUpload();
        } else {
            listener.action(HttpListener.EVENT_NOT_NETWORD, null);
        }
    }

    /**
     * @Title: isRunning
     * @Description: 判断是否正在访问
     * @return
     * @throws
     */
    public boolean isRunning() {
        if (currentRequest != null && currentRequest.isAlive()) {
            return true;
        }
        return false;
    }

    /**
     读取数据
     *
     */
    private String readStream(InputStream inStream) throws Exception {
        String result;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        result = new String(outStream.toByteArray(), ENCODING);
        outStream.close();
        inStream.close();
        return result;
    }

    /**
     * 取消当前HTTP连接处理
     */
    public void cancelHttpRequest() {
        if (currentRequest != null && currentRequest.isAlive()) {
            if (input != null) {
                try {
                    input.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            input = null;
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            conn = null;
            currentRequest = null;
            System.gc();
        }
    }
}