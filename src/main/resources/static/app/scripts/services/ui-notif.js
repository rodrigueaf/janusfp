
/**
 * service utilitaire pour la notification
 */
angular.module('app')
       .factory('uiNotif', ['$mdDialog', '$mdToast', function ($mdDialog, $mdToast){
    	   
    	   var notif = {};
    	   
    	   var pinTo = 'top right'; 
  	     
    	   /**
    	    * message d'alerte 
    	    */
	   	   notif.info = function (message) {
	   	   	 
	   		   $mdToast.show(
	   	      	      $mdToast.simple()
	   	      	        .textContent(message)
	   	      	        .position(pinTo )
	   	      	        .hideDelay(5000)
	   	      	    );
	   	    };
	   	   
	   	   /**
	   	    * construit la boite de dialog
	   	    */ 
		   notif.mdDialog = function ( ev, title, message) {
	   		   
	   		   var confirm = $mdDialog.confirm({
	   			   
	   			   // configuration pour mettre le focus par défaut sur l'action "NON"
	   			   onComplete: function() {
                   var $dialog = angular.element(document.querySelector('md-dialog'));
                   var $actionsSection = $dialog.find('md-dialog-actions');
                   var $cancelButton = $actionsSection.children()[0];
                   var $confirmButton = $actionsSection.children()[1];
                   angular.element($confirmButton).removeClass('md-focused');
                   angular.element($cancelButton).addClass('md-focused');
                   $cancelButton.focus();
	   		   	   }})
	   		   	   
					.title(title)
					.textContent(message)
					.targetEvent(ev)
					.ok('OUI')
					.cancel('NON');
	   		   
				return $mdDialog.show(confirm);
			};
	   	   
	   	   return notif;
    	   
       }]);