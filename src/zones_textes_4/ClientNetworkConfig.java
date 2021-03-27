package zones_textes_4;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

public class ClientNetworkConfig {


    //used to update the text Area
    private ClientView clientView;
    private Connection connection;
    private Channel channel;
    private String outgoingQueue;
    private String host = "localhost";

    private Connection incomingConnection;
    private Channel incomingChannel ;
    private String incomingQueue ;

    public ClientNetworkConfig(String queueName,String otherQueueName, ClientView clientView) {
        this.outgoingQueue = queueName;
        this.incomingQueue = otherQueueName;
        this.clientView = clientView ;
    }


    public void closeClient()
    {
        try {
            channel.close();
            incomingChannel.close();
            connection.close();
            incomingConnection.close() ;
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
            initIncomingConnection(factory);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void initOutgoingConnection( ConnectionFactory factory) throws Exception {
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(outgoingQueue, false, false, false, null);

    }

    public void initIncomingConnection( ConnectionFactory factory) throws Exception {
        incomingConnection = factory.newConnection();
        incomingChannel = connection.createChannel();
        channel.queueDeclare(incomingQueue, false, false, false, null);
        try {
            channel.queueDeclare(incomingQueue, false, false, false, null);

            DeliverCallback ingoingCallback = (consumerTag, delivery) -> {
                byte[] byteArray = delivery.getBody();
                clientView.updateText(byteArray );
            };

            channel.basicConsume(incomingQueue, true, ingoingCallback, consumerTag -> {
            });
        } catch (Exception e) {
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
