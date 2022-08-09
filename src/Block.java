// Vartan Artyunyan
// Martikelnummer 5120007

public class Block {
	
	int count;
	String data;

	public Block(int count, String data) {
		this.count = count;
		this.data = data;
	}
	
	public byte[] getBytes() {
		return data.getBytes();
	}
	
	public String toString() {
		return data;
	}

}
