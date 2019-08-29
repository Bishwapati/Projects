package com.eb.utcl.F110Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.ecm.extension.PluginResponseUtil;
import com.ibm.ecm.extension.PluginService;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.ecm.json.JSONMessage;
import com.ibm.ecm.json.JSONResponse;
import com.ibm.json.java.JSONObject;

/**
 * Provides an abstract class that is extended to create a class implementing
 * each service provided by the plug-in. Services are actions, similar to
 * servlets or Struts actions, that perform operations on the IBM Content
 * Navigator server. A service can access content server application programming
 * interfaces (APIs) and Java EE APIs.
 * <p>
 * Services are invoked from the JavaScript functions that are defined for the
 * plug-in by using the <code>ecm.model.Request.invokePluginService</code>
 * function.
 * </p>
 * Follow best practices for servlets when implementing an IBM Content Navigator
 * plug-in service. In particular, always assume multi-threaded use and do not
 * keep unshared information in instance variables.
 */
public class F110Services  extends PluginService {

	/**
	 * Returns the unique identifier for this service.
	 * <p>
	 * <strong>Important:</strong> This identifier is used in URLs so it must
	 * contain only alphanumeric characters.
	 * </p>
	 * 
	 * @return A <code>String</code> that is used to identify the service.
	 */
	public String getId() {
		return "F110Services";
	}

	/**
	 * Returns the name of the IBM Content Navigator service that this service
	 * overrides. If this service does not override an IBM Content Navigator
	 * service, this method returns <code>null</code>.
	 * 
	 * @returns The name of the service.
	 */
	public String getOverriddenService() {
		return null;
	}

	/**
	 * Performs the action of this service.
	 * 
	 * @param callbacks
	 *            An instance of the <code>PluginServiceCallbacks</code> class
	 *            that contains several functions that can be used by the
	 *            service. These functions provide access to the plug-in
	 *            configuration and content server APIs.
	 * @param request
	 *            The <code>HttpServletRequest</code> object that provides the
	 *            request. The service can access the invocation parameters from
	 *            the request.
	 * @param response
	 *            The <code>HttpServletResponse</code> object that is generated
	 *            by the service. The service can get the output stream and
	 *            write the response. The response must be in JSON format.
	 * @throws Exception
	 *             For exceptions that occur when the service is running. If the
	 *             logging level is high enough to log errors, information about
	 *             the exception is logged by IBM Content Navigator.
	 */
	public void execute(PluginServiceCallbacks callbacks,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		JSONResponse jsonResponse=null;
		JSONMessage msg=null;
		String MethodType= request.getParameter("methodName");

		
		F110Services f110Services= new F110Services();
		
		
		java.util.Properties props = new java.util.Properties();
		String serviceURL=null;
		  
		  
        try {
        

        java.io.InputStream is = null;
        is=	new java.io.FileInputStream("D:\\Ultratech\\Config\\F110ConfigProps.properties");
        props.load(is);
        
      
        
        if(is!=null && MethodType!=null)
        {
        	if(MethodType.equalsIgnoreCase("getVendorBankDetails"))
        	{
        		serviceURL=props.getProperty("VendorBankDetail");
        	}
        	else if(MethodType.equalsIgnoreCase("runPayment"))
        	{
        		serviceURL=props.getProperty("F110Process");
        		
        	}
        	else if(MethodType.equalsIgnoreCase("getpaymentDetails"))
        	{
        		serviceURL=props.getProperty("PaymentDetail");
        	}
        	
        	jsonResponse=f110Services.getF110ServiceData("");
        	
        	
        }

        else
        {
        	if(is==null)
        	{
        		//jsonResponse.put("Error", "Property File Path Not Found");
        		msg = new JSONMessage(0, "Property File Path Not Found", "Exception Occurred, service is having issue to execute", "", "", "");
        		jsonResponse.addErrorMessage(msg);
        	}
        	else
        	{
        		//jsonResponse.put("Error", "No Method Type Found For The Request");
        		msg = new JSONMessage(0, "No Method Type Found For The Request", "Exception Occurred, service is having issue to execute", "", "", "");
        		jsonResponse.addErrorMessage(msg);
        	}
        	
        }
        
        
        } catch (Exception ex) {
        	StringWriter errors = new StringWriter();
    		ex.printStackTrace(new PrintWriter(errors));
    		System.out.println("Exception Caught==>"+errors);
    		
    		//jsonResponse.put("Error", errors.toString().substring(0, 200));
    		msg = new JSONMessage(0, errors.toString().substring(0, 200), "Exception Occurred, service is having issue to execute", "", "", "");
    		jsonResponse.addErrorMessage(msg);
    		
        }
        
        
        PluginResponseUtil.writeJSONResponse(request, response, jsonResponse, callbacks, "F110Services");

	}
	
	
	
	
	
	
	public JSONResponse getF110ServiceData(String SRVC_URL)
	{
		
		JSONResponse rsp = new JSONResponse();
		
		try
		{
			
			 URL obj = new URL(SRVC_URL);
		        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		        con.setRequestMethod("GET");

		        int responseCode = con.getResponseCode();
		      //  System.out.println("GET Response Code :: " + responseCode);
		        if (responseCode == HttpURLConnection.HTTP_OK) { // success
		            BufferedReader in = new BufferedReader(new InputStreamReader(
		                    con.getInputStream()));
		            String inputLine;
		            StringBuffer response = new StringBuffer();

		            while ((inputLine = in.readLine()) != null) {
		                response.append(inputLine);
		            }
		            
		            rsp.put("response", response);
		            in.close();

		        } else {
		            System.out.println("GET request not worked");
		        }
			
			
			
			
		}
		catch(Exception ex)
		{
			System.out.println("Exception ex-->"+ex);
		}
		
		
		return rsp;
	}
	
	
	public static void main()
	{
		F110Services f110Services= new F110Services();
		String url="http://10.1.54.157:9080/UKSCClients/VendBankDet?ICompanyCode=UTCL&IParam1=&IParam2=&IParam3=&IParam4=&IParam5=&IPlant=GW01&IVendor=0000805636";
		System.out.println("Response Received-->"+f110Services.getF110ServiceData(url));
		
	}
}
