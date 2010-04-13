package nz.net.catalyst.mobile.mdl.device;

/**
 * just a simple bean to hold the current status of a capability service
 * 
 * NOTE: the attributes is not in camel case.  This is because status info 
 * is also used externally
 *
 * @author jun yamog
 *
 */
public class StatusInfo {
   private final long last_modified;
   private final String last_error;

   public StatusInfo (long lastModified, String lastError) {
      this.last_modified = lastModified;
      this.last_error = lastError;
   }
   
   /**
    * returns time when service was last modified in unix time.  this is normally
    * useful for consumers of the service that cached the device info
    * 
    * @return
    * @see DeviceInfo
    */
   public long getLast_modified() {
      return last_modified;
   }

   /**
    * if there is a problem w/ the service, this should hold some meaningful message
    * @return
    */
   public String getLast_error() {
      return last_error;
   }
}
