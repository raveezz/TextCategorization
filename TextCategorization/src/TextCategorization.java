import javax.swing.*;
import java.awt.*;
import java.awt.event.*; 
import java.sql.*;
import java.lang.*;
import java.io.*;
import java.util.*;

public class TextCategorization extends javax.swing.JFrame 
{
	//Labels declaration
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JLabel jLabel4;
	private JLabel jLabel5;
	
	//Buttons declaration
	private JButton jButton1;
	private JButton jButton2;
	private JButton jButton3;
	private JButton jButton4;
	
	private JPanel contentPane;
	
	private JPanel jPanel1;
	
	
	DCategory dcat;
	MatrixCalculation obj;
	
	TextCategorization()
	{
		initializeComponent();
		this.setVisible(true);
	}
	
	private void initializeComponent()
	{
     	//Label initailization
     	jLabel1 = new JLabel();
		jLabel2 = new JLabel();
		jLabel3 = new JLabel();
		jLabel4 = new JLabel();
		jLabel5 = new JLabel();
		
		//Buttons initialization
		jButton1=new JButton("Go");
		jButton2=new JButton("Go");
		jButton3=new JButton("Go");
		jButton4=new JButton("Exit");
		
		jPanel1 = new JPanel();
		
		contentPane = (JPanel)this.getContentPane();
		contentPane.setLayout(null);
		
		//Assigning text to Labels
		jLabel1.setText("Text Categorization Project");
		jLabel1.setFont(new Font("Times New Roman",Font.BOLD,20));
		jLabel1.setForeground(Color.MAGENTA);
		jLabel2.setText("Select Your Choice");
		jLabel2.setForeground(Color.RED);
		jLabel3.setText("Train Data");
		jLabel3.setForeground(Color.BLUE);
		jLabel4.setText("Test Data");
		jLabel4.setForeground(Color.BLUE);
		jLabel5.setText("Calculation And Path");
		jLabel5.setForeground(Color.BLUE);
		
		
		
		//Adding Labels to the JFrame
		addComponent(contentPane, jLabel1, 30,10,250,31);
		addComponent(contentPane, jLabel2, 40,50,120,18);
		addComponent(contentPane, jLabel3, 40,90,120,18);
		addComponent(contentPane, jLabel4, 40,130,120,18);
		addComponent(contentPane, jLabel5, 40,170,120,18);
		
		//Adding Buttons to the JFrame
		addComponent(contentPane, jButton1, 180,90,50,18);
		addComponent(contentPane, jButton2, 180,130,50,18);
		addComponent(contentPane, jButton3, 180,170,50,18);
		addComponent(contentPane, jButton4, 120,210,75,18);
		
		//Define Action Listener for JButtons
		jButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae1)
			{
				jButton1_actionPerformed(ae1);
			}

		});
		
		jButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae2)
			{
				jButton2_actionPerformed(ae2);
			}

		});
		
		jButton3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae3)
			{
				jButton3_actionPerformed(ae3);
			}

		});
		
		jButton4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae4)
			{
				jButton4_actionPerformed(ae4);
			}

		});
		
		//Define Window Listener
		addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                System.exit(0);
            }
        });
		
		
		this.setTitle("Text Categorization");
		this.setLocation(new Point(0, 0));
		this.setSize(new Dimension(300, 300));
		this.setResizable(false);
		
    }
    
    private void addComponent(Container container,Component c,int x,int y,int width,int height)
	{
		c.setBounds(x,y,width,height);
		container.add(c);
	}
	
	public static void main(String args[])
	{
		//call Main Class
		TextCategorization text=new TextCategorization();
	}
	
	private void jButton1_actionPerformed(ActionEvent e)
	{
		//call Datamining class to train data
		Datamining dmine=new Datamining();
		dmine.show();
		
	}
	
	private void jButton2_actionPerformed(ActionEvent e)
	{
		//call DCategory to accept and build weightage table
		dcat=new DCategory();
		dcat.show();
		
	}
	
	private void jButton3_actionPerformed(ActionEvent e)
	{
		//call Matrix Calculation to find optimum path and text classification
		obj=new MatrixCalculation();
		obj.setRewardValue();
		obj.createResultTable();
	    obj.calculateOptimum();
	    String res=obj.getResult();
		System.out.println("\nprocess completed !");
		System.out.println("\n"+res);
		JOptionPane.showMessageDialog(getContentPane(),res,"Text Categorization",JOptionPane.INFORMATION_MESSAGE);
			
		
	}
	
	private void jButton4_actionPerformed(ActionEvent e)
	{
		//Closing Application
		System.exit(0);
	}
		
   } 		