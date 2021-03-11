package part23;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ClientNetworkConfig {



    private Connection connection;
    private Channel channel;
    private String queueName;
    private String host = "localhost";

    private static final String ExchangeNAME = "" ;


    public ClientNetworkConfig(String queueName) {
        this.queueName = queueName;
    }


    public void initConnection() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(this.host);
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare().getQueue();

            System.out.println("Ready to sent messages from "+queueName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void publishMessage(String message) {
        try {
            this.channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
