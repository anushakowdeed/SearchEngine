import java.util.Map;           
import java.util.TreeMap;       
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

public class word_offset {

	public static void main(String argv[]) 
	{
		try
		{
			String arr[];
			long bytes=0;
			String name="./index/wIndex";
			String line;
			BufferedReader buf = new BufferedReader(new FileReader("./index/pIndex"));
			BufferedWriter output = new BufferedWriter(new FileWriter(name));
			int count=0;
			line=buf.readLine();
			
			while(true)
			{	
				
                                if(line==null)
					break;
				String lines=new String(line);
				byte[] byt = line.getBytes();
				arr=line.split("\\s+");
					
				
					StringBuffer st=new StringBuffer();
					st.append(arr[0]);
					st.append(":");	
					st.append(bytes);	
					output.write(st.toString());
					output.write("\n");		
				
				
				bytes+=byt.length+1;
				 
				if(count==1000000)
                                    break;
				count++;
				line=buf.readLine();
				
			}
			
			buf.close();
                        output.close();
		}catch(IOException E){}
	}
}
