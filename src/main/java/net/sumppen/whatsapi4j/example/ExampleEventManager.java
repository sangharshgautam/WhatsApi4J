package net.sumppen.whatsapi4j.example;

import java.util.Map;

import net.sumppen.whatsapi4j.AbstractEventManager;
import net.sumppen.whatsapi4j.events.Event;

public class ExampleEventManager extends AbstractEventManager {
	@Override
	public void fireEvent(String event, Map<String, Object> eventData) {
		if(event.equals(AbstractEventManager.EVENT_UNKNOWN)) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		sb.append("Event "+event+": ");
		for(String key: eventData.keySet()) {
			if(!first) {
				sb.append(",");
			} else {
				first = false;
			}
			sb.append(key);
			sb.append("=");
			sb.append(eventData.get(key));
		}
		System.out.println(sb.toString());
		if(event.equals("disconnect")) {
			ExampleApplication.running=false;
		}
	}

	public void fireEvent(Event event) {
		System.out.println(event.toString());
		
	}

}
