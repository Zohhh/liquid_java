package com.wzh.liquid.service.impl;//package com.wzh.vueblog.service.impl;


import com.wzh.liquid.entity.TcpData;
import com.wzh.liquid.entity.TcpData1;
import com.wzh.liquid.service.TcpData1Service;
import com.wzh.liquid.service.TcpDataService;
import com.wzh.liquid.service.TcpSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author WZH
 * @Description
 * @date 2022/1/22 - 13:20
 **/
@Service
public class TcpSocketServiceImpl implements TcpSocketService {
    @Autowired
    ServerSocket serverSocket;

    @Autowired
    TcpDataService tcpDataService;

    @Autowired
    TcpData1Service tcpData1Service;

    @Override
    public void getTcpSocket() throws IOException {
        System.out.println("服务器启动，等待连接。。。");
        //创建一个服务器ServerSocket对象，和系统要指定的端口号
        /*
            让服务器一只处于监听状态
            有客户端上传文件，就保存文件
         */
        final String[] str = new String[1];
        //时间戳数组
        byte[] byteTime=new byte[4];
        //id数组
        byte[] byteId =new byte[4];
        byte[] byteInfo=new byte[17];
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 4, 10, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(), Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        while (true) {
            Socket socket = serverSocket.accept();
            //使用多线程技术，提高程序的效率，有一个客户端上传文件，就开启一个线程，完成文件的上传
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("已连接");
                        System.out.println("==============");
                        //使用socket对象中的方法getInputStream，获取网络字节输入流
                        InputStream is = socket.getInputStream();
                        //读取客户端上传的文件或数据
                        int len = 0;
                        //最大传输240个字节，加上协议27个字节
                        byte[] bytes = new byte[267];
                        while ((len = is.read(bytes)) != -1) {
                            System.out.println(len);
                            if (len > 27) {
//                                System.out.println(len);
                                System.out.println("接收到数据帧");
                                //以16进制打印数据帧
//                                for (int i = 0; i < len; i++) {
//                                    System.out.printf("0x%x ",bytes[i]);
//                                }
//                                System.out.println("");
                                System.arraycopy(bytes,6,byteId,0,4);
//                                System.arraycopy(bytes,18,byteTime,0,4);
                                //根据上传的时间戳求的上传时间
//                                String strTime = byte2HexString(byteTime);
//                                long timeInteger = Integer.parseInt(strTime, 16);
//                                String timeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date((timeInteger-3600*6)*1000));
//                                Timestamp ts= Timestamp.valueOf(timeStr);
                                //截取数据帧，转换为Float形式
                                System.arraycopy(bytes,27,byteInfo,0,17);
                                String info = new String(byteInfo);
                                String[] infoSplit = info.split(",");
                                float temp = Float.parseFloat(infoSplit[0]);
                                float tds= Float.parseFloat(infoSplit[1]);
                                float ph= Float.parseFloat(infoSplit[2]);
                                //求出本地时间存入ts
                                String timeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                                Timestamp ts= Timestamp.valueOf(timeStr);
                                //根据id判断写入哪个数据库
                                switch (byteId[3]){
                                    case 1:
                                        tcpTableSave(ts, temp, tds, ph);
                                        break;
                                    case 2:
                                        tcpTable2Save(ts, temp, tds, ph);
                                        break;
                                    default:
                                        System.out.println("ERROR!! 数据帧ID错误！");
                                }
                            }else {
                                System.out.println("等待数据输入");
                                System.out.println("==============");
                            }
                        }
                        System.out.println("断开连接");
                        socket.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            });
            threadPoolExecutor.shutdown();
        }
    }
    private void tcpTableSave(Timestamp ts, float temp, float tds, float ph) {
        TcpData tcpData = new TcpData();
        tcpData.setId(null);
        tcpData.setTime(ts);
        tcpData.setTempLiquid(temp);
        tcpData.setTdsLiquid(tds);
        tcpData.setPhLiquid(ph);
        tcpDataService.save(tcpData);
        System.out.println("数据写入完成");
        System.out.println("==============");
    }
    private void tcpTable2Save(Timestamp ts, float temp, float tds, float ph) {
        TcpData1 tcpData = new TcpData1();
        tcpData.setId(null);
        tcpData.setTime(ts);
        tcpData.setTempLiquid(temp);
        tcpData.setTdsLiquid(tds);
        tcpData.setPhLiquid(ph);
        tcpData1Service.save(tcpData);
        System.out.println("数据写入完成");
        System.out.println("==============");
    }
}
