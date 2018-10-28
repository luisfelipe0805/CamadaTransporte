package br.edu.ifg.network.emulator;

import java.util.Random;
import java.util.Scanner;

/* ******************************************************************
ALTERNATING BIT AND GO-BACK-N NETWORK EMULATOR: VERSION 1.1  J.F.Kurose

  This code should be used for PA2, unidirectional or bidirectional
  data transfer protocols (from A to B. Bidirectional transfer of data
  is for extra credit and is not required).  Network properties:
  - one way network delay averages five time units (longer if there
    are other messages in the channel for GBN), but can be larger
  - packets can be corrupted (either the header or the data portion)
    or lost, according to user-defined probabilities
  - packets will be delivered in the order in which they were sent
    (although some can be lost).
**********************************************************************/
/**
 * The code below emulates the layer 3 and below network environment: - emulates
 * the tranmission and delivery (possibly with bit-level corruption and packet
 * loss) of packets across the layer 3/4 interface - handles the
 * starting/stopping of a timer, and generates timer interrupts (resulting in
 * calling students timer handler). - generates message to be sent (passed from
 * later 5 to 4)
 * 
 * THERE IS NOT REASON THAT ANY STUDENT SHOULD HAVE TO READ OR UNDERSTAND THE
 * CODE BELOW. YOU SHOLD NOT TOUCH, OR REFERENCE (in your code) ANY OF THE DATA
 * STRUCTURES BELOW. If you're interested in how I designed the emulator, you're
 * welcome to look at the code - but again, you should have to, and you
 * defeinitely should not have to modify
 *
 */
public class NetworkEmulator {

	// change to true if you're doing extra credit and write a routine called
	// B_output
	public static final boolean BIDIRECTIONAL = false;

	/* possible events: */
	public static final int TIMER_INTERRUPT = 0;
	public static final int FROM_LAYER5 = 1;
	public static final int FROM_LAYER3 = 2;
	public static final int OFF = 0;
	public static final int ON = 1;
	public static final int A = 0;
	public static final int B = 1;

	private Event evlist; /* the event list */

	private int TRACE = 1; /* for my debugging */
	private int nsim = 0; /* number of messages from 5 to 4 so far */
	private int nsimmax = 0; /* number of msgs to generate, then stop */
	private float time = 0.0f;
	private float lossprob; /* probability that a packet is dropped */
	private float corruptprob; /*
								 * probability that one bit is packet is flipped
								 */
	private float lambda; /* arrival rate of messages from layer 5 */
	private int ntolayer3; /* number sent into layer 3 */
	private int nlost; /* number lost in media */
	private int ncorrupt; /* number corrupted by media */
	private Random random;

	public NetworkEmulator() {

	}

	public int getTRACE() {
		return TRACE;
	}

	public void setTRACE(int tRACE) {
		TRACE = tRACE;
	}

	public float getTime() {
		return time;
	}

	public float getLambda() {
		return lambda;
	}

	public int getNtolayer3() {
		return ntolayer3;
	}

	public int getNlost() {
		return nlost;
	}

	public int getNcorrupt() {
		return ncorrupt;
	}

	public void runEmulator() {
		Event eventptr;
		Message msg2give = new Message();
		Packet pkt2give = new Packet();
		int i, j;
		TransportProtocol transportProtocol = new TransportProtocolImpl(this);

		init();
		transportProtocol.A_init();
		transportProtocol.B_init();

		while (true) {
			eventptr = evlist; /* get next event to simulate */
			if (eventptr == null)
				break;
			evlist = evlist.getNext(); /* remove this event from event list */
			if (evlist != null)
				evlist.setPrev(null);
			if (TRACE >= 2 && eventptr.getEvtype() == FROM_LAYER5 && nsim < nsimmax) {
				System.out.printf("\nEVENT time: %f,", eventptr.getEvtime());
				System.out.printf("  type: %d", eventptr.getEvtype());
				if (eventptr.getEvtype() == 0)
					System.out.printf(", timerinterrupt  ");
				else if (eventptr.getEvtype() == 1)
					System.out.printf(", fromlayer5 ");
				else
					System.out.printf(", fromlayer3 ");
				System.out.printf(" entity: %d\n", eventptr.getEventity());
			}
			time = eventptr.getEvtime(); /* update time to next event time */
			if (eventptr.getEvtype() == FROM_LAYER5 && nsim < nsimmax) {
				generate_next_arrival(); /* set up future arrival */
				/* fill in msg to give with string of same letter */
				j = nsim % 26;
				for (i = 0; i < 20; i++)
					msg2give.setData(i, (char) (97 + j));
				if (TRACE > 2)
					System.out.println("          MAINLOOP: data given to student: " + msg2give);

				nsim++;
				if (eventptr.getEventity() == A)
					transportProtocol.A_output(msg2give);
				else
					transportProtocol.B_output(msg2give);
			} else if (eventptr.getEvtype() == FROM_LAYER3) {
				pkt2give.setSeqnum(eventptr.getPktptr().getSeqnum());
				pkt2give.setAcknum(eventptr.getPktptr().getAcknum());
				pkt2give.setChecksum(eventptr.getPktptr().getChecksum());
				pkt2give.setPayload(eventptr.getPktptr().getPayload());
				if (eventptr.getEventity() == A) /* deliver packet by calling */
					transportProtocol
							.A_input(pkt2give); /* appropriate entity */
				else
					transportProtocol.B_input(pkt2give);
			} else if (eventptr.getEvtype() == TIMER_INTERRUPT) {
				if (eventptr.getEventity() == A)
					transportProtocol.A_timerinterrupt();
				else
					transportProtocol.B_timerinterrupt();
			} 
		}

		System.out.printf(" Simulator terminated at time %f\n after sending %d msgs from layer5\n", time, nsim);
	}

	/* initialize the simulator */
	private void init() {
		Scanner scanner = new Scanner(System.in);
		random = new Random(System.currentTimeMillis()); /* init random number generator */

		System.out.println("-----  Stop and Wait Network Simulator Version 1.1 -------- \n");
		System.out.print("Enter the number of messages to simulate: ");
		nsimmax = scanner.nextInt();
		System.out.print("Enter  packet loss probability [enter 0.0 for no loss]: ");
		lossprob = scanner.nextFloat();
		System.out.print("Enter packet corruption probability [0.0 for no corruption]: ");
		corruptprob = scanner.nextFloat();
		System.out.print("Enter average time between messages from sender's layer5 [ > 0.0]: ");
		lambda = scanner.nextFloat();
		System.out.print("Enter TRACE: ");
		TRACE = scanner.nextInt();

		scanner.close();

		ntolayer3 = 0;
		nlost = 0;
		ncorrupt = 0;

		time = 0.0f; /* initialize time to 0.0 */
		generate_next_arrival(); /* initialize event list */
	}

	/********************* EVENT HANDLINE ROUTINES *******/

	@SuppressWarnings("unused")
	private void generate_next_arrival() {
		double x;
		Event evptr = new Event();

		if (TRACE > 2)
			System.out.println("          GENERATE NEXT ARRIVAL: creating new arrival");

		x = lambda * random.nextFloat() * 2; /* x is uniform on [0,2*lambda] */
		/* having mean of lambda */
		evptr.setEvtime((float) (time + x));
		evptr.setEvtype(FROM_LAYER5);
		if (BIDIRECTIONAL && (random.nextFloat() > 0.5))
			evptr.setEventity(B);
		else
			evptr.setEventity(A);
		insertevent(evptr);
	}

	private void insertevent(Event p) {
		Event q, qold;

		if (TRACE > 2) {
			System.out.printf("            INSERTEVENT: time is %f\n", time);
			System.out.printf("            INSERTEVENT: future time will be %f\n", p.getEvtime());
		}
		// q points to header of list in which p struct inserted
		q = evlist;
		if (q == null) { /* list is empty */
			evlist = p;
			p.setNext(null);
			p.setPrev(null);
		} else {
			for (qold = q; q != null && p.getEvtime() > q.getEvtime(); q = q.getNext())
				qold = q;
			if (q == null) { /* end of list */
				qold.setNext(p);
				p.setPrev(qold);
				p.setNext(null);
			} else if (q.equals(evlist)) { /* front of list */
				p.setNext(evlist);
				p.setPrev(null);
				p.getNext().setPrev(p);
				evlist = p;
			} else { /* middle of list */
				p.setNext(q);
				p.setPrev(q.getPrev());
				q.getPrev().setNext(p);
				q.setPrev(p);
			}
		}
	}

	public void printevlist() {
		Event q;

		System.out.printf("--------------\nEvent List Follows:\n");
		for (q = evlist; q != null; q = q.getNext())
			System.out.printf("Event time: %f, type: %d entity: %d\n", q.getEvtime(), q.getEvtype(), q.getEventity());

		System.out.println("--------------");
	}

	/********************** Student-callable ROUTINES ***********************/

	/* called by students routine to cancel a previously-started timer */
	/* A or B is trying to stop timer */
	public void stoptimer(int AorB) {
		if (TRACE > 2)
			System.out.printf("          STOP TIMER: stopping timer at %f\n", time);

		for (Event q = evlist; q != null; q = q.getNext())
			if ((q.getEvtype() == TIMER_INTERRUPT && q.getEventity() == AorB)) {
				/* remove this event */
				if (q.getNext() == null && q.getPrev() == null)
					evlist = null; /* remove first and only event on list */
				else if (q.getNext() == null) /*
												 * end of list - there is one in
												 * front
												 */
					q.getPrev().setNext(null);
				else if (q == evlist) { /*
										 * front of list - there must be event
										 * after
										 */
					q.getNext().setPrev(null);
					evlist = q.getNext();
				} else { /* middle of list */
					q.getNext().setPrev(q.getPrev());
					q.getPrev().setNext(q.getNext());
				}
				return;
			}
		System.out.println("Warning: unable to cancel your timer. It wasn't running.");

	}

	/* A or B is trying to stop timer */
	public void starttimer(int AorB, float increment) {

		Event q;
		Event evptr;

		if (TRACE > 2)
			System.out.printf("          START TIMER: starting timer at %f\n", time);
		/*
		 * be nice: check to see if timer is already started, if so, then warn
		 */
		for (q = evlist; q != null; q = q.getNext())
			if ((q.getEvtype() == TIMER_INTERRUPT && q.getEventity() == AorB)) {
				System.out.println("Warning: attempt to start a timer that is already started");
				return;
			}

		/* create future event for when timer goes off */
		evptr = new Event();
		evptr.setEvtime(time + increment);
		evptr.setEvtype(TIMER_INTERRUPT);
		evptr.setEventity(AorB);
		insertevent(evptr);
	}

	/************************** TOLAYER3 ***************/
	public void tolayer3(int AorB, Packet packet) {
		Packet mypktptr;
		Event evptr, q;
		float lastime, x;

		ntolayer3++;

		/* simulate losses: */
		if (random.nextFloat() < lossprob) {
			nlost++;
			if (TRACE > 0)
				System.out.println("          TOLAYER3: packet being lost");
			return;
		}

		/*
		 * make a copy of the packet student just gave me since he/she may
		 * decide
		 */
		/* to do something with the packet after we return back to him/her */
		mypktptr = new Packet();
		mypktptr.setSeqnum(packet.getSeqnum());
		mypktptr.setAcknum(packet.getAcknum());
		mypktptr.setChecksum(packet.getChecksum());
		mypktptr.setPayload(packet.getPayload());
		if (TRACE > 2) {
			System.out.printf("          TOLAYER3: seq: %d, ack %d, check: %d ", mypktptr.getSeqnum(),
					mypktptr.getAcknum(), mypktptr.getChecksum());
			System.out.print(mypktptr.getPayload());
			System.out.println();
		}

		/* create future event for arrival of packet at the other side */
		evptr = new Event();
		evptr.setEvtype(FROM_LAYER3); /* packet will pop out from layer3 */
		evptr.setEventity((AorB + 1) % 2); /* event occurs at other entity */
		evptr.setPktptr(mypktptr); /* save ptr to my copy of packet */
		/*
		 * finally, compute the arrival time of packet at the other end. medium
		 * can not reorder, so make sure packet arrives between 1 and 10 time
		 * units after the latest arrival time of packets currently in the
		 * medium on their way to the destination
		 */
		lastime = time;
		 for (q=evlist; q!=null ; q = q.getNext()) 
			if ((q.getEvtype() == FROM_LAYER3 && q.getEventity() == evptr.getEventity()))
				lastime = q.getEvtime();
		evptr.setEvtime((float) (lastime + 1 + 9 * random.nextFloat()));

		/* simulate corruption: */
		if (random.nextFloat() < corruptprob) {
			ncorrupt++;
			if ((x = random.nextFloat()) < .75)
				mypktptr.setPayload(0, 'Z'); /* corrupt payload */
			else if (x < .875)
				mypktptr.setSeqnum(999999);
			else
				mypktptr.setAcknum(999999);
			if (TRACE > 0)
				System.out.println("          TOLAYER3: packet being corrupted");
		}

		if (TRACE > 2)
			System.out.println("          TOLAYER3: scheduling arrival on other side");
		insertevent(evptr);
	}

	public void tolayer5(int AorB, char[] datasent) {
		if (TRACE > 2)
			System.out.println("          TOLAYER5: data received: " + String.valueOf(datasent));

	}
}
