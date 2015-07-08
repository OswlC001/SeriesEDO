/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgEDO;

import java.util.List;

/**
 *
 * @author pr20og15ec
 */
public class Elemento {

    private String id;
    private int numerador;
    private int denominador;
    private int signo;
    private List<Elemento> subElementos;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumerador() {
        return numerador;
    }

    public void setNumerador(int numerador) {
        this.numerador = numerador;
    }

    public int getDenominador() {
        return denominador;
    }

    public void setDenominador(int denominador) {
        this.denominador = denominador;
    }

    public int getSigno() {
        return signo;
    }

    public void setSigno(int signo) {
        this.signo = signo;
    }

    public List<Elemento> getSubElementos() {
        return subElementos;
    }

    public void setSubElementos(List<Elemento> subElementos) {
        this.subElementos = subElementos;
    }

    public String impElementos() {
        String elementos = "<table><tr>";
        for (Elemento subElemento : subElementos) {
            elementos += "<td><table><tr><td align='center'>";
            if (subElemento.getNumerador() > 1) {
                elementos += subElemento.getSigno() * subElemento.getNumerador();
            } else {
                if (subElemento.getSigno() == -1) {
                    elementos += "-";
                }
            }
            elementos += subElemento.getId() + "</td></tr><tr><td>-------</td></tr><tr><td align='center'>";
            elementos += subElemento.getDenominador() + "</td></tr></table></td>";
        }
        
        elementos+="</tr></table>";

        return elementos;
    }

}
