package br.edu.ifg.network.emulator;

import java.util.Arrays;

/* a packet is the data unit passed from layer 4 (students code) to layer */
/* 3 (teachers code).  Note the pre-defined packet structure, which all   */
/* students must follow. */
public class Packet {

	private int seqnum;
	private int acknum;
	private int checksum;
	private char[] payload;

	public Packet() {
		this.payload = new char[20];
	}

	public int getSeqnum() {
		return seqnum;
	}

	public void setSeqnum(int seqnum) {
		this.seqnum = seqnum;
	}

	public int getAcknum() {
		return acknum;
	}

	public void setAcknum(int acknum) {
		this.acknum = acknum;
	}

	public int getChecksum() {
		return checksum;
	}

	public void setChecksum(int checksum) {
		this.checksum = checksum;
	}

	public char[] getPayload() {
		return payload;
	}

	public char getPayload(int index) {
		return payload[index];
	}

	public void setPayload(char[] payload) {
		for (int i = 0; i < payload.length; i++)
			this.payload[i] = payload[i];
	}

	public void setPayload(int index, char value) {
		this.payload[index] = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + acknum;
		result = prime * result + checksum;
		result = prime * result + Arrays.hashCode(payload);
		result = prime * result + seqnum;
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
		Packet other = (Packet) obj;
		if (acknum != other.acknum)
			return false;
		if (checksum != other.checksum)
			return false;
		if (!Arrays.equals(payload, other.payload))
			return false;
		if (seqnum != other.seqnum)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Packet [seqnum=" + seqnum + ", acknum=" + acknum + ", checksum=" + checksum + ", payload="
				+ String.valueOf(payload) + "]";
	}

}
