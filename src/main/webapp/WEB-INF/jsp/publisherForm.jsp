<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <title>Publisher Form</title>
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

<div class="container mt-5">
    <h1 class="text-center mb-4">Publisher Registration Form</h1>
    <div class="row justify-content-center">
        <div class="col-lg-6">
            <f:form action="savePublisher" method="POST" modelAttribute="publisher">
                <div class="form-group">
                    <label for="publisherId">Publisher Id:</label>
                    <f:input path="publisherId" id="publisherId" value="${p.publisherId}" class="form-control"  disabled="true"/>
                </div>
                
                <div class="form-group">
                    <label for="publisherName">Publisher Name:</label>
                    <f:input path="publisherName" id="publisherName" value="${p.publisherName}"  class="form-control" />
                    <f:errors path="publisherName" cssClass="text-danger"/>
                </div>
                
                <div class="form-group">
                    <label for="addressLine1">Address Line 1:</label>
                    <f:input path="addressLine1" id="addressLine1" value="${p.addressLine1}" class="form-control" />
                    <f:errors path="addressLine1" cssClass="text-danger"/>
                </div>
                
                <div class="form-group">
                    <label for="addressLine2">Address Line 2:</label>
                    <f:input path="addressLine2" id="addressLine2" value="${p.addressLine2}" class="form-control" />
                    <f:errors path="addressLine2" cssClass="text-danger"/>
                </div>
                
                <div class="form-group">
                    <label for="email">Email:</label>
                    <f:input type="email" path="email" id="email" value="${p.email}" class="form-control" />
                    <f:errors path="email" cssClass="text-danger"/>
                </div>

                <div class="form-group text-center">
                    <input type="submit" value="Submit" class="btn btn-primary" />
                </div>
            </f:form>
        </div>
    </div>
</div>
	
<div>
	<h2 class="text-center mt-5">List of Publishers</h2>
	<div class="row justify-content-center">
		<div class="col-lg-8">
			<div class="table-responsive ">
				<table class="table table-bordered table-striped">
					<thead class="thead-dark">
						<tr>
							<th><a href="${pageContext.request.contextPath}/publisherForm?sortBy=publisherId">Publisher Id</a></th>
							<th><a href="${pageContext.request.contextPath}/publisherForm?sortBy=publisherName">Publisher Name</a></th>
							<th><a href="${pageContext.request.contextPath}/publisherForm?sortBy=email">Email</a></th>
							<th><a href="${pageContext.request.contextPath}/publisherForm?sortBy=addressLine1">Publisher Address 1</a></th>
							<th><a href="${pageContext.request.contextPath}/publisherForm?sortBy=addressLine2">Publisher Address 2</a></th>
							
							<th colspan="2">Actions</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${publisherPage.getContent()}" var="p">
							<tr>
								<td>${p.getPublisherId()}</td>
								<td>${p.getPublisherName()}</td>
								<td>${p.getEmail()}</td>
								<td>${p.getAddressLine1()}</td>
								<td>${p.getAddressLine2()}</td>
								<td><a href="updatePublisher?publisherId=${p.getPublisherId()}" class="btn btn-secondary btn-sm">Update</a></td>
								<td><a href="deletePublisher?publisherId=${p.getPublisherId()}" class="btn btn-danger btn-sm">Delete</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				
				<!-- Pagination -->
		        <c:if test="${publisherPage.getTotalPages() > 0}">
			        <nav aria-label="Page navigation">
					  <ul class="pagination justify-content-center">
					    <c:forEach
					      begin="0"
					      end="${publisherPage.getTotalPages() - 1}"
					      var="i"
					    > 
					      <li class="page-item ${i == publisherPage.number ? 'active' : ''}">
					        <a
					          class="page-link"
					          href="${pageContext.request.contextPath}/publisherForm?page=${i}&size=${publisherPage.size}&sortBy=${sortBy}"
					          >${i + 1}</a
					        >
					      </li>
					    </c:forEach>
					  </ul>
					</nav>
				</c:if>
				
			</div>
		</div>
	</div>
</div>		
</body>
</html>