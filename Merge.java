import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.lang.*;
import java.util.Arrays;

public class Merge{

	public Merge(){
	}
	public static void merge(String file1, String file2,String outfile)throws Exception
		{
			int c;
			String s1,s2;
			String[] p1,p2;
			String fout="./index/Indexoutput";
			FileOutputStream out=new FileOutputStream(fout); 
			PrintStream p = new PrintStream(out); 
			FileInputStream fr1 = new FileInputStream(file1); 
			FileInputStream fr2 = new FileInputStream(file2);			
    			DataInputStream in1 = new DataInputStream(fr1);
			DataInputStream in2 = new DataInputStream(fr2);
        		BufferedReader br1 = new BufferedReader(new InputStreamReader(in1));
			BufferedReader br2 = new BufferedReader(new InputStreamReader(in2));
   			//Read File Line By Line
    
			s1 = br1.readLine();
			s2 = br2.readLine();

			while(true)
			{	
			   if(s1!=null && s2!=null)
			   {
				p1 = s1.split("\\s+");
				p2 = s2.split("\\s+");
				c=p1[0].compareTo(p2[0]);
				if(c==0){
					s1=s1+","+p2[1];
					p.println(s1);
					s1 = br1.readLine();
					s2 = br2.readLine();
					
				}else if(c>0){
					p.println(s2);
					s2=br2.readLine();
				}else{
					p.println(s1);
					s1=br1.readLine();
				}
				p.flush();
			    }
			    else if(s1==null)
				{			 
				 while(s2!=null)
				 {
					p.println(s2);
					p.flush();
					s2 = br2.readLine();
				 }
				 break;
				}
			    else{				 
				 while(s1!=null)
				 {
					p.println(s1);
					p.flush();
					s1 = br1.readLine();
				 }
				 break;
				}
			}
			File f1=new File(file1);
			File f2=new File(file2);
			f1.delete();
			f2.delete();
			File newfi=new File(fout);
			File newf2=new File(outfile);
			newfi.renameTo(newf2);
			in1.close();
			in2.close();
			fr1.close();
			fr2.close();
			br1.close();
			br2.close();
			p.close();
			out.close();			

		}
	public static void mergeFiles() throws Exception
	{
		String inpath,outpath,line,indir,outdir,stopwds;
		outdir="./index";
		File newdir=new File(outdir);
				
		//calling for merging of files
		
		
		int i;		
		String fn="./index/i_";
		String[] indexf;
		indexf=newdir.list();
		int filecount,ct=0,flag2=0,tcount;
		String fn1,fn2;
		filecount=indexf.length;


		if(indexf.length>1)
		{
			System.out.println("Merging of files started\n");
			
			while(filecount>1)
			{
				ct=0;
				flag2=0;
				if(filecount%2==0){
					tcount=filecount;
					flag2=0;
				}
				else{
					flag2=1;
					tcount=filecount-1;
				}
				
				for(i=0;i<tcount;)
				{
					fn1=fn+i;
					i++;
					fn2=fn+i;
					i++;
					merge(fn1,fn2,fn+ct);
					ct++;
				}
				if(flag2==1)
				{
					File oldfile=new File("./index/i_"+i);
					File newfile=new File("./index/i_"+ct);
					oldfile.renameTo(newfile);
				}
				
				indexf=newdir.list();
				filecount=indexf.length;
			}
			File oldfile=new File("./index/i_0");
			File newfile=new File("./index/pIndex");
			oldfile.renameTo(newfile);
			System.out.println("merging of files ended");
		}
		
	}

}
