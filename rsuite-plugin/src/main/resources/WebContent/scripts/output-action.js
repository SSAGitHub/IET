(function (global, pluginId) {
	"use strict";
	var RSuite = global.RSuite,
		Ember = global.Ember,
		$ = global.jQuery;

	RSuite.createObject("RSuite.model.ietOutput", { extend: RSuite.ArrayProxy, mixin: [ RSuite.Saveable ] }, function (Self, log) {
		var output = this;
		output.reopen({
			content: [],
			lengthBinding: 'content.length',
			load: function () {				
			},
			updateModel: function (moId) {
				var inst = this;
				
				if (moId){
					console.log("setting moId " + moId);
					var content = inst.get('content');
					content=new Array(); 
					content[0]= moId;
					inst.set('length', content.length);
					inst.set('content', content);
				}
			},
			loadOnFocus: Ember.observer(function () {
								
			}, "RSuite.windowFocus"),
			objects: Ember.computed(function (context) {				
				var content = this.get('content'),
				
					ret = content.map(function (item) {
						return RSuite.model.ManagedObject.getCached(item);
					}).filter(function (item) {
						return !item.get('error');
					});
				return ret;
			}).property('content', 'content.@each', 'content.length')
		});
		RSuite.model.session.done(function () {			
		});
	});
	
	
	RSuite.createClass("RSuite.view.Dialog.IetOutput", { extend: RSuite.View, mixin: [ RSuite.view.Dialog ] }, function (Self, log) {
		Self.reopen({
			classNames: 'briefcase',
			model: RSuite.model.ietOutput,
			templateName: RSuite.url(pluginId, 'templates/outputTemplate.html'),
			
			title: "Output container",
			dialogOpened: function () {
				RSuite.model.briefcase.set('dialogInstance', this);
				this.get('$dialog').droppable({
					accept: function (draggable) {
						return !!$(draggable).data('managedObject');
					},
					drop: function (event, ui) {
						var mo = ui.draggable.data('managedObject');
					
						return true;
					},
					hoverClass: 'accept-managed-object',
					tolerance: 'touch'
				});
			},
			dialogClosed: function () {
				this.get('$dialog').remove();
				this.remove();
				RSuite.model.briefcase.set('dialogInstance', null);
			},
			updateModel: function (moId) {
				this.model.updateModel(moId);
			},
			dialogOptions: {
				modal: false
			},
			attributeBindings: [ 'style' ],
			style: Ember.computed(function () {
				return 'max-height: ' + ((RSuite.document.height - 64) * 0.8) + 'px';
			}).property('RSuite.document.height')
		});
	});
	
	
	RSuite.Action({
		id : "rsuite:iet:show:output",		
		invoke : function (context) {
			
			var moId = Ember.get(context, 'managedObject.id');
			
			var reqData = { rest: "api/projects.helper.webservices.output.locator", "rsuiteId": moId};
			var data = { rsuiteId: moId};
			
			RSuite
			.services({
				service: 'api/projects.helper.webservices.output.locator',
				data: data
			})
			.done(function (data) {
				var outputId  = $(data).find('mo').attr('id');
				console.log("outid " + outputId);

				if (outputId){
					var dlg = RSuite.view.Dialog.IetOutput.create();
					dlg.updateModel(outputId);
					dlg.dialogShow();
				}else{
					global.console.error(context);
					return RSuite.failure;
				}
			})
			.fail(function (data) {
				global.console.error(context);
				return RSuite.failure;
			});
			
			
			
			
			
			return RSuite.success;
		}

	});
}(this, 'iet'));