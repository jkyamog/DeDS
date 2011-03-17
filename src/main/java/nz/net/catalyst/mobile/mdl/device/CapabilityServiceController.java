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

import java.text.ParseException;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * a controller that gives our capability service interface as a simple webservice.
 * functions is what are on the urls.  it then gets the http parameters, uses
 * a serialize and deserializer service to turn to and from objects.
 * 
 * note: methods are lowercase and underscores and not camelcase, as these become
 * urls
 *  
 * @author jun yamog
 *
 */
public class CapabilityServiceController extends MultiActionController {

   public static final String CAPABILITY = "capability";
   public static final String HEADERS = "headers";

   private final static Log logger = LogFactory.getLog(CapabilityServiceController.class);
   
   private ObjectDeSerializer objectDeSerializer;
   private CapabilityService capabilityService;
   

   public void get_capabilities(HttpServletRequest request, HttpServletResponse response) throws Exception {
      String headersStr = request.getParameter(HEADERS);
      String[] capabilities = request.getParameterValues(CAPABILITY);
      
      if (StringUtils.isEmpty(headersStr)) {
         response.getWriter().print("ERROR: Missing required parameter '" + HEADERS + "'");
         return;
      }
      if (capabilities == null || capabilities.length == 0) {
         response.getWriter().print("ERROR: Missing required parameter '" + CAPABILITY + "'");
         return;
      }

      try {
         Map<String, String> headers = objectDeSerializer.deserialize(IOUtils.toInputStream(headersStr));
   
         Map<String, Object> capabilitiesMap = capabilityService.getCapabilitiesForDevice(new RequestInfo(headers), Arrays.asList(capabilities));
         objectDeSerializer.serialize(capabilitiesMap, response.getOutputStream());
      } catch (ParseException e) {
         response.getWriter().print("ERROR: Parsing problem: " + e.getMessage());
      } catch (IllegalArgumentException e) {
         response.getWriter().print("ERROR: Parsing problem: " + e.getMessage());
      } catch (Exception e) {
         logger.error("Unknown problem w/ service", e);
         throw e;
      }
      
   }
   
   public void get_statusinfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
      StatusInfo status = capabilityService.getStatusInfo();
       
      try {
         objectDeSerializer.serialize(status, response.getOutputStream());
      } catch (Exception e) {
         logger.error("Unknown problem w/ service", e);
         throw e;
      }
   }
   
   public ModelAndView status_page(HttpServletRequest request, HttpServletResponse response) {
      ModelAndView mav = new ModelAndView();

      StatusInfo status = capabilityService.getStatusInfo();
      mav.addObject("statusinfo", status);
      
      RequestInfo requestInfo = RequestInfo.getRequestInfo(request);
      mav.addObject("useragent", requestInfo.getUserAgent());
      
      Map<String, Object> capabilitiesMap = capabilityService.getCapabilitiesForDevice(requestInfo,
            Arrays.asList(new String[] {"brand_name", 
                                       "model_name", 
                                       "device_id", 
                                       "max_image_width",
                                       "resolution_width",
                                       "resolution_height",
                                       "xhtml_display_accesskey",
                                       "xhtml_make_phone_call_string",
                                       "xhtml_support_level",
                                       "pointing_method",
                                       "mobile_browser"}));
      
      StringBuilder deviceStr = new StringBuilder();
      for (Entry<String, Object> capability: capabilitiesMap.entrySet()) {
         deviceStr.append(capability.getKey() + "=" + capability.getValue() + "<br>");
      }
      
      mav.addObject("device", deviceStr);
      
      mav.setViewName("status-page");
      return mav;
   }


   public void setObjectDeSerializer(ObjectDeSerializer objectDeSerializer) {
      this.objectDeSerializer = objectDeSerializer;
   }

   public void setCapabilityService(CapabilityService capablityService) {
      this.capabilityService = capablityService;
   }


}