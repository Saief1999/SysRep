package logging_system;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;


public class EmitLogDirect {
    private static String EXCHANGE_NAME = "direct_logs" ;




    private static String getSeverity(String [] argv)
    {
    return (argv.length== 0)?"info":argv[0].substring(0,argv[0].length());
    }

    private static String getMessage(String [] argv)
    {
        return (argv.length==0)?"default message":String.join(" ",Arrays.copyOfRange(argv, 1, argv.length));
    }

    public static void main(String[] argv) throws Exception{

        ConnectionFactory factory = new ConnectionFactory() ;
        factory.setHost("localhost");

    try (
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            ){
            channel.exchangeDeclare(EXCHANGE_NAME,"direct");
            String severity = getSeverity(argv);
            String message = getMessage(argv);

            System.out.println("severity : "+severity);
            System.out.println("message : " +message);

            channel.basicPublish(EXCHANGE_NAME,severity,null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '"+severity+"':'"+message+"'");
    }

    }
}
