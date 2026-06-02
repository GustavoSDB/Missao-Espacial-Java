package br.com.missaoespacial.service;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import br.com.missaoespacial.model.Foguete;
import br.com.missaoespacial.model.Satelite;
import br.com.missaoespacial.repository.FogueteRepository;
import br.com.missaoespacial.repository.SateliteRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CentroControleServiceTest {

    @Mock
    private FogueteRepository fogueteRepository;

    @Mock
    private SateliteRepository sateliteRepository;

    @Test
    void naoAdicionaFogueteComCombustivelAcimaDoMaximo() {
        CentroControleService service = new CentroControleService(fogueteRepository, sateliteRepository);
        Foguete foguete = new Foguete("Artemis", 10001, 1000, "Pronto");

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.adicionarFoguete(foguete))
                .withMessage("Combustivel maximo do foguete e 10000.");
        verifyNoInteractions(fogueteRepository);
    }

    @Test
    void naoAdicionaFogueteComCargaAcimaDaMaxima() {
        CentroControleService service = new CentroControleService(fogueteRepository, sateliteRepository);
        Foguete foguete = new Foguete("Artemis", 1000, 10001, "Pronto");

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.adicionarFoguete(foguete))
                .withMessage("Carga maxima do foguete e 10000.");
        verifyNoInteractions(fogueteRepository);
    }

    @Test
    void deletaFogueteExistente() {
        CentroControleService service = new CentroControleService(fogueteRepository, sateliteRepository);
        Foguete foguete = new Foguete("Artemis", 1000, 1000, "Pronto");
        when(fogueteRepository.findById(7L)).thenReturn(Optional.of(foguete));

        service.deletarFoguete(7L);

        verify(fogueteRepository).delete(foguete);
    }

    @Test
    void deletaSateliteExistente() {
        CentroControleService service = new CentroControleService(fogueteRepository, sateliteRepository);
        Satelite satelite = new Satelite("Aurora", 800, "LEO", 80, "Disponivel");
        when(sateliteRepository.findById(9L)).thenReturn(Optional.of(satelite));

        service.deletarSatelite(9L);

        verify(sateliteRepository).delete(satelite);
    }

    @Test
    void naoAbasteceFogueteEmMissao() {
        CentroControleService service = new CentroControleService(fogueteRepository, sateliteRepository);
        Foguete foguete = new Foguete("Artemis", 1000, 1000, "Em missao");
        when(fogueteRepository.findById(7L)).thenReturn(Optional.of(foguete));

        assertThatIllegalStateException()
                .isThrownBy(() -> service.abastecerFoguete(7L, 100))
                .withMessage("Foguete em missao nao pode ser abastecido.");
        verify(fogueteRepository, never()).save(any());
    }

    @Test
    void naoIniciaMissaoComFogueteQueNaoEstaPronto() {
        CentroControleService service = new CentroControleService(fogueteRepository, sateliteRepository);
        Satelite satelite = new Satelite("Aurora", 800, "LEO", 80, "Disponivel");
        Foguete foguete = new Foguete("Artemis", 1000, 1000, "Em teste");
        when(sateliteRepository.findById(1L)).thenReturn(Optional.of(satelite));
        when(fogueteRepository.findById(2L)).thenReturn(Optional.of(foguete));

        assertThatIllegalStateException()
                .isThrownBy(() -> service.iniciarMissao(1L, 2L))
                .withMessage("Foguete precisa estar Pronto para iniciar missao.");
        verify(fogueteRepository, never()).save(any());
        verify(sateliteRepository, never()).save(any());
    }

    @Test
    void naoIniciaMissaoComSateliteEmOrbita() {
        CentroControleService service = new CentroControleService(fogueteRepository, sateliteRepository);
        Satelite satelite = new Satelite("Aurora", 800, "LEO", 80, "Em orbita");
        Foguete foguete = new Foguete("Artemis", 1000, 1000, "Pronto");
        when(sateliteRepository.findById(1L)).thenReturn(Optional.of(satelite));
        when(fogueteRepository.findById(2L)).thenReturn(Optional.of(foguete));

        assertThatIllegalStateException()
                .isThrownBy(() -> service.iniciarMissao(1L, 2L))
                .withMessage("Satelite em orbita nao pode iniciar nova missao.");
        verify(fogueteRepository, never()).save(any());
        verify(sateliteRepository, never()).save(any());
    }
}
