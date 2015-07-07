/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgEDO;

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
    Derivar derivar = new Derivar();

    /**
     * Creates a new instance of LlamadasBacking
     */
    public CalcularBacking() {
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
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

            respuesta = "";
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

            respuesta += "</br></br>";

            respuesta += formulaDeRecurrencia(arrayEcuacion);
            
            for (int i = 0; i < arrayEcuacion.length; i++) {
                respuesta += "</br>Termino " + (i + 1) + ": " + arrayEcuacion[i];
                respuesta += " Orden: " + (arrayEcuacion[i].length() -1);
            }

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

    private String formulaDeRecurrencia( String[] arrayEcuacion) {      
        String coc = obtenerCociente(arrayEcuacion);
        String den = obtenerDenominador(arrayEcuacion);
        String num = obtenerNumerador(arrayEcuacion);
        
        String respuestaL = "";        
        respuestaL += "<table border='0'>";
        respuestaL += "  <tr>";
        respuestaL += "    <td>";
        respuestaL +=  coc;
        respuestaL += "    </td>";
        respuestaL += "    <td>";
        respuestaL += " = ";
        respuestaL += "    </td>";
        respuestaL += "    <td>";
        respuestaL += "<table border='0'>";
        respuestaL += "  </tr>";
        respuestaL += "  <tr>";
        respuestaL += "    <td>";
        respuestaL +=  num;
        respuestaL += "    </td>";
        respuestaL += "  </tr>";
        respuestaL += "  <tr>";
        respuestaL += "    <td>";
        respuestaL += "     -------------------------------------------------";
        respuestaL += "    </td>";
        respuestaL += "  </tr>";
        respuestaL += "  <tr>";
        respuestaL += "    <td>";
        respuestaL +=  den;
        respuestaL += "    </td>";
        respuestaL += "  </tr>";
        respuestaL += "</table>";
        respuestaL += "    </td>";
        respuestaL += "  </tr>";
        respuestaL += "</table>";

        return respuestaL;
    }

    private String obtenerCociente(String[] arrayEcuacion) {
        int orden = arrayEcuacion[0].length() -1;
        String strOrd = "C<sub>k+" + orden + "</sub> ";
        return strOrd;
    }

    private String obtenerDenominador(String[] arrayEcuacion) {
        int orden = arrayEcuacion[0].length() -1;
        String strOrd = "C<sub>k+" + orden + "</sub> ";
        return strOrd; 
    }

    private String obtenerNumerador(String[] arrayEcuacion) {
        int orden = arrayEcuacion[0].length() -1;
        String strOrd = "C<sub>k+" + orden + "</sub> ";
        return strOrd;
    }

}
