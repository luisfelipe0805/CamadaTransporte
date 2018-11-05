package br.edu.ifg.network.emulator;


public class TransportProtocolImpl extends TransportProtocol {

	public TransportProtocolImpl(NetworkEmulator networkEmulator) {
		super(networkEmulator);
	}

	private int SeqA=0;
	private int AckB=0;
	private int TamanhoPacket=20;	
	private Packet pacote_A;
	private Packet pacote_B;
	
	/**
	 * Funcao que ira transformar a Mensage em packet, onde sera enviada pelo metodo tolayer3
	 * para isso usara uma variavel global pacote_A e preenchera com o cheacksum,seq e payload 
	 */
	@Override
	public void A_output(Message message) {
		
		//tranferir string para um pacote
		pacote_A=PassarStringParaPacket(message);
		
		//Montagem do checksum
		pacote_A.setChecksum(CalcularCheackSum(pacote_A));
				
		//Uso do seq
		//uso da variavel privada A como parametro pra montar o valor do seq;
		CalcularValorSeqnum();
		pacote_A.setSeqnum(SeqA);
		
		
		EnviarPacote();
		
		
		
		
		
	}

	@Override
	public void B_output(Message message) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Funcao responsavel pela recepsao do pacote vinda do lado B,
	 * Onde sera validado se o pacote foi recebido corretamente 
	 */
	@Override
	public void A_input(Packet packet) {
		//caso seja uma mensagem de confirmacao recebimento
		if(packet.getPayload()!=null) {
			System.out.println("--A:  Confirmacao do pacote recebida com sucesso");
			
			//para cronometro do timeout
			networkEmulator.stoptimer(0);
			
			int valorEsperado=SeqA+TamanhoPacket;
			if(packet.getAcknum()==valorEsperado) {
				System.out.println("--A:  Acknum Correto!!!");
				
				
				//Atualizacoa do pacote 
				SeqA=packet.getAcknum();
				System.out.println("\n\n");
				
			}else {
				System.out.println("--A:  Acknum Incorreto!!!");
				System.out.println("\n\n");
				System.out.println("--A:  Iniciar Reenvio do pacote");
				EnviarPacote();
			}
			
		//caso noa seja uma mensagem de confirmacao recebimento	
		}else {
			System.out.println("--A:  Este pacote nao confirmacao de recebimento");
		}
		
	}

	@Override
	public void A_timerinterrupt() {
		System.out.println("--A:  TimeOut Estourado!! "
				+ "\n Reenviando pacote com seq: " +pacote_A.getSeqnum());
		EnviarPacote();
		
		
	}
	
	
	
	/**
	 * Funcao para inicializar o sistema do lado A
	 */
	@Override
	public void A_init() {
		// TODO Auto-generated method stub
		System.out.println("--A:  Iniciando sistema lado A \n");
		pacote_A = new Packet();
	}

	
	/**
	 * Funcao responsavel pela recepsao do pacote vinda do lado A,
	 * Onde sera validado se seria o pacote correto a ser recebido
	 */
	@Override
	public void B_input(Packet packet) {
		//valida o seq do pacote recebido
		if(packet.getSeqnum()==AckB) {
			System.out.println("--B:  Pacote seq: "+packet.getSeqnum()+" recebido com sucesso");
			
			//com o seq valido a funcao ira validar a sua string
			if(ValidaCheackSum(packet)) {
				System.out.println("--B:  Pacote validado com sucesso");
				
				//com o pacote valido ira calcular o acknum
				pacote_B.setAcknum(CalcularValoracknum());
			}else {
				System.out.println("--B:  Pacote corrompido");
				pacote_B.setAcknum(AckB);
			}
			
		}else {
			System.err.println("--B:  Pacote com seq incorreto, seq: "+packet.getSeqnum());
			pacote_B.setAcknum(AckB);
		}
				
		//envio da confirmacoa do pacote recebido
		System.out.println("--B:  Envio da confirmacao do recebimento"+pacote_B.getAcknum());
		networkEmulator.tolayer3(1, pacote_B);		
		
	}

	@Override
	public void B_timerinterrupt() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Funcao para inicializar o sistema do lado B
	 */
	@Override
	public void B_init() {
		System.out.println("--B:  Iniciando sistema lado B \n");
		pacote_B = new Packet();
	}
	
		
	/**
	 * Funcao para passar a informacao do atributo Data uma Message para o atributo Payload de um Packet
	 * @param Um Objeto do tipo Message 
	 * @return Retorna um objeto do tipo Packet com Payload preenchido.
	 */
	private  Packet PassarStringParaPacket(Message message){
		Packet pacote = new Packet();
		for (int i=0;i<20;i++) {
			pacote.setPayload(i, message.getData(i));
		}		
		return pacote;
	}
	
	/**
	 * Funcao que calcula o somatorio de todo vetor de char
	 * @param Um Objeto do tipo Packet com algum dado em Payload
	 * @return valor do tipo integer 
	 */
	private int CalcularCheackSum (Packet pacote) {
		int result=0;
	
		for (int ipayload=0;ipayload<20;ipayload++) {
			//conversao de em int de cada char
			int numero;
			numero=pacote.getPayload(ipayload);
			result=result + numero;
		}	
		return result;
	}
	
	/**
	 * Calcular valor do atributo seqSum de um pacote
	 * @return valor do tipo integer 
	 */
	private int CalcularValorSeqnum() {
		int result=SeqA;
		return result;
	}
	
	/**
	 * Calcular valor do atributo Checksum de um pacote
	 * @return valor do tipo integer 
	 */
	private int CalcularValoracknum() {
		AckB=AckB+TamanhoPacket;
		int result=AckB;
		return result;
	}
	
	/**
	 * Valida o valor do CheackSum com o valor da String Payload
	 * para isso ele utiliza fucao CalcularCheackSum 
	 * @param packet to tipo pocte 
	 * @return true para o pacote com sua string valida false para o pacote com String nao valida
	 */
	private boolean ValidaCheackSum(Packet packet) {
		//Validando o payload do pacote 
			if(CalcularCheackSum(packet)==packet.getChecksum()) {
				return true;
			}else {
				return false;
			}
		
	}
	
	
	private void EnviarPacote() {
		//StartTimer
		networkEmulator.starttimer(0, 1000);
				
		//enviando pacote		
		System.out.println("--A:  Enviando pacote com seq "+pacote_A.getSeqnum() );
		networkEmulator.tolayer3(0, pacote_A);
	}
	
	
}




