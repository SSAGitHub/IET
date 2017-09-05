RSuite.Action.get('rsuite:download').reopen({
    invoke: function(context) {
        var base = RSuite.Webservice.serviceOptions(context),
            options = $.extend({
                    version: 1,
                    service: 'api/iet.download',
                    window: true
                },
                RSuite.Webservice.invokeOptions(context));
        return RSuite.services(options, context)
            .fail(function(xhr, status, reason) {
                return reason.display ? reason.display() : RSuite.error(reason);
            });
    }
});