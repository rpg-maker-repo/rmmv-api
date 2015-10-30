var plugin1 = RMMV.Plugin();
plugin1.name = "Super cool plugin";
plugin1.description = "This is the greatest";
plugin1.version = "1.0";
plugin1.compatibleRMVersion = "RMMV1.0+";
plugin1.script = "console.log('FART');";

var plugin2 = RMMV.Plugin();
plugin2.name = "CorePlugin";
plugin2.description = "This is the greatest";
plugin2.version = "1.5a";
plugin2.compatibleRMVersion = "RMMV1.0+";
plugin2.script = "console.log('FART');";

var plugin3 = RMMV.Plugin();
plugin3.name = "ItemMax Plugin";
plugin3.description = "This is the greatest";
plugin3.version = "2.1b";
plugin3.compatibleRMVersion = "RMMV1.0+";
plugin3.script = "console.log('FART');";

plugin1 = RMMV.Web.createPlugin(plugin1);
plugin2 = RMMV.Web.createPlugin(plugin2);
plugin3 = RMMV.Web.createPlugin(plugin3);
plugin1.addDependencies([plugin2, plugin3]);

console.log(RMMV.Web.getPlugins());
console.log(plugin1.getDependencies());
console.log(plugin1.getScript());