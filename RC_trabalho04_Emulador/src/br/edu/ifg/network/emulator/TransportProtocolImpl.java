package br.edu.ifg.network.emulator;


public class TransportProtocolImpl extends TransportProtocol {

	public TransportProtocolImpl(NetworkEmulator networkEmulator) {
		super(networkEmulator);
	}

	private int A=0;
	private int B=0;
	private int tamanhoPacket=20;	
	
	
	@Override
	public void A_output(Message message) {
		Packet pacote = new Packet();
		//tranferir string para um pacote
		pacote=PassarStringParaPacket(message);
		
		//Montagem do checksum
		pacote.setChecksum(CalcularCheackSum (pacote));
		
		
		//Uso do seq
		//uso da variavel privada A como parametro pra montar o valor do seq;
		CalcularValorSeqsum();
			
		CalcularValoracknum();
		pacote.setSeqnum(A);
		
		
		System.out.println("Enviando pacote com seq "+pacote.getSeqnum() +" ");
		
		
		System.out.println("start a new timer!");
		
	}

	@Override
	public void B_output(Message message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void A_input(Packet packet) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void A_timerinterrupt() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void A_init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void B_input(Packet packet) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void B_timerinterrupt() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void B_init() {
		// TODO Auto-generated method stub
		
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
	private int CalcularValorSeqsum() {
		int result=A;
		return result;
	}
	
	/**
	 * Calcular valor do atributo Checksum de um pacote
	 * @return valor do tipo integer 
	 */
	private int CalcularValoracknum() {
		B=B+tamanhoPacket;
		int result=B;
		A=B;
		return result;
	}
}







////conversao de em binaria de cada char
//int valorBinario = pacote.getPayload(ipayload);
//String binaria = Integer.toBinaryString(valorBinario);
////System.out.println(binaria);
//
//int i = Integer.parseInt(binaria);
//System.out.println(i);