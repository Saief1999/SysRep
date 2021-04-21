package zones_textes_3;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ClientNetworkConfig {


    //used to update the text Area
    private ClientView clientView;
    private Connection connection;
    private Channel channel;
    private String outgoingQueue;
    private String handShakeQueue = "handshake-queue" ;
    private String host = "localhost";


    public ClientNetworkConfig(String queueName, ClientView clientView) {
        this.outgoingQueue = queueName;
        this.clientView = clientView ;
    }


    public void closeClient()
    {
        try {
            channel.close();
            connection.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void initConnection() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(this.host);

            initOutgoingConnection(factory);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void initOutgoingConnection( ConnectionFactory factory) throws Exception {
        connection = factory.newConnection();
        channel = connection.createChannel();
        this.sendQueueName() ;
        channel.queueDeclare(outgoingQueue, false, false, false, null);
        System.out.println("Ready to send messages from "+ outgoingQueue);
    }


    private  void sendQueueName ()
    {
        try {
            this.channel.basicPublish("", handShakeQueue, null,outgoingQueue.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void publishMessage(byte [] byteArray) {
        try {
            this.channel.basicPublish("", outgoingQueue, null,byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
