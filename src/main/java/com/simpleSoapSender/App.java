package com.simpleSoapSender;

import java.net.URL;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeader;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.dom.DOMSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main( String[] args )
	{
		try {
			URL endpointUrl = new URL("file:/E:/Anh2/ws/SendService.asmx.xml");

//			File file = new File("E:/Anh2/ws/SendService.asmx.xml");
//			System.out.println(file.toURI());
			
			// create the service and dispatch
			QName serviceName = QName.valueOf("{com.esendex.ems.soapinterface}SendService");
			Service service = Service.create(endpointUrl, serviceName);

			QName port = QName.valueOf("{com.esendex.ems.soapinterface}SendServiceSoap");
			service.addPort(port, SOAPBinding.SOAP12HTTP_BINDING, "http://127.0.0.1:8098");

			SOAPMessage soapMessage = getSoapMessage("E:/Anh2/ws/samplerequest.xml");
			Dispatch<SOAPMessage> dispatch = service.createDispatch(port, SOAPMessage.class, Service.Mode.MESSAGE);
			dispatch.invoke(soapMessage);
			
//			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
//			SOAPConnection soapConn = soapConnectionFactory.createConnection();
//			soapConn.call(request, to);
			
		}
		catch (Exception ex) {
			System.out.println(ex);
		}
	}
	
	private static SOAPMessage getSoapMessage(String filePath) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			DOMSource domSource = new DOMSource(docBuilder.parse("file:/" + filePath));
			
			MessageFactory messageFactory = MessageFactory.newInstance();
			SOAPMessage soapMessage = messageFactory.createMessage();
			soapMessage.getSOAPPart().setContent(domSource);
			
			MimeHeaders mimeHeaders = soapMessage.getMimeHeaders();
			Iterator<MimeHeader> itor = mimeHeaders.getAllHeaders();
			while(itor.hasNext()) {
				MimeHeader mimeHeader = itor.next();
				System.out.println("mimeHeader: " + mimeHeader.getName() + " : " + mimeHeader.getValue());
			}
			
			return soapMessage;
		} catch (Exception ex) {
			System.out.println(ex);			
		}
		return null;
	}
}
