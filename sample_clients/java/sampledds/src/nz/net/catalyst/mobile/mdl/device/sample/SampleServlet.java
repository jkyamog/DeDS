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
import java.util.Map;
import java.util.Map.Entry;

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

         // get the capabilities as hashmap of capabilities
         Map<String, String> capabilities = DDSClient.getCapabilities(request, ddsUrl);
         out.write("\nget capabilities as map\n");
         for (Entry<String, String> capability: capabilities.entrySet()) {
            out.write(capability.getKey() + " = " + capability.getValue() + "\n");
         }
      }

}
