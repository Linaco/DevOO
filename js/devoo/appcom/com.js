/**
 * Le package appcom permet la communication entre les différents morceaux de l'application 
 * (java/js).
 *
 * @module appcom
 */

///////////////////////////////////////////////////////////////////////////////////////
// >>> Class Com

/**
 * La classe Com offre des méthodes qui encapsulent l'appel de services et l'envoi de 
 * fichiers XML au serveur.
 *
 * @class Com
 * @author Robin Nicolet
 * @constructor
 */
function Com(){

    ///////////////////////////////////////////////////////////////////////////////////
    // >> methodes

    // > Appel d'un service

    /**
     * Appelle le service demandé avec les paramètres données, puis appelle les
     * fonctions données en paramètre lors de la réception d'une réponse serveur.
     * Utilisée de façon synchrone, la méthode retourne directement la reponse.
     *
     * @method appelService
     * @param nomService {String} service à appeler
     * @param params {String} service à appeler
     * @param fctOk {function(String)} fonction à appeler en cas de succes
     * @param fctErr {function(String)} fonction à appeler en cas d'échec
     * @param [async=false] {String} spécifie si l'appel est asynchrone..
     * @return {String|void} Reponse si synchrone.
     */
    this.appelService = function(nomService, params, fctOk, fctErr, async){

        var asynchronous = async == null ? false : async;

        console.log("Appel : "+nomService, (asynchronous? "async" : "sync"));

        var xmlhttp=new XMLHttpRequest();
        xmlhttp.open("POST","http://localhost:4500/"+nomService,asynchronous);
        xmlhttp.overrideMimeType('text/xml');

        var msg = "";
        if(params){
            for( var i = 0; i < params.length; ++i){
                msg += params[i];
            }
        }

        if( fctErr == null)fctErr = ctrl.vue.erreur;

        if( !asynchronous ){
            xmlhttp.send(msg); // bloquant
            if(fctOk){
                fctOk(xmlhttp.responseText);
            }
            return xmlhttp.responseText;
        } else {
            xmlhttp.onload = function (e) {
                if (xmlhttp.readyState === 4) {
                    if (xmlhttp.status === 200) {
                        fctOk(xmlhttp.responseText);
                    } else {
                        fctErr(xmlhttp.responseText);
                    }
                }
            };
            xmlhttp.onerror = function (e) {
                fctErr("Connexion impossible avec le serveur...");
            };
            xmlhttp.send(msg);
        }
        
    }

    // > Envoi d'un fichier XML

    /**
     * Appelle le service demandé avec le fichier XML comme paramètre, puis appelle les
     * fonctions données en paramètre lors de la réception d'une réponse serveur.
     *
     * @method envoyerXml
     * @param fileEvt {FileEvent} Evenement associé à la selection du fichier
     * @param nomService {String} service à appeler
     * @param fctOk {function(String)} fonction à appeler en cas de succes
     * @param fctErr {function(String)} fonction à appeler en cas d'échec
     */
    this.envoyerXml = function(fileEvt, nomService, fctOk, fctErr, async){
        var f = fileEvt.target.files[0];
        if(f){
            var extension = f.name.split('.').pop();
            if(extension === "xml" || extension === "XML"){
                var reader = new FileReader();
                
                reader.onload = function(e){
                    this.appelService(nomService,[e.target.result],fctOk,fctErr, async);
                }.bind(this);
                reader.readAsText(f);
            } else {
                vue.erreur("Le fichier sélectionné (."+extension+") n'a pas la bonne extension (.xml) !");
            }
        }
    }.bind(this);
}