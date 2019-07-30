import java.sql.*;
import java.io.*;
import java.util.*;
public class AssignWeight
{
	Connection con;
	Statement st;
	ResultSet rs,rq;
	List category,maxCount,minCount,ranges,catgs,words,occurances;
	String temp,level,cat,word;
	int range,i,j,max,min,rem,from,to,occurance,weight;
	AssignWeight(Connection c, Statement s)
	{
		try
		{
		
			con=c;
			st=s;
			category=new ArrayList();
			maxCount=new ArrayList();
			minCount=new ArrayList();
			ranges=new ArrayList();
			catgs=new ArrayList();
			words=new ArrayList();
			occurances=new ArrayList();
		
		}
		catch(Exception e1)
		{
			e1.printStackTrace();
		}
		
	}
	
	public void setRange()
	{
		
		try
		{
			rs=st.executeQuery("select * from dm002category");
			
			while(rs.next())
			{
				category.add(rs.getString(1));
			}
			
			for( i=0;i<category.size();i++)
			{
				try
				{
					temp=(String)category.get(i);
					rs=st.executeQuery("Select  max(dm001occuranceI) from dm001wordoccurance   where dm001category='"+temp+"' ");
					rs.next();
					maxCount.add(new Integer(rs.getInt(1)));
					rs=st.executeQuery("select  min(dm001occuranceI) from dm001wordoccurance where dm001category='"+temp+"' ");
					rs.next();
					minCount.add(new Integer(rs.getInt(1)));
				}
				catch(Exception e3)
				{
					e3.printStackTrace();
					
				}
			}
			
			for( j=0;j<category.size();j++)
			{
				System.out.println("\ncategory "+category.get(j)+"\n    Max Limit-"+maxCount.get(j)+"\n    Min Limit-"+minCount.get(j));
				range=((Integer)maxCount.get(j)-(Integer)minCount.get(j))/4;
				ranges.add(range);
				System.out.println("    Range-"+range);
			}
			
			
		}
		catch(Exception e2)
		{
			e2.printStackTrace();
		}
		
	}
	
	public void setLevels()
	{
		
		try
		{
			st.execute("drop table level_and_range");
		}
		catch(Exception e4)
		{}
		try
		{
			st.executeUpdate("create table level_and_range(category varchar(15), level varchar(10), range_to numeric(5), range_from numeric(5), weight numeric(5))");
			for( j=0;j<category.size();j++)
		    {
				cat=(String)category.get(j);
				range=(Integer)ranges.get(j);
				max=(Integer)maxCount.get(j);
				min=(Integer)minCount.get(j);
				System.out.println("\n"+cat+"\n");
				rem=max;
				i=0;
				while(rem>range && rem>min)
				{
					rem=rem-range;
					i=i+1;
				}	
				
				rem=max;	
				while(rem>range && rem>min)
				{
					from=rem;
					to=rem-range+1;
					System.out.println("level "+" from "+from+" to "+to);
					rem=rem-range;
					
					level="level"+(""+i);
					st.executeUpdate("insert into level_and_range values('"+cat+"','"+level+"','"+to+"','"+from+"','"+i+"')");
					i=i-1;
				}
				level="level1";
				st.executeUpdate("update  level_and_range set range_to='"+min+"' where level='"+level+"' and category='"+cat+"'");
				
		    }
	    }
	    catch(Exception e5)
	    {
	    	e5.printStackTrace();
	    }
		
	}
	
	public void assignWeightToWords()
	{
		try
		{
			st.execute("drop table words_with_weight");
		}
		catch(Exception e6)
		{}
		try
		{
			st.executeUpdate("create table words_with_weight(category varchar(25),word varchar(25),weight numeric(5))");
			
			rs=st.executeQuery("select * from dm001wordoccurance");
			while(rs.next())
			{
				catgs.add(rs.getString(1));
				words.add(rs.getString(2));
				occurances.add(rs.getInt(3));
				
		    }		
				System.out.println(catgs.size()+" "+words.size()+" "+occurances.size());
				
				for(i=0;i<catgs.size();i++)
				{
				    
				    cat=(String)catgs.get(i);
				    word=(String)words.get(i);
				    occurance=(Integer)occurances.get(i);
				    
					rq=st.executeQuery("select range_from,range_to,weight from level_and_range where category='"+cat+"'");
					while(rq.next())
					{
							
						from=rq.getInt(1);
						to=rq.getInt(2);
						weight=rq.getInt(3);
						
						if(to<=occurance && occurance<=from)
						{
							
							st.executeUpdate("insert into words_with_weight values('"+cat+"','"+word+"','"+weight+"')");
							
						}
							
					}
					 
			  }		 
	    }
		catch(Exception e7)
		{
			e7.printStackTrace();
		}
	}
	public void formWeightageTable()
	{
		try
		   {
			 st.execute("drop table weightage_table");
	       }
		   catch(Exception e){}
		   
			Iterator it=category.iterator();
			String fields="";
			while(it.hasNext())
			{
				fields=fields+" "+(String)it.next()+" varchar(25),";
					
			}
			fields=fields.substring(0,fields.length()-1);
			try
			{
			  st.executeUpdate("create table weightage_table(word varchar(25),"+fields+")");
	
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
	}
	public void formWeightageTable(String w)
	{
		try
		{
		   		
		    String word=w;
			String query="insert into weightage_table values('"+word+"'";
			String wt="";
			for(i=0;i<category.size();i++)
			{
				rs=st.executeQuery("select weight from words_with_weight where category='"+(String)category.get(i)+"' and word='"+word+"'");
				try
				{
					rs.next();
					wt=wt+","+rs.getInt(1);
					
				}
				catch(Exception e)
				{
					
					wt=wt+","+0;
					
				}
				
			}
			query=query+wt+")";
			
			try
			{   
				st.executeUpdate(query);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				
			}
		
	  }
	  
	  catch(Exception e)
	  {}
	} 

}