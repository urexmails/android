package com.contactpoint.model.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import com.contactpoint.controller.PhotoController;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.DTO.PhotoInDTO;
import com.contactpoint.model.DTO.ci.DeleteInDTO;
import com.contactpoint.model.DTO.ci.DownloadInDTO;
import com.contactpoint.model.DTO.ci.SearchCIInDTO;
import com.contactpoint.model.DTO.ci.UploadInDTO;
import com.contactpoint.model.DTO.prv.BookPRVInDTO;
import com.contactpoint.model.DTO.prv.DeletePRVInDTO;
import com.contactpoint.model.DTO.prv.DownloadPRVInDTO;
import com.contactpoint.model.DTO.prv.IncompletePRVInDTO;
import com.contactpoint.model.DTO.prv.ItemInDTO;
import com.contactpoint.model.DTO.prv.RoomOutDTO;
import com.contactpoint.model.DTO.prv.SearchPRVInDTO;
import com.contactpoint.model.DTO.prv.UploadPRVInDTO;
import com.contactpoint.model.client.CIForm;
import com.contactpoint.model.client.authentication.AuthenticationToken;
import com.contactpoint.model.client.authentication.RequestLogin;
import com.contactpoint.model.client.ci.CIDownload;
import com.contactpoint.model.client.ci.CIUpload;
import com.contactpoint.model.client.ci.SignatureContract;
import com.contactpoint.model.client.ci.VectorCIQuestionAnswer;
import com.contactpoint.model.client.ci.VectorCIResult;
import com.contactpoint.model.client.ci.VectorSignatureContract;
import com.contactpoint.model.client.prv.Client;
import com.contactpoint.model.client.prv.Clients;
import com.contactpoint.model.client.prv.Item;
import com.contactpoint.model.client.prv.Items;
import com.contactpoint.model.client.prv.PRVUpload;
import com.contactpoint.model.client.prv.QuestionAnswer;
import com.contactpoint.model.client.prv.Room;
import com.contactpoint.model.client.prv.RoomItem;
import com.contactpoint.model.client.prv.RoomItems;
import com.contactpoint.model.client.prv.Rooms;
import com.contactpoint.model.client.prv.VectorPRVResult;
import com.contactpoint.model.service.PRVService;

public abstract class SoapEnvelopeGenerator {

	public static SoapSerializationEnvelope getSoapEnvelope(RequestLogin param) {
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;
		SoapObject soapReq = new SoapObject(param.NAMESPACE, "RequestLogin");
		soapEnvelope.addMapping(param.NAMESPACE,"RequestLogin", RequestLogin.class);
		soapReq.addProperty("LoginCredentials", param.loginCredentials);
		soapReq.addProperty("Token", param.token);
		soapEnvelope.setOutputSoapObject(soapReq);
		return soapEnvelope;
	}

	public static SoapSerializationEnvelope getSearchCISoapEnvelope(SearchCIInDTO param, AuthenticationToken token) {
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;
		
		String namespace = token.isMM() ? param.MM_NAMESPACE : param.NAMESPACE;
		String authNamespace = token.isMM() ? AuthenticationToken.MM_NAMESPACE : AuthenticationToken.NAMESPACE;
		String resultNamespace = token.isMM() ? VectorCIResult.MM_NAMESPACE : VectorCIResult.NAMESPACE;

		SoapObject soapReq = new SoapObject(namespace,"RequestSearchCI");
		soapEnvelope.addMapping(resultNamespace,"CIResult[]", VectorCIResult.class);
		soapEnvelope.addMapping(authNamespace, "AuthenticationToken", AuthenticationToken.class);
		//soapReq.addProperty("AuthenticationToken",token);
		
		PropertyInfo pi = new PropertyInfo();
		pi.name = "AuthenticationToken";
		pi.setValue(token);
		soapReq.addProperty(pi);
		
		soapReq.addProperty("BusinessGroup", param.getBusinessGroup());
		soapReq.addProperty("FromDate", param.getFromDate());
		soapReq.addProperty("Region", param.getRegion());
		soapReq.addProperty("ToDate", param.getToDate());
		soapReq.addProperty("Volume", param.getVolume());
		soapReq.addProperty("Zone", param.getZone());

		soapEnvelope.setOutputSoapObject(soapReq);
		return soapEnvelope;
	}

	public static SoapSerializationEnvelope getDeleteCIEnvelope(DeleteInDTO param, AuthenticationToken token) {
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;
		
		String namespace = token.isMM() ? param.MM_NAMESPACE : param.NAMESPACE;
		String authNamespace = token.isMM() ? AuthenticationToken.MM_NAMESPACE : AuthenticationToken.NAMESPACE;
		
		SoapObject soapReq = new SoapObject(namespace, "RequestDeleteCI");
		soapEnvelope.addMapping(authNamespace,"AuthenticationToken", AuthenticationToken.class);
		
		if (token.isMM()) {
			soapEnvelope.addMapping(param.MM_CONTRACT_NAMESPACE,"RequestDeleteCI", param.getClass());
			
			PropertyInfo pi = new PropertyInfo();
			pi.name = "AuthenticationToken";
			pi.namespace = authNamespace;
			pi.type = AuthenticationToken.class;
			pi.setValue(token);
			soapReq.addProperty(pi);
			
			pi = new PropertyInfo();
			pi.name = "DTDeleted";
			pi.namespace = authNamespace;
			pi.type = PropertyInfo.STRING_CLASS;
			pi.setValue(param.Tracker);
			soapReq.addProperty(pi);
			
			pi = new PropertyInfo();
			pi.name = "PONumber";
			pi.namespace = authNamespace;
			pi.type = PropertyInfo.STRING_CLASS;
			pi.setValue(param.getPONumber());
			soapReq.addProperty(pi);
			
			SoapObject soapExtraWrapper = new SoapObject(namespace, "RequestDeleteCI1");
			soapExtraWrapper.addSoapObject(soapReq);
			soapEnvelope.setOutputSoapObject(soapExtraWrapper);
		} else {
			soapEnvelope.addMapping(namespace,"RequestDeleteCI", param.getClass());
			
			PropertyInfo pi = new PropertyInfo();
			pi.name = "AuthenticationToken";
			pi.setValue(token);
			soapReq.addProperty(pi);
			
			soapReq.addProperty("DTDeleted", param.Tracker);
			soapReq.addProperty("PONumber", param.getPONumber());        
			soapEnvelope.setOutputSoapObject(soapReq);
		}

		return soapEnvelope;
	}

	public static SoapSerializationEnvelope getDownloadCIEnvelope(DownloadInDTO param, AuthenticationToken token) {
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;
		
		String namespace = token.isMM() ? param.MM_NAMESPACE : param.NAMESPACE;
		String authNamespace = token.isMM() ? AuthenticationToken.MM_NAMESPACE : AuthenticationToken.NAMESPACE;
		
		SoapObject soapReq = new SoapObject(namespace,"RequestDownloadCI");
		soapEnvelope.addMapping(namespace,"CIDownload",new CIDownload().getClass());
		soapEnvelope.addMapping(authNamespace,"AuthenticationToken", AuthenticationToken.class);
		//soapReq.addProperty("AuthenticationToken",token);
		
		PropertyInfo pi = new PropertyInfo();
		pi.name = "AuthenticationToken";
		pi.setValue(token);
		soapReq.addProperty(pi);

		if (param.getPONumber() != null) {
			soapReq.addProperty("CIType", param.getCIType());
			soapReq.addProperty("DTWorklist", param.Tracker);        
			soapReq.addProperty("PONumber", param.getPONumber());
		} else {
			soapReq.addProperty("CIType", param.getCIType());
			soapReq.addProperty("DTWorklist", param.Tracker);        
			soapReq.addProperty("GroupParentOrderLineId", 0);
			soapReq.addProperty("OrderLineId", param.getOrderLineId());
		}

		soapEnvelope.setOutputSoapObject(soapReq);
		return soapEnvelope;
	}

	public static SoapSerializationEnvelope getUploadCIEnvelope(CIForm param, AuthenticationToken token) {
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;

		CIUpload form = new CIUpload();
		form.isMM = token.isMM();
		
		VectorCIQuestionAnswer AoQA = new VectorCIQuestionAnswer();
		AoQA.isMM = token.isMM();
		
		VectorSignatureContract AoSC = new VectorSignatureContract();
		AoSC.isMM = token.isMM();
		
		com.contactpoint.model.client.ci.QuestionAnswer qa = null;
		SignatureContract sc = null;

		CIDownload download = param.getDownload();
		HashMap<String, String> qas = param.getUpload().getQuestionAnswers();
		qas.put("P_Q1_SERVICE_TYPE", download.cIType);

		// replace with all
		try {
			if (qas.get(UploadInDTO.P_Q3C_9B_PLASMA_REAR_PROJ).compareTo(UploadInDTO.YES) == 0 &&
					qas.get(UploadInDTO.P_Q3C_9B_BIKE).compareTo(UploadInDTO.YES) == 0 &&
					qas.get(UploadInDTO.P_Q3C_9B_PAINTING).compareTo(UploadInDTO.YES) == 0 &&
					qas.get(UploadInDTO.P_Q3C_9B_OTHER).compareTo(UploadInDTO.YES) == 0) {
				qas.remove(UploadInDTO.P_Q3C_9B_PLASMA_REAR_PROJ);
				qas.remove(UploadInDTO.P_Q3C_9B_BIKE);
				qas.remove(UploadInDTO.P_Q3C_9B_PAINTING);
				qas.remove(UploadInDTO.P_Q3C_9B_OTHER);
				qas.put(UploadInDTO.P_Q3C_9B_ALL, UploadInDTO.YES);
			}
		} catch (NullPointerException e) {
			// do nothing
		}

		// replace with all
		try {
			if (qas.get(UploadInDTO.P_Q3C_15A_NO_SAFETY_GEAR).compareTo(UploadInDTO.YES) == 0 &&
					qas.get(UploadInDTO.P_Q3C_15A_TRUCK_LOAD).compareTo(UploadInDTO.YES) == 0 &&
					qas.get(UploadInDTO.P_Q3C_15A_UNSAFE_LIFT_CARRY).compareTo(UploadInDTO.YES) == 0 &&
					qas.get(UploadInDTO.P_Q3C_15A_PACKING).compareTo(UploadInDTO.YES) == 0 &&
					qas.get(UploadInDTO.P_Q3C_15A_TRUCK_LOCATION).compareTo(UploadInDTO.YES) == 0) {
				qas.remove(UploadInDTO.P_Q3C_15A_NO_SAFETY_GEAR);
				qas.remove(UploadInDTO.P_Q3C_15A_TRUCK_LOAD);
				qas.remove(UploadInDTO.P_Q3C_15A_UNSAFE_LIFT_CARRY);
				qas.remove(UploadInDTO.P_Q3C_15A_PACKING);
				qas.remove(UploadInDTO.P_Q3C_15A_TRUCK_LOCATION);
				qas.put(UploadInDTO.P_Q3C_15A_ALL, UploadInDTO.YES);
			}
		} catch (NullPointerException e) {
			// do nothing
		}

		// populate array of question answer
		for(String questionCode : qas.keySet()) {
			qa = new com.contactpoint.model.client.ci.QuestionAnswer();
			qa.questionCode = questionCode;
			qa.answer = param.getUpload().getQuestionAnswers().get(questionCode);
			qa.isMM = token.isMM();
			AoQA.add(qa);
		}

		// populate array of signature contract
		for (String type : param.getUpload().getSignatureContract().keySet()) {
			sc = new SignatureContract();
			sc.type = type;
			sc.signature = param.getUpload().getSignatureContract().get(type);
			if (token.isMM()) {
				sc.signature = ModelFactory.getUtilService().removeBase64Indicator(sc.signature.toString());
			}
			sc.isMM = token.isMM();
			AoSC.add(sc);
		}

		form.questionAnswer = AoQA;
		form.signatureContract = AoSC;

		DateFormat df = new SimpleDateFormat(PRVService.DATE_FORMAT);     	

		String namespace = token.isMM() ? UploadInDTO.MM_NAMESPACE : UploadInDTO.NAMESPACE;
		String authNamespace = token.isMM() ? AuthenticationToken.MM_NAMESPACE : AuthenticationToken.NAMESPACE;
		String uploadNamespace = token.isMM() ? CIUpload.MM_NAMESPACE : CIUpload.NAMESPACE;
		String vQANamespace = token.isMM() ? VectorCIQuestionAnswer.MM_NAMESPACE : VectorCIQuestionAnswer.NAMESPACE;
		String vSCNamespace = token.isMM() ? VectorSignatureContract.MM_NAMESPACE : VectorSignatureContract.NAMESPACE;
		String qaNamespace = token.isMM() ? com.contactpoint.model.client.ci.QuestionAnswer.MM_NAMESPACE : com.contactpoint.model.client.ci.QuestionAnswer.NAMESPACE;
		String scNamespace = token.isMM() ? SignatureContract.MM_NAMESPACE : SignatureContract.NAMESPACE;
		
		soapEnvelope.addMapping(authNamespace, "AuthenticationToken", AuthenticationToken.class);
		soapEnvelope.addMapping(uploadNamespace, "CIUpload", CIUpload.class);
		soapEnvelope.addMapping(vQANamespace, "ArrayOfQuestionAnswer", VectorCIQuestionAnswer.class);
		soapEnvelope.addMapping(vSCNamespace, "ArrayOfSignatureContract", VectorSignatureContract.class);
		soapEnvelope.addMapping(qaNamespace, "QuestionAnswer", QuestionAnswer.class);
		soapEnvelope.addMapping(scNamespace, "SignatureContract", SignatureContract.class);
		
		SoapObject so = new SoapObject(namespace, "RequestUploadCI");
		
		PropertyInfo pi = new PropertyInfo();
		pi.name = "AuthenticationToken";
		pi.setValue(token);
		so.addProperty(pi);
		
		so.addProperty("CIFormUpload", form);
		so.addProperty("PONumber", param.getDownload().poNumber);
		so.addProperty("UploadDate", df.format(new Date()));

		if (token.isMM()) {
			SoapObject soapExtraWrapper = new SoapObject(namespace, "RequestUploadCI1");
			soapExtraWrapper.addSoapObject(so);
			soapEnvelope.setOutputSoapObject(soapExtraWrapper);
		} else {
			soapEnvelope.setOutputSoapObject(so);			
		}
		return soapEnvelope;
	}

	public static SoapSerializationEnvelope getSearchPRVEnvelope(SearchPRVInDTO param, AuthenticationToken token) {
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;
		
		String namespace = token.isMM() ? SearchPRVInDTO.MM_NAMESPACE : SearchPRVInDTO.NAMESPACE;
		String authNamespace = token.isMM() ? AuthenticationToken.MM_NAMESPACE : AuthenticationToken.NAMESPACE;

		SoapObject soapReq = new SoapObject(namespace,"RequestSearchPRV");
		soapEnvelope.addMapping(namespace,"PRVResult[]", VectorPRVResult.class);
		soapEnvelope.addMapping(authNamespace, "AuthenticationToken", AuthenticationToken.class);
		
		PropertyInfo pi = new PropertyInfo();
		pi.name = "AuthenticationToken";
		pi.setValue(token);
		soapReq.addProperty(pi);
		
		if (param.getPONumber().length() != 0) {
			soapReq.addProperty("PONumber",param.getPONumber());
		} else if (param.getOrderNumber().length() != 0) {
			soapReq.addProperty("OrderNumber",param.getOrderNumber());        	
		} else if (param.getProviderCode().ID != SearchPRVInDTO.PROV_CODE_DEFAULT_ID) {
			soapReq.addProperty("ProviderCode",param.getProviderCode().Description);
		} else {
			soapReq.addProperty("BusinessGroup",param.getBusinessGroup());
			soapReq.addProperty("ClientCode",param.getClientCode());
			soapReq.addProperty("FromDate",param.getFromDate());
			soapReq.addProperty("Region",param.getRegion());
			soapReq.addProperty("ToDate",param.getToDate());
			soapReq.addProperty("Zone",param.getZone());
		}

		soapEnvelope.setOutputSoapObject(soapReq);
		return soapEnvelope;
	}

	public static SoapSerializationEnvelope getDownloadPRVEnvelope(DownloadPRVInDTO param, AuthenticationToken token) {
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;
		
		String namespace = token.isMM() ? DownloadPRVInDTO.MM_NAMESPACE : DownloadPRVInDTO.NAMESPACE;
		String authNamespace = token.isMM() ? AuthenticationToken.MM_NAMESPACE : AuthenticationToken.NAMESPACE;
		
		SoapObject soapReq = new SoapObject(namespace, "RequestDownloadPRV");
		soapEnvelope.addMapping(authNamespace, "AuthenticationToken", AuthenticationToken.class);
		
		PropertyInfo pi = new PropertyInfo();
		pi.name = "AuthenticationToken";
		pi.setValue(token);
		soapReq.addProperty(pi);

		if (param.getBookingDetailsID() != null) {
			soapReq.addProperty("BookingDetailsID", param.getBookingDetailsID());
		}
		soapReq.addProperty("DTWorklist", param.Tracker);
		soapReq.addProperty("GroupParentOrderLineId", param.getGroupParentOrderLineId());
		soapReq.addProperty("OrderLineId", param.getOrderLineId());
		soapReq.addProperty("PONumber", param.getPONumber());

		soapEnvelope.setOutputSoapObject(soapReq);
		return soapEnvelope;
	}
	
	public static SoapSerializationEnvelope getDeletePRVEnvelope(DeletePRVInDTO param, AuthenticationToken token) {
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;
		
		String namespace = token.isMM() ? DeletePRVInDTO.MM_NAMESPACE : DeletePRVInDTO.NAMESPACE;
		String authNamespace = token.isMM() ? AuthenticationToken.MM_NAMESPACE : AuthenticationToken.NAMESPACE;
		
		SoapObject soapReq = new SoapObject(namespace, "RequestDeletePRV");
		soapEnvelope.addMapping(authNamespace,"AuthenticationToken", AuthenticationToken.class);
		
		if (token.isMM()) {
			soapEnvelope.addMapping(DeletePRVInDTO.MM_CONTRACT_NAMESPACE, "RequestDeletePRV", param.getClass());
			
			PropertyInfo pi = new PropertyInfo();
			pi.name = "AuthenticationToken";
			pi.namespace = authNamespace;
			pi.type = AuthenticationToken.class;
			pi.setValue(token);
			soapReq.addProperty(pi);
			
			pi = new PropertyInfo();
			pi.name = "BookingDetailsID";
			pi.namespace = authNamespace;
			pi.type = PropertyInfo.STRING_CLASS;
			pi.setValue(param.getBookingDetailsID());
			soapReq.addProperty(pi);
			
			pi = new PropertyInfo();
			pi.name = "DTDelete";
			pi.namespace = authNamespace;
			pi.type = PropertyInfo.STRING_CLASS;
			pi.setValue(param.Tracker);
			soapReq.addProperty(pi);
			
			pi = new PropertyInfo();
			pi.name = "PONumber";
			pi.namespace = authNamespace;
			pi.type = PropertyInfo.STRING_CLASS;
			pi.setValue(param.getPONumber());
			soapReq.addProperty(pi);
			
			SoapObject soapExtraWrapper = new SoapObject(namespace, "RequestDeletePRV1");
			soapExtraWrapper.addSoapObject(soapReq);
			soapEnvelope.setOutputSoapObject(soapExtraWrapper);
		} else {
			PropertyInfo pi = new PropertyInfo();
			pi.name = "AuthenticationToken";
			pi.setValue(token);
			soapReq.addProperty(pi);
			
			soapReq.addProperty("BookingDetailsID", param.getBookingDetailsID());
			soapReq.addProperty("DTDelete", param.Tracker);
			soapReq.addProperty("PONumber", param.getPONumber());
			
			soapEnvelope.setOutputSoapObject(soapReq);
		}
		return soapEnvelope;
	}
	
	public static SoapSerializationEnvelope getBookPRVEnvelope(BookPRVInDTO param, AuthenticationToken token) {
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;
		
		String namespace = token.isMM() ? BookPRVInDTO.MM_NAMESPACE : BookPRVInDTO.NAMESPACE;
		String authNamespace = token.isMM() ? AuthenticationToken.MM_NAMESPACE : AuthenticationToken.NAMESPACE;
		
		SoapObject soapReq = new SoapObject(namespace, "RequestBookPRV");
		soapEnvelope.addMapping(authNamespace, "AuthenticationToken", AuthenticationToken.class);
		
		if (token.isMM()) {
			PropertyInfo pi = new PropertyInfo();
			pi.name = "AuthenticationToken";
			pi.namespace = authNamespace;
			pi.type = AuthenticationToken.class;
			pi.setValue(token);
			soapReq.addProperty(pi);
			
			pi = new PropertyInfo();
			pi.name = "BookedDate";
			pi.namespace = authNamespace;
			pi.type = PropertyInfo.STRING_CLASS;
			pi.setValue(param.getBookedDate());
			soapReq.addProperty(pi);

			pi = new PropertyInfo();
			pi.name = "BookingDetailsID";
			pi.namespace = authNamespace;
			pi.type = PropertyInfo.STRING_CLASS;
			pi.setValue(param.getBookingDetailsID());
			soapReq.addProperty(pi);
			
			pi = new PropertyInfo();
			pi.name = "ContactFailureCode";
			pi.namespace = authNamespace;
			pi.type = PropertyInfo.STRING_CLASS;
			pi.setValue(param.getContactFailureCode());
			soapReq.addProperty(pi);

			pi = new PropertyInfo();
			pi.name = "ContactFailureDate";
			pi.namespace = authNamespace;
			pi.type = PropertyInfo.STRING_CLASS;
			pi.setValue(param.getContactFailureDate());
			soapReq.addProperty(pi);

			pi = new PropertyInfo();
			pi.name = "ContactFailureDetails";
			pi.namespace = authNamespace;
			pi.type = PropertyInfo.STRING_CLASS;
			pi.setValue(param.getContactFailureDetails());
			soapReq.addProperty(pi);

			pi = new PropertyInfo();
			pi.name = "PONumber";
			pi.namespace = authNamespace;
			pi.type = PropertyInfo.STRING_CLASS;
			pi.setValue(param.getPONumber());
			soapReq.addProperty(pi);

			pi = new PropertyInfo();
			pi.name = "PastEndDateReasonCode";
			pi.namespace = authNamespace;
			pi.type = PropertyInfo.STRING_CLASS;
			pi.setValue(param.getPastEndDateReasonCode());
			soapReq.addProperty(pi);

			pi = new PropertyInfo();
			pi.name = "PastEndDateReasonDetails";
			pi.namespace = authNamespace;
			pi.type = PropertyInfo.STRING_CLASS;
			pi.setValue(param.getPastEndDateReasonDetails());
			soapReq.addProperty(pi);

			pi = new PropertyInfo();
			pi.name = "ReBookingReasonCode";
			pi.namespace = authNamespace;
			pi.type = PropertyInfo.STRING_CLASS;
			pi.setValue(param.getReBookingReasonCode());
			soapReq.addProperty(pi);

			pi = new PropertyInfo();
			pi.name = "ReBookingReasonDetails";
			pi.namespace = authNamespace;
			pi.type = PropertyInfo.STRING_CLASS;
			pi.setValue(param.getReBookingReasonDetails());
			soapReq.addProperty(pi);
			
			SoapObject soapExtraWrapper = new SoapObject(namespace, "RequestBookPRV1");
			soapExtraWrapper.addSoapObject(soapReq);
			soapEnvelope.setOutputSoapObject(soapExtraWrapper);
		} else {
			PropertyInfo pi = new PropertyInfo();
			pi.name = "AuthenticationToken";
			pi.setValue(token);
			soapReq.addProperty(pi);
			
			soapReq.addProperty("BookedDate", param.getBookedDate());
			soapReq.addProperty("BookingDetailsID", param.getBookingDetailsID());
			soapReq.addProperty("ContactFailureCode", param.getContactFailureCode());
			soapReq.addProperty("ContactFailureDate", param.getContactFailureDate());
			soapReq.addProperty("ContactFailureDetails", param.getContactFailureDetails());
			soapReq.addProperty("PONumber", param.getPONumber());
			soapReq.addProperty("PastEndDateReasonCode", param.getPastEndDateReasonCode());
			soapReq.addProperty("PastEndDateReasonDetails", param.getPastEndDateReasonDetails());
			soapReq.addProperty("ReBookingReasonCode", param.getReBookingReasonCode());
			soapReq.addProperty("ReBookingReasonDetails", param.getReBookingReasonDetails());
			
			soapEnvelope.setOutputSoapObject(soapReq);
		}

		return soapEnvelope;
	}
	
	public static SoapSerializationEnvelope getIncompletePRVEnvelope(IncompletePRVInDTO param, AuthenticationToken token) {
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;
		
		String namespace = token.isMM() ? IncompletePRVInDTO.MM_NAMESPACE : IncompletePRVInDTO.NAMESPACE;
		String authNamespace = token.isMM() ? AuthenticationToken.MM_NAMESPACE : AuthenticationToken.NAMESPACE;
				
		SoapObject soapReq = new SoapObject(namespace, "RequestIncompletePRV");
		soapEnvelope.addMapping(authNamespace, "AuthenticationToken", AuthenticationToken.class);
		
		if (token.isMM()) {
			PropertyInfo pi = new PropertyInfo();
			pi.name = "AuthenticationToken";
			pi.namespace = authNamespace;
			pi.type = AuthenticationToken.class;
			pi.setValue(token);
			soapReq.addProperty(pi);
			
			pi = new PropertyInfo();
			pi.name = "DTIncomplete";
			pi.namespace = authNamespace;
			pi.type = PropertyInfo.STRING_CLASS;
			pi.setValue(param.Tracker);
			soapReq.addProperty(pi);

			pi = new PropertyInfo();
			pi.name = "PONumber";
			pi.namespace = authNamespace;
			pi.type = PropertyInfo.STRING_CLASS;
			pi.setValue(param.getPONumber());
			soapReq.addProperty(pi);

			pi = new PropertyInfo();
			pi.name = "Reason";
			pi.namespace = authNamespace;
			pi.type = PropertyInfo.STRING_CLASS;
			pi.setValue(param.getReason());
			soapReq.addProperty(pi);

			SoapObject soapExtraWrapper = new SoapObject(namespace, "RequestIncompletePRV1");
			soapExtraWrapper.addSoapObject(soapReq);
			soapEnvelope.setOutputSoapObject(soapExtraWrapper);
		} else {
			PropertyInfo pi = new PropertyInfo();
			pi.name = "AuthenticationToken";
			pi.setValue(token);
			soapReq.addProperty(pi);
			
			soapReq.addProperty("DTIncomplete", param.Tracker);
			soapReq.addProperty("PONumber", param.getPONumber());
			soapReq.addProperty("Reason", param.getReason());
			
			soapEnvelope.setOutputSoapObject(soapReq);
		}
		return soapEnvelope;
	}
	
	public static SoapSerializationEnvelope getReferenceEnvelope(AuthenticationToken token) {
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;
		SoapObject soapReq = null;
		
		soapReq = new SoapObject(BookPRVInDTO.NAMESPACE, "RequestReferenceData");
		soapEnvelope.addMapping(Clients.NAMESPACE,"Client[]",Clients.class);
		soapEnvelope.addMapping(Items.NAMESPACE,"Item[]",Items.class);
		soapEnvelope.addMapping(RoomItems.NAMESPACE,"RoomItem[]",RoomItems.class);
		soapEnvelope.addMapping(Rooms.NAMESPACE,"Room[]",Rooms.class);
		soapEnvelope.addMapping(AuthenticationToken.NAMESPACE,"AuthenticationToken", AuthenticationToken.class);
		
		PropertyInfo pi = new PropertyInfo();
		pi.name = "AuthenticationToken";
		pi.setValue(token);
		soapReq.addProperty(pi);
		
		soapEnvelope.setOutputSoapObject(soapReq);
		return soapEnvelope;
	}
	
	public static SoapSerializationEnvelope getMMReferenceCheckDateEnvelope(AuthenticationToken token) {
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;
		SoapObject soapReq = null;
		
		soapReq = new SoapObject(BookPRVInDTO.MM_NAMESPACE, "RequestReferenceDataCheckDate");
		soapEnvelope.addMapping(AuthenticationToken.MM_NAMESPACE,"AuthenticationToken", AuthenticationToken.class);
		
		SoapObject soapExtraWrapper = new SoapObject(BookPRVInDTO.MM_NAMESPACE, "RequestStandardField");
		
		PropertyInfo pi = new PropertyInfo();
		pi.name = "AuthenticationToken";
		pi.setValue(token);
		soapExtraWrapper.addProperty(pi);
		
		soapReq.addSoapObject(soapExtraWrapper);
		
		soapEnvelope.setOutputSoapObject(soapReq);
		return soapEnvelope;
	}
	
	public static SoapSerializationEnvelope getMMReferenceDownloadEnvelope(AuthenticationToken token) {
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;
		SoapObject soapReq = null;
		
		soapReq = new SoapObject(BookPRVInDTO.MM_NAMESPACE, "RequestReferenceData1");
		soapEnvelope.addMapping(AuthenticationToken.MM_NAMESPACE,"AuthenticationToken", AuthenticationToken.class);
		
		SoapObject soapExtraWrapper = new SoapObject(BookPRVInDTO.MM_NAMESPACE, "RequestReferenceData");
		PropertyInfo pi = new PropertyInfo();
		pi.name = "AuthenticationToken";
		pi.setValue(token);
		soapExtraWrapper.addProperty(pi);
		
		soapReq.addSoapObject(soapExtraWrapper);
		
		soapEnvelope.setOutputSoapObject(soapReq);
		return soapEnvelope;
	}
	
	public static SoapSerializationEnvelope getUploadPRVEnvelope(UploadPRVInDTO param, AuthenticationToken token) {
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;
		
		String namespace = token.isMM() ? PhotoInDTO.MM_NAMESPACE : PhotoInDTO.NAMESPACE;
		String authNamespace = token.isMM() ? AuthenticationToken.MM_NAMESPACE : AuthenticationToken.NAMESPACE;
		//String clientNamespace = token.isMM() ? Client.MM_NAMESPACE : Client.NAMESPACE;
		String itemsNamespace = token.isMM() ? Items.MM_NAMESPACE : Items.NAMESPACE;
		String itemNamespace = token.isMM() ? Item.MM_NAMESPACE : Item.NAMESPACE;
		String roomsNamespace = token.isMM() ? Rooms.MM_NAMESPACE : Rooms.NAMESPACE;
		String roomNamespace = token.isMM() ? Room.MM_NAMESPACE : Room.NAMESPACE;
		String roomItemsNamespace = token.isMM() ? RoomItems.MM_NAMESPACE : RoomItems.NAMESPACE;
		String roomItemNamespace = token.isMM() ? RoomItem.MM_NAMESPACE : RoomItem.NAMESPACE;
		String vqaNamespace = token.isMM() ? com.contactpoint.model.client.prv.VectorPRVQuestionAnswer.MM_NAMESPACE : com.contactpoint.model.client.prv.VectorPRVQuestionAnswer.NAMESPACE;
		String qaNamespace = token.isMM() ? com.contactpoint.model.client.prv.QuestionAnswer.MM_NAMESPACE : com.contactpoint.model.client.prv.QuestionAnswer.NAMESPACE;
				
		SoapObject soapReq = new SoapObject(namespace,"RequestUploadPRV");
		soapEnvelope.addMapping(authNamespace,"AuthenticationToken", AuthenticationToken.class);
		//soapEnvelope.addMapping(Client.NAMESPACE,"Client",Client.class);
		soapEnvelope.addMapping(itemNamespace,"Item",ItemInDTO.class);
		soapEnvelope.addMapping(roomItemNamespace,"RoomItem",RoomItem.class);
		soapEnvelope.addMapping(roomNamespace,"Room",RoomOutDTO.class);
		//soapEnvelope.addMapping(Clients.NAMESPACE,"Clients",Clients.class);
		soapEnvelope.addMapping(itemsNamespace,"Items",Items.class);
		soapEnvelope.addMapping(roomItemsNamespace,"RoomItems",RoomItems.class);
		soapEnvelope.addMapping(roomsNamespace,"Rooms",Rooms.class);
		soapEnvelope.addMapping(vqaNamespace, 
				"ArrayOfQuestionAnswer", com.contactpoint.model.client.prv.VectorPRVQuestionAnswer.class);
		soapEnvelope.addMapping(qaNamespace, 
				"QuestionAnswer", com.contactpoint.model.client.prv.QuestionAnswer.class);
		
		if (token.isMM()) {
			PropertyInfo pi = new PropertyInfo();
			pi.name = "AuthenticationToken";
			pi.namespace = authNamespace;
			pi.type = AuthenticationToken.class;
			pi.setValue(token);
			soapReq.addProperty(pi);
			
			pi = new PropertyInfo();
			pi.name = "PONumber";
			pi.namespace = authNamespace;
			pi.type = PropertyInfo.STRING_CLASS;
			pi.setValue(param.getPONumber());
			soapReq.addProperty(pi);
			
			pi = new PropertyInfo();
			pi.name = "PRVFormUpload";
			pi.namespace = authNamespace;
			pi.type = PRVUpload.class;
			pi.setValue(param.getPRVUpload());
			soapReq.addProperty(pi);
			
			pi = new PropertyInfo();
			pi.name = "UploadDate";
			pi.namespace = authNamespace;
			pi.type = PropertyInfo.STRING_CLASS;
			pi.setValue(ModelFactory.getUtilService()
					.getCurrentDateWithFormat(PRVService.DATE_FORMAT));
			soapReq.addProperty(pi);
			
			SoapObject soapExtraWrapper = new SoapObject(namespace, "RequestUploadPRV1");
			soapExtraWrapper.addSoapObject(soapReq);
			soapEnvelope.setOutputSoapObject(soapExtraWrapper);
		} else {
			PropertyInfo pi = new PropertyInfo();
			pi.name = "AuthenticationToken";
			pi.setValue(token);
			soapReq.addProperty(pi);
			
			soapReq.addProperty("PONumber", param.getPONumber());
			soapReq.addProperty("PRVFormUpload", param.getPRVUpload());
			
			soapReq.addProperty("UploadDate", ModelFactory.getUtilService()
					.getCurrentDateWithFormat(PRVService.DATE_FORMAT));
			
			soapEnvelope.setOutputSoapObject(soapReq);			
		}
		return soapEnvelope;
	}
	
	public static SoapSerializationEnvelope getPhotoEnvelope(PhotoInDTO param, AuthenticationToken token) {
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;
		
		String namespace = token.isMM() ? PhotoInDTO.MM_NAMESPACE : PhotoInDTO.NAMESPACE;
		String authNamespace = token.isMM() ? AuthenticationToken.MM_NAMESPACE : AuthenticationToken.NAMESPACE;
				
		SoapObject soapReq = new SoapObject(namespace, "RequestUploadPhoto");
		soapEnvelope.addMapping(authNamespace, "AuthenticationToken", AuthenticationToken.class);
		
		if (token.isMM()) {
			PropertyInfo pi = new PropertyInfo();
			pi.name = "AppType";
			pi.namespace = authNamespace;
			pi.type = PropertyInfo.STRING_CLASS;
			pi.setValue(param.getAppType());
			soapReq.addProperty(pi);
			
			pi = new PropertyInfo();
			pi.name = "AuthenticationToken";
			pi.namespace = authNamespace;
			pi.type = AuthenticationToken.class;
			pi.setValue(token);
			soapReq.addProperty(pi);
			
			pi = new PropertyInfo();
			pi.name = "Category";
			pi.namespace = authNamespace;
			pi.type = PropertyInfo.STRING_CLASS;
			pi.setValue(param.getCategory());
			soapReq.addProperty(pi);
			
			pi = new PropertyInfo();
			pi.name = "Comment";
			pi.namespace = authNamespace;
			pi.type = PropertyInfo.STRING_CLASS;
			pi.setValue(param.getComment());
			soapReq.addProperty(pi);
			
			pi = new PropertyInfo();
			pi.name = "PONumber";
			pi.namespace = authNamespace;
			pi.type = PropertyInfo.STRING_CLASS;
			pi.setValue(param.getPONumber());
			soapReq.addProperty(pi);
			
			pi = new PropertyInfo();
			pi.name = "Photo";
			pi.namespace = authNamespace;
			pi.type = PropertyInfo.STRING_CLASS;
			String photo = PhotoController.getStringPic(param.getPhoto());
			photo = ModelFactory.getUtilService().removeBase64Indicator(photo);
			pi.setValue(photo);
			soapReq.addProperty(pi);
			
			SoapObject soapExtraWrapper = new SoapObject(namespace, "RequestUploadPhoto1");
			soapExtraWrapper.addSoapObject(soapReq);
			soapEnvelope.setOutputSoapObject(soapExtraWrapper);
		} else {
			soapReq.addProperty("AppType", param.getAppType());
			
			PropertyInfo pi = new PropertyInfo();
			pi.name = "AuthenticationToken";
			pi.setValue(token);
			soapReq.addProperty(pi);
			
			soapReq.addProperty("Category", param.getCategory());
			soapReq.addProperty("Comment", param.getComment());
			soapReq.addProperty("PONumber", param.getPONumber());
			
			//String l = PhotoController.getStringPic(param.getPhoto());
			//System.out.println("Length: " + l.length());
			//soapReq.addProperty("Photo", l);
			
			soapReq.addProperty("Photo", PhotoController.getStringPic(param.getPhoto()));
			soapEnvelope.setOutputSoapObject(soapReq);
		}
		
		return soapEnvelope;
	}
	
	public static SoapSerializationEnvelope getEnvironmentEnvelope(AuthenticationToken token) {
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;
		SoapObject soapReq = new SoapObject(BookPRVInDTO.MM_NAMESPACE,"RequestEnvironment");
		soapEnvelope.addMapping(AuthenticationToken.MM_NAMESPACE,"AuthenticationToken", AuthenticationToken.class);
		PropertyInfo pi = new PropertyInfo();
		pi.name = "AuthenticationToken";
		pi.setValue(token);
		soapReq.addProperty(pi);
		
		soapEnvelope.setOutputSoapObject(soapReq);
		return soapEnvelope;
	}
}
