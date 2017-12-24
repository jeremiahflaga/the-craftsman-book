import java.net.Socket;

class WaitThenClose implements SocketServer {
        public static int threadsActive = 0;

        @Override
        public void serve(Socket socket) {
            threadsActive++;
            delay();
            threadsActive--;
        }

        private void delay() {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }