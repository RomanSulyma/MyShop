<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>

<div class="visible-xs-block xs-option-container">
            <a class="pull-right" data-toggle="collapse" href="#productCatalog">Product catalog <span class="caret"></span></a>
            <a data-toggle="collapse" href="#findProducts">Find products <span class="caret"></span></a>
          </div>
          <!-- Search form -->
          <form class="search" action="/search">
            <div id="findProducts" class="panel panel-primary collapse">
              <div class="panel-heading">Find products</div>
              <div class="panel-body ">
                <div class="input-group">
                  <!--Search button -->
                  <input type="text" name="query" class="form-control" placeholder="Search query" value="${searchForm.query}">
                  <span class="input-group-btn">
                    <a id="goSearch" class="btn btn-default">Go!</a>
                  </span>
                </div>
                <!--/Search button -->
                <div class="more-options">
                  <a data-toggle="collapse" href="#searchOptions">More filters <span class="caret"></span></a>
                </div>
              </div>
              <div id="searchOptions" class="collapse ${searchForm.categoriesNotEmpty or searchForm.producersNotEmpty or searchForm.priceNotEmpty ? 'in' : '' }">
                <!--Category filters-->
                <div class="panel-heading">Category filters</div>
                <div class="panel-body categories">
                  <label><input type="checkbox" id="allCategories"> All</label>
                  <c:forEach var="category" items="${CATEGORY_LIST}">
                    <div class="form-group">
                      <div class="checkbox">
                        <label><input type="checkbox" name="category" value="${category.id }"  ${searchForm.categories.contains(category.id) ? 'checked' : '' } class="search-option" >
                            ${category.name } (${category.productCount })
                        </label>
                      </div>
                    </div>
                  </c:forEach>
                </div>
                <!--/Category filters-->
                <!--Producers filters-->
                <div class="panel-heading">Producer filters</div>
                <div class="panel-body producers">
                  <label><input type="checkbox" id="allProducers"> All</label>
                  <c:forEach var="producer" items="${PRODUCER_LIST}">
                    <div class="form-group">
                      <div class="checkbox">
                        <label><input type="checkbox" name="producer" value="${producer.id }" ${producer.id }" ${searchForm.producers.contains(producer.id) ? 'checked' : '' } class="search-option" >
                            ${producer.name }(${producer.productCount })
                        </label>
                      </div>
                    </div>
                  </c:forEach>
                </div>
                <!--/Producers filters-->
                <!--Price filters-->
                <div class="panel-heading">Price filters</div>
                <div class="panel-body prices">
                  <label> <input type="checkbox" id="allPrices"> All </label>
                  <div class="form-group">
                    <div class="checkbox">
                      <label><input type="checkbox" name="price_value" value="2000" class="search-option" ${searchForm.price.contains(Integer.parseInt(2000)) ? 'checked' : ''}>&lt2000</label>
                    </div>
                  </div>
                  <div class="form-group">
                    <div class="checkbox">
                      <label><input type="checkbox" name="price_value" value="3000" class="search-option" ${searchForm.price.contains(Integer.parseInt(3000)) ? 'checked' : ''}>&lt3000</label>
                    </div>
                  </div>
                  <div class="form-group">
                    <div class="checkbox">
                      <label><input type="checkbox" name="price_value" value="4000" class="search-option" ${searchForm.price.contains(Integer.parseInt(4000)) ? 'checked' : ''}>&lt4000</label>
                    </div>
                  </div>
                  <div class="form-group">
                    <div class="checkbox">
                      <label><input type="checkbox" name="price_value" value="5000" class="search-option" ${searchForm.price.contains(Integer.parseInt(5000)) ? 'checked' : ''}>&lt5000</label>
                    </div>
                  </div>
                </div>
                <!--/Price filters-->
              </div>
            </div>
          </form>
          <!-- /Search form -->
          <!-- Categories , product catalog menu  -->
          <div id="productCatalog" class="panel panel-primary collapse">
            <div class="panel-heading">Product catalog</div>
              <div class="list-group">
                <c:forEach var="cat" items="${CATEGORY_LIST}">
                <a href="/products${cat.url}" class="list-group-item ${selectedCategoryUrl eq cat.url ? 'active' : '' }"> <span class="badge">${cat.productCount}</span> ${cat.name}</a>
                </c:forEach>
              </div>
          </div>
          <!-- /Categories , product catalog menu  -->
