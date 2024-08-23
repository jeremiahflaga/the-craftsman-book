


using System.Net;
using System.Net.Sockets;
using System.Text;

namespace SocketService.Tests;

public class SocketServiceTests
{
    private ConnectionCounterSocketServer connectionCounter;

    public SocketServiceTests()
    {
        connectionCounter = new ConnectionCounterSocketServer();
    }

    // NOTE: using different ports in each test
    // else you will get this error: Only one usage of each socket address (protocol/network address/port) is normally permitted

    [Fact]
    public void testOneConnection()
    {
        SocketService ss = new SocketService();
        ss.Serve(777, connectionCounter);
        Connect(777);
        ss.Close();
        Assert.Equal(1, connectionCounter.Connections);
    }

    [Fact]
    public void testManyConnections()
    {
        SocketService ss = new SocketService();
        ss.Serve(999, connectionCounter);
        for (int i = 0; i < 10; i++)
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

        //var networkStream = new NetworkStream(clientSocket, FileAccess.Read);
        //var streamReader = new StreamReader(networkStream);
        //var answer = streamReader.ReadToEnd();

        byte[] buffer = new byte[1024]; // Create a buffer to hold the received data
        int bytesReceived = clientSocket.Receive(buffer); // Receive data from the server

        clientSocket.Shutdown(SocketShutdown.Both);
        clientSocket.Close();
        var answer = Encoding.UTF8.GetString(buffer, 0, bytesReceived);

        Assert.Equal("Hello", answer);
    }

    private void Connect(int port)
    {
        using var clientSocket = new Socket(SocketType.Stream, ProtocolType.Tcp);
        try
        {
            clientSocket.Connect(new IPEndPoint(IPAddress.Loopback, port));

            Thread.Sleep(100);

            clientSocket.Shutdown(SocketShutdown.Both);
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
        //var streamWriter = new StreamWriter(networkStream);
        //streamWriter.Write("Hello");
        //streamWriter.Flush();

        // Encode the data string into a byte array and send it
        string dataToSend = "Hello"; // The data you want to send
        byte[] byteData = Encoding.ASCII.GetBytes(dataToSend);

        s.Send(byteData);
    }
}