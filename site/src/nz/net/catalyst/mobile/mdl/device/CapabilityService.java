package nz.net.catalyst.mobile.mdl.device;


/**
 * Interface for getting capability for a mobile device
 * 
 * @author Jun Yamog
 * 
 */
public interface CapabilityService {

   /**
    * Given a requestInfo and a capability get a capability value. Normally this
    * value is true or false, but some are not like screen resolution hence
    * return type is string
    * 
    * @param reqeustInfo
    * @param capability
    * @return capability value
    */
   public String getCapabilityForDevice(RequestInfo requestInfo, String capability);

   /**
    * Retrieves the DeviceInfo from the request info which contains importat info about the
    * http request
    * @param requestInfo
    * @return DeviceInfo
    */
   public DeviceInfo getDeviceInfo(RequestInfo requestInfo);
   
   /**
    * gets a status info bean this would let users of the service know the status of the service
    * 
    * @return
    */
   public StatusInfo getStatusInfo();

}
