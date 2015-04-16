package net.sumppen.whatsapi4j.message;

import java.util.Date;

import org.apache.log4j.Logger;

import net.sumppen.whatsapi4j.ProtocolNode;

public class BasicMessage implements Message {

	private final MessageType type;
	private final ProtocolNode node;
	private final String from;
	private final String groupId;
	private final Date date;
	protected final Logger log = Logger.getLogger(BasicMessage.class);
	
	public BasicMessage(MessageType type, ProtocolNode node, String from, String groupId) {
		this.type = type;
		this.node = node;
		this.from = from;
		this.groupId = groupId;
		String t = node.getAttribute("t");
		log.debug("t="+t);
		if(t != null) {
			long tl = Long.parseLong(t)*1000;
			this.date = new Date(tl);
		} else {
			this.date = new Date();
		}
		log.debug("date = "+date);
	}
	
	public MessageType getType() {
		return type;
	}

	public ProtocolNode getProtocolNode() {
		return node;
	}

	public String getFrom() {
		return from;
	}

	public String getGroupId() {
		return groupId;
	}

	public Date getDate() {
		return date;
	}

}
