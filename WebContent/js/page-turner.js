angular.module("deus.pageTurner", [])
	.directive("pageTurner", function() {
		return {
			restrict: "E",
			scope: {
				page: "=page",
				pageSize: "=pagesize",
				recordCount: "=recordcount",
				callback: "&onpagechange"
			},
			controller: function($scope) {
				var callback = $scope.callback();
				
				$scope.$watch('recordCount', function () {				
					$scope.pageCount = Math.ceil($scope.recordCount/$scope.pageSize);
				}, true);
				
				$scope.changePageSize = function(pageSize) {
					if (pageSize > 0) {
						callback(1, pageSize);
					}
				};
				$scope.nextPage = function() {
					if ($scope.hasNext()) {
						callback(++$scope.page, $scope.pageSize);
					}
				};
				$scope.prevPage = function() {
					if ($scope.hasPrev()) {
						callback(--$scope.page, $scope.pageSize);
					}
				};
				$scope.goToPage = function(pageNumber) {
					if ($scope.page == pageNumber) {
						return;
					}
					$scope.page = pageNumber;
					callback($scope.page, $scope.pageSize);
				};
				$scope.hasPrev = function() {
					return $scope.page > 1;
				};
				$scope.hasNext = function() {
					return $scope.page < $scope.pageCount;
				};
				$scope.range = function(min, max, step){
				    step = step || 1;
				    var input = [];
				    for (var i = min; i <= max; i += step) input.push(i);
				    return input;
				};
			},
			templateUrl: "templates/page-turner.html"
		};
});