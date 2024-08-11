


using System.Net;
using System.Net.Sockets;

namespace SocketService.Tests;

public class UnitTest1
{
    [Fact]
    public void testOneConnection()
    {
        SocketService ss = new SocketService();
        ss.Serve(999);
        Connect(999);
        ss.Close();
        Assert.Equal(1, ss.Connections);
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
