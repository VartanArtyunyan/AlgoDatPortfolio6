// Vartan Artyunyan
// Martikelnummer 5120007

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Chain {

	Block Genesis;
	ArrayList<Block> blocks = new ArrayList<>();
	
	public void addBlock() {
		if(Genesis == null) setGenesis();
		
		Block pblock = blocks.get(blocks.size()-1);
		
		long time = System.currentTimeMillis();
		String nonce = generateNonce();
		String PoW = "00000" + hash(pblock);
		
		String data = time + "|" + nonce + "|" + PoW;
		
		Block nblock = new Block(pblock.count+1, data);
		
		blocks.add(nblock);
		
		System.out.println(nblock);
		
		
	}

	public String hash(Block block) {

		String output = "";
		MessageDigest digest;
		byte[] hash = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			hash = digest.digest(block.getBytes());
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}

		for (byte b : hash) {
			output += String.format("%02x", b);

		}
		return output;

	}
	
	public static String generateNonce() {
		char nonce[] = new char[8];
		String alphanumerics = "";
		
		for(int i = 48; i <= 122; i++) {
			if(i == 58) i = 65;
			if(i == 91) i = 97;
			
			alphanumerics += (char)i;		
		}
		
		String output = "";
		
		for(char c : nonce) {
			
			c = alphanumerics.charAt((int)(Math.random()*alphanumerics.length()));
			output += c;
		}
		
		return output;
	}
	
	public void setGenesis() {
		long time = System.currentTimeMillis();
		String nonce = generateNonce();
		String PoW = "0";
		String data = time + "|" + nonce + "|" + PoW;
		Block genesis = new Block(0, data);
		
		if(Genesis == null) { Genesis = genesis;
		blocks = new ArrayList<>();
		blocks.add(genesis);}
		System.out.println(genesis);
		
	}
	
	
}
