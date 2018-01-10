package com.baihui.difu.test;

import java.io.File;
import java.io.PrintWriter;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

public class Test {

	public static void main(String a[])
	{	
		try	
		{
			//----------------------------Fetch Auth Token ----------------------

			String mytext=java.net.URLEncoder.encode("北京百会技术公司1",   "utf-8");   
			
			//String targetURL = "https://crm.baihui.com/crm/private/xml/Vendors/getSearchRecordsByPDC?authtoken=d51a161067f8935329ca2b0ca078cf70&scope=crmapi&searchColumn=vendorname&searchValue=%E5%8C%97%E4%BA%AC%E7%99%BE%E4%BC%9A%E6%8A%80%E6%9C%AF%E5%85%AC%E5%8F%B81"; 
			String targetURL ="https://crm.baihui.com/crm/private/xml/Vendors/getSearchRecordsByPDC?authtoken=d51a161067f8935329ca2b0ca078cf70&scope=crmapi&searchColumn=vendorname&searchValue="+mytext;
			//String url=URLEncoder.encode(targetURL, "utf-8").replace("*","*").replace("~", "~").replace("+"," ");
			System.out.println(targetURL);
			System.out.println("11111");
			//String paramname = "content";
			PostMethod post = new PostMethod(targetURL);
			//post.setParameter("scope",scope);
			//post.setParameter("fromIndex",fromIndex);
			//post.setParameter("toIndex",toIndex);
			//post.setParameter("newFormat",newFormat);
		//	post.setParameter("selectColumns",selectColumns);
			//post.setParameter("searchColumn",searchColumn);
			//post.setParameter("searchValue",searchValue);
			HttpClient httpclient = new HttpClient();
			PrintWriter myout = null;

			// Execute http request
			try 
			{
				long t1 = System.currentTimeMillis();
				int result = httpclient.executeMethod(post);
				System.out.println("HTTP Response status code: " + result);
				System.out.println(">> Time taken " + (System.currentTimeMillis() - t1));

				// writing the response to a file
				myout = new PrintWriter(new File("response.xml"));
				myout.print(post.getResponseBodyAsString());

				/*-----------------------Get response as a string ----------------*/
				String postResp = post.getResponseBodyAsString();
				System.out.println("postResp=======>"+postResp);
				/* ---------------------Get ----------------------------*/
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}	
			finally 
			{
				myout.close();
				post.releaseConnection();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}	
	}
}
