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
	$scope.dependencies1 = [];
	$scope.dependencies2 = [];
	
	$scope.onChangeVersion = function(plugin) {
		if (!plugin.selectedVersion.dependencies) {
			plugin.selectedVersion.dependencies = plugin.selectedVersion.getDependencies();
			plugin.showScript = false;
		}
	};
	
	$scope.onAddDependency1 = function(version) {
		$scope.dependencies1.push(version);
	};
	
	$scope.onAddDependency2 = function(version) {
		$scope.dependencies2.push(version);
	};
	
	$scope.reloadPluginList = function() {
		$scope.plugins = RMMV.PluginBase.Web.getPluginBases();
		for (var i = 0; i < $scope.plugins.length; i++) {
			var plugin = $scope.plugins[i];
			plugin.versions = plugin.getVersions();
			if (plugin.versions && plugin.versions.length > 0) {
				var latest = plugin.versions.length - 1;
				plugin.latestVersion = plugin.versions[latest];
				plugin.selectedVersion = plugin.latestVersion;
				plugin.selectedVersion.dependencies = plugin.selectedVersion.getDependencies();
			}
		}
	};

	$scope.toggleScript = function(plugin) {
		if (!plugin.selectedVersion) {
			plugin.showScript = false;
			return;
		}
		
		if (!plugin.script) {
			plugin.script = plugin.selectedVersion.getScript();
		}
		
		if (!plugin.showScript) {
			plugin.showScript = true;
		} else {
			plugin.showScript = false;
		}
	};
	$scope.loadScript = function($fileContent) {
		$scope.fileContent = $fileContent;
	};
	
	$scope.createPlugin = function(base, pluginVersion, dependencies) {
		// Create the plugin and save it.
		newPlugin = RMMV.PluginBase.create(base);
		newPluginVersion = RMMV.Plugin.create(pluginVersion);
		newPluginVersion.script = $scope.fileContent;
		
		if (!newPluginVersion.script) {
			return;
		}
		
		// Create new plugin and new initial version
		newPlugin = RMMV.PluginBase.Web.createPluginBase(newPlugin);
		newPluginVersion = newPlugin.addVersion(newPluginVersion);
		
		// Add dependencies if there are any.
		if (dependencies) {
			var newDependencies = [];
			for (var i = 0; i < dependencies.length; i++) {
				var newDependency = RMMV.Plugin.create(dependencies[i]);
				newDependencies.push(newDependency);
			}
			newPluginVersion.addDependencies(newDependencies);
		}
		
		// Clear dependencies list
		$scope.dependencies1 = [];
		$scope.dependencies2 = [];
		
		// Go back to view-plugins page.
		$scope.page.view = "view-plugins";
		$scope.reloadPluginList();
	};
	
	$scope.createPluginVersion = function(base, pluginVersion, dependencies) {
		// Create the new plugin version.
		var newPluginVersion = RMMV.Plugin.create(pluginVersion);
		var pluginBase = RMMV.PluginBase.create(base);
		newPluginVersion.script = $scope.fileContent;
		if (!newPluginVersion.script) {
			return;
		}
		
		newPluginVersion = pluginBase.addVersion(newPluginVersion);
		
		// Add dependencies if there are any.
		if (dependencies) {
			var newDependencies = [];
			for (var i = 0; i < dependencies.length; i++) {
				var newDependency = RMMV.Plugin.create(dependencies[i]);
				newDependencies.push(newDependency);
			}
			newPluginVersion.addDependencies(newDependencies);
		}
		
		// Clear dependencies list
		$scope.dependencies1 = [];
		$scope.dependencies2 = [];
		
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