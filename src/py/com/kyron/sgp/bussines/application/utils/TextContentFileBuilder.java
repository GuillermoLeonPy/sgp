package py.com.kyron.sgp.bussines.application.utils;

public class TextContentFileBuilder {
	
	private StringBuffer textFileContent;
	
	public TextContentFileBuilder() {
		// TODO Auto-generated constructor stub
		this.textFileContent = new StringBuffer("");
	}

	public void insertValue(String cellValue){
		this.textFileContent.append(cellValue);                     
	}
	
	public void insertTabCharacter(){
		this.textFileContent.append("\t");
	}
	
	public void insertNewLineCharacter(){
		this.textFileContent.append("\n");
	}
	
	public byte[] getByteArrayData(){
		return this.textFileContent.toString().getBytes();
	}

	/**
	 * @return the textFileContent
	 */
	public String getTextFileContent() {
		return textFileContent.toString();
	}

}
