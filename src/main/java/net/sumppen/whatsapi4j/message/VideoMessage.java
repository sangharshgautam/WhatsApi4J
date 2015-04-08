package net.sumppen.whatsapi4j.message;

import net.sumppen.whatsapi4j.ProtocolNode;

public class VideoMessage extends MediaMessage {

	public VideoMessage(ProtocolNode node, String from,
			String groupId) {
		super(MessageType.VIDEO, node, from, groupId);
	}

}
