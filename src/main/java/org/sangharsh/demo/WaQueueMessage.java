package org.sangharsh.demo;

import java.io.Serializable;

public class WaQueueMessage implements Serializable{
	private final String from;
	private final String text;
	public WaQueueMessage(String from, String text) {
		super();
		this.from = from;
		this.text = text;
	}
	public String getFrom() {
		return from;
	}
	public String getText() {
		return text;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WaQueueMessage other = (WaQueueMessage) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "WaQueueMessage [from=" + from + ", text=" + text + "]";
	}
	
}
