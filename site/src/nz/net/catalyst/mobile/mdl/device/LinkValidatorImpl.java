package nz.net.catalyst.mobile.mdl.device;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class to filter on capabilities
 * 
 */
public class LinkValidatorImpl implements LinkValidator {

   private final static Log logger = LogFactory.getLog(LinkValidatorImpl.class);

   private CapabilityService capabilityService;

   /*
    * (non-Javadoc)
    * 
    * @see nz.net.catalyst.mobile.mdl.api.LinkValidator#isCapable(java.lang.String)
    */
   public boolean isCapable(RequestInfo requestInfo, HashMap<String, String> capabilityProperties) throws IllegalArgumentException {
      
      boolean isCapable = true;

      if(capabilityProperties == null){
         return isCapable;
      }
      checkArguments(requestInfo.getUserAgent());
      Iterator<String> i = capabilityProperties.keySet().iterator();
      
      String device_id = capabilityService.getDeviceInfo(requestInfo).getDevice_id();
      logger.debug("Checking capabilities for DEVICE: " + device_id);

      while (i.hasNext() && isCapable) {
         String key = i.next();
         String capability = capabilityProperties.get(key);
         logger.debug("Checking capability: " + key + " = " + capability);

         String capability_value = capabilityService.getCapabilityForDevice(requestInfo, key);
         logger.debug("Capability value: " + capability_value);

         isCapable = capability_value.equals(capability);
      }
      
      return isCapable;
   }

   public boolean canBeCapable(RequestInfo requestInfo, HashMap<String, String> capabilityProperties) throws IllegalArgumentException {
      boolean isCapable = false;

      if(capabilityProperties == null){
         return true;
      }
      checkArguments(requestInfo.getUserAgent());
      Iterator<String> i = capabilityProperties.keySet().iterator();
      
      String device_id = capabilityService.getDeviceInfo(requestInfo).getDevice_id();
      logger.debug("Checking capabilities for DEVICE: " + device_id);

      while (i.hasNext()) {
         String key = i.next();
         String capability = capabilityProperties.get(key);
         logger.debug("Checking capability: " + key + " = " + capability);

         String capability_value = capabilityService.getCapabilityForDevice(requestInfo, key);
         logger.debug("Capability value: " + capability_value);

         isCapable = capability_value.equals(capability) || isCapable;
      }
      
      return isCapable;
   }
   
   /**
    * @param userAgent
    * @throws IllegalArgumentException
    */
   private static void checkArguments(String userAgent) throws IllegalArgumentException {
      if (userAgent == null)
         throw new IllegalArgumentException(
               "invalid userAgent, must not be null");
   }

   public void setCapabilityService(CapabilityService capabilityService) {
      this.capabilityService = capabilityService;
   }

}
