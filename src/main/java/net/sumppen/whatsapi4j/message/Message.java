package net.sumppen.whatsapi4j.message;

import java.util.Date;

import net.sumppen.whatsapi4j.ProtocolNode;

public interface Message {
	public String getFrom();
	public MessageType getType();
	public Date getDate();
	public ProtocolNode getProtocolNode();
}
