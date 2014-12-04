///////////////////////////////////////////////////
// Class Vue
function VueFeuilleDeRoute(appCom, livraison, vue){

    // attributs
    this.com = appCom;
    var com = this.com;

    this.vue = vue;
    var vue = this.vue;

    this.livraison = livraison;
    var livraison = this.livraison;

    //methodes
    this.chargerFeuille = function(doc) {
        console.log("chargerFeuille",doc);

        console.log("vue",vue);


        $('#FDR').html("<h1>Feuille de Route</h1>");

        var fdr = doc.getElementsByTagName("feuilleDeRoute")[0];
        var etapes = fdr.getElementsByTagName("etape");
        var id1 = etapes[0].getAttribute("idIntersection");

        for(var i = 1; i < etapes.length; ++i){
            var id2 = etapes[i].getAttribute("idIntersection");
            $('#FDR').append("<p>");
            var route = this.vue.getRoute(id1, id2);
            if(route){
                var heure = etapes[i-1].getAttribute("heurePassage");
                //console.log("route", route);
                //console.log("plage", idPlage,this.couleurPlages[idPlage]);
                var livraison = etapes[i].getElementsByTagName("livraison");

                $('#FDR').append(heure+" : ");
                $('#FDR').append(route.nom+ " en direction de ");
                $('#FDR').append(this.vue.getIntersection(id2).id );

                if(livraison.length != 0 ){
                    $('#FDR').append("</p>");

                    for(var a = 0; a < livraison.length ; a++){
                        $('#FDR').append("<p class='livraison'>Livraison pour le client"+ " " + "</p>");

                    }

                } else {
                
                    $('#FDR').append("</p>");

                }
            }
            id1 = id2;
        }

        //var intersection = vue.getIntersection(id);
        //var route = vue.getRoute(id1,id2);
    };

    function checkLivraison(id){

        console.log("livraison", livraison);

        var plages = livraison.getElementsByTagName("plage");
        for(var i = 0; i < plages.length; ++i){
            var plageTxt = plages[i].getAttribute("debut")
                    + "-" + plages[i].getAttribute("fin") + "h";
            var livs = plages[i].getElementsByTagName("livraison");
            for(var j = 0; j < livs.length; ++j){
                var idIntersection = livs[j].getAttribute("idIntersection");
                console.log("livraison", id, idIntersection);

                if(id == idIntersection){
                    return true;
                } else {
                    return false;
                

/*
                var it = this.vue.getIntersection(idIntersection);
                if(it){


                    it.setLivraison(livs[j].getAttribute("id"),
                                    livs[j].getAttribute("idClient"),
                                    plageTxt,
                                    null);*/
                }
            }
        }
    };
}