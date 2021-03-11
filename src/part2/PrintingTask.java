package part2;
import com.rabbitmq.client.ConnectionFactory ;
import com.rabbitmq.client.Connection ;
import com.rabbitmq.client.Channel ;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class PrintingTask {
    private static final String QUEUE1 ="text-section1";
    private static final String QUEUE2 ="text-section2";
    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();


        channel.queueDeclare(QUEUE1, false, false, false, null);
        channel.queueDeclare(QUEUE2, false, false, false, null);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        // receiving message from queue1
        DeliverCallback firstQueueCallback = (consumerTag, delivery) -> {
            String message = new String (delivery.getBody() ,"UTF-8" );
            System.out.println(" [x] Received '" + message + "' from first queue");
        };
        channel.basicConsume(QUEUE1, true, firstQueueCallback, consumerTag -> { });

        // receiving message from queue2
        DeliverCallback secondQueueCallback = (consumerTag, delivery) -> {
            String message = new String (delivery.getBody() ,"UTF-8" );
            System.out.println(" [x] Received '" + message + "' from second queue");
        };
        channel.basicConsume(QUEUE2, true, secondQueueCallback, consumerTag -> { });

    }
}
