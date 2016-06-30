import java.io.Serializable;

public class sentFile implements Serializable {
	public byte[] text;
	
	public sentFile() {
		text = null;
	}
	
	public byte[] getFile() {
		return text;
	}
	
	public void printFile() {
		String printedText = new String(text, 0, text.length);
		System.out.println(printedText);
	}
	
	public void setFile(byte [] textData) {
		text = textData;
	}

}
