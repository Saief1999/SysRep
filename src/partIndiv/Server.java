package partIndiv;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class Server {
    private static String QUEUE1 = "incoming-text1"; // listens for incoming messages from Client 1
    private static String QUEUE2 = "incoming-text2"; // listens for incoming messages from Client 2
    private static Channel outGoingChannel;

    private static String updateExchange = "update-exchange";

    private static void setQueueNames(String queueName1, String queueName2) {
        QUEUE1 = queueName1;
        QUEUE2 = queueName2;
    }

    private static void initGlobalSettings() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        initIncomingConnection(factory, QUEUE1);
        initIncomingConnection(factory, QUEUE2);
        initOutgoingConnection(factory);
        System.out.println("Listening to messages... ( CTRL+C to Cancel )");

    }

    private static void initIncomingConnection(ConnectionFactory factory, String queueName) {


        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(queueName, false, false, false, null);

            DeliverCallback ingoingCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "' from " + queueName);
                updateClients(message);
            };

            channel.basicConsume(queueName, true, ingoingCallback, consumerTag -> {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initOutgoingConnection(ConnectionFactory factory) {
        try {
            Connection connection = factory.newConnection();
            outGoingChannel = connection.createChannel();
            outGoingChannel.exchangeDeclare(updateExchange, "fanout");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void updateClients(String message) {
        try {
            System.out.println("updating started with "+message) ;
            outGoingChannel.basicPublish(updateExchange, "", null, message.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

        String queue1 = (args.length < 1) ? "incoming-text1" : args[0];
        String queue2 = (args.length < 2) ? "incoming-text2" : args[1];
        setQueueNames(queue1, queue2);

        initGlobalSettings();
    }
}
