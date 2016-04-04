interface EscamberModule {
	init(data: { [key: string]: any }) : void;
}

var eModules: { [name: string]: EscamberModule } = {};