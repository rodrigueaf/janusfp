
/**
 * Définir ici les librairies à charger à la demande sur certaines page.
 */
angular.module('app')
  .constant('MODULE_CONFIG', [
      {
          name: 'ui.select',
          module: true,
          files: [
              'app/librairies/angular/angular-ui-select/dist/select.min.js',
              'app/librairies/angular/angular-ui-select/dist/select.min.css'
          ]
      },
      {
          name: 'textAngular',
          module: true,
          files: [
              'app/librairies/angular/textAngular/dist/textAngular-sanitize.min.js',
              'app/librairies/angular/textAngular/dist/textAngular.min.js'
          ]
      },
      {
          name: 'angularFileUpload',
          module: true,
          files: [
              'app/librairies/angular/angular-file-upload/angular-file-upload.js'
          ]
      },
      {
          name: 'moment',
          module: false,
          files: [
              'app/librairies/jquery/moment/moment.js'
          ]
      },
      {
          name: 'ngFileSaver',
          module: true,
          files: [
              'app/librairies/jquery/file-saver/FileSaver.min.js',
        	  'app/librairies/angular/angular-file-saver/angular-file-saver.min.js'
          ]
      }
    ]
  )
  .config(['$ocLazyLoadProvider', 'MODULE_CONFIG', function($ocLazyLoadProvider, MODULE_CONFIG) {
      $ocLazyLoadProvider.config({
          debug: false,
          events: false,
          modules: MODULE_CONFIG
      });
  }]);
