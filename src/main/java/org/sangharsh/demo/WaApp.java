package org.sangharsh.demo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import net.sumppen.whatsapi4j.MessageProcessor;
import net.sumppen.whatsapi4j.WhatsApi;
import net.sumppen.whatsapi4j.WhatsAppException;
import net.sumppen.whatsapi4j.example.ExampleMessagePoller;

public class WaApp {

	public static void main(String[] args) throws UnknownHostException, IOException {
		// init spring context
		ApplicationContext ctx = new ClassPathXmlApplicationContext("app-context.xml");

		String password = "4txDKz+/hCfY2V/B1IHJ2034ydQ=";
		String identity = decodeIdentity("%C3%AD%094%C3%B6%C2%A7a%C2%897WX%21%C2%BD%0D%5E%C3%8F%C3%88%C2%98%3C%C2%BB%2B");
		WhatsApi wa = null;
		try {
			wa = ctx.getBean("whatsApi", WhatsApi.class);
			MessageProcessor mp = ctx.getBean("incomingMsgProcessor", IncomingMessageProcessor.class);
			wa.setNewMessageBind(mp);
			if (!wa.connect()) {
				System.out.println("Failed to connect to WhatsApp");
				System.exit(1);
			}
			if (password != null) {
				wa.loginWithPassword(password);
				ExampleMessagePoller poller = new ExampleMessagePoller(wa);
				poller.start();
				//wa.sendMessage("447786092600", "Genie Online");
			}
		} catch (WhatsAppException e) {
			e.printStackTrace();
		} finally {
			if (wa != null) {
				//wa.disconnect();
			}
			//((ClassPathXmlApplicationContext) ctx).close();
		}

		// close spring application context
	}

	private static String decodeIdentity(String encodedIdentity) throws UnsupportedEncodingException {
		return URLDecoder.decode(encodedIdentity, StandardCharsets.UTF_8.toString());
	}
}
