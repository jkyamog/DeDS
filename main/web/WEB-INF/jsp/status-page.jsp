<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
<title>DDS Status</title>
</head>
<body>
<jsp:useBean id="date" class="java.util.Date">
  <jsp:setProperty name="date" property="time" value="${statusinfo.last_modified}"/>
</jsp:useBean>
<p>Data Last Modified: <fmt:formatDate value="${date}" type="both" pattern="dd-MMM-yyyy HH:mm:ss"/></p>
<p>Last Error: ${statusinfo.last_error}</p>
<p>Device Info: ${device}</p>
</body>
</html>