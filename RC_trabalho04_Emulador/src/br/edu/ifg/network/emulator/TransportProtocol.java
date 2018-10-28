package br.edu.ifg.network.emulator;

/********* STUDENTS WRITE THE NEXT SEVEN ROUTINES *********/
public abstract class TransportProtocol {
	
	protected NetworkEmulator networkEmulator;
	
	public TransportProtocol(NetworkEmulator networkEmulator) {
		super();
		this.networkEmulator = networkEmulator;
	}

	/* called from layer 5, passed the data to be sent to other side */
	public abstract void A_output(Message message);

	/* need be completed only for extra credit */
	public abstract void B_output(Message message);

	/* called from layer 3, when a packet arrives for layer 4 */
	public abstract void A_input(Packet packet);

	/* called when A's timer goes off */
	public abstract void A_timerinterrupt();

	/* the following routine will be called once (only) before any other */
	/* entity A routines are called. You can use it to do any initialization */
	public abstract void A_init();

	/* Note that with simplex transfer from a-to-B, there is no B_output() */

	/* called from layer 3, when a packet arrives for layer 4 at B */
	public abstract void B_input(Packet packet);

	/* called when B's timer goes off */
	public abstract void B_timerinterrupt();

	/* the following rouytine will be called once (only) before any other */
	/* entity B routines are called. You can use it to do any initialization */
	public abstract void B_init();

}
