package model.chat;

public abstract class Message {
	private long iTimesTamp = -1;
	Object iContent = null;
	
	//set method set
	public void setTimesTamp(long aTimesTamp) {
		iTimesTamp = aTimesTamp;
	}
	
	public void setContent(Object aContent) {
		iContent = aContent;
	}
	//get method set
	public long getTimesTamp() {
		return iTimesTamp;
	}
	
	public abstract int getContentLengt();
}
