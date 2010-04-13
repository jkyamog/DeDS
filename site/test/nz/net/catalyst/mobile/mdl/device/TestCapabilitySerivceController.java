package nz.net.catalyst.mobile.mdl.device;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import junit.framework.TestCase;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class TestCapabilitySerivceController  extends TestCase {

   private MockHttpServletRequest request;
   private MockHttpServletResponse response;
   private CapabilityServiceController csController;
   
   private ObjectMapper mapper = new ObjectMapper();


   public void setUp() throws IOException {
      request = new MockHttpServletRequest();
      response = new MockHttpServletResponse();
      ApplicationContext ac = new FileSystemXmlApplicationContext("site/test/mdl-context.xml");
      
      csController = (CapabilityServiceController) ac.getBean("capabilityServiceController");
   }
   
   public void testRequiredParams() throws Exception {
      csController.get_deviceinfo(request, response);
      String content = response.getContentAsString();
      assertEquals("ERROR: Missing required parameter 'headers'", content);
      
      request = new MockHttpServletRequest();
      response = new MockHttpServletResponse();
      csController.get_capability(request, response);
      content = response.getContentAsString();
      assertEquals("ERROR: Missing required parameter 'headers'", content);
      

      request = new MockHttpServletRequest();
      response = new MockHttpServletResponse();
      request.setParameter("headers", "some headers");
      csController.get_capability(request, response);
      content = response.getContentAsString();
      assertEquals("ERROR: Missing required parameter 'capability'", content);
      
   }
   
   public void testGetDeviceInfo() throws Exception {
      HashMap<String, String> headers = new HashMap<String, String> ();
      headers.put("user-agent", "Mozilla/5.0 (SymbianOS/9.2; U; Series60/3.1 NokiaE71-1/100.07.57; Profile/MIDP-2.0 Configuration/CLDC-1.1 ) AppleWebKit/413 (KHTML, like Gecko) Safari/413");
      ByteArrayOutputStream outStream = new ByteArrayOutputStream();
      mapper.writeValue(outStream, headers);
      request.setParameter("headers", outStream.toString());
           
      csController.get_deviceinfo(request, response);
      String content = response.getContentAsString();
      DeviceInfo deviceInfo = mapper.readValue(content, DeviceInfo.class);
      assertEquals("E71", deviceInfo.getModel_name());
      
   }

   public void testParseError() throws Exception {
      HashMap<String, String> headers = new HashMap<String, String> ();
      headers.put("user-agent", "Mozilla/5.0 (SymbianOS/9.2; U; Series60/3.1 NokiaE71-1/100.07.57; Profile/MIDP-2.0 Configuration/CLDC-1.1 ) AppleWebKit/413 (KHTML, like Gecko) Safari/413");
      ByteArrayOutputStream outStream = new ByteArrayOutputStream();
      mapper.writeValue(outStream, headers);
      request.setParameter("headers", "corrupted string" + outStream.toString());
           
      csController.get_deviceinfo(request, response);
      String content = response.getContentAsString();
      assertTrue(content.contains("ERROR: Parsing problem:"));
      
   }
   
   public void testMissingUserAgent() throws Exception {
      HashMap<String, String> headers = new HashMap<String, String> ();
      headers.put("user-agent-missing", "Mozilla/5.0 (SymbianOS/9.2; U; Series60/3.1 NokiaE71-1/100.07.57; Profile/MIDP-2.0 Configuration/CLDC-1.1 ) AppleWebKit/413 (KHTML, like Gecko) Safari/413");
      ByteArrayOutputStream outStream = new ByteArrayOutputStream();
      mapper.writeValue(outStream, headers);
      request.setParameter("headers", outStream.toString());
           
      csController.get_deviceinfo(request, response);
      String content = response.getContentAsString();
      assertTrue(content.contains("ERROR: Parsing problem: required http header 'user-agent' is missing"));
      
   }


}
