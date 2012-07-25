package model.chat;

public class TextMessage extends Message {

	public int getContentLengt() {
		if (iContent != null && iContent instanceof String)
		{
			String sText = (String)iContent;
			return sText.length();
		}
		return 0;
	}

}
  