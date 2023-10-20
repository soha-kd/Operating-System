
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThreadThread extends Thread {
    private ServerPeer serverPeer;
    private Socket socket;
    private PrintWriter printWriter;

    public ServerThreadThread(Socket socket, ServerPeer serverPeer) {
        this.serverPeer = serverPeer;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.printWriter = new PrintWriter(socket.getOutputStream(), true);
            while (true) serverPeer.sendMessage(bufferedReader.readLine());
        } catch (Exception e) {
            serverPeer.getServerThreadThreads().remove(this);
        }
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }
}
