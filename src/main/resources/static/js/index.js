
    /* Config mapa */
    const map = L.map('map').setView([0, 0], 16);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; OpenStreetMap'
}).addTo(map);

    const iconAccessible = L.icon({
    iconUrl: '/icons/accessible.png',
    iconSize: [24, 24],
    iconAnchor: [12, 24],
    popupAnchor: [0, -24],
});
    const iconDefault = L.icon({
    iconUrl: '/icons/default.png',
    iconSize: [24, 24],
    iconAnchor: [12, 24],
    popupAnchor: [0, -24],
});

    // Variável global para rota
    let routingControl = null;

    map.on('click',e=>{
    const {lat,lng}=e.latlng;
    L.popup().setLatLng(e.latlng).setContent(`
    <div class="text-center">
      <b class="block mb-2 text-lg text-gray-800">Deseja cadastrar um banheiro aqui?</b>
      <button id="btnAbrirInline" class="inline-block bg-indigo-600 hover:bg-indigo-700 text-white font-semibold px-4 py-2 rounded transition">📍 Cadastrar aqui</button>
    </div>
  `).openOn(map);

    setTimeout(()=>{
    const btn=document.getElementById('btnAbrirInline');
    if(btn){
    btn.onclick=()=>{
    abrirModalCadastro(lat,lng);
    map.closePopup();
};
}
},50);
});

    function abrirModalCadastro(lat = null, lng = null) {
        const modal = document.getElementById('modalCadastro');
        const inputLat = document.getElementById('cLat');
        const inputLng = document.getElementById('cLng');

        if (lat !== null && lng !== null) {
            // chamado pelo mapa (com coordenadas)
            inputLat.value = lat.toFixed(8);
            inputLng.value = lng.toFixed(8);
            modal.classList.remove('hidden');
        } else if (navigator.geolocation) {
            // chamado pelo botão do topo
            navigator.geolocation.getCurrentPosition(
                pos => {
                    inputLat.value = pos.coords.latitude.toFixed(8);
                    inputLng.value = pos.coords.longitude.toFixed(8);
                    modal.classList.remove('hidden');
                },
                () => showAlerta('Não foi possível obter sua localização.', 'erro'),
                { enableHighAccuracy: true, timeout: 10000 }
            );
        } else {
            showAlerta('Geolocalização não suportada.', 'erro');
        }
    }


    document.getElementById('cancelCadastro').onclick=()=>{
    document.getElementById('modalCadastro').classList.add('hidden');
};

    document.getElementById('formCadastroInline').addEventListener('submit', async e => {
        e.preventDefault();
        const f = e.target;

        const data = {
            place: {
                name: f.cName.value,
                description: f.cDesc.value,
                type: f.cType.value,
                latitude: parseFloat(f.cLat.value),
                longitude: parseFloat(f.cLng.value)
            },
            acess: f.cAcess.value === 'true',
            publicPlace: f.cPublic.value === 'true',
            free: f.cFree.value === 'true',
            babyChangingTable: f.cBaby.value === 'true',
            hasPaper: f.cPaper.value === 'true',
            hasSoap: f.cSoap.value === 'true',
            hasSanitizer: f.cAlc.value === 'true',
            averageStars: 0
        };


        try {
            const res = await fetch('/api/restrooms', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });

            if (res.ok) {
                await carregar();
                showAlerta('Banheiro cadastrado com sucesso!');
                f.reset();
                document.getElementById('modalCadastro').classList.add('hidden');
            } else {
                showAlerta('Erro ao cadastrar', 'erro');
            }
        } catch (err) {
            showAlerta('Erro de rede', 'erro');
        }
    });

    let markers = [], allRestrooms = [];

    function renderMarkers(list) {
    markers.forEach(m => map.removeLayer(m));
    markers = [];

    const f = {
    acess: f_acess.checked,
    gratis: f_gratis.checked,
    publico: f_publico.checked,
    fraldario: f_fraldario.checked,
    papel: f_papel.checked,
    sabao: f_sabao.checked,
    alcool: f_alcool.checked,
    tipo: f_tipo.value
};

    list.forEach(r => {
        if (!r.location) return;
        const { latitude: lat, longitude: lng } = r.location;

        if (lat < -90 || lat > 90 || lng < -180 || lng > 180) return;
    if (f.acess && !r.accessible) return;
    if (f.gratis && !r.free) return;
    if (f.publico && !r.publicPlace) return;
    if (f.fraldario && !r.babyChangingTable) return;
    if (f.papel && !r.hasPaper) return;
    if (f.sabao && !r.hasSoap) return;
    if (f.alcool && !r.hasSanitizer) return;
    if (f.tipo && r.type !== f.tipo) return;

        const distRaw = map.distance(map.getCenter(), [lat, lng]);
        const distFormatted = distRaw >= 1000
            ? `${(distRaw / 1000).toFixed(2)} km`
            : `${Math.round(distRaw)} m`;

        const popup = `
          <div class="popup">
            <b class="text-indigo-700 text-lg">${r.name}</b>
            <span>⭐ ${(r.averageStars ?? 0).toFixed(1)}</span>
            <span>📍 ${distFormatted}</span>
            <button class="btn-detalhes mt-2 bg-indigo-600 text-white rounded px-3 py-1 hover:bg-indigo-700 transition" data-id="${r.id}">Ver detalhes</button>
          </div>
        `;
    const icon = r.accessible ? iconAccessible : iconDefault;
    const marker = L.marker([lat, lng], { icon }).addTo(map).bindPopup(popup);

    marker.on("popupopen", () => {
    setTimeout(() => {
    const btn = document.querySelector(".btn-detalhes");
    if (btn) {
    btn.addEventListener("click", () => abrirDetalhes(r.id));
}
}, 50);
});

    markers.push(marker);
});
}

    async function carregar() {
    const res = await fetch('/api/restrooms/all');
    allRestrooms = await res.json();
    renderMarkers(allRestrooms);
}

    navigator.geolocation.getCurrentPosition(p => {
    map.setView([p.coords.latitude, p.coords.longitude], 16);
    L.marker([p.coords.latitude, p.coords.longitude]).addTo(map).bindPopup('Você está aqui').openPopup();
    carregar();
}, () => {
    carregar();
    showAlerta('Não foi possível obter sua localização');
});

    document.getElementById('filtros').addEventListener('change', () => renderMarkers(allRestrooms));

    /* Modal avaliação */
    let currentId = null, selectedStars = 0;
    document.addEventListener('click', e => {
    const btnRate = e.target.closest('.btn-rate');
    if (btnRate) {
    currentId = btnRate.dataset.id;
    document.getElementById('ratingModal').classList.remove('hidden');
}
});

    document.querySelectorAll('#stars span').forEach(s => {
    s.onclick = () => {
        selectedStars = +s.dataset.val;
        document.querySelectorAll('#stars span').forEach(x => x.classList.toggle('sel', +x.dataset.val <= selectedStars));
    };
});

    rateSubmit.onclick = async () => {
    if (!selectedStars) return showAlerta('Escolha a nota!','erro');
    const body = { stars: selectedStars, comment: rateComment.value };
    const res = await fetch(`/api/restrooms/${currentId}/ratings`, {
    method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body)
});
    if (res.ok) { showAlerta('Avaliação enviada!'); location.reload(); }
    else showAlerta('Erro ao avaliar','erro');
};
    rateClose.onclick = () => ratingModal.classList.add('hidden');

    /* Botão ver detalhes */
    document.addEventListener('click', e => {
    const btn = e.target.closest('.btn-detalhes');
    if (!btn) return;
    abrirDetalhes(+btn.dataset.id);
});

    async function abrirDetalhes(id) {
    map.closePopup();

    if (routingControl) {
    map.removeControl(routingControl);
    routingControl = null;
}

    const res = await fetch(`/api/restrooms/${id}/detalhes`);
    if (!res.ok) {
    showAlerta('Erro ao carregar detalhes','erro');
    return;
}
    const data = await res.json();

    const r = data.restroom;
    const ratings = data.ratings;
    const stars = data.average?.toFixed(1) ?? 'N/A';

    const div = document.getElementById("conteudoDetalhes");

    let comentarios = '';
    if (ratings.length > 0) {
    comentarios = ratings.slice(0, 5).map(c => `
      <div class="border-t border-gray-300 pt-2 mt-2 text-sm">
        ⭐ ${c.stars} <br>
        ${c.comment || '<i>Sem comentário</i>'}
      </div>
    `).join('');
} else {
    comentarios = '<i>Sem comentários ainda</i>';
}

    div.innerHTML = `
    <h2 class="text-2xl font-bold mb-2">${r.name}</h2>
    <p class="mb-2">${r.description || 'Sem descrição'}</p>
    <p class="mb-4 font-semibold">⭐ Avaliação média: ${stars}</p>
    <ul class="mb-4 list-disc list-inside space-y-1 text-gray-700">
      <li><b>Acessível:</b> ${r.accessible ? 'Sim' : 'Não'}</li>
      <li><b>Gratuito:</b> ${r.free ? 'Sim' : 'Não'}</li>
      <li><b>Público:</b> ${r.publicPlace ? 'Sim' : 'Não'}</li>
      <li><b>Fraldário:</b> ${r.babyChangingTable ? 'Sim' : 'Não'}</li>
      <li><b>Papel:</b> ${r.hasPaper ? 'Sim' : 'Não'}</li>
      <li><b>Sabão:</b> ${r.hasSoap ? 'Sim' : 'Não'}</li>
      <li><b>Álcool:</b> ${r.hasSanitizer ? 'Sim' : 'Não'}</li>
      <li><b>Tipo:</b> ${r.type || 'Não informado'}</li>
    </ul>
    <h3 class="font-semibold text-lg mb-2">Comentários:</h3>
    ${comentarios}
    <div class="flex gap-2 mt-4">
      <button id="btnAvaliar" class="flex-grow bg-indigo-600 text-white rounded px-4 py-2 hover:bg-indigo-700 transition">Avaliar e comentar</button>
      <button id="btnTraçarRota" class="flex-grow bg-green-600 text-white rounded px-4 py-2 hover:bg-green-700 transition">Traçar rota</button>
    </div>
  `;

    document.getElementById("detalhesModal").classList.remove("hidden");

    document.getElementById("btnAvaliar").addEventListener("click", () => {
    currentId = id;
    document.getElementById("detalhesModal").classList.add("hidden");
    document.getElementById("ratingModal").classList.remove("hidden");
});

    document.getElementById("btnTraçarRota").addEventListener("click", () => {
    adicionarRota(r.location.latitude, r.location.longitude);
    document.getElementById("detalhesModal").classList.add("hidden");
});
}


    function fecharModal() {
    document.getElementById("detalhesModal").classList.add("hidden");
    if (routingControl) {
    map.removeControl(routingControl);
    routingControl = null;
}
    if (watchId) {
    navigator.geolocation.clearWatch(watchId);
    watchId = null;
}
}

    let destinoAtual = null;
    let watchId = null;
    let userMarker = null;

    function adicionarRota(destinoLat, destinoLng) {
    if (!navigator.geolocation) {
    showAlerta("Geolocalização não suportada", 'erro');
    return;
}

    // inicia diretamente o rastreamento em tempo real
    adicionarRotaEmTempoReal(destinoLat, destinoLng);
}



    function adicionarRotaEmTempoReal(destinoLat, destinoLng) {
    destinoAtual = L.latLng(destinoLat, destinoLng);

    if (watchId) {
    navigator.geolocation.clearWatch(watchId);
    watchId = null;
}

    watchId = navigator.geolocation.watchPosition(pos => {
    const origem = L.latLng(pos.coords.latitude, pos.coords.longitude);

    // Atualiza marcador do usuário na posição atual
    if (!userMarker) {
    userMarker = L.marker(origem).addTo(map).bindPopup('Você está aqui');
} else {
    userMarker.setLatLng(origem);
}

    // Centraliza o mapa na posição do usuário
    if (centralizarAtivo) {
    map.setView(origem, map.getZoom());
}

    // Remove rota antiga se existir
    if (routingControl) map.removeControl(routingControl);

    // Desenha nova rota atualizada
    routingControl = L.Routing.control({
    waypoints: [origem, destinoAtual],
    routeWhileDragging: false,
    draggableWaypoints: false,
    addWaypoints: false,
    createMarker: () => null,
    lineOptions: {
    styles: [{ color: '#4f46e5', opacity: 0.9, weight: 6 }]
}
}).addTo(map);
}, err => {
    console.error("Erro ao obter localização em tempo real", err);
}, {
    enableHighAccuracy: true,
    maximumAge: 1000,
    timeout: 10000
});
}


    function showAlerta(msg, tipo='sucesso') {
    const box = document.getElementById('modalAlertaBox');
    const icon = document.getElementById('modalAlertaIcon');
    const msgDiv = document.getElementById('modalAlertaMsg');
    const modal = document.getElementById('modalAlerta');

    msgDiv.textContent = msg;

    if (tipo === 'erro') {
    icon.textContent = '❌';
    box.classList.remove('border-green-500');
    box.classList.add('border-red-500');
} else {
    icon.textContent = '✅';
    box.classList.remove('border-red-500');
    box.classList.add('border-green-500');
}

    modal.classList.remove('hidden');
}

    function fecharAlerta() {
    document.getElementById('modalAlerta').classList.add('hidden');
}

    function renderBanheirosProximos(banheiros) {
    const container = document.getElementById('listaProximos');
    container.innerHTML = '';

    banheiros.forEach(b => {
    const div = document.createElement('div');
    div.className = 'bg-white rounded-lg p-4 shadow border flex flex-col gap-2';

    const media = b.averageStars ? b.averageStars.toFixed(1) : '—';
    const stars = b.averageStars ? '⭐'.repeat(Math.round(b.averageStars)) : '⭐—';
        let distancia = '';
        if (b.distanciaKm != null) {
            distancia = b.distanciaKm < 1
                ? `${Math.round(b.distanciaKm * 1000)} m`
                : `${b.distanciaKm.toFixed(2)} km`;
        }


        div.innerHTML = `
            <div class="flex items-center justify-between">
                <h3 class="text-lg font-semibold text-indigo-700">${b.name}</h3>
                <span class="text-sm text-gray-500">${distancia}</span>
            </div>
            <p class="text-sm text-gray-600">${b.type}</p>
            <div class="flex items-center text-yellow-500 text-sm">${stars} <span class="ml-2 text-gray-500">(${media})</span></div>
            <div class="flex gap-2 mt-auto">
                <button class="text-sm text-blue-600 hover:underline" onclick="abrirDetalhes(${b.id})">Detalhes</button>
                <button class="text-sm bg-indigo-600 text-white px-2 py-1 rounded hover:bg-indigo-700" onclick="adicionarRota(${b.latitude}, ${b.longitude})">Rota</button>
            </div>
        `;
    container.appendChild(div);
});
}


    async function buscarBanheirosProximos() {
    if (!navigator.geolocation) return;

    const raio = parseFloat(document.getElementById('filtroDistancia')?.value || 2);

    navigator.geolocation.getCurrentPosition(async pos => {
    const { latitude, longitude } = pos.coords;

    try {
    const res = await fetch(`/api/restrooms/proximos?lat=${latitude}&lng=${longitude}&radiusKm=${raio}`);
    const data = await res.json();
    renderBanheirosProximos(data);
} catch (e) {
    console.error("Erro ao buscar banheiros próximos:", e);
}
}, () => {
    console.error("Não foi possível obter localização");
});
}


    window.onload = () => {
    buscarBanheirosProximos();
};

    let debounceTimeout = null;

    const inputRaio = document.getElementById('filtroDistancia');

    inputRaio.addEventListener('input', () => {
    clearTimeout(debounceTimeout);
    debounceTimeout = setTimeout(() => {
    buscarBanheirosProximos();
}, 500); // espera 500ms após parar de digitar
});

    let centralizarAtivo = true;

    const btnCentralizar = document.getElementById('btnCentralizar');
    btnCentralizar.addEventListener('click', () => {
    centralizarAtivo = !centralizarAtivo;
    btnCentralizar.textContent = `Centralizar: ${centralizarAtivo ? 'ON' : 'OFF'}`;
});

    function toggleLoading(show) {
        document.getElementById('loading').classList.toggle('hidden', !show);
    }
