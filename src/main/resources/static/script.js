const API_BASE = window.location.protocol === "file:" ? "http://localhost:8080/api" : "/api";
const META_KEY = "central-missoes-espaciais-meta";
const PANEL_CHARGE = 8;
const ROCKET_MAX_FUEL = 10000;

const labels = {
    Manutencao: "Manutencao",
    Disponivel: "Disponivel",
    "Em calibracao": "Em calibracao",
    "Em orbita": "Em orbita",
    "Em missao": "Em missao",
    "Paineis ativos": "Paineis ativos",
    Comunicacao: "Comunicacao",
};

let state = {
    rockets: [],
    satellites: [],
    missions: [],
    transmissions: 0,
    logs: [],
    apod: null,
};

const elements = {
    rocketList: document.querySelector("#rocketList"),
    satelliteList: document.querySelector("#satelliteList"),
    eventLog: document.querySelector("#eventLog"),
    rocketCount: document.querySelector("#rocketCount"),
    satelliteCount: document.querySelector("#satelliteCount"),
    missionCount: document.querySelector("#missionCount"),
    messageCount: document.querySelector("#messageCount"),
    generalStatus: document.querySelector("#generalStatus"),
    rocketForm: document.querySelector("#rocketForm"),
    satelliteForm: document.querySelector("#satelliteForm"),
    missionForm: document.querySelector("#missionForm"),
    fuelForm: document.querySelector("#fuelForm"),
    panelForm: document.querySelector("#panelForm"),
    messageForm: document.querySelector("#messageForm"),
    nasaTitle: document.querySelector("#nasaTitle"),
    nasaDate: document.querySelector("#nasaDate"),
    nasaMedia: document.querySelector("#nasaMedia"),
    nasaExplanation: document.querySelector("#nasaExplanation"),
};

elements.rocketForm.addEventListener("submit", createRocket);
elements.satelliteForm.addEventListener("submit", createSatellite);
elements.missionForm.addEventListener("submit", startMission);
elements.fuelForm.addEventListener("submit", fuelRocket);
elements.panelForm.addEventListener("submit", activatePanel);
elements.messageForm.addEventListener("submit", sendData);

bootstrap();

async function bootstrap() {
    loadMeta();
    render();
    await Promise.all([refreshResources(), refreshNasaPicture()]);
}

async function refreshResources() {
    try {
        const status = await api("/status");
        state.rockets = (status.foguetes ?? []).map(normalizeRocket);
        state.satellites = (status.satelites ?? []).map(normalizeSatellite);
        syncMissionsWithResources();
        render();
    } catch (error) {
        addLog("API indisponivel", error.message);
        render();
    }
}

async function refreshNasaPicture() {
    try {
        const apod = await api("/nasa/apod");
        state.apod = normalizeApod(apod);
    } catch (error) {
        state.apod = {
            title: "Imagem da NASA indisponivel",
            explanation: error.message,
            mediaType: "error",
        };
    }

    renderNasaPicture();
}

async function api(path, options = {}) {
    const response = await fetch(`${API_BASE}${path}`, {
        headers: {
            "Content-Type": "application/json",
            ...(options.headers ?? {}),
        },
        ...options,
    });

    const text = await response.text();
    const data = text ? JSON.parse(text) : null;

    if (!response.ok) {
        throw new Error(data?.erro ?? data?.message ?? "Erro ao comunicar com a API.");
    }

    return data;
}

function loadMeta() {
    const fallbackLog = {
        title: "Sistema iniciado",
        message: "Central orbital conectada ao Spring Boot.",
        time: new Date().toISOString(),
    };

    try {
        const saved = JSON.parse(localStorage.getItem(META_KEY));
        state.missions = saved?.missions ?? [];
        state.transmissions = saved?.transmissions ?? 0;
        state.logs = saved?.logs?.length ? saved.logs : [fallbackLog];
    } catch {
        state.logs = [fallbackLog];
    }
}

function saveMeta() {
    try {
        localStorage.setItem(
            META_KEY,
            JSON.stringify({
                missions: state.missions,
                transmissions: state.transmissions,
                logs: state.logs.slice(0, 20),
            })
        );
    } catch {
        return;
    }
}

function normalizeRocket(rocket) {
    return {
        id: String(rocket.id),
        name: rocket.name ?? rocket.nome,
        maxLoad: Number(rocket.maxLoad ?? rocket.cargaMaxima ?? 0),
        status: rocket.status ?? "Indefinido",
        fuel: Number(rocket.fuel ?? rocket.combustivelRestante ?? 0),
    };
}

function normalizeSatellite(satellite) {
    return {
        id: String(satellite.id),
        name: satellite.name ?? satellite.nome,
        mass: Number(satellite.mass ?? satellite.massa ?? 0),
        orbit: satellite.orbit ?? satellite.orbitaAlvo ?? "Nao informada",
        energy: Number(satellite.energy ?? satellite.energia ?? 0),
        status: satellite.status ?? "Indefinido",
        panels: satellite.panels ?? satellite.paineis ?? [],
        lastMessage: satellite.lastMessage ?? satellite.ultimaMensagem ?? "",
    };
}

function normalizeApod(apod) {
    return {
        title: apod?.title ?? "Foto astronomica do dia",
        explanation: apod?.explanation ?? "Descricao nao informada pela NASA.",
        url: apod?.url ?? "",
        hdurl: apod?.hdurl ?? "",
        mediaType: apod?.mediaType ?? apod?.media_type ?? "",
        date: apod?.date ?? "",
        copyright: apod?.copyright ?? "",
    };
}

function render() {
    renderCounters();
    renderNasaPicture();
    renderRockets();
    renderSatellites();
    renderSelects();
    renderLog();
}

function renderNasaPicture() {
    if (!elements.nasaTitle || !elements.nasaMedia || !elements.nasaExplanation || !elements.nasaDate) {
        return;
    }

    const apod = state.apod;

    if (!apod) {
        elements.nasaTitle.textContent = "Astronomy Picture of the Day";
        elements.nasaDate.textContent = "NASA";
        elements.nasaExplanation.textContent = "Aguardando dados da API da NASA.";
        elements.nasaMedia.replaceChildren(emptyState("Carregando imagem da NASA..."));
        return;
    }

    elements.nasaTitle.textContent = apod.title;
    elements.nasaDate.textContent = apod.date ? formatDate(apod.date) : "NASA APOD";
    elements.nasaExplanation.textContent = apod.copyright
        ? `${apod.explanation} Credito: ${apod.copyright}.`
        : apod.explanation;

    elements.nasaMedia.replaceChildren();

    if (apod.mediaType === "image" && apod.url) {
        const image = document.createElement("img");
        image.src = apod.url;
        image.alt = apod.title;
        elements.nasaMedia.appendChild(image);
        return;
    }

    if (apod.mediaType === "video" && apod.url) {
        const link = document.createElement("a");
        link.className = "nasa-link";
        link.href = apod.url;
        link.target = "_blank";
        link.rel = "noopener noreferrer";
        link.textContent = "Abrir video da NASA";
        elements.nasaMedia.appendChild(link);
        return;
    }

    elements.nasaMedia.appendChild(emptyState("Midia da NASA indisponivel no momento."));
}

function renderCounters() {
    const activeMissionCount = getActiveMissionCount();

    elements.rocketCount.textContent = state.rockets.length;
    elements.satelliteCount.textContent = state.satellites.length;
    elements.missionCount.textContent = activeMissionCount;
    elements.messageCount.textContent = state.transmissions;
    elements.generalStatus.textContent = activeMissionCount > 0 ? "Missao ativa" : "Operacional";
}

function renderRockets() {
    elements.rocketList.replaceChildren();

    if (!state.rockets.length) {
        elements.rocketList.appendChild(emptyState("Nenhum foguete cadastrado."));
        return;
    }

    state.rockets.forEach((rocket) => {
        const card = document.createElement("article");
        card.className = "resource-card";

        const top = document.createElement("div");
        top.className = "resource-top";

        const title = document.createElement("h3");
        title.className = "resource-title";
        title.textContent = rocket.name;

        const actions = document.createElement("div");
        actions.className = "resource-actions";
        actions.append(statusBadge(rocket.status), createDeleteButton(`Excluir foguete ${rocket.name}`, () => deleteRocket(rocket)));

        top.append(title, actions);

        const details = document.createElement("div");
        details.className = "details-grid";
        details.append(
            detail("Combustivel", `${formatNumber(rocket.fuel)} L`),
            detail("Carga maxima", `${formatNumber(rocket.maxLoad)} kg`),
            detail("Identificador", formatId(rocket.id))
        );

        const meter = document.createElement("div");
        meter.className = "meter";
        const fuelBar = document.createElement("span");
        fuelBar.style.width = `${clamp((rocket.fuel / ROCKET_MAX_FUEL) * 100, 0, 100)}%`;
        meter.appendChild(fuelBar);

        card.append(top, details, meter);
        elements.rocketList.appendChild(card);
    });
}

function renderSatellites() {
    elements.satelliteList.replaceChildren();

    if (!state.satellites.length) {
        elements.satelliteList.appendChild(emptyState("Nenhum satelite cadastrado."));
        return;
    }

    state.satellites.forEach((satellite) => {
        const card = document.createElement("article");
        card.className = "resource-card";

        const top = document.createElement("div");
        top.className = "resource-top";

        const title = document.createElement("h3");
        title.className = "resource-title";
        title.textContent = satellite.name;

        const actions = document.createElement("div");
        actions.className = "resource-actions";
        actions.append(
            statusBadge(satellite.status),
            createDeleteButton(`Excluir satelite ${satellite.name}`, () => deleteSatellite(satellite))
        );

        top.append(title, actions);

        const details = document.createElement("div");
        details.className = "details-grid";
        details.append(
            detail("Massa", `${formatNumber(satellite.mass)} kg`),
            detail("Orbita alvo", satellite.orbit),
            detail("Energia", `${satellite.energy}%`),
            detail("Identificador", formatId(satellite.id))
        );

        const meter = document.createElement("div");
        meter.className = "meter";
        const energyBar = document.createElement("span");
        energyBar.style.width = `${Math.max(0, Math.min(100, satellite.energy))}%`;
        meter.appendChild(energyBar);

        const tags = document.createElement("div");
        tags.className = "panel-tags";
        const panels = satellite.panels.length ? satellite.panels : ["Sem paineis ativos"];
        panels.forEach((panel) => {
            const tag = document.createElement("span");
            tag.textContent = labelFor(panel);
            tags.appendChild(tag);
        });

        card.append(top, details, meter, tags);
        elements.satelliteList.appendChild(card);
    });
}

function renderSelects() {
    const missionRocketSelect = elements.missionForm.querySelector('select[name="rocket"]');
    const fuelRocketSelect = elements.fuelForm.querySelector('select[name="rocket"]');
    const missionSatelliteSelect = elements.missionForm.querySelector('select[name="satellite"]');
    const panelSatelliteSelect = elements.panelForm.querySelector('select[name="satellite"]');
    const messageSatelliteSelect = elements.messageForm.querySelector('select[name="satellite"]');

    fillSelect(
        missionRocketSelect,
        state.rockets.filter(isRocketReadyForMission),
        (rocket) => `${rocket.name} - ${labelFor(rocket.status)}`
    );
    fillSelect(
        fuelRocketSelect,
        state.rockets.filter(isRocketAvailableForFuel),
        (rocket) => `${rocket.name} - ${labelFor(rocket.status)}`
    );

    fillSelect(
        missionSatelliteSelect,
        state.satellites.filter(isSatelliteAvailableForMission),
        (satellite) => `${satellite.name} - ${labelFor(satellite.status)}`
    );
    fillSelect(panelSatelliteSelect, state.satellites, (satellite) => `${satellite.name} - ${labelFor(satellite.status)}`);
    fillSelect(messageSatelliteSelect, state.satellites, (satellite) => `${satellite.name} - ${labelFor(satellite.status)}`);
}

function renderLog() {
    elements.eventLog.replaceChildren();

    state.logs.slice(0, 9).forEach((entry) => {
        const item = document.createElement("p");
        item.className = "log-entry";

        const title = document.createElement("strong");
        title.textContent = `${entry.title} | ${formatTime(entry.time)}`;

        const message = document.createTextNode(entry.message);
        item.append(title, message);
        elements.eventLog.appendChild(item);
    });
}

async function createRocket(event) {
    event.preventDefault();
    const form = event.currentTarget;
    const data = new FormData(form);
    const name = data.get("nome").trim();
    const fuelAmount = Number(data.get("combustivel"));
    const maxLoad = Number(data.get("carga"));
    const status = data.get("status");

    if (!name || Number.isNaN(fuelAmount) || fuelAmount < 0 || maxLoad <= 0) {
        return;
    }

    await runOperation(async () => {
        await api("/foguetes", {
            method: "POST",
            body: JSON.stringify({
                nome: name,
                combustivelRestante: fuelAmount,
                cargaMaxima: maxLoad,
                status,
            }),
        });

        addLog("Foguete criado", `${name} entrou no hangar com ${formatNumber(fuelAmount)} L de combustivel.`);
        form.reset();
    });
}

async function createSatellite(event) {
    event.preventDefault();
    const form = event.currentTarget;
    const data = new FormData(form);
    const name = data.get("nome").trim();
    const mass = Number(data.get("massa"));
    const orbit = data.get("orbita").trim();
    const energy = clamp(Number(data.get("energia")), 0, 100);
    const status = data.get("status");

    if (!name || !orbit || mass <= 0) {
        return;
    }

    await runOperation(async () => {
        await api("/satelites", {
            method: "POST",
            body: JSON.stringify({
                nome: name,
                massa: mass,
                orbitaAlvo: orbit,
                energia: energy,
                status,
            }),
        });

        addLog("Satelite criado", `${name} foi configurado para a orbita ${orbit}.`);
        form.reset();
    });
}

async function startMission(event) {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const rocket = findRocket(data.get("rocket"));
    const satellite = findSatellite(data.get("satellite"));

    if (!rocket || !satellite) {
        return;
    }

    if (!isRocketReadyForMission(rocket) || !isSatelliteAvailableForMission(satellite)) {
        return;
    }

    await runOperation(async () => {
        const response = await api("/missoes/iniciar", {
            method: "POST",
            body: JSON.stringify({
                fogueteId: Number(rocket.id),
                sateliteId: Number(satellite.id),
            }),
        });

        state.missions.push({
            id: createId("mission"),
            rocketId: rocket.id,
            satelliteId: satellite.id,
            time: new Date().toISOString(),
        });

        addLog("Missao iniciada", response.resultado ?? `${rocket.name} levou ${satellite.name} para ${satellite.orbit}.`);
    });
}

async function deleteRocket(rocket) {
    if (!rocket || !window.confirm(`Excluir foguete ${rocket.name}?`)) {
        return;
    }

    await runOperation(async () => {
        await api(`/foguetes/${rocket.id}`, { method: "DELETE" });
        state.missions = state.missions.filter((mission) => mission.rocketId !== rocket.id);
        addLog("Foguete excluido", `${rocket.name} foi removido do hangar.`);
    });
}

async function deleteSatellite(satellite) {
    if (!satellite || !window.confirm(`Excluir satelite ${satellite.name}?`)) {
        return;
    }

    await runOperation(async () => {
        await api(`/satelites/${satellite.id}`, { method: "DELETE" });
        state.missions = state.missions.filter((mission) => mission.satelliteId !== satellite.id);
        addLog("Satelite excluido", `${satellite.name} foi removido da lista orbital.`);
    });
}

async function fuelRocket(event) {
    event.preventDefault();
    const form = event.currentTarget;
    const data = new FormData(form);
    const rocket = findRocket(data.get("rocket"));
    const amount = Number(data.get("quantidade"));

    if (!rocket || amount <= 0) {
        return;
    }

    if (!isRocketAvailableForFuel(rocket)) {
        return;
    }

    await runOperation(async () => {
        await api(`/foguetes/${rocket.id}/abastecer`, {
            method: "POST",
            body: JSON.stringify({ quantidade: amount }),
        });

        addLog("Foguete abastecido", `${rocket.name} recebeu ${formatNumber(amount)} L de combustivel.`);
        form.reset();
    });
}

async function activatePanel(event) {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const satellite = findSatellite(data.get("satellite"));
    const panel = data.get("panel");

    if (!satellite || !panel) {
        return;
    }

    await runOperation(async () => {
        await api(`/satelites/${satellite.id}/ativar-paineis`, {
            method: "POST",
            body: JSON.stringify({
                painel: panel,
                quantidade: PANEL_CHARGE,
            }),
        });

        addLog("Painel ativado", `${labelFor(panel)} de ${satellite.name} esta ativo.`);
    });
}

async function sendData(event) {
    event.preventDefault();
    const form = event.currentTarget;
    const data = new FormData(form);
    const satellite = findSatellite(data.get("satellite"));
    const message = data.get("mensagem").trim();

    if (!satellite || !message) {
        return;
    }

    await runOperation(async () => {
        const response = await api(`/satelites/${satellite.id}/enviar-dados`, {
            method: "POST",
            body: JSON.stringify({ mensagem: message }),
        });

        state.transmissions += 1;
        addLog("Dados enviados", response.mensagem ?? `${satellite.name}: "${message}"`);
        form.reset();
    });
}

async function runOperation(operation) {
    setFormsDisabled(true);

    try {
        await operation();
        saveMeta();
        await refreshResources();
    } catch (error) {
        addLog("Operacao recusada", error.message);
        saveMeta();
        render();
    } finally {
        setFormsDisabled(false);
    }
}

function addLog(title, message) {
    state.logs.unshift({
        title,
        message,
        time: new Date().toISOString(),
    });
}

function fillSelect(select, items, labelBuilder) {
    if (!select) {
        return;
    }

    const current = select.value;
    select.replaceChildren();

    if (!items.length) {
        const option = document.createElement("option");
        option.value = "";
        option.textContent = "Nenhum item disponivel";
        option.disabled = true;
        option.selected = true;
        select.appendChild(option);
        return;
    }

    items.forEach((item) => {
        const option = document.createElement("option");
        option.value = item.id;
        option.textContent = labelBuilder(item);
        select.appendChild(option);
    });

    if (items.some((item) => item.id === current)) {
        select.value = current;
    }
}

function statusBadge(status) {
    const badge = document.createElement("span");
    badge.className = `status-badge ${statusClass(status)}`;
    badge.textContent = labelFor(status);
    return badge;
}

function createDeleteButton(label, onClick) {
    const button = document.createElement("button");
    button.type = "button";
    button.className = "delete-button";
    button.title = label;
    button.setAttribute("aria-label", label);
    button.textContent = "Excluir";
    button.addEventListener("click", onClick);
    return button;
}

function detail(label, value) {
    const wrapper = document.createElement("div");
    const detailLabel = document.createElement("span");
    const detailValue = document.createElement("strong");

    detailLabel.className = "detail-label";
    detailLabel.textContent = label;
    detailValue.className = "detail-value";
    detailValue.textContent = value;

    wrapper.append(detailLabel, detailValue);
    return wrapper;
}

function emptyState(message) {
    const empty = document.createElement("p");
    empty.className = "empty-state";
    empty.textContent = message;
    return empty;
}

function statusClass(status) {
    return `status-${removeAccents(labelFor(status))
        .toLowerCase()
        .replace(/[^a-z0-9]+/g, "-")
        .replace(/^-|-$/g, "")}`;
}

function labelFor(value) {
    return labels[value] ?? value;
}

function removeAccents(value) {
    return value.normalize("NFD").replace(/[\u0300-\u036f]/g, "");
}

function findRocket(id) {
    return state.rockets.find((rocket) => rocket.id === id);
}

function findSatellite(id) {
    return state.satellites.find((satellite) => satellite.id === id);
}

function syncMissionsWithResources() {
    const previousCount = state.missions.length;

    state.missions = state.missions.filter((mission) => {
        const rocket = findRocket(String(mission.rocketId));
        const satellite = findSatellite(String(mission.satelliteId));
        return isRocketInMission(rocket) && isSatelliteInMission(satellite);
    });

    if (state.missions.length !== previousCount) {
        saveMeta();
    }
}

function getActiveMissionCount() {
    const savedMissionCount = state.missions.filter((mission) => {
        const rocket = findRocket(String(mission.rocketId));
        const satellite = findSatellite(String(mission.satelliteId));
        return isRocketInMission(rocket) && isSatelliteInMission(satellite);
    }).length;

    if (savedMissionCount > 0) {
        return savedMissionCount;
    }

    const activeRockets = state.rockets.filter(isRocketInMission).length;
    const activeSatellites = state.satellites.filter(isSatelliteInMission).length;
    return Math.min(activeRockets, activeSatellites);
}

function isRocketInMission(rocket) {
    return rocket?.status === "Em missao";
}

function isRocketAvailableForFuel(rocket) {
    return !isRocketInMission(rocket);
}

function isRocketReadyForMission(rocket) {
    return rocket?.status === "Pronto";
}

function isSatelliteInMission(satellite) {
    return satellite?.status === "Em orbita" || satellite?.status === "Paineis ativos";
}

function isSatelliteAvailableForMission(satellite) {
    return !isSatelliteInMission(satellite);
}

function createId(prefix) {
    return `${prefix}-${Date.now().toString(36)}-${Math.random().toString(36).slice(2, 7)}`;
}

function clamp(value, min, max) {
    return Math.min(max, Math.max(min, value || 0));
}

function formatNumber(value) {
    return new Intl.NumberFormat("pt-BR").format(value);
}

function formatId(id) {
    return `#${String(id).replace(/^(rocket|sat)-/, "")}`;
}

function formatTime(value) {
    return new Intl.DateTimeFormat("pt-BR", {
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit",
    }).format(new Date(value));
}

function formatDate(value) {
    return new Intl.DateTimeFormat("pt-BR", {
        day: "2-digit",
        month: "long",
        year: "numeric",
    }).format(new Date(`${value}T00:00:00`));
}

function setFormsDisabled(disabled) {
    document.querySelectorAll("button, input, select, textarea").forEach((element) => {
        element.disabled = disabled;
    });
}
