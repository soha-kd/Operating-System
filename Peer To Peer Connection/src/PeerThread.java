import javax.json.Json;
import javax.json.JsonObject;
import java.io.*;
import java.net.Socket;

public class PeerThread extends Thread {
    private BufferedReader bufferedReader;
    private String username;
    private ServerPeer serverPeer;

    public PeerThread(Socket socket, String username, ServerPeer serverPeer){
        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
            this.serverPeer = serverPeer;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        boolean flag = true;
        while (flag){
            try {
                JsonObject jsonObject = Json.createReader(bufferedReader).readObject();
                if (jsonObject.containsKey("username")) {
                    if(Peer.listRumor.size() > 0) {
                        if (Peer.listRumor.contains(jsonObject.getString("message"))) {
                            System.out.println(jsonObject.getString("username") + " sent a rumor.it is in the list");
                        } else {
                            System.out.println("[" + jsonObject.getString("username") + "]: " + jsonObject.getString("message"));
                            addListAndSendRumor(jsonObject);
                        }
                    }else {
                        System.out.println("[" + jsonObject.getString("username") + "]: " + jsonObject.getString("message"));
                        addListAndSendRumor(jsonObject);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                flag = false;
                interrupt();
            }
        }
    }

    private void addListAndSendRumor(JsonObject jsonObject) {
        Peer.listRumor.add(jsonObject.getString("message"));
        System.out.println("New rumor in the add-on list");
        StringWriter stringWriter = new StringWriter();
        Json.createWriter(stringWriter).writeObject(Json.createObjectBuilder()
                .add("username", username)
                .add("message", jsonObject.getString("message"))
                .build());
        serverPeer.sendMessage(stringWriter.toString());
    }
}
