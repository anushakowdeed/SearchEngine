import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.lang.*;
import java.util.Arrays;
import java.text.DecimalFormat;

@SuppressWarnings("unchecked")
public class Retrieval {
	public static String query,str,ns;
	public static int N=2930478;
	public static HashMap<Integer,String> map = new HashMap<Integer,String>();
	
	public static void storeLine(String input,char startletter) throws Exception
	{
		
		//DecimalFormat twoDForm = new DecimalFormat("#.###");
		String[] id,docs;
		String[] words = input.split(" ");
		String search_word = words[0];
		id = words[1].split(",");
		int df = id.length;
		for(int i=0;i<id.length;i++)
		{
			docs = id[i].split(":");
			int pageid = Integer.parseInt(docs[0]);
			int tf = Integer.parseInt(docs[1]);
			
			Double tfidf=tf*Math.log10(N/df);
			switch(startletter)
			{
				case 'l':
					tfidf *= 128;break;
				case 'i':
					tfidf *= 64;break;
				case 'c':
					tfidf *= 32;break;
				case 't':
 					tfidf *= 16;break;
				case 'o':
					tfidf *= 8; break;
			}
			//tfidf = Double.valueOf(twoDForm.format(tfidf));
			if(map.containsKey(pageid))
			{
				String str=map.get(pageid);
				String [] arr=str.split(":");
				int freq=Integer.parseInt(arr[0]);
				double sumtfidf = Double.valueOf(arr[1].trim()).doubleValue();
				freq++;
				sumtfidf+=tfidf;
				map.put(pageid,freq+":"+sumtfidf);
					
			}
			else
			{						
				map.put(pageid,"1:"+tfidf);
								
			}			
		}
		
		 
			
		
	}
	public static void main(String args[]) throws Exception
	{
		String[] qstring;
		Stemmer stem=new Stemmer();
                File file = new File("./Input.txt");
	     	RandomAccessFile inpt = new RandomAccessFile(file, "r");
                BufferedReader input = new BufferedReader(new FileReader(inpt.getFD()));
                FileOutputStream out=new FileOutputStream("output.txt"); 
		PrintStream p = new PrintStream(out); 
		inpt.seek(0);
		RandomAccessFile pri = new RandomAccessFile("./index/pIndex", "r");
		RandomAccessFile sec = new RandomAccessFile("./index/sIndex", "r");
                BufferedReader primary = new BufferedReader(new FileReader(pri.getFD()));
                BufferedReader secondary = new BufferedReader(new FileReader(sec.getFD()));
                
		//System.out.println("enter query");
		//BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		//query=br.readLine().trim();
               query=input.readLine();
	       query=query.toLowerCase();
             while(query!=null)
            {
		long tm1=new Date().getTime();
		qstring=query.split("\\s+");
                String prev="";
                int index = query.indexOf(':');
                if(index!=-1)
               {
		  for(int i=0;i<qstring.length;i++)
		 {

			String[] temp=qstring[i].split(":");
                    
                   if(temp.length==1)
                     {
                        String st=stem.stripAffixes(temp[0].toString());
                        ns=prev+st;
                         qstring[i]=ns;
                          
                      }  
                     else if(temp.length>1)
                    {
			String st=stem.stripAffixes(temp[1].toString()); 
			if(st!=""&&st.length()>1)
			{
				if((temp[0].compareTo("C"))==0||(temp[0].compareTo("c"))==0)
                                {
					ns="c_"+st;
                                        prev="c_";
                                }
				else if((temp[0].compareTo("i"))==0||(temp[0].compareTo("I"))==0)
                                 {
                                        prev="i_";
					ns="i_"+st;
                                 }
				else if((temp[0].compareTo("t"))==0||(temp[0].compareTo("T"))==0)
                                 {
                                        prev="l_";
					ns="l_"+st;
                                 }
				else if((temp[0].compareTo("o"))==0||(temp[0].compareTo("O"))==0)
                                  {
                                        prev="o_";
					ns="o_"+st;
                                   }
				else if((temp[0].compareTo("b"))==0||(temp[0].compareTo("B"))==0)
                                {
					ns="t_"+st;
                                        prev="t_";
                                   }
				qstring[i]=ns;
			  }
                    }
			
		  }
              }
               
            for(int i=0;i<qstring.length;i++)
		{
			int slen=(int)sec.length();
			byte[] bytearr = new byte[slen];
			sec.seek(0);
			sec.read(bytearr);
			String s=new String(bytearr);
			String []secarray=s.split("\n");
			int offset=Arrays.binarySearch(secarray,qstring[i]);
			offset=offset*(-1)-1;
			String [] temp=secarray[offset].split(":");
			if(temp[0].compareTo(qstring[i])==0)
			{
				long l = Long.parseLong(temp[1].trim());
				pri.seek(l);
				String in=primary.readLine();
				storeLine(in,in.charAt(0));
			}
			else
			{
				String [] temp1=secarray[offset-1].split(":");
				long l1 = Long.parseLong(temp1[1].trim());
				long lenarr = Long.parseLong(temp1[2].trim());
				byte[] byt = new byte[(int)lenarr];
				pri.seek(l1);
				pri.read(byt);
				String s1=new String(byt);
				String [] priarray=s1.split("\n");
				int offst=Arrays.binarySearch(priarray,qstring[i]);
				offst=offst*(-1)-1;
				String [] temp2=priarray[offst].split(" ");
				if(temp2[0].compareTo(qstring[i])==0)
				{
					storeLine(priarray[offst],priarray[offst].charAt(0));
				}
				
			}

		}
		
		List keys = new ArrayList();
	       	keys.addAll(map.keySet());
		Collections.sort(keys, new Comparator() {
		    public int compare(Object a, Object b) {
			String s11=(String)map.get(a);
			String s21=(String)map.get(b);
			String st1 = s11.substring(0,s11.indexOf(':'));
			String st2 = s21.substring(0,s21.indexOf(':'));
			int temp1 = Integer.parseInt(st1);
			int temp2 = Integer.parseInt(st2);
			
	    		if(temp1<temp2)
				return 1;
			else if(temp1==temp2)
			{
				String[] st3 = s11.split(":");
				String[] st4 = s21.split(":");
				double d1 = Double.valueOf(st3[1].trim()).doubleValue();
			        double d2 = Double.valueOf(st4[1].trim()).doubleValue();
				if(d1>=d2)
					return -1;
				else
					return 1;
			}
			else 	
				return -1;
		        
		    }
		});

		int listsize = keys.size();
                String []text;
		RandomAccessFile pmap = new RandomAccessFile("mapfile", "r");
	        BufferedReader pmapbr = new BufferedReader(new FileReader(pmap.getFD()));
                //p.println(query);
                int plen=(int)pmap.length();
		byte[] bytemparr = new byte[plen];
		pmap.seek(0);
		pmap.read(bytemparr);
		String s=new String(bytemparr);
               
		String []pmaparray=s.split("\n");
        		int i1=0;int count=1;int flag=0;
		System.out.println();
		while(i1<listsize && count<=10) {

			
			int id=Integer.parseInt(keys.get(i1).toString().trim());
			int low=0;
			int high = pmaparray.length-1;
			int mid = (low+high) /2;

			while(high-low!=1)
			{
				String substr=pmaparray[mid].substring(0,pmaparray[mid].indexOf(':'));
				int prid = Integer.parseInt(substr);
				
				if( id < prid)
				{
					high = mid;
					mid = (low+high)/2;
				}
				else if(prid==id)
				{
					low  = mid;
					flag=1;
					break;
				}
				else
				{
					low = mid;
					mid = (low+high)/2;
				}
			}
			/*if(flag==0)
			{
				text = secmaparray[low].split(":");
				seek_offset = Long.parseLong(text[1].trim());
				total = Long.parseLong(text[2].trim());
				pmap.seek(seek_offset);
				byte[] byt1 = new byte[(int)total];
				pmap.read(byt1);
				s1=new String(byt1);
				String[] str1 = s1.split("\n");
				Arrays.sort(str1);
				int sofset;
				sofset=Arrays.binarySearch(str1,Integer.toString(id).trim());
				int soffset=(sofset*-1) -1;
				String[] retrieve;
				if(soffset<str1.length)
				{
					retrieve = str1[soffset].split(":");
					//System.out.println(id+"\t\t"+retrieve[1]);
					p.println(id+"\t\t"+retrieve[1]);
						p.flush();
				}	
					count++;
			}
			else
			{maparray
				text = secmaparray[low].split(":");
				seek_offset = Long.parseLong(text[1].trim());
				s1=pmapbr.readLine();
				String[] str1 = s1.split(":");
				//System.out.println(id+"\t\t"+str1[1]);
                               p.println(id+"\t\t"+str1[1]);
						p.flush();
				count++;
			}*/
                      if(flag==1)
                    {
                      text=pmaparray[low].split(":");
                       p.println(text[0]+"\t\t"+text[1]);
                        p.flush();
                         count++;
                     }
			i1++;
				
		}

		p.println("\n"+listsize+"  results found\n");
                                p.flush();		
		if(count==1)
                {
			p.println("\nno results found\n");
                        p.flush();
                 }
		long tm2=new Date().getTime();
		p.println("Time Limit : "+(tm2-tm1));
                p.flush();		
     		query=input.readLine();
              // pmap.seek(0);
              //pri.seek(0);
              //sec.seek(0);
              // smap.seek(0);
                map.clear();
               
           }
           
           input.close();
           p.close();
           pri.close();
           sec.close();
           primary.close();
                secondary.close();
	} 
}

