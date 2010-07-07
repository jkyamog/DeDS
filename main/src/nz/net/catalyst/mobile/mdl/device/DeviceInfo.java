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

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * Bean that holds most used attributes from a device.  Take note the attributes
 * is not in camel case.  This is because device info is also used externally and
 * also it makes the attributes consistent w/ wurfl names
 * 
 * @author jun yamog
 *
 */

public class DeviceInfo {
   private String brand_name;
   private String model_name;
   private String user_agent;
   private String device_id;
   private int max_image_width;
   private boolean xhtml_display_accesskey;
   private String xhtml_make_phone_call_string;
   private int xhtml_support_level;
   private long last_modified;

   /**
    * set the last known unix time when this device was last modified
    * @param last_modified
    */
   public void setLast_modified(long last_modified) {
      this.last_modified = last_modified;
   }
   /**
    * get the last known last modified in unix time, normally used in
    * conjuction of StatusInfo 
    * @return
    * @see StatusInfo#getLast_modified()
    */
   public long getLast_modified() {
      return last_modified;
   }
   
   public void setBrand_name(String brandName) {
      this.brand_name = brandName;
   }
   public String getBrand_name() {
      return brand_name;
   }
   public void setModel_name(String modelName) {
      this.model_name = modelName;
   }
   public String getModel_name() {
      return model_name;
   }
   public void setUser_agent(String userAgent) {
      this.user_agent = userAgent;
   }
   public String getUser_agent() {
      return user_agent;
   }
   public void setDevice_id(String deviceID) {
      this.device_id = deviceID;
   }
   public String getDevice_id() {
      return device_id;
   }
   public void setMax_image_width(int maxImageWidth) {
      this.max_image_width = maxImageWidth;
   }
   public int getMax_image_width() {
      return max_image_width;
   }
   public void setXhtml_display_accesskey(boolean xhtmlDisplayAccesskey) {
      this.xhtml_display_accesskey = xhtmlDisplayAccesskey;
   }
   public boolean isXhtml_display_accesskey() {
      return xhtml_display_accesskey;
   }
   public void setXhtml_make_phone_call_string(String xhtmlMakePhoneCallString) {
      this.xhtml_make_phone_call_string = xhtmlMakePhoneCallString;
   }
   public String getXhtml_make_phone_call_string() {
      return xhtml_make_phone_call_string;
   }  
   public void setXhtml_support_level(int xhtml_support_level) {
      this.xhtml_support_level = xhtml_support_level;
   }
   public int getXhtml_support_level() {
      return xhtml_support_level;
   }
   /**
    * for debugging purposes ONLY!!!
    * MAKE SURE TO WRAP THIS ON logger.isDebugEnabled() condition
    */ 
   public String toStringForDebug() {
      return ReflectionToStringBuilder.toString(this);
   }

}
