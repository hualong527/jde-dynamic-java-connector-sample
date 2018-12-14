package com.jdedwards.system.connector.dynamic.sample.addressbook;

import java.util.*;
import com.jdedwards.system.lib.MathNumeric;

/**
 * @testcase test.com.jdedwards.system.connector.dynamic.sample.addressbook.TestD0100031 
 */
public class D0100031{
	private Map parameterValues= new HashMap();

	public D0100031() {}

	public Map getParameterValues(){
		return parameterValues;
	}

	public void reset(){
		parameterValues.clear();
	}

	public void setszNameAlpha(String value){
		parameterValues.put("szNameAlpha",value);
	}
	public String getszNameAlpha(){
		return (String)parameterValues.get("szNameAlpha");
	}


	public void setszKanjialpha(String value){
		parameterValues.put("szKanjialpha",value);
	}
	public String getszKanjialpha(){
		return (String)parameterValues.get("szKanjialpha");
	}


	public void setmnAddressNumber(MathNumeric value){
		parameterValues.put("mnAddressNumber",value);
	}
	public void setmnAddressNumber(String value){
		parameterValues.put("mnAddressNumber",value);
	}
	public MathNumeric getmnAddressNumber(){
		return ((MathNumeric)parameterValues.get("mnAddressNumber"));
	}


	public void setszSecondaryMailingName(String value){
		parameterValues.put("szSecondaryMailingName",value);
	}
	public String getszSecondaryMailingName(){
		return (String)parameterValues.get("szSecondaryMailingName");
	}


	public void setszDescripCompressedC(String value){
		parameterValues.put("szDescripCompressedC",value);
	}
	public String getszDescripCompressedC(){
		return (String)parameterValues.get("szDescripCompressedC");
	}


	public void setszNameMailing(String value){
		parameterValues.put("szNameMailing",value);
	}
	public String getszNameMailing(){
		return (String)parameterValues.get("szNameMailing");
	}


	public void setmnLineNumberID(MathNumeric value){
		parameterValues.put("mnLineNumberID",value);
	}
	public void setmnLineNumberID(String value){
		parameterValues.put("mnLineNumberID",value);
	}
	public MathNumeric getmnLineNumberID(){
		return ((MathNumeric)parameterValues.get("mnLineNumberID"));
	}


}
