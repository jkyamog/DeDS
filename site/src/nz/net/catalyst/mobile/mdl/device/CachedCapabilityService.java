/*
 * Copyright (C) 2010  Catalyst IT Limited
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

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
