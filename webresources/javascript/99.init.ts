/// <reference path="002.bootstrap.min.lib.ts" />
/// <reference path="01.modules.ts" />

interface Window {
	eCtx: {
		jsModules: Array<string>
	}
}

$(document).ready(() => {
	window.eCtx.jsModules.forEach((moduleName: string) => {
		var eModule: EscamberModule = eModules[moduleName];
		if (eModule) {
			eModule.init({});
		}
	});
});