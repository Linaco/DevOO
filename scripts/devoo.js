var ctrl = new Controleur();

/////////////////////////////////////////////////////////////////////////
/*
if (window.XMLHttpRequest)
{// code for IE7+, Firefox, Chrome, Opera, Safari
    xmlhttp=new XMLHttpRequest();
}
else
{// code for IE6, IE5
    xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
}
xmlhttp.open("GET","xml/plan10x10.xml",false);
xmlhttp.send();
xmlDoc=xmlhttp.responseXML;

console.log(xmlDoc);
*/
var map = ctrl.vue.map;
/*
var nodes = xmlDoc.getElementsByTagName("Noeud");
for( var i = 0; i < nodes.length; ++i){
    var node = nodes[i];
    var pos = [node.getAttribute('x')/1000, node.getAttribute('y')/1000];

    var trs = node.getElementsByTagName("LeTronconSortant");
    for( var j = 0; j < trs.length; ++j){
        var tr = trs[j];
        var node2 = xmlDoc.getElementById(tr.getAttribute("idNoeudDestination"));
        var pos2 = [node2.getAttribute('x')/1000, node2.getAttribute('y')/1000];

        addLine(pos, pos2, tr.getAttribute("nomRue"));
    }
}

for( var i = 0; i < nodes.length; ++i){
    var node = nodes[i];
    var pos = [node.getAttribute('x')/1000, node.getAttribute('y')/1000];
    addCircle(pos,node.getAttribute('id'));
}

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

function addCircle( pos, id){
    var circle = L.circle(pos, 520, {
        color: 'red',
        fillColor: '#f03',
        fillOpacity: 0.5
    }).addTo(map)
        .bindPopup("Node "+id+"<br>("+pos[0]+","+pos[1]+")", {offset: L.point(0,-10),closeButton:false})
        .on("mouseover",function () {this.openPopup();})
        .on("mouseout",function () {this.closePopup();})
        .on("click",function () {alert("Vous avez sélectionné un noeud. Malheureusement cette fonctionnalité n'est pas encore implémentée...");});
};
*/
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
    };
    this.retablir = function(){
        console.log("redo");
    };
    this.clicChargerPlan = function(){
        document.getElementById('charger-plan').click();
        //... récupérer fichier
        //c.appelService("test", [50,20], function(reponse){alert(reponse);});
    };
    this._chargerPlanOk = function(msg){
        vue.info(msg);
        // vue.creerPlan();
    }.bind(this);
    this.chargerPlan = function(evt){
        com.envoyerXml(evt,'charger-plan',this._chargerPlanOk);
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
        com.envoyerXml(evt,'charger-livraisons',this._chargerLivraisonsOk);
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

    // init
    this.vue = vue = new Vue(this);

}



var path2 = [[0.4,0.4],[0.6,0.5]];
//var fg = L.rainbowLine(path,["#fff","#7a6bd9","#fe6a6d","#67e860","#ffe06a","#de252a"]).bindLabel("HEY").on("click",function(){alert("hey");}).addTo(map);
//var fg2 = L.rainbowLine([[0.6,0.5],[0.7,0.4]],["#fff","#7a6bd9","#fe6a6d","#67e860","#ffe06a","#de252a"]).bindLabel("HEY").on("click",function(){alert("hey");}).addTo(map);
var colors = ["#fff","#7a6bd9","#fe6a6d","#67e860","#ffe06a","#de252a"];


var inter = [];
inter[0] = new VueIntersection([0.2,0.2],1);
inter[1] = new VueIntersection([0.4,0.2],2);
inter[2] = new VueIntersection([0.5,0.3],3);
inter[3] = new VueIntersection([0.4,0.4],4);
inter[4] = new VueIntersection([0.2,0.5],5);
inter[5] = new VueIntersection([0.3,0.5],6);
inter[6] = new VueIntersection([0.3,0.6],7);
inter[7] = new VueIntersection([0.5,0.6],8);
inter[8] = new VueIntersection([0.6,0.5],9);
var routes = [];
routes[0] = new VueRoute(inter[0],inter[1],"Rue de la paix");
routes[0].ajouterPassage(0,colors[0]);
routes[1] = new VueRoute(inter[1],inter[0], "route");
routes[2] = new VueRoute(inter[1],inter[2], "route");
routes[2].ajouterPassage(0,colors[0]);
routes[3] = new VueRoute(inter[2],inter[8], "route");
routes[3].ajouterPassage(0,colors[0]);
routes[3].ajouterPassage(0,colors[1]);
routes[4] = new VueRoute(inter[8],inter[3], "route");
routes[4].ajouterPassage(0,colors[0]);
routes[4].ajouterPassage(0,colors[1]);
routes[5] = new VueRoute(inter[3],inter[2], "route");
routes[5].ajouterPassage(0,colors[1]);
routes[6] = new VueRoute(inter[2],inter[7], "route");
routes[7] = new VueRoute(inter[7],inter[6], "route");
routes[7].ajouterPassage(0,colors[3]);
routes[8] = new VueRoute(inter[6],inter[5], "route");
routes[8].ajouterPassage(0,colors[3]);
routes[9] = new VueRoute(inter[5],inter[6], "route");
routes[9].ajouterPassage(0,colors[1]);
routes[10] = new VueRoute(inter[5],inter[7], "route");
routes[10].ajouterPassage(0,colors[2]);
routes[11] = new VueRoute(inter[5],inter[3], "route");
routes[12] = new VueRoute(inter[3],inter[5], "route");
routes[12].ajouterPassage(0,colors[1]);
routes[12].ajouterPassage(0,colors[3]);
routes[13] = new VueRoute(inter[3],inter[1], "route");
routes[14] = new VueRoute(inter[3],inter[0], "route");
routes[14].ajouterPassage(0,colors[3]);
routes[15] = new VueRoute(inter[6],inter[4], "route");
routes[15].ajouterPassage(0,colors[2]);
routes[16] = new VueRoute(inter[4],inter[5], "route");
routes[16].ajouterPassage(0,colors[2]);
for( var i = 0; i < routes.length; ++i){
    routes[i].afficher();
}
for( var i = 0; i < inter.length; ++i){
    inter[i].afficher();
}

//var path = ArcMaker.arcPath([0.5,0.5],[0.2,0.2],0.05+0.02,10);
/*for(var i = 0; i < 5; ++i){
    var path = ArcMaker.arcPath([0.2,0.2],[0.5,0.5],0.05+0.02*i,10);
    var pl = L.polyline(path,{weight: 3,color: colors[i]}).addTo(map);
}*/


///////////////////////////////////////////////////
// Class Com

function Com(){

    this.appelService = function(nomService, params, fonctionRetour){
        var xmlhttp=new XMLHttpRequest();
        xmlhttp.open("POST","http://localhost:4500/"+nomService,false);
        //xmlhttp.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
        xmlhttp.overrideMimeType('text/xml');

        var msg = "";
        for( var i = 0; i < params.length; ++i){
            msg += params[i];
        }
        xmlhttp.send(msg); // bloquant
        fonctionRetour(xmlhttp.responseText);
    }

    this.envoyerXml = function(fileEvt, nomService, fonctionRetour){
        var f = fileEvt.target.files[0];
        if(f){
            var extension = f.name.split('.').pop();
            if(extension === "xml" || extension === "XML"){
                var reader = new FileReader();
                
                reader.onload = function(e){
                    this.appelService(nomService,[e.target.result],fonctionRetour);
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