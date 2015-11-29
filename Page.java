class Page {

	private String title;
	private int id;
        private String text;
	public static String[] infoarr;
	public static String notascii="[^\\p{ASCII}]";
	
	public Page(){

	}

	public Page(String title, int id,String text){  
		this.title = title;
		this.id  = id;
                this.text = text;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		title=title.replaceAll(":"," ");
		this.title = title;
	}


	public String getText() {
		
		return text;
	}

	public void setText(String text) {
		text=text.replaceAll("[']","");
		text=text.replaceAll("[0-9]"," ");
		text=text.replaceAll(notascii,"");
		text=text.toLowerCase();
		text=text.replace("#redirect"," ");
		text=text.replaceAll("!--.*?--"," ");
		text=text.replaceAll("\\s+", " ");
		text=text.replaceAll("\\<[ ]?ref.*?[\\/ --][ ]?\\>"," ");
		text=text.replaceAll("\\<[ ]?ref.*?\\>.*?\\<[ ]?\\/[ ]?ref[ ]?\\>"," ");
		text = text.replaceAll("\\{\\{cit.*?\\}\\}"," ");
		text = text.replaceAll("http[^\\s]+"," ");
		
		this.text = text;
	}
	
}
