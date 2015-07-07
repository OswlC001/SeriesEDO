/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgEDO;

import org.lsmp.djep.djep.DJep;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;

/**
 *
 * @author guambo
 */
public class Derivar {
        public String Derivar(String fucion) {
        String derivada = "";
        DJep Derivar = new DJep();
        Derivar.addStandardFunctions();
        Derivar.addStandardConstants();//add constes euler
        Derivar.addComplex();//num complejos
        Derivar.setAllowUndeclared(true);
        Derivar.setAllowAssignment(true);//
        Derivar.setImplicitMul(true);
        Derivar.addStandardDiffRules();//añadi reglas para las derivadas
        try {
            Node node = Derivar.parse(fucion);
            Node diff = Derivar.differentiate(node, "x");
            Node sim = Derivar.simplify(diff);//simplifico la diff
            derivada = Derivar.toString(sim);
        } catch (ParseException e) {
            System.out.println("Error al ejecutar la Derivada-->" + e);
        }
        return derivada;
    }
    
}
