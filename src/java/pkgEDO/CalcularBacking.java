/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgEDO;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Oswaldo Cruz
 */
@ManagedBean
@RequestScoped
public class CalcularBacking {

    private String dato;
    private String respuesta;
    private boolean verCalcauladora = true;
    Derivar derivar = new Derivar();
    List<Elemento> serie;

    /**
     * Creates a new instance of LlamadasBacking
     */
    public CalcularBacking() {
        this.serie = new ArrayList<>();
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public boolean isVerCalcauladora() {
        return verCalcauladora;
    }

    public void setVerCalcauladora(boolean verCalcauladora) {
        this.verCalcauladora = verCalcauladora;
    }

    public void verCalc() {
        this.verCalcauladora = true;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public void leerDato(String num) {
        dato += num;
    }

    public void limpiar() {
        dato = null;
    }

    public void calcular() {
        try {
            //dato = "y'''+y''+y'+y=0";
            verCalcauladora = false;
            respuesta = "<center><hr/><big>Resultado</big><hr/></center><br/>";
            String EDO = dato;
            respuesta += "La EDO es: " + EDO + "</br>";
            String[] arrayEcuacion = EDO.split("[=+-]");
            for (int i = 0; i < arrayEcuacion.length; i++) {
                respuesta += "</br>Termino " + (i + 1) + ": " + arrayEcuacion[i];
            }

            int orden = arrayEcuacion[0].length();

            respuesta += "</br></br> Orden de la Ecuación: " + (orden - 1);

            String der, serie = "C(x)^n", serieStr = "<big>∑<sup>∞</sup></big><sub>n=0</sub> <big>[</big>  C<sub>n</sub>(x)<sup>n</sup> <big>]</big>";
            respuesta += "</br></br>* Serie : " + serieStr;

            String[] arrayEDOComp = new String[100];
            arrayEDOComp[0] = serieStr;

            for (int i = 1; i < orden; i++) {
                der = derivar.Derivar(serie);
                String aux = der;
                serie = aux;
                respuesta += "</br>D" + i + "[f(x)]: " + darFormato(aux, i);
                arrayEDOComp[i] = darFormato(aux, i);
            }

            respuesta += "</br></br>";

            String EDOAux = EDO;
            String y = "y'''''''''''''''''''''''''''''''''''''''''''''''''''''";
            for (int i = orden; i > 0; i--) {
                y = y.substring(0, i);
                EDOAux = EDOAux.replace(y, arrayEDOComp[i - 1]);
            }

            respuesta += EDOAux;

            String EDOAuxBal = EDOAux;
            for (int i = orden; i > 0; i--) {
                String aux = arrayEDOComp[i - 1];
                arrayEDOComp[i - 1] = arrayEDOComp[i - 1].replace("C<sub>n</sub>", "C<sub>n+" + (i - 1) + "</sub>");
                arrayEDOComp[i - 1] = arrayEDOComp[i - 1].replace("(n)", "(n+" + (i - 1) + ")");
                for (int j = i - 1, k = 1; j > 0; j--, k++) {
                    arrayEDOComp[i - 1] = arrayEDOComp[i - 1].replace("(n-" + k + ")", "(n+" + (j - 1) + ")");
                }
                arrayEDOComp[i - 1] = arrayEDOComp[i - 1].replace("n+0", "n");
                arrayEDOComp[i - 1] = arrayEDOComp[i - 1].replace("(n)", "n");
                arrayEDOComp[i - 1] = arrayEDOComp[i - 1].replace("<sub>n=" + (i - 1) + "</sub>", "<sub>n=0</sub>");
                EDOAuxBal = EDOAuxBal.replace(aux, arrayEDOComp[i - 1]);
            }

            respuesta += "</br></br>";

            EDOAuxBal = EDOAuxBal.replace("n", "k");
            respuesta += EDOAuxBal;

            String EDOAuxSinSum = EDOAuxBal;
            EDOAuxSinSum = EDOAuxSinSum.replace("<big>∑<sup>∞</sup></big><sub>k=0</sub> <big>[</big>", "");
            EDOAuxSinSum = EDOAuxSinSum.replace("]", "");
            EDOAuxSinSum = EDOAuxSinSum.replace("x<sup>k<big></sup></big>", "");
            EDOAuxSinSum = EDOAuxSinSum.replace("(x)", "");
            EDOAuxSinSum = EDOAuxSinSum.replace("=0", "");
            EDOAuxSinSum = EDOAuxSinSum.replace("<sup>k</sup>", "");

            respuesta += "</br></br>";

            respuesta += "<big>∑<sup>∞</sup></big><sub>k=0</sub> <big>[</big>" + EDOAuxSinSum + "<big>]</big>(x)<sup>k</sup> = 0";

            respuesta += "</br></br>";

            respuesta += EDOAuxSinSum + " = 0";

            respuesta += "</br></br><center>(Formula de Recurrencia)</br></br>";

            respuesta += formulaDeRecurrencia(arrayEcuacion);

            respuesta += "</center></br></br>";

            respuesta += calcularK(arrayEcuacion);

            respuesta += "</br></br>";

            respuesta += generarSerie();

        } catch (Exception e) {
            respuesta += "Error: " + e;
        }
    }

    private String darFormato(String texto, int i) {
        texto = "<big>∑<sup>∞</sup></big><sub>n=" + i + "</sub> <big>[</big>  " + texto + "<big>]</big>";
        texto = texto.replace("C", "C<sub>n</sub>");
        texto = texto.replace(".0", "");
        texto = texto.replace("^", "<sup>");
        texto = texto.replace("]", "</sup>]");
        texto = texto.replace("*n*", "(n)");
        texto = texto.replace("*", "");
        return texto;
    }

    private String formulaDeRecurrencia(String[] arrayEcuacion) {
        String coc = obtenerCociente(arrayEcuacion);
        String den = obtenerDenominador(arrayEcuacion);
        String num = obtenerNumerador(arrayEcuacion);

        return armarFormula(coc, den, num, "");
    }

    private String armarFormula(String coc, String den, String num, String igual) {
        String respuestaL = "";
        respuestaL += "<table border='0'>";
        respuestaL += "  <tr>     <td>";
        respuestaL += coc;
        respuestaL += "    </td>     <td>  =      </td>     <td>";
        respuestaL += "<table border='0'>";
        respuestaL += "  </tr>";
        respuestaL += "  <tr>     <td>";
        respuestaL += num;
        respuestaL += "    </td>   </tr>   <tr>     <td style='border-top: solid black 1px'>";
        respuestaL += den;
        respuestaL += "    </td>   </tr>";
        respuestaL += "</table>";
        respuestaL += "    </td>";
        respuestaL += "  </tr>";
        respuestaL += "</table>";
        return respuestaL;
    }

    private String llenarLinea(String num, String den) {
        String linea = "";
        int mayor;
        if (num.length() > den.length()) {
            mayor = num.length();
        } else {
            mayor = den.length() * 2;
        }
        for (int i = 0; i < mayor; i++) {
            linea += "-";
        }
        return linea;
    }

    private String obtenerCociente(String[] arrayEcuacion) {
        int orden = arrayEcuacion[0].length() - 1;
        String strR = "C<sub>k+" + orden + "</sub> ";
        return strR;
    }

    private String obtenerDenominador(String[] arrayEcuacion) {
        int orden = arrayEcuacion[0].length() - 1;
        String strR = "";
        for (int i = orden; i > 0; i--) {
            strR += "(k+" + i + ")";
        }
        return strR;
    }

    private String obtenerNumerador(String[] arrayEcuacion) {
        String strR = "";
        int[] signos = obtSignos(arrayEcuacion);
        for (int i = 1; i < arrayEcuacion.length - 1; i++) {
            int orden = arrayEcuacion[i].length() - 1;
            if (signos[i] == 1) {
                strR += " - ";
            } else {
                if (i == 1) {
                    strR += " ";
                } else {
                    strR += " + ";
                }
            }
            if (orden > 0) {
                strR += "C<sub>k+" + orden + "</sub>";
            } else {
                strR += "C<sub>k</sub>";
            }
            for (int j = orden; j > 0; j--) {
                strR += "(k+" + j + ")";
            }
        }

        return strR;
    }

    private int[] obtSignos(String[] arrayEcuacion) {
        int[] signos = new int[100];
        String signo;
        int ind, indAux = 1;
        for (int k = 1, l = 1; k < arrayEcuacion.length - 1; k++, l++) {
            ind = dato.indexOf(arrayEcuacion[k], indAux);
            indAux = ind + 1;
            signo = dato.substring(ind - 1, ind);
            if (signo.equals("+")) {
                signos[l] = 1;
            } else {
                signos[l] = -1;
            }
        }
        return signos;
    }

    private String obtenerCocienteRec(String[] arrayEcuacion, int k) {
        int orden = arrayEcuacion[0].length() - 1;
        String strR = "C<sub>" + (k + orden) + "</sub> ";
        return strR;
    }

    private String obtenerDenominadorRec(String[] arrayEcuacion, int k) {
        int orden = arrayEcuacion[0].length() - 1;
        String strR = "";
        for (int i = orden; i > 0; i--) {
            strR += "(" + (k + i) + ")";
        }
        return strR;
    }

    private int obtenerDenominadorCalc(String[] arrayEcuacion, int k) {
        int orden = arrayEcuacion[0].length() - 1;
        int intR = 1;
        for (int i = orden; i > 0; i--) {
            intR *= (k + i);
        }
        return intR;
    }

    private String obtenerNumeradorRec(String[] arrayEcuacion, int k) {
        String strR = "";
        int[] signos = obtSignos(arrayEcuacion);
        for (int i = 1; i < arrayEcuacion.length - 1; i++) {
            int orden = arrayEcuacion[i].length() - 1;
            if (signos[i] == 1) {
                strR += " - ";
            } else {
                if (i == 1) {
                    strR += " ";
                } else {
                    strR += " + ";
                }
            }
            if (orden > 0) {
                strR += "C<sub>" + (k + orden) + "</sub>";
            } else {
                strR += "C<sub>" + k + "</sub>";
            }
            for (int j = orden; j > 0; j--) {
                strR += "(" + (k + j) + ")";
            }
        }

        return strR;
    }

    private Elemento obtenerElemento(String[] arrayEcuacion, int k, String coc, int denInt) {
        Elemento elemento = new Elemento();
        String idElem;
        List<Elemento> subElementos = new ArrayList<>();
        int[] signos = obtSignos(arrayEcuacion);
        for (int i = 1; i < arrayEcuacion.length - 1; i++) {
            Elemento elementoAux = new Elemento();
            int orden = arrayEcuacion[i].length() - 1;
            if (signos[i] == 1) {
                elementoAux.setSigno(-1);
            } else {
                elementoAux.setSigno(1);
            }
            if (orden > 0) {
                idElem = "C<sub>" + (k + orden) + "</sub>";
            } else {
                idElem = "C<sub>" + k + "</sub>";
            }

            int acumNum = 1;
            for (int j = orden; j > 0; j--) {
                acumNum *= (k + j);
            }

            elementoAux.setId(idElem);

            if (existeElemento(elementoAux)) {
                elementoAux.setSubElementos(cargarElemento(elementoAux).getSubElementos());
            }

            elementoAux.setNumerador(acumNum);
            elementoAux.setDenominador(denInt);

            subElementos.add(elementoAux);
        }

        elemento.setId(coc);
        elemento.setSubElementos(subElementos);
        return elemento;
    }

    private String calcularK(String[] arrayEcuacion) {
        String respuestaL = "Cuando: <br/>";
        String coc, den, num;
        int denInt;
        for (int k = 0; k <= 10; k++) {
            coc = obtenerCocienteRec(arrayEcuacion, k);
            den = obtenerDenominadorRec(arrayEcuacion, k);
            num = obtenerNumeradorRec(arrayEcuacion, k);

            denInt = obtenerDenominadorCalc(arrayEcuacion, k);
            Elemento elemento = obtenerElemento(arrayEcuacion, k, coc, denInt);
            serie.add(elemento);
            respuestaL += "<br/><br/><table><tr><td>k=" + k + " : </td><td>" + armarFormula(coc, den, num, " = ") + "</td>";
            respuestaL += "<td> = </td><td>" + elemento.impElementos() + "</td></tr></table>";
        }

        return respuestaL;
    }

    private boolean existeElemento(Elemento elemento) {
        boolean existe = false;
        for (Elemento elemAux : serie) {
            if (elemAux.getId().trim().equals(elemento.getId().trim())) {
                existe = true;
                break;
            }
        }
        return existe;
    }

    private Elemento cargarElemento(Elemento elemento) {
        Elemento e = new Elemento();
        for (Elemento elemAux : serie) {
            if (elemAux.getId().trim().equals(elemento.getId().trim())) {
                e = elemAux;
                break;
            }
        }
        return e;
    }

    private String cargarSubElementos(Elemento elemento, int sigAcum, int numAcum, int denAcum) {
        String subElementosStr = "";

        for (Elemento subElemento : elemento.getSubElementos()) {
            if (existeElemento(subElemento)) {
                subElementosStr += cargarSubElementos(subElemento, sigAcum * subElemento.getSigno(), numAcum * subElemento.getNumerador(), denAcum * subElemento.getDenominador());
            } else {
                subElementosStr += "<td><table><tr><td align='center'>";
                subElementosStr += subElemento.getSigno() * sigAcum == -1 ? "-" : "+";
                subElementosStr += subElemento.getNumerador() * numAcum > 1 ? subElemento.getNumerador() * numAcum : "";
                subElementosStr += subElemento.getId() + "</td></tr><tr><td align='center'; style='border-top: solid black 1px'>";
                subElementosStr += subElemento.getDenominador() * denAcum + "</td></tr></table></td>";
            }
        }

        return subElementosStr;
    }

    private String generarSerie() {
        String serieStr = "<table><tr><td>Serie: f(x) = </td><td>1</td>";
        int i = 0;
        for (Elemento elemento : serie) {
            serieStr += "<td>+<big style='font-size: 200%;'>[</big></td>" + cargarSubElementos(elemento, 1, 1, 1) + "<td><big style='font-size: 200%;'>]</big>x<sup>" + i++ + "</sup></td>";
        }
        return serieStr + "<td>+...</td></tr></table>";
    }
}
