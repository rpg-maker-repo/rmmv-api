var RMMV = RMMV || {};
RMMV.Types = {};
RMMV.PluginBase = {};
RMMV.Plugin = {};
RMMV.PluginBase.Web = {};
RMMV.Plugin.Web = {};
RMMV.Web = {};
RMMV.Web.baseUrl = "http://localhost:8080/rmmv-api";
RMMV.Web.authString = "";

// Test credentials

RMMV.Web.testCredentials = function(authString) {
	var success = false;
	$.ajax({
		type: "GET",
		accept: "application/json",
		url: RMMV.Web.baseUrl + "/v1/base",
		headers: {
			"Authorization": authString
		},
		success: function(data) {
			success = true;
		},
		async: false
	});
	
	return success;
}

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
	
	plugin.addVersion = function(version) {
		return RMMV.PluginBase.Web.addVersion(this.id, version);
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
		headers: {
			"Authorization": RMMV.Web.authString
		},
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

RMMV.PluginBase.Web.addVersion = function(id, version) {
	var plugin = null;
	$.ajax({
		type: "POST",
		accept: "application/json",
		contentType: "application/json",
		url: RMMV.Web.baseUrl + "/v1/base/" + id + "/version",
		data: JSON.stringify(version),
		dataType: "json",
		headers: {
			"Authorization": RMMV.Web.authString
		},
		success: function(data) {
			plugin = RMMV.Plugin.create(data);
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
	plugin.filename = null;
	
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
	plugin.filename = oplugin.filename;
	
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
		plugin.filename = oplugin.filename;
		plugins.push(plugin);
	}
	
	return plugins;
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
		headers: {
			"Authorization": RMMV.Web.authString
		},
		success: function(data) {
			plugin = RMMV.Plugin.create(data);
		},
		async: false
	});
	
	return plugin;
};

// Utility module
RMMV.Util = (function() {
	// Retrieve next comment block starting at offset
	function getNextCommentBlock(str, offset) {
		var sub   = str.substring(offset);
		
		// Get locale of comment block
		var locale = "EN";
		var pattern = /\/\*:(.*)/;
		var groups = pattern.exec(sub);
		if (groups && groups[1]) {
			locale = groups[1].toUpperCase();
		}
	
		var start = sub.indexOf("/*");
	
		if (start == -1) {
			return null;
		}
	
		var comment = sub.substring(start + 2);
		var end   = comment.indexOf("*/");
	
		comment = comment.substring(0, end);
	
		return {comment: comment, lastIndex: offset + start + end + 2, locale: locale};
	}

	return {
		getPluginMetaData: function(data, locale) {
			var map = {};
			var params = {};
			var lastParamFound = "";
	
			// Retrieve first comment
			var lastIndex = 0;
	
			// Start scanning for comments.
			while ((commentBlock = getNextCommentBlock(data, lastIndex)) != null) {
				if (locale != commentBlock.locale) {
					lastIndex = commentBlock.lastIndex;
					continue;
				}
		
				// Scan this comment
				var end = commentBlock.comment.indexOf('@');
				var buffer = commentBlock.comment.substring(end + 1);
				while (end != -1) {
					var start = 0;
					var end   = buffer.indexOf('@');
	
					var markup = "";
					if (end != -1) {
						markup = "@" + buffer.substring(start, end).split("*").join("").split("\r\n").join("$NEWLINE$").split("\n").join("$NEWLINE$").split("  ").join(" ");
					} else {
						markup = "@" + buffer.substring(start).split("*").join("").split("\r\n").join("$NEWLINE$").split("\r\n").join("$NEWLINE$").split("  ").join(" ");
					}
	
					var pattern = /@([^ ]+) (.*)/;
					var groups = pattern.exec(markup);
		
					if (!groups) {
						buffer = buffer.substring(end + 1);
						continue;
					}
					var annotation = groups[1].split("$NEWLINE$").join("").trim();
					var value      = groups[2].trim();
			
					if (annotation == "help") {
						value = value.split("$NEWLINE$").join("\n");
					} else {
						value = value.split("$NEWLINE$").join("");
					}
	
					if (annotation == "param") {
						params[value] = {};
						lastParamFound = value;
					} else if (annotation == "desc" && lastParamFound) {
						params[lastParamFound].description = value;
					} else if (annotation == "default" && lastParamFound) {
						params[lastParamFound].defaultValue = value;
					} else {
						map[annotation] = value;
					}
	
					buffer = buffer.substring(end + 1);
				}
				lastIndex = commentBlock.lastIndex;
			}
	
			// Remove dead params
			for (var key in params) {
				if (!params[key].description) {
					delete params[key];
				}
			}
	
			return {data: map, params: params};
		}
	};
})();