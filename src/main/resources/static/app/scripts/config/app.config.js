// config

var app =
angular.module('app')
  .config(['$controllerProvider', '$compileProvider', '$filterProvider', '$provide',
    function ($controllerProvider,   $compileProvider,   $filterProvider,   $provide) {

        // lazy controller, directive and service
        app.controller = $controllerProvider.register;
        app.directive  = $compileProvider.directive;
        app.filter     = $filterProvider.register;
        app.factory    = $provide.factory;
        app.service    = $provide.service;
        app.constant   = $provide.constant;
        app.value      = $provide.value;
    }
  ])

  //configuration du module de translation
  .config(['$translateProvider', function($translateProvider){
    // Register a loader for the static files
    // So, the module will search missing translation tables under the specified urls.
    // Those urls are [prefix][langKey][suffix].
    $translateProvider.useStaticFilesLoader({
      prefix: 'app/i18n/',
      suffix: '.js'
    });
    // Tell the module what language to use by default
    $translateProvider.preferredLanguage('fr');
    // Tell the module to store the language in the local storage
    $translateProvider.useLocalStorage();
    $translateProvider.useSanitizeValueStrategy('escapeParameters');
  }])

  // configuration format de la date dans mddatepicker
  .config(['$mdDateLocaleProvider', '$compileProvider',
    function($mdDateLocaleProvider) {
	  $mdDateLocaleProvider.formatDate = function(date) {
		  return date ? moment(date).format('DD/MM/YYYY') : null;
		};

    }])

    // accepter les liens vers les fichiers blob
   .config(['$compileProvider', function ($compileProvider) {
        $compileProvider.aHrefSanitizationWhitelist(/^\s*(|blob|):/);
     }])

    // configuration du thème
   .config(['$mdThemingProvider', function($mdThemingProvider) {
	   $mdThemingProvider.theme('default')
	    .primaryPalette('indigo')
	    .accentPalette('blue-grey');
	}])

    // configuration scrollbar
    .config(['ScrollBarsProvider', function (ScrollBarsProvider) {
            ScrollBarsProvider.defaults = {
                    scrollButtons: {
                        scrollAmount: 'auto',
                        enable: true
                    },
                    scrollInertia: 250,
                    axis: 'yx',
                    theme: 'dark',
                    autoHideScrollbar: true,
                    setHeight: 100
            };
    }])

	// configuration de la fil d'arial
	.config(['$breadcrumbProvider', function($breadcrumbProvider) {
		    $breadcrumbProvider.setOptions({
		      templateUrl: 'app/views/templates/breadcrumb.html'
		    })
	}])

    // configuration par défaut xeditable
    .run(['editableOptions', 'editableThemes', function(editableOptions, editableThemes) {
	  editableThemes.bs3.inputClass = 'input-sm';
	  editableOptions.theme = 'bs3';
	}]);