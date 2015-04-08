package net.sumppen.whatsapi4j.message;

import net.sumppen.whatsapi4j.ProtocolNode;

public interface Message {
	public String getFrom();
	public MessageType getType();
	public ProtocolNode getProtocolNode();
}
