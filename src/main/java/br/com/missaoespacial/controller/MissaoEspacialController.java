package br.com.missaoespacial.controller;

import br.com.missaoespacial.dto.AbastecimentoRequest;
import br.com.missaoespacial.dto.FogueteRequest;
import br.com.missaoespacial.dto.MensagemRequest;
import br.com.missaoespacial.dto.MissaoRequest;
import br.com.missaoespacial.dto.PainelRequest;
import br.com.missaoespacial.dto.SateliteRequest;
import br.com.missaoespacial.dto.StatusMissaoResponse;
import br.com.missaoespacial.model.Foguete;
import br.com.missaoespacial.model.Satelite;
import br.com.missaoespacial.service.CentroControleService;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MissaoEspacialController {

    private final CentroControleService centroControleService;

    public MissaoEspacialController(CentroControleService centroControleService) {
        this.centroControleService = centroControleService;
    }

    @PostMapping("/foguetes")
    @ResponseStatus(HttpStatus.CREATED)
    public Foguete adicionarFoguete(@RequestBody FogueteRequest request) {
        return centroControleService.adicionarFoguete(request.toModel());
    }

    @GetMapping("/foguetes")
    public List<Foguete> listarFoguetes() {
        return centroControleService.listarFoguetes();
    }

    @PostMapping("/foguetes/{id}/abastecer")
    public Foguete abastecerFoguete(@PathVariable long id, @RequestBody AbastecimentoRequest request) {
        return centroControleService.abastecerFoguete(id, request.getQuantidade());
    }

    @PostMapping("/satelites")
    @ResponseStatus(HttpStatus.CREATED)
    public Satelite adicionarSatelite(@RequestBody SateliteRequest request) {
        return centroControleService.adicionarSatelite(request.toModel());
    }

    @GetMapping("/satelites")
    public List<Satelite> listarSatelites() {
        return centroControleService.listarSatelites();
    }

    @PostMapping("/satelites/{id}/ativar-paineis")
    public Satelite ativarPaineis(@PathVariable long id, @RequestBody PainelRequest request) {
        return centroControleService.ativarPaineis(id, request.getPainel(), request.getQuantidade());
    }

    @PostMapping("/satelites/{id}/enviar-dados")
    public Map<String, String> enviarDados(@PathVariable long id, @RequestBody MensagemRequest request) {
        return Map.of("mensagem", centroControleService.enviarDados(id, request.getMensagem()));
    }

    @PostMapping("/missoes/iniciar")
    public Map<String, String> iniciarMissao(@RequestBody MissaoRequest request) {
        return Map.of("resultado", centroControleService.iniciarMissao(request.getSateliteId(), request.getFogueteId()));
    }

    @GetMapping("/status")
    public StatusMissaoResponse statusMissao() {
        return centroControleService.statusMissao();
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> tratarNaoEncontrado(NoSuchElementException exception) {
        return Map.of("erro", exception.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> tratarRequisicaoInvalida(RuntimeException exception) {
        return Map.of("erro", exception.getMessage());
    }
}
