package com.jdedwards.system.connector.dynamic.sample.util;

import javax.swing.table.*;
import java.util.Vector;

/**
 * @testcase test.com.jdedwards.system.connector.dynamic.sample.util.TestOrderDetailTableModel 
 */
public class OrderDetailTableModel extends AbstractTableModel {
    protected static int NUM_COLUMNS = 8;
    protected static int START_NUM_ROWS = 1;
    protected int nextEmptyRow = 0;
    protected int numRows = 0;

    static final public String itemNumber = "Item Number";
    static final public String itemDescription = "Item Description";
    static final public String quantity = "Quantity Ordered";
    static final public String unitPrice = "Unit Price";
	static final public String extPrice = "Extended Price";
	static final public String lineType = "Line Type";
	static final public String unit = "Unit of Measure";
	static final public String supplier = "Supplier Number";

    protected Vector data = null;

    public OrderDetailTableModel() {
        data = new Vector();
    }

    public String getColumnName(int column) {
	switch (column) {
	  case 0:
	    return itemNumber;
	  case 1:
	    return itemDescription;
	  case 2:
	    return quantity;
	  case 3:
	    return unitPrice;
	  case 4:
		return extPrice;
	  case 5:
		return lineType;
	  case 6:
		return unit;
	  case 7:
		return supplier;
	
	}
	return "";
    }

    //XXX Should this really be synchronized?
    public synchronized int getColumnCount() {
        return NUM_COLUMNS;
    }

    public synchronized int getRowCount() {
        if (numRows < START_NUM_ROWS) {
            return START_NUM_ROWS;
        } else {
            return numRows;
        }
    }

    public synchronized Object getValueAt(int row, int column) {
		
		try {
		        OrderItem p = (OrderItem)data.elementAt(row);
		        switch (column) {
		          case 0:
		            return p.item_number;
		          case 1:
		            return p.item_desc;
		          case 2:
		            return String.valueOf(p.quantity_ordered);
		          case 3:
		            return String.valueOf(p.unit_price);
				case 4:
					return String.valueOf(p.extended_price);
				case 5:
					return p.line_type;
				case 6:
					return p.unit_of_measure;
				case 7:
					return p.supplier_number;
		        }
		} catch (Exception e) {
		}		
		return "";
    }

	public void setValueAt(Object value, int row, int col) {
           if (data.size() == 0) 
		   {
			   data.addElement(new OrderItem());
			   nextEmptyRow ++;
			   numRows ++;
		   }
		   
		   try {
            OrderItem p = (OrderItem)data.elementAt(row);
            switch (col) {
              case 0:
                p.item_number = (String) value;
				break;
              case 1:
                p.item_desc = (String) value;
				break;
              case 2:
               p.quantity_ordered = (new Integer((String) value)).intValue();
			   break;
              case 3:
                p.unit_price = (new Double((String) value)).doubleValue();
				break;
			case 4:
				p.extended_price = (new Double((String) value)).doubleValue();
				break;
			case 5:
				p.line_type= (String) value;
				break;
			case 6:
				p.unit_of_measure= (String) value;
				break;
			case 7:
				p.supplier_number= (String) value;
				break;
            }
	} catch (Exception e) {
	}
		   fireTableCellUpdated(row, col);
		   
           
    }
	
	public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
           
                return true;

      }
	
    public synchronized int update(int currentRow) {
        
        int index = -1; 
        
		if (currentRow == nextEmptyRow -1)
		{
		//add a row
			
			OrderItem p = (OrderItem)data.elementAt(currentRow);
            if (p.item_number == null)
            {
                return 3;
            }
			if (p.item_number.equals("")) 
				return 1;
            else if (p.quantity_ordered == 0)
				return 2;
			else {
				numRows++;
				nextEmptyRow++;
				index = nextEmptyRow;       

				//Notify listeners that the data changed.

				fireTableRowsInserted(index, index);
				data.addElement(new OrderItem());
			}
		}
		return 0;

	}
	public synchronized void reset()
	{
		int currentRows = getRowCount();
		
		for(int i = currentRows; i > 0; i--)
		{
			fireTableRowsDeleted(i, i);
		}	
		numRows = 0;
		data.removeAllElements();
		data = new Vector();
		nextEmptyRow = 0;		
	}
	
	public synchronized void cleanRow(int currentRow)
	{
		numRows--;
		nextEmptyRow--;
		
		data.removeElementAt(currentRow);
		fireTableRowsDeleted(currentRow, currentRow);
	}
	
	public class OrderItem
	{

		String item_number;
		String item_desc;
		int   quantity_ordered;
		double unit_price;
		double extended_price;
		String line_type;
		String unit_of_measure;
		String supplier_number;

	}
}