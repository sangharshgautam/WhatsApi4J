package net.sumppen.whatsapi4j;

import net.sumppen.whatsapi4j.message.Message;

public interface MessageProcessor {
	public void processMessage(Message message);
}
