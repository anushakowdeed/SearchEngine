import java.util.Map;           // Superclass of HashMap
import java.util.TreeMap;       // A container that supports quick retrieval
import java.util.Iterator;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.io.*;
import java.lang.*;
import java.util.*;
import  java.util.StringTokenizer;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;


public class striping {

public static void main(String argv[]) 
{
try{
	 BufferedReader buf1 = new BufferedReader(new FileReader("./index/pIndex"));
          FileOutputStream out=new FileOutputStream("finalindex"); 
		PrintStream p = new PrintStream(out); 
	 String nline1,nline2,term1,term2,term3,term,t1,t2;
	StringBuffer sbuff = new StringBuffer();
	StringBuffer sbuff1;
	long df,postings=0,line=0;
        String []id;int flag=0;
	 while(true)
       	 {
		nline1=buf1.readLine();
                // System.out.println(nline1);
		if(nline1==null) break;
               line++;
                String[] words = nline1.split(" ");
		String search_word = words[0];
                char ch = search_word.charAt(0);
                                            //System.out.println("c"+ch);
		id = words[1].split(",");
		 df = id.length;
                 flag=0;
		   sbuff1 = new StringBuffer();                            //System.out.println(df);
		if(df>30000&&((ch=='t')||ch=='o'||ch=='c'))
		{	
			int count=0;postings=0;
                      	System.out.println("before df"+df+";line"+line);	                               // System.out.println("here\n");
                         while(count<df)
                       {
                           String temp1=id[count];
                            String temp[]=temp1.split(":");
                            int t=Integer.parseInt(temp[1]);
                           // System.out.println("t:"+t);
                             if(t!=1)
                             {
                                 if(flag==0)
                                {
                                                      		                               // System.out.println("here\n");
                                  sbuff1.append(search_word);		
		 		  sbuff1.append(" ");
				  sbuff1.append(temp1);
                                   flag=1;
                                  }
                                 else
                                   {
                                       sbuff1.append(",");
				  sbuff1.append(temp1);        
                                  postings++;    
 
 
                                  }   

                              }
                              count++;
                       }
                                       //System.out.println(sbuff1);
                                   System.out.println("postings size after:"+postings+";line"+line);
                        p.println(sbuff1.toString());
				p.flush();  
              }
              else
              {		
                                
                                 p.println(nline1);
				p.flush(); 
                  }
                                   
				

	 }

	}
		catch(IOException E){}
}
}	
