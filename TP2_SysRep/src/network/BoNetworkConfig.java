package network;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

public class BoNetworkConfig {

    private ConnectionFactory factory;
    private String host = "localhost" ;
    private Connection connection ;
    private Channel channel  ;
    private String outgoingQueue ;



    public void closeBo()
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

    public BoNetworkConfig (String queueName)
    {
        this.outgoingQueue = queueName ;
        initConnection();
    }

    public void initConnection ()
    {

        try {
            factory = new ConnectionFactory() ;
            factory.setHost(host);
            initOutgoingConnection(factory);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void initOutgoingConnection(ConnectionFactory factory)throws Exception
    {
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(outgoingQueue, false, false, false, null);

        System.out.println("Ready to send messages from "+ outgoingQueue);
    }
    public void publishMessage(byte [] byteArray) {
        try {
            this.channel.basicPublish("", outgoingQueue, null,byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
