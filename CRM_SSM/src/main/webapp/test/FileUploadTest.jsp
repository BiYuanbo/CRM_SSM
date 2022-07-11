<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>演示文件上传</title>
</head>
<body>
<%--文件上传表单的三个条件
    1.表单组件标签必须用<input type="file">
    2.请求方式只能用post
    3.文件上传格式只能用：multipart/form-data--%>
<form action="workbench/activity/fileUpload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="myFile"> <br>
    <input type="text" name="username"><br>
    <input type="submit" value="提交">
</form>
</body>
</html>
