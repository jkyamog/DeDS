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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

import nz.net.catalyst.mobile.mdl.device.CapabilityService;
import nz.net.catalyst.mobile.mdl.device.RequestInfo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations={"classpath:mdl-context.xml"})
public class CapabilityServiceTest extends AbstractJUnit4SpringContextTests {
   
   @Autowired
   CapabilityService cs;

   @Test
   public void testGetPrefix() {

      String userAgent = "NokiaN90-1/2.0523.1.4 Series60/2.8 Profile/MIDP-2.0 Configuration/CLDC-1.1";
      // Expect true
      HashMap<String, String> headers = new HashMap<String, String> ();
      headers.put("user-agent", userAgent);
      RequestInfo requestInfo = new RequestInfo(headers);
      Map<String, Object> capabilitiesMap = cs.getCapabilitiesForDevice(requestInfo, Arrays.asList(new String[] {"xhtml_make_phone_call_string"}));
      String result = (String) capabilitiesMap.get("xhtml_make_phone_call_string");
      assertEquals("wtai://wp/mc;", result);
   }
   
}
