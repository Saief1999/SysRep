package zones_textes_1;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class ServerNetworkConfig {
    private  String QUEUE1 = "incoming-text1"; // listens for incoming messages from Client 1
    private  String QUEUE2 = "incoming-text2"; // listens for incoming messages from Client 2

    private ServerView serverView ;



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
        initIncomingConnection(factory, QUEUE1);
        initIncomingConnection(factory, QUEUE2);
        System.out.println("Listening to messages... ( CTRL+C to Cancel )");

    }

    private void initIncomingConnection(ConnectionFactory factory, String queueName) {


        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(queueName, false, false, false, null);

            DeliverCallback ingoingCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "' from " + queueName);
                serverView.updateText(queueName.equals("incoming-text1")?1:2,message );
            };

            channel.basicConsume(queueName, true, ingoingCallback, consumerTag -> {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
