

(function($) {
	$.fn.validationEngineLanguage = function() {};
	$.validationEngineLanguage = {
		newLang: function() {
			$.validationEngineLanguage.allRules = {"required":{    
						"regex":"none",
						"alertText":"* Ce champs est requis",
						"alertTextCheckboxMultiple":"*Choisir une option",
						"alertTextCheckboxe":"* Cette checkbox est requise"},
					"length":{
						"regex":"none",
						"alertText":"* Entre ",
						"alertText2":" et ",
						"alertText3":" caractères requis"},
					"maxCheckbox":{
						"regex":"none",
						"alertText":"* Nombre max the boite exceder"},	
					"minCheckbox":{
						"regex":"none",
						"alertText":"* Veuillez choisir ",
						"alertText2":" options"},		
					"confirm":{
						"regex":"none",
						"alertText":"* Ce champ doit être identique au précédent"},
					"telephone":{
						"regex":"/^[0-9\-\(\)\ ]+$/",
						"alertText":"* Numéro de téléphone invalide"},	
					"email":{
						"regex":"/^[a-zA-Z0-9_\.\-]+\@([a-zA-Z0-9\-])+\.+[a-zA-Z0-9]{2,4}$/",
						"alertText":"* Adresse email invalide"},	
					"date":{
                         "regex":"/^[0-9]{4}\-\[0-9]{1,2}\-\[0-9]{1,2}$/",
                         "alertText":"* Date invalide, format YYYY-MM-DD requis"},
					"onlyNumber":{
						"regex":"/^[0-9\ ]+$/",
						"alertText":"* Chiffres seulement accepté"},	
					"noSpecialCaracters":{
						"regex":"/^[0-9a-zA-Z]+$/",
						"alertText":"* Aucun caractère spécial accepté"},	
					"onlyLetter":{
						"regex":"/^[a-zA-Z\ \']+$/",
						"alertText":"* Lettres seulement accepté"},
					"ajaxUser":{
						"file":"validateUser.php",
						"alertTextOk":"* Ce nom est déjà pris",	
						"alertTextLoad":"* Chargement, veuillez attendre",
						"alertText":"* Ce nom est déjà pris"},	
					"ajaxName":{
						"file":"validateUser.php",
						"alertText":"* Ce nom est déjà pris",
						"alertTextOk":"*Ce nom est disponible",	
						"alertTextLoad":"* LChargement, veuillez attendre"},
					"validate2fields":{
    					"nname":"validate2fields",
    					"alertText":"Vous devez avoir un prénom et un nom"}	
					}	
		}
	}
})(jQuery);

$(document).ready(function() {	
	$.validationEngineLanguage.newLang()
});