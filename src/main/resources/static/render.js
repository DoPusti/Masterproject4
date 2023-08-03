document.addEventListener("DOMContentLoaded", function() {
    var templates = document.getElementsByClassName("dynamic-content");
    var data = {
        title: "Eingabeformular zur Auswahl kombinierter Ressourcen",
        headerLeft: "Technische Rahmenbedingungen am Aufstellort",
        left0: "Durschnittliche Raumtemperatur:",
        left1: "Relative Luftfeuchtigkeit:",
        left2: "Max. Verfügbare Pneumatikversorgung:",
        left3: "Max. Verfügbare Stromversorgung:",
        elect0:"Wechselstrom (230 V, 16 A)",
        elect1:"Drehstrom (400 V, 16 A)",
        elect2:"Drehstrom (400 V, 32 A)",
        elect3:"Drehstrom (400 V, 63 A",
        headerRight:"Wirtschaftliche Rahmenbedingungen am Aufstellort",
        right0: "Flächenkosten (pro m²):",
        right1: "Geplante Einsatzdauer in Monaten:",
        right2: "Vorhandene Ressourcen (XML)",
        start: "Anfrage starten",
        requirments: "Anforderungsdatei (XML)",
        assurances: "Zusicherungen (XML)"


    };

    for (var i = 0; i < templates.length; i++) {
        var template = templates[i].innerHTML;
        var renderedHTML = Mustache.render(template, data);
        templates[i].innerHTML = renderedHTML;
    }
});