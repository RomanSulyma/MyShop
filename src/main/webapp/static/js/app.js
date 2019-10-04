;$(function(){
	/* initialize function while page loading */
	var init = function (){
		initBuyBtn();
		$('#addToCart').click(addProductToCart);
		$('#addProductPopup .count').change(calculateCost);
		$('#loadMore').click(loadMoreProducts);
		initSearchForm();
		$('#goSearch').click(goSearch);
		$('.remove-product').click(removeProductFromCart);
		$('.post-request').click(function(){
			postRequest($(this).attr('data-url'));
		});
		$(window).on('load', function () {
			var $preloader = $('#p_prldr'),
				$svg_anm = $preloader.find('.svg_anm');
			$svg_anm.fadeOut();
			$preloader.delay(1000).fadeOut('slow');
		});
	};
	/* animate background */

 /* /initialize function while page loading */

 /* popup function */
	var showAddProductPopup = function (){
		var idProduct = $(this).attr('data-id-product');
		var product = $('#product'+idProduct);
		$('#addProductPopup').attr('data-id-product', idProduct);
		$('#addProductPopup .product-image').attr('src', product.find('.thumbnail img').attr('src'));
		$('#addProductPopup .name').text(product.find('.name').text());
		var price = product.find('.price').text();
		$('#addProductPopup .price').text(price);
		$('#addProductPopup .category').text(product.find('.category').text());
		$('#addProductPopup .producer').text(product.find('.producer').text());
		$('#addProductPopup .count').val(1);
		$('#addProductPopup .cost').text(price);
		$('#addToCart').removeClass('hidden');
		$('#addToCartIndicator').addClass('hidden');
		$('#addProductPopup').modal({
			show:true
		});
	};
	 /* /popup function */

	/* post request to sign-out*/
	var postRequest = function(url){
		var form = '<form id="postRequestForm" action="'+url+'" method="post"></form>';
		$('body').append(form);
		$('#postRequestForm').submit();
	};
	/* /post request to sign-out*/

 /* popup show function */
	var initBuyBtn = function(){
		$('.buy-btn').click(showAddProductPopup);
	};
/* /popup show function */

	var addProductToCart = function (){
		var idProduct = $('#addProductPopup').attr('data-id-product');
		var count = $('#addProductPopup .count').val();
		var btn = $('#addToCart');
		convertButtonToLoader(btn, 'btn-primary');
		$.ajax({
			url : '/ajax/json/product/add',
			method : 'POST',
			data : {
				idProduct : idProduct,
				count : count
			},
			success : function(data) {
				$('#currentShoppingCart .total-count').text(data.totalCount);
				$('#currentShoppingCart .total-cost').text(data.totalCost);
				$('#currentShoppingCart').removeClass('hidden');
				$('#addProductPopup').modal('hide');
				convertLoaderToButton(btn, 'btn-primary', addProductToCart);
			},
			error : function(xhr) {
				convertLoaderToButton(btn, 'btn-primary', addProductToCart);
				if(xhr.status == 400) {
					alert(xhr.responseJSON.message);
				}else {
					alert('Error');
				}
			}
		});
	};

/* /adding product to cart function */

 /* calculating cost in popup */
	var calculateCost = function(){
		var priceStr = $('#addProductPopup .price').text();
		var price = parseFloat(priceStr.replace('$',' '));
		var count = parseInt($('#addProductPopup .count').val());
		var min = parseInt($('#addProductPopup .count').attr('min'));
		var max = parseInt($('#addProductPopup .count').attr('max'));
		if(count >= min && count <= max) {
			var cost = price * count;
			$('#addProductPopup .cost').text('$ '+cost);
		} else {
			$('#addProductPopup .count').val(1);
			$('#addProductPopup .cost').text(priceStr);
		}
	};
	 /* /calculating cost in popup */

	/* turns a button into ajax resource loader*/
	var convertButtonToLoader = function (btn, btnClass) {
		btn.removeClass(btnClass);
		btn.removeClass('btn');
		btn.addClass('load-indicator');
		var text = btn.text();
		btn.text('');
		btn.attr('data-btn-text', text);
		btn.off('click');
	};
	/* turns a button into ajax resource loader*/
	var convertLoaderToButton = function (btn, btnClass, actionClick) {
		btn.removeClass('load-indicator');
		btn.addClass('btn');
		btn.addClass(btnClass);
		btn.text(btn.attr('data-btn-text'));
		btn.removeAttr('data-btn-text');
		btn.click(actionClick);
	};
	/* load more products button function*/
	/* turns the button into a loader and executes an ajax request and finally turns the loader back into a button*/
	var loadMoreProducts = function (){
		var btn = $('#loadMore');
		convertButtonToLoader(btn, 'btn-success');
		var pageNumber = parseInt($('#productList').attr('data-page-number'));
		var url = '/ajax/html/more' + location.pathname + '?page=' + (pageNumber + 1) + '&' + location.search.substring(1);
		$.ajax({
			url : url,
			success : function(html) {
				$('#productList .row').append(html);
				pageNumber++;
				var pageCount = parseInt($('#productList').attr('data-page-count'));
				$('#productList').attr('data-page-number', pageNumber);
				if(pageNumber < pageCount) {
					convertLoaderToButton(btn, 'btn-success', loadMoreProducts);
				} else {
					btn.remove();
				}
				initBuyBtn();
			},
			error : function(data) {
				convertLoaderToButton(btn, 'btn-success', loadMoreProducts);
				alert('Error');
			}
		});
	};
	/* /turns the button into a loader and executes an ajax request and finally turns the loader back into a button*/

	/* load more orders by AJAX request */
	var loadMoreMyOrders = function (){
		var btn = $('#loadMoreMyOrders');
		convertButtonToLoader(btn, 'btn-success');
		var pageNumber = parseInt($('#myOrders').attr('data-page-number'));
		var url = '/ajax/html/more/my-orders?page=' + (pageNumber + 1);
		$.ajax({
			url : url,
			success : function(html) {
				$('#myOrders tbody').append(html);
				pageNumber++;
				var pageCount = parseInt($('#myOrders').attr('data-page-count'));
				$('#myOrders').attr('data-page-number', pageNumber);
				if (pageNumber < pageCount) {
					convertLoaderToButton(btn, 'btn-success', loadMoreMyOrders);
				} else {
					btn.remove();
				}
			},
			error : function(xhr) {
				convertLoaderToButton(btn, 'btn-success', loadMoreMyOrders);
				if (xhr.status == 401) {
					window.location.href = '/sign-in';
				} else {
					alert('Error');
				}
			}
		});
	};

	/* /load more orders by AJAX request */

	/* checkbox verifying function*/
	var initSearchForm = function (){
		$('#allCategories').click(function(){
			$('.categories .search-option').prop('checked', $(this).is(':checked'));
		});
		$('.categories .search-option').click(function(){
			$('#allCategories').prop('checked', false);
		});
		$('#allProducers').click(function(){
			$('.producers .search-option').prop('checked', $(this).is(':checked'));
		});
		$('.producers .search-option').click(function(){
			$('#allProducers').prop('checked', false);
		});
		$('#allPrices').click(function(){
			$('.prices .search-option').prop('checked', $(this).is(':checked'));
		});
		$('.prices .search-option').click(function(){
			$('#allPrices').prop('checked', false);
		});
	};
	/* /checkbox verifying function*/

	/* button search and submit to server function*/
	var goSearch = function(){
		var isAllSelected = function(selector) {
			var unchecked = 0;
			$(selector).each(function(index, value) {
				if(!$(value).is(':checked')) {
					unchecked ++;
				}
			});
			return unchecked === 0;
		};
		if(isAllSelected('.categories .search-option')) {
			$('.categories .search-option').prop('checked', false);
		}
		if(isAllSelected('.producers .search-option')) {
			$('.producers .search-option').prop('checked', false);
		}
		if(isAllSelected('.prices .search-option')) {
			$('.prices .search-option').prop('checked', false);
		}
		$('form.search').submit();
	};
	/* /button search and submit to server function*/

	/* confirm message function*/
	var confirm = function (msg, okFunction) {
		if(window.confirm(msg)) {
			okFunction();
		}
	};
	/* /confirm message function*/

	/* remove product message confirm*/
	var removeProductFromCart = function (){
		var btn = $(this);
		confirm('Are you sure?', function(){
			executeRemoveProduct(btn);
		});
	};
	/* /remove product message confirm*/

	/* refresch total cost while removed product*/
	var refreshTotalCost = function () {
		var total = 0;
		$('#shoppingCart .item').each(function(index, value) {
			var count = parseInt($(value).find('.count').text());
			var price = parseFloat($(value).find('.price').text().replace('$', ' '));
			var val = price * count;
			total = total + val;
		});
		$('#shoppingCart .total').text('$'+total);
	};
	/* /refresch total cost while removed product*/

	/* remove product from cart and indicator state*/
	var executeRemoveProduct = function (btn) {
		var idProduct = btn.attr('data-id-product');
		var count = btn.attr('data-count');
		convertButtonToLoader(btn, 'btn-danger');

		$.ajax({
			url : '/ajax/json/product/remove',
			method : 'POST',
			data : {
				idProduct : idProduct,
				count : count
			},
			success : function(data) {
				if (data.totalCount == 0) {
					window.location.href = '/products';
				} else {
					var prevCount = parseInt($('#product' + idProduct + ' .count').text());
					var remCount = parseInt(count);
					if (remCount >= prevCount) {
						$('#product' + idProduct).remove();
					} else {
						convertLoaderToButton(btn, 'btn-danger', removeProductFromCart);
						$('#product' + idProduct + ' .count').text(prevCount - remCount);
						if(prevCount - remCount == 1) {
							$('#product' + idProduct + ' a.remove-all').remove();
						}
					}
					refreshTotalCost();
				}
			},
			error : function(data) {
				convertLoaderToButton(btn, 'btn-danger', removeProductFromCart);
				alert('Error');
			}
		});
	}
	/* /remove product from cart and indicator state*/




	init();
});