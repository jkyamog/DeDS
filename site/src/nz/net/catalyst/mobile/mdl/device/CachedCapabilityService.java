package nz.net.catalyst.mobile.mdl.device;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Abstract class that implements the retrieval and caching of capability
 * and deviceInfo
 * 
 * @author jun yamog
 *
 */

public abstract class CachedCapabilityService implements CapabilityService {
   
   private static final Log logger = LogFactory.getLog(CachedCapabilityService.class);
   
   public final String getCapabilityForDevice(RequestInfo requestInfo,
         String capability) {

      String value = getCachedCapability(requestInfo, capability);
      if (StringUtils.isEmpty(value)) {
         value = getCapabilityForDeviceInternal(requestInfo, capability);
         logger.debug("caching capability = " + capability + " value = " + value);
         cacheCapability(requestInfo, capability, value);
      }
      
      return value;
   }

   
   public final DeviceInfo getDeviceInfo(RequestInfo requestInfo) {
      
      DeviceInfo deviceInfo = getCachedDeviceInfo(requestInfo);
      if (deviceInfo == null) {
         deviceInfo = getDeviceInfoInternal(requestInfo);
         if (logger.isDebugEnabled())
            logger.debug("caching deviceInfo " + deviceInfo.toStringForDebug());
         cacheDeviceInfo(requestInfo, deviceInfo);
      }
      
      return deviceInfo;
      
   }

   /**
    * retrieve the uncached capability
    * 
    * @see CapabilityService#getCapabilityForDevice(RequestInfo, String)
    * @param requestInfo
    * @param capability
    * @return
    */
   protected abstract String getCapabilityForDeviceInternal(RequestInfo requestInfo, String capability);
   /**
    * retrieve the uncached deviceInfo
    * 
    * @see CapabilityService#getDeviceInfo(RequestInfo)
    * @param requestInfo
    * @return
    */
   protected abstract DeviceInfo getDeviceInfoInternal(RequestInfo requestInfo);
   
   /**
    * subclass must implement on how to get the cached deviceInfo
    * @param requestInfo
    * @return
    */
   protected abstract DeviceInfo getCachedDeviceInfo(RequestInfo requestInfo);
   
   /**
    * subclass must implement on how to cache the deviceInfo
    * @param requestInfo
    * @param deviceInfo
    */
   protected abstract void cacheDeviceInfo(RequestInfo requestInfo, DeviceInfo deviceInfo);
   
   /**
    * subclass must implement on how to get the cached capability
    * @param requestInfo
    * @param capability
    * @return
    */
   protected abstract String getCachedCapability(RequestInfo requestInfo, String capability);
   
   /**
    * subclass must implement on how to cache the capability
    * @param requestInfo
    * @param capability
    * @param capabilityValue
    */
   protected abstract void cacheCapability(RequestInfo requestInfo, String capability, String capabilityValue);

}
