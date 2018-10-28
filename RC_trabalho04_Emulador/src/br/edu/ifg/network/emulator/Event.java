package br.edu.ifg.network.emulator;

public class Event {

	private float evtime; /* event time */
	private int evtype; /* event type code */
	private int eventity; /* entity where event occurs */
	private Packet pktptr; /* packet (if any) assoc w/ this event */
	private Event prev;
	private Event next;

	public Event() {

	}

	public float getEvtime() {
		return evtime;
	}

	public void setEvtime(float evtime) {
		this.evtime = evtime;
	}

	public int getEvtype() {
		return evtype;
	}

	public void setEvtype(int evtype) {
		this.evtype = evtype;
	}

	public int getEventity() {
		return eventity;
	}

	public void setEventity(int eventity) {
		this.eventity = eventity;
	}

	public Packet getPktptr() {
		return pktptr;
	}

	public void setPktptr(Packet pktptr) {
		this.pktptr = pktptr;
	}

	public Event getPrev() {
		return prev;
	}

	public void setPrev(Event prev) {
		this.prev = prev;
	}

	public Event getNext() {
		return next;
	}

	public void setNext(Event next) {
		this.next = next;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + eventity;
		result = prime * result + Float.floatToIntBits(evtime);
		result = prime * result + evtype;
		result = prime * result + ((next == null) ? 0 : next.hashCode());
		result = prime * result + ((pktptr == null) ? 0 : pktptr.hashCode());
		result = prime * result + ((prev == null) ? 0 : prev.hashCode());
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
		Event other = (Event) obj;
		if (eventity != other.eventity)
			return false;
		if (Float.floatToIntBits(evtime) != Float.floatToIntBits(other.evtime))
			return false;
		if (evtype != other.evtype)
			return false;
		if (next == null) {
			if (other.next != null)
				return false;
		} else if (!next.equals(other.next))
			return false;
		if (pktptr == null) {
			if (other.pktptr != null)
				return false;
		} else if (!pktptr.equals(other.pktptr))
			return false;
		if (prev == null) {
			if (other.prev != null)
				return false;
		} else if (!prev.equals(other.prev))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Event time: " + evtime + ", type: " + evtype + ", entity: " + eventity;
	}

}
