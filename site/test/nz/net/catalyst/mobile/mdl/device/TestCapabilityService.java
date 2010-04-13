package nz.net.catalyst.mobile.mdl.device;

import java.io.IOException;
import java.util.HashMap;

import junit.framework.TestCase;

import nz.net.catalyst.mobile.mdl.device.CapabilityService;
import nz.net.catalyst.mobile.mdl.device.RequestInfo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class TestCapabilityService extends TestCase {
    private ApplicationContext ac;

   public void setUp() throws IOException {
       ac = new FileSystemXmlApplicationContext("site/test/mdl-context.xml");

   }

   public void testGetPrefix() {

      String userAgent = "NokiaN90-1/2.0523.1.4 Series60/2.8 Profile/MIDP-2.0 Configuration/CLDC-1.1";
      CapabilityService cs = (CapabilityService) ac.getBean("capabilityService");
      // Expect true
      HashMap<String, String> headers = new HashMap<String, String> ();
      headers.put("user-agent", userAgent);
      RequestInfo requestInfo = new RequestInfo(headers);
      assertEquals(true, cs.getDeviceInfo(requestInfo).getXhtml_make_phone_call_string().equals("wtai://wp/mc;"));
   }
   
   public void testInternetExplorer() {
      String userAgent = "Treo850/v0100 Mozilla/4.0 (compatible; MSIE 6.0; Windows CE; IEMobile 7.11)";
      String uaPixels = "320x320";
      CapabilityService cs = (CapabilityService) ac.getBean("capabilityService");
      HashMap<String, String> headers = new HashMap<String, String> ();
      headers.put("user-agent", userAgent);
      headers.put("UA-pixels", uaPixels);
      RequestInfo requestInfo = new RequestInfo(headers);
      assertEquals(320, cs.getDeviceInfo(requestInfo).getMax_image_width());
      
      cs = (CapabilityService) ac.getBean("wurflCapabilityService");
      assertEquals(300, cs.getDeviceInfo(requestInfo).getMax_image_width());
      
   }
   
   public void testOperaMini() {

      String userAgent = "Opera/9.60 (J2ME/MIDP; Opera Mini/4.2.13337/608; U; en) Presto/2.2.0";
      String operaMiniUA = "Mozilla/5.0 (SymbianOS/9.2; U; Series60/3.1 NokiaN95_8GB/30.0.018; Profile/MIDP-2.0 Configuration/CLDC-1.1 ) AppleWebKit/413 (KHTML, like Gecko) Safari/413";
      CapabilityService cs = (CapabilityService) ac.getBean("capabilityService");

      HashMap<String, String> headers = new HashMap<String, String> ();
      headers.put("user-agent", userAgent);
      headers.put("X-OperaMini-Phone-UA", operaMiniUA);
      RequestInfo requestInfo = new RequestInfo(headers);
      assertEquals(232, cs.getDeviceInfo(requestInfo).getMax_image_width());

      cs = (CapabilityService) ac.getBean("wurflCapabilityService");
      assertEquals(240, cs.getDeviceInfo(requestInfo).getMax_image_width());

   }
}
