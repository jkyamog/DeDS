package nz.net.catalyst.mobile.mdl.device;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A bean that holds important info about the http request
 * Create a new ReqeustInfo object and avoid modifying existing ones
 * This is not 100% immutable, its been made as immutable has possible
 * w/o over complicating it.
 * 
 * @author jun yamog
 *
 */
public class RequestInfo {
   
   private static final Log logger = LogFactory.getLog(RequestInfo.class);
   
   public static final String REQUEST_INFO = "request-info";
   
   private Map<String, String> headers;
   
   public RequestInfo(Map<String, String> headers) {
      this.headers = Collections.unmodifiableMap(RequestInfo.getNewMap(headers));
      validateHeaders();
   }
   
   /**
    * creates a RequestInfo object based from the HttpServletRequest
    * 
    * use RequestInfo.getRequest when possible as it will cache the object
    * @param request
    */
   private RequestInfo(HttpServletRequest request) {
      HashMap<String, String> headers = new HashMap<String, String>();
      
      for (Enumeration<?> h=request.getHeaderNames(); h.hasMoreElements();) {
         String headerName = (String) h.nextElement();
         String headerValue = request.getHeader(headerName);
         
         logger.debug("header: " + headerName + " = " + headerValue);
         
         headers.put(headerName.toLowerCase(), headerValue);
      }
      
      this.headers = Collections.unmodifiableMap(getNewMap(headers));
      validateHeaders();
   }
   
   /**
    * this either creates a new RequestInfo or gets the cached instance from the
    * reqeust
    * @param request
    * @return
    */
   public static RequestInfo getRequestInfo(HttpServletRequest request) {
      RequestInfo requestInfo = (RequestInfo) request.getAttribute(REQUEST_INFO);
      if (requestInfo == null) {
         requestInfo = new RequestInfo(request);
         request.setAttribute(REQUEST_INFO, requestInfo);
      }
      
      return requestInfo;
   }
   
   @SuppressWarnings("unchecked") // CaseInsesitiveMap is not a generic class
   private static Map<String, String> getNewMap(Map<String, String> map) {
      return new CaseInsensitiveMap(map);
   }
   
   private void validateHeaders() throws IllegalArgumentException {
      if (headers == null)
         throw new IllegalArgumentException ("headers is empty");
      
      if (!headers.containsKey("user-agent")) 
         throw new IllegalArgumentException ("required http header 'user-agent' is missing");
   }

   
   
   /**
    * convenience method get the user agent header which is the mostly used
    * header
    * @return
    */
   public String getUserAgent() {
      return StringUtils.defaultString(headers.get("user-agent"));
   }

   /**
    * get a new copy of the header map.
    * @return
    */
   public Map<String, String> getHeaders() {
      return getNewMap(headers);
   }

}
