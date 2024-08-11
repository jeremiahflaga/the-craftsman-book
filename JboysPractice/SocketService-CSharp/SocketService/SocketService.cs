﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace SocketService;

public class SocketService
{
    private Socket serverSocket = null;
    private Thread serverThread = null;

    public int Connections { get; private set; } = 0;

    public void Serve(int port)
    {
        IPHostEntry host = Dns.GetHostEntry("localhost");
        // This is the IP address of the local machine
        IPAddress ipAddress = host.AddressList[0];
        serverSocket = new Socket(ipAddress.AddressFamily, SocketType.Stream, ProtocolType.Tcp);

        IPEndPoint ipEndPoint = new(ipAddress, port);

        serverThread = new Thread(() =>
        {
            serverSocket.Bind(ipEndPoint);
            serverSocket.Listen();
            var handler = serverSocket.Accept();
            serverSocket.Close();
            Connections++;
        });
        serverThread.Start();
    }

    public void Close()
    {
        serverSocket.Close();
    }
}