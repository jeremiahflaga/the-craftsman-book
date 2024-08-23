using System;
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
    private bool running = false;

    //public int Connections { get; private set; } = 0;

    public void Serve(int port, SocketServer socketServer)
    {

        serverSocket = new Socket(SocketType.Stream, ProtocolType.Tcp);
        serverSocket.Bind(new IPEndPoint(IPAddress.Loopback, port));
        serverSocket.Listen(10);

        serverThread = new Thread(() =>
        {
            running = true;
            while (running)
            {
                Socket clientSocket = serverSocket.Accept();
                socketServer.Serve(clientSocket);
                clientSocket.Shutdown(SocketShutdown.Both);
                clientSocket.Close();
            }
        });
        serverThread.Start();
    }

    public void Close()
    {
        running = false;
        //serverSocket.Shutdown(SocketShutdown.Both);
        serverSocket.Close();
    }
}