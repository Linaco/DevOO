///////////////////////////////////////////////////
// Class Vue
function Vue(controleur, com){
    // attributs
    this.com = com;
    this.ctrl = controleur;
    var com = this.com;

    this.couleurPlages = ['#2974FF','#62FF29','#FF00FF','#00C8FF'];

    this.intersections = [];
    this.routes = [];
    this.itineraire;

    this.vueLegende = new VueLegende(com, this);

    // functions
    this.enableRedo = function(){
        console.log("Vue.enableRedo");
    };
    this.disableRedo = function(){
        console.log("Vue.disableRedo");
    }
    this.erreur = function(msg){
        this.fermerInfo();
        document.getElementById("msg-erreur").textContent = msg;
        $('#modal-erreur').modal('show');
    }
    this.fermerErreur = function(){
        $('#modal-erreur').modal('hide');
    }
    this.info = function(msg){
        this.fermerErreur();
        document.getElementById("msg-info").textContent = msg;
        $('#modal-info').modal('show');
    }
    this.fermerInfo = function(){
        $('#modal-info').modal('hide');
    }

    this.afficherChargement = function(msg){
        document.getElementById("msg-chargement").textContent
            = msg;
        $('#modalChargement').modal({backdrop: 'static', keyboard: false});
    }

    this.fermerChargement = function(){
        $('#modalChargement').modal('hide');
    }

    //visibilité
    this.nouvelItineraire = function(chargerLivraisons) {
        for(var i = 0; i < this.intersections.length; ++i){
            var routes = this.intersections[i].routes;
            for(var j = 0; j < this.routes.length; ++j){
                this.routes[j].razPassages();
            }
        }
        if(chargerLivraisons){
            com.appelService('modele/livraisons','',this.livraisonPuisItineraireOk,this.nouvelItineraireErr,true);
        } else {
            com.appelService('modele/itineraire','',this.nouvelItineraireOk,this.nouvelItineraireErr, true);   
        }
     };
    this.livraisonPuisItineraireOk = function(str) {
        this.nouvellesLivraisonsOk(str);
        this.afficherChargement("Récupération du nouvel itinéraire");
        com.appelService('modele/itineraire','',this.nouvelItineraireOk,this.nouvelItineraireErr, true);
    }
    this.nouvelItineraireErr = function(msg) {
        this.fermerChargement();
        this.erreur(msg);
    }.bind(this);
    this.nouvelItineraireOk = function(str) {
        var parser=new DOMParser();
        var doc=parser.parseFromString(str,"text/xml");
        console.log("itineraire",doc);

        var fdr = doc.getElementsByTagName("feuilleDeRoute")[0];
        var etapes = fdr.getElementsByTagName("etape");
        var id1 = etapes[0].getAttribute("idIntersection");

        for(var i = 1; i < etapes.length; ++i){
            var id2 = etapes[i].getAttribute("idIntersection");
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
        this.fermerChargement();
        this.masquer();
        this.afficher();
    }.bind(this);

    this.nouvellesLivraisons = function(){
        this.razLivraison();
        this.vueLegende.raz();
        com.appelService('modele/livraisons','',this.nouvellesLivraisonsOk,this.nouvellesLivraisonsErr, true);
    };

    /*<plage debut="8" fin="10">
        <livraison id="2" idClient="546" idIntersection="" />
    </plage>*/
    this.nouvellesLivraisonsErr = function(msg) {
        this.fermerChargement();
        this.erreur(msg);
    }.bind(this);
    this.nouvellesLivraisonsOk = function(str) {
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
        this.fermerChargement();
    }.bind(this);
    this.nouveauPlan = function(){
        this.masquer();
        this.intersections = [];
        this.routes = [];
        com.appelService('modele/plan','',this.retourModelePlanOk,this.retourModelePlanErr, true);
    };
    this.retourModelePlanErr = function(msg) {
        this.fermerChargement();
        this.erreur(msg);
    };
    this.retourModelePlanOk = function(str){
        //this.info(str);
        var parser=new DOMParser();
        var doc=parser.parseFromString(str,"text/xml");

        var xmin, xmax, ymin, ymax;

        var plan = doc.getElementsByTagName("plan")[0];
        //console.log("plan",plan);
        var its = doc.getElementsByTagName("intersection");
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
                this.ajouterRoute(id1,id2);
            }
        }
        var dw = (xmax - xmin)*0.25, dh = (ymax - ymin)*0.25;
        map.setMaxBounds([[xmin-dw,ymin-dh],[xmax+dw,ymax+dh]]);
        map.fitBounds([[xmin,ymin],[xmax,ymax]]);

        this.afficher();
        this.fermerChargement();
    }.bind(this);

    this.afficher = function() {
        for( var i = 0; i < this.routes.length; ++i ){
            this.routes[i].afficher();
        }
        for( var i = 0; i < this.intersections.length; ++i ){
            this.intersections[i].afficher();
        }
    };

    this.masquer = function() {
        for( var i = 0; i < this.intersections.length; ++i ){
            this.intersections[i].masquer();
        }
        for( var i = 0; i < this.routes.length; ++i ){
            this.routes[i].masquer();
        }
    };

    //métier
    this.razLivraison = function() {
        for(var i = 0; i < this.intersections.length; ++i){
            this.intersections[i].razLivraison();
            var routes = this.intersections[i].routes;
            for(var j = 0; j < this.routes.length; ++j){
                this.routes[j].razPassages();
            }
        }
    };

    this.ajouterIntersection = function(pos, id) {
        return this.intersections[this.intersections.length]
            = new VueIntersection(pos,id);
    }

    this.ajouterRoute = function(i1,i2){
        var a = this.getIntersection(i1),
            b = this.getIntersection(i2);
        if ( a && b ) return this.routes[this.routes.length] 
            = new VueRoute(a,b);
        return null;
    }

    /*this.ajouterLivraison = function(idIntersection) {
        var i = getIntersection(idIntersection);
        if(i) i.setLivraison(true);
        return this;
    }*/

    this.getIntersection = function(id) {
        for( var i = 0; i < this.intersections.length; ++i ){
            if( this.intersections[i].id == id){
                return this.intersections[i];
            }
        }
        return null;
    }

    this.getRoute = function(id1,id2) {
        var it1 = this.getIntersection(id1);
        if(it1){
            return it1.getRouteIntersection(id2);
        }
        return null;
    };

    this._livraisonSupprimee = function(idLivraison, idIntersection){
        var it = this.getIntersection(idIntersection);
        it.closePopup();
        //console.log(idLivraison, it);
        this.ctrl.demandeDeSuppression(idLivraison);
    }



    //Constructeur
    // initialisation de la map
    this.map = L.map('map',{maxBounds:[[-0.1,-0.1],[0.9,0.9]],zoomControl:false}).setView([0.4, 0.4], 10);
    //this.map.attributionControl.setPosition('bottomleft');
    L.control.attribution({prefix: 'Projet DevOO - INSA de Lyon - H4104 - 2014', position: 'topleft'}).addTo(this.map);
    // Ajout des controls (boutons)
    //console.log("ctrl : ");
    //console.log(controleur);
    L.control.zoom({position:'topright', zoomInTitle: "Zoomer"}).addTo(this.map);
    L.easyButton('fa-arrow-circle-left', controleur.annuler, 'Undo', this.map);
    L.easyButton('fa-arrow-circle-right', controleur.retablir, 'Redo', this.map);
    L.easyButton('fa-road', controleur.clicChargerPlan, 'Charger un plan', this.map).setPosition('bottomleft');
    document.getElementById('charger-plan').addEventListener('change', controleur.chargerPlan, false);
    L.easyButton('fa-cubes', controleur.clicChargerLivraisons, 'Charger les livraison', this.map).setPosition('bottomleft');
    document.getElementById('charger-livraisons').addEventListener('change', controleur.chargerLivraisons, false);
    L.easyButton('fa-plus', null, 'Ajouter une livraison', this.map).setPosition('bottomleft');
    this.controlCalcul = L.easyButton('fa-refresh', controleur._clicCalcul, "Calculer l'itinéraire", this.map).setPosition('bottomleft');
    //this.controlCalcul.getContainer().getElementsByTagName('i')[0].className += " fa-spin";
    //console.log(this.controlCalcul.getContainer().className);
    var controlFDR = L.easyButton('fa-file-text', controleur.clicTelechargerInitineraire, "Télécharger la feuille de route", this.map).setPosition('bottomright');

    $('#modal-info').modal({backdrop: false, show: false});
    $('#modal-erreur').modal({backdrop: false, show: false});
}

function VueLegende(com, vue){
    this.vue = vue;
    this.com = com;
    this.lateral = document.getElementById('lateral');
    this.couleurPlages = ['#2974FF','#62FF29','#FF00FF','#00C8FF'];

    this.displayPlagesHoraires = function() {
        this.com.appelService('modele/plagesHoraires', '', this._plageOk, this._plageErr, true);
    };

    this._plageOk = function(reponse){
        var parser=new DOMParser();
        var doc=parser.parseFromString(reponse,"text/xml");
        var liste = doc.getElementsByTagName('plage');

        var listePlages = document.createElement('div');
        listePlages.id = "listePlages";
        lateral.appendChild(listePlages);
        var titre = document.createElement('h3');
        titre.innerHTML = 'Liste plages horaires';
        titre.style.color = 'white';
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

    this.raz = function() {
        while (this.lateral.firstChild) {
            this.lateral.removeChild(this.lateral.firstChild);
        }
    };
}

function VueIntersection(pos, id){

    // attributs
    this.pos = pos;
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

    this.cercle;

    this.livraison = null;
    this.entrepot = false;
    this.routesSortantes = [];

    // methodes
    this.setLivraison = function(id, idClient, plage, hdp) {
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
        this.majEtat();
        return this;
    }

    this.setEntrepot = function(bool) {
        this.entrepot = bool ? true : false;
        if(this.entrepot){
            this.setRayon(this.rayonEntrepot);
            this.majEtat();
        }
    };

    this.razLivraison = function() {
        this.livraison = null;
        this.entrepot = false;
        this.setRayon(this.rayonDefaut);
        this.cercle.unbindPopup();
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
    }

    this.ajouterRouteSortante = function(route) {
        //console.log("ajouterRouteSortante : route ", route);
        this.routesSortantes[this.routesSortantes.length] = route;
    }

    this.etatSelectionnable = function(){
        this.cercle.setStyle(this.paramSelec);
        this.etat = "selectionnable";
        //this._clic = this._clicSelectionnable;
        this.activerClic(this._clicSelectionnable);
        return this;
    }

    this.etatStandard = function(){
        this.cercle.setStyle(this.livraison ? this.paramLivraison :
                            (this.entrepot ? this.paramEntrepot : this.paramDefaut));
        this.etat = "standard";
        //this._clic = this._clicStandard;
        this.activerClic(this._clicStandard);
        return this;
    }

    this.etatDesactive = function(){
        this.cercle.setStyle(this.paramDesactive);
        this.etat = "desactive";
        this.desactiverClic();
        return this;
    }

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
        return this;
    }

    this.masquer = function(){
        map.removeLayer(this.cercle);
        return this;
    }

    this._clicSelectionnable = function() {
        console.log("clic selectionnable", this);
        //this.etatStandard();
    }
    this._clicStandard = function() {
        console.log("clic standard",this);
        if(this.livraison){
            this.cercle.openPopup();
        }
        //this.etatSelectionnable();
    }

    // Constructeur
    this.cercle = L.circle(pos, this.rayonDefaut, this.paramDefaut)/*
            .bindPopup("Intersection "+id+"<br>("+pos[0]+","+pos[1]+")", {offset: L.point(0,-10),closeButton:false})
            .on("mouseover",function () {this.openPopup();})
            .on("mouseout",function () {this.closePopup();})*/;
    return this;
}

function VueRoute(intersec1, intersec2){
    // attributs
    this.defaut = {
        ecartArc: 0.05,
        ecartSuivant : 0.06,
        nbLigne: 20,
        couleur: '#5f5f5f'
    }

    this.A = intersec1;
    this.B = intersec2;
    this.nom = "route sans nom";


    this.paramDefaut = {weight:5,color: this.defaut.couleur ,opacity:0.8};
    
    this.ligneBase;

    this.passages = [];

    // methodes
    // setters
    this.setNom = function(nom) {
        this.ligneBase.unbindLabel();
        this.nom = nom;
        this.ligneBase.bindLabel(nom);
        return this;
    };

    // affichage
    this.afficher = function(){
        this.ligneBase.addTo(map);
        for( var i = 0; i < this.passages.length; ++i){
            this.passages[i].addTo(map);
        }
        this.decorateurSens.addTo(map);
        return this;
    }

    this.masquer = function(){
        map.removeLayer(this.ligneBase);
        map.removeLayer(this.decorateurSens);
        for( var i=0; i < this.passages.length; ++i){
            map.removeLayer(this.passages[i]);
        }
        return this;
    }

    // events
    this._clic = function(){
        console.log('clic',this);
    }.bind(this);

    this._mouseover = function() {
        this.ligneBase.setStyle({color:"#0f0"});
    }.bind(this);

    this._mouseout = function() {
        this.ligneBase.setStyle({color: '#5f5f5f'});
    }.bind(this);

    // métier
    this.ajouterPassage = function(idLivraison, couleur) {
        var path = ArcMaker.arcPath(this.A.pos, this.B.pos,
                this.defaut.ecartArc + this.defaut.ecartSuivant*(1+this.passages.length),
                this.defaut.nbLigne);
        var opt = this.paramDefaut;
        opt.color = couleur;
        opt.opacity = 0.9;
        opt.idLivraison = idLivraison;
        //console.log(opt);
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

    // initialisation
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

function VueEtape(idIntersection, idLivraison, plage){
    this.intersection = idIntersection;
    this.livraison = idLivraison;
    this.plage = plage;
}

function VueItineraire(){
    // attributs
    this.etapes = [];
    // methodes
    this.ajouterEtape = function(idIntersection, idLivraison, plage){
        this.etapes[this.etapes.length]
            = new VueEtape(idIntersection, idLivraison, plage);
    }
}