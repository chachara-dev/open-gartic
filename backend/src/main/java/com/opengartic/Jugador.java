package com.opengartic;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "jugadores")
public class Jugador {

    @Id
    @Column(name = "id_jugador")
    private UUID idJugador;

    @Column(name = "id_sala")
    private UUID idSala;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "es_host")
    private Boolean esHost;

    @Column(name = "socket_id")
    private String socketId;

    @Column(name = "avatar_url")
    private String avatarUrl;

    public Jugador() {} // Hibernate necesita constructor vacío

    public Jugador(UUID idSala, String nickname, boolean esHost) {
        this.idJugador = UUID.randomUUID();
        this.idSala = idSala;
        this.nickname = nickname;
        this.esHost = esHost;
    }

    public UUID getIdJugador() {
        return idJugador;
    }

    public UUID getIdSala() {
        return idSala;
    }

    public String getNickname() {
        return nickname;
    }

    public Boolean getEsHost() {
        return esHost;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEsHost(Boolean esHost) {
        this.esHost = esHost;
    }
}