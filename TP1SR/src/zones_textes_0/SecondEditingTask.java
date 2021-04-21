package zones_textes_0;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

public class SecondEditingTask {

    private static final String QUEUE_NAME ="text-section2";


    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory() ;
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            String message = String.join(" ",args);
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "' from task 2");
        }

    }
}
