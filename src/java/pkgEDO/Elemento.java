/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgEDO;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pr20og15ec
 */
public class Elemento {

    private String id;
    private String var;
    private int numerador;
    private int denominador;
    private int signo;
    private List<Elemento> subElementos;

    public Elemento() {
        this.id = "";
        this.var = "";
        this.numerador = 0;
        this.denominador = 1;
        this.signo = 1;
        this.subElementos = new ArrayList<>();
    }

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

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public List<Elemento> getSubElementos() {
        return subElementos;
    }

    public void setSubElementos(List<Elemento> subElementos) {
        this.subElementos = subElementos;
    }

    public String impElementos() {
        String elementos = "<table><tr>";
        String sign;
        for (Elemento subElemento : subElementos) {
            elementos += "<td><table><tr><td align='center'>";

            if (subElemento.getSigno() == -1) {
                elementos += "-";
            } else {
                elementos += "+";
            }

            if (subElemento.getNumerador() > 1) {
                elementos += subElemento.getNumerador();
            }

            elementos += subElemento.getId() + "</td></tr><tr><td align='center'; style='border-top: solid black 1px'>";
            elementos += subElemento.getDenominador() + "</td></tr></table></td>";
        }

        elementos += "</tr></table>";

        return elementos;
    }

    public Elemento simplificar() {
        int dividir = mcd();
        numerador /= dividir;
        denominador /= dividir;
        return this;
    }

    int mcd() {
        int u = Math.abs(numerador);
        int v = Math.abs(denominador);
        if (v == 0) {
            return u;
        }
        int r;
        while (v != 0) {
            r = u % v;
            u = v;
            v = r;
        }
        return u;
    }
}
