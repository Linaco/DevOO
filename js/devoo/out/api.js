YUI.add("yuidoc-meta", function(Y) {
   Y.YUIDoc = { meta: {
    "classes": [
        "Com",
        "Controleur",
        "Vue",
        "VueIntersection",
        "VueLegende",
        "VueRoute"
    ],
    "modules": [
        "appcom",
        "controleur",
        "vue"
    ],
    "allModules": [
        {
            "displayName": "appcom",
            "name": "appcom",
            "description": "Le package appcom permet la communication entre les différents morceaux de l'application \n(java/js)."
        },
        {
            "displayName": "controleur",
            "name": "controleur",
            "description": "Controleur de l'application. Reçois les évènements utilisateurs de la vue, puis invoque\nles services adaptés de l'application java pour modifier les données."
        },
        {
            "displayName": "vue",
            "name": "vue",
            "description": "Vue de l'application, gère toutes les informations graphiques renvoyées à l'utilsateur."
        }
    ]
} };
});