package zones_textes_2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ServerNetworkConfig {
    private  String QUEUE1 = "incoming-text1"; // listens for incoming messages from Client 1
    private  String QUEUE2 = "incoming-text2"; // listens for incoming messages from Client 2

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
        setQueueNames("incoming-text1","incoming-text2");

        this.initGlobalSettings();
    }
    private  void setQueueNames(String queueName1, String queueName2) {
        QUEUE1 = queueName1;
        QUEUE2 = queueName2;
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

        initIncomingConnection(factory, QUEUE1);
        initIncomingConnection(factory, QUEUE2);
        System.out.println("Listening to messages... ( CTRL+C to Cancel )");

    }

    private void initIncomingConnection(ConnectionFactory factory, String queueName) {
        try {
            channel.queueDeclare(queueName, false, false, false, null);

            DeliverCallback ingoingCallback = (consumerTag, delivery) -> {
                byte[] byteArray = delivery.getBody();
                serverView.updateText(queueName.equals("incoming-text1")?1:2,byteArray );
            };

            channel.basicConsume(queueName, true, ingoingCallback, consumerTag -> {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
