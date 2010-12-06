/*
 * Copyright (C) 2010 Catalyst IT Limited
 *
 * Copying and distribution of this file, with or without modification,
 * are permitted in any medium without royalty provided the copyright
 * notice and this notice are preserved.  This file is offered as-is,
 * without any warranty.
 */

package nz.net.catalyst.mobile.mdl.device.sample;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 * DDS Client, does http request and json encode/decode
 * 
 * @author jun yamog
 *
 */
public class DDSClient {
   private static Logger logger = Logger.getLogger(DDSClient.class.getName());
   
   /**
    * Get the capabilities as a map of capabilities.  There are times that a strict data type is not desirable.
    * Getting it as map provides some flexibility.  This does not require any knowledge of what capabilities
    * will be returned by the service.
    *  
    * @param request
    * @param ddsUrl
    * @return
    * @throws IOException
    */
   public static Map<String, String> getCapabilities(HttpServletRequest request, String ddsUrl) throws IOException {
      URL url;

      try {
         url = new URL(ddsUrl + "/get_capabilities?capability=resolution_width&capability=model_name&capability=xhtml_support_level&" +
         		"headers=" + URLEncoder.encode(jsonEncode(getHeadersAsHashMap(request)), "UTF-8"));
      } catch (MalformedURLException e) {
         throw new IOException(e);
      }
      
      String httpResponse = fetchHttpResponse(url);
      return jsonDecode(httpResponse, new TypeReference<Map<String, String>>() { });

   }
   
   /**
    * convenience method to do a http request on url, and return result as a string
    * 
    * @param url
    * @return
    * @throws IOException
    */
   private static String fetchHttpResponse(URL url) throws IOException {
      HttpURLConnection conn = null;
      try {
         logger.info("fetching from url = " + url);
         conn = (HttpURLConnection) url.openConnection();
         int response = conn.getResponseCode();
         if (response != HttpURLConnection.HTTP_ACCEPTED) {
            String responseContent = IOUtils.toString(conn.getInputStream());
            logger.info("responseContent = " + responseContent);
            
            return responseContent;
         }
         throw new IOException("response from service not valid");
      } catch (IOException e) {
         logger.severe("unable to do proper http request url = " + url.toString());
         throw e;
      } finally {
         conn.disconnect();
      }

   }
   
   /**
    * Given a string and a type ref.  Decode the string and return the values.
    * 
    * @param <T>
    * @param responseContent
    * @param valueTypeRef
    * @return
    * @throws IOException
    */
   private static <T> T jsonDecode(String responseContent, TypeReference<T> valueTypeRef) throws IOException {

      ObjectMapper mapper = new ObjectMapper();
      
      try {
         return mapper.<T>readValue(responseContent, valueTypeRef);
      } catch (JsonGenerationException e) {
         logger.severe("unable decode");
         throw new IOException(e);
      } catch (JsonMappingException e) {
         logger.severe("unable decode");
         throw new IOException(e.getMessage());
      }
   }

   
   /**
    * Encode a java object into a json which is returned as a string
    * 
    * @param object
    * @return
    * @throws IOException
    */
   private static String jsonEncode(Object object) throws IOException {
      
      ObjectMapper mapper = new ObjectMapper();
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      try {
         mapper.writeValue(outputStream, object);
         return new String(outputStream.toByteArray());
      } catch (JsonGenerationException e) {
         logger.severe("unable encode");
         throw new IOException(e);
      } catch (JsonMappingException e) {
         logger.severe("unable encode");
         throw new IOException(e.getMessage());
      }
      
   }

   /**
    * convenience method turn http headers into a hash map
    * 
    * @param request
    * @return
    */
   private static Map<String, String> getHeadersAsHashMap(HttpServletRequest request) {
      Map<String, String> headers = new HashMap<String, String>();
      
      for (Enumeration<?> h=request.getHeaderNames(); h.hasMoreElements();) {
         String headerName = (String) h.nextElement();
         String headerValue = request.getHeader(headerName);
         headers.put(headerName.toLowerCase(), headerValue);
      }
      
      return headers;
      
   }

   
}
