package org.sangharsh.demo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import net.sumppen.whatsapi4j.MessageProcessor;
import net.sumppen.whatsapi4j.WhatsApi;
import net.sumppen.whatsapi4j.WhatsAppException;
import net.sumppen.whatsapi4j.example.ExampleMessagePoller;

public class MsgProcessor {

	public static void main(String[] args) throws UnknownHostException, IOException {
		String username = "919958674455";
		String password = "DtxDKz+/hCfY2V/B1IHJ2034ydQ=";
		String identity = decodeIdentity("%C3%AD%094%C3%B6%C2%A7a%C2%897WX%21%C2%BD%0D%5E%C3%8F%C3%88%C2%98%3C%C2%BB%2B");
		String nickname = "sangharsh";
		WhatsApi wa = null;
		try {
			wa = new WhatsApi(username, identity, nickname);
			MessageProcessor mp = new IncomingMessageProcessor();
			wa.setNewMessageBind(mp);
			if(!wa.connect()) {
				System.out.println("Failed to connect to WhatsApp");
				System.exit(1);
			}
			if(password != null) {
				wa.loginWithPassword(password);
				ExampleMessagePoller poller = new ExampleMessagePoller(wa);
				poller.start();
				wa.sendMessage("447786092600", "Genie Online");
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (WhatsAppException e) {
			e.printStackTrace();
		}finally{
			if(wa!=null){
//				wa.disconnect();
			}
		}
		
	}
	private static String decodeIdentity(String encodedIdentity) throws UnsupportedEncodingException{
		return URLDecoder.decode(encodedIdentity, StandardCharsets.UTF_8.toString());
	}
}
