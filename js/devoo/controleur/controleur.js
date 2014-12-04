///////////////////////////////////////////////////////////////////////////////////////
// >>> Projet DevOO - INSA de Lyon - 2014

///////////////////////////////////////////////////////////////////////////////////////
// >>> Variables globales

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

/**
 * Controleur de l'application. Reçois les évènements utilisateurs de la vue, puis invoque
 * les services adaptés de l'application java pour modifier les données.
 *
 * @module controleur
 */

///////////////////////////////////////////////////////////////////////////////////////
// >>> Class Controleur

/**
 * Controleur de l'application. Reçois les évènements utilisateurs de la vue, puis invoque
 * les services adaptés de l'application java pour modifier les données.
 *
 * @class Controleur
 * @author Robin Nicolet
 * @constructor
 */
function Controleur(){

    ///////////////////////////////////////////////////////////////////////////////////
    // >> attributs

    /**
     * Instance de la classe {{#crossLink "Com"}}{{/crossLink}} permettant de communiquer
     * avec le serveur.
     * @property com
     * @type Com
     */
    var com = new Com();

    var vue;

    ///////////////////////////////////////////////////////////////////////////////////
    // >> methodes

    // > Annuler

    /**
     * Démarre l'écran de chargement, puis appel le service `/controleur/annuler`.<br/>
     * Si l'appel au service réussit, la méthode 
     * {{#crossLink "Controleur/_annulerOk:method"}}{{/crossLink}} est appelée 
     * ( {{#crossLink "Controleur/_annulerErr:method"}}{{/crossLink}} dans le cas contraire).
     * @method annuler
     */
    this.annuler = function() {
        console.log("undo");
        vue.afficherChargement("Annulation de l'action...");
        com.appelService("controleur/annuler","",this._annulerOk,this._annulerErr,true);
    }.bind(this);

    /**
     * Cette méthode est appelée lorsqu'un appel au service `/controleur/annuler` a réussi.
     * <br/> On demande alors à la vue de regénérer l'itinéraire (l'annulation d'un chargement
     * de plan/livraisons n'est pas prise en charge, voir spec.s).
     * <br/> Voir {{#crossLink "Controleur/annuler:method"}}{{/crossLink}}
     * @method _annulerOk
     * @param {String} rep Réponse du service (aucune pour ce service)
     * @private
     */
    this._annulerOk = function(rep) {
        vue.nouvelItineraire(true);
    }.bind(this);

    /**
     * Cette méthode est appelée lorsqu'un appel au service `/controleur/annuler` a échoué.
     * Elle ferme alors l'écran de chargement et affiche un message d'erreur.
     * <br/> Voir {{#crossLink "Controleur/annuler:method"}}{{/crossLink}}
     * @method _annulerErr
     * @param {String} msg Message d'erreur retourné par le service.
     * @private
     */
    this._annulerErr = function(msg) {
        vue.fermerChargement();
        vue.erreur(msg);
    }.bind(this);

    // > Rétablir

    /**
     * Démarre l'écran de chargement, puis appel le service `/controleur/retablir`.<br/>
     * Si l'appel au service réussit, la méthode 
     * {{#crossLink "Controleur/_retablirOk:method"}}{{/crossLink}} est appelée 
     * ( {{#crossLink "Controleur/_retablirErr:method"}}{{/crossLink}} dans le cas contraire).
     * @method retablir
     */
    this.retablir = function(){
        vue.afficherChargement("Retablissement de l'action...");
        com.appelService("controleur/retablir","",this._retablirOk,this._retablirErr,true);
    }.bind(this);

    /**
     * Cette méthode est appelée lorsqu'un appel au service `/controleur/retablir` a réussi.
     * <br/> On demande alors à la vue de regénérer l'itinéraire (l'annulation d'un chargement
     * de plan/livraisons n'est pas prise en charge, voir spec.s).
     * <br/> Voir {{#crossLink "Controleur/retablir:method"}}{{/crossLink}}
     * @method _retablirOk
     * @param {String} rep Réponse du service (aucune pour ce service)
     * @private
     */
    this._retablirOk = function(rep) {
        vue.nouvelItineraire(true);
    }.bind(this);

    /**
     * Cette méthode est appelée lorsqu'un appel au service `/controleur/retablir` a échoué.
     * Elle ferme alors l'écran de chargement et affiche un message d'erreur.
     * <br/> Voir {{#crossLink "Controleur/retablir:method"}}{{/crossLink}}
     * @method _retablirErr
     * @param {String} msg Message d'erreur retourné par le service.
     * @private
     */
    this._retablirErr = function(msg) {
        vue.fermerChargement();
        vue.erreur(msg);
    }.bind(this);

    // > Charger le plan

    /**
     * Simule un clic sur l'input HTML dédié à l'ouverture de fichier pour le plan. 
     * @method clicChargerPlan
     */
    this.clicChargerPlan = function(){
        document.getElementById('charger-plan').click();
    };

    /**
     * Méthode associée à la selection d'un fichier plan. Affiche l'écran de chargement puis
     * appelle le service `/controleur/charger-plan` avec le contenu du fichier comme paramètre.
     * <br/>Si l'appel au service réussit, la méthode 
     * {{#crossLink "Controleur/_chargerPlanOk:method"}}{{/crossLink}} est appelée 
     * ( {{#crossLink "Controleur/_chargerPlanErr:method"}}{{/crossLink}} dans le cas contraire).
     * <br/> Voir le module {{#crossLinkModule "appcom"}}{{/crossLinkModule}}.
     * @method chargerPlan
     * @param {FileEvent} evt FileEvent contenant le fichier choisi par l'utilisateur.
     */
    this.chargerPlan = function(evt){
        vue.afficherChargement("Création du réseau routier...\n"
                + "Merci de patienter quelques instants.");
        com.envoyerXml(evt,'controleur/charger-plan',this._chargerPlanOk,this._chargerLivraisonsErr, true);
        document.getElementById("charger-plan").value = null;
    }.bind(this);

    /**
     * Cette méthode est appelée lorsqu'un appel au service `/controleur/charger-plan` a réussi.
     * Elle demande alors la vue de recharger le plan.
     * <br/> Voir {{#crossLink "Controleur/chargerPlan:method"}}{{/crossLink}}
     * @method _chargerPlanOk
     * @param {String} msg Réponse du service (aucune pour ce service).
     * @private
     */
    this._chargerPlanOk = function(msg){
        vue.nouveauPlan();
    }.bind(this);

    /**
     * Cette méthode est appelée lorsqu'un appel au service `/controleur/charger-plan` a échoué.
     * Elle ferme alors l'écran de chargement et affiche un message d'erreur.
     * <br/> Voir {{#crossLink "Controleur/chargerPlan:method"}}{{/crossLink}}
     * @method _chargerPlanErr
     * @param {String} msg Message d'erreur retourné par le service.
     * @private
     */
    this._chargerPlanErr = function(msg){
        vue.fermerChargement();
        vue.erreur(msg);
    }.bind(this);

    // > Charger les livraisons

    /**
     * Simule un clic sur l'input HTML dédié à l'ouverture de fichier pour les livraisons.
     * @method clicChargerLivraisons
     */
    this.clicChargerLivraisons = function(){
        document.getElementById('charger-livraisons').click();
    };

    /**
     * Méthode associée à la selection d'un fichier livraisons. Affiche l'écran de chargement puis
     * appelle le service `/controleur/charger-livraisons` avec le contenu du fichier comme paramètre.
     * <br/>Si l'appel au service réussit, la méthode 
     * {{#crossLink "Controleur/_chargerLivraisonsOk:method"}}{{/crossLink}} est appelée 
     * ( {{#crossLink "Controleur/_chargerLivraisonsErr:method"}}{{/crossLink}} dans le cas contraire).
     * <br/> Voir le module {{#crossLinkModule "appcom"}}{{/crossLinkModule}}.
     * @method chargerLivraisons
     * @param {FileEvent} evt FileEvent contenant le fichier choisi par l'utilisateur.
     */
    this.chargerLivraisons = function(evt){
        vue.afficherChargement("Chargement des données de livraisons...\n"
                + "Merci de patienter quelques instants.");
        com.envoyerXml(evt,'controleur/charger-livraisons',this._chargerLivraisonsOk,this._chargerLivraisonsErr,true);
        document.getElementById("charger-livraisons").value = null;
    }.bind(this);

    /**
     * Cette méthode est appelée lorsqu'un appel au service `/controleur/charger-livraisons` a réussi.
     * Elle demande alors la vue de recharger les livraisons.
     * <br/> Voir {{#crossLink "Controleur/chargerLivraisons:method"}}{{/crossLink}}
     * @method _chargerLivraisonsOk
     * @param {String} msg Réponse du service (aucune pour ce service).
     * @private
     */
    this._chargerLivraisonsOk = function(msg){
        vue.nouvellesLivraisons();
    };

    /**
     * Cette méthode est appelée lorsqu'un appel au service `/controleur/charger-livraisons` a échoué.
     * Elle ferme alors l'écran de chargement et affiche un message d'erreur.
     * <br/> Voir {{#crossLink "Controleur/chargerLivraisons:method"}}{{/crossLink}}
     * @method _chargerLivraisonsErr
     * @param {String} msg Message d'erreur retourné par le service.
     * @private
     */
    this._chargerLivraisonsErr = function(msg){
        vue.fermerChargement();
        vue.erreur(msg);
    }.bind(this);

    // > Telecharger l'itinéraire en pdf

    /**
     * Cette méthode utilise la librairie jsPDF pour générer un itinéraire au format PDF qui
     * s'ouvre dans un nouvel onglet. Ce PDF est généré à partir de la div `FDR`(pour feuille de
     * route) du code HTML.
     * @method clicTelechargerInitineraire
     */
    this.clicTelechargerInitineraire = function(){
        var pdf = new jsPDF();
        var elementHandler = {
            /**
             * Description
             * @param {} element
             * @param {} renderer
             * @return Literal
             */
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

    /**
     * Affiche l'écran de chargement puis appelle le service `/controleur/calculer-itineraire` qui permet de lancer le calcul
     * de l'itinéraire côté serveur.
     * <br/>Si l'appel au service réussit, càd que le calcul est réussi, la méthode 
     * {{#crossLink "Controleur/_clicCalculOk:method"}}{{/crossLink}} est appelée 
     * ( {{#crossLink "Controleur/_clicCalculErr:method"}}{{/crossLink}} dans le cas contraire).
     * @method _clicCalcul
     */
    this._clicCalcul = function () {
        vue.afficherChargement("Calcul en cours, veuillez patienter...");
        com.appelService('controleur/calculer-itineraire','',
                        this._clicCalculOk,
                        this._clicCalculErr, true);
    }.bind(this);

    /**
     * Cette méthode est appelée lorsqu'un appel au service `/controleur/calculer-itineraire` a réussi.
     * Elle demande alors la vue de recharger l'itinéraire.
     * <br/> Voir {{#crossLink "Controleur/_clicCalcul:method"}}{{/crossLink}}
     * @method _clicCalculOk
     * @param {String} msg Réponse du service (aucune pour ce service).
     * @private
     */
    this._clicCalculOk = function(msg) {
        vue.nouvelItineraire();
    }.bind(this);

    /**
     * Cette méthode est appelée lorsqu'un appel au service `/controleur/calculer-itineraire` a échoué.
     * Elle ferme alors l'écran de chargement et affiche un message d'erreur.
     * <br/> Voir {{#crossLink "Controleur/_clicCalcul:method"}}{{/crossLink}}
     * @method _clicCalculErr
     * @param {String} msg Message d'erreur retourné par le service.
     * @private
     */
    this._clicCalculErr = function(msg) {
        vue.fermerChargement();
        vue.erreur(msg);
    }.bind(this);

    // > Suppression d'une livraison

    /**
     * Affiche l'écran de chargement puis appelle le service `/controleur/supprimer-livraison` qui permet de supprimer une livraison.
     * <br/>Si l'appel au service réussit, càd que la suppression s'est bien déroulée, la méthode 
     * {{#crossLink "Controleur/_suppressionOk:method"}}{{/crossLink}} est appelée 
     * ( {{#crossLink "Controleur/_suppressionErr:method"}}{{/crossLink}} dans le cas contraire).
     * 
     * @method demandeDeSuppression
     * @param {Number} id ID de la livraison à supprimer.
     */
    this.demandeDeSuppression = function(id) {
        vue.afficherChargement("Suppression en cours, veuillez patienter...");
        com.appelService("controleur/supprimer-livraison",""+id,this._suppressionOk, this._suppressionErr, true);
    };   

    /**
     * Cette méthode est appelée lorsqu'un appel au service `/controleur/supprimer-livraison` a réussi.
     * Elle demande alors la vue de recharger l'itinéraire ainsi que les livraisons.
     * <br/> Voir {{#crossLink "Controleur/demandeDeSuppression:method"}}{{/crossLink}}
     * @method _suppressionOk
     * @param {String} msg Réponse du service (aucune pour ce service).
     * @private
     */
    this._suppressionOk = function(rep) {
        vue.nouvelItineraire(true);
    }; 

    /**
     * Cette méthode est appelée lorsqu'un appel au service `/controleur/supprimer-livraison` a échoué.
     * Elle ferme alors l'écran de chargement et affiche un message d'erreur.
     * <br/> Voir {{#crossLink "Controleur/demandeDeSuppression:method"}}{{/crossLink}}
     * @method _suppressionErr
     * @param {String} msg Message d'erreur retourné par le service.
     * @private
     */
    this._suppressionErr = function(msg) {
        vue.fermerChargement();
        vue.erreur(msg);
    };

    // > Ajout d'une livraison

    /**
     * 
     * Affiche l'écran de chargement puis appelle le service `/controleur/ajouter-livraison` qui permet d'ajouter une livraison.
     * <br/>Si l'appel au service réussit, càd que l'ajout s'est bien déroulé, la méthode 
     * {{#crossLink "Controleur/_ajouterLivraisonOk:method"}}{{/crossLink}} est appelée 
     * ( {{#crossLink "Controleur/_ajouterLivraisonErr:method"}}{{/crossLink}} dans le cas contraire).
     * 
     * @method ajouterLivraison
     * @param {} idIntersection ID de l'intersection à laquelle on veut livrer.
     * @param {} idLivraison ID de la livraison devant précéder celle que l'on crée.
     * @param {} idClient ID du client à livrer. 
     */
    this.ajouterLivraison = function(idIntersection, idLivraison, idClient){
        vue.afficherChargement("Ajout en cours, veuillez patienter...");
        var param = idIntersection+"\n"+idClient+"\n"+idLivraison;
        com.appelService("controleur/ajouter-livraison",param,this._ajouterLivraisonOk,this._ajouterLivraisonErr,true);
    }

    /**
     * Cette méthode est appelée lorsqu'un appel au service `/controleur/ajouter-livraison` a réussi.
     * Elle demande alors la vue de recharger l'itinéraire ainsi que les livraisons.
     * <br/> Voir {{#crossLink "Controleur/ajouterLivraison:method"}}{{/crossLink}}
     * @method nouvelItineraire
     * @param {String} msg Réponse du service (aucune pour ce service).
     * @private
     */
    this._ajouterLivraisonOk = function(rep) {
        vue.nouvelItineraire(true);
    };

    /**
     * Cette méthode est appelée lorsqu'un appel au service `/controleur/ajouter-livraison` a échoué.
     * Elle ferme alors l'écran de chargement et affiche un message d'erreur.
     * <br/> Voir {{#crossLink "Controleur/ajouterLivraison:method"}}{{/crossLink}}
     * @method _ajouterLivraisonErr
     * @param {String} msg Message d'erreur retourné par le service.
     * @private
     */
    this._ajouterLivraisonErr = function(msg) {
        vue.fermerChargement();
        vue.erreur(msg);
    };

    ///////////////////////////////////////////////////////////////////////////////////
    // >> Initialisation de l'objet

    /**
     * Instance de la classe {{#crossLink "Vue"}}{{/crossLink}} associée au modèle.
     * @property vue
     * @type Vue
     */
    this.vue = vue = new Vue(this, com);

}