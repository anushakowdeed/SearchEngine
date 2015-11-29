import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.lang.*;
import java.util.Arrays;

@SuppressWarnings("unchecked")
public class Parser extends DefaultHandler{

	List list;
        int flag,id;
        StringBuffer strbuf= new StringBuffer();
	Page tempPage;
	String title,text,infobox,categ,info1,cate,temp,title1;
	String [] titlearr,infoarr,textarr,catearr,outlinkarr;
	public static String [] fnames;
	public static TreeMap tr,t;
	public static TreeMap stopwrds=new TreeMap();
	public static TreeMap tree = new TreeMap();
	public static Stemmer stem;
	public static String notascii="[^\\p{ASCII}]";
	public static Pattern info=Pattern.compile("\\{\\{.?infobox.?(.*?)[|<}]");
	public static Pattern category=Pattern.compile("\\[\\[category.(.*?)\\]");
	public static Pattern outlink = Pattern.compile("\\[\\[.*?\\]\\]");
	public static String delimitter="[/:\\.[-]`!+=%<>}{,$^()@#&|?\\\\\\*\";\\[\\]_']";
	public static int count=0;

	public Parser()
	{
		tr=new TreeMap();
		list = new ArrayList();
	}

	

	private void parseDocument(String filepath) {

		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {

			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();

			//parse the file and also register this class for call backs
			sp.parse(filepath, this);

		}catch(SAXException se) {
			se.printStackTrace();
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}
	}

        public void store(String str,String prefix,int p)
	{
	    if(str.length()!=0)
	    {					
	      if(!stopwrds.containsKey(str))
	      {
		String st=stem.stripAffixes(str.toString());
		if(st!=""&&st.length()>1)
		{
			t=new TreeMap();
			st=prefix+"_"+st;
			if(tr.containsKey(st))
			{
				t=(TreeMap)tr.get(st);
				if(!t.containsKey(p))
				{
					t.put(p,1);
					tr.put(st,t);
				}
				else
				{
					int p1=(Integer)t.get(p);
					t.put(p,p1+1);
					tr.put(st,t);
				}	
			}
			else
			{						
				t.put(p,1);
				tr.put(st,t);					
			}
		 }
	     }
	  }
	}
	
	


	//Event Handlers
	public void startElement(String uri, String localtitle, String qtitle, Attributes attributes) throws SAXException 
	{
	
                strbuf.setLength(0);
                if(qtitle.equalsIgnoreCase("page")) 
		{
			//create a new instance of Page
                        flag=1;
			count++;
			tempPage = new Page();
			
		}
	}


	public void characters(char[] ch, int start, int length) throws SAXException 
	{
                strbuf.append(ch,start,length);
	}

	public void endElement(String uri, String localtitle, String qtitle) throws SAXException 
	{

		if(qtitle.equalsIgnoreCase("page")) 
		{
			//add it to the list
			list.add(tempPage);
		}
		else if (qtitle.equalsIgnoreCase("title")) 
		{
	
                    tempPage.setTitle(strbuf.toString().trim());
		}
		else if (qtitle.equalsIgnoreCase("Id")) 
		{
                        if(flag==1)
			{
				tempPage.setId(Integer.parseInt(strbuf.toString().trim()));
	                        flag=0;
                        }
		}
		
		else if (qtitle.equalsIgnoreCase("text")) 
		{
			id=tempPage.getId();
			try
			{
 			   FileWriter fw = new FileWriter("./mapfile",true);
			   temp=tempPage.getTitle();	
 			   fw.write(id+":"+temp);   //appends the string to the file
			   fw.write("\n");
 			   fw.close();
			}	

			catch(IOException ioe)
			{
 			   System.err.println("IOException: " + ioe.getMessage());
			}
			title1=tempPage.getTitle();
			title1 = title1.replaceAll("[']","");
			title1 = title1.replaceAll(notascii,"");
			title1 = title1.replaceAll("[0-9]"," ");
			title1 = title1.replaceAll(delimitter," ");
			title1 = title1.toLowerCase();
			titlearr=title1.split("\\s+");
			for(int i=0;i<titlearr.length;i++)
			{
				this.store(titlearr[i],"l",id);
			}
			tempPage.setText(strbuf.toString().trim());
			text=tempPage.getText();
			Matcher m1 = info.matcher(text);
			while(m1.find())
			{
				info1=m1.group(1).replaceAll(delimitter," ");
				infoarr = info1.split("[' '_]");
				for(int i=0;i<infoarr.length;i++)
					this.store(infoarr[i],"i",id);
				
			}
			text = text.replaceAll("\\{\\{.?infobox.?(.*?)[|<}]"," ");
			Matcher doublebrac = outlink.matcher(text);
			int flag1=0;
			while(doublebrac.find())
			{
				flag1=0;
				
				Matcher m2= category.matcher(doublebrac.group());
				while (m2.find())
                 		{
					flag1=1;
					catearr = m2.group(1).split("[' ':|,.!<>-_+=\";?]");
					for(int i=0;i<catearr.length;i++)
						this.store(catearr[i],"c",id);
				}
				if(flag1==0)
				{
					outlinkarr=doublebrac.group().split("[' ':|,.!<>-_+=\";?]");
					for(int i=0;i<outlinkarr.length;i++)
						this.store(outlinkarr[i],"o",id);
						
				}
			}		
			
			text=text.replaceAll("\\[\\[.*?\\]\\]"," ");
			text=text.replaceAll(notascii,"");	
			text=text.replaceAll(delimitter," ");
			text=text.replaceAll("\\s+", " ");
			textarr=text.split("\\s+");
			for(int i=0;i<textarr.length;i++)
			{
				this.store(textarr[i],"t",id);
			}
      			
                }
		
	}

	
	
	public static void main(String[] args) throws Exception
	{
		FileReader fr;
		BufferedReader br;
		Parser spe;
		ArrayList arr1,arr2;
		String inpath,outpath,line,indir,outdir,stopwds;
		stem=new Stemmer();
		indir="./wikifiles";
		outdir="index";
		stopwds="stopwords.txt";
		File wiki=new File(indir);
		File newdir=new File(outdir);
		newdir.mkdir();
		fnames=wiki.list();		

		// code for storing stopwords in a treemap

		fr=new FileReader(stopwds);
		br = new BufferedReader(fr);
		
		while((line=br.readLine())!=null) 
		{
         		stopwrds.put(line,1);	
                }
		fr.close();
		br.close();

		// processing file by file for indexing
		Arrays.sort(fnames);
		for(int i=0;i<fnames.length;i++)
		{
			spe=new Parser();
			inpath=indir+"/"+fnames[i];
			outpath=outdir+"/i_"+i;
			System.out.println("Indexing of File "+i+" Started"+fnames[i]);
			spe.parseDocument(inpath);
			
			try{
				arr1=new ArrayList(tr.keySet());
				FileWriter fstream = new FileWriter(outpath);
				BufferedWriter out = new BufferedWriter(fstream);
				for(int j=0;j<arr1.size();j++)
				{
					TreeMap t=new TreeMap();
					t=(TreeMap)tr.get(arr1.get(j));
					out.write(arr1.get(j)+" ");
					arr2=new ArrayList(t.keySet());
					for(int k=0;k<arr2.size();k++)
					{
						if(k==0)
							out.write(arr2.get(k)+":"+t.get(arr2.get(k)));
						else
							out.write(","+arr2.get(k)+":"+t.get(arr2.get(k)));						
					}
					out.write("\n");
				}
				
    				out.close();
				fstream.close();
			}
			catch (Exception e)
			{
      					System.err.println("Error: " + e.getMessage());
			}
		
			//System.out.println("Indexing of File "+i+" Ended");
		}
		
		String[] indexf;
		indexf=newdir.list();
		String o1="./index/i_0";
		String n1="./index/pIndex";
		File olindex,newindex;
		if(indexf.length>1)
		{
			Merge m1=new Merge();
			m1.mergeFiles();
		}
		else
		{
			olindex=new File(o1);
			newindex=new File(n1);
			olindex.renameTo(newindex);
		}
		
		System.out.println("secondary index creation started");	
		SecondaryIndex si=new SecondaryIndex();
		si.secondaryindexcreation();
		
		SecondaryMap sm=new SecondaryMap();
		sm.secondarymapindex();
		System.out.println("ended successfully");
	}

}
