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

import java.util.List;
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

   @Override
   public Map<String, Object> getCapabilitiesForDevice(RequestInfo requestInfo,
         List<String> capabilities) {
      // TODO Auto-generated method stub
      return null;
   }
   
   

}
