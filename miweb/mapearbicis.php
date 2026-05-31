<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mapa de Estaciones Valenbisi</title>
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
    <style>
    * { box-sizing: border-box; margin: 0; padding: 0; }
    body {
        font-family: 'Segoe UI', Arial, sans-serif;
        min-height: 100vh;
        padding-bottom: 40px;
    }
    header { text-align: center; padding: 28px 20px 10px; }
    header h1 {
        font-size: 2rem; color: #2e7d32;
        letter-spacing: 1px; text-shadow: 1px 1px 2px rgba(0,0,0,.1);
    }
    .legend {
        display: inline-flex; gap: 20px; flex-wrap: wrap; justify-content: center;
        margin: 14px auto 10px; background: #fff; padding: 10px 24px;
        border-radius: 30px; box-shadow: 0 2px 10px rgba(0,0,0,.1);
        font-size: .85rem; color: #444;
    }
    .legend span { display: flex; align-items: center; gap: 6px; }
    .dot {
        width: 14px; height: 14px; border-radius: 50%;
        display: inline-block; border: 1px solid rgba(0,0,0,.2);
    }
    #map {
        height: 580px; width: 92%; max-width: 1200px;
        margin: 10px auto 0; border-radius: 12px;
        box-shadow: 0 4px 18px rgba(0,0,0,.15); border: 3px solid #fff;
    }
    .btn-volver-wrapper { text-align: center; margin-top: 24px; }
    .btn-volver {
        display: inline-block; background-color: #2e7d32; color: #fff;
        text-decoration: none; padding: 12px 36px; border-radius: 8px;
        font-size: 1rem; font-weight: bold;
        box-shadow: 0 4px 12px rgba(46,125,50,.35);
        transition: background-color .2s, transform .15s;
    }
    .btn-volver:hover { background-color: #1b5e20; transform: translateY(-2px); }
    </style>
</head>
<body>
    <header>
        <h1>Mapa de Estaciones ValenBisi</h1>
        <div class="legend">
            <span><span class="dot" style="background:#2ecc71;"></span> &ge;20 bicis</span>
            <span><span class="dot" style="background:#f1c40f;"></span> 10&ndash;19 bicis</span>
            <span><span class="dot" style="background:#e67e22;"></span> 5&ndash;9 bicis</span>
            <span><span class="dot" style="background:#e74c3c;"></span> &lt;5 bicis</span>
        </div>
    </header>

    <div id="map"></div>

    <div class="btn-volver-wrapper">
    <a class="btn-volver" href="index.php">&#8592; Volver al listado</a>
    </div>


    <script>
    // Inicializa el mapa centrado en Valencia
    var map = L.map('map').setView([39.47, -0.37], 13);

    // Añadir capa base de OpenStreetMap
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map);

    // Color del marcador según bicis disponibles
    function getMarkerColor(available) {
        if (available < 5)        return '#e74c3c'; // rojo
        if (available < 10)       return '#e67e22'; // naranja
        if (available < 20)       return '#f1c40f'; // amarillo
        return '#2ecc71';                            // verde
    }

    // Cargar el archivo data.json
    fetch('data.json')
        .then(response => {
            if (!response.ok) throw new Error('Error al cargar data.json: ' + response.statusText);
            return response.json();
        })
        .then(data => {
            Object.values(data).forEach(station => {
                // Usamos latitude/longitude (ya convertidas a WGS84)
                const { latitude, longitude, address, available, free, total, open } = station;
                if (latitude && longitude) {
                    L.circleMarker([latitude, longitude], {
                        color: getMarkerColor(available),
                        fillColor: getMarkerColor(available),
                        radius: 9,
                        fillOpacity: 0.85,
                        weight: 2
                    })
                    .addTo(map)
                    .bindPopup(
                        '<strong>' + address + '</strong><br>' +
                        '<b>Estado:</b> ' + (open ? 'Abierta' : 'Cerrada') + '<br>' +
                        '<b>Disponibles:</b> ' + available + '<br>' +
                        '<b>Libres:</b> ' + free + '<br>' +
                        '<b>Total:</b> ' + total
                    );
                }
            });
        })
        .catch(error => {
            console.error('Error cargando los datos:', error);
            document.getElementById('map').innerHTML =
                '<p style="color:red;padding:20px;">Error al cargar los datos del mapa.</p>';
        });
    </script>
</body>
</html>
