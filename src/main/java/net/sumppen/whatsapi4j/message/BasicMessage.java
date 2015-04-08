package net.sumppen.whatsapi4j.message;

import net.sumppen.whatsapi4j.ProtocolNode;

public class BasicMessage implements Message {

	private final MessageType type;
	private final ProtocolNode node;
	private final String from;
	private final String groupId;
	
	public BasicMessage(MessageType type, ProtocolNode node, String from, String groupId) {
		this.type = type;
		this.node = node;
		this.from = from;
		this.groupId = groupId;
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

}
