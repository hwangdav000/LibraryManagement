<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <title>Transaction History</title>
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
	
<div>
	<h2 class="text-center mt-5">Transaction History</h2>
	<div class="row justify-content-center">
		<div class="col-lg-8">
			<div class="table-responsive ">
				<table class="table table-bordered table-striped">
					<thead class="thead-dark">
						<tr>
							<th>Transaction Id</th>
							<th>Transaction Type</th>
							<th>Book ID</th>
							<th>Book Status</th>
							<th>User ID</th>
							<th>Amount</th>
							<th>Transaction Date</th>
							
							<th>Action</th>
							
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${transactions}" var="t" varStatus="status">
							<tr>
								<td>${t.transactionId}</td>
								<td>${t.transactionType}</td>
								<td>${t.bookId}</td>
								<td>${bookStatusList[status.index]}</td>
								<td>${t.userId}</td>
								<td>
								    <c:if test="${not empty t.amount}">
								        $${t.amount}
								    </c:if>
								</td>
								<td>${t.transactionDate}</td>
								
								<td>
									<c:choose>
							        <c:when test="${t.transactionType == 'BORROW'}">
							        	<form action="${pageContext.request.contextPath}/transactionForm" method="POST">
										    <input type="hidden" name="bkId" value="${t.bookId}" />
										    <input type="hidden" name="activeTab" value="returnTab" />
										    <button type="submit" class="btn btn-success btn-sm">Return Book</button>
										</form>
							            
							        </c:when>
							        <c:when test="${t.transactionType == 'RETURN'}">
							        	<!--  TODO -->
							            <button class="btn btn-success disabled btn-sm" disabled>Return Book</button>
							        </c:when>
							        <c:when test="${t.transactionType == 'PAYFINE'}">
							            <form action="${pageContext.request.contextPath}/transactionForm" method="POST">
										    <input type="hidden" name="activeTab" value="payFineTab" />
										    <button type="submit" class="btn btn-success btn-sm">View Balance</button>
										</form>
							        </c:when>
							</c:choose>
								
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