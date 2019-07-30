import edu.gwu.wordnet.*;

import  edu.gwu.wordnet.util.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import java.lang.*;


public class anisample
{
static DictionaryDatabase dictionary;
String[] before = {"&","<",">","\"","\'"};
String[] after = {"&amp;","&lt;","&gt;","&quot;","&apos;"};
public anisample()
{
	try{
        Class.forName("org.gjt.mm.mysql.Driver");
       	Connection con = DriverManager.getConnection("jdbc:mysql://localhost/Datamining?user=root&password=admin");
		Statement st=con.createStatement();
		st.executeUpdate("DELETE FROM dm004wordnetoccurance");
		st.executeUpdate("DELETE FROM temp_wordnet");
		PreparedStatement ps2=con.prepareStatement("SELECT * FROM dm001wordoccurance");
		ResultSet ds2=ps2.executeQuery();
		int kinh=0;
		Enumeration names;
		Hashtable ht=new Hashtable();
		while(ds2.next())
		{
			String word23=decode(ds2.getString(2));
			String category23=ds2.getString(1);
		    System.out.println("word23------------>"+word23);
		    
			
	    IndexWord word=null;
        dictionary = new FileBackedDictionary("G:/Projects Directory/annauniv/wordnetdata");
        //for (int i = 0; i < POS.CATS.length; i++)
        for (int i = 0; i < 1; i++)
            {
             try
             { 	
            word = dictionary.lookupIndexWord(POS.CATS[i],word23);
             }
              catch(Exception e)
              {
              }
            if (word != null)
               {
               Synset[] senses;
               try
                 {
	             senses = word.getSenses();
                 }
                 catch(Exception e)
                   {
                    return;
                   }
            	       
            for (int k = 0; k < senses.length; k++)
                {
                //System.out.println("senses.length--->"+senses.length);	
                Synset sense = senses[k];
                if (sense == null)
                   {
                   continue;
                   }
            //Word[] printWords = sense.getWords();
            String wordnetString=sense.getGloss();
            
            System.out.println("Gloss--->"+wordnetString);
            
            
            StringTokenizer stk=new StringTokenizer(wordnetString," ");
			while(stk.hasMoreTokens())
				{
				String wordnetString1=stk.nextToken();
                //System.out.println("word--->"+wordnetString1);
				if(!wordnetString1.equals(" ") && !wordnetString1.equals(""))
				   {
                   ht.put(encode(wordnetString1),encode(wordnetString1));
                   
				   st.executeUpdate("insert into temp_wordnet values('"+encode(wordnetString1)+"','"+category23+"')");
				   }
				}
            //System.out.println("description--->"+sense.getDescription());
            //System.out.println("LOng description--->"+sense.getLongDescription());
               }
              }
              
            }
            
      }
      names=ht.keys();
				//names=v.elements();
				
        
        while(names.hasMoreElements())
			{
					
					String temp=(String) names.nextElement();
					String temp1=(String)ht.get(temp);
					
					String qry22="SELECT DISTINCT TEMP_CATEGORY FROM TEMP_WORDNET WHERE TEMP_WORDNET='"+temp1+"'";
					System.out.println("--------->"+qry22);
					PreparedStatement ps22=con.prepareStatement(qry22);
		            ResultSet ds22=ps22.executeQuery();
		            
		            
		            while(ds22.next())
		             {
		             
			         String category232=ds22.getString(1);
			         String qry222="SELECT count(*) as tot FROM TEMP_WORDNET WHERE TEMP_WORDNET='"+temp1+"' and temp_category='"+category232+"'";
			         System.out.println("qry222--------->"+qry222);
			         PreparedStatement ps222=con.prepareStatement(qry222);
		             ResultSet ds222=ps222.executeQuery();
		             System.out.println("ppppppppppppppppp");
			         int occurance=0;
			         if (ds222.next())
			            {
			            occurance=ds222.getInt(1);
			           }
			         if(occurance>3)
			           {
			           st.executeUpdate("insert into dm004wordnetoccurance values('"+category232+"','"+temp1+"',"+occurance+")");
			           }
			         } 
			}       
      }            
     catch(Exception e)
{
	System.out.println(e.toString());
}
       
}
public String encode(String sData)
      {
      	     
      		if(sData!=null)	{
      			for( int i=0; i<before.length; i++)	{
      				sData = Replace(sData, before[i], after[i]);
      				 
      			}
      		}
      		else	{
      			sData = "";
      		}
      		return sData;
      }

public String decode(String sData)
      {
      		if(sData!=null)	{
      			for( int i=0; i<before.length; i++)	{
      				sData = Replace(sData, after[i], before[i]); 
      				
      			}
      		}
      		else	{
      			sData = "";
      		}
      		return sData;
      }

      
public String Replace( String content, String oldWord, String newWord)
      {
      		int position = content.indexOf(oldWord);
      		while( position > -1)	{
      			content = content.substring(0,position) + newWord +
      				content.substring(position+ oldWord.length());	
      			position = content.indexOf(oldWord, position+newWord.length());
      		}      	
      		return content;
      }
      
public static void main(String args[])
{


      
anisample as=new anisample();
System.out.println("Kamal");
}
}