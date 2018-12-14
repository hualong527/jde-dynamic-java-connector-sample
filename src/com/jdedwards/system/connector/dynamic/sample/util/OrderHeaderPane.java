package com.jdedwards.system.connector.dynamic.sample.util;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @testcase test.com.jdedwards.system.connector.dynamic.sample.util.TestOrderHeaderPane 
 */
public class OrderHeaderPane extends JPanel
{
    public static String SO="SO";
    public static String OP="OP";

	public JTextField orderNo;
	public JTextField co;
	public JTextField type;
	public JTextField branchPlant;
	public JTextField previousOrderNo;
	public JTextField previousType;
	public JTextField previousCo;
	public JTextField addressNo;
	public JTextField orderDate;
	public JTextField shipto;
	public JTextField orderTotal;
		
    public OrderHeaderPane(String orderType)
	{
		
        JPanel main = new JPanel();
		JPanel form = new JPanel();	
		GridBagLayout gridBag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		
		setBorder(new EmptyBorder(10,10,10,10));
		setLayout(new BorderLayout());
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.anchor = GridBagConstraints.NORTH;

		main.setLayout(new BorderLayout());
        String title= (orderType.equalsIgnoreCase(SO))?"Sales Order Entry":"Purchase Order Entry";
		main.setBorder(BorderFactory.createTitledBorder
		(BorderFactory.createLineBorder(Color.gray, 1), title));
		
		form.setBorder(new EmptyBorder(0,0,0,0));
		form.setLayout(gridBag);
				
		Label label_otc = new Label("Order#/Type/CO");
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 1.0;
		gridBag.setConstraints(label_otc, c);
		form.add(label_otc);
				
		orderNo = new JTextField();
		orderNo.setEditable(false);
		orderNo.setBackground(Color.lightGray);
		c.gridx = 1;
		c.gridy = 0;
		gridBag.setConstraints(orderNo, c);
		form.add(orderNo);
		
		type = new JTextField();
		c.gridx = 2;
		c.gridy = 0;
		gridBag.setConstraints(type, c);
		form.add(type);
		
		co = new JTextField();
		c.gridx = 3;
		c.gridy = 0;
		gridBag.setConstraints(co, c);
		form.add(co);
		
		Label label_bp = new Label("Branch/Plant");
		c.insets = new Insets(0,1,0,0);
		c.gridx = 4;
		c.gridy = 0;
		gridBag.setConstraints(label_bp, c);
		form.add(label_bp);
		
		branchPlant = new JTextField();
		c.gridx = 5;
		c.gridy = 0;
		gridBag.setConstraints(branchPlant, c);
		form.add(branchPlant);
		
		Label label_previous = new Label("Previous");
		c.gridx = 0;
		c.gridy = 1;
		gridBag.setConstraints(label_previous, c);
		form.add(label_previous);
		
		previousOrderNo = new JTextField();
		previousOrderNo.setEditable(false);
		previousOrderNo.setBackground(Color.lightGray);
		c.gridx = 1;
		c.gridy = 1;
		gridBag.setConstraints(previousOrderNo, c);
		form.add(previousOrderNo);
		
		previousType = new JTextField();
		previousType.setEditable(false);
		previousType.setBackground(Color.lightGray);
		c.gridx = 2;
		c.gridy = 1;
		gridBag.setConstraints(previousType, c);
		form.add(previousType);
		
		previousCo = new JTextField();
		previousCo.setEditable(false);
		previousCo.setBackground(Color.lightGray);
		c.gridx = 3;
		c.gridy = 1;
		gridBag.setConstraints(previousCo, c);
		form.add(previousCo);
		
		Label label_soldto= (orderType.equalsIgnoreCase(SO))?new Label("Sold To"):new Label("Supplier Number");
		c.gridx = 0;
		c.gridy = 2;
		gridBag.setConstraints(label_soldto, c);
		form.add(label_soldto);
		
		addressNo = new JTextField();
		c.gridx = 1;
		c.gridy = 2;
		gridBag.setConstraints(addressNo, c);
		form.add(addressNo);
		
		Label label_orderdate = new Label("Order Date");
		c.gridx = 4;
		c.gridy = 2;
		gridBag.setConstraints(label_orderdate, c);
		form.add(label_orderdate);
		
		orderDate = new JTextField();
        orderDate.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e) {
                if (((e.getKeyCode() == KeyEvent.VK_TAB || (e.getKeyCode()==KeyEvent.VK_DOWN)))){
                    if (orderDate.getText().length()==0) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                        orderDate.setText(simpleDateFormat.format(new Date()));
                    }
                }
            }
        }
            );
		c.gridx = 5;
		c.gridy = 2;
		gridBag.setConstraints(orderDate, c);
		form.add(orderDate);
		
		Label label_shipto = new Label("Ship To");
		c.gridx = 0;
		c.gridy = 3;
		gridBag.setConstraints(label_shipto, c);
		form.add(label_shipto);
		
		shipto = new JTextField();
		c.gridx = 1;
		c.gridy = 3;
		gridBag.setConstraints(shipto, c);
		form.add(shipto);
		
		Label label_orderTotal = new Label("Order Total");
		c.gridx = 4;
		c.gridy = 3;
		gridBag.setConstraints(label_orderTotal, c);
		form.add(label_orderTotal);
		
		orderTotal = new JTextField();
		orderTotal.setEditable(false);
		c.gridx = 5;
		c.gridy = 3;
		gridBag.setConstraints(orderTotal, c);
		form.add(orderTotal);
		
		add("Center", main);
		main.add("Center", form);
    }

    public void reset(){
        orderNo.setText("");
        co.setText("");
        type.setText("");
        branchPlant.setText("");
        addressNo.setText("");
        orderDate.setText("");
        shipto.setText("");
        orderTotal.setText("");
//        previousOrderNo.setText("");
//        previousType.setText("");
//        previousCo.setText("");
    }
}