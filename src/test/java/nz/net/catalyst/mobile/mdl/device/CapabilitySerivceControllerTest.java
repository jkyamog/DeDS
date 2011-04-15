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

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

@ContextConfiguration(locations={"classpath:mdl-context.xml"})
public class CapabilitySerivceControllerTest  extends AbstractJUnit4SpringContextTests {

   @Autowired
   private CapabilityServiceController csController;
   
   @Autowired
   private AnnotationMethodHandlerAdapter handlerAdapter;
   
   private ObjectMapper mapper = new ObjectMapper();

   @Test
   public void testRequiredParams() throws Exception {
      MockHttpServletRequest request = new MockHttpServletRequest("GET", "/services/v1/get_capabilities");
      MockHttpServletResponse response = new MockHttpServletResponse();
      
      HashMap<String, String> headers = new HashMap<String, String> ();
      headers.put("user-agent", "Mozilla/5.0 (SymbianOS/9.2; U; Series60/3.1 NokiaE71-1/100.07.57; Profile/MIDP-2.0 Configuration/CLDC-1.1 ) AppleWebKit/413 (KHTML, like Gecko) Safari/413");
      String headersStr = mapper.writeValueAsString(headers);

      request.addParameter("headers", headersStr);

      try {
         handlerAdapter.handle(request, response, csController);
         fail("exception expected");
      } catch (MissingServletRequestParameterException e) {
         assertEquals("Required String[] parameter 'capability' is not present", e.getMessage());
      }

      request = new MockHttpServletRequest("GET", "/services/v1/get_capabilities");
      request.addParameter("capability", "device_id");

      try {
         handlerAdapter.handle(request, response, csController);
         fail("exception expected");
      } catch (MissingServletRequestParameterException e) {
         assertEquals("Required String parameter 'headers' is not present", e.getMessage());
         csController.handleMissingParameters(e, response);
         
         assertEquals(400, response.getStatus());
      }
      
      
   }
   
   @Test
   public void testGetDeviceInfo() throws Exception {
      HashMap<String, String> headers = new HashMap<String, String> ();
      headers.put("user-agent", "Mozilla/5.0 (SymbianOS/9.2; U; Series60/3.1 NokiaE71-1/100.07.57; Profile/MIDP-2.0 Configuration/CLDC-1.1 ) AppleWebKit/413 (KHTML, like Gecko) Safari/413");
      String headersStr = mapper.writeValueAsString(headers);
           
      String content = csController.getCapabilities(headersStr, new String[] {"model_name"});
      Map<String, Object> capabilityMap = mapper.readValue(content, new TypeReference<Map<String, Object>> () {});
      assertEquals("E71", capabilityMap.get("model_name"));
      
   }

   @Test
   public void testParseError() throws Exception {
      HashMap<String, String> headers = new HashMap<String, String> ();
      headers.put("user-agent", "Mozilla/5.0 (SymbianOS/9.2; U; Series60/3.1 NokiaE71-1/100.07.57; Profile/MIDP-2.0 Configuration/CLDC-1.1 ) AppleWebKit/413 (KHTML, like Gecko) Safari/413");
      String headersStr = mapper.writeValueAsString(headers);
      
      try {
         csController.getCapabilities("corrupted string" + headersStr, new String[] {"model_name"});
         fail("exception expected");
      } catch (ParseException e) {
         MockHttpServletResponse response = new MockHttpServletResponse();
         csController.handleParseProblems(e, response);
         assertEquals(400, response.getStatus());
      }
   }
   
   @Test
   public void testMissingUserAgent() throws Exception {
      HashMap<String, String> headers = new HashMap<String, String> ();
      headers.put("user-agent-missing", "Mozilla/5.0 (SymbianOS/9.2; U; Series60/3.1 NokiaE71-1/100.07.57; Profile/MIDP-2.0 Configuration/CLDC-1.1 ) AppleWebKit/413 (KHTML, like Gecko) Safari/413");
      String headersStr = mapper.writeValueAsString(headers);
      
      try {
         csController.getCapabilities(headersStr, new String[] {"model_name"});
         fail("exception expected");
      } catch (IllegalArgumentException e) {
         assertEquals("required http header 'user-agent' is missing", e.getMessage());
         MockHttpServletResponse response = new MockHttpServletResponse();
         csController.handleParseProblems(e, response);
         assertEquals(400, response.getStatus());
      }
      
   }
   
   @Test
   public void testStatusInfo() throws Exception {
      MockHttpServletRequest request = new MockHttpServletRequest();
      Model model = new ExtendedModelMap();

      request.addHeader("user-agent", "Mozilla/5.0 (SymbianOS/9.2; U; Series60/3.1 NokiaE71-1/100.07.57; Profile/MIDP-2.0 Configuration/CLDC-1.1 ) AppleWebKit/413 (KHTML, like Gecko) Safari/413");
      
      csController.getStatusPage(request, model);
      
      Map<String, Object> modelMap = model.asMap();
      StatusInfo statusInfo = (StatusInfo) modelMap.get("statusinfo");
      assertTrue(StringUtils.isBlank(statusInfo.getLast_error()));
      
      String statusInfoStr = csController.getStatusInfo();
      assertEquals(statusInfoStr, mapper.writeValueAsString(statusInfo));
   }
   
   @Test
   public void testHandleUnknownError() {
      MockHttpServletResponse response = new MockHttpServletResponse();
      csController.handleUnknownException(new Exception(), response);
      assertEquals(500, response.getStatus());
   }


}
