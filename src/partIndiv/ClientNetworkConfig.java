package partIndiv;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ClientNetworkConfig {


    //used to update the text Area
    private ClientView clientView;
    private Connection connection;
    private Channel channel;
    private String outgoingQueue;
    private String host = "localhost";

    private String updateExchange = "update-exchange" ;
    private Connection updateConnection;
    private Channel updateChannel ;
    private String updateQueue ;

    public ClientNetworkConfig(String queueName,ClientView clientView) {
        this.outgoingQueue = queueName;
        this.clientView = clientView ;
    }


    public void initConnection() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(this.host);

            initOutgoingConnection(factory);
            initIncomingConnection(factory) ;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void initOutgoingConnection( ConnectionFactory factory) throws Exception {
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(outgoingQueue, false, false, false, null);

        System.out.println("Ready to sent messages from "+ outgoingQueue);
    }



    public void initIncomingConnection( ConnectionFactory factory) throws Exception
    {
        updateConnection = factory.newConnection();
        updateChannel = updateConnection.createChannel();
        updateChannel.exchangeDeclare(updateExchange, "fanout");
        updateQueue = updateChannel.queueDeclare().getQueue();

        updateChannel.queueBind(updateQueue, updateExchange, "");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {

            String message = new String(delivery.getBody(), "UTF-8");
            triggerClientUpdate(message) ;
            System.out.println(" [x] Received '"+message+"'");
        };

        updateChannel.basicConsume(updateQueue,true,deliverCallback,consumerTag -> {});

    }


    public void publishMessage(String message) {
        try {
            this.channel.basicPublish("", outgoingQueue, null, message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void triggerClientUpdate(String newText)
    {
        System.out.println("New text : "+newText);
        this.clientView.updateTextFieldNetwork(newText);
    }

}
