package prodjogo4;

import cenarios.Cenario;
import cenarios.Plataforma;
import prodjogo4.Robo.Robo;

import java.util.List;

public class MotorColisao {


    public Robo processarColisao (Robo robo, List<Cenario> cenarios) {
        if (!robo.isMorreu()) {
            boolean conseguiu = false;

            for (int i = 0; i < cenarios.size(); i++) {
                Cenario cenario = cenarios.get(i);

                if (robo.getPosx() + robo.getLargura() * 2 / 3 > cenario.getPosx()
                        && robo.getPosx() + robo.getLargura() * 1 / 3 <= cenario.getPosx() + cenario.getTamx()) {

                    if (cenario.solido() && cenario instanceof Plataforma) {
                        if (robo.getPosy() + robo.getAltura() >= cenario.getPosy()
                                && robo.getPosy() + robo.getAltura() <= cenario.getPosy() + cenario.getTamy() - robo.quantoCaiu) {
                            robo.encontrouChao();
                            robo.setPosy(cenario.getPosy() - robo.getAltura() + 7);
                            conseguiu = true;
                        }
                    }

                    if (cenario.causaDano()) {
                        if (robo.getPosy() + robo.getAltura() >= cenario.getPosy()
                                && robo.getPosy() <= cenario.getPosy() + cenario.getTamy()) {
                            robo.morra();
                        }
                    }
                }
            }

            if (!conseguiu) {
                robo.setEstaNoChao(false);
            }
        }
        return robo;
    }
}
