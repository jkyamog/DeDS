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
