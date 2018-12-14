package com.jdedwards.system.connector.dynamic.sample.addressbook;

import java.util.*;
import com.jdedwards.system.lib.MathNumeric;

/**
 * @testcase test.com.jdedwards.system.connector.dynamic.sample.addressbook.TestD0100033 
 */
public class D0100033{
	private Map parameterValues= new HashMap();

	public D0100033() {}

	public Map getParameterValues(){
		return parameterValues;
	}

	public void reset(){
		parameterValues.clear();
	}

	public void setszAddressLine4(String value){
		parameterValues.put("szAddressLine4",value);
	}
	public String getszAddressLine4(){
		return (String)parameterValues.get("szAddressLine4");
	}


	public void setcEffectiveDateExistence10(char value){
		parameterValues.put("cEffectiveDateExistence10",new Character(value));
	}
	public void setcEffectiveDateExistence10(String value){
		parameterValues.put("cEffectiveDateExistence10",value);
	}
	public char getcEffectiveDateExistence10(){
		return ((Character)parameterValues.get("cEffectiveDateExistence10")).charValue();
	}


	public void setszWorkstationid(String value){
		parameterValues.put("szWorkstationid",value);
	}
	public String getszWorkstationid(){
		return (String)parameterValues.get("szWorkstationid");
	}


	public void setszAddressLine3(String value){
		parameterValues.put("szAddressLine3",value);
	}
	public String getszAddressLine3(){
		return (String)parameterValues.get("szAddressLine3");
	}


	public void setszState(String value){
		parameterValues.put("szState",value);
	}
	public String getszState(){
		return (String)parameterValues.get("szState");
	}


	public void setszAddressLine2(String value){
		parameterValues.put("szAddressLine2",value);
	}
	public String getszAddressLine2(){
		return (String)parameterValues.get("szAddressLine2");
	}


	public void setszAddressLine1(String value){
		parameterValues.put("szAddressLine1",value);
	}
	public String getszAddressLine1(){
		return (String)parameterValues.get("szAddressLine1");
	}


	public void setjdDateupdated(Date value){
		parameterValues.put("jdDateupdated",value);
	}
	public void setjdDateupdated(String value){
		parameterValues.put("jdDateupdated",value);
	}
	public Date getjdDateupdated(){
		return ((Date)parameterValues.get("jdDateupdated"));
	}


	public void setmnTimelastupdated(MathNumeric value){
		parameterValues.put("mnTimelastupdated",value);
	}
	public void setmnTimelastupdated(String value){
		parameterValues.put("mnTimelastupdated",value);
	}
	public MathNumeric getmnTimelastupdated(){
		return ((MathNumeric)parameterValues.get("mnTimelastupdated"));
	}


	public void setszCountyAddress(String value){
		parameterValues.put("szCountyAddress",value);
	}
	public String getszCountyAddress(){
		return (String)parameterValues.get("szCountyAddress");
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


	public void setszCity(String value){
		parameterValues.put("szCity",value);
	}
	public String getszCity(){
		return (String)parameterValues.get("szCity");
	}


	public void setjdDateBeginningEffective(Date value){
		parameterValues.put("jdDateBeginningEffective",value);
	}
	public void setjdDateBeginningEffective(String value){
		parameterValues.put("jdDateBeginningEffective",value);
	}
	public Date getjdDateBeginningEffective(){
		return ((Date)parameterValues.get("jdDateBeginningEffective"));
	}


	public void setszUserid(String value){
		parameterValues.put("szUserid",value);
	}
	public String getszUserid(){
		return (String)parameterValues.get("szUserid");
	}


	public void setszZipCodePostal(String value){
		parameterValues.put("szZipCodePostal",value);
	}
	public String getszZipCodePostal(){
		return (String)parameterValues.get("szZipCodePostal");
	}


	public void setszNamealpha(String value){
		parameterValues.put("szNamealpha",value);
	}
	public String getszNamealpha(){
		return (String)parameterValues.get("szNamealpha");
	}


	public void setszProgramid(String value){
		parameterValues.put("szProgramid",value);
	}
	public String getszProgramid(){
		return (String)parameterValues.get("szProgramid");
	}


	public void setszCountry(String value){
		parameterValues.put("szCountry",value);
	}
	public String getszCountry(){
		return (String)parameterValues.get("szCountry");
	}


}
