<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Login Form</title>
<title>Please sign in</title>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">
    <style>
		body {
			display: flex;
		    flex-direction: column;
		    min-height: 100vh;
		  	background-color: #eee;
		}
		
		.container-wrapper {
		    flex-grow: 1;
		    display: flex;
		    justify-content: center;
		    align-items: center;
		    margin-bottom: 150px
		}
    
    </style>
</head>
<body>
	<nav class="navbar navbar-expand-lg navbar-light bg-light">
	    <a class="navbar-brand" href="${pageContext.request.contextPath}/home">Library Management</a>
	
	    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
	        <span class="navbar-toggler-icon"></span>
	    </button>
	
	    <div class="collapse navbar-collapse" id="navbarNav">
	        <ul class="navbar-nav mr-auto">
	        	
	            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/memberForm">Sign Up</a></li>
	            
	            <sec:authorize access="hasAnyAuthority('ADMIN', 'USER')">
	            	<li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/transactionForm">Transaction Form</a></li>
	            	<li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/transactionHistory">Transaction History</a></li>
	            </sec:authorize>
	            <sec:authorize access="hasAuthority('ADMIN')">
		            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/librarianForm">Librarian Form</a></li>
		            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/publisherForm">Publisher Form</a></li>
		            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/bookForm">Book Form</a></li>
	            </sec:authorize>
	        </ul>
	        <ul class="navbar-nav ml-auto">
	            <sec:authorize access="isAuthenticated()">
	                <li class="nav-item">
	                    USER: ${pageContext.request.userPrincipal.name}
	                    <br />
	                    <sec:authentication property="principal.authorities" var="authorities" />
	                    Roles:
	                    <c:forEach items="${authorities}" var="authority">
	                        ${authority.authority}&nbsp;
	                    </c:forEach>
	                </li>
	            </sec:authorize>
	
	            <sec:authorize access="!isAuthenticated()">
	                <li class="nav-item"><a class="btn btn-primary" href="${pageContext.request.contextPath}/login">Login</a></li>
	            </sec:authorize>
	            <sec:authorize access="isAuthenticated()">
	                <li class="nav-item"><a class="btn btn-danger" href="${pageContext.request.contextPath}/logout">Logout</a></li>
	            </sec:authorize>
	        </ul>
	    </div>
	</nav>
    
   <div class="container-wrapper">
<div class="container">
	<div class="d-flex justify-content-center">
	    
	    <f:form action="login" >
	    	<div class="form-group">
	            <h2 style="color:#007bff;">Login</h2>
	        </div>
	        <div class="form-group">
	            <label for="username">Username:</label>
	            <input id="username" class="form-control" type="text" name="username" required>
	        </div>
	
	        <div class="form-group">
	            <label for="password">Password:</label>
	            <input id="password" class="form-control" type="password" name="password" required>
	        </div>
	        
	        <div class="text-muted">
	            Don't have an account? <a href="${pageContext.request.contextPath}/memberForm">Create one here!</a>
	        </div>
	
	        <input class="btn btn-primary mt-3" type="submit" value="Submit">
	        <sec:csrfInput />
	    </f:form>
	</div>
</div>
</div>
</body>
</html>