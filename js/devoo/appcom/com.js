///////////////////////////////////////////////////////////////////////////////////////
// >>> Class Com
/**
 * Controleur de l'application. Reçois les évènements utilisateurs de la vue, puis invoque
 * les services adaptés de l'application java pour modifier les données.
 *
 * @module appcom
 */

function Com(){

    ///////////////////////////////////////////////////////////////////////////////////
    // >> methodes

    // > Appel d'un service

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