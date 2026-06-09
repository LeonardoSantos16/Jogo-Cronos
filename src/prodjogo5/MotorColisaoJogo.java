package prodjogo5;

import prodjogo4.Robo.Robo;
import prodjogo4.Robo.Tiro;
import prodjogo4.Tile;
import prodjogo5.Inimigos.InimigoDino;
import prodjogo5.Inimigos.InimigoZombie;
import prodjogo5.Inimigos.Inimigos;

import java.util.List;

public class MotorColisaoJogo {

    public void processarFisicaMundo(Robo robo, List<Inimigos> listaInimigos, List<InimigoDino> listaDinos, List<InimigoZombie> listaZumbi, List<Tile> plataformas) {
        if (robo.isMorreu()) {
            return;
        }

        boolean heroiNoChao = false;
        for (Tile c : plataformas) {
            if (robo.getPosx() + robo.getLargura() * 2 / 3 > c.getPosx()
                    && robo.getPosx() + robo.getLargura() * 1 / 3 <= c.getPosx() + c.getTamx()) {
                if (robo.getPosy() + robo.getAltura() >= c.getPosy()
                        && robo.getPosy() + robo.getAltura() <= c.getPosy() + c.getTamy() - robo.quantoCaiu) {
                    robo.encontrouChao();
                    robo.setPosy(c.getPosy() - robo.getAltura() + 7);
                    heroiNoChao = true;
                }
            }

            for (InimigoDino dino : listaDinos) {
                if (dino.getPosx() + dino.getLargura() * 2 / 3 + 25 > c.getPosx()
                        && dino.getPosx() + dino.getLargura() * 1 / 3 + 25 <= c.getPosx() + c.getTamx()) {
                    if (dino.getPosy() + dino.getAltura() >= c.getPosy()
                            && dino.getPosy() + dino.getAltura() <= c.getPosy() + c.getTamy()) {
                        dino.encontrouChao();
                        dino.setPosy(c.getPosy() - dino.getAltura() + 7);
                    }
                }
            }

            for (InimigoZombie zumbi : listaZumbi) {
                if (zumbi.getPosx() + zumbi.getLargura() * 2 / 3 + 25 > c.getPosx()
                        && zumbi.getPosx() + zumbi.getLargura() * 1 / 3 + 25 <= c.getPosx() + c.getTamx()) {
                    if (zumbi.getPosy() + zumbi.getAltura() >= c.getPosy()
                            && zumbi.getPosy() + zumbi.getAltura() <= c.getPosy() + c.getTamy()) {
                        zumbi.encontrouChao();
                    }
                }
            }

            for (Inimigos inimigo : listaInimigos) {
                if (inimigo.getPosx() + inimigo.getTamx() * 2 / 3 > c.getPosx()
                        && inimigo.getPosx() + inimigo.getTamx() * 1 / 3 <= c.getPosx() + c.getTamx()) {
                    if (inimigo.getPosy() + inimigo.getTamy() >= c.getPosy()
                            && inimigo.getPosy() + inimigo.getTamy() <= c.getPosy() + c.getTamy()) {
                        inimigo.encontrouChao();
                    }
                }
            }
        }

        if (!heroiNoChao) {
            robo.setEstaNoChao(false);
        }

        for (Inimigos inimigo : listaInimigos) {
            boolean noChao = false;
            for (Tile c : plataformas) {
                if (inimigo.getPosx() + inimigo.getTamx() * 2 / 3 > c.getPosx()
                        && inimigo.getPosx() + inimigo.getTamx() * 1 / 3 <= c.getPosx() + c.getTamx()) {
                    if (inimigo.getPosy() + inimigo.getTamy() >= c.getPosy()
                            && inimigo.getPosy() + inimigo.getTamy() <= c.getPosy() + c.getTamy()) {
                        noChao = true;
                        break;
                    }
                }
            }
            if (!noChao) inimigo.setEstaNoChao(false);
        }

        for (InimigoDino dino : listaDinos) {
            boolean noChao = false;
            for (Tile c : plataformas) {
                if (dino.getPosx() + dino.getLargura() * 2 / 3 + 25 > c.getPosx()
                        && dino.getPosx() + dino.getLargura() * 1 / 3 + 25 <= c.getPosx() + c.getTamx()) {
                    if (dino.getPosy() + dino.getAltura() >= c.getPosy()
                            && dino.getPosy() + dino.getAltura() <= c.getPosy() + c.getTamy()) {
                        noChao = true;
                        break;
                    }
                }
            }
            if (!noChao) dino.setEstaNoChao(false);
        }

        for (InimigoZombie zumbi : listaZumbi) {
            boolean noChao = false;
            for (Tile c : plataformas) {
                if (zumbi.getPosx() + zumbi.getLargura() * 2 / 3 + 25 > c.getPosx()
                        && zumbi.getPosx() + zumbi.getLargura() * 1 / 3 + 25 <= c.getPosx() + c.getTamx()) {
                    if (zumbi.getPosy() + zumbi.getAltura() >= c.getPosy()
                            && zumbi.getPosy() + zumbi.getAltura() <= c.getPosy() + c.getTamy()) {
                        noChao = true;
                        break;
                    }
                }
            }
            if (!noChao) zumbi.setEstaNoChao(false);
        }
    }

    public void processarDanoETiros(Robo robo, List<Inimigos> listaInimigos, List<InimigoDino> listaDinos, List<InimigoZombie> listaZumbi, List<Tiro> tiros, List<Tile> perigos) {
        if (robo.isMorreu()) {
            return;
        }

        for (InimigoDino dino : listaDinos) {
            if (dino.collideMelle(robo)) {
                robo.morra();
                return;
            }
        }

        for (Tile c2 : perigos) {
            if (robo.getPosx() + robo.getLargura() * 2 / 3 > c2.getPosx()
                    && robo.getPosx() + robo.getLargura() * 1 / 3 <= c2.getPosx() + c2.getTamx()) {
                if (robo.getPosy() + robo.getAltura() >= c2.getPosy()
                        && robo.getPosy() <= c2.getPosy() + c2.getTamy()) {
                    robo.morra();
                    return;
                }
            }
        }

        for (int i = tiros.size() - 1; i >= 0; i--) {
            Tiro tiro = tiros.get(i);

            if (tiro.collideHeroi(robo)) {
                tiros.remove(i);
                robo.morra();
                continue;
            }

            boolean tiroSumiu = false;

            for (InimigoZombie zumbi : listaZumbi) {
                if (tiro.collideZumbi(zumbi)) {
                    if (!zumbi.isMorreu()) {
                        tiros.remove(i);
                        zumbi.morra();
                        tiroSumiu = true;
                    }
                    break;
                }
            }

            if (tiroSumiu) continue;

            for (Inimigos inimigo : listaInimigos) {
                if (tiro.collide(inimigo)) {
                    if (!inimigo.isMorreu()) {
                        tiros.remove(i);
                        inimigo.morra();
                    }
                    break;
                }
            }
        }
    }
}
