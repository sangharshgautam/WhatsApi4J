package org.sangharsh.demo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import net.sumppen.whatsapi4j.MessageProcessor;
import net.sumppen.whatsapi4j.ProtocolNode;
import net.sumppen.whatsapi4j.WhatsApi;
import net.sumppen.whatsapi4j.WhatsAppException;
import net.sumppen.whatsapi4j.message.Message;
import net.sumppen.whatsapi4j.message.TextMessage;
@Service
public class IncomingMessageProcessor implements MessageProcessor {

	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private Queue queue;
	
	@Autowired
	private WhatsApi whatsApi;
	
	public void processMessage(ProtocolNode message) {
		String from = message.getAttribute("from");
		if(message.getAttribute("type").equals("text")) {
			ProtocolNode body = message.getChild("body");
			String hex = new String(body.getData());
			byte[] bytes;
			try {
				bytes = Hex.decodeHex(hex.toCharArray());
				System.out.println("MESSAGE: "+new String(bytes, "UTF-8"));
			} catch (DecoderException e) {
			} catch (UnsupportedEncodingException e) {
			}
			String participant = message.getAttribute("participant");
			if(participant != null && !participant.isEmpty()) {
				//Group message
				System.out.println(participant+"("+from+") ::: "+hex);
			} else {
				queueMsg(new WaQueueMessage(from, hex));
				System.out.println(from+" ::: "+hex);
			}
		}
		if(message.getAttribute("type").equals("media")) {
			ProtocolNode media = message.getChild("media");
			String type = media.getAttribute("type");
			if(type.equals("location")) {
				System.out.println(from+" ::: ("+media.getAttribute("longitude")+","+media.getAttribute("latitude")+")");
			} else if (type.equals("image")) {
				String caption = media.getAttribute("caption");
				if(caption == null)
					caption = "";
				String pathname = "preview-image-"+(new Date().getTime())+".jpg";
				System.out.println(from+" ::: "+caption+"(image): "+media.getAttribute("url"));
				byte[] preview = media.getData();
				writePreview(pathname, preview);
			} else if (type.equals("video")) {
				String caption = media.getAttribute("caption");
				if(caption == null)
					caption = "";
				String pathname = "preview-video-"+(new Date().getTime())+".jpg";
				System.out.println(from+" ::: "+caption+"(video): "+media.getAttribute("url"));
				byte[] preview = media.getData();
				writePreview(pathname, preview);
			} else {
				System.out.println(from+" ::: media/"+type);
			}
			
		}
	}

	private void queueMsg(final WaQueueMessage queueMessage) {
		/*try {
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://activemq-waclient.rhcloud.com");

            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue("TEST.FOO");

            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            ObjectMessage message = session.createObjectMessage(queueMessage);

            // Tell the producer to send the message
            System.out.println("Sent queue: "+ queueMessage.toString());
            producer.send(message);

            // Clean up
            session.close();
            connection.close();
        }
        catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }*/
		this.jmsTemplate.send(queue,new MessageCreator() {

			public ObjectMessage createMessage(Session session) throws JMSException {
				ObjectMessage message = session.createObjectMessage(queueMessage);
		        return message;
			}

	    });
	}

	private void writePreview(String pathname, byte[] preview) {
		Path path = Paths.get(pathname);
		try {
			Files.write(path, preview);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Preview: "+path.toString());
	}

	public void processMessage(Message message) {

		//TODO add all supported message types
		switch(message.getType()) {
		case TEXT:
			TextMessage msg = (TextMessage)message;
			if(msg.getGroupId() != null && !msg.getGroupId().isEmpty()) {
				//Group message
				System.out.println(msg.getDate()+" :: "+msg.getFrom()+"("+msg.getGroupId()+"): "+msg.getText());
			} else {
				//Private message
				//queueMsg(new WaQueueMessage(msg.getFrom(), msg.getText()));
				try {
					whatsApi.sendMessage(msg.getFrom(), msg.getText());
				} catch (WhatsAppException e) {
					e.printStackTrace();
				}
				System.out.println(msg.getDate()+" :: "+msg.getFrom()+" : "+msg.getText());
			}
			break;
		default:
			processMessage(message.getProtocolNode());
		}
	}

}
