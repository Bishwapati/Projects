package com.eb.utcl.F110Utils;

public class TestClass {

	public static void main()
	{
		F110Services f110Services= new F110Services();
		String url="http://10.1.54.157:9080/UKSCClients/VendBankDet?ICompanyCode=UTCL&IParam1=&IParam2=&IParam3=&IParam4=&IParam5=&IPlant=GW01&IVendor=0000805636";
		System.out.println("Response Received-->"+f110Services.getF110ServiceData(url));
		
	}
	
	
}
