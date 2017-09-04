/*  T. J. Flesher
 *  4 Sept 2017
 *  Assignment 2.1
 * Create a Swing application that looks and behaves like the example located at the top of this assignment. Start with your code from week one and implement the use of the Oracle database in place of the array used in week one. The functionality you are looking for is the following:
 *	Modify your CLASSPATH environment variable to include both the Oracle thin drivers .jar file and the current working directory.
 *	Load the Oracle Thin Drivers
 *	Create a connection to the Oracle database.
 *	Execute a query of the database to retrieve a populated ResultSet.
 *	Then with the populated ResultSet: 
 *	The Previous button will iterate through the ResultSet moving to the previous element each time the button is clicked and will then update the GUI with the newly selected data. If the Previous button is selected while the ResultSet is positioned at the first element, your program should then move the last element and update the display with the newly selected data. 
 *	The Next button will iterate through the ResultSet moving to the next element each time the button is clicked and will then update the GUI with the newly selected data. If the Next button is selected while the ResultSet is positioned at the last element, your program should then move the first element and update the display with the newly selected data. 
 *	When the Reset button is selected you should move to the first element in the ResultSet and update the display.
 *
*/

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.sql.*;
import oracle.jdbc.OracleResultSetMetaData;

public class Assignment2_1 extends JFrame
  {

    private JButton buttonPrev = new JButton("Prev");
    private JButton buttonReset = new JButton("Reset");
    private JButton buttonNext = new JButton("Next");
    private JLabel labelHeader = new JLabel("Database Browser", JLabel.CENTER);
    private JLabel labelName = new JLabel("Name");
    private JLabel labelAddress = new JLabel("Address");
    private JLabel labelCity = new JLabel("City");
    private JLabel labelState = new JLabel("State");
    private JLabel labelZip = new JLabel("Zip");
    private JTextField textFieldName = new JTextField();
    private JTextField textFieldAddress = new JTextField();
    private JTextField textFieldCity = new JTextField();
    private JTextField textFieldState = new JTextField();
    private JTextField textFieldZip = new JTextField();
	
    int arrayPointer = 1;
    int arrayPointerEnd = 1;
    ResultSet rs;
    Connection con = null;

    public Assignment2_1(String title)
      {
        super(title);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        JPanel cp = (JPanel) getContentPane();
        labelHeader.setFont(new Font("TimesRoman", Font.BOLD, 24));
        labelHeader.setBounds(40, 10, 300, 50);
        buttonPrev.setBounds(30, 250, 80, 25);
        buttonReset.setBounds(150, 250, 80, 25);
        buttonNext.setBounds(270, 250, 80, 25);
        labelName.setBounds(10, 80, 80, 25);
        labelAddress.setBounds(10, 110, 80, 25);
        labelCity.setBounds(10, 140, 80, 25);
        labelState.setBounds(10, 170, 80, 25);
        labelZip.setBounds(10, 200, 80, 25);
        textFieldName.setBounds(120, 80, 250, 25);
        textFieldAddress.setBounds(120, 110, 250, 25);
        textFieldCity.setBounds(120, 140, 250, 25);
        textFieldState.setBounds(120, 170, 250, 25);
        textFieldZip.setBounds(120, 200, 250, 25);
        cp.setLayout(null);
        cp.add(labelHeader);
        cp.add(buttonPrev);
        cp.add(buttonReset);
        cp.add(buttonNext);
        cp.add(labelName);
        cp.add(textFieldName);
        cp.add(labelAddress);
        cp.add(textFieldAddress);
        cp.add(labelCity);
        cp.add(textFieldCity);
        cp.add(labelState);
        cp.add(textFieldState);
        cp.add(labelZip);
        cp.add(textFieldZip);
        addWindowListener(new java.awt.event.WindowAdapter()
          {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt)
              {
                shutDown();
              }
          });
        buttonPrev.addActionListener(new java.awt.event.ActionListener()
          {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt)
              {
                if (arrayPointer == 1)   //if pointer eq 1
                  {
                    arrayPointer = arrayPointerEnd;	//move pointer to last element
                  } 
				  else
                  {
                    --arrayPointer;  //decrement pointer
                  }
                setFields(arrayPointer);
              }
          });
        buttonNext.addActionListener(new java.awt.event.ActionListener()
          {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt)
              {
                if (arrayPointer == arrayPointerEnd)	//if pointer eq length of array
                  {
                    arrayPointer = 1;	//set pointer to first
                  } else
                  {
                    ++arrayPointer;		//increment pointer
                  }
                setFields(arrayPointer);
              }
          });
        buttonReset.addActionListener(new java.awt.event.ActionListener()
          {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent ae)
              {
                arrayPointer = 1;	// set default value of pointer to initial element
                setFields(arrayPointer);
              }
          });
      }

    public void fetchResultSet()
      {
        try
          {
            if (con == null || con.isClosed())
              {
                DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
                //Connect to the URL
                con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "student1", "pass");
              }
            //Execute a select statement
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String query = "SELECT * FROM ADDRESS";
            rs = stmt.executeQuery(query);
          } catch (Exception ex)
          {
            ex.printStackTrace();
            try
              {
                if (con != null)
                  {
                    rs.close();
                    con.close();
                  }
              }
               catch (Exception x)
              {
                // do nothing
              }
          }
      }
  
private void setFields(int position)
      {
        try
          {
            //check ResultSet for null, else fill
            if (rs == null)
              {
                fetchResultSet();
              }
            // get all until last record in resultset
            rs.last();
            
            arrayPointerEnd = rs.getRow();
            // set position from button event
            rs.absolute(position);
            textFieldName.setText(rs.getString(3) + " " + rs.getString(2));
            textFieldAddress.setText(rs.getString(4));
            textFieldCity.setText(rs.getString(5));
            textFieldState.setText(rs.getString(6));
            textFieldZip.setText(rs.getString(7));

          } catch (java.lang.Exception ex)
          {
            ex.printStackTrace();
          }

      }

    private void shutDown()
      {
        int returnVal = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?");
        if (returnVal == JOptionPane.YES_OPTION)
          {
            System.exit(0);
          }
      }

    public static void main(String args[])
      {
        Assignment2_1 a2 = new Assignment2_1("Database Browser w/ DBMS Connection");
        a2.setFields(1);	// set default value for DataClassArray to zero when program starts.
        a2.setSize(400, 350);
        a2.setVisible(true);
      }
  }