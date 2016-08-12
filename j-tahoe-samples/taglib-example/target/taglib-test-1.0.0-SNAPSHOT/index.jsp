<%--<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>--%>
<%@ taglib prefix="ex" uri="WEB-INF/jtahoe.tld" %>
<html>
    <head>
        <title>First JSP</title>
    </head>
    <body>
        <ex:block name="test.BlockA">
            <ex:attr name="a" value="<%= request.getRequestURI() %>"/>
        </ex:block>
    </body>
</html>