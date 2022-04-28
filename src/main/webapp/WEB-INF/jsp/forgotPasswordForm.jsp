<%--
  Created by IntelliJ IDEA.
  User: DEBOTUSH
  Date: 8/6/2019
  Time: 9:53 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="formm" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Sign Up</title>

    <%@include file="bootstrapFiles.jsp"%>
</head>

<body class="container" style="padding-top: 1%;">
<div class="logo-div">
    <img src="/img/logo.png" class="mx-auto d-block"/>
</div>

<div class="card book-share-card">
    <div class="card-body text-center">
        <form:form modelAttribute="user" method="post" enctype="multipart/form-data">

            <div class="row">
                <div class="col-sm-4 form-group">
                    <form:label path="userName">Username</form:label>
                </div>
                <div class="col-sm-8 form-group">
                    <form:input path="userName" size = "20" maxlength="20" cssClass="form-control" placeholder="Username" required="required"/>
                    <form:errors path="userName" />
                </div>
            </div>

            <div class="row">
                <div class="col-sm-4  form-group">
                    <form:label path="userPassword">New Password</form:label>
                </div>
                <div class="col-sm-8  form-group">
                    <form:password path="userPassword" size = "20" maxlength="20" cssClass="form-control" placeholder="New Password" required="required"/>
                    <form:errors path="userPassword" />
                </div>
            </div>

            
            <div class="row">
                <div class="col-sm-4 form-group">
                    <form:label path="userSecurityAns">Favorite Fruit</form:label>
                </div>
                <div class="col-sm-8 form-group">
                    <form:input path="userSecurityAns" size = "50" maxlength="20" cssClass="form-control" placeholder="Security Answer" required="required"/>
                    <form:errors path="userSecurityAns"/>
                </div>
            </div>

            <input type="submit" name="button" onclick="signUp()" class="btn btn-lg btn-primary btn-block" value="Change Password"/>
            <a href="/login" class="btn btn-lg btn-secondary btn-block" role="button" aria-pressed="true" style="margin-top: 10px;">Login</a>
        </form:form>
    </div>
</div>
</body>
</html>
