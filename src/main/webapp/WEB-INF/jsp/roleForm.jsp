<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <title>Role Form</title>
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
    <h1 class="text-center mb-4">Role Registration Form</h1>
    <div class="row justify-content-center">
        <div class="col-lg-6">
			<f:form action="saveRole" method="POST" modelAttribute="role">
				<div class="form-group">
				    <label for="roleId">Role Id: </label>
				    <f:input path="roleId" id="roleId" value="${r.roleId}" class="form-control"  disabled="true"/>
				</div>
				
				<div class="form-group">
				    <label for="roleName">Role Name: </label>
				    <f:input path="roleName" id="roleName" value="${r.roleName}" class="form-control" />
				</div>

				
				<div class="form-group text-center">
					<input type="submit" value="Submit" class="btn btn-primary" />
				</div>
			</f:form>
        </div>
    </div>
</div>

<div>
	<h2 class="text-center mt-5">List of Roles</h2>
	<div class="row justify-content-center">
		<div class="col-lg-8">
			<div class="table-responsive">
				<table class="table table-bordered table-striped">
					<thead class="thead-dark">
						<tr>
							<th>Role Id</th>
							<th>Role Name</th>
							<th colspan="2">Actions</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${roles}" var="r">
							<tr>
								<td>${r.getRoleId()}</td>
								<td>${r.getRoleName()}</td>
								<td>
									<a href="updateRole?roleId=${r.getRoleId()}" class="btn btn-secondary btn-sm">Update</a>
									<a href="deleteRole?roleId=${r.getRoleId()}" class="btn btn-danger btn-sm">Delete</a>
								</td>
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