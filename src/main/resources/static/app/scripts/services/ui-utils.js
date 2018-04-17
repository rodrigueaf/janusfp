
/**
 * service utilitaire
 */
angular.module('app')
       .factory('utils', ['$translate', function ($translate){
    	   
    	   var utils = {};
	   	   
    	   /**
    	    * initialisation des données de pagination 
    	    */
	   	   utils.initPagination =  {
   				limit : 10,
				limitOption : [10, 20, 30],
				page : 1,
				order : 'Compte',
				label : {page: 'Page:', rowsPerPage: 'Ligne par page:', of: 'sur'},
				count : 0
	   	   };
	   	   
	   	   /**
	   	    * Les états
	   	    */
	   	   utils.states = [{'value' : 'ENABLED'},
   				           {'value' : 'DISABLED'}];
	   	   
	   	   /**
	   	    * traduction avec promesse
	   	    *  NB : ne marche pas bien encore
	   	    */
	   	   utils.qTranslate = function(key){
		   		
	   		     var result = "";
	   		     
	   		     $translate(key).then(function (translation) {
	   		    	 result =  angular.copy(translation);
		   		 }, function (translationId) {
		   		     result = angular.copy(translationId);
		   		 });
		   		 
		   		 return result;
	   	   };
	   	   
	   	   /**
	   	    * traductin instantannée sans promesse
	   	    */
	   	   utils.iTranslate = function (key){
	   		   return $translate.instant(key);
	   	   };
	   	   
	   	   /**
	   	    * types de message
	   	    */
	   	   utils.dialogTitleRemoval = utils.iTranslate("dialog.title.REMOVAL");
	   	   utils.dialogTitleChangeState = utils.iTranslate("dialog.title.CHANGESTATE");
	   	   
	   	   return utils;
       }]);