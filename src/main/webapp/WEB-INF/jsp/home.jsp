<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Library Management System</title>
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
	
	<div class="container text-center mt-4">
    
    <div class="container border rounded mt-5 p-4">
    <h3>Book Search</h3>
	    <form action="home" method="get">
	        <div class="form-row">
	            <div class="form-group col-md-4">
	                <label>Book Title</label>
	                <input class="form-control" type="text" id="bookTitle" name="bookTitle" value="${fTitle}" }/>
	            </div>
	            
	            <div class="form-group">
				    <label for="bookCategory">Category</label>
				    <select id="bookCategory" name="bookCategory" class="form-control">
				        <!-- Default option -->
				        <option value="" selected>Select a Category</option>
				        
				        <!-- Dynamically generated options -->
				        <c:forEach items="${categories}" var="cat">
				            <option value="${cat}" ${cat eq fCat ? 'selected' : ''}>${cat}</option>
				        </c:forEach>
				    </select>
				    <c:if test="${not empty errors['bookCategory']}">
				        <div class="text-danger">${errors['bookCategory']}</div>
				    </c:if>
				</div>
	       
	            <div class="form-group col-md-4">
	                <label>Author</label>
	                <input class="form-control" type="text" id="author" name="author" value="${fAuthor}"/>
	            </div>
	        </div>
	        <div>
	        <button type="submit" class="btn btn-primary mt-3">Search</button>
	        <a class="btn btn-danger mt-3" href="${pageContext.request.contextPath}/home">Reset</a>
	        </div>
	    </form>
	</div>

    <div align="center" class="mt-5">
        <h1>List of Books</h1>
        <table class="table table-striped table-bordered">
            <thead>
                <tr>
			        <th><a href="${pageContext.request.contextPath}/home?sortBy=bookId">Book Id</a></th>
			        <th><a href="${pageContext.request.contextPath}/home?sortBy=title">Title</a></th>
			        <th><a href="${pageContext.request.contextPath}/home?sortBy=isbn">ISBN</a></th>
			        <th><a href="${pageContext.request.contextPath}/home?sortBy=publicationDate">Publication Date</a></th>
			        <th><a href="${pageContext.request.contextPath}/home?sortBy=bookCategory">Category</a></th>
			        <th>Author(s)</th>
			        <th><a href="${pageContext.request.contextPath}/home?sortBy=publisher">Publisher</a></th>
			        <th><a href="${pageContext.request.contextPath}/home?sortBy=status">Status</a></th>
			        
					<th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${homePage.getContent()}" var="bk">
                    <tr>
                        <td>${bk.bookId}</td>
                        <td>${bk.title}</td>
                        <td>${bk.isbn}</td>
                        <td>${bk.publicationDate}</td>
                        <td>${bk.bookCategory}</td>
                		        
	               		<td>
						    <c:forEach items="${bk.bookAuthors}" var="author" varStatus="status">
						        ${author}<c:if test="${!status.last}">, </c:if>
						    </c:forEach>
						</td>
                        
                        <td>${bk.publisher.publisherName}</td>
                        <td>${bk.status}</td>
                        
                        <td>
                        
                        	<c:choose>
							        <c:when test="${bk.status != 'AVAILABLE'}">
							            <button class="btn btn-success disabled btn-sm" disabled>Borrow Book</button>
							        </c:when>
							        <c:otherwise>
							            <form action="${pageContext.request.contextPath}/transactionForm" method="POST">
										    <input type="hidden" name="bkId" value="${bk.bookId}" />
										    <input type="hidden" name="activeTab" value="borrowTab" />
										    <button type="submit" class="btn btn-success btn-sm">Borrow Book</button>
										</form>
							        </c:otherwise>
							</c:choose>

                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        
        <!-- Pagination -->
        <c:if test="${homePage.getTotalPages() > 0}">
	        <nav aria-label="Page navigation">
			  <ul class="pagination justify-content-center">
			    <c:forEach
			      begin="0"
			      end="${homePage.getTotalPages() - 1}"
			      var="i"
			    > 
			      <li class="page-item ${i == homePage.number ? 'active' : ''}">
			        <a
			          class="page-link"
			          href="${pageContext.request.contextPath}/home?page=${i}&size=${homePage.size}&sortBy=${sortBy}&bookTitle=${sTitle}&bookCategory=${bookCategory}&author=${sAuthor}"
			          >${i + 1}</a
			        >
			      </li>
			    </c:forEach>
			  </ul>
			</nav>
		</c:if>
    </div>
</div>

	
</body>
</html>