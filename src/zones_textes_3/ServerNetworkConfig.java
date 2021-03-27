package zones_textes_3;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class ServerNetworkConfig {



    private String handShakeQueue = "handshake-queue" ;
    private ServerView serverView ;
    Connection connection ;
    Channel channel ;

    public void closeServer()
    {
        System.out.println("Closing server...");
        try {
            channel.close();
            connection.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public ServerNetworkConfig(ServerView serverView )
    {
        this.serverView = serverView ;
        this.initGlobalSettings();
    }

    private void initGlobalSettings() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        initIncomingHandshake(factory) ;
        System.out.println("Connection opened, waiting for Handshakes... ") ;
    }

    private void initIncomingConnection(ConnectionFactory factory, String queueName) {
        try {
            channel.queueDeclare(queueName, false, false, false, null);

            DeliverCallback ingoingCallback = (consumerTag, delivery) -> {
                byte[] byteArray = delivery.getBody();
                serverView.updateText(queueName,byteArray );
            };

            channel.basicConsume(queueName, true, ingoingCallback, consumerTag -> {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Listening to messages from '"+queueName+"' ...");
    }

    private void initIncomingHandshake(ConnectionFactory factory) {

        try {
            channel.queueDeclare(handShakeQueue, false, false, false, null);

            DeliverCallback ingoingCallback = (consumerTag, delivery) -> {
                String queueName = new String(delivery.getBody(), StandardCharsets.UTF_8);
                addClient(factory,queueName);
            };

            channel.basicConsume(handShakeQueue, true, ingoingCallback, consumerTag -> {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addClient(ConnectionFactory factory , String queueName)
    {

        this.initIncomingConnection(factory,queueName);
        this.serverView.addNewClientView(queueName); ;

    }

}
