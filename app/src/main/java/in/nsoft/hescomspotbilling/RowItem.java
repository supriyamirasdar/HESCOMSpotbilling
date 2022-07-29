package in.nsoft.hescomspotbilling;

public class RowItem {
	private int id=0;
	private String value="";
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public RowItem(int id, String value) {
		super();
		this.id = id;
		this.value = value;
	}
	
	
	public RowItem() {
		super();		
	}
	
	
}