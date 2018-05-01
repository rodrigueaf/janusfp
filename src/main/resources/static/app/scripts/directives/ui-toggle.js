angular.module('app')
  .directive('uiToggleClass', function() {
    return {
      restrict: 'AC',
      link: function(scope, el, attr) {
        el.on('click', function(e) {
          e.preventDefault();
          var classes = attr.uiToggleClass.split(','),
              a2 = [el],
              targets = (attr.target && attr.target.split(',')) || a2,
              key = 0;
          angular.forEach(classes, function( _class ) {
            var target = targets[(targets.length && key)];
            $( target ).toggleClass(_class);
            key ++;
          });
          el.toggleClass('active');

        });
      }
    };
  });

/**
 *
 */
angular.module('app')
.directive('uiToggle', function() {
	return {
		restrict: 'AC',
		link: function(scope, el, attr) {
			el.on('click', function(e) {
				e.preventDefault();
				var a2 = [el];
				var targets = (attr.target && attr.target.split(',')) || a2;
				angular.forEach(targets, function( _target ) {
					$( _target ).slideToggle("slow");
				});
			});
		}
	};
});
