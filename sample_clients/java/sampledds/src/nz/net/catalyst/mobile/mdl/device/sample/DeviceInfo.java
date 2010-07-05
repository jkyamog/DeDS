/*
 * Copyright (C) 2010 Catalyst IT Limited
 *
 * Copying and distribution of this file, with or without modification,
 * are permitted in any medium without royalty provided the copyright
 * notice and this notice are preserved.  This file is offered as-is,
 * without any warranty.
 */

package nz.net.catalyst.mobile.mdl.device.sample;

/**
 * Bean that holds most used attributes for a device.  Take note the attributes
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


}
