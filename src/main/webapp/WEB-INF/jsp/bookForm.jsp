<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <title>Book Form</title>
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
    <h1 class="text-center mb-4">Book Registration Form</h1>
    <div class="row justify-content-center">
        <div class="col-lg-6">
            <f:form action="saveBook" method="POST" modelAttribute="book">
                <div class="form-group">
                    <label for="bookId">Book Id:</label>
                    <f:input path="bookId" id="bookId"  value="${b.bookId}" class="form-control" disabled="true"/>
                </div>
                
                <div class="form-group">
                    <label for="title">Title:</label>
                    <f:input path="title" id="title" value="${b.title}" class="form-control" />
                    <f:errors path="title" cssClass="text-danger"/>
                </div>
                
                <div class="form-group">
                    <label for="isbn">ISBN:</label>
                    <f:input path="isbn" id="isbn" value="${b.isbn}" class="form-control" />
                    <f:errors path="isbn" cssClass="text-danger"/>
                </div>
                
                <div class="form-group">
                    <label for="publicationDate">Publication Date:</label>
                    <f:input type="date" path="publicationDate" id="publicationDate" value="${b.publicationDate}" class="form-control" />
                    <f:errors path="publicationDate" cssClass="text-danger"/>
                </div>
                
                <%-- need book category, select authors, select publisher --%>
				<div class="form-group">
				    <label for="bookCategory">Book Categories: </label>
				    <f:select id="bookCategory" path="bookCategory" class="form-control">
				        <c:forEach items="${categories}" var="c">
			                <c:choose>
			                    <c:when test="${b.bookCategory == c}">
			                        <f:option value="${c}" selected="selected">${c}</f:option>
			                    </c:when>
			                    <c:otherwise>
			                        <f:option value="${c}"> ${c}</f:option>
			                    </c:otherwise>
			                </c:choose>
				        </c:forEach>
				    </f:select>
				    <f:errors path="bookCategory" cssClass="text-danger"/>
				</div>
				
	 			<div class="form-group">
                   <label for="publisher">Publishers</label>
                   <f:select id="publisher" path="publisher" class="form-control">
                       <c:forEach items="${publishers}" var="p">
                           <c:choose>
                               <c:when test="${p.publisherId == b.publisher.publisherId}">
                                   <f:option value="${p.publisherId}" selected="selected">${p.publisherName}</f:option>
                               </c:when>
                               <c:otherwise>
                                   <f:option value="${p.publisherId}">${p.publisherName}</f:option>
                               </c:otherwise>
                           </c:choose>
                       </c:forEach>
                   </f:select>
                   <f:errors path="publisher" cssClass="text-danger"/>
                </div>
                
                <%-- add multiple authors  --%>
                <div class="form-group">
				    <label>Authors: </label>
				   
				    <div class="input-group mb-3">
				        <input id="authorInput" type="text" class="form-control" placeholder="Enter Author Name">
				        <div class="input-group-append">
				            <button class="btn btn-success add-author" type="button">Add Author</button>
				        </div>
				    </div>
				
				    <!-- Container for added authors -->
				    <div id="authorContainer">
				    	<c:forEach items="${b.bookAuthors}" var="author">
				            <div class="input-group mb-3">
				                <input type="text" name="bookAuthors" class="form-control" value="${author}" readonly>
				                <div class="input-group-append">
				                    <button class="btn btn-danger remove-author" type="button">Remove</button>
				                </div>
				            </div>
				        </c:forEach>
				    </div>
				    <f:errors path="bookAuthors" cssClass="text-danger"/>
				</div>
                
                
                <div class="form-group text-center">
                    <input type="submit" value="Submit" class="btn btn-primary" />
                </div>
            </f:form>
        </div>
    </div>
</div>
	
<div>
	<h2 class="text-center mt-5">List of Books</h2>
	<div class="row justify-content-center">
		<div class="col-lg-8">
			<div class="table-responsive ">
				<table class="table table-bordered table-striped">
					<thead class="thead-dark">
						<tr>
							<th><a href="${pageContext.request.contextPath}/bookForm?sortBy=bookId">Book Id</a></th>
							<th><a href="${pageContext.request.contextPath}/bookForm?sortBy=title">Title</a></th>
							<th><a href="${pageContext.request.contextPath}/bookForm?sortBy=isbn">ISBN</a></th>
							<th><a href="${pageContext.request.contextPath}/bookForm?sortBy=publicationDate">Publication Date</a></th>
							<th><a href="${pageContext.request.contextPath}/bookForm?sortBy=bookCategory">Book Category</a></th>
							<th><a href="${pageContext.request.contextPath}/bookForm?sortBy=publisher">Publisher</a></th>
							<th>Authors</th>
							
							<th colspan="2">Actions</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${bookPage.getContent()}" var="b">
							<tr>
								<td>${b.getBookId()}</td>
								<td>${b.getTitle()}</td>
								<td>${b.getIsbn()}</td>
								<td>${b.getPublicationDate()}</td>
								<td>${b.getBookCategory()}</td>
								<td>${b.publisher.publisherName}</td>
								<td>
									<c:forEach var="author" items="${b.getBookAuthors()}" varStatus="status">
								        ${author}<c:if test="${!status.last}">, </c:if>
								    </c:forEach>
								</td>
								
								<td><a href="updateBook?bookId=${b.getBookId()}" class="btn btn-secondary btn-sm">Update</a></td>
								<td><a href="deleteBook?bookId=${b.getBookId()}" class="btn btn-danger btn-sm">Delete</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				
				<!-- Pagination -->
		        <c:if test="${bookPage.getTotalPages() > 0}">
			        <nav aria-label="Page navigation">
					  <ul class="pagination justify-content-center">
					    <c:forEach
					      begin="0"
					      end="${bookPage.getTotalPages() - 1}"
					      var="i"
					    > 
					      <li class="page-item ${i == bookPage.number ? 'active' : ''}">
					        <a
					          class="page-link"
					          href="${pageContext.request.contextPath}/bookForm?page=${i}&size=${bookPage.size}&sortBy=${sortBy}"
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
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    $(document).ready(function(){
    
        $('.add-author').click(function(){
            var authorName = $('#authorInput').val().trim();  
			console.log(authorName)
            // Check if the input is not empty
            if (authorName) {
            	// create the input for the author and the remove button
                var authorHtml = `
                    <div class="input-group mb-3">
                        <input type="text" name="bookAuthors" class="form-control" readonly>
                        <div class="input-group-append">
                            <button class="btn btn-danger remove-author" type="button">Remove</button>
                        </div>
                    </div>`;
                
                // Append the new author input to the container
                $('#authorContainer').append(authorHtml);

                // Set the value of the last added input field to authorName
                $('#authorContainer .input-group:last-child input').val(authorName);

                // Clear the input field after adding
                $('#authorInput').val('');
            }
        });

        // Remove author 
        $(document).on('click', '.remove-author', function(){
            var authorToRemove = $(this).closest('.input-group').find('input').val();
            $(this).closest('.input-group').remove(); 
        });
    });
</script>
		
</body>
</html>