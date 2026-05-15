package br.com.missaoespacial.dto;

public class MissaoRequest {

    private long sateliteId;
    private long fogueteId;

    public long getSateliteId() {
        return sateliteId;
    }

    public void setSateliteId(long sateliteId) {
        this.sateliteId = sateliteId;
    }

    public long getFogueteId() {
        return fogueteId;
    }

    public void setFogueteId(long fogueteId) {
        this.fogueteId = fogueteId;
    }
}
