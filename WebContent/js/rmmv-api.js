var RMMV = RMMV || {};
RMMV.Web = {};

RMMV.Web.baseUrl = "http://localhost:8080/rmmv-api";

RMMV.Plugin = function() {
	var plugin = {};
	plugin.id = null;
	plugin.dateCreated = null;
	plugin.name = null;
	plugin.description = null;
	plugin.version = null;
	plugin.compatibleRMVersion = null;
	plugin.hash = null;
	plugin.script = null;
	
	plugin.refreshObject = function() {
		return RMMV.Web.getPlugin(this.id);
	};
	
	plugin.getScript = function() {
		return RMMV.Web.getPluginScript(this.id);
	};
	
	plugin.getDependencies = function() {
		return RMMV.Web.getDependencies(this.id);
	};
	
	plugin.addDependencies = function(dependencies) {
		return RMMV.Web.addDependencies(this.id, dependencies);
	};
	
	return plugin;
}

RMMV.Plugin.create = function(oplugin) {
	var plugin = RMMV.Plugin();
	plugin.id = oplugin.id;
	plugin.dateCreated = oplugin.dateCreated;
	plugin.name = oplugin.name;
	plugin.description = oplugin.description;
	plugin.version = oplugin.version;
	plugin.compatibleRMVersion = oplugin.compatibleRMVersion;
	plugin.hash = oplugin.hash;
	plugin.script = oplugin.script;
	
	return plugin;
}

RMMV.Plugin.createArray = function(oplugins) {
	var plugins = [];
	for (var i = 0; i < oplugins.length; i++) {
		var oplugin = oplugins[i];
		var plugin = RMMV.Plugin();
		plugin.id = oplugin.id;
		plugin.dateCreated = oplugin.dateCreated;
		plugin.name = oplugin.name;
		plugin.description = oplugin.description;
		plugin.version = oplugin.version;
		plugin.compatibleRMVersion = oplugin.compatibleRMVersion;
		plugin.hash = oplugin.hash;
		plugin.script = oplugin.script;
		plugins.push(plugin);
	}
	
	return plugins;
}

RMMV.Web.createPlugin = function(plugin) {
	var saved = RMMV.Plugin();
	$.ajax({
		type: "POST",
		accept: "application/json",
		contentType: "application/json",
		url: RMMV.Web.baseUrl + "/v1/plugin",
		data: JSON.stringify(plugin),
		dataType: "json",
		success: function(data) {
			saved = RMMV.Plugin.create(data);
		},
		async: false
	});
	
	return saved;
};

RMMV.Web.getPlugin = function(id) {
	var ret = RMMV.Plugin();
	$.ajax({
		type: "GET",
		accept: "application/json",
		url: RMMV.Web.baseUrl + "/v1/plugin/" + id,
		success: function(data) {
			ret = RMMV.Plugin.create(data);
		},
		async: false
	});
	
	return ret;
};

RMMV.Web.getPlugins = function() {
	var ret = null;
	$.ajax({
		type: "GET",
		accept: "application/json",
		url: RMMV.Web.baseUrl + "/v1/plugin",
		success: function(data) {
			ret = RMMV.Plugin.createArray(data);
		},
		async: false
	});
	
	return ret;
};

RMMV.Web.getPluginScript = function(id) {
	var script = $.ajax({
		type: "GET",
		accept: "text/plain",
		url: RMMV.Web.baseUrl + "/v1/plugin/" + id + "/script",
		success: function(data) {},
		error: function(data, status) {},
		dataType: "script",
		async: false
	}).responseText;
	
	return script;
};

RMMV.Web.getDependencies = function(id) {
	var dependencies = null;
	$.ajax({
		type: "GET",
		accept: "application/json",
		url: RMMV.Web.baseUrl + "/v1/plugin/" + id + "/dependency",
		success: function(data) {
			dependencies = RMMV.Plugin.createArray(data);
		},
		async: false
	});
	
	return dependencies;
};

RMMV.Web.addDependencies = function(id, dependencies) {
	var plugin = null;
	$.ajax({
		type: "POST",
		accept: "application/json",
		contentType: "application/json",
		url: RMMV.Web.baseUrl + "/v1/plugin/" + id + "/dependency",
		data: JSON.stringify(dependencies),
		dataType: "json",
		success: function(data) {
			plugin = RMMV.Plugin.create(data);
		},
		async: false
	});
	
	return plugin;
};