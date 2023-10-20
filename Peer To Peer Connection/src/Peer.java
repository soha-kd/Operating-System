import javax.json.Json;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Peer {
    public static List<String> listRumor;

    public static void main(String[] args) throws Exception {
        listRumor = new ArrayList<String>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("> enter your username and port for this peer(username port):");
        String[] setupValues = bufferedReader.readLine().split(" ");
        ServerPeer serverPeer = new ServerPeer(setupValues[1]);
        serverPeer.start();
        new Peer().updateListenToPeers(bufferedReader, setupValues[0], serverPeer);
    }

    public void updateListenToPeers(BufferedReader bufferedReader, String username, ServerPeer serverPeer){
        System.out.println("> enter all peer (space separated): Example:[localhost:5000 localhost:5001]");
        System.out.println(" press ro receive messages from (s to skip):");
        String input = null;
        try {
            input = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] inputValues = input.split(" ");
        if(!input.equals("s")) for (int i = 0; i < inputValues.length; i++) {
            String[] address = inputValues[i].split(":");
            Socket socket = null;
            try {
                socket = new Socket(address[0], Integer.valueOf(address[1]));
                new PeerThread(socket , username , serverPeer).start();
            }catch (Exception e){
                if(socket!= null) {
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                else System.out.println("invalid input. skipping to next step.");
            }
        }
        communicate(bufferedReader, username, serverPeer);
    }

    public void communicate(BufferedReader bufferedReader, String username, ServerPeer serverPeer) {
        try{
            System.out.println("> you can now communicate (e to exit, c to change)");
            boolean flag = true;
            while (flag){
                String message = bufferedReader.readLine();
                if(message.equals("e")){
                    flag = false;
                    break;
                }else if(message.equals("c")){
                    updateListenToPeers(bufferedReader, username, serverPeer);
                }else {
                    listRumor.add(message);
                    StringWriter stringWriter = new StringWriter();
                    Json.createWriter(stringWriter).writeObject(Json.createObjectBuilder()
                            .add("username" , username)
                            .add("message" , message)
                            .build());
                    serverPeer.sendMessage( stringWriter.toString());
                }
            }
            System.exit(0);
        }catch (Exception e){}
    }
}
