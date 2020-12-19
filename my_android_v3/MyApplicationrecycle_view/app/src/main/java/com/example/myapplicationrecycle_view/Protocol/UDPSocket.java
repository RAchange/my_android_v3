package com.example.myapplicationrecycle_view.Protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class UDPSocket {
    private byte[] buffer = new byte[1024];
    private DatagramSocket recieveSocket;
    private DatagramSocket sendSocket;
    private String remoteHost;
    private int sendPort;

    public UDPSocket(String localHost, String remoteHost, int recievePort, int sendPort) throws SocketException {
        this.remoteHost = remoteHost;
        this.sendPort = sendPort;
        this.recieveSocket = new DatagramSocket(new InetSocketAddress(localHost, recievePort));
        this.sendSocket = new DatagramSocket();
    }

    public byte[] recieve() throws IOException {
        DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
        recieveSocket.receive(dp);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(dp.getData(), 0, dp.getLength());
        byte[] data = baos.toByteArray();
        baos.flush();
        baos.close();
        return data;
    }

    public void send(byte[] data) throws IOException {
        DatagramPacket dp = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(remoteHost), sendPort);
        dp.setData(data);
        sendSocket.send(dp);
    }

    public void close() {
        try {
            if (recieveSocket.isConnected()) {
                recieveSocket.disconnect();
                recieveSocket.close();
            }
            if (sendSocket.isConnected()) {
                sendSocket.disconnect();
                sendSocket.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
