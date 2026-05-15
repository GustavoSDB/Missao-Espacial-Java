package br.com.missaoespacial.dto;

import br.com.missaoespacial.model.Foguete;
import br.com.missaoespacial.model.Satelite;
import java.util.List;

public class StatusMissaoResponse {

    private List<Satelite> satelites;
    private List<Foguete> foguetes;

    public StatusMissaoResponse(List<Satelite> satelites, List<Foguete> foguetes) {
        this.satelites = satelites;
        this.foguetes = foguetes;
    }

    public List<Satelite> getSatelites() {
        return satelites;
    }

    public void setSatelites(List<Satelite> satelites) {
        this.satelites = satelites;
    }

    public List<Foguete> getFoguetes() {
        return foguetes;
    }

    public void setFoguetes(List<Foguete> foguetes) {
        this.foguetes = foguetes;
    }
}
