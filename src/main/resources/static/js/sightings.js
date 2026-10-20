// Wait for the page to finish loading
document.addEventListener("DOMContentLoaded", async () => {

    try {
        // 1️⃣ Load the world map data from Highcharts
        const worldMap = await fetch('https://code.highcharts.com/mapdata/custom/world.topo.json')
            .then(response => response.json());

        if (!worldMap) {
            console.error("Could not load the map!");
            return;
        }

        // 2️⃣ Prepare the sightings data from the server
        // `window.heatmapData` comes from Thymeleaf, populated by the controller
        console.log(window.heatmapData[0], window.heatmapData[1],window.heatmapData[3]);

        // Prepare data
        const pointData = window.heatmapData.map(p => {
            const radius = 2 + (p.z); // 2px + 1px voor elke sighting scale radius with number of sightings
            return {
                lat: p.lat,
                lon: p.lon,
                marker: {
                    radius: radius,
                    lineColor: '#000',
                    lineWidth: 1,
                    fillColor: 'aqua'
                },
                tooltip: p.tooltip
            };
        });


        console.log("Point data:", pointData);

        // 3️⃣ Create the map chart using Highcharts
        Highcharts.mapChart('container', {
            chart: {
                map: worldMap // the map we loaded
            },
            title: {
                text: '' // map title
            },
            mapNavigation: {
                enabled: true // allow zoom and pan
            },


            series: [
                {
                    // Base map series (the countries)
                    mapData: worldMap,
                    nullColor: '#EFEFEF', // color for empty countries
                    borderColor: '#D3D3D3', //border for countries
                    borderWith: 0.33, //1px width
                    showInLegend: false
                },
                {
                    // Sightings dots
                    type: 'mappoint',
                    name: 'Sightings',
                    data: pointData,
                    tooltip: {
                        headerFormat: '',
                        pointFormat: '{point.tooltip}' // show all sightings at this location
                    },


                }
            ]
        });

    } catch (error) {
        console.error("Error creating the map:", error);
    }

});
