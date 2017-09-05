(function(global, pluginId) {
	"use strict";
	var RSuite = global.RSuite,
		Ember = global.Ember,
		$ = global.jQuery;


	RSuite.IetRadioButton = Ember.View.extend({
		tagName: "input",
		type: "radio",
		attributeBindings: ["name", "type", "value", "checked:checked:"],

		change: function() {
			this.set("selection", this.$().val())
		},
		checked: function() {
			return this.get("value") == this.get("selection");
		}.property()
	});

	RSuite.IetFlexibleList = Ember.ArrayController.extend({
		filterBy: null,
		content: [],
		sortProperties: [],
		sortAscending: true,

		filters: {
			special_issue_only: function(item) {
				return item.specialTitle != null;
			},

			standard_article_only: function(item) {
				return item.specialTitle == null;
			},
		},

		filterArticles: function() {
			var filterFunction = this.filters[this.get('filterBy')] || function() {
					return true;
				};
			var list = this.get("arrangedContent").filter(filterFunction);
			return list
		}.property("filterBy", "sortProperties", "sortAscending"),

		sortBy: function(property) {
			var sortBy = property.target.getAttribute('id');

			if (this.get("sortProperties")[0] == sortBy) {
				this.toggleProperty("sortAscending")
			} else {
				this.set("sortAscending", true)
				this.set("sortProperties", [sortBy])
			}
			return

		},
	});

	RSuite.createClass("RSuite.view.Dialog.IetIssueAssingArticles", {
		extend: RSuite.View,
		mixin: [RSuite.view.Dialog]
	}, function(Self, log) {
		Self.reopen({
			classNames: 'issueAssingArticles',
			templateName: RSuite.url(pluginId, 'templates/issue-assing-article.hbr'),
			list: null,
			title: "Assign articles to issue",
			issueCaId: "",

			dialogOpened: function() {},

			dialogClosed: function() {
				this.get('$dialog').remove();
			},

			updateModel: function(issueCaId, availableArticles) {
				this.issueCaId = issueCaId;
				this.list = RSuite.IetFlexibleList.extend({
					view: this
				}).create();
				this.list.set('content', availableArticles);

			},

			dialogOptions: {
				modal: false,
				buttons: [
				RSuite.view.DialogButton.commit({
					text: 'Assign Articles',
					click: function() {

						var checkedArticles = $("#articlesTable input:checkbox:checked").map(function() {
							return $(this).val();
						}).get();

						var issueCaId = this.get('view').issueCaId;
						var data = {
							sourceId: issueCaId,
							rsuiteId: issueCaId,
							articleToAssing: checkedArticles
						};


						RSuite.services({
							service: 'api/iet.journals.assign.articles',
							data: data
						})
							.done(function(data) {})
							.fail(function(data) {
							global.console.error(context);
							return RSuite.failure;
						});

						this.get('view').dialogClose();
						return RSuite.success;
					}
				}),

				RSuite.view.DialogButton.cancel()]
			},

			attributeBindings: ['style'],
			style: Ember.computed(function() {
				return 'max-height: ' + ((RSuite.document.height - 64) * 0.8) + 'px';
			}).property('RSuite.document.height')

		});
	});


	RSuite.Action({
		id: "rsuite:iet:issue:assign:articles",
		invoke: function(context) {

			var moId = Ember.get(context, 'managedObject.id');
			var data = {
				rsuiteId: moId
			};

			RSuite.services({
				service: 'api/iet.journals.get.available.articles',
				data: data
			})
				.done(function(data) {

				if (data && Object.prototype.toString.call(data) === '[object Array]') {
					var dlg = RSuite.view.Dialog.IetIssueAssingArticles.create();
					dlg.updateModel(moId, data);
					dlg.fail(function() {
						dlg.dialogClose()
					});
					dlg.dialogShow();
				} else {
					global.console.error(context);
					return RSuite.failure;
				}
			})
				.fail(function(data) {
				global.console.error(context);
				return RSuite.failure;
			});

			return RSuite.success;
		}

	});

}(this, 'iet'));
