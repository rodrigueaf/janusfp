
/**
 * directive pour appliquer la classe "input-sm" de bootstrap
 * au composant ui-select
 */
angular.module('app')
      .directive('inputSm', function() {
	    return {
	      restrict: 'A',
	      link: function(scope, element, attrs) {
	        var elements = angular.element(element).find(attrs.inputSm);
	        Array.prototype.forEach.call(elements, function(element) {
	            var aE = angular.element(element);
	            if (element.nodeName === 'INPUT') {
	              aE.addClass('input-sm');
	            } else if (aE.hasClass('btn')) {
	              aE.addClass('btn-sm');
	            }
	          });
	      }
	    }
  });