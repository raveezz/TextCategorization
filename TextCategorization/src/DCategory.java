import javax.swing.*;
import java.awt.*;
import java.awt.event.*; 
import java.sql.*;
import java.lang.*;
import java.io.*;
import java.util.*;
import edu.gwu.wordnet.*;
import  edu.gwu.wordnet.util.*;

class DCategory extends javax.swing.JFrame 
{
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1,jLabel2;
    private javax.swing.JTextField jTextField1;
    
    Connection con1;
    Statement st1;
    public DCategory() 
    {
    	
    	try
    	{
	    	Class.forName("org.gjt.mm.mysql.Driver");
			con1 = DriverManager.getConnection("jdbc:mysql://localhost/datamining?user=root&password=12345&schema=datamining");
			st1=con1.createStatement();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
        initComponents1();
        
        setTitle("Text Categorization");
        setSize(400,300);
        setLocation(200, 50);
        show();
    }
    
    private void initComponents1() 
    {
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        getContentPane().setLayout(null);

        addWindowListener(new java.awt.event.WindowAdapter() 
        {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        jButton1.setText("Ok");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(120, 190, 81, 18);
        
        jButton3.setText("Close");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3);
        jButton3.setBounds(220, 190, 80, 18);

        getContentPane().add(jTextField1);
        jTextField1.setBounds(90, 120, 230, 20);

        jLabel1.setText("Document");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(20, 120, 60, 16);
        
        jLabel2.setFont(new java.awt.Font("Monotype Corsiva", 1, 24));
        jLabel2.setText("Text Classification");
        jLabel2.setFont(new Font("Times New Roman",Font.BOLD,20));
		jLabel2.setForeground(Color.MAGENTA);
        getContentPane().add(jLabel2);
        jLabel2.setBounds(110, 50, 190, 28);

        jButton2.setText("...");
        jButton2.setToolTipText("Click here to Open File");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        getContentPane().add(jButton2);
        jButton2.setBounds(330, 120, 30, 18);
        
        

        pack();
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {

        try 
        {
        		
        		st1.executeUpdate("delete from   dm003wordcat ");
        		AssignWeight obj=new AssignWeight(con1,st1);
        		obj.setRange();
        		obj.setLevels();
        	    obj.assignWeightToWords();
        		obj.formWeightageTable();
        	
        		
        		Datamining dm = new Datamining();
        		
         		String Srch= jTextField1.getText();
				Hashtable hash1=new Hashtable();
				Vector v1=new Vector();
				Enumeration names1;
				FileReader fr=new FileReader(Srch);
				BufferedReader br=new BufferedReader(fr);
				String s;
				String ab="";
				while((s=br.readLine()) != null)
				{
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
					
				        hash1.put(first,new String(first));
						
				   }
				}
				names1=hash1.keys();
			
				int j=0,k=0;
				
				try 
				{
					while(names1.hasMoreElements())
					{
						j=0;
						temp=(String) names1.nextElement();
						temp1=(String)hash1.get(temp);
						
						k=ab.indexOf(temp1);
						while(k!=-1)
						{
							k=ab.indexOf(temp1,(k+1));
							j=j+1;
						}
						
						PreparedStatement ps12=con1.prepareStatement("SELECT * FROM stopwordstab where stopwords='"+dm.encode(temp1)+"'");
					    ResultSet ds12=ps12.executeQuery();
					    int recchk=0;
					    if(ds12.next())
					    {
					    	recchk=1;
					    }
						if(j!=0 && recchk==0)
						{
							obj.formWeightageTable(temp1);
							
							st1.executeUpdate("insert into dm003wordcat values('"+dm.encode(temp1)+"',"+j+")");
					    }
					    
					}
					String username = "";
					String pth="";
					MatrixCalculation matrix=new MatrixCalculation();
					matrix.setRewardValue();
					matrix.createResultTable();
				    matrix.calculateOptimum();
				    matrix.trainTestDocument(Srch,dm);
				    String res=matrix.getResult();
					System.out.println("\nprocess completed !");
					System.out.println("\n"+res);
					JOptionPane.showMessageDialog(getContentPane(),res,"Text Categorization",JOptionPane.INFORMATION_MESSAGE);
			
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				 
	
     }

	catch(Exception e)
	{
		e.printStackTrace();
	}

        
  }
    
     private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) 
     {
     	hide();
     }	
     	

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) 
    {
        
        JFileChooser chooser = new JFileChooser();
    
        ExampleFileFilter filter = new ExampleFileFilter();
        filter.addExtension("txt");
        
        filter.setDescription("Text Files");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(getContentPane());
        if(returnVal == JFileChooser.APPROVE_OPTION) 
        {
       
       		jTextField1.setText(chooser.getSelectedFile().getPath());
        }
 
       
    }
    
   
    private void exitForm(java.awt.event.WindowEvent evt) 
    {
        System.exit(0);
    }
    
}


