package br.com.missaoespacial.service;

import br.com.missaoespacial.dto.StatusMissaoResponse;
import br.com.missaoespacial.model.Foguete;
import br.com.missaoespacial.model.Satelite;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CentroControleService {

    private final Map<Long, Foguete> foguetes = new ConcurrentHashMap<>();
    private final Map<Long, Satelite> satelites = new ConcurrentHashMap<>();
    private final AtomicLong fogueteSequence = new AtomicLong(1);
    private final AtomicLong sateliteSequence = new AtomicLong(1);

    public Foguete adicionarFoguete(Foguete foguete) {
        validarFoguete(foguete);
        long id = fogueteSequence.getAndIncrement();
        foguete.setId(id);
        foguetes.put(id, foguete);
        return foguete;
    }

    public Satelite adicionarSatelite(Satelite satelite) {
        validarSatelite(satelite);
        long id = sateliteSequence.getAndIncrement();
        satelite.setId(id);
        satelites.put(id, satelite);
        return satelite;
    }

    public List<Foguete> listarFoguetes() {
        return foguetes.values().stream()
                .sorted(Comparator.comparing(Foguete::getId))
                .collect(Collectors.toList());
    }

    public List<Satelite> listarSatelites() {
        return satelites.values().stream()
                .sorted(Comparator.comparing(Satelite::getId))
                .collect(Collectors.toList());
    }

    public Foguete abastecerFoguete(long fogueteId, float quantidade) {
        Foguete foguete = buscarFoguete(fogueteId);
        foguete.abastecer(quantidade);
        return foguete;
    }

    public Satelite ativarPaineis(long sateliteId, String painel, float quantidade) {
        Satelite satelite = buscarSatelite(sateliteId);
        satelite.ativarPaineis(painel, quantidade);
        return satelite;
    }

    public String enviarDados(long sateliteId, String mensagem) {
        if (mensagem == null || mensagem.isBlank()) {
            throw new IllegalArgumentException("A mensagem nao pode estar vazia.");
        }
        return buscarSatelite(sateliteId).enviarDados(mensagem);
    }

    public String iniciarMissao(long sateliteId, long fogueteId) {
        Satelite satelite = buscarSatelite(sateliteId);
        Foguete foguete = buscarFoguete(fogueteId);

        if (foguete.getCargaMaxima() < satelite.getMassa()) {
            throw new IllegalStateException("Massa do satelite ultrapassa capacidade do foguete. Missao cancelada.");
        }

        if (!foguete.lancar()) {
            throw new IllegalStateException("Foguete sem combustivel suficiente. Missao cancelada.");
        }

        satelite.orbita();
        return "Missao iniciada. Foguete: " + foguete.getNome() + ". Satelite: " + satelite.getNome() + ".";
    }

    public StatusMissaoResponse statusMissao() {
        return new StatusMissaoResponse(listarSatelites(), listarFoguetes());
    }

    private Foguete buscarFoguete(long id) {
        Foguete foguete = foguetes.get(id);
        if (foguete == null) {
            throw new NoSuchElementException("Foguete nao encontrado: " + id);
        }
        return foguete;
    }

    private Satelite buscarSatelite(long id) {
        Satelite satelite = satelites.get(id);
        if (satelite == null) {
            throw new NoSuchElementException("Satelite nao encontrado: " + id);
        }
        return satelite;
    }

    private void validarFoguete(Foguete foguete) {
        if (foguete == null) {
            throw new IllegalArgumentException("Foguete nao informado.");
        }
        if (foguete.getNome() == null || foguete.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome do foguete e obrigatorio.");
        }
        if (foguete.getCombustivelRestante() < 0) {
            throw new IllegalArgumentException("Combustivel nao pode ser negativo.");
        }
        if (foguete.getCargaMaxima() <= 0) {
            throw new IllegalArgumentException("Carga maxima deve ser maior que zero.");
        }
    }

    private void validarSatelite(Satelite satelite) {
        if (satelite == null) {
            throw new IllegalArgumentException("Satelite nao informado.");
        }
        if (satelite.getNome() == null || satelite.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome do satelite e obrigatorio.");
        }
        if (satelite.getMassa() <= 0) {
            throw new IllegalArgumentException("Massa deve ser maior que zero.");
        }
        if (satelite.getEnergia() < 0) {
            throw new IllegalArgumentException("Energia nao pode ser negativa.");
        }
    }
}
