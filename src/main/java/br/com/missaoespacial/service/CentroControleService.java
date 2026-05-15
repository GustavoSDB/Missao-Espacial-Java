package br.com.missaoespacial.service;

import br.com.missaoespacial.dto.StatusMissaoResponse;
import br.com.missaoespacial.model.Foguete;
import br.com.missaoespacial.model.Satelite;
import br.com.missaoespacial.repository.FogueteRepository;
import br.com.missaoespacial.repository.SateliteRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CentroControleService {

    private final FogueteRepository fogueteRepository;
    private final SateliteRepository sateliteRepository;

    public CentroControleService(FogueteRepository fogueteRepository, SateliteRepository sateliteRepository) {
        this.fogueteRepository = fogueteRepository;
        this.sateliteRepository = sateliteRepository;
    }

    @Transactional
    public Foguete adicionarFoguete(Foguete foguete) {
        validarFoguete(foguete);
        return fogueteRepository.save(foguete);
    }

    @Transactional
    public Satelite adicionarSatelite(Satelite satelite) {
        validarSatelite(satelite);
        return sateliteRepository.save(satelite);
    }

    @Transactional(readOnly = true)
    public List<Foguete> listarFoguetes() {
        return fogueteRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Transactional(readOnly = true)
    public List<Satelite> listarSatelites() {
        return sateliteRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Transactional
    public Foguete abastecerFoguete(long fogueteId, float quantidade) {
        Foguete foguete = buscarFoguete(fogueteId);
        foguete.abastecer(quantidade);
        return fogueteRepository.save(foguete);
    }

    @Transactional
    public Satelite ativarPaineis(long sateliteId, String painel, float quantidade) {
        Satelite satelite = buscarSatelite(sateliteId);
        satelite.ativarPaineis(painel, quantidade);
        return sateliteRepository.save(satelite);
    }

    @Transactional
    public String enviarDados(long sateliteId, String mensagem) {
        if (mensagem == null || mensagem.isBlank()) {
            throw new IllegalArgumentException("A mensagem nao pode estar vazia.");
        }
        Satelite satelite = buscarSatelite(sateliteId);
        String resposta = satelite.enviarDados(mensagem);
        sateliteRepository.save(satelite);
        return resposta;
    }

    @Transactional
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
        fogueteRepository.save(foguete);
        sateliteRepository.save(satelite);
        return "Missao iniciada. Foguete: " + foguete.getNome() + ". Satelite: " + satelite.getNome() + ".";
    }

    @Transactional(readOnly = true)
    public StatusMissaoResponse statusMissao() {
        return new StatusMissaoResponse(listarSatelites(), listarFoguetes());
    }

    private Foguete buscarFoguete(long id) {
        return fogueteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Foguete nao encontrado: " + id));
    }

    private Satelite buscarSatelite(long id) {
        return sateliteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Satelite nao encontrado: " + id));
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
