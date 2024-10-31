<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <title>Member Form</title>
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
    <h1 class="text-center mb-4">Member Registration Form</h1>
    <div class="row justify-content-center">
        <div class="col-lg-6">
            <f:form action="saveMember" method="POST" modelAttribute="member">
                
                <f:hidden path="memberId" id="memberId" value="${m.memberId}" class="form-control"  readonly="true"/>
                
                <%-- member attributes --%>
                <div class="form-group">
                    <label for="firstName">First Name:</label>
                    <f:input path="firstName" id="firstName" value="${m.firstName}" class="form-control" />
                    <f:errors path="firstName" cssClass="text-danger"/>
                </div>
                
                <div class="form-group">
                    <label for="lastName">Last Name:</label>
                    <f:input path="lastName" id="lastName" value="${m.lastName}" class="form-control" />
                    <f:errors path="lastName" cssClass="text-danger"/>
                </div>
                
                <div class="form-group">
                    <label for="addressLine1">Address Line 1:</label>
                    <f:input path="addressLine1" id="addressLine1" value="${m.addressLine1}" class="form-control" />
                    <f:errors path="addressLine1" cssClass="text-danger"/>
                </div>
                
                <div class="form-group">
                    <label for="addressLine2">Address Line 2:</label>
                    <f:input path="addressLine2" id="addressLine2" value="${m.addressLine2}" class="form-control" />
                    <f:errors path="addressLine2" cssClass="text-danger"/>
                </div>
                
                <div class="form-group">
                    <label for="phone">Phone:</label>
                    <f:input path="phone" id="phone" value="${m.phone}" class="form-control" />
                    <f:errors path="phone" cssClass="text-danger"/>
                </div>
                
                <div class="form-group">
                    <label for="email">Email</label>
                    <f:input path="email" type="email" id="email" value="${m.email}" class="form-control" />
                    <f:errors path="email" cssClass="text-danger"/>
                </div>
                
                <!-- Username -->
			    <div class="form-group">
			        <label for="username">Username:</label>
			        <input type="text" name="username" id="username" value="${m.user.username}" class="form-control" />
			        <c:if test="${not empty errorUsername}">
				        <div class="text-danger">${errorUsername}</div>
				    </c:if>
			        
			    </div>
			
			    <!-- User Password -->
			    <div class="form-group">
			        <label for="userPassword">Password:</label>
			        <input type="password" name="userPassword" id="userPassword" class="form-control" />
			        <c:if test="${not empty errorPassword}">
				        <div class="text-danger">${errorPassword}</div>
				    </c:if>
			        
			    </div>
                                
                <div class="form-group text-center">
                    <input type="submit" value="Submit" class="btn btn-primary" />
                </div>
            </f:form>
        </div>
    </div>
</div>
<sec:authorize access="hasAuthority('ADMIN')">
<div>
	<div class="row justify-content-center">
		<div>
			<div class="table-responsive ">
				<table class="table table-bordered table-striped">
					<thead class="thead-dark">
						<tr>
							<th><a href="${pageContext.request.contextPath}/memberForm?sortBy=memberId">Member Id</a></th>
							<th><a href="${pageContext.request.contextPath}/memberForm?sortBy=firstName"></a>Member Name</th>
							<th><a href="${pageContext.request.contextPath}/memberForm?sortBy=email">Member Email</a></th>
							<th><a href="${pageContext.request.contextPath}/memberForm?sortBy=addressLine1">Member Address</a></th>
							<th><a href="${pageContext.request.contextPath}/memberForm?sortBy=membershipDate">Membership Date</a></th>
							<th>Username</th>
							<th colspan="2">Actions</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${memberPage.getContent()}" var="m">
							<tr>
								<td>${m.getMemberId()}</td>
								<td>${m.getFirstName()} ${m.getLastName()}</td>
								<td>${m.getEmail()}</td>
								<td>${m.getAddressLine1()}</td>
								<td>${m.getMembershipDate()}</td>
								<td>${m.user.username}</td>
								
								
								<td><a href="updateMember?memberId=${m.getMemberId()}" class="btn btn-secondary btn-sm">Update</a></td>
								<td><a href="deleteMember?memberId=${m.getMemberId()}" class="btn btn-danger btn-sm">Delete</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				
				<!-- Pagination -->
		        <c:if test="${memberPage.getTotalPages() > 0}">
			        <nav aria-label="Page navigation">
					  <ul class="pagination justify-content-center">
					    <c:forEach
					      begin="0"
					      end="${memberPage.getTotalPages() - 1}"
					      var="i"
					    > 
					      <li class="page-item ${i == memberPage.number ? 'active' : ''}">
					        <a
					          class="page-link"
					          href="${pageContext.request.contextPath}/memberForm?page=${i}&size=${memberPage.size}&sortBy=${sortBy}"
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
</sec:authorize>
<sec:authorize access="hasAuthority('USER')">
<div>
	<div class="row justify-content-center">
		<div>
			<div class="table-responsive ">
				<table class="table table-bordered table-striped">
					<thead class="thead-dark">
						<tr>
							<th>Member Id</th>
							<th>Member Name</th>
							<th>Member Email</th>
							<th>Member Address</th>
							<th>Membership Date</th>
							<th>Username</th>
							<th colspan="2">Actions</th>
						</tr>
					</thead>
					<tbody>
						
						<tr>
							<td>${md.getMemberId()}</td>
							<td>${md.getFirstName()} ${m.getLastName()}</td>
							<td>${md.getEmail()}</td>
							<td>${md.getAddressLine1()}</td>
							<td>${md.getMembershipDate()}</td>
							<td>${md.user.username}</td>
							
							
							<td><a href="updateMember?memberId=${md.getMemberId()}" class="btn btn-secondary btn-sm">Update</a></td>
							<td><a href="deleteMember?memberId=${md.getMemberId()}" class="btn btn-danger btn-sm">Delete</a></td>
						</tr>
						
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
</sec:authorize>		
</body>
</html>