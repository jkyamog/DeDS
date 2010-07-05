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
    * Get a device info bean from the service.  This is a way for getting capabilities
    * w/ data types.  This requires the device info bean is similarly defined on the
    * service.
    * 
    * @param request
    * @param ddsUrl
    * @return
    * @throws IOException
    */
   public static DeviceInfo getDeviceInfo(HttpServletRequest request, String ddsUrl) throws IOException {

      String httpResponse = getDeviceInfoAsString(request, ddsUrl);
      return jsonDecode(httpResponse, DeviceInfo.class);

   }

   /**
    * Get the device Info as a hashmap of capabilities.  There are times that a strict data type is not desirable.
    * Getting it as hash map provides some flexibility.  This does not require any knowledge of what capabilities
    * will be returned by the service.
    *  
    * @param request
    * @param ddsUrl
    * @return
    * @throws IOException
    */
   public static HashMap<String, String> getDeviceInfoAsHashMap(HttpServletRequest request, String ddsUrl) throws IOException {
      
      String httpResponse = getDeviceInfoAsString(request, ddsUrl);
      return (HashMap<String, String>) jsonDecode(httpResponse, new TypeReference<Map<String, String>>() { });

   }
   
   /**
    * Get a single capability from DDS
    * 
    * @param capability - name of capability (ex. resolution_width, brand_name, etc.)
    * @param request
    * @param ddsUrl
    * @return
    * @throws IOException
    */
   public static String getCapability(String capability, HttpServletRequest request, String ddsUrl) throws IOException {
      URL url;

      try {
         url = new URL(ddsUrl + "/get_capability?capability=" + capability + "&headers=" 
               + URLEncoder.encode(jsonEncode(getHeadersAsHashMap(request)), "UTF-8"));
      } catch (MalformedURLException e) {
         throw new IOException(e);
      }

      String httpResponse = fetchHttpResponse(url);
      return jsonDecode(httpResponse, String.class);

   }

   /**
    * Get the device info, but not yet json decoded, still as a string
    * 
    * @param request
    * @param ddsUrl
    * @return
    * @throws IOException
    */
   private static String getDeviceInfoAsString(HttpServletRequest request, String ddsUrl) throws IOException{
      URL url;

      try {
         url = new URL(ddsUrl + "/get_deviceinfo?headers=" 
               + URLEncoder.encode(jsonEncode(getHeadersAsHashMap(request)), "UTF-8"));
      } catch (MalformedURLException e) {
         throw new IOException(e);
      }
      
      return fetchHttpResponse(url);

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
      }

   }
   
   /**
    * Given a string and bean class.  Decode the string and return bean w/ values filled
    * 
    * @param <T>
    * @param responseContent
    * @param valueType
    * @return
    * @throws IOException
    */
   private static <T> T jsonDecode(String responseContent, Class<T> valueType) throws IOException {

      ObjectMapper mapper = new ObjectMapper();
      
      try {
         return valueType.cast(mapper.readValue(responseContent, valueType));
      } catch (JsonGenerationException e) {
         logger.severe("unable decode");
         throw new IOException(e);
      } catch (JsonMappingException e) {
         logger.severe("unable decode");
         throw new IOException(e.getMessage());
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
   @SuppressWarnings("unchecked")
   private static <T> T jsonDecode(String responseContent, TypeReference<T> valueTypeRef) throws IOException {

      ObjectMapper mapper = new ObjectMapper();
      
      try {
         return (T) mapper.readValue(responseContent, valueTypeRef);
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
   private static HashMap<String, String> getHeadersAsHashMap(HttpServletRequest request) {
      HashMap<String, String> headers = new HashMap<String, String>();
      
      for (Enumeration<?> h=request.getHeaderNames(); h.hasMoreElements();) {
         String headerName = (String) h.nextElement();
         String headerValue = request.getHeader(headerName);
         headers.put(headerName.toLowerCase(), headerValue);
      }
      
      return headers;
      
   }

   
}
