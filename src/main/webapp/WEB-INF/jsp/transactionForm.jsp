<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <title>Transaction Form</title>
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
		<c:if test="${not empty message}">
	        <div class="alert alert-success text-center" role="alert">
	            ${message}
	        </div>
	    </c:if>
	    <c:if test="${not empty messageFail}">
	        <div class="alert alert-danger text-center" role="alert">
	            ${messageFail}
	        </div>
	    </c:if>
    
	    <!-- Nav tabs -->
		<ul class="nav nav-tabs" id="transactionTabs">
		    <li class="nav-item">
		        <a class="nav-link ${activeTab eq 'borrowTab' ? 'active' : ''}" data-toggle="tab" href="#borrowTab">Borrow</a>
		    </li>
		    <li class="nav-item">
		        <a class="nav-link ${activeTab eq 'returnTab' ? 'active' : ''}" data-toggle="tab" href="#returnTab">Return</a>
		    </li>
		    <li class="nav-item">
		        <a class="nav-link ${activeTab eq 'payfineTab' ? 'active' : ''}" data-toggle="tab" href="#payfineTab">Pay Fine</a>
		    </li>
		</ul>
		
		<!-- Tab panes -->
		<div class="tab-content">
		    <!-- Borrow Tab -->
		    <div id="borrowTab" class="tab-pane ${activeTab eq 'borrowTab' ? 'active' : ''}"><br>
		        <h3>Borrow a Book</h3>
		        <f:form action="${pageContext.request.contextPath}/borrowBook" method="post" modelAttribute="transaction">
		            <div class="form-group">
		                <label for="bookId">Book ID:</label>
		                <f:input path="bookId" type="text" class="form-control" id="bookId" value="${borrowId}" name="bookId" placeholder="Enter Book ID" />
		                <c:if test="${not empty bookError}">
					        <div class="text-danger">${bookError}</div>
					    </c:if>
		            </div>
		            <div class="form-group">
		                <label for="borrowDate">Borrow Date:</label>
		                <f:input path="transactionDate" type="date" class="form-control" id="borrowDate" value="${transactionDate}" name="borrowDate" />
		                <f:errors path="transactionDate" cssClass="text-danger"/>
		            </div>
		            <button type="submit" class="btn btn-primary">Submit Borrow</button>
		        </f:form>
		    </div>
		
		    <!-- Return Tab -->
		    <div id="returnTab" class="tab-pane ${activeTab eq 'returnTab' ? 'active' : ''}"><br>
		        <h3>Return a Book</h3>
		        <f:form action="${pageContext.request.contextPath}/returnBook" method="post" modelAttribute="transaction">
		            <div class="form-group">
		                <label for="bookIdReturn">Book ID:</label>
		                <f:input path="bookId" type="text" class="form-control" id="bookIdReturn" value="${returnId}" name="bookIdReturn" placeholder="Enter Book ID" />
		                <c:if test="${not empty bookError}">
					        <div class="text-danger">${bookError}</div>
					    </c:if>
		            </div>
		            <div class="form-group">
		                <label for="returnDate">Return Date:</label>
		                <f:input path="transactionDate" type="date" class="form-control" id="returnDate" value="${transactionDate}" name="returnDate" />
		                <f:errors path="transactionDate" cssClass="text-danger"/>
		            </div>
		            <button type="submit" class="btn btn-primary">Submit Return</button>
		        </f:form>
		    </div>
		    
		    <!-- Return Tab -->
		    <div id="renewTab" class="tab-pane ${activeTab eq 'renewTab' ? 'active' : ''}"><br>
		        <h3>Return a Book</h3>
		        <f:form action="${pageContext.request.contextPath}/renewBook" method="post" modelAttribute="transaction">
		            <div class="form-group">
		                <label for="bookIdRenew">Book ID:</label>
		                <f:input path="bookId" type="text" class="form-control" id="bookIdRenew" value="${renewId}" name="bookIdRenew" placeholder="Enter Book ID" />
		                <c:if test="${not empty bookError}">
					        <div class="text-danger">${bookError}</div>
					    </c:if>
		            </div>
		            <div class="form-group">
		                <label for="renewDate">Renew Date:</label>
		                <f:input path="transactionDate" type="date" class="form-control" id="renewDate" value="${transactionDate}" name="renewDate" />
		                <f:errors path="transactionDate" cssClass="text-danger"/>
		            </div>
		            <button type="submit" class="btn btn-primary">Submit Renewal</button>
		        </f:form>
		    </div>
		
		    <!-- Pay Fine Tab -->
		    <div id="payfineTab" class="tab-pane ${activeTab eq 'payFineTab' ? 'active' : ''}"><br>
		        <h3>Pay Fine</h3>
		        <f:form action="${pageContext.request.contextPath}/payFine" method="post" modelAttribute="transaction">
		            <c:if test="${not empty totalFine}">
		                <p>Fine Due: $${totalFine}</p>
		            </c:if>
		            <c:if test="${not empty errorFine}">
		                <p>${errorFine}</p>
		            </c:if>
		            <div class="form-group">
		                <label for="amount">Fine Amount:</label>
		                <f:input path="amount" type="number" class="form-control" id="amount" name="amount" step="0.01" placeholder="Enter Fine Amount" />
		                <c:if test="${not empty amountError}">
					        <div class="text-danger">${amountError}</div>
					    </c:if>
		            </div>
		            <div class="form-group">
		                <label for="fineDate">Fine Date:</label>
		                <f:input path="transactionDate" type="date" class="form-control" id="transactionDate" value="${transactionDate}" name="transactionDate" />
		                <f:errors path="transactionDate" cssClass="text-danger"/>
		            </div>
		            <button type="submit" class="btn btn-primary">Submit Fine Payment</button>
		        </f:form>
		    </div>
		</div>
	</div>
</body>
</html>