package at.tugraz.ist.agileconfigurator.resourceoptimizer.models;

public class ProtocolsKnowledgeBase {
	
	// DATA ENCODING
	public static int  numberOfDataEncodingProtocols = 4;
	
	public static Protocol [] dataProtocolKnowledgeBase= new Protocol [numberOfDataEncodingProtocols];
	
	// CONNECTIVITY
	public static int  numberOfConnectivityProtocols = 4;
		
	public static Protocol [] connetivityProtocolKnowledgeBase= new Protocol [numberOfConnectivityProtocols];
	
	public ProtocolsKnowledgeBase() {
		super();
		
		for (int i=0;i<numberOfDataEncodingProtocols;i++)
			this.dataProtocolKnowledgeBase[i] = new Protocol();
		
		for (int i=0;i<numberOfConnectivityProtocols;i++)
			this.connetivityProtocolKnowledgeBase[i] = new Protocol();
		
	    // DATA ENCODING PROTOCOLS
		//0		CBOR  				+++
		//1		XML					++
		//2		JSON				+
		//3		ProtocolBuffers		++
		this.dataProtocolKnowledgeBase[0].setCost(3);
		this.dataProtocolKnowledgeBase[1].setCost(2);
		this.dataProtocolKnowledgeBase[2].setCost(1);
		this.dataProtocolKnowledgeBase[3].setCost(2);
		
		
		this.dataProtocolKnowledgeBase[0].setPerformance(4);
		this.dataProtocolKnowledgeBase[1].setPerformance(6);
		this.dataProtocolKnowledgeBase[2].setPerformance(7);
		this.dataProtocolKnowledgeBase[3].setPerformance(9);
		
		this.dataProtocolKnowledgeBase[0].setReliability(4);
		this.dataProtocolKnowledgeBase[1].setReliability(2);
		this.dataProtocolKnowledgeBase[2].setReliability(4);
		this.dataProtocolKnowledgeBase[3].setReliability(8);
		
		
		// CONNECTIVITIY PROTOCOLS
		connetivityProtocolKnowledgeBase[0].setCost(2);
		connetivityProtocolKnowledgeBase[1].setCost(5);
		connetivityProtocolKnowledgeBase[2].setCost(1);
		connetivityProtocolKnowledgeBase[3].setCost(8);
		
		connetivityProtocolKnowledgeBase[0].setPerformance(2);
		connetivityProtocolKnowledgeBase[1].setPerformance(5);
		connetivityProtocolKnowledgeBase[2].setPerformance(6);
		connetivityProtocolKnowledgeBase[3].setPerformance(8);
		
		connetivityProtocolKnowledgeBase[0].setReliability(2);
		connetivityProtocolKnowledgeBase[1].setReliability(5);
		connetivityProtocolKnowledgeBase[2].setReliability(6);
		connetivityProtocolKnowledgeBase[3].setReliability(8);
		
	}

	public Protocol[] getDataProtocolKnowledgeBase() {
		return this.dataProtocolKnowledgeBase;
	}

	public void setDataProtocolKnowledgeBase(Protocol[] dataProtocolKnowledgeBase) {
		this.dataProtocolKnowledgeBase = dataProtocolKnowledgeBase;
	}

	public Protocol[] getConnetivityProtocolKnowledgeBase() {
		return connetivityProtocolKnowledgeBase;
	}

	public void setConnetivityProtocolKnowledgeBase(Protocol[] connetivityProtocolKnowledgeBase) {
		this.connetivityProtocolKnowledgeBase = connetivityProtocolKnowledgeBase;
	}
	
	
}
