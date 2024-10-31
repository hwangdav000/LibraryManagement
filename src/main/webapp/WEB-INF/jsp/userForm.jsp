<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <title>User Form</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
	<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.1/dist/umd/popper.min.js"></script>
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>
	<nav class="navbar navbar-expand-lg navbar-light bg-light">
	    <a class="navbar-brand" href="${pageContext.request.contextPath}/home">Library Management</a>
	
	    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
	        <span class="navbar-toggler-icon"></span>
	    </button>
	
	    <div class="collapse navbar-collapse" id="navbarNav">
	        <ul class="navbar-nav mr-auto">
	            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/roleForm">Role Form</a></li>
	            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/userForm">User Form</a></li>
	            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/memberForm">Member Form</a></li>
	            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/librarianForm">Librarian Form</a></li>
	            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/publisherForm">Publisher Form</a></li>
	            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/bookForm">Book Form</a></li>
	            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/transactionHistory">Transaction History</a></li>
	            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/transactionForm">Transaction Form</a></li>
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

<div class="container mt-5">
    <h1 class="text-center mb-4">User Registration Form</h1>
    <div class="row justify-content-center">
        <div class="col-lg-6">
            <f:form action="saveUser" method="POST" modelAttribute="user">
                <div class="form-group">
                    <label for="userId">User Id:</label>
                    <f:input path="userId" id="userId" class="form-control"  disabled="true"/>
                </div>
                
                <div class="form-group">
                    <label for="username">User Name:</label>
                    <f:input path="username" id="username" class="form-control" />
                </div>
                
                <div class="form-group">
                    <label for="userPassword">Password:</label>
                    <f:input path="userPassword" id="userPassword" class="form-control" />
                </div>
                
                <div class="form-group">
                    <label>Roles:</label>
                    <div>
                        <c:forEach items="${roles}" var="role">
                            <div class="form-check">
                                <c:choose>    
                                    <c:when test="${retrievedRoles.contains(role.roleId)}">
                                        <f:checkbox path="userRoles" value="${role.roleId}" class="form-check-input" checked="true"/>
                                        <label class="form-check-label">${role.roleName}</label>
                                    </c:when>
                                    <c:otherwise>
                                        <f:checkbox path="userRoles" value="${role.roleId}" class="form-check-input"/>
                                        <label class="form-check-label">${role.roleName}</label>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:forEach>
                    </div>
                </div>
                
                <div class="form-group text-center">
                    <input type="submit" value="Submit" class="btn btn-primary" />
                </div>
            </f:form>
        </div>
    </div>
</div>

<div>
	<h2 class="text-center mt-5">List of Users</h2>
	<div class="row justify-content-center">
		<div class="col-lg-8">
			<div class="table-responsive ">
				<table class="table table-bordered table-striped">
					<thead class="thead-dark">
						<tr>
							<th>User Id</th>
							<th>User Name</th>
							<th colspan="2">Actions</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${users}" var="u">
							<tr>
								<td>${u.getUserId()}</td>
								<td>${u.getUsername()}</td>
								<td><a href="updateUser?userId=${u.getUserId()}" class="btn btn-secondary btn-sm">Update</a></td>
								<td><a href="deleteUser?userId=${u.getUserId()}" class="btn btn-danger btn-sm">Delete</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>	
</body>
</html>