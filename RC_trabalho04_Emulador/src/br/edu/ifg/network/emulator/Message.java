package br.edu.ifg.network.emulator;

/* a "msg" is the data unit passed from layer 5 (teachers code) to layer  */
/* 4 (students' code).  It contains the data (characters) to be delivered */
/* to layer 5 via the students transport level protocol entities.         */
public class Message {

	private char[] data;

	public Message() {
		this.data = new char[20];
	}

	public char[] getData() {
		return data;
	}

	public char getData(int index) {
		return data[index];
	}

	public void setData(char[] data) {
		for (int i = 0; i < data.length; i++)
			this.data[i] = data[i];
	}

	public void setData(int index, char value) {
		data[index] = value;
	}

	@Override
	public String toString() {
		return new String(data);
	}
}
