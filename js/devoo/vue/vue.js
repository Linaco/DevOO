
/**
 * Vue de l'application, gère toutes les informations graphiques renvoyées à l'utilsateur.
 * @module vue
 */

///////////////////////////////////////////////////////////////////////////////////////
// >>> Class Vue

/**
 * Vue de l'application, gère toutes les informations graphiques renvoyées à l'utilsateur.
 * <br/>
 * L'application utilise Leaflet (plus les plugins suivants : label, easyButton, polylineDecorator),
 * Bootstrap et jsPDF.
 *
 * @class Vue
 * @author Robin Nicolet
 * @constructor
 */
function Vue(controleur, com){

    ///////////////////////////////////////////////////////////////////////////////////
    // >> attributs

    /**
     * Instance de la classe {{#crossLink "Com"}}{{/crossLink}} permettant de communiquer
     * avec le serveur.
     * @property com
     * @type Com
     */
    this.com = com;
    var com = this.com;

    /**
     * Instance de la classe {{#crossLink "Com"}}{{/crossLink}} permettant de transmettre
     * les interactions utilisateur.
     * @property ctrl
     * @type Controleur
     */
    this.ctrl = controleur;

    /**
     * Tableau de String. Banque de couleurs des plages horaires
     * @property couleurPlages {String[]}
     */
    this.couleurPlages = ['#2974FF','#62FF29','#FF00FF','#00C8FF','pink','green','blue','white'];

    /**
     * Liste des intersection du plan
     * @property intersections {VueIntersection[]}
     */
    this.intersections = [];
    /**
     * Liste des routes du plan
     * @property routes {VueRoute[]}
     */
    this.routes = [];

    /**
     * Icone donné aux intersections associées à une livraison hors de sa plage horaire
     * @property warningIcon {Icon}
     */
    this.warningIcon = L.icon({
        iconUrl: 'http://icons.iconarchive.com/icons/custom-icon-design/flatastic-2/512/process-warning-icon.png',
        shadowUrl: null,

        iconSize:     [25, 25], // size of the icon
        shadowSize:   [0, 0], // size of the shadow
        iconAnchor:   [12, 10], // point of the icon which will correspond to marker's location
        shadowAnchor: [0, 0],  // the same for the shadow
        popupAnchor:  [-3, -76] // point from which the popup should open relative to the iconAnchor
    });

    /**
     * Vue de la feuille de route (PDF)
     * @property feuilleDeRoute {VueFeuilleDeRoute}
     */
    this.feuilleDeRoute = new VueFeuilleDeRoute(com, this);
    var feuilleDeRoute = this.feuilleDeRoute;

    /**
     * Vue de la légende (Panneau latéral)
     * @property vueLegende {VueLegende}
     */
    this.vueLegende = new VueLegende(com, this);

    ///////////////////////////////////////////////////////////////////////////////////
    // >> methodes

    // > Popups

    /**
     * Ouvre la popup d'erreur avec le message souhaité (voir modals Bootstrap)
     * @method erreur
     * @param msg {String} Message à afficher
     */
    this.erreur = function(msg){
        this.fermerInfo();
        document.getElementById("msg-erreur").textContent = msg;
        $('#modal-erreur').modal('show');
    }

    /**
     * Ferme la popup d'erreur (voir modals Bootstrap)
     * @method fermerErreur
     */
    this.fermerErreur = function(){
        $('#modal-erreur').modal('hide');
    }

    /**
     * Ouvre la popup d'info avec le message souhaité (voir modals Bootstrap)
     * @method info
     * @param msg {String} Message à afficher
     */
    this.info = function(msg){
        this.fermerErreur();
        document.getElementById("msg-info").textContent = msg;
        $('#modal-info').modal('show');
    }

    /**
     * Ferme la popup d'info (voir modals Bootstrap)
     * @method fermerInfo
     */
    this.fermerInfo = function(){
        $('#modal-info').modal('hide');
    }

    /**
     * Affiche l'écran de chargement avec le message souhaité (voir modals Bootstrap)
     * @method afficherChargement
     * @param msg {String} Message à afficher
     */
    this.afficherChargement = function(msg){
        document.getElementById("msg-chargement").textContent
            = msg;
            console.log("ouvrir");
        //$('#modalChargement').modal({backdrop: 'static', keyboard: false});
        //$("#modalChargement").css("visibility", "visible");
        $('#modalChargement').modal('show');
    }

    /**
     * Ferme l'écran de chargement' (voir modals Bootstrap)
     * @method fermerChargement
     */
    this.fermerChargement = function(){
        console.log("fermer");
        //console.log(document.getElementsByTagName(" bootstrap-backdrop"));
        $('.modal.in').modal('hide') ;
        $('#modalChargement').modal('hide');
        //$("#modalChargement").css("visibility", "hidden");
        //console.log(document.getElementsByTagName(" bootstrap-backdrop"));
    }

    // > Actions utilisateur

    /**
     * Appel le bon service pour recharger l'itinéraire.
     * @method nouvelItineraire
     * @param chargerLivraisons {boolean} faut-il recharger aussi les livraisons
     */
    this.nouvelItineraire = function(chargerLivraisons) {
            console.log(this.intersections.length);
        for(var j = 0; j < this.routes.length; ++j){
            //console.log("RAAAAZ");
            this.routes[j].razPassages();
        }
        if(chargerLivraisons){
            this.razLivraison();
            this.vueLegende.raz();
            com.appelService('modele/livraisons','',this.livraisonPuisItineraireOk,this.nouvelItineraireErr,true);
        } else {
            com.appelService('modele/itineraire','',this.nouvelItineraireOk,this.nouvelItineraireErr, true);   
        }
    };

    /**
     * Retour du service `/modele/livraisons`, on charge les livraisons sur le plan
     * puis on demande le nouvel itinéraire.
     * @method livraisonPuisItineraireOk
     * @param str {String} Reponse du service, pas utilisé
     * @private
     */
    this.livraisonPuisItineraireOk = function(str) {
        this.nouvellesLivraisonsOk(str, true);
        this.nouvelItineraire(false);
    }.bind(this);

    /**
     * Echec du service `/modele/itineraire`, on ferme l'écran de chargement
     * puis on affiche le message d'erreur.
     * @method nouvelItineraireErr
     * @param msg {String} Message d'erreur renvoyé par le service
     * @private
     */
    this.nouvelItineraireErr = function(msg) {
        this.fermerChargement();
        this.erreur(msg);
    }.bind(this);

    /**
     * Retour du service `/modele/itineraire`,
     * on parcours alors le XML reçu pour créer l'itinéraire.<br/>
     *
     *      <?xml version="1.0" encoding="UTF-8"?>
     *      <feuilleDeRoute>
     *          <itineraire>
     *              <etape heurePassage="0h00" secondesAttente="0" idIntersection="0" idPlageHoraire="0">
     *                  <livraison idClient="0" adresse="0"/>
     *              </etape>
     *          </itineraire>
     *          <livraisonsImpossibles>
     *              <livraison idClient="0" adresse="0"/>
     *          </livraisonsImpossibles>
     *      </feuilleDeRoute>
     *
     * @method nouvelItineraireOk
     * @param str {String} XML renvoyé par le service
     * @private
     */
    this.nouvelItineraireOk = function(str) {
        var parser=new DOMParser();
        var doc=parser.parseFromString(str,"text/xml");
        console.log("itineraire",doc);

        var fdr = doc.getElementsByTagName("feuilleDeRoute")[0];
        var etapes = fdr.getElementsByTagName("etape");
        var id1 = etapes[0].getAttribute("idIntersection");

        console.log("etapes",etapes.length);
        for(var i = 1; i < etapes.length; ++i){
            var id2 = etapes[i].getAttribute("idIntersection");
            var intersection = this.getIntersection(id2);
            var hdp = etapes[i].getAttribute("heurePassage");
            if(intersection.livraison)intersection.setHeurePassage(hdp);

            var route = this.getRoute(id1, id2);
            if(route){
                var idPlage = etapes[i-1].getAttribute("idPlageHoraire");
                //console.log("route", route);
                //console.log("plage", idPlage,this.couleurPlages[idPlage]);
                var couleur = this.couleurPlages[idPlage];
                if(couleur == null)couleur = "red";
                route.ajouterPassage(0,  couleur );
            }
            id1 = id2;
        }

        var impossible = doc.getElementsByTagName("livraisonsImpossibles")[0];
        var livs = impossible.getElementsByTagName("livraison");
        for(var i = 0; i < livs.length; ++i){
            var liv = livs[i];
            var it = this.getIntersection(liv.getAttribute("idIntersection"));
            it.ajouterMarker(this.warningIcon);
        }

        this.fermerChargement();
        this.masquer();
        this.afficher();

        this.feuilleDeRoute.chargerFeuille(doc);

    }.bind(this);

    /**
     * Détruit les livraisons, la légende, appelle le bon service pour recharger les livraisons.
     * @method nouvellesLivraisons
     */
    this.nouvellesLivraisons = function(){
        this.razLivraison();
        this.vueLegende.raz();
        com.appelService('modele/livraisons','',this.nouvellesLivraisonsOk,this.nouvellesLivraisonsErr, true);
    };

    /**
     * Echec du service `/modele/livraisons`, on ferme l'écran de chargement
     * puis on affiche le message d'erreur.
     * @method nouvellesLivraisonsErr
     * @param msg {String} Message d'erreur renvoyé par le service
     * @private
     */
    this.nouvellesLivraisonsErr = function(msg) {
        this.fermerChargement();
        this.erreur(msg);
    }.bind(this);

    /**
     * Retour du service `/modele/livraisons`,
     * on parcours alors le XML reçu pour créer les livraisons.<br/>
     *
     *      <?xml version="1.0" encoding="UTF-8"?>
     *      <livraisons idEntrepot="0">
     *          <plage debut="0h00" fin="23h59">
     *              <livraison idClient="0" adresse="0"/>
     *          </plage>
     *      </livraisons>
     *
     * @method nouvellesLivraisonsOk
     * @param str {String} XML renvoyé par le service
     * @param laisserChargement {boolean} Empeche la fermeture de l'écran de chargement
     * @private
     */
    this.nouvellesLivraisonsOk = function(str, laisserChargement) {
        var parser=new DOMParser();
        var doc=parser.parseFromString(str,"text/xml");
        console.log("livraisons",doc);

        var entrepot = doc.getElementsByTagName("livraisons")[0].getAttribute("idEntrepot");
        var itEntrepot  = this.getIntersection(entrepot);
        if(itEntrepot)itEntrepot.setEntrepot(true);

        var plages = doc.getElementsByTagName("plage");
        for(var i = 0; i < plages.length; ++i){
            var plageTxt = plages[i].getAttribute("debut")
                    + "-" + plages[i].getAttribute("fin") + "h";
            var livs = plages[i].getElementsByTagName("livraison");
            for(var j = 0; j < livs.length; ++j){
                var idIntersection = livs[j].getAttribute("idIntersection");
                var it = this.getIntersection(idIntersection);
                if(it){
                    it.setLivraison(livs[j].getAttribute("id"),
                                    livs[j].getAttribute("idClient"),
                                    plageTxt,
                                    null);
                }
            }
        }

        this.vueLegende.displayPlagesHoraires(); // affiche les plages horaires dès le chargement
        this.vueLegende.displayLivraisons();
        if(!laisserChargement) {
            this.fermerChargement();
        }
        this.afficher();
    }.bind(this);

    /**
     * Détruit les routes, les intersections, appelle le bon service pour recharger le plan.
     * @method nouveauPlan
     */
    this.nouveauPlan = function(){
        this.masquer();
        this.intersections = [];
        this.routes = [];
        com.appelService('modele/plan','',this.retourModelePlanOk,this.retourModelePlanErr, true);
    };

    /**
     * Echec du service `/modele/plan`, on ferme l'écran de chargement
     * puis on affiche le message d'erreur.
     * @method retourModelePlanErr
     * @param msg {String} Message d'erreur renvoyé par le service
     * @private
     */
    this.retourModelePlanErr = function(msg) {
        this.fermerChargement();
        this.erreur(msg);
    };

    /**
     * Retour du service `/modele/plan`,
     * on parcours alors le XML reçu pour créer le plan.<br/>
     *
     *      <?xml version="1.0" encoding="UTF-8"?>
     *      <plan>
     *          <intersection id="0" x="0" y="0">
     *              <route id="0" idDestination="0" nom="nom de la route"/>
     *          </intersection>
     *      </plan>
     *
     * @method retourModelePlanOk
     * @param str {String} XML renvoyé par le service
     * @private
     */
    this.retourModelePlanOk = function(str){
        var parser=new DOMParser();
        var doc=parser.parseFromString(str,"text/xml");

        var xmin, xmax, ymin, ymax;

        var plan = doc.getElementsByTagName("plan")[0];
        console.log("plan",plan);
        var its = plan.getElementsByTagName("intersection");
        for( var i = 0; i < its.length; ++i){
            var it = its[i];
            var id = it.getAttribute("id");
            var x = -it.getAttribute("x")/500;
            var y = it.getAttribute("y")/500;
            if( i == 0 ){
                xmin = xmax = x;
                ymin = ymax = y;
            } else {
                if( x < xmin){
                    xmin = x;
                } else if( x > xmax){
                    xmax = x;
                }
                if( y < ymin){
                    ymin = y;
                } else if( y > ymax){
                    ymax = y;
                }
            }
            this.ajouterIntersection([x,y],id);
        }
        for( var i = 0; i < its.length; ++i){
            var it = its[i];
            var id1 = it.getAttribute("id");
            var routes = it.getElementsByTagName("route");

            for( var j = 0; j < routes.length; ++j){
                var route = routes[j];
                var idRoute = route.getAttribute("id");
                var id2 = route.getAttribute("idDestination");
                var nom = route.getAttribute("nom");
                this.ajouterRoute(id1,id2,nom);
            }
        }
        var dw = (xmax - xmin)*0.25, dh = (ymax - ymin)*0.25;
        map.setMaxBounds([[xmin-dw,ymin-dh],[xmax+dw,ymax+dh]]);
        map.fitBounds([[xmin,ymin],[xmax,ymax]]);

        this.afficher();
        this.fermerChargement();
    }.bind(this);

    // > Affichage

    /**
     * Affiche les éléments du plan dans la map Leaflet.
     * @method afficher
     */
    this.afficher = function() {
        for( var i = 0; i < this.routes.length; ++i ){
            this.routes[i].afficher();
        }
        for( var i = 0; i < this.intersections.length; ++i ){
            this.intersections[i].afficher();
        }
    };

    /**
     * Enlève les éléments du plan de la map Leaflet.
     * @method masquer
     */
    this.masquer = function() {
        for( var i = 0; i < this.intersections.length; ++i ){
            this.intersections[i].masquer();
        }
        for( var i = 0; i < this.routes.length; ++i ){
            this.routes[i].masquer();
        }
    };

    // > Opérations sur les objets

    /**
     * Réinitialise les données de livraison. Remet un plan vierge.
     * @method razLivraison
     */
    this.razLivraison = function() {
        for(var i = 0; i < this.intersections.length; ++i){
            this.intersections[i].razLivraison();
            var routes = this.intersections[i].routes;
            for(var j = 0; j < this.routes.length; ++j){
                this.routes[j].razPassages();
            }
        }
    };

    /**
     * Crée et ajoute une intersection à la liste des intersections.
     * @method ajouterIntersection
     * @param pos {LngLat} position sur la map (cf Leaflet)
     * @param id {Integer} ID de l'intersection
     */
    this.ajouterIntersection = function(pos, id) {
        return this.intersections[this.intersections.length]
            = new VueIntersection(pos,id);
    }

    /**
     * Crée et ajoute une route à la liste des routes.
     * @method ajouterRoute
     * @param i1 {VueIntersection} Départ de la route
     * @param i2 {VueIntersection} Arrivée de la route
     * @param nom {String} Nom de la route
     */
    this.ajouterRoute = function(i1,i2,nom){
        var a = this.getIntersection(i1),
            b = this.getIntersection(i2);
        if ( a && b ) return this.routes[this.routes.length] 
            = new VueRoute(a,b,nom);
        return null;
    }

    /**
     * Renvoie la VueIntersection correspondant à cet ID
     * @method getIntersection
     * @param id {Integer} ID de l'intersection
     * @return {VueIntersection}
     */
    this.getIntersection = function(id) {
        for( var i = 0; i < this.intersections.length; ++i ){
            if( this.intersections[i].id == id){
                return this.intersections[i];
            }
        }
        return null;
    }

    /**
     * Renvoie la route, si elle existe, qui va de l'intersection dont l'id vaut id1
     * à l'intersection dont l'id vaut id2
     * @method getRoute
     * @param id1 {Integer} ID départ
     * @param id2 {Integer} ID arrivée
     * @return {VueRoute}
     */
    this.getRoute = function(id1,id2) {
        var it1 = this.getIntersection(id1);
        if(it1){
            return it1.getRouteIntersection(id2);
        }
        return null;
    };

    // > Evenements 

    /**
     * Signale au controleur que l'utilisateur veut supprimer une livraison.
     * @method _livraisonSupprimee
     * @param idLivraison {Integer} ID de la livraison.
     * @param idIntersection {Integer} ID de l'intersection associée à la livraison.
     */
    this._livraisonSupprimee = function(idLivraison, idIntersection){
        var it = this.getIntersection(idIntersection);
        it.closePopup();
        console.log("livraison supprimée",idLivraison, it);
        this.ctrl.demandeDeSuppression(idLivraison);
    }

    /**
     * Passe les intersections où il y a une livraison en mode `séléctionnable` et leur
     * associe un écouteur de clic.
     * @method _ajoutLivraison
     * @param idIntersection {Integer} ID de l'intersection où l'on veut la livraison.
     */
    this._ajoutLivraison = function(idIntersection) {
        var value = document.getElementById("input-client").value;
        if(!value){
            this.erreur("Veuillez rentrer un id client avant de pouvoir ajouter votre livraison.");
            return;
        }

        var it = this.getIntersection(idIntersection);
        it.closePopup();
        this.info("Veuillez sélectionner la livraison après laquelle votre nouvelle livraison sera effectuée.");
        for( var i = 0; i < this.intersections.length; ++i){
            var it = this.intersections[i];
            if(!it.livraison){
                it.etatDesactive();
            } else {
                it.etatSelectionnable();
                it.activerClic(this.genererFctPopup(idIntersection, it.livraison.id, value));
            }
        }
    }.bind(this);

    /**
     * Renvoie une fonction bien formée pour l'ajout de livraison.
     * @method genererFctPopup
     * @param idInter {Integer} ID de l'intersection où l'on veut la livraison.
     * @param idLiv {Integer} ID de la livraison qui doit précéder celle que l'on crée.
     * @param idClient {Integer} ID du client à livrer.
     */
    this.genererFctPopup = function(idInter, idLiv, idClient) {
        return function() {
            ctrl.vue._livraisonPrecedenteOk(idInter, idLiv, idClient);
        };
    };

    /**
     * Notifie le controleur que l'utilisateur veut ajouter une livraison.
     * @method genererFctPopup
     * @param idInter {Integer} ID de l'intersection où l'on veut la livraison.
     * @param livraison {Integer} ID de la livraison qui doit précéder celle que l'on crée.
     * @param client {Integer} ID du client à livrer.
     */
    this._livraisonPrecedenteOk = function(idIntersection, livraison, client){
        // Pas besoin de tout remettre, on va réafficher
        this.ctrl.ajouterLivraison(idIntersection,livraison,client);
    }

    ///////////////////////////////////////////////////////////////////////////////////
    // >> Initialisation

    // > Création de la map 

    this.map = L.map('map',{maxBounds:[[-0.1,-0.1],[0.9,0.9]],zoomControl:false}).setView([0.4, 0.4], 10);
    
    // > Création des controles 

    L.control.attribution({prefix: 'Projet DevOO - INSA de Lyon - H4104 - 2014', position: 'topleft'}).addTo(this.map);
    L.control.zoom({position:'topright', zoomInTitle: "Zoomer"}).addTo(this.map);
    L.easyButton('fa-arrow-circle-left', controleur.annuler, 'Undo', this.map);
    L.easyButton('fa-arrow-circle-right', controleur.retablir, 'Redo', this.map);
    L.easyButton('fa-road', controleur.clicChargerPlan, 'Charger un plan', this.map).setPosition('bottomleft');
    document.getElementById('charger-plan').addEventListener('change', controleur.chargerPlan, false);
    L.easyButton('fa-cubes', controleur.clicChargerLivraisons, 'Charger les livraison', this.map).setPosition('bottomleft');
    document.getElementById('charger-livraisons').addEventListener('change', controleur.chargerLivraisons, false);
    this.controlCalcul = L.easyButton('fa-refresh', controleur._clicCalcul, "Calculer l'itinéraire", this.map).setPosition('bottomleft');
    var controlFDR = L.easyButton('fa-file-text', controleur.clicTelechargerInitineraire, "Télécharger la feuille de route", this.map).setPosition('bottomright');

    // > Initialisation > Préparation des dialogues

    $('#modalChargement').modal({backdrop: 'static', keyboard: false, show:false});
    $('#modal-info').modal({backdrop: false, show: false});
    $('#modal-erreur').modal({backdrop: false, show: false});
}

///////////////////////////////////////////////////////////////////////////////////////
// >>> Class VueLegende

/**
 * Vue de la légende, gère la partie droite de l'IHM
 *
 * @class VueLegende
 * @author Anthony Laou-Hine
 * @constructor
 */
function VueLegende(com, vue){

    ///////////////////////////////////////////////////////////////////////////////////
    // >> Attributs 

    this.vue = vue;
    this.com = com;
    this.lateral = document.getElementById('lateral');
    this.couleurPlages = ['#2974FF','#62FF29','#FF00FF','#00C8FF'];

    ///////////////////////////////////////////////////////////////////////////////////
    // >> Methodes

    this.displayPlagesHoraires = function() {
        this.com.appelService('modele/plagesHoraires', '', this._plageOk, this._plageErr, true);
    };

    this._plageOk = function(reponse){

        this.raz();

        var parser=new DOMParser();
        var doc=parser.parseFromString(reponse,"text/xml");
        var liste = doc.getElementsByTagName('plage');

        var listePlages = document.createElement('div');
        listePlages.id = "listePlages";
        lateral.appendChild(listePlages);

        var titre = document.createElement('h3');
        titre.innerHTML = 'Liste plages horaires';
        listePlages.appendChild(titre);

        for (var i=0; i < liste.length; i++) {
            var p = document.createElement('p');
            p.innerHTML = liste[i].getAttribute('debut') + ' - ';
            p.innerHTML += liste[i].getAttribute('fin');
            listePlages.appendChild(p);

            var colorId = liste[i].getAttribute('id');
            p.style.color = this.couleurPlages[colorId];
        }
    }.bind(this);

    this._plageErr = function(msgErreur) {
        var msg = document.createElement('p');
        msg.value = 'Erreur de chargement des plages horaires.';
        msg.id = 'errorListePlages';
        this.lateral.appendChild(msg);
    }.bind(this);

    this.displayLivraisons = function() {
        this.com.appelService('modele/livraisons', '', this._livraisonsOk, this._livraisonsErr, true);
    };

    this._livraisonsOk = function(reponse) {

        var parser=new DOMParser();
        var doc=parser.parseFromString(reponse,"text/xml");
        var liste = doc.getElementsByTagName('plage');

        var listeLivraisons = document.createElement('div');
        listeLivraisons.id = "listeLivraisons";
        lateral.appendChild(listeLivraisons);

        var titre = document.createElement('h3');
        titre.innerHTML = 'Liste des livraisons';
        listeLivraisons.appendChild(titre);;
        for (var i=0; i < liste.length; i++) {
            var p = document.createElement('p');
            p.innerHTML = liste[i].getAttribute('debut') + ' - ';
            p.innerHTML += liste[i].getAttribute('fin') + ' : <br />';
            var sous_liste = liste[i].getElementsByTagName('livraison');

            for(var j=0; j < sous_liste.length; j++) {
                p.innerHTML += 'Client : ' + sous_liste[j].getAttribute('idClient') + ' -- ';
                p.innerHTML += 'Adresse : ' + sous_liste[j].getAttribute('idIntersection');
                p.innerHTML += '<br />';
            }
            p.innerHTML += '<br />';
            
            listeLivraisons.appendChild(p);
        }

    }.bind(this);

    this._livraisonsErr = function(msgErreur) {
        var msg = document.createElement('p');
        msg.value = 'Erreur de chargement des livraisons.';
        msg.id = 'errorListeLivraisons';
        this.lateral.appendChild(msg);
    }.bind(this);

    this.raz = function() {
        while (this.lateral.firstChild) {
            this.lateral.removeChild(this.lateral.firstChild);
        }
    };
}

///////////////////////////////////////////////////////////////////////////////////////
// >>> Class VueIntersection

/**
 * Vue graphique d'une intersection, d'un noeud du graphe
 *
 * @class VueIntersection
 * @author Robin Nicolet
 * @constructor
 */
function VueIntersection(pos, id){

    ///////////////////////////////////////////////////////////////////////////////////
    // >> Attributs

    /**
     * Position géographique
     * @property pos {[x,y]}
     */
    this.pos = pos;
    /**
     * ID de l'intersection
     * @property id {Integer}
     */
    this.id = id;

    this.paramDefaut = {color: '#fff', opacity: 0.5, fillColor: '#fff', fillOpacity: 0.5};
    this.paramLivraison = {color: '#0f0', opacity: 0.5, fillColor: '#ff0', fillOpacity: 0.5};
    this.paramEntrepot = {color: '#0ff', opacity: 0.5, fillColor: '#0ff', fillOpacity: 0.5};
    this.paramSelec = {color: 'red', opacity: 0.8, fillColor: 'yellow', fillOpacity: 0.8};
    this.paramDesactive = {color: '#a0a0a0', fillColor: 'a0a0a0'};
    this.rayonDefaut = 520;
    this.rayonLivraison = 1000;
    this.rayonEntrepot = 1500;
    this.rayon = this.rayonDefaut;

    this.etat = "standard";
    this.marker = null;

    this.divPopup = null;

    this.cercle;

    this.livraison = null;
    this.entrepot = false;
    this.routesSortantes = [];

    ///////////////////////////////////////////////////////////////////////////////////
    // >> Methodes

    this.setLivraison = function(id, idClient, plage, hdp) {
        console.log("setLivraison");
        this.livraison = {
            id: id,
            idClient: idClient,
            plage: plage,
            hdp: hdp
        };
        this.setRayon(this.rayonLivraison);
        var div = document.getElementById('popup-livraison').cloneNode(true);
        //console.log("div",div);
        div.getElementsByTagName("client")[0].textContent = this.livraison.idClient;
        if(hdp)div.getElementsByTagName("hdp")[0].textContent = this.livraison.hdp;
        div.getElementsByTagName("plage")[0].textContent = plage;
        div.getElementsByTagName("button")[0].setAttribute("onclick","ctrl.vue._livraisonSupprimee("+this.livraison.id+","+this.id+");");
        this.cercle.bindPopup(div);
        this.divPopup = div;
        this.majEtat();
        return this;
    }

    this.setHeurePassage = function(hdp) {
        var div = this.divPopup;
        div.getElementsByTagName("hdp")[0].textContent = hdp;
    };

    this.setEntrepot = function(bool) {
        this.entrepot = bool ? true : false;
        if(this.entrepot){
            this.setRayon(this.rayonEntrepot);
            this.majEtat();
        }
    };

    this.razLivraison = function() {
        this.livraison = null;
        this.enleverMarker();
        this.marker = null;
        this.entrepot = false;
        this.setRayon(this.rayonDefaut);
        this.cercle.unbindPopup();
        this.etatStandard();
        this.majEtat();
    };

    this.getRouteIntersection = function(idIntersection) {
        for(var i = 0; i < this.routesSortantes.length; ++i){
            if(this.routesSortantes[i].B.id == idIntersection){
                return this.routesSortantes[i];
            }
        }
        return null;
    };

    this.setRayon = function(r){
        this.rayon = r;
        this.cercle.setRadius(r);
    }

    this.closePopup = function() {
        this.cercle.closePopup();
    };

    this.majEtat = function(){
        switch(this.etat){
            case "standard":
                this.etatStandard();
                break;
            case "selectionnable":
                this.etatSelectionnable();
                break;
            case "desactive":
                this.etatDesactive();
                break;
        }
    };

    this.ajouterRouteSortante = function(route) {
        //console.log("ajouterRouteSortante : route ", route);
        this.routesSortantes[this.routesSortantes.length] = route;
    };

    this.etatSelectionnable = function(){
        this.cercle.setStyle(this.paramSelec);
        this.etat = "selectionnable";
        
        this.activerClic(this._clicSelectionnable);
        return this;
    };

    this.etatStandard = function(){
        this.cercle.setStyle(this.livraison ? this.paramLivraison :
                            (this.entrepot ? this.paramEntrepot : this.paramDefaut));
        this.etat = "standard";

        if(!this.livraison){
            this.cercle.unbindPopup();
            var div = document.getElementById('popup-ajout').cloneNode(true);
            div.getElementsByTagName("adresse")[0].textContent = this.id;
            div.getElementsByTagName("button")[0].setAttribute("onclick","ctrl.vue._ajoutLivraison("+this.id+");");
            this.cercle.bindPopup(div);
        }
        this.activerClic(this._clicStandard);
        return this;
    };

    this.etatDesactive = function(){
        this.cercle.setStyle(this.paramDesactive);
        this.etat = "desactive";
        this.desactiverClic();
        return this;
    };

    this.desactiverClic = function() {
        this.cercle.off("click");
        return this;
    }

    this.activerClic = function(fct){
        this.desactiverClic();
        f = fct ? fct : this._clicStandard;
        this.cercle.on("click",f,this);
        return this;
    }

    this.afficher = function(){
        this.cercle.addTo(map);
        if(this.marker){
            this.marker.addTo(map);
        }
        return this;
    }

    this.masquer = function(){
        map.removeLayer(this.cercle);
        if(this.marker){
            this.enleverMarker();
        }
        return this;
    }

    this.enleverMarker = function() {
        if(this.marker)map.removeLayer(this.marker);
    };
    this._clicMarker = function() {
        this.cercle.fire("click");
    };

    this.ajouterMarker = function(warning) {
        console.log("Ajouter Marker",warning);
        console.log(this.cercle.getLatLng());
        this.marker = L.marker(this.cercle.getLatLng(),{icon:warning})
                    .on('click',this._clicMarker,this)
                    .addTo(map);
    };

    this._clicSelectionnable = function() {
        console.log("clic selectionnable", this);
        //this.etatStandard();
    }
    this._clicStandard = function() {
        console.log("clic standard",this);
        if(this.livraison){
            this.cercle.openPopup();
            console.log(this.livraison);
        } else {
            this.cercle.openPopup();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////
    // >> Initialisation

    this.cercle = L.circle(pos, this.rayonDefaut, this.paramDefaut);

    return this;
}

///////////////////////////////////////////////////////////////////////////////////////
// >>> Class VueRoute

/**
 * Vue graphique d'une route, d'un tronçon du graphe
 *
 * @class VueRoute
 * @author Robin Nicolet
 * @constructor
 */
function VueRoute(intersec1, intersec2, nom){
    
    ///////////////////////////////////////////////////////////////////////////////////
    // >> Attributs

    /**
     * Valeurs par défaut pour les arcs
     *
     *      {ecartArc, ecartSuivant, nbLigne, couleur}
     *
     * @property defaut {Object}
     */
    this.defaut = {
        ecartArc: 0.05,
        ecartSuivant : 0.06,
        nbLigne: 20,
        couleur: '#5f5f5f'
    }

    /**
     * Intersection de départ
     * @property A {VueIntersection}
     */
    this.A = intersec1;

    /**
     * Intersection d'arrivée
     * @property B {VueIntersection}
     */
    this.B = intersec2;

    /**
     * Nom de la route
     * @property nom {String}
     */
    this.nom = nom;

    /**
     * Options par défaut pour une route dans Leaflet
     * @property paramDefaut {Object}
     */
    this.paramDefaut = {weight:5,color: this.defaut.couleur ,opacity:0.8};
    
    /**
     * Objet Polyline Leaflet représentant la route 
     * @property ligneBase {L.Polyline}
     */
    this.ligneBase;

    /**
     * Tableau d'objets Polyline Leaflet représentant les différents passages sur cette route.
     * @property ligneBase {L.Polyline[]}
     */
    this.passages = [];

    ///////////////////////////////////////////////////////////////////////////////////
    // >> Methodes

    // > Setters

    /**
     * Change le nom de la route.
     * @method setNom
     * @param nom {String} nom de la route.
     * @return {VueRoute} Retourne l'objet courant.
     */
    this.setNom = function(nom) {
        this.ligneBase.unbindLabel();
        this.nom = nom;
        this.ligneBase.bindLabel(nom);
        return this;
    };

    // > Affichage

    /**
     * Ajoute à la map Leaflet
     * @method afficher
     * @return {VueRoute} Retourne l'objet courant.
     */
    this.afficher = function(){
        this.ligneBase.addTo(map);
        for( var i = 0; i < this.passages.length; ++i){
            this.passages[i].addTo(map);
        }
        this.decorateurSens.addTo(map);
        return this;
    }

    /**
     * Enlève de la map Leaflet
     * @method masquer
     * @return {VueRoute} Retourne l'objet courant.
     */
    this.masquer = function(){
        map.removeLayer(this.ligneBase);
        map.removeLayer(this.decorateurSens);
        for( var i=0; i < this.passages.length; ++i){
            map.removeLayer(this.passages[i]);
        }
        return this;
    }

    // > Evenement

    /**
     * Gestion du `click`.
     * @method _clic
     */
    this._clic = function(){
        console.log('clic',this);
    }.bind(this);

    /**
     * Gestion du `mouseover`. On change la couleur de la route.
     * @method _mouseover
     */
    this._mouseover = function() {
        this.ligneBase.setStyle({color:"#0f0"});
    }.bind(this);

    /**
     * Gestion du `mouseout`. On remet la couleur de la route.
     * @method _mouseout
     */
    this._mouseout = function() {
        this.ligneBase.setStyle({color: '#5f5f5f'});
    }.bind(this);

    // > Modification des objets

    this.ajouterPassage = function(idLivraison, couleur) {
        var path = ArcMaker.arcPath(this.A.pos, this.B.pos,
                this.defaut.ecartArc + this.defaut.ecartSuivant*(1+this.passages.length),
                this.defaut.nbLigne);
        var opt = this.paramDefaut;
        opt.color = couleur;
        opt.opacity = 0.9;
        opt.idLivraison = idLivraison;
        var ligne = L.polyline(path, opt);
        ligne.on('mouseover', this._mouseover, this);
        ligne.on('mouseout', this._mouseout, this);
        this.passages[this.passages.length] = ligne;
        return this;
    }

    this.razPassages = function() {
        for (var i = this.passages.length - 1; i >= 0; i--) {
            map.removeLayer(this.passages[i]);
        }
        this.passages = [];
    };

    ///////////////////////////////////////////////////////////////////////////////////
    // >> Initialisation

    this.A.ajouterRouteSortante(this);

    this.path = ArcMaker.arcPath(this.A.pos, this.B.pos,
                this.defaut.ecartArc,
                this.defaut.nbLigne);

    this.ligneBase = L.polyline(this.path,this.paramDefaut)
        .on("mouseover", this._mouseover)
        .on("mouseout", this._mouseout)
        .on('click', this._clic, this);

    this.decorateurSens = L.polylineDecorator(this.ligneBase, {
        patterns: [
            // define a pattern of 10px-wide dashes, repeated every 20px on the line 
            {offset: "50%", repeat: '100%', symbol: new L.Symbol.ArrowHead ({pixelSize: 10, headAngle:40, pathOptions: {opacity:0.5,fillOpacity:0.2,weight:1,color: "black",fillColor:"yellow",fillOpacity:0.8}})}
        ]
    });
    return this;
}
