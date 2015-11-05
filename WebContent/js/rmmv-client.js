var RMMV = RMMV || {};
RMMV.Types = {};
RMMV.PluginBase = {};
RMMV.Plugin = {};
RMMV.PluginBase.Web = {};
RMMV.Plugin.Web = {};
RMMV.Web = {};
RMMV.Web.baseUrl = "http://localhost:8080/rmmv-api";

// Plugin Base

RMMV.Types.PluginBase = function() {
	var plugin = {};
	plugin.id = null;
	plugin.dateCreated = null;
	plugin.name = null;
	plugin.description = null;
	
	plugin.refreshObject = function() {
		return RMMV.PluginBase.Web.getBasePlugin(this.id);
	};
	
	plugin.getVersions = function() {
		return RMMV.PluginBase.Web.getVersions(this.id);
	};
	
	plugin.addVersions = function(versions) {
		return RMMV.PluginBase.Web.addVersions(this.id, versions);
	};
	
	return plugin;
};

RMMV.PluginBase.create = function(oplugin) {
	var plugin = RMMV.Types.PluginBase();
	plugin.id = oplugin.id;
	plugin.dateCreated = oplugin.dateCreated;
	plugin.name = oplugin.name;
	plugin.description = oplugin.description;
	
	return plugin;
};

RMMV.PluginBase.createArray = function(oplugins) {
	var plugins = [];
	for (var i = 0; i < oplugins.length; i++) {
		var oplugin = oplugins[i];
		var plugin = RMMV.Types.PluginBase();
		plugin.id = oplugin.id;
		plugin.dateCreated = oplugin.dateCreated;
		plugin.name = oplugin.name;
		plugin.description = oplugin.description;
		plugins.push(plugin);
	}
	
	return plugins;
};

RMMV.PluginBase.Web.createPluginBase = function(plugin) {
	var saved = RMMV.Types.PluginBase();
	$.ajax({
		type: "POST",
		accept: "application/json",
		contentType: "application/json",
		url: RMMV.Web.baseUrl + "/v1/base",
		data: JSON.stringify(plugin),
		dataType: "json",
		success: function(data) {
			saved = RMMV.PluginBase.create(data);
		},
		async: false
	});
	
	return saved;
};

RMMV.PluginBase.Web.getPluginBase = function(id) {
	var ret = RMMV.Types.PluginBase();
	$.ajax({
		type: "GET",
		accept: "application/json",
		url: RMMV.Web.baseUrl + "/v1/base/" + id,
		success: function(data) {
			ret = RMMV.PluginBase.createBase(data);
		},
		async: false
	});
	
	return ret;
}

RMMV.PluginBase.Web.getPluginBases = function() {
	var ret = null;
	$.ajax({
		type: "GET",
		accept: "application/json",
		url: RMMV.Web.baseUrl + "/v1/base",
		success: function(data) {
			ret = RMMV.PluginBase.createArray(data);
		},
		async: false
	});
	
	return ret;
}

RMMV.PluginBase.Web.getVersions = function(id) {
	var ret = null;
	$.ajax({
		type: "GET",
		accept: "application/json",
		url: RMMV.Web.baseUrl + "/v1/base/" + id + "/version",
		success: function(data) {
			ret = RMMV.Plugin.createArray(data);
		},
		async: false
	});
	
	return ret;
}

RMMV.PluginBase.Web.addVersions = function(id, versions) {
	var plugin = null;
	$.ajax({
		type: "POST",
		accept: "application/json",
		contentType: "application/json",
		url: RMMV.Web.baseUrl + "/v1/base/" + id + "/version",
		data: JSON.stringify(versions),
		dataType: "json",
		success: function(data) {
			plugin = RMMV.PluginBase.create(data);
		},
		async: false
	});
	
	return plugin;
};

// Plugin

RMMV.Types.Plugin = function() {
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
		return RMMV.Plugin.Web.getPlugin(this.id);
	};
	
	plugin.getScript = function() {
		return RMMV.Plugin.Web.getPluginScript(this.id);
	};
	
	plugin.getDependencies = function() {
		return RMMV.Plugin.Web.getDependencies(this.id);
	};
	
	plugin.addDependencies = function(dependencies) {
		return RMMV.Plugin.Web.addDependencies(this.id, dependencies);
	};
	
	return plugin;
};

RMMV.Plugin.create = function(oplugin) {
	var plugin = RMMV.Types.Plugin();
	plugin.id = oplugin.id;
	plugin.dateCreated = oplugin.dateCreated;
	plugin.name = oplugin.name;
	plugin.description = oplugin.description;
	plugin.version = oplugin.version;
	plugin.compatibleRMVersion = oplugin.compatibleRMVersion;
	plugin.hash = oplugin.hash;
	plugin.script = oplugin.script;
	
	return plugin;
};

RMMV.Plugin.createArray = function(oplugins) {
	var plugins = [];
	for (var i = 0; i < oplugins.length; i++) {
		var oplugin = oplugins[i];
		var plugin = RMMV.Types.Plugin();
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
};

RMMV.Plugin.Web.createPlugin = function(plugin) {
	var saved = RMMV.Types.Plugin();
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

RMMV.Plugin.Web.getPlugin = function(id) {
	var ret = null
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

RMMV.Plugin.Web.getPlugins = function() {
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

RMMV.Plugin.Web.getPluginScript = function(id) {
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

RMMV.Plugin.Web.getDependencies = function(id) {
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

RMMV.Plugin.Web.addDependencies = function(id, dependencies) {
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