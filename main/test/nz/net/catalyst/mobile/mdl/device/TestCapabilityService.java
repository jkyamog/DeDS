/*
 * Copyright (C) 2010  Catalyst IT Limited
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

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
      CapabilityService cs = (CapabilityService) ac.getBean("wurflCapabilityService");
      // Expect true
      HashMap<String, String> headers = new HashMap<String, String> ();
      headers.put("user-agent", userAgent);
      RequestInfo requestInfo = new RequestInfo(headers);
      assertEquals(true, cs.getDeviceInfo(requestInfo).getXhtml_make_phone_call_string().equals("wtai://wp/mc;"));
   }
   
   public void testInternetExplorer() {
      String userAgent = "Treo850/v0100 Mozilla/4.0 (compatible; MSIE 6.0; Windows CE; IEMobile 7.11)";
      String uaPixels = "320x320";
      CapabilityService cs = (CapabilityService) ac.getBean("internetExplorerCapabilityService");
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
      CapabilityService cs = (CapabilityService) ac.getBean("operaMiniCapabilityService");

      HashMap<String, String> headers = new HashMap<String, String> ();
      headers.put("user-agent", userAgent);
      headers.put("X-OperaMini-Phone-UA", operaMiniUA);
      RequestInfo requestInfo = new RequestInfo(headers);
      assertEquals(234, cs.getDeviceInfo(requestInfo).getMax_image_width());

      cs = (CapabilityService) ac.getBean("wurflCapabilityService");
      assertEquals(165, cs.getDeviceInfo(requestInfo).getMax_image_width());

   }
}
