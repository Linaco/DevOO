var ctrl = new Controleur();
var map = ctrl.vue.map;
//////////////////////////////////////////////////////////////////////////////////
if (window.File && window.FileReader && window.FileList && window.Blob) {
    // Great success! All the File APIs are supported.
} else {
    alert('Les APIs pour l\'ouverture des fichiers ne sont pas pris en charge');
}


///////////////////////////////////////////////////
// Class Controleur

function Controleur(){

    // attributes
    var com = new Com();
    var vue;

    // functions
    this.annuler = function() {
        console.log("undo");
        vue.info("Annulation...");
    };
    this.retablir = function(){
        console.log("redo");
        vue.erreur("Erreur lors du rétablissement de l'action...");
    };
    this.clicChargerPlan = function(){
        document.getElementById('charger-plan').click();
        //... récupérer fichier
        //c.appelService("test", [50,20], function(reponse){alert(reponse);});
    };
    this._chargerPlanOk = function(msg){
        vue.nouveauPlan();
    }.bind(this);
    this.chargerPlan = function(evt){
        vue.afficherChargement("Création du réseau routier...\n"
                + "Merci de patienter quelques instants.");
        com.envoyerXml(evt,'controleur/charger-plan',this._chargerPlanOk, true);
    }.bind(this);

    // déclenche le clic sur l'élément 'input' de la page html
    this.clicChargerLivraisons = function(){
        document.getElementById('charger-livraisons').click();
    };
    // Handler pour le retour du service charger-livraisons
    this._chargerLivraisonsOk = function(msg){
        vue.info(msg);
        // vue.creerPlan();
    };
    this.chargerLivraisons = function(evt){
        com.envoyerXml(evt,'controleur/charger-livraisons',this._chargerLivraisonsOk);
    }.bind(this);

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
        //... récupérer fichier
    };

    this._clicCalcul = function () {
        vue.afficherChargement("Calcul en cours, veuillez patienter...");
    }

    

    // init
    this.vue = vue = new Vue(this, com);

}



var path2 = [[0.4,0.4],[0.6,0.5]];
//var fg = L.rainbowLine(path,["#fff","#7a6bd9","#fe6a6d","#67e860","#ffe06a","#de252a"]).bindLabel("HEY").on("click",function(){alert("hey");}).addTo(map);
//var fg2 = L.rainbowLine([[0.6,0.5],[0.7,0.4]],["#fff","#7a6bd9","#fe6a6d","#67e860","#ffe06a","#de252a"]).bindLabel("HEY").on("click",function(){alert("hey");}).addTo(map);
var colors = ["#fff","#7a6bd9","#fe6a6d","#67e860","#ffe06a","#de252a"];


var inter = [];
ctrl.vue.ajouterIntersection([0.2,0.2],1).activerClic();
ctrl.vue.ajouterIntersection([0.4,0.2],2).activerClic();
ctrl.vue.ajouterIntersection([0.5,0.3],3).activerClic();
ctrl.vue.ajouterIntersection([0.4,0.4],4).activerClic();
ctrl.vue.ajouterIntersection([0.2,0.5],5).activerClic();
ctrl.vue.ajouterIntersection([0.3,0.5],6).activerClic();
ctrl.vue.ajouterIntersection([0.3,0.6],7).activerClic();
ctrl.vue.ajouterIntersection([0.5,0.6],8).activerClic();
ctrl.vue.ajouterIntersection([0.6,0.5],9).activerClic();
ctrl.vue.getIntersection(4).setLivraison(0,45,'8-12h');
ctrl.vue.getIntersection(5).setLivraison(1,65,'8-12h');
ctrl.vue.getIntersection(6).setLivraison(2,12,'8-12h');

ctrl.vue.ajouterRoute(1,2).setNom("Rue de la paix")
    .ajouterPassage(0,colors[0]);
ctrl.vue.ajouterRoute(2,1).setNom("route");
ctrl.vue.ajouterRoute(2,3).setNom("route")
    .ajouterPassage(0,colors[0]);
ctrl.vue.ajouterRoute(3,9).setNom("route")
    .ajouterPassage(0,colors[0])
    .ajouterPassage(0,colors[1]);
ctrl.vue.ajouterRoute(9,4).setNom("route")
    .ajouterPassage(0,colors[0])
    .ajouterPassage(0,colors[1]);
ctrl.vue.ajouterRoute(4,3).setNom("route")
    .ajouterPassage(0,colors[1]);
ctrl.vue.ajouterRoute(3,8).setNom("route");
ctrl.vue.ajouterRoute(8,7).setNom("route")
    .ajouterPassage(0,colors[3]);
ctrl.vue.ajouterRoute(7,6).setNom("route")
    .ajouterPassage(0,colors[3]);
ctrl.vue.ajouterRoute(6,7).setNom("route")
    .ajouterPassage(0,colors[1]);
ctrl.vue.ajouterRoute(6,8).setNom("route")
    .ajouterPassage(0,colors[2]);
ctrl.vue.ajouterRoute(6,4).setNom("route");
ctrl.vue.ajouterRoute(4,6).setNom("route")
    .ajouterPassage(0,colors[1])
    .ajouterPassage(0,colors[3]);
ctrl.vue.ajouterRoute(4,2).setNom("route");
ctrl.vue.ajouterRoute(4,1).setNom("route")
    .ajouterPassage(0,colors[3]);
ctrl.vue.ajouterRoute(7,5).setNom("route")
    .ajouterPassage(0,colors[2]);
ctrl.vue.ajouterRoute(5,6).setNom("route")
    .ajouterPassage(0,colors[2]);


ctrl.vue.afficher();

/*for( var i = 0; i < routes.length; ++i){
    routes[i].afficher();
}
for( var i = 0; i < inter.length; ++i){
    inter[i].afficher();
}*/

//var path = ArcMaker.arcPath([0.5,0.5],[0.2,0.2],0.05+0.02,10);
/*for(var i = 0; i < 5; ++i){
    var path = ArcMaker.arcPath([0.2,0.2],[0.5,0.5],0.05+0.02*i,10);
    var pl = L.polyline(path,{weight: 3,color: colors[i]}).addTo(map);
}*/


///////////////////////////////////////////////////
// Class Com

function Com(){

    this.appelService = function(nomService, params, fonctionRetour, async){
        console.log("async",async);
        var asynchronous = async == null ? false : async;
        console.log(nomService, (asynchronous? "async" : "sync"));
        var xmlhttp=new XMLHttpRequest();
        xmlhttp.open("POST","http://localhost:4500/"+nomService,asynchronous);
        //xmlhttp.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
        xmlhttp.overrideMimeType('text/xml');

        var msg = "";
        if(params){
            for( var i = 0; i < params.length; ++i){
                msg += params[i];
            }
        }
        if( !asynchronous ){
            xmlhttp.send(msg); // bloquant
            if(fonctionRetour){
                fonctionRetour(xmlhttp.responseText);
            }
            return xmlhttp.responseText;
        } else {
            var ctl = this;
            xmlhttp.onload = function (e) {
            if (xmlhttp.readyState === 4) {
                if (xmlhttp.status === 200) {
                    fonctionRetour(xmlhttp.responseText);
                } else {
                    ctl.vue.erreur(xmlhttp.statusText);
                }
            }
            };
            xmlhttp.onerror = function (e) {
                ctl.vue.erreur(xmlhttp.statusText);
            };
            xmlhttp.send(msg);
        }
        
    }

    this.envoyerXml = function(fileEvt, nomService, fonctionRetour, async){
        var f = fileEvt.target.files[0];
        if(f){
            var extension = f.name.split('.').pop();
            if(extension === "xml" || extension === "XML"){
                var reader = new FileReader();
                
                reader.onload = function(e){
                    this.appelService(nomService,[e.target.result],fonctionRetour, async);
                }.bind(this);
                reader.readAsText(f);
            } else {
                vue.erreur("Le fichier sélectionné (."+extension+") n'a pas la bonne extension (.xml) !");
            }
        }
    }.bind(this);
}

/*
function addLine( A, B, label){
    L.polyline([A,B],{weight:5,color:'#fff',opacity:0.8})
        .addTo(map)
        .bindLabel(label)
        .on("mouseover", function (){
            this.setStyle({color:"#0f0"});
        })
        .on("mouseout", function (){
            this.setStyle({color:'#fff'});
        });
}
*/