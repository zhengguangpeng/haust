<%@ page contentType="text/html;charset=UTF-8"%>
<!--每个页面的分页-->
	<div class="pageDiv">
			<ul style="text-align: center; ">
				
				<span class="pageLink"> <a class="page" href="javascript:topage('1')" >首页</a> 
					 <c:if test="${page.currentpage>1}">
        				<a class="page" href="javascript:topage('${page.currentpage-1}')" >
        					上一页 
        				</a>
        			</c:if>        
					<c:if test="${page.currentpage<=1}">
							<a class="page" href="javascript:topage('1')" >上一页</a> 
					</c:if>
					<c:choose>
						<c:when test="${!empty PageNum}">
							<c:forEach items="${PageNum}" var="row">
								<c:if test="${page.pages * 5 < row && page.pages * 5 + 5 >= row}">
									<c:if test="${page.currentpage == row}">
										<a class="active" href="javascript:topage('${row}')">${row}</a>
									</c:if>
									<c:if test="${page.currentpage != row}">
										<a href="javascript:topage('${row}')">${row}</a>
									</c:if>
								</c:if>
							</c:forEach>
						</c:when>
					</c:choose>
					 <c:if test="${page.currentpage<page.totalpage}">
	   					<a class="page" href="javascript:topage('${page.currentpage+1}')" >
	   						下一页
	   					</a>
	   				</c:if>
	   				<c:if test="${page.currentpage>=page.totalpage}">
	   						<a class="page" href="javascript:topage('${page.totalpage}')" >下一页 </a>
	   				</c:if>
					 <a class="page"  href="javascript:topage('${page.totalpage}')">末页</a> </span>
				<span>共${ResultNum} 条</span>
			</ul>
	</div>