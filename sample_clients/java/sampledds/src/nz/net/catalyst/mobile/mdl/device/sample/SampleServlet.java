/*
 * Copyright (C) 2010 Catalyst IT Limited
 *
 * Copying and distribution of this file, with or without modification,
 * are permitted in any medium without royalty provided the copyright
 * notice and this notice are preserved.  This file is offered as-is,
 * without any warranty.
 */

package nz.net.catalyst.mobile.mdl.device.sample;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SampleServlet extends HttpServlet {

   private static final long serialVersionUID = 4960800053661237654L;
   
      protected void doGet(HttpServletRequest request, HttpServletResponse response) 
         throws ServletException, IOException {
         
         response.setContentType("text/plain");
         PrintWriter out = response.getWriter();
         String ddsUrl = this.getInitParameter("dds_url");

         // get a single capability from the service
         String resolutionWidth = DDSClient.getCapability("resolution_width", request, ddsUrl);
         out.write("get a single capability\n");
         out.write("resolutionWidth = " + resolutionWidth + "\n");

         // get the device info object
         DeviceInfo deviceInfo = DDSClient.getDeviceInfo(request, ddsUrl);
         out.write("\nget device info object\n");
         out.write("deviceInfo.getBrandName() = " + deviceInfo.getBrand_name() + "\n");
         
         // get the device info, but this time as hashmap of capabilities
         HashMap<String, String> deviceHash = DDSClient.getDeviceInfoAsHashMap(request, ddsUrl);
         out.write("\nget device info as hash map\n");
         Iterator<String> i = deviceHash.keySet().iterator();
         while (i.hasNext()) {
            String capability = i.next();
            out.write(capability + " = " + deviceHash.get(capability) + "\n");
         }
      }

}
