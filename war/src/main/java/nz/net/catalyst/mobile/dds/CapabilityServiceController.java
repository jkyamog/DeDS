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

package nz.net.catalyst.mobile.dds;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * a controller that gives our capability service interface as a simple webservice.
 * functions is what are on the urls.  it then gets the http parameters, uses json
 * a serialize and deserializer service to turn to and from objects.
 * 
 * note: not yet using spring's response body to transform objects to json
 * explicitly writing the json to the response as some client will not properly set
 * the header Accept application/json
 * 
 * @author jun yamog
 *
 */

@Controller
@RequestMapping("/services/v1")
public class CapabilityServiceController {

   public static final String CAPABILITY = "capability";
   public static final String HEADERS = "headers";

   private final Logger logger = LoggerFactory.getLogger(this.getClass());
   
   @Autowired
   private JsonService jsonService;
   @Autowired
   private CapabilityService capabilityService;
   
   @RequestMapping("get_capabilities")
   public @ResponseBody String getCapabilities(@RequestParam(value=HEADERS) String headersStr,
         @RequestParam(value=CAPABILITY) String[] capabilities) throws IOException, ParseException {
      
      Map<String, String> headers = jsonService.deserialize(headersStr);

      Map<String, Object> capabilitiesMap = capabilityService.getCapabilitiesForDevice(new RequestInfo(headers), Arrays.asList(capabilities));
      return jsonService.serialize(capabilitiesMap);
      
   }
   
   @RequestMapping("get_statusinfo")
   public @ResponseBody String getStatusInfo() throws Exception {
      StatusInfo status = capabilityService.getStatusInfo();
       
         return jsonService.serialize(status);
   }
   
   @RequestMapping({"", "/", "status_page"})
   public String getStatusPage(HttpServletRequest request, Model model) {

      StatusInfo status = capabilityService.getStatusInfo();
      model.addAttribute("statusinfo", status);
      
      RequestInfo requestInfo = RequestInfo.getRequestInfo(request);
      model.addAttribute("useragent", requestInfo.getUserAgent());
      
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
      
      model.addAttribute("device", deviceStr);
      
      return "status-page";
   }

   @ExceptionHandler(MissingServletRequestParameterException.class)
   public ModelAndView handleMissingParameters(MissingServletRequestParameterException ex, HttpServletResponse response) {
      logger.warn("parse problems on input data", ex);

      ModelAndView mav = new ModelAndView();
      mav.addObject("error_message", ex.getMessage());
      
      mav.setViewName("error");
      response.setStatus(400);
      
      return mav;
   }
   
   @ExceptionHandler({ParseException.class, IllegalArgumentException.class, ClassCastException.class})
   public ModelAndView handleParseProblems(Exception ex, HttpServletResponse response) {
      logger.warn("parse problems on input data", ex);
      
      ModelAndView mav = new ModelAndView();
      mav.addObject("error_message", "parse problems on input data, Details: " + ex.getMessage());
      
      mav.setViewName("error");
      response.setStatus(400);
      
      return mav;
   }

   @ExceptionHandler({Exception.class})
   public ModelAndView handleUnknownException(Exception ex, HttpServletResponse response) {
      logger.error("unknown exception", ex);
      
      ModelAndView mav = new ModelAndView();
      mav.addObject("error_message", "unknown exception, Details: " + ex.getMessage());
      
      mav.setViewName("error");
      response.setStatus(500);
      
      return mav;
   }

}
