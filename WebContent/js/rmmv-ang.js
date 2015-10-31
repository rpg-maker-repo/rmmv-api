var app = angular.module("rmmv-ui", ["ngCookies", "ngRoute", "ngResource"]);

app.directive('onReadFile', function ($parse) {
	return {
		restrict: 'A',
		scope: false,
		link: function(scope, element, attrs) {
            var fn = $parse(attrs.onReadFile);
            
			element.on('change', function(onChangeEvent) {
				var reader = new FileReader();
                
				reader.onload = function(onLoadEvent) {
					scope.$apply(function() {
						fn(scope, {$fileContent:onLoadEvent.target.result});
					});
				};

				reader.readAsText((onChangeEvent.srcElement || onChangeEvent.target).files[0]);
			});
		}
	};
});

app.controller('page-controller', function($scope) {
	$scope.page = {};
	$scope.page.view = "view-plugins";
	$scope.plugins = [];
	
	$scope.reloadPluginList = function() {
		$scope.plugins = RMMV.Web.getPlugins();
		for (var i = 0; i < $scope.plugins.length; i++) {
			var plugin = $scope.plugins[i];
			plugin.dependencies = plugin.getDependencies();
		}
	};

	$scope.toggleScript = function(plugin) {
		if (!plugin.script) {
			plugin.script = plugin.getScript();
		}
		
		if (!plugin.showScript) {
			plugin.showScript = true;
		} else {
			plugin.showScript = false;
		}
	};
	$scope.loadScript = function(plugin, $fileContent) {
		$scope.fileContent = $fileContent;
	};
	
	$scope.createPlugin = function(newPlugin, dependencyIds) {
		// Create the plugin and save it.
		plugin = RMMV.Plugin.create(newPlugin);
		plugin.script = $scope.fileContent;
		if (!plugin.script) {
			return;
		}
		plugin = RMMV.Web.createPlugin(plugin);
		
		// Add depdencies if there are any.
		if (dependencyIds) {
			var dependencies = [];
			for (var i = 0; i < dependencyIds.length; i++) {
				var dependency = RMMV.Plugin();
				dependency.id = dependencyIds[i];
				dependencies.push(dependency);
			}
			plugin.addDependencies(dependencies);
		}
		
		// Go back to view-plugins page.
		$scope.page.view = "view-plugins";
		$scope.reloadPluginList();
	};
	
	$scope.clearForm = function(formId) {
		$(':input', formId)
		.not(':button, :submit, :reset, :hidden')
		.val('')
		.removeAttr('checked')
		.removeAttr('selected');
	}
	
	$scope.reloadPluginList();
});