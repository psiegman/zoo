var zoo = new Vue({
    el: '#zoo',
    data: {
        pens: [],
        animalsById: {}
    },
    methods: {
        createAnimal: function (species) {
            const animalName = prompt('Please enter the new ' + species.emoji + ' name');
            if (animalName) {
                axios.put('/api/animals/create', [{speciesId: species.id, name: animalName}]);
            }
        },
        likeAnimal: function (animalId) {
            axios.post('/api/animals/' + animalId + "/like");
        },
        animalUpdateStatus: function (animalId, status) {
            axios.post('/api/animals/' + animalId + "/status/" + status);
        }
    },
    mounted() {
        axios
            .get('/api/zoo')
            .then(function (response) {
                zoo.pens = response.data.pens;

                // build this.animals lookup cache
                zoo.pens.forEach(p => p.animals.forEach(a => zoo.animalsById[a.id] = a));
            });

        const client = new StompJs.Client({
            brokerURL: "ws://" + new URL(window.location.href).host + '/zoo/events/websocket'
        });

        client.onConnect = function (frame) {
            client.subscribe('/animals/likes', function (message) {
                var likeUpdateEvent = JSON.parse(message.body);
                zoo.animalsById[likeUpdateEvent.animalId].nrLikes = likeUpdateEvent.nrLikes;
            });
            client.subscribe('/animals/status', function (message) {
                var statusUpdateEvent = JSON.parse(message.body);
                zoo.animalsById[statusUpdateEvent.animalId].status = statusUpdateEvent.status;
            });
            client.subscribe('/animals/create', function (message) {
                var animalCreatedEvent = JSON.parse(message.body);
                for (pen of zoo.pens) {
                    if (pen.species.id === animalCreatedEvent.speciesId) {
                        var animal = {
                            id: animalCreatedEvent.animalId,
                            name: animalCreatedEvent.name,
                            nrLikes: animalCreatedEvent.nrLikes,
                            status: animalCreatedEvent.status
                        };
                        pen.animals.push(animal);
                        zoo.animalsById[animal.id] = animal;
                        break;
                    }
                }
            });
        };

        client.activate();
    }
});