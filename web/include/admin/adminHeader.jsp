<%--
  Created by IntelliJ IDEA.
  User: E7450
  Date: 2020/6/18
  Time: 20:40
  To change this template use File | Settings | File Templates.
--%>
<%--表示本页面会使用html5新技术--%>
<!DOCTYPE html >
<%--contentType告诉浏览器使用utf-8进行中文编码
pageEncoding表示当前jsp上的中文文字会使用utf-8进行编码
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
         pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<html>
<head>
    <script src="js/jquery/2.0.0/jquery.min.js"></script>
    <link href="css/bootstrap/3.3.6/bootstrap.min.css" rel="stylesheet">
    <script src="js/bootstrap/3.3.6/bootstrap.min.js"></script>
    <link href="css/back/style.css" rel="stylesheet">
    <script>
        function checkEmpty(id, name) {
            let value = $('#' + id).val();
            if (value.length == 0) {
                alert(name + "");
                $("#" + id)[0].focus();
                return false;
            }
            return true;
        }

        function checkNumber(id, name) {
            let value = $('#' + id).val();
            if (value.length === 0) {
                alert(name + "");
                $("#" + id)[0].focus();
                return false;
            }
            if (isNaN(value)) {
                alert(name + "必须是数字");
                $("#" + id)[0].focus();
                return false;
            }
            return true;
        }

        function checkInt(id, name) {
            let value = $('#' + id).val();
            if (value.length === 0) {
                alert(name + "");
                $("#" + id)[0].focus();
                return false;
            }
            if (parseInt(value) !== value) {
                alert(name + "必须是整数");
                $("#" + id)[0].focus();
                return false;
            }
            return true;
        }

        $(function () {
            $("a").click(function () {
                let deleteLink = $(this).attr("deleteLink");
                console.log(deleteLink);
                if ("true" === deleteLink) {
                    return confirm("确认要删除");
                }
            });
        });
    </script>
</head>
<body>

</body>
</html>
