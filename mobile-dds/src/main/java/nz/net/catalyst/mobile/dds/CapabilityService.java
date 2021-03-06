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

package nz.net.catalyst.mobile.dds;

import java.util.List;
import java.util.Map;


/**
 * Interface for getting capability for a mobile device
 * 
 * @author Jun Yamog
 * 
 */
public interface CapabilityService {

   /**
    * Given the requestInfo and a List of capabilities, return a Map that would contain the
    * capabilities as keys with values
    * @param requestInfo
    * @param capabilities
    * @return
    */
   public Map<String, Object> getCapabilitiesForDevice(RequestInfo requestInfo, List<String> capabilities);
   
   /**
    * gets a status info bean this would let users of the service know the status of the service
    * 
    * @return
    */
   public StatusInfo getStatusInfo();

}
