// ==========================================
// Hauptlogik: Karte, Geokodierung, Autocomplete & Fahrtkosten
// ==========================================

let map = null;
let tileLayer = null;
let routingControl = null;
let currentDistanceKm = 0;

const LIGHT_TILE_URL = 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
const DARK_TILE_URL = 'https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png';

function initMap() {
    map = L.map('map').setView([48.3665, 10.8944], 10);

    const isDark = document.documentElement.classList.contains('dark');
    const tileUrl = isDark ? DARK_TILE_URL : LIGHT_TILE_URL;

    tileLayer = L.tileLayer(tileUrl, {
        attribution: '© OpenStreetMap contributors, © CARTO'
    }).addTo(map);
}

function updateMapTheme(isDark) {
    if (tileLayer && map) {
        map.removeLayer(tileLayer);
        const newUrl = isDark ? DARK_TILE_URL : LIGHT_TILE_URL;
        tileLayer = L.tileLayer(newUrl, {
            attribution: '© OpenStreetMap contributors, © CARTO'
        }).addTo(map);
    }
}

/**
 * Löscht den Inhalt eines Eingabefeldes.
 * @param {string} inputId - ID des Eingabefeldes
 */
function clearInput(inputId) {
    const input = document.getElementById(inputId);
    if (input) {
        input.value = '';
        input.focus();
    }
}

function swapAddresses() {
    const startInput = document.getElementById('startInput');
    const destInput = document.getElementById('destInput');

    if (startInput && destInput) {
        const temp = startInput.value;
        startInput.value = destInput.value;
        destInput.value = temp;
        calculateRoute();
    }
}

function setFuel(type) {
    const fuelBtns = document.querySelectorAll('.fuel-btn');
    const fuelHeader = document.getElementById('fuelHeader');

    fuelBtns.forEach(btn => {
        btn.className = "fuel-btn bg-slate-100 dark:bg-slate-800 border border-slate-300 dark:border-slate-700 text-slate-700 dark:text-slate-300 hover:bg-slate-200 dark:hover:bg-slate-700 rounded-xl py-2 px-3 text-xs font-medium transition-all duration-200 flex items-center justify-center gap-1.5";
    });

    const activeBtn = document.getElementById('btn' + type);
    const consLabel = document.getElementById('consumptionLabel');
    const priceLabel = document.getElementById('priceLabel');

    if (type === 'Elektro') {
        if (activeBtn) activeBtn.className = "fuel-btn bg-emerald-600 text-white py-2 rounded-xl text-xs font-bold transition shadow-md";
        if (fuelHeader) fuelHeader.className = "text-lg font-bold text-emerald-600 dark:text-emerald-400 flex items-center gap-2 transition-colors";

        if (consLabel) consLabel.innerText = "Verbrauch (kWh/100km)";
        if (priceLabel) priceLabel.innerText = "Preis pro kWh (€)";
        if (document.getElementById('priceInput')) document.getElementById('priceInput').value = "0.40";
        if (document.getElementById('consumptionInput')) document.getElementById('consumptionInput').value = "18.0";

    } else if (type === 'Diesel') {
        if (activeBtn) activeBtn.className = "fuel-btn bg-amber-600 text-white py-2 rounded-xl text-xs font-bold transition shadow-md";
        if (fuelHeader) fuelHeader.className = "text-lg font-bold text-amber-600 dark:text-amber-400 flex items-center gap-2 transition-colors";

        if (consLabel) consLabel.innerText = "Verbrauch (L/100km)";
        if (priceLabel) priceLabel.innerText = "Preis pro Lit. (€)";
        if (document.getElementById('priceInput')) document.getElementById('priceInput').value = "1.65";
        if (document.getElementById('consumptionInput')) document.getElementById('consumptionInput').value = "5.5";

    } else { // Benzin
        if (activeBtn) activeBtn.className = "fuel-btn bg-blue-600 text-white py-2 rounded-xl text-xs font-bold transition shadow-md";
        if (fuelHeader) fuelHeader.className = "text-lg font-bold text-blue-600 dark:text-blue-400 flex items-center gap-2 transition-colors";

        if (consLabel) consLabel.innerText = "Verbrauch (L/100km)";
        if (priceLabel) priceLabel.innerText = "Preis pro Lit. (€)";
        if (document.getElementById('priceInput')) document.getElementById('priceInput').value = "1.75";
        if (document.getElementById('consumptionInput')) document.getElementById('consumptionInput').value = "6.5";
    }

    updateCalculations();
}

async function geocode(address) {
    try {
        const url = `https://nominatim.openstreetmap.org/search?format=json&limit=1&accept-language=de&q=${encodeURIComponent(address)}`;
        const response = await fetch(url, { headers: { 'Accept-Language': 'de' } });

        if (!response.ok) throw new Error("Fehler beim Abrufen der Daten.");
        const data = await response.json();

        if (data && data.length > 0) {
            return [parseFloat(data[0].lat), parseFloat(data[0].lon)];
        } else {
            throw new Error(`Adresse nicht gefunden: "${address}"`);
        }
    } catch (error) {
        console.error("Geocoding-Fehler:", error);
        throw error;
    }
}

function setupAutocomplete(inputId, suggestionsId) {
    const input = document.getElementById(inputId);
    const suggestionsBox = document.getElementById(suggestionsId);
    let debounceTimer;

    if (!input || !suggestionsBox) return;

    input.addEventListener('input', (e) => {
        clearTimeout(debounceTimer);
        const query = e.target.value.trim();

        if (query.length < 3) {
            suggestionsBox.innerHTML = '';
            suggestionsBox.classList.add('hidden');
            return;
        }

        debounceTimer = setTimeout(async () => {
            try {
                const url = `https://nominatim.openstreetmap.org/search?format=json&limit=5&accept-language=de&q=${encodeURIComponent(query)}`;
                const response = await fetch(url, { headers: { 'Accept-Language': 'de' } });
                const data = await response.json();

                if (data && data.length > 0) {
                    suggestionsBox.innerHTML = '';
                    data.forEach(item => {
                        const itemDiv = document.createElement('div');
                        itemDiv.className = 'px-4 py-2.5 hover:bg-blue-50 dark:hover:bg-slate-700/60 cursor-pointer transition flex items-center gap-2';
                        itemDiv.innerHTML = `<i class="fa-solid fa-location-dot text-blue-500 text-xs flex-shrink-0"></i><span class="truncate">${item.display_name}</span>`;

                        itemDiv.addEventListener('click', () => {
                            input.value = item.display_name;
                            suggestionsBox.innerHTML = '';
                            suggestionsBox.classList.add('hidden');
                            calculateRoute();
                        });

                        suggestionsBox.appendChild(itemDiv);
                    });
                    suggestionsBox.classList.remove('hidden');
                } else {
                    suggestionsBox.classList.add('hidden');
                }
            } catch (err) {
                console.error("Autocomplete-Fehler:", err);
            }
        }, 350);
    });
}

document.addEventListener('click', (e) => {
    ['startInput', 'destInput'].forEach(id => {
        const input = document.getElementById(id);
        const sug = document.getElementById(id === 'startInput' ? 'startSuggestions' : 'destSuggestions');
        if (sug && input && !input.contains(e.target) && !sug.contains(e.target)) {
            sug.classList.add('hidden');
        }
    });
});

async function calculateRoute() {
    const startInput = document.getElementById('startInput');
    const destInput = document.getElementById('destInput');

    if (!startInput || !destInput || !startInput.value.trim() || !destInput.value.trim()) return;

    const startStr = startInput.value;
    const destStr = destInput.value;

    try {
        const startCoords = await geocode(startStr);
        const destCoords = await geocode(destStr);

        if (routingControl) {
            map.removeControl(routingControl);
        }

        routingControl = L.Routing.control({
            waypoints: [
                L.latLng(startCoords[0], startCoords[1]),
                L.latLng(destCoords[0], destCoords[1])
            ],
            lineOptions: { styles: [{ color: '#2563eb', weight: 6 }] },
            createMarker: function(i, wp) {
                return L.marker(wp.latLng, { title: i === 0 ? "Start" : "Ziel" });
            }
        }).addTo(map);

        routingControl.on('routesfound', function(e) {
            const routes = e.routes;
            const summary = routes[0].summary;
            currentDistanceKm = summary.totalDistance / 1000;

            const distDisplay = document.getElementById('distDisplay');
            if (distDisplay) {
                distDisplay.innerText = currentDistanceKm.toFixed(1) + " km";
            }

            updateCalculations();
        });

    } catch (err) {
        console.warn("Routenberechnung fehlgeschlagen:", err.message);
    }
}

function updateCalculations() {
    const consumptionInput = document.getElementById('consumptionInput');
    const priceInput = document.getElementById('priceInput');
    const daysInput = document.getElementById('daysInput');

    const consumption = parseFloat(consumptionInput ? consumptionInput.value : 0) || 0;
    const price = parseFloat(priceInput ? priceInput.value : 0) || 0;
    const days = parseInt(daysInput ? daysInput.value : 0) || 0;

    const oneWayCost = (currentDistanceKm / 100) * consumption * price;
    const dailyCost = oneWayCost * 2;
    const monthlyCost = dailyCost * days;
    const yearlyCost = monthlyCost * 11;

    const dailyDisplay = document.getElementById('dailyCostDisplay');
    const monthlyDisplay = document.getElementById('monthlyCostDisplay');
    const yearlyDisplay = document.getElementById('yearlyCostDisplay');

    if (dailyDisplay) dailyDisplay.innerText = dailyCost.toFixed(2) + " €";
    if (monthlyDisplay) monthlyDisplay.innerText = monthlyCost.toFixed(2) + " €";
    if (yearlyDisplay) yearlyDisplay.innerText = yearlyCost.toFixed(2) + " €";
}

window.addEventListener('DOMContentLoaded', () => {
    if (typeof initTheme === 'function') {
        initTheme();
    }

    initMap();
    setupAutocomplete('startInput', 'startSuggestions');
    setupAutocomplete('destInput', 'destSuggestions');

    setTimeout(() => {
        calculateRoute();
    }, 500);
});