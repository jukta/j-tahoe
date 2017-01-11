<%--<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>--%>
<%@ taglib prefix="ex" uri="http://jukta.com/tahoe/taglib" %>
<html>
    <head>
        <title>First JSP</title>
        <link rel="stylesheet" href="/all.css" type="text/css"/>
        <script src="/all.js" type="text/javascript"></script>
    </head>
    <body>
        <ex:block name="com.jukta.taglib.test.BlockA">
            <ex:attr name="a" value="<%= request.getRequestURI() %>"/>
        </ex:block>
    </body>
</html>