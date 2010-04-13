package nz.net.catalyst.mobile.mdl.device;

import java.io.File;

import org.apache.commons.jci.listeners.FileChangeListener;
import org.apache.commons.jci.monitor.FilesystemAlterationMonitor;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sourceforge.wurfl.core.CapabilityNotDefinedException;
import net.sourceforge.wurfl.core.CustomWURFLHolder;
import net.sourceforge.wurfl.core.Device;
import net.sourceforge.wurfl.core.WURFLHolder;

public class WurflCapabilityServiceImpl implements CapabilityService {
   
   private final static Log logger = LogFactory.getLog(WurflCapabilityServiceImpl.class);

   private WURFLHolder wurflHolder;
   
   private FilesystemAlterationMonitor fam;
   private String wurflPath;
   private String wurflPatchPath;
   private StatusInfo statusInfo;

   public WurflCapabilityServiceImpl(String wurflPath) {
      this.wurflPath = wurflPath;
      
      String wurflDir = StringUtils.removeEnd(wurflPath, "wurfl.xml");
      this.wurflPatchPath = wurflDir + "wurfl_patch.xml";
      
      wurflHolder = new CustomWURFLHolder(this.wurflPath, new String[] {this.wurflPatchPath});
      
      watchWurflFile(this.wurflPath);
      this.statusInfo = getNewStatusInfo("");
   }
   
   @Override
   public String getCapabilityForDevice(RequestInfo requestInfo,
         String capability) {
      try {
         Device device = wurflHolder.getWURFLManager().getDeviceForRequest(requestInfo.getUserAgent());
         String capabilityValue = device.getCapability(capability);
         
         logger.debug(" Capability: " + capability + 
               " Capability value: " + capabilityValue);
   
         return capabilityValue;
      } catch (CapabilityNotDefinedException e) {
         logger.warn(e);
         return "";
      }
   }

   @Override
   public DeviceInfo getDeviceInfo(RequestInfo requestInfo) {
      Device device = wurflHolder.getWURFLManager().getDeviceForRequest(requestInfo.getUserAgent());
      DeviceInfo deviceInfo = new DeviceInfo();
      try {
         deviceInfo.setBrand_name(device.getCapability("brand_name"));
         deviceInfo.setModel_name(device.getCapability("model_name"));
         deviceInfo.setDevice_id(device.getId());  
         deviceInfo.setUser_agent(requestInfo.getUserAgent());
         deviceInfo.setMax_image_width(
               Integer.valueOf(device.getCapability("max_image_width")).intValue());
         deviceInfo.setXhtml_display_accesskey(
               Boolean.valueOf(device.getCapability("xhtml_display_accesskey")).booleanValue());
         deviceInfo.setXhtml_make_phone_call_string(device.getCapability("xhtml_make_phone_call_string"));
         deviceInfo.setLast_modified(getStatusInfo().getLast_modified());
      } catch (CapabilityNotDefinedException e) {
         logger.warn(e);
      }
      
      return deviceInfo;
   }
   
   @Override
   public StatusInfo getStatusInfo() {
      return this.statusInfo;
   }
   
   protected StatusInfo getNewStatusInfo(String wurflError) {
      long wurflLastModified = (new File(this.wurflPath)).lastModified();
      long wurflPatchLastModified = (new File(this.wurflPatchPath)).lastModified();
      long lastModified = (wurflLastModified > wurflPatchLastModified) ? wurflLastModified : wurflPatchLastModified;

      StatusInfo statusInfo = new StatusInfo(lastModified, wurflError);
      return statusInfo;
      
   }
   
   /**
    * create listeners and monitors for wurfl.xml and wurfl_patch.xml
    * 
    * @param wurflPath
    */
   protected void watchWurflFile(String wurflPath) {
      String wurflDir = StringUtils.removeEnd(wurflPath, "wurfl.xml");
      String patchPath = wurflDir + "wurfl_patch.xml";
      File wurflFile = new File(wurflPath);
      File patchFile = new File(patchPath);
      
      FileChangeListener wurflListener = new WurflFileListener();
      FileChangeListener wurflPatchListener = new WurflFileListener();
      
      fam = new FilesystemAlterationMonitor();
      fam.addListener(wurflFile, wurflListener);
      fam.addListener(patchFile, wurflPatchListener);
      logger.debug("watching " + wurflPath + " " + patchPath);
      fam.start();
   }

   protected synchronized void reloadWurfl() {
      fam.stop(); // stop watching the file, so we dont keep on calling the callback it while reload is happening
      try {
         WURFLHolder tempWurflHolder = new CustomWURFLHolder(this.wurflPath, new String[] {this.wurflPatchPath});
         wurflHolder = tempWurflHolder;
         this.statusInfo = getNewStatusInfo("");
         logger.debug("reload successful");
      } catch (Exception e) {
         logger.error("error in reloading wurfl", e);
         String errorMessage = (e.getCause() != null) ? e.getCause().getMessage() : e.getMessage();
         this.statusInfo = getNewStatusInfo(errorMessage);
      } finally {
         watchWurflFile(this.wurflPath);
      }
   }

   public void cleanup() {
      logger.info("stopping watching wurfl");
      fam.stop();
   }

   
   public void setWurflHolder(WURFLHolder wurflHolder) {
      this.wurflHolder = wurflHolder;
   }

   protected class WurflFileListener extends FileChangeListener {

      public void onFileChange(File pFile) {
         super.onFileChange(pFile);
         if (hasChanged()) {
            logger.info("wurfl file changed, reloading"); 
            reloadWurfl();
         }
      }
   }


}
