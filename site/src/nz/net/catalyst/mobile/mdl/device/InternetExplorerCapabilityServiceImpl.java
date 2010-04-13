package nz.net.catalyst.mobile.mdl.device;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Service that detects Internet Explorer based devices
 *  
 * @author jun yamog
 *
 */
public class InternetExplorerCapabilityServiceImpl implements CapabilityService {
   
   private static final Log logger = LogFactory.getLog(InternetExplorerCapabilityServiceImpl.class);
   
   private static final String UA_PIXELS = "UA-pixels";
   private static final String MAX_IMAGE_WIDTH = "max_image_width";

   public String getCapabilityForDevice(RequestInfo requestInfo,
         String capability) {
      if (MAX_IMAGE_WIDTH.equals(capability))
         return getMaxImageWidth(requestInfo);
      else
         return "";
   }

   public DeviceInfo getDeviceInfo(RequestInfo requestInfo) {
      DeviceInfo deviceInfo = new DeviceInfo();
      
      String maxImageWidthStr = getMaxImageWidth(requestInfo);
      if (!StringUtils.isEmpty(maxImageWidthStr)) {
            int maxImageWidth = Integer.valueOf(maxImageWidthStr);
            logger.debug("setting maxImageWidth to " + maxImageWidth);
            deviceInfo.setMax_image_width(maxImageWidth);
      }

      return deviceInfo;
   }
   
   @Override
   public StatusInfo getStatusInfo() {
      StatusInfo statusInfo = new StatusInfo(0,"");
      return statusInfo;
   }
   
   private String getMaxImageWidth(RequestInfo requestInfo) {
      Map<String, String> headers = requestInfo.getHeaders();
      if (headers.containsKey(UA_PIXELS)) {
         // get the max image width from the UA-pixels header
         String uaPixels = headers.get(UA_PIXELS);
         if (!StringUtils.isEmpty(uaPixels)) {
            String[] dimensions = StringUtils.split(uaPixels, 'x');
            return dimensions[0];
         }
      }
      return null;
   }
   
   

}
