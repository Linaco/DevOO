///////////////////////////////////////////////////
// Class Vue
function Vue(controleur, com){
    // attributs
    this.com = com;
    var com = this.com;

    this.intersections = [];
    this.routes = [];
    this.itineraire;

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
    this.nouveauPlan = function(){
        this.masquer();
        this.intersections = [];
        this.routes = [];
        com.appelService('modele/plan','',this.retourModelePlan, true);
    }
    this.retourModelePlan = function(str){
        this.info(str);
        var parser=new DOMParser();
        var doc=parser.parseFromString(str,"text/xml");

        var plan = doc.getElementsByTagName("plan")[0];
        //console.log("plan",plan);
        var its = doc.getElementsByTagName("intersection");
        for( var i = 0; i < its.length; ++i){
            var it = its[i];
            var id = it.getAttribute("id");
            var x = it.getAttribute("x")/500;
            var y = it.getAttribute("y")/500;
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

    this.ajouterLivraison = function(idIntersection) {
        var i = getIntersection(idIntersection);
        if(i) i.setLivraison(true);
        return this;
    }

    this.getIntersection = function(id) {
        for( var i = 0; i < this.intersections.length; ++i ){
            if( this.intersections[i].id == id){
                return this.intersections[i];
            }
        }
        return null;
    }

    this._livraisonSupprimee = function(idLivraison, idIntersection){
        var it = this.getIntersection(idIntersection);
        it.closePopup();
        console.log(idLivraison, it);
        this.info("Suppression prise en compte");
    }



    //Constructeur
    // initialisation de la map
    this.map = L.map('map',{maxBounds:[[-0.1,-0.1],[0.9,0.9]],zoomControl:false}).setView([0.4, 0.4], 10);
    // Ajout des controls (boutons)
    //console.log("ctrl : ");
    //console.log(controleur);
    L.control.zoom({position:'topright', zoomInTitle: "Zoomer"}).addTo(this.map);
    L.easyButton('Heeey fa-arrow-circle-left', controleur.annuler, 'Undo', this.map);
    L.easyButton('fa-arrow-circle-right', controleur.retablir, 'Redo', this.map);
    L.easyButton('fa-road', controleur.clicChargerPlan, 'Charger un plan', this.map).setPosition('bottomleft');
    document.getElementById('charger-plan').addEventListener('change', controleur.chargerPlan, false);
    L.easyButton('fa-cubes', controleur.clicChargerLivraisons, 'Charger les livraison', this.map).setPosition('bottomleft');
    L.easyButton('fa-plus', null, 'Ajouter une livraison', this.map).setPosition('bottomleft');
    this.controlCalcul = L.easyButton('fa-refresh', controleur._clicCalcul, "Calculer l'itinéraire", this.map).setPosition('bottomleft');
    //this.controlCalcul.getContainer().getElementsByTagName('i')[0].className += " fa-spin";
    //console.log(this.controlCalcul.getContainer().className);
    var controlFDR = L.easyButton('fa-file-text', controleur.clicTelechargerInitineraire, "Télécharger la feuille de route", this.map).setPosition('bottomright');

    $('#modal-info').modal({backdrop: false, show: false});
    $('#modal-erreur').modal({backdrop: false, show: false});
}

function VueIntersection(pos, id){

    // attributs
    this.pos = pos;
    this.id = id;

    this.paramDefaut = {color: '#fff', opacity: 0.5, fillColor: '#fff', fillOpacity: 0.5};
    this.paramLivraison = {color: '#f0f', opacity: 0.5, fillColor: '#ff0', fillOpacity: 0.5};
    this.paramSelec = {color: 'red', opacity: 0.8, fillColor: 'yellow', fillOpacity: 0.8};
    this.paramDesactive = {color: '#a0a0a0', fillColor: 'a0a0a0'};
    this.rayonDefaut = 520;

    this.etat = "standard";

    this.cercle;

    this.livraison = null;
    this.routesSortantes = [];

    // methodes
    this.setLivraison = function(id, idClient, hdp) {
        this.livraison = {
            id: id,
            idClient: idClient,
            hdp: hdp
        };
        var div = document.getElementById('popup-livraison').cloneNode(true);
        console.log("div",div);
        div.getElementsByTagName("client")[0].textContent = this.livraison.idClient;
        div.getElementsByTagName("hdp")[0].textContent = this.livraison.hdp;
        div.getElementsByTagName("button")[0].setAttribute("onclick","ctrl.vue._livraisonSupprimee("+this.livraison.id+","+this.id+");");
        this.cercle.bindPopup(div);
        this.majEtat();
        return this;
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
        this.cercle.setStyle(this.livraison ? this.paramLivraison : this.paramDefaut);
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
        ecartSuivant : 0.02,
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
        opt.opacity = 0.7;
        opt.idLivraison = idLivraison;
        //console.log(opt);
        this.passages[this.passages.length] = L.polyline(path, opt);
        return this;
    }

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
            {offset: "20px", repeat: '50px', symbol: new L.Symbol.ArrowHead ({pixelSize: 10, headAngle:40, pathOptions: {opacity:0.5,fillOpacity:0.2,weight:1,color: "black",fillColor:"yellow",fillOpacity:0.8}})}
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