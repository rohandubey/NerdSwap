<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!doctype html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Log-In</title>

    <%@include file="bootstrapFiles.jsp"%>
    <style>
	body {
	  background-image: url('/img/bg.jpg');
	}
</style>
</head>

<body class="container" style="padding-top: 1%">
<div class="logo-div">
    <img src="/img/logo.png" class="mx-auto d-block"/>
</div>

<div class="card book-share-card">
    <div class="card-body text-center">
        
        <%--@elvariable id="user" type="org"--%>
        <form:form modelAttribute="user" method="post" enctype="multipart/form-data">
            <div class="row">
                <div class="col-sm-4 form-group">
                    <form:label path="userName">Username</form:label>
                </div>
                <div class="col-sm-8 form-group">
                    <form:input cssClass="form-control" path="userName" size="15" maxlength="15" placeholder="Username" required="required"/>
                    <form:errors path="userName"/>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-4 form-group">
                    <form:label path="userPassword">Password</form:label>
                </div>
                <div class="col-sm-8 form-group">
                    <form:password cssClass="form-control" path="userPassword" size="15" placeholder="Password" required="required" />
                    <form:errors path="userPassword"/>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-4 form-group">
                    <form:label path="userType">User Type</form:label>
                </div>
                <div class="col-sm-8 form-group">
                    <form:select cssClass="form-control" path="userType" items="${userTypeOptionList}"/>
                </div>
            </div>

            <input type="submit" name="button" class="btn btn-lg btn-primary btn-block" value="Log in"/>
            <a href="/signUp" class="btn btn-lg btn-secondary btn-block" role="button" aria-pressed="true" style="margin-top: 10px;">Sign Up</a>
        	<div style="margin-top: 10px;">
                <a href="/forgetPassword" >Forgot Password</a>
            </div>
        </form:form>
    </div>
</div>
</body>
</html>
