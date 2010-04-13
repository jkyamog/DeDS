package nz.net.catalyst.mobile.mdl.device;

import java.text.ParseException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
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
   
   private CapabiltyServiceDeSerializer capabilityServiceDeSerializer;
   private CapabilityService capabilityService;
   

   public void get_deviceinfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
      
      String headersStr = request.getParameter(HEADERS);
      
      if (StringUtils.isEmpty(headersStr)) {
         response.getWriter().print("ERROR: Missing required parameter '" + HEADERS + "'");
         return;
      }
      logger.debug("headers = " + headersStr);
      
      try {
         Map<String, String>headers = 
            capabilityServiceDeSerializer.deserializeHeaders(IOUtils.toInputStream(headersStr));
   
         DeviceInfo deviceInfo = capabilityService.getDeviceInfo(new RequestInfo(headers));
         capabilityServiceDeSerializer.serializeDeviceInfo(deviceInfo, response.getOutputStream());
   
         if (logger.isDebugEnabled())
            ReflectionToStringBuilder.toString(deviceInfo);
         
      } catch (ParseException e) {
         response.getWriter().print("ERROR: Parsing problem: " + e.getMessage());
      } catch (IllegalArgumentException e) {
         response.getWriter().print("ERROR: Parsing problem: " + e.getMessage());
      } catch (Exception e) {
         logger.error("Unknown problem w/ service", e);
         throw e;
      }

   }
   
   public void get_capability(HttpServletRequest request, HttpServletResponse response) throws Exception {
      
      String headersStr = request.getParameter(HEADERS);
      String capability = request.getParameter(CAPABILITY);

      if (StringUtils.isEmpty(headersStr)) {
         response.getWriter().print("ERROR: Missing required parameter '" + HEADERS + "'");
         return;
      }
      if (StringUtils.isEmpty(capability)) {
         response.getWriter().print("ERROR: Missing required parameter '" + CAPABILITY + "'");
         return;
      }
      logger.debug("capability = " + capability + " headers = " + headersStr);

      Map<String, String> headers;
      try {
         headers = capabilityServiceDeSerializer.deserializeHeaders(IOUtils.toInputStream(headersStr));
         
         String capabilityValue = capabilityService.getCapabilityForDevice(new RequestInfo(headers), capability);
         capabilityServiceDeSerializer.serializeCapability(capabilityValue, response.getOutputStream());
         
         logger.debug("capabilityValue = " + capabilityValue);
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
       
      // removing last error, as we not yet allowing 3rd party to view this
      StatusInfo noErrorStatus = new StatusInfo(status.getLast_modified(), ""); 
      
      try {
         capabilityServiceDeSerializer.serializeStatusInfo(noErrorStatus, response.getOutputStream());
      } catch (Exception e) {
         logger.error("Unknown problem w/ service", e);
         throw e;
      }
   }
   
   public ModelAndView status_page(HttpServletRequest request, HttpServletResponse response) {
      ModelAndView mav = new ModelAndView();

      StatusInfo status = capabilityService.getStatusInfo();
      mav.addObject("statusinfo", status);
      
      DeviceInfo deviceInfo = capabilityService.getDeviceInfo(RequestInfo.getRequestInfo(request));
      String deviceStr = StringUtils.substringBetween(deviceInfo.toStringForDebug(), "[", "]");
      mav.addObject("device", deviceStr);
      
      mav.setViewName("status-page");
      return mav;
   }


   public void setCapabilityServiceDeSerializer(CapabiltyServiceDeSerializer capabilityServiceDeSerializer) {
      this.capabilityServiceDeSerializer = capabilityServiceDeSerializer;
   }

   public void setCapabilityService(CapabilityService capablityService) {
      this.capabilityService = capablityService;
   }


}
