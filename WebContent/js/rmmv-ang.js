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

app.controller('page-controller', function($scope, $cookies) {
	$scope.page = {};
	$scope.page.view = "view-plugins";
	$scope.plugins = [];
	$scope.newPluginVersion = {};
	$scope.newPluginVersion.dependencies = [];
	$scope.newPluginVersion.selectedDependency = {};
	$scope.newPluginVersion.selectedVersion = "";
	$scope.newPlugin = {};
	$scope.newPlugin.dependencies = [];
	$scope.newPlugin.selectedDependency = {};
	$scope.newPlugin.selectedVersion = "";
	$scope.newPlugin.initialPluginVersion = {};
	$scope.newPlugin.initialPluginVersion.dependencies = [];
	$scope.authentication = {username: "", password: ""};
	$scope.accessToken = {};
	$scope.hasDeveloper = false;
	$scope.hasSuperUser = false;
	$scope.isAuthenticated = false;
	$scope.loggedInUser = "";
	$scope.locale = "EN";
	
	$scope.refreshCookie = function() {
		$scope.accessToken = $cookies.getObject("token");
		
		if ($scope.accessToken) {
			$scope.isAuthenticated = true;
			$scope.loggedInUser = $scope.accessToken.principal;
			$scope.hasDeveloper = $scope.accessToken.roles.indexOf("DEVELOPER") != -1;
			$scope.hasSuperUser = $scope.accessToken.roles.indexOf("SUPERUSER") != -1;
		}
	};
	
	$scope.authenticate = function(authentication) {
		$scope.accessToken = RMMV.Web.authenticate(authentication.username, authentication.password);
		
		if (!$scope.accessToken) {
			alert("Authentication Failed!");
			authentication.username = "";
			authentication.password = "";
			return;
		}
		
		$scope.isAuthenticated = true;
		$scope.loggedInUser = authentication.username;
		$scope.hasDeveloper = $scope.accessToken.roles.indexOf("DEVELOPER") != -1;
		$scope.hasSuperUser = $scope.accessToken.roles.indexOf("SUPERUSER") != -1;
		
		$cookies.putObject("token", $scope.accessToken, {
			expires: new Date($scope.accessToken.expires)
		});
		
		authentication.username = "";
		authentication.password = "";
	};
	
	$scope.deauthenticate = function() {
		RMMV.Web.deauthenticate($scope.accessToken);
		
		$scope.clearAuth();
	}
	
	$scope.checkAuthentication = function() {
		var token = null;
		if ($scope.accessToken) {
			token = RMMV.Web.reauthenticate($scope.accessToken);
		}
		
		if (!token) {
			$scope.clearAuth();
		}
	}
	
	$scope.clearAuth = function() {
		$scope.isAuthenticated = false;
		$scope.accessToken = {};
		$scope.hasDeveloper = false;
		$scope.hasSuperUser = false;
		
		$cookies.remove("token");
	}
	
	$scope.onChangeBase = function(base, newPluginVersion) {
		$scope.newPluginVersion.dependencies = base.latestVersion.getDependencies();
		$scope.newPluginVersion.version = base.latestVersion.version;
		$scope.newPluginVersion.compatibleRMVersion = base.latestVersion.compatibleRMVersion;
	};
	
	$scope.onChangeVersion = function(plugin) {
		if (!plugin.selectedVersion.dependencies) {
			plugin.selectedVersion.dependencies = plugin.selectedVersion.getDependencies();
		}
		
		if (plugin.showScript) {
			plugin.script = plugin.selectedVersion.getScript();
		}
	};
	
	$scope.removeDependency = function(version, dependency) {
		var index = version.dependencies.indexOf(dependency);
		if (index >= 0) {
			version.dependencies.splice(index, 1);
		}
	}
	
	$scope.onAddDependency = function(plugin, dependency) {
		plugin.dependencies.push(dependency);
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
	
	$scope.loadScript = function($fileContent, plugin, pluginVersion) {
		var scriptMetadata = RMMV.Util.getPluginMetaData($fileContent, $scope.locale);
		pluginVersion.script = $fileContent;
		
		// Fill in fields if metadata annotations are present
		
		if (plugin) {
			if (scriptMetadata.data["pluginname"]) {
				plugin.name = scriptMetadata.data["pluginname"];
			}
		
			if (scriptMetadata.data["plugindesc"]) {
				plugin.description = scriptMetadata.data["plugindesc"];
			}
		}
		
		if (pluginVersion) {
			if (scriptMetadata.data["pluginvers"]) {
				pluginVersion.version = scriptMetadata.data["pluginvers"];
			}
			
			if (scriptMetadata.data["pluginrmvers"]) {
				pluginVersion.compatibleRMVersion = scriptMetadata.data["pluginrmvers"];
			}
		}
	};
	
	$scope.createPlugin = function(base, pluginVersion, dependencies) {
		// Create the plugin and save it.
		newPlugin = RMMV.PluginBase.create(base);
		newPluginVersion = RMMV.Plugin.create(pluginVersion);
		newPluginVersion.script = pluginVersion.script;
		//newPluginVersion.filename = $scope.fileNames[0];
		
		if (!newPluginVersion.script) {
			return;
		}
		
		// Create new plugin
		newPlugin = RMMV.PluginBase.Web.createPluginBase(newPlugin);
		if (!newPlugin) {
			alert("You must be authenticated to do this!");
			return;
		}
		
		// Create new initial version
		newPluginVersion = newPlugin.addVersion(newPluginVersion);
		if (!newPluginVersion) {
			alert("You must be authenticated to do this!");
			return;
		}
		
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
		$scope.newPluginVersion.dependencies = [];
		$scope.newPlugin.dependencies = [];
		
		// Go back to view-plugins page.
		$scope.page.view = "view-plugins";
		$scope.reloadPluginList();
	};
	
	$scope.createPluginVersion = function(base, pluginVersion, dependencies) {
		// Create the new plugin version.
		var newPluginVersion = RMMV.Plugin.create(pluginVersion);
		var pluginBase = RMMV.PluginBase.create(base);
		newPluginVersion.script = pluginVersion.script;
		//newPluginVersion.filename = $scope.fileNames[0];
		
		if (!newPluginVersion.script) {
			return;
		}
		
		newPluginVersion = pluginBase.addVersion(newPluginVersion);
		
		if (!newPluginVersion) {
			alert("You must be authenticated to do this!");
			return;
		}
		
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
		$scope.newPluginVersion.dependencies = [];
		$scope.newPlugin.dependencies = [];
		
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
	};
	
	$scope.fileNameChanged = function(element, pluginVersion) {
		pluginVersion.filename = element.files[0].name;
	};
	
	$scope.reloadPluginList();
	$scope.refreshCookie();
	$scope.checkAuthentication();
});