import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.lang.*;
import java.util.Arrays;

public class SecondaryMap {

	public static String[] arr;
	public static long stind;
	public static void secondarymapindex() throws Exception {
		FileOutputStream out=new FileOutputStream("smap"); 
		PrintStream p = new PrintStream(out); 
		
		try {
			int ct=0;
			long len,l1;
			String temp;
		    	File file = new File("mapfile");
	     	        RandomAccessFile raf = new RandomAccessFile(file, "r");
		   	raf.seek(0);
			len=raf.length();
			String str;
			int flag=0;
			l1=new Long(raf.getFilePointer());
			str=raf.readLine();
			ct++;
		
			while(str!=null)
			{
				if(ct==1)
				{
					arr=str.split(":");
					str=raf.readLine();
					ct++;
				}
				if(ct==2000 || str==null)
				{
					long offset=new Long(raf.getFilePointer()-1);
					offset=offset-l1+1;
					p.println(arr[0]+":"+l1+":"+offset);
					p.flush();
					l1=new Long(raf.getFilePointer());
					if(str!=null)
					{
						str=raf.readLine();
						ct=1;
					}
				
				}
				else{
					long offset=new Long(raf.getFilePointer()-1);
				
					str=raf.readLine();
					ct++;
					if(str==null)
					{
						offset=offset-l1+1;
						p.println(arr[0]+":"+l1+":"+offset);
						p.flush();
						break;
					}
					
				}
			} 
		    	raf.close();
			out.close();
			p.close();
		  
		} catch (Exception e) {
		    System.out.println("IOException:");
		    e.printStackTrace();
		}
	}
 
}
