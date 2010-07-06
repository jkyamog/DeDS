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

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This has the routines to properly detect devices that are Opera
 * Mini based devices.
 * 
 * Currently the implemetation just switches the user-agent and uses
 * wurfl for detection
 * 
 * @author jun yamog
 *
 */
public class OperaMiniCapabilityServiceImpl implements CapabilityService {
   
   private static final Log logger = LogFactory.getLog(OperaMiniCapabilityServiceImpl.class);

   public static final String OPERA_PHONE_UA = "X-OperaMini-Phone-UA";

   private WurflCapabilityServiceImpl wurflCapabilityService;
   
   public String getCapabilityForDevice(RequestInfo requestInfo,
         String capability) {
      if (isOpera(requestInfo))
         return wurflCapabilityService.getCapabilityForDevice(
            modifiedRequestInfo(requestInfo), capability);
      else
         return "";
   }

   public DeviceInfo getDeviceInfo(RequestInfo requestInfo) {
      if (isOpera(requestInfo))
         return wurflCapabilityService.getDeviceInfo(modifiedRequestInfo(requestInfo));
      else 
         return new DeviceInfo();
   }
   
   @Override
   public StatusInfo getStatusInfo() {
      StatusInfo statusInfo = new StatusInfo(0,"");
      return statusInfo;
   }
   
   private boolean isOpera(RequestInfo requestInfo) {
      return requestInfo.getHeaders().containsKey(OPERA_PHONE_UA);
   }
   
   private RequestInfo modifiedRequestInfo(RequestInfo requestInfo) {
      if (isOpera(requestInfo)) {
         // modify the user agent header and use the original one
         Map<String, String> headers = requestInfo.getHeaders();
         
         String newUserAgent = headers.get(OPERA_PHONE_UA);
         if (logger.isDebugEnabled()) {
            String oldUserAgent = requestInfo.getUserAgent();
            logger.debug("opera mini detected, modifying user-agent from " + oldUserAgent +
                  " to " + newUserAgent);
         }
         
         headers.put("user-agent", newUserAgent);
         RequestInfo modifiedReqeustInfo = new RequestInfo(headers);
         return modifiedReqeustInfo;
      }
      
      return requestInfo;
   }
   
   public void setWurflCapabilityService(WurflCapabilityServiceImpl wurflCapabilityService) {
      this.wurflCapabilityService = wurflCapabilityService;
   }

   public WurflCapabilityServiceImpl getWurflCapabilityService() {
      return wurflCapabilityService;
   }



}
