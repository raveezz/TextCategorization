import java.sql.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

import java.awt.event.*; 
public class MatrixCalculation
{
	Connection con;
	Statement st;
	ResultSet rs,rq;
	ResultSetMetaData rsmd;
	double matrix[][],m,max,summation=0;
	int rows,columns,i,j,col;
    List list,category,words;
    String domain,result;
	MatrixCalculation()
	{
		 
		 try
		 {
			 Class.forName("org.gjt.mm.mysql.Driver");
		     con = DriverManager.getConnection("jdbc:mysql://localhost/Datamining?user=root&password=12345");
		     st=con.createStatement();
		     list=new ArrayList();
		     category=new ArrayList();
		     words=new ArrayList();
		     
	     }
	     catch(Exception e)
	     {}
	}
	
	public void setRewardValue()
	{
		try
		{
			// getting number of rows
			rs=st.executeQuery("select count(*)  from weightage_table");
			rs.next();
			rows=rs.getInt(1);
			System.out.println("rows "+rows);
			
			
			//getting number of columns
			rs=st.executeQuery("select *   from weightage_table");
			rsmd=rs.getMetaData();
			columns=rsmd.getColumnCount();
			System.out.println("columns "+columns);
			columns=columns-1;
			
			// creating two dimensional array ie)matrix
			matrix=new double[rows][columns];
			for(i=0;i<rows;i++)
			{
				for(j=0;j<columns;j++)
				{
				  matrix[i][j]=0;
				}
			}
			
			//setting values to matrix 
			rs=st.executeQuery("select * from weightage_table");
			i=0;
			col=2;
			
			while(rs.next())
			{
				for(j=0;j<columns;j++)
				{
					matrix[i][j]=rs.getInt(col);
					
					col=col+1;
				}
				
				i=i+1;
				col=2;
			}
			
			System.out.println("\n weightage table \n");
			for(i=0;i<rows;i++)
			{
				for(j=0;j<columns;j++)
				{
				  System.out.print("\t"+matrix[i][j]);
				}
				System.out.println("");
			}
			
			
			//setting reward value
			
			i=rows-1;
			for(int z=1;z<=rows-1;z++)
			{
					
				for(j=columns-1;j>=0;j--)
				{
					list.add(new Double(matrix[i][j]));
						
				}
				max=getMaximum();
					
				i=i-1;
					
				for(j=columns-1;j>=0;j--)
				{
					matrix[i][j]=matrix[i][j]+(0.8*max);
				
				}
					
			}
			
			System.out.println("\n Matrix \n");
			for(i=0;i<rows;i++)
			{
				for(j=0;j<columns;j++)
				{
				  System.out.print("\t"+matrix[i][j]);
				}
				System.out.println("");
			}
			System.out.println("\n");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void createResultTable()
	{
		try
		   {
				rs=st.executeQuery("select * from dm002category");
				
				while(rs.next())
				{ 
					category.add(rs.getString(1));
				}
				try
				{
				 	st.execute("drop table result_table");
				}
				catch(Exception e){}
				 
				Iterator it=category.iterator();
				String fields="";
				while(it.hasNext())
				{
					fields=fields+" "+(String)it.next()+" numeric(15,10),";
						
				}
				fields=fields.substring(0,fields.length()-1);
				
				st.executeUpdate("create table result_table(word varchar(25),"+fields+")");
				
				rs=st.executeQuery("select word from weightage_table");
				while(rs.next())
				{
					words.add(rs.getString(1));
				}
				
				String temp="";
				for(i=0;i<rows;i++)
				{
					for(j=0;j<columns;j++)
					{
						temp=temp+matrix[i][j]+",";
					}
					
					temp=temp.substring(0,temp.length()-1);
					st.executeUpdate("insert into result_table values('"+(String)words.get(i)+"',"+temp+")");
					temp="";
				}
		
				
			}	
			catch(Exception e)
			{
				e.printStackTrace();
			}

	}
	
	public void calculateOptimum()
	{
		Hashtable hash=new Hashtable();
		list.clear();
		
		double sum[][]=new double[rows][columns];
		
		for(i=0;i<rows;i++)
		{
			for(j=0;j<columns;j++)
			{
				sum[i][j]=0;	
			}
		}
		
		System.out.println("\n optimum path \n");
		try
		{
			rs=st.executeQuery("select * from result_table");
			i=0;
			boolean eq=true;
			String temp1,temp2;
			while(rs.next())
			{
			    hash.clear();
			    for(j=2;j<=columns+1;j++)
			    {
			    	m=rs.getDouble(j);
			    	hash.put(m,j);
			    	list.add(m);
			    	
				
			    }
			    
			    for(j=1;j<list.size();j++)
			    {
			    	temp1=""+(Double)list.get(0);
			    	temp2=""+(Double)list.get(j);
			    	
			    	if(temp1.equals(temp2))
			    	{
			    	 eq=false;
			    	}
			    	else
			    	{
			    		eq=true;
			    		break;
			    	}
			    	 
			    }
			    if(eq)
			    {
			    	
			    	max=getMaximum();
				    sum[i][(Integer)hash.get(max)-2]=max;
					System.out.print((Integer)hash.get(max)+"-"+max+"-->");
					i=i+1;
			    }
			    else
			    {
			    	list.clear();
			    	i=i+1;
			    }
			    
			   eq=true;
			}	
			
			
			System.out.println("\n");
			for(i=0;i<rows;i++)
		    {
				for(j=0;j<columns;j++)
				{
					System.out.print("\t\t"+ sum[i][j]);	
				}
				System.out.println("");
		    }
		    
			System.out.println("\n");
			
			list.clear();
			hash.clear();
			for( i=0;i<columns;i++)
			{
				for(j=0;j<rows;j++)
				{
					summation=summation+sum[j][i];
				}	
				System.out.println("Domain Name: "+(String)category.get(i)+"\tSummation Value: "+summation);
				list.add(summation);
				hash.put(summation,(String)category.get(i));
				summation=0;
			}
			
			max=getMaximum();
			domain=(String)hash.get(max);
			result="The given document comes under "+domain+" category";
			
					
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public String getResult()
	{
		return result;
	}
	
	public double getMaximum()
	{
		Collections.sort(list);
		m=(Double)list.get(list.size()-1);
		list.clear();
		return m;
	}
	
	public void trainTestDocument(String file,Datamining obj)
	{
		try
        {
	        String Textdata=domain;
			String Srch = file;
			Datamining dm=obj;
			Statement st1=con.createStatement();
			st1.executeUpdate("DELETE FROM dm002category WHERE dm002category='"+Textdata+"' ");	
			st1.executeUpdate("insert into dm002category values('"+Textdata+"')");
			
			Hashtable hash=new Hashtable();
					
			Vector v=new Vector();
			Enumeration names;
			FileReader fr=new FileReader(Srch);
			BufferedReader br=new BufferedReader(fr);
			String s;
			String ab="";
			while((s=br.readLine()) != null)
			{
				//System.out.println("ll-->"+s);
				ab=ab.concat(s);
			}
			fr.close();
			String temp="";
		    String temp1="";
			String temp2="";
			StringTokenizer stk=new StringTokenizer(ab," ");
			while(stk.hasMoreTokens())
			{
				String first=stk.nextToken();
				int nmchk=dm.numcheck(first);
				if(nmchk==0)
			    {
					hash.put(first,new String(first));
					
				}
					
			}
			names=hash.keys();
			int j=0,k=0;
	        
	        while(names.hasMoreElements())
			{
				j=0;
				temp=(String) names.nextElement();
				temp1=(String)hash.get(temp);
				//System.out.println("temp1-->"+temp1);
			    k=ab.indexOf(temp);
				while(k!=-1)
				{
					k=ab.indexOf(temp1,(k+1));
					j=j+1;
				}
				PreparedStatement ps2=con.prepareStatement("SELECT * FROM stopwordstab where stopwords='"+dm.encode(temp1)+"'");
				ResultSet ds2=ps2.executeQuery();
				int recchk=0;
			    if(ds2.next())
			    {
					 recchk=1;
				}
				//System.out.println("word"+temp1);
			    //System.out.println("occurance"+j);
				if(j!=0 && recchk==0)
				{
					String qry12="SELECT dm001occurancei,dm001word FROM dm001wordoccurance where dm001word='"+dm.encode(temp1)+"' and dm001category='"+Textdata+"'";	
					PreparedStatement ps12=con.prepareStatement(qry12);
					ResultSet ds12=ps12.executeQuery();
					int flag=0;
					int nooccurance=0;
					while (ds12.next())
					{
						String dm001word=ds12.getString(2);
						if(dm001word.equals(temp1))
						    {
						       nooccurance=ds12.getInt(1);
						       j=nooccurance+j;	
						    }	
					}
						
					st1.executeUpdate("DELETE FROM dm001wordoccurance WHERE dm001word='"+dm.encode(temp1)+"' and dm001category='"+Textdata+"'");	
					st.executeUpdate("insert into dm001wordoccurance values('"+Textdata+"','"+dm.encode(temp1)+"',"+j+")");
					        
			    }
	
			}
			PreparedStatement pps2=con.prepareStatement("SELECT * FROM dm001wordoccurance where dm001occuranceI>3");
			ResultSet dds2=pps2.executeQuery();	
			while(dds2.next())
			{
				String cat89=dds2.getString(1);
				String word89=dds2.getString(2);
				int occ89=dds2.getInt(3);
				st.executeUpdate("DELETE FROM dm005thresholdword where dm005category='"+cat89+"' and dm005word='"+word89+"'");
				st.executeUpdate("insert into dm005thresholdword values('"+cat89+"','"+word89+"',"+occ89+")");
				
			}
			
			
	        				           
		}
		catch(Exception e)
        {
            
            System.out.println("ERROR"+e);
        }
        
       
	}
	
}	
