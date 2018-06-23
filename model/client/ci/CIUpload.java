//------------------------------------------------------------------------------
// <wsdl2code-generated>
//    This code was generated by http://www.wsdl2code.com version Beta 1.2
//
//    Please dont change this code, regeneration will override your changes
//</wsdl2code-generated>
//
//------------------------------------------------------------------------------
//
//This source code was auto-generated by Wsdl2Code Beta Version
//
package com.contactpoint.model.client.ci;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import com.contactpoint.model.client.ci.VectorCIQuestionAnswer;


public class CIUpload implements KvmSerializable {

	public static final String NAMESPACE ="http://TMSConsultant.Model/2013/ProcessCI";
	public final static String MM_NAMESPACE = "http://schemas.datacontract.org/2004/07/TollTransitions.API.DataContracts";
	//public static final String NAMESPACE ="http://TMSConsultant.Model/2012/TMSConsultantCI";
	public VectorCIQuestionAnswer questionAnswer;
	public VectorSignatureContract signatureContract;
	public boolean isMM;

	public CIUpload(){}

	public CIUpload(SoapObject soapObject){

		if (soapObject.hasProperty("QuestionAnswer"))
		{
			SoapObject j15 = (SoapObject)soapObject.getProperty("QuestionAnswer");
			questionAnswer = new VectorCIQuestionAnswer(j15);
		}
		if (soapObject.hasProperty("SignatureContract"))
		{
			SoapObject j16 = (SoapObject)soapObject.getProperty("SignatureContract");
			signatureContract = new VectorSignatureContract(j16);
		}
	}
	@Override
	public Object getProperty(int arg0) {
		switch(arg0){
		case 0:
			return questionAnswer;
		case 1:
			return signatureContract;
		}
		return null;
	}
	@Override
	public int getPropertyCount() {
		return 2;
	}
	@Override
	public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
		info.type = PropertyInfo.VECTOR_CLASS;
		switch(index){
		case 0:
			info.name = "CIQuestionAnswer";
			if (isMM) {
				info.namespace = VectorCIQuestionAnswer.MM_NAMESPACE;
			} else {
				info.namespace = VectorCIQuestionAnswer.NAMESPACE;
			}
			break;
		case 1:
			info.name = "SignatureContract";
			if (isMM) {
				info.namespace = VectorSignatureContract.MM_NAMESPACE;
			} else {
				info.namespace = VectorSignatureContract.NAMESPACE;
			}
			break;
		}
	}
	@Override
	public void setProperty(int index, Object value) {
		switch(index){
		case 0:
			questionAnswer = (VectorCIQuestionAnswer)value;
			break;
		case 1:
			signatureContract = (VectorSignatureContract)value;
			break;
		}
	}
}
