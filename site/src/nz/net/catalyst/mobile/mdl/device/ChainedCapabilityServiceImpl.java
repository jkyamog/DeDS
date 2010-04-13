package nz.net.catalyst.mobile.mdl.device;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * This is a simple implementation of a CapabilityService that invokes other
 * implementation of CapabilityService.  There are no rules, but rather the
 * just a priority where the later CapablityService on capablityServices gets
 * higher priority.
 * 
 * @author jun yamog
 *
 */
public class ChainedCapabilityServiceImpl extends CachedCapabilityService {

   private static final Log logger = LogFactory.getLog(ChainedCapabilityServiceImpl.class);

   public static final String DEVICE_INFO = "device-info";
   public static final String CAPABILITY_PREFIX = "capability-";
   
   private ArrayList<CapabilityService> capabilityServices;
   private boolean cacheOn = true;

   public String getCapabilityForDeviceInternal(RequestInfo requestInfo,
         String capability) {
      String value = "";
      for (CapabilityService cs:capabilityServices) {
         String result = cs.getCapabilityForDevice(requestInfo, capability);
         if (!StringUtils.isEmpty(result)) {
            value = result;
            logger.debug(cs.getClass().getSimpleName() + " result = " + result);
         }
      }
      return value;
   }

   protected String getCachedCapability(RequestInfo requestInfo,
         String capability) {       
      if (cacheOn)
         return (String) getRequestAttribute(CAPABILITY_PREFIX + capability);
      else
         return null;
   }

   
   protected void cacheCapability(RequestInfo requestInfo, String capability,
         String capabilityValue) {
      setRequestAttribute(CAPABILITY_PREFIX + capability, capabilityValue);
   }


   public DeviceInfo getDeviceInfoInternal(RequestInfo requestInfo) {
      DeviceInfo deviceInfo = new DeviceInfo();
      for (CapabilityService cs:capabilityServices) {
         logger.debug("using CapabilityService - " + cs.getClass().getSimpleName());
         deviceInfo = mergeDeviceInfo(deviceInfo, cs.getDeviceInfo(requestInfo));
      }
      
      return deviceInfo;
   }
   
   @Override
   public StatusInfo getStatusInfo() {
      StatusInfo statusInfo = new StatusInfo(0,"");
      for (CapabilityService cs:capabilityServices) {
         logger.debug("using CapabilityService - " + cs.getClass().getSimpleName());
         statusInfo = mergeStatusInfo(statusInfo, cs.getStatusInfo());
           }
   
      return statusInfo;
   }

   
   protected DeviceInfo getCachedDeviceInfo(RequestInfo requestInfo) {
      if (cacheOn)
         return (DeviceInfo) getRequestAttribute(DEVICE_INFO);
      else
         return null;
   }

   protected void cacheDeviceInfo(RequestInfo requestInfo, DeviceInfo deviceInfo) {
      setRequestAttribute(DEVICE_INFO, deviceInfo);
   }
   
   
   /**
    * this would merge 2 deviceInfo objects, its not ideal however this is the simplest
    * 
    * @param di1
    * @param di2
    * @return
    */
   private DeviceInfo mergeDeviceInfo(DeviceInfo di1, DeviceInfo di2) {
      if (logger.isDebugEnabled()) { 
         logger.debug("merging di1 = " + di1.toStringForDebug());
         logger.debug("merging di2 = " + di2.toStringForDebug());
      }
      
      if (StringUtils.isNotEmpty(di2.getBrand_name())) di1.setBrand_name(di2.getBrand_name());
      if (StringUtils.isNotEmpty(di2.getModel_name())) di1.setModel_name(di2.getModel_name());
      if (StringUtils.isNotEmpty(di2.getUser_agent())) di1.setUser_agent(di2.getUser_agent());
      if (StringUtils.isNotEmpty(di2.getDevice_id())) di1.setDevice_id(di2.getDevice_id());
      if (di2.getMax_image_width() > 0) di1.setMax_image_width(di2.getMax_image_width());
      di1.setXhtml_display_accesskey(di1.isXhtml_display_accesskey() || di2.isXhtml_display_accesskey());
      if (!StringUtils.isEmpty(di2.getXhtml_make_phone_call_string())) 
         di1.setXhtml_make_phone_call_string(di2.getXhtml_make_phone_call_string());
      if (di2.getXhtml_support_level() > di1.getXhtml_support_level()) di1.setXhtml_support_level(di2.getXhtml_support_level());
      if (di2.getLast_modified() > di1.getLast_modified()) di1.setLast_modified(di2.getLast_modified());
      
      if (logger.isDebugEnabled())
         logger.debug("merged result = " + di1.toStringForDebug());
      
      return di1;
   }
   
   /**
    * merge to 2 status info object into 1 status info
    * @param si1
    * @param si2
    * @return
    */
   private StatusInfo mergeStatusInfo(StatusInfo si1, StatusInfo si2) {
      
      long lastModified = si1.getLast_modified();
      String lastError = si1.getLast_error();
      if (si2.getLast_modified() > si1.getLast_modified()) lastModified = si2.getLast_modified();
      if (StringUtils.isNotBlank(si2.getLast_error())) lastError = lastError + " " + si2.getLast_error();
      return new StatusInfo(lastModified, lastError);
   }
   
   private Object getRequestAttribute(String key) {
      ServletRequestAttributes sa = 
         (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
      if (sa != null) {
         HttpServletRequest request = sa.getRequest();
         return request.getAttribute(key);
      }
      return null;
   }
   
   private void setRequestAttribute(String key, Object object) {
      ServletRequestAttributes sa = 
         (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
      if (sa != null) {
         HttpServletRequest request = sa.getRequest();
         request.setAttribute(key, object);
      }
      
   }

   public void setCapabilityServices(ArrayList<CapabilityService> capabilityServices) {
      this.capabilityServices = capabilityServices;
   }

   public ArrayList<CapabilityService> getCapabilityServices() {
      return capabilityServices;
   }
   
   public void setCacheOn(boolean cacheOn) {
      this.cacheOn = cacheOn;
   }
   
   public boolean isCacheOn() {
      return this.cacheOn;
   }

}
