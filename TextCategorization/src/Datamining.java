import javax.swing.*;
import java.awt.*;
import java.awt.event.*; 
import java.sql.*;
import java.lang.*;
import java.io.*;
import java.util.*;
import edu.gwu.wordnet.*;
import  edu.gwu.wordnet.util.*;

public class Datamining extends javax.swing.JFrame 
{
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2,jButton3;
    private javax.swing.JLabel jLabel1,jLabel2,jLabel3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
   
    static DictionaryDatabase dictionary;
    public Datamining() 
	{    	
    	initComponents();
    	setTitle("Text Categorization");
        setSize(400,300);
        setLocation(200, 50);               
    }
    
    private void initComponents() 
    {
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

        getContentPane().setLayout(null);

        setBackground(new java.awt.Color(204, 204, 255));
        setForeground(new java.awt.Color(204, 204, 255));
        addWindowListener(new java.awt.event.WindowAdapter() 
        {
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                exitForm(evt);
            }
        });

        
        jButton1.setText("Ok");
        jButton1.addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                jButton1ActionPerformed(evt);
            }
        });

        getContentPane().add(jButton1);
        jButton1.setBounds(100, 190, 81, 18);

        getContentPane().add(jTextField1);
        jTextField1.setBounds(100, 90, 200, 20);

        getContentPane().add(jTextField2);
        jTextField2.setBounds(100, 130, 200, 20);

        
        jButton2.setText("Close");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        getContentPane().add(jButton2);
        jButton2.setBounds(210, 190, 90, 18);

        jLabel1.setFont(new java.awt.Font("Monotype Corsiva", 1, 24));
        jLabel1.setText("Training Data");
        jLabel1.setFont(new Font("Times New Roman",Font.BOLD,20));
		jLabel1.setForeground(Color.MAGENTA);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(130, 40, 170, 20);

        jLabel2.setText("Category");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(30, 90, 60, 16);

        jLabel3.setText("File Name");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(30, 130, 60, 16);

        jButton3.setText("...");
        jButton3.setToolTipText("Click here to open the file");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3);
        jButton3.setBounds(310, 130, 30, 18);

        pack();
    
    }
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) 
    {
    
        JFileChooser chooser = new JFileChooser();
        ExampleFileFilter filter = new ExampleFileFilter();
        filter.addExtension("txt");
        //filter.addExtension("doc");
        filter.setDescription("Text Files");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(getContentPane());
        if(returnVal == JFileChooser.APPROVE_OPTION) 
        {
	        System.out.println("You chose to open this file: " +
	        chooser.getSelectedFile().getName());
	        jTextField2.setText(chooser.getSelectedFile().getPath());
        
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) 
    {
    	hide();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) 
    {
        try
        {
	        Datamining dm = new Datamining();
	       	Class.forName("org.gjt.mm.mysql.Driver");
	       	Connection con = DriverManager.getConnection("jdbc:mysql://localhost/Datamining?user=root&password=12345");
			Statement st=con.createStatement();
			String Textdata = jTextField1.getText();
			String Srch = jTextField2.getText();
			JOptionPane.showMessageDialog(getContentPane(),Srch);
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
			jTextField1.setText("");
			jTextField2.setText("");
			
	        				           
		}
		catch(Exception e)
        {
            JOptionPane.showMessageDialog(getContentPane(),"Error"+e);
            System.out.println("ERROR"+e);
        }
    }    
   
    private void exitForm(java.awt.event.WindowEvent evt) {
        System.exit(0);
    }
    
    
    String[] before = {"&","<",">","\"","\'"};
	String[] after = {"&amp;","&lt;","&gt;","&quot;","&apos;"};

      String encode(String sData)
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

      String decode(String sData)
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

      
      String Replace( String content, String oldWord, String newWord)
      {
      		int position = content.indexOf(oldWord);
      		while( position > -1)	{
      			content = content.substring(0,position) + newWord +
      				content.substring(position+ oldWord.length());	
      			position = content.indexOf(oldWord, position+newWord.length());
      		}      	
      		return content;
      }
      
      
      int numcheck( String stcontent)
      {
      	char a[]=stcontent.toCharArray();
		int j=0;
		for(int i=0;i<a.length;i++)
		{
			if(Character.isDigit(a[i]))
			  {
			  	j=j+1;
			  }
		}
		if(j==a.length)
		{
			return(1);
		}
		else
		{
			return(0);
		}
      } 
    
}
 
