/// <reference path="001.jquery-1.12.2.min.lib.ts" />
/// <reference path="01.modules.ts" />

module IndexModule {
	abstract class Option {
		private static TOOL_BAR_HEIGHT = 49;
		private static MIN_HEIGHT = 618;

		private initialized: boolean;
		
		private wrapper: JQuery;

		abstract getId(): string;
		abstract initialize(): void;
		abstract deactivate(): void;

		activate() : void {
			if (!this.initialized) {
				this.initialized = true;
				this.initialize();
			}
			this.resize();
		}

		setWrapper(wrapper: JQuery) : void {
			this.wrapper = wrapper;
		}

		index(): number {
			return this.wrapper.index();
		}

		resize() : void {
			var height = $(window).height() - (Option.TOOL_BAR_HEIGHT);
			height = Math.max(height, Option.MIN_HEIGHT);
			this.wrapper.height(height);
		}

		protected getWrapper() : JQuery {
			return this.wrapper;
		}
	}

	class ProfileOption extends Option {
		getId(): string {
			return 'settings-option';
		}

		initialize() : void {}
		deactivate(): void {}
	}

	class MatchesOption extends Option {
		getId(): string {
			return 'matches-option';
		}

		initialize(): void { }
		deactivate(): void { }
	}

	class ChatOption extends Option {
		private convo: JQuery;

		getId(): string {
			return 'chat-option';
		}

		initialize(): void {
			this.convo = this.getWrapper().find('.chat-conversation');
			var convoPreview = this.convo.find('.chat-preview');

			convoPreview.click(() => {
				this.convo.toggleClass('open');
			});
		}

		deactivate() : void {
			this.convo.removeClass('open');
		}
	}

	class OptionsManager {
		private inputs: JQuery;
		private optionsContent: JQuery;

		private value: string;
		private options: { [optionName: string]: Option };

		constructor(nav: JQuery, content: JQuery, ...options: Array<Option>) {
			this.optionsContent = content;

			this.options = {};
			for (var i = options.length - 1; i >= 0; i--) {
				var option = options[i];
				option.setWrapper(content.find('.tab-option-content.' + option.getId()));
				this.options[option.getId()] = option;
			}

			this.inputs = nav.find('input');
			this.value = null;
			this.triggerChange();

			this.addChangeListener();
		}

		private refreshSelectedValue(): void {
			this.value = this.inputs.filter(':checked').attr('id');
		}

		private addChangeListener() : void {
			this.inputs.change(() => {
				this.triggerChange();
			});
		}

		private getCurrentOption() : Option {
			return this.options[this.value];
		}

		private triggerChange(): void {
			var oldOption = this.getCurrentOption();			
			if (oldOption) {
				oldOption.deactivate();
			}

			this.refreshSelectedValue();
			var option = this.getCurrentOption();
			option.activate();

			this.optionsContent.css('margin-left', (-option.index() * 100) + '%');
		}

		resize() : void {
			this.getCurrentOption().resize();
		}
	}

	class IndexInitializer implements EscamberModule {
		private optionsManager: OptionsManager;

		init(data: { [key: string]: any }): void {
			var body = jQuery('.escamber-index-page');

			var nav = body.find('.tab-options');
			var content = body.find('.tab-options-content-wrapper');

			var profileOption = new ProfileOption();
			var matchesOption = new MatchesOption();
			var chatOption = new ChatOption();

			this.optionsManager = new OptionsManager(nav, content, profileOption, matchesOption, chatOption);
			this.optionsManager.resize();
			this.addEventListeners();
		}

		private addEventListeners() : void {
			$(window).on('resize', ()=> {
				this.optionsManager.resize();
			});
		}
	}

	eModules['Index'] = new IndexInitializer();
}