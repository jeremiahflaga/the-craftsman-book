


using System.Net;
using System.Net.Sockets;

namespace SocketService.Tests;

public class UnitTest1
{
    // NOTE: using different ports in each test
    // else you will get this error: Only one usage of each socket address (protocol/network address/port) is normally permitted

    [Fact]
    public void testOneConnection()
    {
        var connectionCounter = new ConnectionCounterSocketServer();
        SocketService ss = new SocketService();
        ss.Serve(777, connectionCounter);
        Connect(777);
        //ss.Close();
        Assert.Equal(1, connectionCounter.Connections);
    }

    [Fact]
    public void testManyConnections()
    {
        var connectionCounter = new ConnectionCounterSocketServer();
        SocketService ss = new SocketService();
        ss.Serve(999, connectionCounter);
        for (int i = 0; i< 10; i++)
            Connect(999);
        ss.Close();
        Assert.Equal(10, connectionCounter.Connections);
    }

    [Fact]
    public void testSendMessage()
    {
        SocketService ss = new SocketService();
        ss.Serve(888, new HelloServer());

        using var clientSocket = new Socket(SocketType.Stream, ProtocolType.Tcp);
        clientSocket.Connect(new IPEndPoint(IPAddress.Loopback, 888));

        //Thread.Sleep(100);
        var networkStream = new NetworkStream(clientSocket, FileAccess.Read);
        var streamReader = new StreamReader(networkStream);
        var answer = streamReader.ReadToEnd();

        clientSocket.Close();

        Assert.Equal("Hello", answer);
    }

    private void Connect(int port)
    {
        try
        {
            //IPHostEntry host = Dns.GetHostEntry("localhost");
            //// This is the IP address of the local machine
            //IPAddress ipAddress = host.AddressList[0];
            //Socket s = new Socket(ipAddress.AddressFamily, SocketType.Stream, ProtocolType.Tcp);

            //IPEndPoint ipEndPoint = new(ipAddress, port);
            //s.Connect(ipEndPoint);

            //Thread.Sleep(100);

            //s.Close();

            using var clientSocket = new Socket(SocketType.Stream, ProtocolType.Tcp);
            clientSocket.Connect(new IPEndPoint(IPAddress.Loopback, port));

            Thread.Sleep(100);

            clientSocket.Close();
        }
        catch (Exception ex)
        {
            Assert.Fail("Could not connect");
        }
    }
}

internal class ConnectionCounterSocketServer : SocketServer
{
    public int Connections { get; private set; } = 0;

    public void Serve(Socket s)
    {
        Connections++;
    }
}

internal class HelloServer : SocketServer
{
    public void Serve(Socket s)
    {
        var networkStream = new NetworkStream(s, FileAccess.Write);
        var streamWriter = new StreamWriter(networkStream);
        streamWriter.Write("Hello");
        streamWriter.Flush();
    }
}