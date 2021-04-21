package network;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import views.HoView;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class HoNetworkConfig {

    private String host = "localhost";
    private ConnectionFactory factory ;
    private Connection connection ;
    private Channel channel ;
    private String[] queues;

    private HoView hoView;
    public void closeHo()
    {
        System.out.println("Closing HO...");
        try {
            connection.close() ;
            channel.close();
        } catch (IOException | TimeoutException throwables) {
            throwables.printStackTrace();
        }
    }


    public HoNetworkConfig(HoView hoView)
    {
        this.hoView=hoView ;
        this.initGlobalSettings() ;
    }

    public void initGlobalSettings()
    {
        factory = new ConnectionFactory() ;
        factory.setHost(this.host);

        for (String queue : queues) {
            initIncomingConnection(factory,queue);
        }
    }


    private void initIncomingConnection(ConnectionFactory factory, String queueName) {

        try {
            connection = factory.newConnection();
            channel = connection.createChannel() ;
            DeliverCallback callback = (consumerTag,delivery) -> {
                byte [] byteArray = delivery.getBody() ;
                //serverView.updateText(queueName.equals("incoming-text1")?1:2,byteArray );
                /** Here we need to update the HO based on the payload */
            };
            channel.basicConsume(queueName, true, callback, consumerTag -> {
            });
        } catch (IOException|TimeoutException e) {
            e.printStackTrace();
        }
    }
}
