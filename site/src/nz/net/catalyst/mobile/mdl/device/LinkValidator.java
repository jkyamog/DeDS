package nz.net.catalyst.mobile.mdl.device;

import java.util.HashMap;

public interface LinkValidator {


   /**
    * Check a certain reqeustInfo against a list of capabilities.  all capabilities must match
    * to be true
    * @param requestInfo
    * @param capabilityProperties a HashMap with capabilities in name, value pairs
    * @return
    */
   public boolean isCapable(RequestInfo requestInfo, HashMap<String, String> capabilityProperties) throws IllegalArgumentException;

   /**
    * Check a certain reqeustInfo against a list of capabilities.  only 1 capability must match
    * to be true
    * @param reqeustInfo
    * @param capabilityProperties a HashMap with capabilities in name, value pairs
    * @return
    */
   public boolean canBeCapable(RequestInfo requestInfo, HashMap<String, String> capabilityProperties) throws IllegalArgumentException;
   
}