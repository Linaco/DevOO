// GLOBAL
var ctrl = new Controleur();
var map = ctrl.vue.map;

///////////////////////////////////////////////////////////////////////////////////////
// >>> Initialisation de la page web

if (window.File && window.FileReader && window.FileList && window.Blob) {
    // Super ! Toutes les File APIs sont prises en charge.
} else {
    alert('Les APIs pour l\'ouverture des fichiers ne sont pas prises en charge');
}
$.fn.modal.Constructor.prototype.enforceFocus = function () {};

///////////////////////////////////////////////////////////////////////////////////////
// >>> Class Controleur

function Controleur(){

    ///////////////////////////////////////////////////////////////////////////////////
    // >> attributes

    var com = new Com();

    var vue;

    ///////////////////////////////////////////////////////////////////////////////////
    // >> methodes

    // > Annuler

    this.annuler = function() {
        console.log("undo");
        vue.afficherChargement("Annulation de l'action...");
        com.appelService("controleur/annuler","",this._annulerOk,this._annulerErr,true);
    }.bind(this);

    this._annulerOk = function(rep) {
        vue.nouvelItineraire(true);
    }.bind(this);

    this._annulerErr = function(msg) {
        vue.fermerChargement();
        vue.erreur(msg);
    }.bind(this);

    // > Rétablir

    this.retablir = function(){
        console.log("redo");
        vue.afficherChargement("Retablissement de l'action...");
        com.appelService("controleur/retablir","",this._retablirOk,this._retablirErr,true);
    }.bind(this);

    this._retablirOk = function(rep) {
        vue.nouvelItineraire(true);
    }.bind(this);

    this._retablirErr = function(msg) {
        vue.fermerChargement();
        vue.erreur(msg);
    }.bind(this);

    // > Charger le plan

    this.clicChargerPlan = function(){
        document.getElementById('charger-plan').click();
    };

    this.chargerPlan = function(evt){
        vue.afficherChargement("Création du réseau routier...\n"
                + "Merci de patienter quelques instants.");
        com.envoyerXml(evt,'controleur/charger-plan',this._chargerPlanOk,this._chargerLivraisonsErr, true);
        document.getElementById("charger-plan").value = null;
    }.bind(this);

    this._chargerPlanOk = function(msg){
        vue.nouveauPlan();
    }.bind(this);

    this._chargerPlanErr = function(msg){
        vue.fermerChargement();
        vue.erreur(msg);
    }.bind(this);

    // > Charger les livraisons

    this.clicChargerLivraisons = function(){
        document.getElementById('charger-livraisons').click();
    };

    this.chargerLivraisons = function(evt){
        vue.afficherChargement("Chargement des données de livraisons...\n"
                + "Merci de patienter quelques instants.");
        com.envoyerXml(evt,'controleur/charger-livraisons',this._chargerLivraisonsOk,this._chargerLivraisonsErr,true);
        document.getElementById("charger-livraisons").value = null;
    }.bind(this);

    this._chargerLivraisonsOk = function(msg){
        vue.nouvellesLivraisons();
    };

    this._chargerLivraisonsErr = function(msg){
        vue.fermerChargement();
        vue.erreur(msg);
    }.bind(this);

    // > Telecharger l'itinéraire en pdf

    this.clicTelechargerInitineraire = function(){
        var pdf = new jsPDF();
        var elementHandler = {
            '#ignorePDF': function (element, renderer) {
                return true;
            }
        };
        var source = window.document.getElementById('FDR');
        pdf.fromHTML(
            source,
            15,
            15,
            {
              'width': 180,'elementHandlers': elementHandler
            });

        pdf.output("dataurlnewwindow");
    };

    // > Calcul du parcours

    this._clicCalcul = function () {
        vue.afficherChargement("Calcul en cours, veuillez patienter...");
        com.appelService('controleur/calculer-itineraire','',
                        this._clicCalculOk,
                        this._clicCalculErr, true);
    }.bind(this);

    this._clicCalculOk = function(msg) {
        vue.nouvelItineraire();
    }.bind(this);

    this._clicCalculErr = function(msg) {
        vue.fermerChargement();
        vue.erreur(msg);
    }.bind(this);

    // > Suppression d'une livraison

    this.demandeDeSuppression = function(id) {
        vue.afficherChargement("Suppression en cours, veuillez patienter...");
        com.appelService("controleur/supprimer-livraison",""+id,this._suppressionOk, this._suppressionErr, true);
    };   

    this._suppressionOk = function(rep) {
        vue.nouvelItineraire(true);
    }; 

    this._suppressionErr = function(msg) {
        vue.fermerChargement();
        vue.erreur(msg);
    };

    // > Ajout d'une livraison

    this.ajouterLivraison = function(idIntersection, idLivraison, idClient){
        vue.afficherChargement("Ajout en cours, veuillez patienter...");
        var param = idIntersection+"\n"+idClient+"\n"+idLivraison;
        com.appelService("controleur/ajouter-livraison",param,this._ajouterLivraisonOk,this._ajouterLivraisonErr,true);
    }

    this._ajouterLivraisonOk = function(rep) {
        vue.nouvelItineraire(true);
    };

    this._ajouterLivraisonErr = function(msg) {
        vue.fermerChargement();
        vue.erreur(msg);
    };

    ///////////////////////////////////////////////////////////////////////////////////
    // >> Initialisation de l'objet

    this.vue = vue = new Vue(this, com);

}


///////////////////////////////////////////////////////////////////////////////////////
// >>> Class Com

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