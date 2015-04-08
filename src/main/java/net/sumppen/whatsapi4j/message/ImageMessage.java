package net.sumppen.whatsapi4j.message;

import net.sumppen.whatsapi4j.ProtocolNode;

public class ImageMessage extends MediaMessage {

	public ImageMessage(ProtocolNode node, String from,
			String groupId) {
		super(MessageType.IMAGE, node, from, groupId);
	}

}
