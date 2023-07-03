    function showTemperature() {
      var input = document.getElementById("Temperatur");
      var value = input.value;
      var display = document.getElementById("TemperaturAnzeige");
      display.textContent = value + '°C';
    }
    function showLuftfeuchtigkeit() {
      var input = document.getElementById("Luftfeuchtigkeit");
      var value = input.value;
      var display = document.getElementById("LuftfeuchtigkeitAnzeige");
      display.textContent = value + '%';
    }
      window.addEventListener('DOMContentLoaded', function() {
      // Standardmäßig 0 Grad anzeigen
      showTemperature();
      showLuftfeuchtigkeit();
    });