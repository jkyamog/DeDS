package nz.net.catalyst.mobile.mdl.device;

import java.io.File;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.apache.commons.jci.listeners.FileChangeListener;
import org.apache.commons.jci.monitor.FilesystemAlterationMonitor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ServletContextAware;

import net.sourceforge.wurfl.core.CapabilityNotDefinedException;
import net.sourceforge.wurfl.core.CustomWURFLHolder;
import net.sourceforge.wurfl.core.Device;
import net.sourceforge.wurfl.core.WURFLHolder;

public class WurflCapabilityServiceImpl implements CapabilityService, ServletContextAware {
   
   private final static Log logger = LogFactory.getLog(WurflCapabilityServiceImpl.class);
   
   private final static String WURFL_FILENAME = "wurfl.xml";

   private WURFLHolder wurflHolder;
   private ServletContext servletContext;
   
   private FilesystemAlterationMonitor fam;
   private String wurflDirPath;

   private File wurflFile;
   private File[] wurflPatchFiles;
   
   private StatusInfo statusInfo;

   public void init() {
      
      String fullWurflDirPath = servletContext.getRealPath(wurflDirPath);
      File wurflDir = new File(fullWurflDirPath);
      
      if (!wurflDir.isDirectory()) 
         throw new IllegalArgumentException("wurflDirPath " + wurflDir.getAbsolutePath() + " is not a directory");
      
      ArrayList<File> patchFiles = new ArrayList<File> ();

      logger.info("search for wurfl file and patches on: " + wurflDir.getAbsolutePath());
      for (String filePath:wurflDir.list()) {
         File file = new File(wurflDir.getAbsoluteFile() + "/" + filePath);
         if (WURFL_FILENAME.equals(file.getName())) {
            wurflFile = file;
            logger.debug("wurfl file: " + wurflFile.getAbsolutePath());
         } else {
            patchFiles.add(file);
            logger.debug("wurfl patch file: " + file.getAbsolutePath());
         }
                  
      }
      this.wurflPatchFiles = patchFiles.toArray(new File[] {});

      this.wurflHolder = new CustomWURFLHolder(this.wurflFile, this.wurflPatchFiles);
      
      watchWurflFile();
      this.statusInfo = getNewStatusInfo("");
   }
   
   public void cleanup() {
      logger.info("stopping watching wurfl");
      fam.stop();
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
      long lastModified = wurflFile.lastModified();

      for (File patchFile:wurflPatchFiles) {
         long wurflPatchLastModified = patchFile.lastModified();
         if (lastModified < wurflPatchLastModified) lastModified = wurflPatchLastModified;
      }

      StatusInfo statusInfo = new StatusInfo(lastModified, wurflError);
      return statusInfo;
      
   }
   
   /**
    * create listeners and monitors for wurfl and its patches
    * 
    * @param wurflPath
    */
   protected void watchWurflFile() {

      fam = new FilesystemAlterationMonitor();
      FileChangeListener wurflListener = new WurflFileListener();
      fam.addListener(wurflFile, wurflListener);
      logger.debug("watching " + wurflFile.getAbsolutePath());
      
      for (File patchFile:wurflPatchFiles) {
         FileChangeListener wurflPatchListener = new WurflFileListener();
         fam.addListener(patchFile, wurflPatchListener);
         logger.debug("watching " + patchFile.getAbsolutePath());
      }
      
      fam.start();
   }

   protected synchronized void reloadWurfl() {
      fam.stop(); // stop watching the file, so we dont keep on calling the callback it while reload is happening
      try {
         WURFLHolder tempWurflHolder = new CustomWURFLHolder(this.wurflFile, this.wurflPatchFiles);
         wurflHolder = tempWurflHolder;
         this.statusInfo = getNewStatusInfo("");
         logger.debug("reload successful");
      } catch (Exception e) {
         logger.error("error in reloading wurfl", e);
         String errorMessage = (e.getCause() != null) ? e.getCause().getMessage() : e.getMessage();
         this.statusInfo = getNewStatusInfo(errorMessage);
      } finally {
         watchWurflFile();
      }
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
   
   public void setWurflDirPath(String wurflDirPath) {
      this.wurflDirPath = wurflDirPath;
   }

   @Override
   public void setServletContext(ServletContext servletContext) {
      this.servletContext = servletContext;
      
   }


}
