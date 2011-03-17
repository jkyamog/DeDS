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

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations={"classpath:mdl-context.xml"})
public class TestCapabilitySerivceController  extends AbstractJUnit4SpringContextTests {

   @Autowired
   private CapabilityServiceController csController;
   
   private ObjectMapper mapper = new ObjectMapper();

   @Test
   public void testRequiredParams() throws Exception {
      String content = csController.getCpabilities(null, null);
      assertEquals("ERROR: Missing required parameter 'headers'", content);
      

      content = csController.getCpabilities("some headers", null);
      assertEquals("ERROR: Missing required parameter 'capability'", content);
      
   }
   
   @Test
   public void testGetDeviceInfo() throws Exception {
      HashMap<String, String> headers = new HashMap<String, String> ();
      headers.put("user-agent", "Mozilla/5.0 (SymbianOS/9.2; U; Series60/3.1 NokiaE71-1/100.07.57; Profile/MIDP-2.0 Configuration/CLDC-1.1 ) AppleWebKit/413 (KHTML, like Gecko) Safari/413");
      String headersStr = mapper.writeValueAsString(headers);
           
      String content = csController.getCpabilities(headersStr, new String[] {"model_name"});
      Map<String, Object> capabilityMap = mapper.readValue(content, new TypeReference<Map<String, Object>> () {});
      assertEquals("E71", capabilityMap.get("model_name"));
      
   }

   @Test
   public void testParseError() throws Exception {
      HashMap<String, String> headers = new HashMap<String, String> ();
      headers.put("user-agent", "Mozilla/5.0 (SymbianOS/9.2; U; Series60/3.1 NokiaE71-1/100.07.57; Profile/MIDP-2.0 Configuration/CLDC-1.1 ) AppleWebKit/413 (KHTML, like Gecko) Safari/413");
      String headersStr = mapper.writeValueAsString(headers);
      
      String content = csController.getCpabilities("corrupted string" + headersStr, new String[] {"model_name"});
      assertTrue(content.contains("ERROR: Parsing problem:"));
      
   }
   
   @Test
   public void testMissingUserAgent() throws Exception {
      HashMap<String, String> headers = new HashMap<String, String> ();
      headers.put("user-agent-missing", "Mozilla/5.0 (SymbianOS/9.2; U; Series60/3.1 NokiaE71-1/100.07.57; Profile/MIDP-2.0 Configuration/CLDC-1.1 ) AppleWebKit/413 (KHTML, like Gecko) Safari/413");
      String headersStr = mapper.writeValueAsString(headers);
      
      String content = csController.getCpabilities(headersStr, new String[] {"model_name"});
      assertTrue(content.contains("ERROR: Parsing problem: required http header 'user-agent' is missing"));
      
   }


}
