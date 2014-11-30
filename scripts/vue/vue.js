///////////////////////////////////////////////////
// Class Vue

function Vue(controleur){
    // functions
    this.enableRedo = function(){
        console.log("Vue.enableRedo");
    };
    this.disableRedo = function(){
        console.log("Vue.disableRedo");
    };
    this.erreur = function(msg){
        alert(msg);
    };
    this.info = function(msg){
        alert(msg);
        /*var popup = L.popup()
        .setLatLng([0.4, 0.4])
        .setContent(msg)
        .openOn(map);*/
    };

    //Constructeur
    // initialisation de la map
    this.map = L.map('map',{maxBounds:[[-0.1,-0.1],[0.9,0.9]],zoomControl:false}).setView([0.4, 0.4], 10);
    // Ajout des controls (boutons)
    //console.log("ctrl : ");
    //console.log(controleur);
    L.control.zoom({position:'topright'}).addTo(this.map);
    L.easyButton('fa-arrow-circle-left', controleur.annuler, 'Undo', this.map);
    L.easyButton('fa-arrow-circle-right', controleur.retablir, 'Redo', this.map);
    L.easyButton('fa-road', controleur.clicChargerPlan, 'Charger un plan', this.map).setPosition('bottomleft');
    document.getElementById('charger-plan').addEventListener('change', controleur.chargerPlan, false);
    L.easyButton('fa-cubes', controleur.clicChargerLivraisons, 'Charger les livraison', this.map).setPosition('bottomleft');
    L.easyButton('fa-plus', null, 'Ajouter une livraison', this.map).setPosition('bottomleft');
    this.controlCalcul = L.easyButton('fa-refresh', null, "Calculer l'itinéraire", this.map).setPosition('bottomleft');
    this.controlCalcul.getContainer().getElementsByTagName('i')[0].className += " fa-spin";
    //console.log(this.controlCalcul.getContainer().className);
    var controlFDR = L.easyButton('fa-file-text', controleur.clicTelechargerInitineraire, "Télécharger la feuille de route", this.map).setPosition('bottomright');

}

function VueIntersection(pos, id){

    // attributs
    this.pos = pos;
    this.id = id;

    this.paramDefaut = {color: '#fff', fillColor: '#fff', fillOpacity: 0.5};
    this.rayonDefaut = 520;

    this.cercle;

    // methodes
    this.afficher = function(){
        this.cercle.addTo(map);
        return this;
    }

    this.masquer = function(){
        map.removeLayer(this.cercle);
        return this;
    }

    this._clic = function(e){
        //todo : Clic sur une intersection
        console.log(this);
    }

    // Constructeur
    this.cercle = L.circle(pos, this.rayonDefaut, this.paramDefaut)
            .bindPopup("Intersection "+id+"<br>("+pos[0]+","+pos[1]+")", {offset: L.point(0,-10),closeButton:false})
            .on("mouseover",function () {this.openPopup();})
            .on("mouseout",function () {this.closePopup();})
            .on("click",this._clic, this); // 3e parametre -> pour changer le this

    return this;
}

function VueRoute(intersec1, intersec2, nom){
    // attributs
    this.defaut = {
        ecartArc: 0.05,
        ecartSuivant : 0.02,
        nbLigne: 20,
        couleur: '#5f5f5f'
    }

    this.A = intersec1;
    this.B = intersec2;
    this.nom = nom;


    this.paramDefaut = {weight:5,color: this.defaut.couleur ,opacity:0.8};
    
    this.ligneBase;

    this.passages = [];

    // methodes
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
        this.A.masquer();
        this.B.masquer();
        return this;
    }

    this._clic = function(evt){
        this.masquer();
    }

    this.ajouterPassage = function(idLivraison, color) {
        var path = ArcMaker.arcPath(this.A.pos, this.B.pos,
                this.defaut.ecartArc + this.defaut.ecartSuivant*(1+this.passages.length),
                this.defaut.nbLigne);
        var opt = this.paramDefaut;
        opt.color = color;
        opt.opacity = 0.7;
        opt.idLivraison = idLivraison;
        console.log(opt);
        this.passages[this.passages.length] = L.polyline(path, opt);
    }

    this.path = ArcMaker.arcPath(this.A.pos, this.B.pos,
                this.defaut.ecartArc,
                this.defaut.nbLigne);

    this.ligneBase = L.polyline(this.path,this.paramDefaut)
        .bindLabel(this.nom)
        .on("mouseover", function (){
            this.setStyle({color:"#0f0"});
        })
        .on("mouseout", function (){
            this.setStyle({color: '#5f5f5f'});
        })
        .on('click', this._clic, this);

    this.decorateurSens = L.polylineDecorator(this.ligneBase, {
        patterns: [
            // define a pattern of 10px-wide dashes, repeated every 20px on the line 
            {offset: "20px", repeat: '50px', symbol: new L.Symbol.ArrowHead ({pixelSize: 10, headAngle:40, pathOptions: {opacity:0.5,fillOpacity:0.2,weight:1,color: "black",fillColor:"yellow",fillOpacity:0.8}})}
        ]
    });
    return this;
}

